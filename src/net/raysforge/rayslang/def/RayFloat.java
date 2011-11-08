package net.raysforge.rayslang.def;

import net.raysforge.rayslang.RayInstance;
import net.raysforge.rayslang.RayLang;

public class RayFloat extends RayString implements NativeClass {
	
	double d;
	
	@Override
	public void register(RayLang rayLang) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RayInstance invoke( NativeClass nc, String methodName, RayInstance... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NativeClass getNewInstance() {
		// TODO Auto-generated method stub
		return null;
	}

}
