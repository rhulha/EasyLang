package net.raysforge.rayslang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


//Integer extends Float extends String

// Names: Pure, Simply, Kids, Easy


public class RayLang {

	HashMap<String, RayClass> classes = new HashMap<String, RayClass>();

	public RayLang() {
	}
	
	public void run() throws IOException {
		
		RayClass rc = RayClass.parse(new File("test.ray"));
		classes.put(rc.getFullName(), rc);
		
		
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

}
