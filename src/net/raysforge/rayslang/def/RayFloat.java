package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayMethod;

public class RayFloat implements RayClassInterface {
	
	protected double floatValue;
	
	public RayFloat() {
	}
	
	public RayFloat(double floatValue) {
		this.floatValue = floatValue;
	}

	@Override
	public String getName() {
		return KeyWords.CLASS_FLOAT;
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure, List<RayClassInterface> parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayFloat();
	}

	

}
