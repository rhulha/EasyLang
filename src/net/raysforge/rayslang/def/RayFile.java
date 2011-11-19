package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayLambda;
import net.raysforge.rayslang.RaySource;

public class RayFile implements RayClassInterface {
	
	String name;

	@Override
	public String getName() {
		return "Datei";
	}

	@Override
	public RayClassInterface invoke(String methodName, RayClassInterface... parameter) {
			return null;
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayFile();
	}

	@Override
	public RayClassInterface invoke(String methodName, RayLambda closure) {
		// TODO Auto-generated method stub
		return null;
	}

}
