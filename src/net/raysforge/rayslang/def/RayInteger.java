package net.raysforge.rayslang.def;

import java.util.Arrays;

import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RayLog;
import net.raysforge.rayslang.RayVar;


public class RayInteger extends RayFloat {
	
	public RayInteger(RayLang rayLang) {
		super( rayLang);
		name = "Integer";
	}
	
	
	public RayVar invokeNative( RayVar instance, String methodName, RayVar ... parameter )
	{
		RayLog.log( methodName + " " + Arrays.asList(parameter) + " on " + instance.getValue());

		if( methodName.equals("add!") && (parameter.length == 1) )
		{
			int a = Integer.parseInt( instance.getValue());
			instance.setValue( "" + ( a + Integer.parseInt( parameter[0].getValue() )));
			
		} else if( methodName.equals("square!") && (parameter.length == 0) )
		{
			long a = Long.parseLong( instance.getValue());
			System.out.println("test: " + a);
			instance.setValue( "" + ( a * a));
			
		} else {
			return super.invokeNative(instance, methodName, parameter);
		}
		return instance;
	}

}
