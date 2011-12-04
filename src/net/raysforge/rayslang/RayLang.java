package net.raysforge.rayslang;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.raysforge.rayslang.def.RayArray;
import net.raysforge.rayslang.def.RayFileReader;
import net.raysforge.rayslang.def.RayFrame;
import net.raysforge.rayslang.def.RayGraphics;
import net.raysforge.rayslang.def.RayInteger;
import net.raysforge.rayslang.def.RayString;


//Integer extends Float extends String

// Names: Pure, Simply, Kids, Easy

// no constructors == less rules, easy extends

// goal: subset of Java
// few rules, no exceptions:

// only default invisible constructor, init method instead.
// no void, all methods must return something
// no null pointer

// default is serializable

// no checked exceptions, only runtime exceptions 


public class RayLang {

	private HashMap<String, RayClassInterface> classes = new HashMap<String, RayClassInterface>();

	public RayLang() {
		initNativeClasses();
	}
	
	private void initNativeClasses() {
		registerClasses( new RayInteger());
		registerClasses( new RayString());
		registerClasses( new RayFrame());
		registerClasses( new RayFileReader());
		registerClasses( new RayGraphics());
	}
	
	public static void main(String[] args) throws IOException {
		
		RayLang rayLang = new RayLang();
		rayLang.parse(new File("raysrc"));
//		runClass(rayLang.getClass("DateiTest"));
//		runClass(rayLang.getClass("Sokoban"));
		runClass(rayLang.getClass("Test"));
//		runClass(rayLang.getClass("Loop"));
		runClass(rayLang.getClass("TestGrafik"));
//		runClass(rayLang.getClass("SimpleTest"));
//		runClass(rayLang.getClass("ArrayTest"));
//		runClass(rayLang.getClass("TestHashMap"));
	}

	private static void runClass(RayClassInterface rc) {
		RayClassInterface ri = rc.getNewInstance(null);
		ri.invoke( "start", null, null);
	}

	private void parse(File dir) {
		File[] list = dir.listFiles();
		for (int i = 0; i < list.length; i++) {
			File file = list[i];
			if( file.isDirectory())
				parse( file);
			else if( file.getName().endsWith(".ray"))
				RayClass.parse( this, file.getName().substring(0, file.getName().length()-4), file);
		}
	}

	public RayClassInterface getClass( String fullClassName) {
		boolean array = false;
		if( fullClassName.endsWith("[]"))
		{
			array = true;
			fullClassName = fullClassName.substring(0, fullClassName.length()-4);
		}
		RayClassInterface rci = classes.get(fullClassName);
		if( rci == null)
			System.out.println(fullClassName + " not found.");
		if( array)
			return new RayArray(fullClassName);
		else
			return rci;
	}

	public void registerClasses(RayClassInterface rayClass) {
		classes.put( rayClass.getName(), rayClass);
		
	}

}
