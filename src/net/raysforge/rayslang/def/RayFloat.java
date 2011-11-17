package net.raysforge.rayslang.def;

import net.raysforge.rayslang.RayClassInterface;

public class RayFloat implements RayClassInterface {
	
	protected double floatValue;
	
	public RayFloat() {
	}
	
	public RayFloat(double floatValue) {
		this.floatValue = floatValue;
	}

	@Override
	public String getName() {
		return "Flieﬂ";
	}

	@Override
	public RayClassInterface invoke(String methodName, RayClassInterface... parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RayClassInterface getNewInstance() {
		return new RayFloat();
	}
	

}
