package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyMethod;
import net.raysforge.rayslang.EasyMethodInterface;
import net.raysforge.rayslang.EasyVar;

public class NativeMethod implements EasyMethodInterface {
	
	private final String name;
	private final String returnType;
	private final List<EasyVar> parameterList;

	public NativeMethod(String returnType, String name, List<EasyVar> parameterList) {
		this.returnType = returnType;
		this.name = name;
		this.parameterList = parameterList;
	}
	
	public String getName() {
		return name;
	}
	
	public List<EasyVar> getParameterList() {
		return parameterList;
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	public EasyClassInterface invoke( EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter)
	{
		return null;
	}
	
	public void assertParameterSize( List<EasyClassInterface> parameter, int size)
	{
		if( parameter.size() != size)
		{
			throw new RuntimeException("parameters given to not match required ammount: " + size + ", " + parameter.size());
		}
	}

	public void assertClosure( Object o)
	{
		if( o == null)
		{
			throw new RuntimeException("closure missing");
		}
	}

}
