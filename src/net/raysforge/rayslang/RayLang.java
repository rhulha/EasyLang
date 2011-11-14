package net.raysforge.rayslang;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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

	public HashMap<String, RayClass> classes = new HashMap<String, RayClass>();

	public RayLang() {
		initNativeClasses();
	}
	
	private void initNativeClasses() {
		new RayInteger().register(this);
		new RayString().register(this);
	}
	
	public static void main(String[] args) throws IOException {
		
		RayLang rayLang = new RayLang();
		rayLang.parse(new File("raysrc"));
		RayClass rc = rayLang.getClass("default", "Sokoban");
		RayInstance ri = rc.getNewInstance();
		RayMethod rm = rc.getMethod( "start");
		rm.invoke( ri);
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

	public RayClass getClass(String package_, String className) {
		return classes.get(package_+"."+className);
		
	}

}
