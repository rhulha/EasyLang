package net.raysforge.easylang.utils;

import net.raysforge.easylang.EasyLang;

public enum EasyLog {
	
	error, warn, info, debug, trace;
	
	public static EasyLog level = EasyLog.info;
	
	public void log(Object s) {
		if( level.ordinal() >= ordinal())
			EasyLang.instance.writeln(name() + ": " + s);
	}
	
}
