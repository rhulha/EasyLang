package net.raysforge.rayslang.def;

import net.raysforge.rayslang.RayInstance;
import net.raysforge.rayslang.RayLang;

public interface NativeClass {
	
	public void register( RayLang rayLang);
	
	public NativeClass getNewInstance();
	
	public RayInstance invoke( NativeClass nc, String methodName,  RayInstance ... parameter);
	

}
