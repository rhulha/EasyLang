package net.raysforge.rayslang;

import java.util.HashMap;

import net.raysforge.rayslang.def.NativeClass;

public class RayInstance {
	
	public RayClass type;
	public NativeClass nativeClass;
	
	// variables = RayUtils.newHashMap();
	public HashMap<String, RayVar> variables = new HashMap<String, RayVar>();

	public RayInstance(RayClass type) {
		this.type = type;
		if( type.isNative)
			Thread.dumpStack();
	}

	public RayInstance(NativeClass nativeClass) {
		this.nativeClass = nativeClass;
	}


}
