package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayMethod;

public class RayFile implements RayClassInterface {
	
	String name;

	@Override
	public String getName() {
		return KeyWords.CLASS_FILE;
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure, RayClassInterface... parameter) {
			return null;
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayFile();
	}

}
