package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyMethod;

public class EasyFloat implements EasyClassInterface {
	
	protected double floatValue;
	
	public EasyFloat() {
	}
	
	public EasyFloat(double floatValue) {
		this.floatValue = floatValue;
	}

	@Override
	public String getName() {
		return KeyWords.CLASS_FLOAT;
	}

	@Override
	public EasyClassInterface invoke(String methodName, EasyMethod closure, List<EasyClassInterface> parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyFloat();
	}

	

}
