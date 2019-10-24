package org.lemma.ems.base.core;

import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.procimg.InputRegister;

/**
 * @author RTS Sathish Kumar
 *
 */
public class EMSDeviceConnectionManager extends Core {

	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(EMSDeviceConnectionManager.class);

	/**
	 * 
	 */
	public EMSDeviceConnectionManager() {
		// NOOP
	}

	/**
	 * @param connection
	 * @param device
	 * @return
	 * @throws ModbusException
	 */
	public ResponseHandler executeTransaction(SerialConnection connection, ExtendedSerialParameter device)
			throws Exception {

		if (connection == null || device == null)
			throw new ModbusException("Invalid SerialialConnection or SerialParameter");

		return execute(connection, device);
	}

	/**
	 * @param connection
	 * @param method
	 * @param reference
	 * @param count
	 * @param unitId
	 * @param uniqueId
	 * @param retries
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ModbusException
	 */
	private ResponseHandler execute(SerialConnection connection, ExtendedSerialParameter device)
			throws InstantiationException, IllegalAccessException {

		ResponseHandler handler = device.getResponseHandler().newInstance();

		// Every transaction is mutually locked, needless to create additional MUTEX
		ModbusSerialTransaction tran = new ModbusSerialTransaction(connection);
		// Atleast minimum gap between every request is required even in same connection
		tran.setTransDelayMS(GAP_BETWEEN_REQUEST);
		// Callback handlers events
		handler.pollStart(device);

		for (ExtendedDeviceMemoryDTO mapping : device.getDeviceMemoryList()) {

			ModbusRequest request = createModbusRequest(device.getMethod(), mapping.getReference(), mapping.getCount(),
					device.getUnitId());
			tran.setRequest(request);
			tran.setRetries(device.getRetries());
			logger.trace("Ref : {}, Count : {}, Function code {}, UniqueId {}", mapping.getReference(),
					mapping.getCount(), request.getFunctionCode(), device.getUniqueId());

			try {
				tran.execute();
				ModbusResponse response = tran.getResponse();

				logger.trace("UniqueId {} : device response : {}  in {}ms", device.getUniqueId(),
						response.getHexMessage(), tran.getTransDelayMS());

				InputRegister[] responseRegisters = getResponseRegisters(response);

				// Handler responses
				handler.handleResponse(responseRegisters);
			} catch (Exception e) {
				logger.error(" Pooling failed for device {}  with exception {}", device.getUniqueId(), e);
				// Set exception object for referencce
				handler.setException(e);
			}
		}

		tran = null;

		// Notifify polling completed
		handler.pollCompleted();

		return handler;
	}

}
