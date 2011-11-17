package net.raysforge.rayslang.def;

import java.util.Arrays;

import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayLog;

public class RayInteger implements RayClassInterface {

	private long intValue;

	public RayInteger() {
	}

	public RayInteger(long value) {
		this.setIntValue(value);
	}

	@Override
	public RayClassInterface getNewInstance() {
		return new RayInteger();
	}

	@Override
	public RayClassInterface invoke(String methodName, RayClassInterface... parameter) {

		RayLog.debug(methodName + " " + Arrays.asList(parameter) + " on " + this);

		if (methodName.equals("add!") && (parameter.length == 1)) {
			RayClassInterface p0 = parameter[0];
			RayInteger p0int = (RayInteger) p0;

			setIntValue(getIntValue() + p0int.getIntValue());
		} else if (methodName.equals("add") && (parameter.length == 1)) {
			RayClassInterface p0 = parameter[0];
			RayInteger p0int = (RayInteger) p0;

			return new RayInteger(getIntValue() + p0int.getIntValue());

		} else if (methodName.equals("square!") && (parameter.length == 0)) {
			setIntValue(getIntValue() * getIntValue());

		} else if (methodName.equals("print") && (parameter.length == 0)) {
			System.out.println( getIntValue());

		} else {
			return new RayString(""+intValue).invoke( methodName, parameter);
		}
		return null;
	}
	
	public long getIntValue() {
		return intValue;
	}

	public void setIntValue(long intValue) {
		RayLog.debug( "setIntValue: " + intValue);
		this.intValue = intValue;
	}
	
	@Override
	public String toString() {
		return "RayInteger( "+intValue+ " )";
	}

	@Override
	public String getName() {
		return "Zahl";
	}

}
