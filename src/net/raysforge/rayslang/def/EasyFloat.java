package net.raysforge.rayslang.def;

import java.util.List;
import java.util.Map;

import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyMethodInterface;

public class EasyFloat implements EasyClassInterface {
	
	protected double floatValue;
	
	public EasyFloat() {
	}
	
	public EasyFloat(double floatValue) {
		this.floatValue = floatValue;
	}

	@Override
	public String getName() {
		return "Float";
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyFloat();
	}

	@Override
	public EasyMethodInterface getMethod(String methodName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, EasyMethodInterface> getMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
