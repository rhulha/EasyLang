package net.raysforge.easylang.utils;

import java.util.ArrayList;
import java.util.List;

import net.raysforge.easylang.EasyClass;

public class EasyDebug {
	
	private static final List<EasyClass> classes = new ArrayList<EasyClass>();
	
	public static void add( EasyClass rc)
	{
		if( classes.contains(rc))
		{
//			System.err.println("Class already exists: " + rc);
//			Thread.dumpStack();
		}
		classes.add(rc);
	}

	public static List<EasyClass> getClasses() {
		return classes;
	}
	
	

}
