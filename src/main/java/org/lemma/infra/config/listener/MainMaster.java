package org.lemma.infra.config.listener;

import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;

public class MainMaster {

	public static void main(String[] args) throws Exception {
		
		SerialParameters params = new SerialParameters();
		params.setPortName("COM");
		params.setBaudRate(9600);
		params.setDatabits(8);
		params.setParity("None");
		params.setStopbits(1);
		params.setEncoding("ascii");
		params.setEcho(false);
		
		
		SerialConnection connection = new SerialConnection(params);
		connection.open();
		
		ModbusRequest request = new ReadMultipleRegistersRequest(250, 1);
		request.setUnitID(2);
		ModbusSerialTransaction tran = new ModbusSerialTransaction(connection);
		tran.setRequest(request);
		tran.execute();
		ModbusResponse response = tran.getResponse();
		
		System.out.println(response.getHexMessage());

		connection.close();
	}

}
