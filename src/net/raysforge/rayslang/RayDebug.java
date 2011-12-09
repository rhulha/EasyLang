package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.List;

public class RayDebug {
	
	private static final List<RayClass> classes = new ArrayList<RayClass>();
	
	public static void add( RayClass rc)
	{
		if( classes.contains(rc))
		{
//			System.err.println("Class already exists: " + rc);
//			Thread.dumpStack();
		}
		classes.add(rc);
	}

	public static List<RayClass> getClasses() {
		return classes;
	}
	
	

}
