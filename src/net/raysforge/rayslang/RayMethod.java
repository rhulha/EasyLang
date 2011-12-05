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
		rm.closureVariables = closureVariables;
		rm.code = tokenList.getSubList('{', '}');
		TokenList test = rm.code.copy().getAndRemoveSourceTokenUntil( true, ";", "{", "=");
		if (test.contains("->")) {
			TokenList parameterList = rm.code.getAndRemoveSourceTokenUntil(false, "->");
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

		if (RayLog.level == RayLog.trace)
			RayLog.trace.log("RayMethod.invoke instance: " + name + " " + instance + " - " + parameterList + " - " + code);

		HashMap<String, RayVar> variables = new HashMap<String, RayVar>();
		if( closureVariables != null)
			for (RayVar closureRayVar : closureVariables.values()) {
				variables.put(closureRayVar.getName(), closureRayVar);
			}
		for (RayVar parameterRayVar : parameterList) {
			RayVar rayVarForVariables = parameterRayVar.copy();
			rayVarForVariables.setValue(parameter.get(0));
			variables.put(parameterRayVar.getName(), rayVarForVariables);
		}

		TokenList tokenList = code.copy().resetPosition();

		while (true) {

			if (tokenList.isEmpty())
				break;
			else {

				String varTypeName = null;
				String varName = null;
				RayArrayExpr arrayExpr = null;

				if (tokenList.startsWithPattern("ii;") || tokenList.startsWithPattern("i[]i;")) {
					varTypeName = tokenList.popString();
					if (tokenList.startsWithPattern("[]")) {
						tokenList.remove("[");
						tokenList.remove("]");
						varTypeName += "[]";
					}
					varName = tokenList.popString();
					tokenList.remove(";");
					RayVar rv = new RayVar(Visibility.private_, varTypeName, varName);
					RayClassInterface instanceTypeClass = rayClass.rayLang.getClass(varTypeName);
					rv.setValue(instanceTypeClass.getNewInstance(null));
					variables.put(rv.getName(), rv);
					continue;
				}
				if (tokenList.startsWithPattern("ii=") || tokenList.startsWithPattern("i[]i=")) {
					varTypeName = tokenList.popString();
					if (tokenList.startsWithPattern("[]")) {
						tokenList.remove("[");
						tokenList.remove("]");
						varTypeName += "[]";
					}
					varName = tokenList.popString();
					tokenList.remove("=");
				} else if (tokenList.startsWithPattern("i=")) {
					varName = tokenList.popString();
					tokenList.remove("=");
				} else {
					// this next line tries to find out if the next tokens are an array expression on the left hand side of an assignment.
					// it is broken at least for when a closure is used in an expression to generate the array index.
					TokenList preview = tokenList.copy().getAndRemoveSourceTokenUntil( true, ";", "{", "=");

					if ( preview.getLast().equals("=") && preview.contains("[")  ) {
						// this means that there is an array expression on the left hand side of an assignment
						varName = tokenList.popString();
						tokenList.remove("[");
						RayClassInterface arrayIndex = evaluateExpression(rayClass, variables, tokenList.getSubList('[', ']'));
						arrayExpr = mustGetArray(rayClass, variables, varName, arrayIndex);
						tokenList.remove("=");
					}
				}

				RayClassInterface eval = evaluateExpression(rayClass, variables, tokenList);
				// closure don't have an ending semicolon, so we only remove it otherwise.
				if (!tokenList.get(-1).equals("}"))
					tokenList.remove(";");

				if (varTypeName != null) {
					//RayClassInterface mytype = rayClass.rayLang.getClass( mytypeName);
					RayVar rv = new RayVar(Visibility.private_, varTypeName, varName);
					rv.setValue(eval);
					variables.put(rv.getName(), rv);
				} else if (varName != null) {
					if (arrayExpr != null) {
						// TODO: verify that the value is of the correct type !
						arrayExpr.put(eval);
					} else {
						RayVar rayVar = mustGetVariable(parentClass, variables, varName);
						rayVar.setValue(eval);
					}
				}

				/*
				if (tokenList.startsWithPattern("ii=ii;")) {
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
				}
				*/

			}
		}
		return null;
	}

	protected RayVar makeARayVar(RayClass rayClass, String varType, String varName, String instanceType, List<RayClassInterface> params) {
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

	public static RayClassInterface evaluateExpression(RayClass parentClass, HashMap<String, RayVar> variables, TokenList tokenList) {

		RayLog.trace.log("ee: " + tokenList);

		RayClassInterface value;

		if (tokenList.startsWithPattern("v")) {
			value = tokenList.pop().getValue();
		} else if (tokenList.startsWithPattern("i")) {

			String varName = tokenList.popString();

			if (varName.equals(KeyWords.NEW)) {
				if (!tokenList.startsWithPattern("i"))
					RayUtils.runtimeExcp("unknown token after " + KeyWords.NEW + ": " + tokenList.get(0));
				String instanceType = tokenList.popString();
				if (tokenList.startsWithPattern("[]")) {
					tokenList.remove("[");
					tokenList.remove("]");
					value = new RayArray(instanceType+"[]");
				} else {
					tokenList.remove("(");
					TokenList parameter2 = tokenList.getSubList('(', ')');
					List<RayClassInterface> params = evaluateParams(parentClass, variables, parameter2);
					value = parentClass.rayLang.getClass(instanceType).getNewInstance(params);
				}
			} else {
				if (tokenList.startsWithPattern("[")) {
					tokenList.remove("[");
					RayClassInterface arrayIndex = evaluateExpression(parentClass, variables, tokenList.getSubList('[', ']'));
					value = mustGetArray(parentClass, variables, varName, arrayIndex).get();
				} else {
					value = mustGetVariable(parentClass, variables, varName).getValue();
				}
			}
		} else {
			System.err.println("unknown token: " + tokenList);
			value = null;
		}

		while (tokenList.startsWithPattern(".")) {
			tokenList.remove(".");
			String methodName = tokenList.popString();
			List<RayClassInterface> evaluatedParams = null;
			if (tokenList.startsWithPattern("(")) {
				tokenList.remove("(");
				TokenList parameter = tokenList.getSubList('(', ')');
				evaluatedParams = evaluateParams(parentClass, variables, parameter);
			}
			RayMethod rm = null;
			if (tokenList.startsWithPattern("{")) {
				tokenList.remove("{");
				rm = RayMethod.parseClosure(parentClass, variables, tokenList);
			}
			value = value.invoke(methodName, rm, evaluatedParams); // this was the last line that I changed before it all worked magically !!! woot !
		}
		return value;
	}

	private static RayArrayExpr mustGetArray(RayClass parentClass, HashMap<String, RayVar> variables, String varName, RayClassInterface arrayIndex) {
		RayVar rayVar = mustGetVariable(parentClass, variables, varName);
		RayArray ra = (RayArray) rayVar.getValue();
		return new RayArrayExpr(ra, arrayIndex);
	}

	private static RayVar mustGetVariable(RayClass parentClass, HashMap<String, RayVar> variables, String varName) {
		RayVar rayVar = variables.get(varName);
		if (rayVar == null) {
			rayVar = parentClass.variables.get(varName); // TODO: loop over parents ?
		}
		if (rayVar == null) {
			RayLog.error.log("RayMethod: " + "variable not found: " + varName);
			RayUtils.runtimeExcp("unknown var: " + varName);
		}
		return rayVar;
	}

	protected static List<RayClassInterface> evaluateParams(RayClass parentClass, HashMap<String, RayVar> variables, TokenList paramTokenList) {
		List<RayClassInterface> evaluatedParams = RayUtils.newArrayList();

		RayLog.trace.log("ep: " + paramTokenList);

		while (paramTokenList.remaining() > 0) {

			RayClassInterface evaluatedExpr = evaluateExpression(parentClass, variables, paramTokenList);
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
