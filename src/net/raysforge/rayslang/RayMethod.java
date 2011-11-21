package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.raysforge.rayslang.def.RayArray;
import net.raysforge.rayslang.def.RayInteger;
import net.raysforge.rayslang.def.RayString;

public class RayMethod {

	protected String name;

	protected RayClass parentClass;

	List<RayVar> parameterList = new ArrayList<RayVar>();

	HashMap<String, RayVar> closureVariables;

	String returnType;

	TokenList code;

	public RayMethod(RayClass parentClass, String name) {
		RayUtils.assertNotNull(parentClass);
		this.name = name;
		this.parentClass = parentClass;

		if (!name.equals("#"))
			parentClass.methods.put(name, this);
	}

	private static void populateParameter(RayMethod rm, TokenList parameterList) {
		RayLog.trace.log("parameterList "+parameterList);
		while (parameterList.remaining() >= 2) {
			rm.parameterList.add(new RayVar(Visibility.local_, parameterList.popString(), parameterList.popString()));
			if( parameterList.hasMore())
				parameterList.remove(",");
		}
	}

	public static RayMethod parseClosure(RayClass parentClass, HashMap<String, RayVar> closureVariables, TokenList tokenList) {
		RayMethod rm = new RayMethod(parentClass, "#");
		rm.returnType = KeyWords.VOID;

		rm.code = tokenList.getInnerList('{', '}');
		if (rm.code.contains("->")) {
			TokenList parameterList = rm.code.getAndRemoveSourceTokenUntil("->", false);
			//parameterList.removeLast("->");

			rm.code.hideCodeBeforePosAndResetPos();
			populateParameter(rm, parameterList);
		}
		return rm;
	}

	public static RayMethod parse(RayClass parentClass, String returnType, String name, TokenList tokenList) {

		RayMethod rm = new RayMethod(parentClass, name);
		rm.returnType = returnType;

		TokenList parameterList = tokenList.getInnerList('(', ')');
		populateParameter(rm, parameterList);

		tokenList.remove("{");

		rm.code = tokenList.getInnerList('{', '}');

		return rm;
	}

	public RayClassInterface invoke(RayClassInterface instance, RayClassInterface... parameter) {

		RayClass rayClass = (RayClass) instance;

		RayLog.debug.log("RayMethod.invoke instance: " + name + " " + instance);

		HashMap<String, RayVar> variables = new HashMap<String, RayVar>();
		for (RayVar parameterRayVar : parameterList) {
			RayVar rayVarForVariables = parameterRayVar.copy();
			rayVarForVariables.setValue(parameter[0]);
			variables.put(parameterRayVar.getName(), rayVarForVariables);
		}

		TokenList tokenList = code.copy();
		
		while (true) {
			
			if (tokenList.isEmpty())
				break;
			else {
				if (false && tokenList.startsWithPattern("ii=") ) {
					RayLog.info.log("RayMethod: "+"variable decl. and assignment found: " + tokenList.subList(0,3));
					String mytypeName = tokenList.pop().s();
					String myname = tokenList.pop().s();
					tokenList.pop(); // '='
					//evaluateExpression(tokenList);
					
				} 
				
				if (tokenList.startsWithPattern("ii=v;")) {

					String mytypeName = tokenList.popString();
					String myname = tokenList.popString();
					tokenList.remove("=");
					Token value = tokenList.pop();
					tokenList.remove(";");
					//RayClassInterface mytype = rayClass.rayLang.getClass( mytypeName);
					RayClassInterface ri = null;
					if (value.isDigit()) {
						ri = new RayInteger(Long.parseLong(value.s()));
					} else if (value.isQuote()) {
						ri = new RayString(value.s().substring(1, value.length() - 1)); // TODO: extract to method
					}
					RayVar rv = new RayVar(Visibility.private_, mytypeName, myname);
					rv.setValue(ri);
					variables.put(rv.getName(), rv);

				} else if (tokenList.startsWithPattern("ii=ii(")) {
					//variable decl. and new assignment
					
					String varType = tokenList.popString();
					String varName = tokenList.popString();
					tokenList.remove("=");
					tokenList.remove(KeyWords.NEW);
					String instanceType = tokenList.popString();
					tokenList.remove("(");

					TokenList parameter2 = tokenList.getInnerList('(', ')');
					List<RayClassInterface> params = evaluateParams(variables, parameter2);

					RayVar rayVar = makeARayVar(rayClass, varType, varName, instanceType, params);

					variables.put(varName, rayVar);

					tokenList.remove(";");

				} else if (tokenList.startsWithPattern("i.i(")) {
					// method invocation
					RayLog.debug.log("RayMethod: method invocation found: " + tokenList.subList(0, 6));

					String varName = tokenList.popString();
					tokenList.remove(".");
					String methodName = tokenList.popString();
					tokenList.remove("(");
					
					TokenList paramTokenList = tokenList.getInnerList('(', ')');
					RayLog.debug.log("RayMethod: "+paramTokenList);
					
					tokenList.remove(";");
					List<RayClassInterface> myparams = evaluateParams(variables, paramTokenList);

					RayVar rayVar = variables.get(varName);
					// check parameter
					if (rayVar == null) {
						rayVar = rayClass.variables.get(varName); // TODO: loop over parents ?
					}
					if (rayVar == null) {
						RayLog.error.log("RayMethod: "+"variable not found: " + varName);
					}

					rayVar.getValue().invoke(methodName, null, myparams.toArray(new RayClassInterface[0]));

				} else if (tokenList.startsWithPattern("ii=ii;")) {
					// Arrays 
					String newVarType = tokenList.popString();
					String newVarName = tokenList.popString();
					tokenList.remove("=");
					tokenList.remove(KeyWords.NEW);
					String instanceType = tokenList.popString();
					tokenList.remove(";");

					RayArray ra = new RayArray(instanceType);

					RayVar rayVar = new RayVar(Visibility.protected_, newVarType, newVarName);
					rayVar.setValue(ra);

					variables.put(newVarName, rayVar);
				} else if (tokenList.startsWithPattern("i.i{")) {
					// closure found

					String varName = tokenList.popString();
					tokenList.remove(".");
					String methodName = tokenList.popString();
					tokenList.remove("{");

					RayVar rayVar = variables.get(varName);
					// check parameter
					if (rayVar == null) {
						rayVar = rayClass.variables.get(varName); // TODO: loop over parents ?
					}
					if (rayVar == null) {
						RayLog.error.log("RayMethod: "+"variable not found: " + varName);
					}

					RayMethod rm = RayMethod.parseClosure(parentClass, variables, tokenList);
					rayVar.getValue().invoke(methodName, rm);

				} else if (tokenList.startsWithPattern("ii=i.i(")) {

					String newVarType = tokenList.popString();
					String newVarName = tokenList.popString();
					tokenList.remove("=");
					String existingVarName = tokenList.popString();
					tokenList.remove(".");
					String methodName = tokenList.popString();
					tokenList.remove("(");
					TokenList paramTokenList = tokenList.getInnerList('(', ')');
					tokenList.remove(";");
					List<RayClassInterface> myparams = evaluateParams(variables, paramTokenList);

					RayVar rayVar = variables.get(existingVarName);
					// check parameter
					if (rayVar == null) {
						rayVar = rayClass.variables.get(existingVarName); // TODO: loop over parents ?
					}
					if (rayVar == null) {
						RayLog.error.log("RayMethod: "+"variable not found: " + existingVarName);
					}

					RayVar rv = new RayVar(Visibility.private_, newVarType, newVarName);
					rv.setValue(rayVar.getValue().invoke(methodName, null, myparams.toArray(new RayClassInterface[0])));
					variables.put(rv.getName(), rv);
				} else {
					RayLog.warn.log("RayMethod: "+"unknown code in line: " + tokenList);
					System.exit(0);
				}

			}
		}
		return null;
	}

	private RayVar makeARayVar(RayClass rayClass, String varType, String varName, String instanceType, List<RayClassInterface> params) {
		RayClassInterface varTypeClass = rayClass.rayLang.getClass(varType);
		if (varTypeClass == null)
			RayUtils.runtimeExcp(varType + " not found");

		RayClassInterface instanceTypeClass = rayClass.rayLang.getClass(instanceType);

		RayUtils.assert_(instanceTypeClass == varTypeClass, instanceTypeClass + " != " + varTypeClass); // check for inhertiance ? // TODO: check using equals ?

		Visibility v = Visibility.protected_;
		RayVar rayVar = new RayVar(v, varType, varName);
		rayVar.setValue(instanceTypeClass.getNewInstance(params));
		return rayVar;
	}

	protected static List<RayClassInterface> evaluateParams(HashMap<String, RayVar> variables, TokenList paramTokenList) {
		List<RayClassInterface> myparams = RayUtils.newArrayList();
		for (int i = 0; i < paramTokenList.remaining(); i++) {
			if (paramTokenList.get(i).isQuote()) {
				String value = paramTokenList.get(i).s();
				RayClassInterface ri = new RayString(value.substring(1, value.length() - 1));
				myparams.add(ri);
			} else if (paramTokenList.get(i).isDigit()) {
				String v = paramTokenList.get(i).s();
				RayClassInterface ri = new RayInteger(Long.parseLong(v));
				myparams.add(ri);

			} else if (paramTokenList.get(i).isIdentifier()) {
				RayVar rayVar2 = variables.get(paramTokenList.get(i).s());
				myparams.add(rayVar2.getValue());
			} else {

				RayLog.error.log("RayMethod: "+"unknown code in param line: " + paramTokenList);
			}
		}
		return myparams;
	}

	@Override
	public String toString() {
		return /*returnType + " " + */parentClass + "." + name;
	}

	public RayClass getParentClass() {
		return parentClass;
	}

	public TokenList getCode() {
		return code;
	}

	public List<RayVar> getParameterList() {
		return parameterList;
	}

}
