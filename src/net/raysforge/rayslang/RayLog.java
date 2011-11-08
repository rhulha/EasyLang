package net.raysforge.rayslang;

public class RayLog {
	
	enum Level { error, warn, info, debug, trace };
	
	static Level level = Level.debug;
	
	public static void warn( Object s)
	{
		if( level.ordinal() >= Level.warn.ordinal())
			System.out.println(s);
	}
	
	public static void info( Object s)
	{
		if( level.ordinal() >= Level.info.ordinal())
			System.out.println(s);
	}

	public static void debug( Object s)
	{
		if( level.ordinal() >= Level.debug.ordinal())
			System.out.println(s);
	}
	
	public static void trace( Object s)
	{
		if( level.ordinal() >= Level.trace.ordinal())
			System.out.println(s);
	}
	

}
