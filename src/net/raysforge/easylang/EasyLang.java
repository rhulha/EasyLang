package net.raysforge.easylang;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.raysforge.easylang.def.EasyArray;
import net.raysforge.easylang.def.EasyAssert;
import net.raysforge.easylang.def.EasyBoolean;
import net.raysforge.easylang.def.EasyFileReader;
import net.raysforge.easylang.def.EasyFrame;
import net.raysforge.easylang.def.EasyGraphics;
import net.raysforge.easylang.def.EasyInteger;
import net.raysforge.easylang.def.EasyLangList;
import net.raysforge.easylang.def.EasyString;

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

	private static final String EASY_FILE_EXT = ".easy";

	Output output;

	private HashMap<String, EasyClassInterface> classes = new HashMap<String, EasyClassInterface>();

	public static EasyLang instance;
	
	public Random random = new Random();
	public ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	public ScriptEngine javaScriptEngine = scriptEngineManager.getEngineByName("JavaScript");

	public static ResourceBundle rb;

	public EasyLang() {
		instance = this;
		rb = ResourceBundle.getBundle("net.raysforge.easylang.EasyLang");
		output = new Output() {
			@Override
			public void writeln(Object o) {
				System.out.println(o);
			}
		};
		initNativeClasses();
	}

	private void initNativeClasses() {
		registerClass(new EasyAssert());
		registerClass(new EasyBoolean());
		registerClass(new EasyFileReader());
		registerClass(new EasyFrame());
		registerClass(new EasyGraphics());
		registerClass(new EasyInteger());
		registerClass(new EasyLangList());
		registerClass(new EasyString());
	}

	public static void main(String[] args) throws IOException {

		EasyLang easyLang = new EasyLang();
		easyLang.parse(new File("easysrc"));
		//		runClass(easyLang.getClass("TestDatei"));
		// runClass(easyLang.getClass("BrainTease"));
		//		runClass(easyLang.getClass("TestElseClosure"));
		//		runClass(easyLang.getClass("Sokoban"));
				//runClass(easyLang.getClass("TestFunction"));
		//		runClass(easyLang.getClass("Test"));
		runClass(easyLang.getClass("TestString"));
		//		runClass(easyLang.getClass("Loop"));
		//		runClass(easyLang.getClass("TestGrafik"));
		//		runClass(easyLang.getClass("SimpleTest"));
		//runClass(easyLang.getClass("TestArray"));
		//		runClass(easyLang.getClass("TestHashMap"));
	}

	public static void runClass(EasyClassInterface rc) {
		EasyClassInterface eci = rc.getNewInstance(null);
		EasyMethodInterface method = eci.getMethod("start");
		if( method == null)
		{
			EasyLang.instance.writeln("The class you are trying to start does not contain a method called 'start'");
		}
		method.invoke(eci, null, null, null);
	}

	public void parse(File dir) {
		File[] list = dir.listFiles();
		for (int i = 0; i < list.length; i++) {
			File file = list[i];
			if (file.isDirectory())
				parse(file);
			else if (file.getName().endsWith(EASY_FILE_EXT))
				EasyClass.parse(file.getName().substring(0, file.getName().length() - EASY_FILE_EXT.length()), file);
		}
	}

	public EasyClassInterface getClass(String fullClassName) {
		boolean array = false;
		if (fullClassName.endsWith("[]")) {
			array = true;
			fullClassName = fullClassName.substring(0, fullClassName.length() - 4);
		}
		EasyClassInterface rci = classes.get(fullClassName);
		if (rci == null)
			EasyLang.instance.writeln(fullClassName + " not found.");
		if (array)
			return new EasyArray(fullClassName);
		else
			return rci;
	}

	public void registerClass(EasyClassInterface easyClass) {
		//System.out.println("register: " + easyClass.getName());
		classes.put(easyClass.getName(), easyClass);

	}

	public void unregisterClasses(String name) {
		classes.remove(name);
	}
	
	public void setOutput(Output op) {
		this.output = op;
	}

	public void writeln(Object o) {
		output.writeln(o);
	}

	public HashMap<String, EasyClassInterface> getClasses() {
		return classes;
	}


}
