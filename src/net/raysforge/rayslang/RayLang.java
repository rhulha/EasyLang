package net.raysforge.rayslang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import net.raysforge.rayslang.def.RayFloat;
import net.raysforge.rayslang.def.RayInteger;
import net.raysforge.rayslang.def.RayString;


//Integer extends Float extends String

// Names: Pure, Simply, Kids, Easy


public class RayLang {

	HashMap<String, RayClass> classes = new HashMap<String, RayClass>();

	public RayLang() {
	}
	
	public void run() throws IOException {
		
		classes.put( "default.String", new RayString());
		classes.put( "default.Float", new RayFloat());
		classes.put( "default.Integer", new RayInteger());

		RayClass rc = RayClass.parse( this, "Test", new File("Test.ray"));
		classes.put( rc.getFullName(), rc);
		RayMethod rm = rc.getMethod( "main");
		rm.invoke("main", null);
		
		//rc.run( "test");
		
	}
	
	public RayClass parseClass(InputStream is)
	{
		RayClass rc = new RayClass();
		return rc;
	}
	
	public static void main(String[] args) throws IOException {
		
		new RayLang().run();
	}

	public RayClass getClass(String package_, Token className) {
		return classes.get(package_+"."+className);
		
	}

}
