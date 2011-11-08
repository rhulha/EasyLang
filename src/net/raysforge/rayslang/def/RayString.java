package net.raysforge.rayslang.def;

import net.raysforge.rayslang.RayClass;
import net.raysforge.rayslang.RayInstance;
import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RayMethod;

public class RayString implements NativeClass {

	String value = "";

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
			System.err.println(((RayString) nc).value);
		}
		return null;
	}

}
