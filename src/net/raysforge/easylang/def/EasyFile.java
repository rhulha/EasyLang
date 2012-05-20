package net.raysforge.easylang.def;

import java.util.List;
import java.util.Map;

import net.raysforge.easylang.EasyClassInterface;
import net.raysforge.easylang.EasyLang;
import net.raysforge.easylang.EasyMethodInterface;

public class EasyFile implements EasyClassInterface {
	
	String name;

	@Override
	public String getName() {
		return EasyLang.rb.getString("File");
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyFile();
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
