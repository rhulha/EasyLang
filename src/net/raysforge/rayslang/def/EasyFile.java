package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyMethod;

public class EasyFile implements EasyClassInterface {
	
	String name;

	@Override
	public String getName() {
		return KeyWords.CLASS_FILE;
	}

	@Override
	public EasyClassInterface invoke(String methodName, EasyMethod closure, List<EasyClassInterface> parameter) {
			return null;
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyFile();
	}

}
