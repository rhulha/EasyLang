package net.raysforge.rayslang;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.raysforge.rayslang.def.RayInteger;
import net.raysforge.rayslang.def.RayString;


//Integer extends Float extends String

// Names: Pure, Simply, Kids, Easy

// goal: subset of Java
// few rules, no exceptions:

// only default invisible constructor, init method instead.
// no void, all methods must return something
// no null pointer


public class RayLang {

	public HashMap<String, RayClass> classes = new HashMap<String, RayClass>();

	public RayLang() {
	}
	
	public void run() throws IOException {
		
		initNativeClasses();
		
		RayClass rc = RayClass.parse( this, "Test", new File("Test.ray"));
		RayInstance ri = rc.getNewInstance();
		RayMethod rm = rc.getMethod( "main");
		rm.invoke( ri);
		
		//rc.run( "test");
		
	}

	private void initNativeClasses() {
		
		new RayInteger().register(this);
		new RayString().register(this);
		
	}
	
	
	public static void main(String[] args) throws IOException {
		
		new RayLang().run();
	}

	public RayClass getClass(String package_, String className) {
		return classes.get(package_+"."+className);
		
	}

}
