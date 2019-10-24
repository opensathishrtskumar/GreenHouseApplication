package org.lemma.ems.base.dao.dto;

import java.io.Serializable;

/**
 * @author RTS Sathish Kumar
 *
 */
public class PollingDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7353069892688430105L;
	private long deviceUniqueId;
	private long polledOn;
	// FIXME: check float and double compatibility, since we convert registers to
	// fload {@link ModbusUtil}
	private double voltage_bn;
	private double voltage_br;
	private double voltage_rn;
	private double voltage_ry;
	private double voltage_yb;
	private double voltage_yn;
	private double voltage_avg_ll;
	private double voltage_avg_ln;
	private double r_current;
	private double y_current;
	private double b_current;
	private double current_avg;
	private double frequency;
	private double power_factor;
	private double w1;
	private double w2;
	private double w3;
	private double wh;
	private double w_avg;
	private double va1;
	private double va2;
	private double va3;
	private double vah;
	private double va_avg;

	public long getDeviceUniqueId() {
		return deviceUniqueId;
	}

	public void setDeviceUniqueId(long deviceUniqueId) {
		this.deviceUniqueId = deviceUniqueId;
	}

	public long getPolledOn() {
		return polledOn;
	}

	public void setPolledOn(long polledOn) {
		this.polledOn = polledOn;
	}

	public double getVoltage_bn() {
		return voltage_bn;
	}

	public void setVoltage_bn(double voltage_bn) {
		this.voltage_bn = voltage_bn;
	}

	public double getVoltage_br() {
		return voltage_br;
	}

	public void setVoltage_br(double voltage_br) {
		this.voltage_br = voltage_br;
	}

	public double getVoltage_rn() {
		return voltage_rn;
	}

	public void setVoltage_rn(double voltage_rn) {
		this.voltage_rn = voltage_rn;
	}

	public double getVoltage_ry() {
		return voltage_ry;
	}

	public void setVoltage_ry(double voltage_ry) {
		this.voltage_ry = voltage_ry;
	}

	public double getVoltage_yb() {
		return voltage_yb;
	}

	public void setVoltage_yb(double voltage_yb) {
		this.voltage_yb = voltage_yb;
	}

	public double getVoltage_yn() {
		return voltage_yn;
	}

	public void setVoltage_yn(double voltage_yn) {
		this.voltage_yn = voltage_yn;
	}

	public double getVoltage_avg_ll() {
		return voltage_avg_ll;
	}

	public void setVoltage_avg_ll(double voltage_avg_ll) {
		this.voltage_avg_ll = voltage_avg_ll;
	}

	public double getVoltage_avg_ln() {
		return voltage_avg_ln;
	}

	public void setVoltage_avg_ln(double voltage_avg_ln) {
		this.voltage_avg_ln = voltage_avg_ln;
	}

	public double getR_current() {
		return r_current;
	}

	public void setR_current(double r_current) {
		this.r_current = r_current;
	}

	public double getY_current() {
		return y_current;
	}

	public void setY_current(double y_current) {
		this.y_current = y_current;
	}

	public double getB_current() {
		return b_current;
	}

	public void setB_current(double b_current) {
		this.b_current = b_current;
	}

	public double getCurrent_avg() {
		return current_avg;
	}

	public void setCurrent_avg(double current_avg) {
		this.current_avg = current_avg;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public double getPower_factor() {
		return power_factor;
	}

	public void setPower_factor(double power_factor) {
		this.power_factor = power_factor;
	}

	public double getW1() {
		return w1;
	}

	public void setW1(double w1) {
		this.w1 = w1;
	}

	public double getW2() {
		return w2;
	}

	public void setW2(double w2) {
		this.w2 = w2;
	}

	public double getW3() {
		return w3;
	}

	public void setW3(double w3) {
		this.w3 = w3;
	}

	public double getWh() {
		return wh;
	}

	public void setWh(double wh) {
		this.wh = wh;
	}

	public double getW_avg() {
		return w_avg;
	}

	public void setW_avg(double w_avg) {
		this.w_avg = w_avg;
	}

	public double getVa1() {
		return va1;
	}

	public void setVa1(double va1) {
		this.va1 = va1;
	}

	public double getVa2() {
		return va2;
	}

	public void setVa2(double va2) {
		this.va2 = va2;
	}

	public double getVa3() {
		return va3;
	}

	public void setVa3(double va3) {
		this.va3 = va3;
	}

	public double getVah() {
		return vah;
	}

	public void setVah(double vah) {
		this.vah = vah;
	}

	public double getVa_avg() {
		return va_avg;
	}

	public void setVa_avg(double va_avg) {
		this.va_avg = va_avg;
	}

}
