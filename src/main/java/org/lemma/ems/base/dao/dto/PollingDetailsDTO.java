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
	private String timeFormat;
	private long uniqueId;
	private long polledOn;
	private float voltage_bn;
	private float voltage_br;
	private float voltage_rn;
	private float voltage_ry;
	private float voltage_yb;
	private float voltage_yn;
	private float voltage_avg_ll;
	private float voltage_avg_ln;
	private float r_current;
	private float y_current;
	private float b_current;
	private float current_avg;
	private float frequency;
	private float power_factor;
	private float w1;
	private float w2;
	private float w3;
	private float wh;
	private float w_avg;
	private float va1;
	private float va2;
	private float va3;
	private float vah;
	private float va_avg;

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public long getPolledOn() {
		return polledOn;
	}

	public void setPolledOn(long polledOn) {
		this.polledOn = polledOn;
	}

	public float getVoltage_bn() {
		return voltage_bn;
	}

	public void setVoltage_bn(float voltage_bn) {
		this.voltage_bn = voltage_bn;
	}

	public float getVoltage_br() {
		return voltage_br;
	}

	public void setVoltage_br(float voltage_br) {
		this.voltage_br = voltage_br;
	}

	public float getVoltage_rn() {
		return voltage_rn;
	}

	public void setVoltage_rn(float voltage_rn) {
		this.voltage_rn = voltage_rn;
	}

	public float getVoltage_ry() {
		return voltage_ry;
	}

	public void setVoltage_ry(float voltage_ry) {
		this.voltage_ry = voltage_ry;
	}

	public float getVoltage_yb() {
		return voltage_yb;
	}

	public void setVoltage_yb(float voltage_yb) {
		this.voltage_yb = voltage_yb;
	}

	public float getVoltage_yn() {
		return voltage_yn;
	}

	public void setVoltage_yn(float voltage_yn) {
		this.voltage_yn = voltage_yn;
	}

	public float getVoltage_avg_ll() {
		return voltage_avg_ll;
	}

	public void setVoltage_avg_ll(float voltage_avg_ll) {
		this.voltage_avg_ll = voltage_avg_ll;
	}

	public float getVoltage_avg_ln() {
		return voltage_avg_ln;
	}

	public void setVoltage_avg_ln(float voltage_avg_ln) {
		this.voltage_avg_ln = voltage_avg_ln;
	}

	public float getR_current() {
		return r_current;
	}

	public void setR_current(float r_current) {
		this.r_current = r_current;
	}

	public float getY_current() {
		return y_current;
	}

	public void setY_current(float y_current) {
		this.y_current = y_current;
	}

	public float getB_current() {
		return b_current;
	}

	public void setB_current(float b_current) {
		this.b_current = b_current;
	}

	public float getCurrent_avg() {
		return current_avg;
	}

	public void setCurrent_avg(float current_avg) {
		this.current_avg = current_avg;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public float getPower_factor() {
		return power_factor;
	}

	public void setPower_factor(float power_factor) {
		this.power_factor = power_factor;
	}

	public float getW1() {
		return w1;
	}

	public void setW1(float w1) {
		this.w1 = w1;
	}

	public float getW2() {
		return w2;
	}

	public void setW2(float w2) {
		this.w2 = w2;
	}

	public float getW3() {
		return w3;
	}

	public void setW3(float w3) {
		this.w3 = w3;
	}

	public float getWh() {
		return wh;
	}

	public void setWh(float wh) {
		this.wh = wh;
	}

	public float getW_avg() {
		return w_avg;
	}

	public void setW_avg(float w_avg) {
		this.w_avg = w_avg;
	}

	public float getVa1() {
		return va1;
	}

	public void setVa1(float va1) {
		this.va1 = va1;
	}

	public float getVa2() {
		return va2;
	}

	public void setVa2(float va2) {
		this.va2 = va2;
	}

	public float getVa3() {
		return va3;
	}

	public void setVa3(float va3) {
		this.va3 = va3;
	}

	public float getVah() {
		return vah;
	}

	public void setVah(float vah) {
		this.vah = vah;
	}

	public float getVa_avg() {
		return va_avg;
	}

	public void setVa_avg(float va_avg) {
		this.va_avg = va_avg;
	}

}
