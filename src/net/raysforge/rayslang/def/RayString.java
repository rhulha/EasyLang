package net.raysforge.rayslang.def;

import net.raysforge.rayslang.RayClassInterface;

public class RayString implements RayClassInterface {

	private String stringValue = "";

	public RayString() {
	}

	public RayString(String s) {
		stringValue = s;
	}

	public RayClassInterface getNewInstance() {
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
		return "Zeichen";
	}

	@Override
	public RayClassInterface invoke(String methodName, RayClassInterface... parameter) {
		if (methodName.equals("print") && (parameter.length == 0)) {
			System.err.println(stringValue);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "RayString( "+stringValue+ " )";
	}

}
