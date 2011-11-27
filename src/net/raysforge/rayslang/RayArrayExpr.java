package net.raysforge.rayslang;

import net.raysforge.rayslang.def.RayArray;

public class RayArrayExpr {

	RayArray rayArray;
	String key = null;
	int index;

	public RayArrayExpr(RayArray rayArray, int index) {
		this.rayArray = rayArray;
		this.index = index;
	}

	public RayArrayExpr(RayArray rayArray, String key) {
		this.rayArray = rayArray;
		this.key = key;
	}

	public RayClassInterface get() {
		if (key != null)
			return rayArray.get(key);
		return rayArray.get(index);
	}
	
	public RayClassInterface put(RayClassInterface value)
	{
		if (key != null)
			return rayArray.put(key, value);
		return rayArray.put(index, value);
		
	}

}
