package net.raysforge.rayslang;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.raysforge.rayslang.def.RayFloat;
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

	HashMap<String, RayClass> classes = new HashMap<String, RayClass>();

	public RayLang() {
	}
	
	public void run() throws IOException {
		
		initNativeClasses();
		
		RayClass rc = RayClass.parse( this, "Test", new File("Test.ray"));
		
		RayMethod rm = rc.getMethod( "main");
		rm.invoke( rc);
		
		//rc.run( "test");
		
	}

	private void initNativeClasses() {
		RayClass stringClass = new RayString(this);
		classes.put( "default.String", stringClass);
		RayClass floatClass = new RayFloat(this);
		classes.put( "default.Float", floatClass);
		RayClass integerClass = new RayInteger(this);
		classes.put( "default.Integer", integerClass);
		
		new RayMethod( integerClass, "add!", true);
		new RayMethod( integerClass, "square!", true);
		new RayMethod( integerClass, "print", true);
		
		
	}
	
	
	public static void main(String[] args) throws IOException {
		
		new RayLang().run();
	}

	public RayClass getClass(String package_, Token className) {
		return classes.get(package_+"."+className);
		
	}

}
