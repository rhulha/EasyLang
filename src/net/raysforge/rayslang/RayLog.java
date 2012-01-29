package net.raysforge.rayslang;

public enum RayLog {
	
	error, warn, info, debug, trace;
	
	static RayLog level = RayLog.info;
	
	public void log(Object s) {
		if( level.ordinal() >= ordinal())
			RayLang.instance.writeln(name() + ": " + s);
	}
	
}
