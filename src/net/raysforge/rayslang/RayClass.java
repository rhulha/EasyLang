package net.raysforge.rayslang;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
		return parse(rayLang, name, new RaySource(FileUtils.readCompleteFile(file)));
	}

	public static RayClass parse(RayLang rayLang, String name, RaySource rs) {
		RayClass rc = new RayClass(rayLang, name);

		while (true) {
			TokenList tokenList = rs.getSourceTokenUntil(";", "(");

			if (tokenList.size() == 0)
				break;

			if (tokenList.equalsPattern("ii;")) {
				String typeStr = tokenList.get(0).s();
				String varName = tokenList.get(1).s();
				RayClassInterface type = rayLang.getClass(typeStr);
				if (type == null)
					RayUtils.runtimeExcp(typeStr + " not found");

				Visibility v = Visibility.protected_;
				rc.variables.put(varName, new RayVar(v, typeStr, varName));
				RayLog.trace("var: " + type + " - " + varName);

			} else if (tokenList.equalsPattern("ii=ii(")) {

				Token varType = tokenList.get(0);
				Token varName = tokenList.get(1);
				RayClassInterface varTypeClass = rayLang.getClass(varType.s());
				if (varTypeClass == null)
					RayUtils.runtimeExcp(varType + " not found");

				RayUtils.assert_(tokenList.get(3).equals(KeyWords.NEW), tokenList.get(3).s() + " != " + KeyWords.NEW);

				Token instanceType = tokenList.get(4);
				RayClassInterface instanceTypeClass = rayLang.getClass(instanceType.s());

				RayUtils.assert_(instanceTypeClass == varTypeClass, instanceTypeClass + " != " + varTypeClass); // check for inhertiance ? // TODO: check using equals ?

				Visibility v = Visibility.protected_;
				RayVar rayVar = new RayVar(v, varType.s(), varName.s());
				rayVar.setValue( instanceTypeClass.getNewInstance(null));
				rc.variables.put(varName.s(), rayVar);

				RayUtils.assert_(rs.getSourceToken().isClosedParentheses(), " missing: )");
				RayUtils.assert_(rs.getSourceToken().isSemicolon(), " missing: ;");

			} else if (tokenList.equalsPattern("iii;")) {

				String typeStr = tokenList.get(1).s();
				String varName = tokenList.get(2).s();
				RayClassInterface type = rayLang.getClass(typeStr);
				if (type == null)
					RayUtils.runtimeExcp(typeStr + " not found");

				Visibility v = Visibility.valueOf(tokenList.get(0) + "_");
				rc.variables.put(varName, new RayVar(v, typeStr, varName));
				RayLog.trace("var: " + type + " - " + varName);
			} else if (tokenList.equalsPattern("iii(")) {

				// Visibility v = Visibility.valueOf(tokenList.get(0) + "_");
				String typeStr = tokenList.get(1).s();
				String methodName = tokenList.get(2).s();
				//Token visStr = tokenList.pollLast();
				//RayClassInterface type = rayLang.getClass(typeStr.s());

				RayLog.trace("methodName: " + rc.name + "." + methodName);
				RayMethod.parse(rc, typeStr, methodName, rs);

			} else {
				RayLog.warn("hm: " + tokenList);
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
	public RayClassInterface invoke(String methodName, RayClassInterface... params) {
		RayMethod method = getMethod(methodName);
		return method.invoke(this, params);
	}

	@Override
	public RayClassInterface invoke(String methodName, RayLambda closure) {
		// TODO Auto-generated method stub
		return null;
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
