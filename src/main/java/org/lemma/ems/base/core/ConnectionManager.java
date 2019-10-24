package org.lemma.ems.base.core;

import java.io.IOException;

import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.util.EMSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;

/**
 * @author RTS Sathish Kumar
 *
 */
public abstract class ConnectionManager extends Core {
	
	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

	/**
	 * @param parameters
	 * @return
	 */
	public static SerialConnection getConnection(SerialParameters parameters) {
		SerialConnection connection = new SerialConnection(parameters);
		connection.setTimeout(TIMEOUT);
		try {
			connection.open();
		} catch (IOException e) {
			logger.error("Error", e);
		}
		return connection;
	}

	/**
	 * @param connection
	 * @param device
	 * @return
	 * @throws ModbusException
	 */
	public static InputRegister[] executeTransaction(SerialConnection connection, ExtendedSerialParameter device)
			throws ModbusException {

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
	private static InputRegister[] execute(SerialConnection connection, String method, int reference, int count,
			int unitId, long uniqueId, int retries) throws ModbusException {
		// Atleast minimum gap between every request is required even in same connection
		try {
			Thread.sleep(GAP_BETWEEN_REQUEST);
		} catch (Exception e) {
		}

		ModbusSerialTransaction tran = new ModbusSerialTransaction(connection);
		ModbusRequest request = EMSUtility.getRequest(method, reference, count);
		logger.trace("Hex request : {}, Ref : {}, Count : {}, Function code {}, UniqueId {}", request.getHexMessage(),
				reference, count, request.getFunctionCode(), uniqueId);
		request.setUnitID(unitId);
		tran.setRequest(request);
		tran.setRetries(retries);
		tran.execute();

		ModbusResponse response = tran.getResponse();
		logger.trace("UniqueId {} : Dashboard device response : {} ", uniqueId, response.getHexMessage());
		tran = null;
		return getResponseRegisters(response);
	}

	/**
	 * @param response
	 * @return
	 */
	public static InputRegister[] getResponseRegisters(ModbusResponse response) {
		if (response instanceof ReadMultipleRegistersResponse) {
			return ((ReadMultipleRegistersResponse) response).getRegisters();
		} else {
			return ((ReadInputRegistersResponse) response).getRegisters();
		}
	}
}
