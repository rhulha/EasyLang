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
	}

	public RayInstance(NativeClass nativeClass) {
		this.nativeClass = nativeClass;
	}

	@Override
	public String toString() {
		return "RayInstance [type=" + type + ", nativeClass=" + nativeClass + ", variables=" + variables + "]";
	}

	public boolean isNative() {
		return nativeClass != null;
	}
	
	

}
