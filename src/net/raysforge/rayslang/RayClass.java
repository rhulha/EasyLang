package net.raysforge.rayslang;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class RayClass implements RayClassInterface {

	protected String name;
	RayLang rayLang;

	HashMap<String, RayVar> variables = new HashMap<String, RayVar>();

	HashMap<String, RayMethod> methods = new HashMap<String, RayMethod>();

	public RayClass(RayLang rayLang, String name) {
		this.name = name;
		this.rayLang = rayLang;
		rayLang.registerClasses(this);
	}

	public static RayClass parse(RayLang rayLang, String name, File file) {
		return parse(rayLang, name, RayUtils.convertSourceToTokenList(file));
	}

	public static RayClass parse(RayLang rayLang, String name, TokenList tokenList) {
		RayClass rc = new RayClass(rayLang, name);

		while (true) {

			if (tokenList.remaining() == 0)
				break;

			if (tokenList.startsWithPattern("ii;")) {
				String typeStr = tokenList.popString();
				String varName = tokenList.popString();
				tokenList.remove(";");

				RayClassInterface type = rayLang.getClass(typeStr);
				if (type == null)
					RayUtils.runtimeExcp(typeStr + " not found");

				Visibility v = Visibility.protected_;
				rc.variables.put(varName, new RayVar(v, typeStr, varName));
				RayLog.trace.log("var: " + type + " - " + varName);

			} else if (tokenList.startsWithPattern("ii=ii(")) {

				String varType = tokenList.popString();
				String varName = tokenList.popString();
				tokenList.remove("=");
				tokenList.remove(KeyWords.NEW);
				String instanceType = tokenList.popString();
				
				RayClassInterface varTypeClass = rayLang.getClass(varType);
				if (varTypeClass == null)
					RayUtils.runtimeExcp(varType + " not found");

				RayClassInterface instanceTypeClass = rayLang.getClass(instanceType);

				RayUtils.assert_(instanceTypeClass == varTypeClass, instanceTypeClass + " != " + varTypeClass); // check for inhertiance ? // TODO: check using equals ?

				tokenList.remove("(");
				TokenList paramTokenList = tokenList.getInnerList('(', ')');
				List<RayClassInterface> myparams = RayMethod.evaluateParams(rc.variables, paramTokenList);
				
				Visibility v = Visibility.protected_;
				RayVar rayVar = new RayVar(v, varType, varName);
				rayVar.setValue( instanceTypeClass.getNewInstance(myparams));
				rc.variables.put(varName, rayVar);

				tokenList.remove(";");

			} else if (tokenList.startsWithPattern("iii;")) {

				String visibility = tokenList.popString();
				String typeStr = tokenList.popString();
				String varName = tokenList.popString();
				tokenList.remove(";");

				RayClassInterface type = rayLang.getClass(typeStr);
				if (type == null)
					RayUtils.runtimeExcp(typeStr + " not found");

				Visibility v = Visibility.valueOf(visibility + "_");
				rc.variables.put(varName, new RayVar(v, typeStr, varName));
				RayLog.trace.log("var: " + type + " - " + varName);
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

	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {

		RayClass ri = new RayClass(rayLang, name);
		ri.methods = methods;

		for (String key : variables.keySet()) {
			RayVar rayVar = this.variables.get(key);
			RayVar rayVar2 = rayVar.copy();
			rayVar2.setValue( rayLang.getClass( rayVar.getType()).getNewInstance(null));
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
	public RayClassInterface invoke(String methodName, RayMethod closure, RayClassInterface... params) {
		RayMethod method = getMethod(methodName);
		return method.invoke(this, params);
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

}
