package org.lemma.ems.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.lemma.ems.base.cache.CacheEntryConstants;
import org.lemma.ems.base.cache.CacheUtil;
import org.lemma.ems.base.cache.Caches;
import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.DeviceMemoryDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.DeviceMemoryDTO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.base.mqueue.subscriber.DeviceSettingsListener;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.lemma.ems.ui.model.DeviceFormDetails;
import org.lemma.ems.ui.model.DeviceStateRequest;
import org.lemma.ems.ui.model.DeviceStateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository("deviceManagementService")
public class DeviceManagementService {

	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementService.class);

	@Autowired
	private DeviceDetailsDAO deviceDetailsDAO;

	@Autowired
	private ReloadableResourceBundleMessageSource msgSource;
	
	@Autowired
	private Sender sender;
	
	@Autowired
	private CacheUtil cacheUtil;
	/**
	 * @return
	 */
	public ModelAndView showDeviceReportsPage() {
		ModelAndView modelAndView = new ModelAndView("devicelist");

		// List of devices not DELETED and Type EMS
		List<DeviceDetailsDTO> deviceList = deviceDetailsDAO.fetchDeviceDetails(DeviceDetailsDAO.RETRIEVE_EMS_DEVICES,
				new Object[] { DeviceDetailsDAO.Status.DELETED.getStatus(), DeviceDetailsDAO.Type.EMS.getType() });

		// Holds existing device details
		modelAndView.addObject("existingDeviceList", deviceList);
		// Form values like parity list and so on
		modelAndView.addObject("formDetails", new DeviceFormDetails());

		// Holds new device information
		modelAndView.addObject("deviceDetailsForm", new DeviceDetailsForm());

		return modelAndView;
	}

	/**
	 * @param form
	 * @return
	 */
	public ModelAndView addNewDevice(DeviceDetailsForm form) {
		ModelAndView modelAndView = new ModelAndView("redirect:/ems/devices/show");
		modelAndView.addObject("msg", msgSource.getMessage("device.added", null, Locale.getDefault()));

		/**
		 * On successfull insertion 1. publish reload event 2. Redirect back to show
		 * devices page with success
		 */
		long timeStamp = System.currentTimeMillis();
		form.setCreatedTimeStamp(timeStamp);
		form.setModifiedTimeStamp(timeStamp);
		form.setStatus(form.isEnabled() ? DeviceDetailsDAO.Status.ACTIVE.getStatus()
				: DeviceDetailsDAO.Status.DISABLED.getStatus());
		form.setType(DeviceDetailsDAO.Type.EMS.getType());

		for (DeviceMemoryDTO memory : form.getMemoryMappings()) {
			memory.setCreatedTimeStamp(timeStamp);
			memory.setStatus(DeviceMemoryDAO.Status.ACTIVE.getStatus());
		}

		DeviceDetailsDTO dto = DeviceMapper.mapForm2Dto(form);

		try {
			long insertDeviceDetails = deviceDetailsDAO.insertDeviceDetails(dto);
			
			//Notifiy listeners to pick up new devices
			sender.publishEvent(DeviceSettingsListener.Topics.LOAD_DEVICES.getTopic(),
					DeviceSettingsListener.Topics.LOAD_DEVICES.getTopic());
			
		} catch (Exception e) {
			logger.error("Failed to insert new device {}", e);
			modelAndView.addObject("msg", msgSource.getMessage("device.added.error", null, Locale.getDefault()));
		}

		return modelAndView;
	}
	
	/**
	 * @param form
	 * @return
	 */
	public ModelAndView updateExistingDevice(DeviceDetailsForm form) {
		
		ModelAndView modelAndView = new ModelAndView("redirect:/ems/devices/show");
		modelAndView.addObject("msg", msgSource.getMessage("device.updated", null, Locale.getDefault()));
		modelAndView.addObject("accordionIndex", form.getAccordionIndex());

		/**
		 * On successfull insertion 1. publish reload event 2. Redirect back to show
		 * devices page with success
		 */
		long timeStamp = System.currentTimeMillis();
		form.setModifiedTimeStamp(timeStamp);
		
		form.setStatus(form.isEnabled() ? DeviceDetailsDAO.Status.ACTIVE.getStatus()
				: DeviceDetailsDAO.Status.DISABLED.getStatus());
		form.setStatus(form.isDeleted() ? DeviceDetailsDAO.Status.DELETED.getStatus() : form.getStatus());

		DeviceDetailsDTO dto = DeviceMapper.mapForm2Dto(form);

		try {
			long count = deviceDetailsDAO.updateDeviceDetails(dto);
			
			logger.debug(" DeviceDetails updated count {} for {} {}", count, form.getDeviceId(),form.getDeviceName());
			
			//Notifiy listeners to pick up new devices
			sender.publishEvent(DeviceSettingsListener.Topics.LOAD_DEVICES.getTopic(),
					DeviceSettingsListener.Topics.LOAD_DEVICES.getTopic());
			
		} catch (Exception e) {
			logger.error("Failed to insert new device {}", e);
			modelAndView.addObject("msg", msgSource.getMessage("device.updated.error", null, Locale.getDefault()));
		}

		return modelAndView;
	}

	
	/**
	 * @param request
	 * @returns Given devices currrent state from Cache is updated using Live Poller
	 */
	public DeviceStateResponse retrieveDeviceStates(DeviceStateRequest request) {
		DeviceStateResponse response = new DeviceStateResponse();
		
		if(request.getDeviceIds()  !=  null) {
			for(long deviceId : request.getDeviceIds()) {
				
				List<PollingDetailsDTO> deviceStateFromCache = retrieveDeviceStateFromCache(deviceId);
				//When firstRecord is true, set only first record in response
				//otherwise whole details available in cahce will be responded
				if(request.isFirstRecord() && deviceStateFromCache != null && !deviceStateFromCache.isEmpty()) {
					response.getDeviceStates().put(deviceId, Arrays.asList(deviceStateFromCache.get(0)));
				} else {
					response.getDeviceStates().put(deviceId, deviceStateFromCache);
				}
			}
		}
		
		return response;
	}
	
	
	private List<PollingDetailsDTO> retrieveDeviceStateFromCache(long deviceId) {
		List<PollingDetailsDTO> dto = new ArrayList<>();  
		
		Map<Long, CircularFifoQueue<PollingDetailsDTO>> cacheEntry = (Map<Long, CircularFifoQueue<PollingDetailsDTO>>)
				cacheUtil.getCacheEntry(Caches.DEVICECACHE, CacheEntryConstants.DeviceEntryConstants.DEVICE_STATES.getName());
		
		CircularFifoQueue<PollingDetailsDTO> circularFifoQueue = cacheEntry.get(deviceId);
		
		if(circularFifoQueue !=  null) {
			circularFifoQueue.forEach(object -> dto.add(object));
		}
		
		return  dto;
	}
	
}
