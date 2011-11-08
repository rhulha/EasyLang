package net.raysforge.rayslang.def;

import net.raysforge.rayslang.RayClass;
import net.raysforge.rayslang.RayLang;

public class RayString extends RayClass {
	
	String s = "";
	
	public RayString(RayLang rayLang) {
		super(rayLang, "String");
	}
	
	@Override
	public String getValue() {
		return s;
	}
	
	@Override
	public void setValue(String s) {
		this.s = s;
	}
	
	

}
