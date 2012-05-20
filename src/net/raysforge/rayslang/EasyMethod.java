package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.raysforge.rayslang.def.EasyArray;
import net.raysforge.rayslang.utils.EasyLog;
import net.raysforge.rayslang.utils.EasyUtils;

public class EasyMethod implements EasyMethodInterface {

	protected String name;

	private String parentClass;

	List<EasyVar> parameterList = new ArrayList<EasyVar>();

	HashMap<String, EasyVar> closureVariables;

	String returnType;

	TokenList code;

	public EasyMethod( String parentClass, String name) {
		EasyUtils.assertNotNull(parentClass);
		this.name = name;
		this.parentClass = parentClass;

//		if (!name.equals("#"))
//			parentClass.methods.put(name, this);
	}

	private static void populateParameter(EasyMethod rm, TokenList parameterList) {
		EasyLog.trace.log("parameterList " + parameterList);
		while (parameterList.remaining() >= 2) {
			rm.parameterList.add(new EasyVar(Visibility.local_, parameterList.popString(), parameterList.popString()));
			if (parameterList.hasMore())
				parameterList.remove(",");
		}
	}

	public static EasyMethod parseClosure( String parentClass, HashMap<String, EasyVar> closureVariables, TokenList tokenList) {
		EasyMethod rm = new EasyMethod( parentClass, "#");
		rm.returnType = EasyLang.rb.getString("void");
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

	public static EasyMethod parse( String parentClass, String returnType, String name, TokenList tokenList) {

		EasyMethod rm = new EasyMethod(parentClass, name);
		rm.returnType = returnType;

		TokenList parameterList = tokenList.getSubList('(', ')');
		populateParameter(rm, parameterList);

		tokenList.remove("{");

		rm.code = tokenList.getSubList('{', '}');

		return rm;
	}

	public static EasyClassInterface evaluateExpression(EasyClass parentClass, HashMap<String, EasyVar> variables, TokenList tokenList) {

		EasyLog.trace.log("ee: " + tokenList);

		EasyClassInterface value;

		if (tokenList.startsWithPattern("v")) {
			value = tokenList.pop().getValue();
		} else if (tokenList.startsWithPattern("i")) {

			String varName = tokenList.popString();

			if (varName.equals(EasyLang.rb.getString("new"))) {
				if (!tokenList.startsWithPattern("i"))
					EasyUtils.runtimeExcp("unknown token after " + EasyLang.rb.getString("new") + ": " + tokenList.get(0));
				String instanceType = tokenList.popString();
				if (tokenList.startsWithPattern("[]")) {
					tokenList.remove("[");
					tokenList.remove("]");
					value = new EasyArray(instanceType+"[]");
				} else {
					tokenList.remove("(");
					TokenList parameter2 = tokenList.getSubList('(', ')');
					List<EasyClassInterface> params = evaluateParams(parentClass, variables, parameter2);
					value = EasyLang.instance.getClass(instanceType).getNewInstance(params);
				}
			} else {
				if (tokenList.startsWithPattern("[")) {
					tokenList.remove("[");
					EasyClassInterface arrayIndex = evaluateExpression(parentClass, variables, tokenList.getSubList('[', ']'));
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
			List<EasyClassInterface> evaluatedParams = null;
			if (tokenList.startsWithPattern("(")) {
				tokenList.remove("(");
				TokenList parameter = tokenList.getSubList('(', ')');
				evaluatedParams = evaluateParams(parentClass, variables, parameter);
			}
			EasyMethod rm = null;
			if (tokenList.startsWithPattern("{")) {
				tokenList.remove("{");
				rm = EasyMethod.parseClosure(parentClass.getName(), variables, tokenList);
			}
			value = value.getMethod(methodName).invoke( value, rm, evaluatedParams);
		}
		return value;
	}

	private static EasyArrayExpr mustGetArray(EasyClass parentClass, HashMap<String, EasyVar> variables, String varName, EasyClassInterface arrayIndex) {
		EasyVar rayVar = mustGetVariable(parentClass, variables, varName);
		EasyArray ra = (EasyArray) rayVar.getValue();
		return new EasyArrayExpr(ra, arrayIndex);
	}

	private static EasyVar mustGetVariable(EasyClass parentClass, HashMap<String, EasyVar> variables, String varName) {
		EasyVar rayVar = variables.get(varName);
		if (rayVar == null) {
			rayVar = parentClass.variables.get(varName); // TODO: loop over parents ?
		}
		if (rayVar == null) {
			EasyLog.error.log("EasyMethod: " + "variable not found: " + varName);
			EasyUtils.runtimeExcp("unknown var: " + varName);
		}
		return rayVar;
	}

	protected static List<EasyClassInterface> evaluateParams(EasyClass parentClass, HashMap<String, EasyVar> variables, TokenList paramTokenList) {
		List<EasyClassInterface> evaluatedParams = EasyUtils.newArrayList();

		EasyLog.trace.log("ep: " + paramTokenList);

		while (paramTokenList.remaining() > 0) {

			EasyClassInterface evaluatedExpr = evaluateExpression(parentClass, variables, paramTokenList);
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

	public String getParentClass() {
		return parentClass;
	}

	public TokenList getCode() {
		return code;
	}

	public List<EasyVar> getParameterList() {
		return parameterList;
	}

	public EasyMethod copy(EasyClass ri) {
		EasyMethod rm = new EasyMethod(ri.getName(), name);
		rm.parameterList = parameterList;
		rm.returnType = returnType;
		rm.code = code.copy();
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReturnType() {
		return returnType;
	}

	@Override
	public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
		EasyClass instance_ = (EasyClass) instance;
		if (EasyLog.level == EasyLog.trace)
			EasyLog.info.log("EasyMethod.invoke instance: " + name + " " + parentClass + " - " + parameterList + " - " + code);

		HashMap<String, EasyVar> variables = new HashMap<String, EasyVar>();
		variables.put(EasyLang.rb.getString("this"), new EasyVar( parentClass, EasyLang.rb.getString("this"), instance));
		if( closureVariables != null)
			for (EasyVar closureVar : closureVariables.values()) {
				variables.put(closureVar.getName(), closureVar);
			}
		for (EasyVar parameterVar : parameterList) {
			EasyVar VarForVariables = parameterVar.copy();
			VarForVariables.setValue(parameter.get(0));
			variables.put(parameterVar.getName(), VarForVariables);
		}

		TokenList tokenList = code.copy().resetPosition();
		EasyClassInterface eval=null;
		
		while (true) {

			if (tokenList.isEmpty())
				break;

			String varTypeName = null;
			String varName = null;
			EasyArrayExpr arrayExpr = null;

			if (tokenList.startsWithPattern("ii;") || tokenList.startsWithPattern("i[]i;")) {
				varTypeName = tokenList.popString();
				if (tokenList.startsWithPattern("[]")) {
					tokenList.remove("[");
					tokenList.remove("]");
					varTypeName += "[]";
				}
				varName = tokenList.popString();
				tokenList.remove(";");
				EasyVar rv = new EasyVar(Visibility.private_, varTypeName, varName);
				EasyClassInterface instanceTypeClass = EasyLang.instance.getClass(varTypeName);
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
					EasyClassInterface arrayIndex = evaluateExpression( instance_, variables, tokenList.getSubList('[', ']'));
					arrayExpr = mustGetArray( instance_, variables, varName, arrayIndex);
					tokenList.remove("=");
				}
			}

			//debugVariables();
			eval = evaluateExpression( instance_, variables, tokenList);
			// closure don't have an ending semicolon, so we only remove it otherwise.
			if (!tokenList.get(-1).equals("}"))
				tokenList.remove(";");

			if (varTypeName != null) {
				//EasyClassInterface mytype = easyClass.rayLang.getClass( mytypeName);
				EasyVar rv = new EasyVar(Visibility.private_, varTypeName, varName);
				if( eval == null)
					EasyLang.instance.writeln("null value");
				rv.setValue(eval);
				variables.put(rv.getName(), rv);
			} else if (varName != null) {
				if (arrayExpr != null) {
					// TODO: verify that the value is of the correct type !
					arrayExpr.put(eval);
				} else {
					EasyVar rayVar = mustGetVariable( instance_, variables, varName);
					rayVar.setValue(eval);
				}
			}
		} // while
		return eval;
	}

}
