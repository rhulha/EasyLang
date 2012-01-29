package net.raysforge.rayslang;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.raysforge.rayslang.def.RayArray;
import net.raysforge.rayslang.def.RayAssert;
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
	
	Output op;

	private HashMap<String, RayClassInterface> classes = new HashMap<String, RayClassInterface>();
	
	public static RayLang instance;

	public RayLang() {
		instance = this;
		initNativeClasses();
	}
	
	private void initNativeClasses() {
		registerClasses( new RayInteger());
		registerClasses( new RayString());
		registerClasses( new RayFrame());
		registerClasses( new RayFileReader());
		registerClasses( new RayGraphics());
		registerClasses( new RayAssert());
	}
	
	public static void main(String[] args) throws IOException {
		
		RayLang rayLang = new RayLang();
		rayLang.parse(new File("raysrc"));
//		runClass(rayLang.getClass("TestDatei"));
		runClass(rayLang.getClass("Sokoban"));
//		runClass(rayLang.getClass("TestFunction"));
//		runClass(rayLang.getClass("Test"));
//		runClass(rayLang.getClass("TestString"));
//		runClass(rayLang.getClass("Loop"));
//		runClass(rayLang.getClass("TestGrafik"));
//		runClass(rayLang.getClass("SimpleTest"));
//		runClass(rayLang.getClass("TestArray"));
//		runClass(rayLang.getClass("TestHashMap"));
	}

	public static void runClass(RayClassInterface rc) {
		RayClassInterface ri = rc.getNewInstance(null);
		ri.invoke( "start", null, null);
	}

	public void parse(File dir) {
		File[] list = dir.listFiles();
		for (int i = 0; i < list.length; i++) {
			File file = list[i];
			if( file.isDirectory())
				parse( file);
			else if( file.getName().endsWith(".ray"))
				RayClass.parse( file.getName().substring(0, file.getName().length()-4), file);
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
			RayLang.instance.writeln(fullClassName + " not found.");
		if( array)
			return new RayArray(fullClassName);
		else
			return rci;
	}

	public void registerClasses(RayClassInterface rayClass) {
		classes.put( rayClass.getName(), rayClass);
		
	}

	public void unregisterClasses(String name) {
		classes.remove(name);
	}

	public void setOutput( Output op) {
		this.op = op;
	}
	
	public void writeln( Object o)
	{
		op.writeln( o );
	}

}
