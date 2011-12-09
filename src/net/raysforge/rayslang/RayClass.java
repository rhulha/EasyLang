package net.raysforge.rayslang;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class RayClass implements RayClassInterface {

	protected String name;

	HashMap<String, RayVar> variables = new HashMap<String, RayVar>();

	HashMap<String, RayMethod> methods = new HashMap<String, RayMethod>();

	public RayClass( String name) {
		this.name = name;
		RayDebug.add(this);
	}

	public static RayClass parse( String name, File file) {
		return parse( name, RayUtils.convertSourceToTokenList(file));
	}

	public static RayClass parse( String name, TokenList tokenList) {
		RayClass rc = new RayClass( name);
		RayLang.instance.registerClasses(rc);

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
				RayClassInterface eval = RayMethod.evaluateExpression(rc, rc.variables, tokenList);
				
				/*
				tokenList.remove(KeyWords.NEW);
				String instanceType = tokenList.popString();
				
				RayClassInterface varTypeClass = rayLang.getClass(varTypeStr);
				if (varTypeClass == null)
					RayUtils.runtimeExcp(varTypeStr + " not found");

				RayClassInterface instanceTypeClass = rayLang.getClass(instanceType);

				RayUtils.assert_(instanceTypeClass == varTypeClass, instanceTypeClass + " != " + varTypeClass); // check for inhertiance ? // TODO: check using equals ?
				tokenList.remove("(");
				tokenList.remove(")");
				*/

				// removed the following code, because currently I don't want to support constructors with parameters. 
				//TokenList paramTokenList = tokenList.getSubList('(', ')');
				//List<RayClassInterface> myparams = RayMethod.evaluateParams( rc, rc.variables, paramTokenList);
				
				Visibility v = Visibility.protected_;
				RayVar rayVar = new RayVar(v, varTypeStr, varName);
				//rayVar.setValue( instanceTypeClass.getNewInstance(myparams));
				// rayVar.setValue( instanceTypeClass.getNewInstance(null));
				rayVar.setValue( eval);
				rc.variables.put(varName, rayVar);
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

				RayLog.trace.log("methodName: " + rc.name + "." + methodName);
				RayMethod.parse(rc, typeStr, methodName, tokenList);

			} else {
				RayLog.warn.log("hm: " + tokenList);
			}
			//RayLog.log("XX" + token + "YY");
		}

		return rc;
	}

	private static void parseNewVariable(  Visibility v, RayClass rc, TokenList tokenList) {
		String typeStr = tokenList.popString();
		if( tokenList.startsWithPattern("[]"))
		{
			tokenList.remove("[");
			tokenList.remove("]");
			typeStr += "[]";
		}
		String varName = tokenList.popString();
		tokenList.remove(";");

		RayClassInterface type = RayLang.instance.getClass(typeStr);
		if (type == null)
			RayUtils.runtimeExcp(typeStr + " not found");

		rc.variables.put(varName, new RayVar(v, typeStr, varName));
		RayLog.debug.log("var: " + type.getName() + " - " + varName);
	}

	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {

		RayClass ri = new RayClass( name);
		ri.methods = new HashMap<String, RayMethod>();
		for( String key : methods.keySet())
		{
			methods.get(key).copy(ri);
		}

		for (String key : variables.keySet()) {
			RayVar rayVar = this.variables.get(key);
			RayVar rayVar2 = rayVar.copy();
			rayVar2.setValue( rayVar.getValue() == null ? RayLang.instance.getClass( rayVar.getType()).getNewInstance(null) : rayVar.getValue());
			ri.variables.put(key, rayVar2);
		}
		return ri;
	}

	@Override
	public String toString() {
		return getName();
	}

	public RayMethod getMethod(String name) {
		return methods.get(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure, List<RayClassInterface> parameter) {
		if( methodName.equals("debug"))
		{
			System.out.println("debug start.");
			//Thread.dumpStack();
			for( RayClass rc : RayDebug.getClasses())
			{
				//System.out.println(rc.getName());
				if( rc.getName().equals("TestFunction"))
				{
					System.out.println("debugging: " + rc.getName());
					for( String k : rc.variables.keySet())
					{
						System.out.println(rc.variables.get(k));
					}
				}
			}
			System.out.println("debug end.");
			return null;
		} else {
			RayMethod method = getMethod(methodName);
			return method.invoke( parameter);
		}
	}

	/*
	public RayClass invokeNative(RayClass rc, String methodName, RayClass ... parameter) {
		if( methodName.equals("print") && (parameter.length == 0) )
		{
			System err.println(((RayString)rc).getValue());
		} else {
			RayUtils.RunExp("method not found: " + methodName);
		}
		return null;
	}
	*/
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RayClass) {
			RayClass rc = (RayClass) obj;
			return rc.getName().equals(getName());
			
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

}
