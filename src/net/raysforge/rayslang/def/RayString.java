package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.easyswing.Lists;
import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayMethod;
import net.raysforge.rayslang.RayUtils;

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
	public RayClassInterface invoke(String methodName, RayMethod closure, List<RayClassInterface> parameter) {
		if (methodName.equals("schreibe") && (parameter.size() == 0)) {
			System.out.println(stringValue);
		} else if (methodName.equals("spalte") && (parameter.size() == 1)) {
			RayClassInterface p0 = parameter.get(0);
			String[] split = stringValue.split(p0.toString());
			RayArray ra = new RayArray(getName()+"[]");
			for (String string : split) {
				ra.list.add(new RayString(string));
			}
			return ra;
		} else if (methodName.equals("alsZahl") ) {
			return new RayInteger(Long.parseLong(stringValue));
		} else if ((methodName.equals("istGleich") || methodName.equals("gleicht")) && closure != null && (parameter.size() == 1)) {
			RayClassInterface p0 = parameter.get(0);
			if( stringValue.equals(p0.toString()))
			{
				List<RayClassInterface> p = Lists.newArrayList();
				closure.invoke( closure.getParentClass(), p);
			}
		} else {
			RayUtils.runtimeExcp("method not found: " + methodName);
		}
		return null;
	}

	@Override
	public String toString() {
		return stringValue;
	}

}
