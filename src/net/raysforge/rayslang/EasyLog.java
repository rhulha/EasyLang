package net.raysforge.rayslang;

public enum EasyLog {
	
	error, warn, info, debug, trace;
	
	static EasyLog level = EasyLog.info;
	
	public void log(Object s) {
		if( level.ordinal() >= ordinal())
			EasyLang.instance.writeln(name() + ": " + s);
	}
	
}
