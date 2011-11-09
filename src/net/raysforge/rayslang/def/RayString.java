package net.raysforge.rayslang.def;

import net.raysforge.rayslang.RayClass;
import net.raysforge.rayslang.RayInstance;
import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RayMethod;

public class RayString implements NativeClass {

	private String stringValue = "";
	
	public RayString() {
	}
	
	public RayString(String s) {
		stringValue = s;
	}
	
	@Override
	public void register(RayLang rayLang) {

		RayClass rc = new RayClass(rayLang, "String");
		rc.nativeClass = this;

		new RayMethod(rc, "print", true);

	}

	@Override
	public NativeClass getNewInstance() {
		return new RayString();
	}

	@Override
	public RayInstance invoke(NativeClass nc, String methodName, RayInstance... parameter) {
		if (methodName.equals("print") && (parameter.length == 0)) {
			System.err.println(((RayString) nc).getStringValue());
		}
		return null;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

}
