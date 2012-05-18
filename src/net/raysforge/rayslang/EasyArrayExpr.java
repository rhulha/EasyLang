package net.raysforge.rayslang;

import net.raysforge.rayslang.def.EasyArray;
import net.raysforge.rayslang.def.EasyInteger;
import net.raysforge.rayslang.def.EasyString;

public class EasyArrayExpr {

	EasyArray rayArray;
	String key = null;
	int index;

	public EasyArrayExpr(EasyArray rayArray, int index) {
		this.rayArray = rayArray;
		this.index = index;
	}

	public EasyArrayExpr(EasyArray rayArray, String key) {
		this.rayArray = rayArray;
		this.key = key;
	}

	public EasyArrayExpr(EasyArray rayArray, EasyClassInterface index) {
		this.rayArray = rayArray;
		if (index instanceof EasyString) {
			EasyString rs = (EasyString) index;
			this.key = rs.getStringValue();
		} else if (index instanceof EasyInteger) {
			EasyInteger ri = (EasyInteger) index;
			this.index = (int) ri.getIntValue();
		} else {
			EasyUtils.runtimeExcp(index + " is not usable as an array index.");
		}

	}

	public EasyClassInterface get() {
		if (key != null)
			return rayArray.get(key);
		return rayArray.get(index);
	}

	public EasyClassInterface put(EasyClassInterface value) {
		if (key != null)
			return rayArray.put(key, value);
		return rayArray.put(index, value);

	}

}
