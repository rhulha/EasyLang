package net.raysforge.rayslang;

import net.raysforge.rayslang.def.RayArray;
import net.raysforge.rayslang.def.RayInteger;
import net.raysforge.rayslang.def.RayString;

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

	public RayArrayExpr(RayArray rayArray, RayClassInterface index) {
		this.rayArray = rayArray;
		if (index instanceof RayString) {
			RayString rs = (RayString) index;
			this.key = rs.getStringValue();
		} else if (index instanceof RayInteger) {
			RayInteger ri = (RayInteger) index;
			this.index = (int) ri.getIntValue();
		} else {
			RayUtils.runtimeExcp(index + " is not usable as an array index.");
		}

	}

	public RayClassInterface get() {
		if (key != null)
			return rayArray.get(key);
		return rayArray.get(index);
	}

	public RayClassInterface put(RayClassInterface value) {
		if (key != null)
			return rayArray.put(key, value);
		return rayArray.put(index, value);

	}

}
