package net.raysforge.rayslang.def;

import java.util.Arrays;

import net.raysforge.rayslang.RayClass;
import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RayLog;


public class RayInteger extends RayFloat {
	
	public RayInteger(RayLang rayLang) {
		super( rayLang);
		name = "Integer";
	}
	
	
	public RayClass invokeNative( RayClass rc, String methodName, RayClass ... parameter )
	{
		RayLog.info( methodName + " " + Arrays.asList(parameter) + " on " + rc.getFullName());

		if( methodName.equals("add!") && (parameter.length == 1) )
		{
			int a = Integer.parseInt( rc.getValue().toString());
			System.out.println(rc.getFullName());
			rc.setValue( "" + ( a + Long.parseLong( parameter[0].getValue() )));
			
		} else if( methodName.equals("square!") && (parameter.length == 0) )
		{
			long a = Long.parseLong( rc.getValue());
			System.out.println("test: " + a);
			rc.setValue( "" + ( a * a));
			
		} else {
			return super.invokeNative(rc, methodName, parameter);
		}
		return rc;
	}

}
