package org.lemma.infra.config.listener;

import org.lemma.ems.base.core.ModbusCoupler;

import com.ghgande.j2mod.modbus.net.ModbusSerialListener;
import com.ghgande.j2mod.modbus.procimg.SimpleDigitalIn;
import com.ghgande.j2mod.modbus.procimg.SimpleDigitalOut;
import com.ghgande.j2mod.modbus.procimg.SimpleInputRegister;
import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;

public class MainSlave {

	public static void main(String[] args) throws Exception {

		try {

			SimpleProcessImage spi = new SimpleProcessImage();
			spi.addDigitalOut(new SimpleDigitalOut(true));
			spi.addDigitalOut(new SimpleDigitalOut(false));
			spi.addDigitalIn(new SimpleDigitalIn(false));
			spi.addDigitalIn(new SimpleDigitalIn(true));
			spi.addDigitalIn(new SimpleDigitalIn(false));
			spi.addDigitalIn(new SimpleDigitalIn(true));
			spi.addRegister(new SimpleRegister(251));
			spi.addInputRegister(new SimpleInputRegister(45));

			// 2. Create the coupler and set the slave identity
			ModbusCoupler.getReference().setProcessImage(spi);
			ModbusCoupler.getReference().setMaster(false);
			ModbusCoupler.getReference().setUnitID(2);

			SerialParameters params = new SerialParameters();
			params.setPortName("COM");
			params.setBaudRate(9600);
			params.setDatabits(8);
			params.setParity("None");
			params.setStopbits(1);
			params.setEncoding("ascii");
			params.setEcho(false);

			ModbusSerialListener listener = new ModbusSerialListener(params);
			listener.setListening(true);
			System.out.println(listener.getError());
			Thread thread = new Thread(listener);
			thread.setDaemon(true);
			thread.start();

			Thread.currentThread().join();

			System.out.println("Listening on COM");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
