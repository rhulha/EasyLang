package net.raysforge.easylang;

import net.raysforge.easylang.def.EasyArray;
import net.raysforge.easylang.def.EasyInteger;
import net.raysforge.easylang.def.EasyString;
import net.raysforge.easylang.utils.EasyUtils;

public class EasyArrayExpr {

	EasyArray easyArray;
	String key = null;
	int index;

	public EasyArrayExpr(EasyArray easyArray, int index) {
		this.easyArray = easyArray;
		this.index = index;
	}

	public EasyArrayExpr(EasyArray easyArray, String key) {
		this.easyArray = easyArray;
		this.key = key;
	}

	public EasyArrayExpr(EasyArray easyArray, EasyClassInterface index) {
		this.easyArray = easyArray;
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
			return easyArray.get(key);
		return easyArray.get(index);
	}

	public EasyClassInterface put(EasyClassInterface value) {
		if (key != null)
			return easyArray.put(key, value);
		return easyArray.put(index, value);

	}

}
