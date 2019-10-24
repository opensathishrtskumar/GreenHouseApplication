package org.lemma.ems.base.core;

import org.lemma.ems.base.core.constants.Core;
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
		//NOOP
	}
	
	/**
	 * @param connection
	 * @param device
	 * @return
	 * @throws ModbusException
	 */
	public InputRegister[] executeTransaction(SerialConnection connection, ExtendedSerialParameter device)
			throws ModbusException {
		
		if(connection == null || device == null)
			throw new ModbusException("Invalid SerialialConnection or SerialParameter");
		
		return execute(connection, device.getMethod(), device.getReference(), device.getCount(), device.getUnitId(),
				device.getUniqueId(), device.getRetries());
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
	 * @throws ModbusException
	 */
	private InputRegister[] execute(SerialConnection connection, String method, int reference, int count, int unitId,
			long uniqueId, int retries) throws ModbusException {

		ModbusSerialTransaction tran = new ModbusSerialTransaction(connection);
		// Atleast minimum gap between every request is required even in same connection
		tran.setTransDelayMS(GAP_BETWEEN_REQUEST);

		ModbusRequest request = createModbusRequest(method, reference, count,unitId);
		logger.trace("Ref : {}, Count : {}, Function code {}, UniqueId {}", reference, count, request.getFunctionCode(),
				uniqueId);
		tran.setRequest(request);
		tran.setRetries(retries);
		tran.execute();

		ModbusResponse response = tran.getResponse();

		logger.trace("UniqueId {} : device response : {}  in {}ms", uniqueId, response.getHexMessage(),
				tran.getTransDelayMS());
		tran = null;
		return getResponseRegisters(response);
	}
	
}
