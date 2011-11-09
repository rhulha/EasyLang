package net.raysforge.rayslang.def;

import java.util.Arrays;

import net.raysforge.rayslang.RayClass;
import net.raysforge.rayslang.RayInstance;
import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RayLog;
import net.raysforge.rayslang.RayMethod;

public class RayInteger extends RayFloat implements NativeClass {

	private long intValue;

	public RayInteger() {
	}

	public RayInteger(long value) {
		this.setIntValue(value);
	}

	@Override
	public void register(RayLang rayLang) {

		RayClass rc = new RayClass(rayLang, "Integer");
		rc.nativeClass = this;

		new RayMethod(rc, "add!", true);
		new RayMethod(rc, "add", true);
		new RayMethod(rc, "square!", true);
		new RayMethod(rc, "print", true);
	}

	@Override
	public NativeClass getNewInstance() {
		return new RayInteger();
	}

	@Override
	public RayInstance invoke(NativeClass nc, String methodName, RayInstance... parameter) {
		RayInteger rint = (RayInteger) nc;

		RayLog.info(methodName + " " + Arrays.asList(parameter) + " on " + nc);

		if (methodName.equals("add!") && (parameter.length == 1)) {
			RayInstance p0 = parameter[0];
			RayInteger p0int = (RayInteger) p0.nativeClass;

			rint.setIntValue(rint.getIntValue() + p0int.getIntValue());
		} else if (methodName.equals("add") && (parameter.length == 1)) {
			RayInstance p0 = parameter[0];
			RayInteger p0int = (RayInteger) p0.nativeClass;

			return new RayInstance(new RayInteger(rint.getIntValue() + p0int.getIntValue()));

		} else if (methodName.equals("square!") && (parameter.length == 0)) {
			rint.setIntValue(rint.getIntValue() * rint.getIntValue());

		} else if (methodName.equals("print") && (parameter.length == 0)) {
			System.out.println( rint.getIntValue());

		} else {
			return super.invoke(nc, methodName, parameter);
		}
		return null;
	}

	public long getIntValue() {
		return intValue;
	}

	public void setIntValue(long intValue) {
		this.intValue = intValue;
	}
	
	@Override
	public String toString() {
		return "RayInteger( "+intValue+ " )";
	}

}
