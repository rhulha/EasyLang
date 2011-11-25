package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayLog;
import net.raysforge.rayslang.RayMethod;

public class RayInteger implements RayClassInterface {

	private long intValue;

	public RayInteger() {
	}

	public RayInteger(long value) {
		this.setIntValue(value);
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayInteger();
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure, List<RayClassInterface> parameter) {

		RayLog.debug.log(getName()+"."+methodName + " " + parameter + " on " + this);

		if (methodName.equals("plus!") && (parameter.size() == 1)) {
			RayClassInterface p0 = parameter.get(0);
			RayInteger p0int = (RayInteger) p0;

			setIntValue(getIntValue() + p0int.getIntValue());
		} else if (methodName.equals("plus") && (parameter.size() == 1)) {
			RayClassInterface p0 = parameter.get(0);
			RayInteger p0int = (RayInteger) p0;
			return new RayInteger(getIntValue() + p0int.getIntValue());
		} else if (methodName.equals("quadrat!") && (parameter.size() == 0)) {
			setIntValue(getIntValue() * getIntValue());
		} else if (methodName.equals("schreibe") && (parameter.size() == 0)) {
			System.out.println( getIntValue());
		} else {
			System.out.println("OMG");
			return new RayString(""+intValue).invoke( methodName, null, parameter);
		}
		return this;
	}
	
	public long getIntValue() {
		return intValue;
	}

	public void setIntValue(long intValue) {
		RayLog.debug.log( getName()+".setIntValue: " + intValue);
		this.intValue = intValue;
	}
	
	@Override
	public String toString() {
		return getName()+"( " +intValue+ " )";
	}

	@Override
	public String getName() {
		return KeyWords.CLASS_INTEGER;
	}

}
