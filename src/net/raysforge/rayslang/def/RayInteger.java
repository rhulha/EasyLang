package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.easyswing.Lists;
import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayLang;
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
		
		int pc = 0;
		if( parameter != null)
			pc = parameter.size();
		
		RayInteger p0int = null;
		if( pc >= 1 && parameter.get(0) instanceof RayInteger)
			p0int = (RayInteger) parameter.get(0);
		
		RayLog.debug.log(getName()+"."+methodName + " " + parameter + " on " + this);

		if (methodName.equals("plus!") && p0int != null) {
			setIntValue(intValue + p0int.getIntValue());
		} else if (methodName.equals("und") && parameter.size() == 1) {
			return new RayString(intValue+parameter.get(0).toString());
		} else if (methodName.equals("plus") && p0int != null) {
			return new RayInteger(intValue + p0int.getIntValue());
		} else if (methodName.equals("minus!") && p0int != null) {
			setIntValue(intValue - p0int.getIntValue());
		} else if (methodName.equals("minus") && p0int != null) {
			return new RayInteger(intValue - p0int.getIntValue());
		} else if (methodName.equals("quadrat!") && (pc == 0)) {
			setIntValue(intValue * intValue);
		} else if (methodName.equals("schreibe") && (pc == 0)) {
			RayLang.instance.writeln( intValue);
		} else if (methodName.equals("mal!") && closure == null && p0int != null) {
			setIntValue(intValue * p0int.getIntValue());
		} else if (methodName.equals("mal") && closure == null && p0int != null) {
			return new RayInteger(intValue * p0int.getIntValue());
		} else if (methodName.equals("geteiltDurch!") && closure == null && p0int != null) {
			setIntValue(intValue / p0int.getIntValue());
		} else if (methodName.equals("geteiltDurch") && closure == null && p0int != null) {
			return new RayInteger(intValue / p0int.getIntValue());
		} else if (methodName.equals("mal") && closure != null) {
			List<RayClassInterface> p = Lists.newArrayList();
			for (int i = 0; i < intValue; i++) {
				p.clear();
				p.add(new RayInteger(i));
				closure.invoke(p);
			}
		} else if (methodName.equals("gleicht") && p0int != null && closure != null) {
			if( p0int.getIntValue() == intValue )
			{
				List<RayClassInterface> p = Lists.newArrayList();
				closure.invoke(p);
			}
		} else {
			RayLang.instance.writeln("OMG");
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
		return ""+intValue;
	}

	@Override
	public String getName() {
		return KeyWords.CLASS_INTEGER;
	}

}
