package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.raysforge.rayslang.def.RayArray;

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
		RayLog.trace.log("parameterList " + parameterList);
		while (parameterList.remaining() >= 2) {
			rm.parameterList.add(new RayVar(Visibility.local_, parameterList.popString(), parameterList.popString()));
			if (parameterList.hasMore())
				parameterList.remove(",");
		}
	}

	public static RayMethod parseClosure(RayClass parentClass, HashMap<String, RayVar> closureVariables, TokenList tokenList) {
		RayMethod rm = new RayMethod(parentClass, "#");
		rm.returnType = KeyWords.VOID;

		rm.code = tokenList.getSubList('{', '}');
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

		TokenList parameterList = tokenList.getSubList('(', ')');
		populateParameter(rm, parameterList);

		tokenList.remove("{");

		rm.code = tokenList.getSubList('{', '}');

		return rm;
	}

	public RayClassInterface invoke(RayClassInterface instance, List<RayClassInterface> parameter) {

		RayClass rayClass = (RayClass) instance;

		RayLog.debug.log("RayMethod.invoke instance: " + name + " " + instance);

		HashMap<String, RayVar> variables = new HashMap<String, RayVar>();
		for (RayVar parameterRayVar : parameterList) {
			RayVar rayVarForVariables = parameterRayVar.copy();
			rayVarForVariables.setValue(parameter.get(0));
			variables.put(parameterRayVar.getName(), rayVarForVariables);
		}

		TokenList tokenList = code.copy();

		while (true) {

			if (tokenList.isEmpty())
				break;
			else {
				if (tokenList.get(0).equals('$')) {
					tokenList.remove("$");

					RayLog.info.log("RayMethod: " + "variable decl. and assignment found: " + tokenList.getSubList(0, 3));
					//String mytypeName = tokenList.pop().s();
					//String myname = tokenList.pop().s();
					//tokenList.pop(); // '='
					evaluateExpression( parentClass, variables, tokenList);

				}

				if (tokenList.startsWithPattern("ii=v;")) {

					String mytypeName = tokenList.popString();
					String myname = tokenList.popString();
					tokenList.remove("=");
					RayClassInterface eval = evaluateExpression(rayClass, variables, tokenList);
					tokenList.remove(";");
					//RayClassInterface mytype = rayClass.rayLang.getClass( mytypeName);
					RayVar rv = new RayVar(Visibility.private_, mytypeName, myname);
					rv.setValue(eval);
					variables.put(rv.getName(), rv);

				} else if (tokenList.startsWithPattern("ii=ii(")) {
					//variable decl. and new assignment

					String varType = tokenList.popString();
					String varName = tokenList.popString();
					tokenList.remove("=");
					tokenList.remove(KeyWords.NEW);
					String instanceType = tokenList.popString();
					tokenList.remove("(");

					TokenList parameter2 = tokenList.getSubList('(', ')');
					List<RayClassInterface> params = evaluateParams( parentClass, variables, parameter2);

					RayVar rayVar = makeARayVar(rayClass, varType, varName, instanceType, params);

					variables.put(varName, rayVar);

					tokenList.remove(";");

				} else if (tokenList.startsWithPattern("i.i(")) {
					// method invocation
					RayLog.debug.log("RayMethod: method invocation found: " + tokenList.getSubList(0, 6));
					
					evaluateExpression(rayClass, variables, tokenList);
					tokenList.remove(";");
					
					/*
					String varName = tokenList.popString();
					tokenList.remove(".");
					String methodName = tokenList.popString();
					tokenList.remove("(");

					TokenList paramTokenList = tokenList.getSubList('(', ')');
					RayLog.debug.log("RayMethod: " + paramTokenList);

					tokenList.remove(";");
					List<RayClassInterface> myparams = evaluateParams( parentClass, variables, paramTokenList);

					RayVar rayVar = variables.get(varName);
					// check parameter
					if (rayVar == null) {
						rayVar = rayClass.variables.get(varName); // TODO: loop over parents ?
					}
					if (rayVar == null) {
						RayLog.error.log("RayMethod: " + "variable not found: " + varName);
					}

					rayVar.getValue().invoke(methodName, null, myparams);
					*/
					

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
						RayLog.error.log("RayMethod: " + "variable not found: " + varName);
					}

					RayMethod rm = RayMethod.parseClosure(parentClass, variables, tokenList);
					rayVar.getValue().invoke(methodName, rm, null);

				} else if (tokenList.startsWithPattern("ii=i.i(")) {

					String newVarType = tokenList.popString();
					String newVarName = tokenList.popString();
					tokenList.remove("=");
					
					/*
					String existingVarName = tokenList.popString();
					tokenList.remove(".");
					String methodName = tokenList.popString();
					tokenList.remove("(");
					TokenList paramTokenList = tokenList.getSubList('(', ')');
					tokenList.remove(";");
					List<RayClassInterface> myparams = evaluateParams( parentClass, variables, paramTokenList);

					RayVar rayVar = variables.get(existingVarName);
					// check parameter
					if (rayVar == null) {
						rayVar = rayClass.variables.get(existingVarName); // TODO: loop over parents ?
					}
					if (rayVar == null) {
						RayLog.error.log("RayMethod: " + "variable not found: " + existingVarName);
					}
					RayVar rv = new RayVar(Visibility.private_, newVarType, newVarName);
					rv.setValue(rayVar.getValue().invoke(methodName, null, myparams));
					variables.put(rv.getName(), rv);
					*/

					RayVar rv = new RayVar(Visibility.private_, newVarType, newVarName);
					rv.setValue(evaluateExpression(rayClass, variables, tokenList));
					variables.put(rv.getName(), rv);
					tokenList.remove(";");
				} else {
					RayLog.warn.log("RayMethod: " + "unknown code in line: " + tokenList);
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

	private static RayClassInterface evaluateExpression( RayClass parentClass, HashMap<String, RayVar> variables, TokenList tokenList) {

		RayLog.trace.log("ee: " + tokenList);
		
		RayClassInterface value;

		if (tokenList.startsWithPattern("i")) {
			String varName = tokenList.popString();
			RayVar rayVar = variables.get(varName);
			if (rayVar == null)
				RayUtils.runtimeExcp("unknown var: " + varName);
			value = rayVar.getValue();
		} else if (tokenList.startsWithPattern("v")) {
			value = tokenList.pop().getValue();
		} else {
			System.err.println("arg678");
			value = null;
		}

		while (tokenList.startsWithPattern(".")) {
			tokenList.remove(".");
			String methodName = tokenList.popString();
			List<RayClassInterface> evaluatedParams = null;
			if (tokenList.startsWithPattern("(")) {
				tokenList.remove("(");
				TokenList parameter = tokenList.getSubList('(', ')');
				evaluatedParams = evaluateParams( parentClass, variables, parameter);
			}
			RayMethod rm = null;
			if (tokenList.startsWithPattern("{")) {
				tokenList.remove("{");
				rm = RayMethod.parseClosure( parentClass, variables, tokenList);
			}
			value = value.invoke(methodName, rm, evaluatedParams); // this was the last line that I changed before it all worked magically !!! woot !
		}
		
		return value;
	}

	protected static List<RayClassInterface> evaluateParams( RayClass parentClass, HashMap<String, RayVar> variables, TokenList paramTokenList) {
		List<RayClassInterface> evaluatedParams = RayUtils.newArrayList();

		RayLog.trace.log("ep: " + paramTokenList);
		
		while (paramTokenList.remaining() > 0) {
			
			RayClassInterface evaluatedExpr = evaluateExpression( parentClass, variables, paramTokenList);
			evaluatedParams.add(evaluatedExpr);
			if (paramTokenList.hasMore())
				paramTokenList.remove(",");
		}

		return evaluatedParams;
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
