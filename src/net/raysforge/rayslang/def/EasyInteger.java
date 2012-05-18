package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.commons.Generics;
import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyLang;
import net.raysforge.rayslang.EasyLog;
import net.raysforge.rayslang.EasyMethod;

public class EasyInteger implements EasyClassInterface {

	private long intValue;

	public EasyInteger() {
	}

	public EasyInteger(long value) {
		this.setIntValue(value);
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyInteger();
	}

	@Override
	public EasyClassInterface invoke(String methodName, EasyMethod closure, List<EasyClassInterface> parameter) {
		
		int pc = 0;
		if( parameter != null)
			pc = parameter.size();
		
		EasyInteger p0int = null;
		if( pc >= 1 && parameter.get(0) instanceof EasyInteger)
			p0int = (EasyInteger) parameter.get(0);
		
		EasyLog.debug.log(getName()+"."+methodName + " " + parameter + " on " + this);

		if (methodName.equals("plus!") && p0int != null) {
			setIntValue(intValue + p0int.getIntValue());
		} else if (methodName.equals("und") && parameter.size() == 1) {
			return new EasyString(intValue+parameter.get(0).toString());
		} else if (methodName.equals("plus") && p0int != null) {
			return new EasyInteger(intValue + p0int.getIntValue());
		} else if (methodName.equals("minus!") && p0int != null) {
			setIntValue(intValue - p0int.getIntValue());
		} else if (methodName.equals("minus") && p0int != null) {
			return new EasyInteger(intValue - p0int.getIntValue());
		} else if (methodName.equals("quadrat!") && (pc == 0)) {
			setIntValue(intValue * intValue);
		} else if (methodName.equals("schreibe") && (pc == 0)) {
			EasyLang.instance.writeln( intValue);
		} else if (methodName.equals("mal!") && closure == null && p0int != null) {
			setIntValue(intValue * p0int.getIntValue());
		} else if (methodName.equals("mal") && closure == null && p0int != null) {
			return new EasyInteger(intValue * p0int.getIntValue());
		} else if (methodName.equals("geteiltDurch!") && closure == null && p0int != null) {
			setIntValue(intValue / p0int.getIntValue());
		} else if (methodName.equals("geteiltDurch") && closure == null && p0int != null) {
			return new EasyInteger(intValue / p0int.getIntValue());
		} else if (methodName.equals("mal") && closure != null) {
			List<EasyClassInterface> p = Generics.newArrayList();
			for (int i = 0; i < intValue; i++) {
				p.clear();
				p.add(new EasyInteger(i));
				closure.invoke(p);
			}
		} else if (methodName.equals("gleicht") && p0int != null && closure != null) {
			if( p0int.getIntValue() == intValue )
			{
				List<EasyClassInterface> p = Generics.newArrayList();
				closure.invoke(p);
			}
		} else {
			EasyLang.instance.writeln("OMG");
			return new EasyString(""+intValue).invoke( methodName, null, parameter);
		}
		return this;
	}
	
	public long getIntValue() {
		return intValue;
	}

	public void setIntValue(long intValue) {
		EasyLog.debug.log( getName()+".setIntValue: " + intValue);
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
