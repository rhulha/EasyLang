package net.raysforge.easylang;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import net.raysforge.easylang.utils.EasyDebug;
import net.raysforge.easylang.utils.EasyLog;
import net.raysforge.easylang.utils.EasyUtils;

public class EasyClass implements EasyClassInterface {

	protected String name;

	HashMap<String, EasyVar> variables = new HashMap<String, EasyVar>();

	HashMap<String, EasyMethodInterface> methods = new HashMap<String, EasyMethodInterface>();

	public EasyClass( String name) {
		this.name = name;
		EasyDebug.add(this);
	}

	public static EasyClass parse( String name, File file) {
		return parse( name, EasyUtils.convertSourceToTokenList(file));
	}

	public static EasyClass parse( String name, TokenList tokenList) {
		EasyClass rc = new EasyClass( name);
		EasyLang.instance.registerClass(rc);

		while (true) {

			if (tokenList.remaining() == 0)
				break;

			if (tokenList.startsWithPattern("ii;") || tokenList.startsWithPattern("i[]i;")) {
				parseNewVariable( Visibility.protected_, rc, tokenList);
			} else if (tokenList.startsWithPattern("ii=") || tokenList.startsWithPattern("i[]i=") ) {

				String varTypeStr = tokenList.popString();
				if( tokenList.startsWithPattern("[]"))
				{
					tokenList.remove("[");
					tokenList.remove("]");
					varTypeStr += "[]";
				}
				String varName = tokenList.popString();
				tokenList.remove("=");
				EasyClassInterface eval = EasyMethod.evaluateExpression(rc, rc.variables, tokenList);
				
				/*
				tokenList.remove(KeyWords.NEW);
				String instanceType = tokenList.popString();
				
				EasyClassInterface varTypeClass = easyLang.getClass(varTypeStr);
				if (varTypeClass == null)
					EasyUtils.runtimeExcp(varTypeStr + " not found");

				EasyClassInterface instanceTypeClass = easyLang.getClass(instanceType);

				EasyUtils.assert_(instanceTypeClass == varTypeClass, instanceTypeClass + " != " + varTypeClass); // check for inhertiance ? // TODO: check using equals ?
				tokenList.remove("(");
				tokenList.remove(")");
				*/

				// removed the following code, because currently I don't want to support constructors with parameters. 
				//TokenList paramTokenList = tokenList.getSubList('(', ')');
				//List<EasyClassInterface> myparams = EasyMethod.evaluateParams( rc, rc.variables, paramTokenList);
				
				Visibility v = Visibility.protected_;
				EasyVar var = new EasyVar(v, varTypeStr, varName);
				//var.setValue( instanceTypeClass.getNewInstance(myparams));
				//var.setValue( instanceTypeClass.getNewInstance(null));
				var.setValue( eval);
				rc.variables.put(varName, var);
				tokenList.remove(";");

			} else if (tokenList.startsWithPattern("iii;") || tokenList.startsWithPattern("ii[]i;")) {

				String visibility = tokenList.popString();
				Visibility v = Visibility.valueOf(visibility + "_");
				parseNewVariable( v, rc, tokenList);
			} else if (tokenList.startsWithPattern("iii(")) {
				/* this is a method declaration */

				String visibility = tokenList.popString();
				visibility.hashCode();
				String typeStr = tokenList.popString();
				String methodName = tokenList.popString();
				tokenList.remove("(");

				EasyLog.trace.log("methodName: " + rc.name + "." + methodName);
				EasyMethod em = EasyMethod.parse( rc.getName(), typeStr, methodName, tokenList);
				System.out.println(methodName);
				rc.methods.put(methodName, em);

			} else {
				EasyLog.warn.log("hm: " + tokenList);
			}
			//EasyLog.log("XX" + token + "YY");
		}

		return rc;
	}

	private static void parseNewVariable(  Visibility v, EasyClass rc, TokenList tokenList) {
		String typeStr = tokenList.popString();
		if( tokenList.startsWithPattern("[]"))
		{
			tokenList.remove("[");
			tokenList.remove("]");
			typeStr += "[]";
		}
		String varName = tokenList.popString();
		tokenList.remove(";");

		EasyClassInterface type = EasyLang.instance.getClass(typeStr);
		if (type == null)
			EasyUtils.runtimeExcp(typeStr + " not found");

		rc.variables.put(varName, new EasyVar(v, typeStr, varName));
		EasyLog.debug.log("var: " + type.getName() + " - " + varName);
	}

	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {

		EasyClass ri = new EasyClass( name);
		ri.methods = new HashMap<String, EasyMethodInterface>();
		for( String key : methods.keySet())
		{
			((EasyMethod)methods.get(key)).copy(ri);
		}

		for (String key : variables.keySet()) {
			EasyVar var = this.variables.get(key);
			EasyVar var2 = var.copy();
			var2.setValue( var.getValue() == null ? EasyLang.instance.getClass( var.getType()).getNewInstance(null) : var.getValue());
			ri.variables.put(key, var2);
		}
		return ri;
	}

	@Override
	public String toString() {
		return getName();
	}

	public EasyMethodInterface getMethod(String name) {
		return methods.get(name);
	}

	@Override
	public String getName() {
		return name;
	}

	/*
	@Override
	public EasyClassInterface invoke(String methodName, EasyMethod closure, List<EasyClassInterface> parameter) {
		if( methodName.equals("debug"))
		{
			EasyLang.instance.writeln("debug start.");
			//Thread.dumpStack();
			for( EasyClass rc : EasyDebug.getClasses())
			{
				//EasyLang.instance.writeln(rc.getName());
				if( rc.getName().equals("TestFunction"))
				{
					EasyLang.instance.writeln("debugging: " + rc.getName());
					for( String k : rc.variables.keySet())
					{
						EasyLang.instance.writeln(rc.variables.get(k));
					}
				}
			}
			EasyLang.instance.writeln("debug end.");
			return null;
		} else {
			EasyMethod method = getMethod(methodName);
			return method.invoke( parameter);
		}
	}
	*/

	/*
	public EasyClass invokeNative(EasyClass rc, String methodName, EasyClass ... parameter) {
		if( methodName.equals("print") && (parameter.length == 0) )
		{
			System err.println(((EasyString)rc).getValue());
		} else {
			EasyUtils.RunExp("method not found: " + methodName);
		}
		return null;
	}
	*/
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EasyClass) {
			EasyClass rc = (EasyClass) obj;
			return rc.getName().equals(getName());
			
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public HashMap<String, EasyMethodInterface> getMethods() {
		// TODO Auto-generated method stub
		return null;
	}

}
