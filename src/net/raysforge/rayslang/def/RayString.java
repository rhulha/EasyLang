package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayMethod;

public class RayString implements RayClassInterface {

	private String stringValue = "";

	public RayString() {
	}

	public RayString(String s) {
		stringValue = s;
	}

	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayString();
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String getName() {
		return KeyWords.CLASS_STRING;
	}

	@Override
	public RayClassInterface invoke(String methodName, RayClassInterface... parameter) {
		if (methodName.equals("schreibe") && (parameter.length == 0)) {
			System.err.println(stringValue);
		}
		if (methodName.equals("spalte") && (parameter.length == 1)) {
			RayClassInterface p0 = parameter[0];
			String[] split = stringValue.split(p0.toString());
			RayArray ra = new RayArray(getName());
			for (String string : split) {
				ra.list.add(new RayString(string));
			}
			return ra;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return stringValue;
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure) {
		// TODO Auto-generated method stub
		return null;
	}

}
