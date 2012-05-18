package net.raysforge.rayslang;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

import net.raysforge.rayslang.def.EasyArray;
import net.raysforge.rayslang.def.EasyAssert;
import net.raysforge.rayslang.def.EasyFileReader;
import net.raysforge.rayslang.def.EasyFrame;
import net.raysforge.rayslang.def.EasyGraphics;
import net.raysforge.rayslang.def.EasyInteger;
import net.raysforge.rayslang.def.EasyString;


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


public class EasyLang {
	
	Output op;

	private HashMap<String, EasyClassInterface> classes = new HashMap<String, EasyClassInterface>();
	
	public static EasyLang instance;

	public static ResourceBundle rb;

	public EasyLang() {
		instance = this;
		rb = ResourceBundle.getBundle("net.raysforge.rayslang.EasyLang");
		initNativeClasses();
	}
	
	private void initNativeClasses() {
		registerClasses( new EasyInteger());
		registerClasses( new EasyString());
		registerClasses( new EasyFrame());
		registerClasses( new EasyFileReader());
		registerClasses( new EasyGraphics());
		registerClasses( new EasyAssert());
	}
	
	public static void main(String[] args) throws IOException {
		
		EasyLang rayLang = new EasyLang();
		rayLang.parse(new File("raysrc"));
//		runClass(rayLang.getClass("TestDatei"));
//		runClass(rayLang.getClass("Sokoban"));
//		runClass(rayLang.getClass("TestFunction"));
//		runClass(rayLang.getClass("Test"));
//		runClass(rayLang.getClass("TestString"));
		runClass(rayLang.getClass("Loop"));
//		runClass(rayLang.getClass("TestGrafik"));
//		runClass(rayLang.getClass("SimpleTest"));
//		runClass(rayLang.getClass("TestArray"));
//		runClass(rayLang.getClass("TestHashMap"));
	}

	public static void runClass(EasyClassInterface rc) {
		EasyClassInterface ri = rc.getNewInstance(null);
		ri.invoke( "start", null, null);
	}

	public void parse(File dir) {
		File[] list = dir.listFiles();
		for (int i = 0; i < list.length; i++) {
			File file = list[i];
			if( file.isDirectory())
				parse( file);
			else if( file.getName().endsWith(".ray"))
				EasyClass.parse( file.getName().substring(0, file.getName().length()-4), file);
		}
	}

	public EasyClassInterface getClass( String fullClassName) {
		boolean array = false;
		if( fullClassName.endsWith("[]"))
		{
			array = true;
			fullClassName = fullClassName.substring(0, fullClassName.length()-4);
		}
		EasyClassInterface rci = classes.get(fullClassName);
		if( rci == null)
			EasyLang.instance.writeln(fullClassName + " not found.");
		if( array)
			return new EasyArray(fullClassName);
		else
			return rci;
	}

	public void registerClasses(EasyClassInterface rayClass) {
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
