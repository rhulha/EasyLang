package net.raysforge.rayslang.def;

import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RayVar;


public class RayInteger extends RayFloat {
	
	public RayInteger(RayLang rayLang) {
		super( rayLang);
		name = "Integer";
	}
	
	
	public RayVar invokeNative( RayVar instance, String methodName, RayVar ... parameter )
	{
		if( methodName.equals("add!") && (parameter.length == 1) )
		{
			System.out.println("add! " + parameter[0] + " on " + instance.getValue());
			
			int a = Integer.parseInt( instance.getValue());
			instance.setValue( "" + ( a + Integer.parseInt( parameter[0].getValue() )));
			
		} else {
			return super.invokeNative(instance, methodName, parameter);
		}
		return instance;
	}

}
