package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.raysforge.rayslang.def.RayInteger;
import net.raysforge.rayslang.def.RayString;

public class RayMethod {

	protected String name;

	protected RayClass parentClass;

	List<RayVar> parameter = new ArrayList<RayVar>();

	String returnType;

	RaySource code;

	public RayMethod(RayClass parentClass, String name) {
		RayUtils.assertNotNull(parentClass);
		this.name = name;
		this.parentClass = parentClass;

		parentClass.methods.put(name, this);
	}

	public static RayMethod parse(RayClass parentClass, String returnType, String name, RaySource rs) {
		
		RayMethod rm = new RayMethod(parentClass, name);
		rm.returnType = returnType;

		RaySource parameter = rs.getInnerText('(', ')');
		RayLog.trace("parameter: " + parameter);

		Token token = rs.getSourceToken();
		if (!token.isOpenBrace())
			RayUtils.runtimeExcp("missing { " + rs.pos);

		rm.code = rs.getInnerText('{', '}');

		return rm;
	}

	public RayClassInterface invoke(RayClassInterface instance, RayClassInterface... parameter) {
		
		RayClass rayClass = (RayClass) instance;

		RayLog.debug("RayMethod.invoke instance: " + name + " " + instance);

		//System.out.println(name + " " + instance);

		HashMap<String, RayVar> variables = new HashMap<String, RayVar>();

		RaySource rs = new RaySource(code.src.clone());
		while (true) {
			TokenList tokenList = rs.getSourceTokenUntil(";", "(");
			if (tokenList.isEmpty())
				break;
			else {
				if (tokenList.equalsPattern("ii=v;")) {
					RayLog.trace("variable decl. and assignment found: " + tokenList);

					String mytypeName = tokenList.get(0).s();
					String myname = tokenList.get(1).s();
					Token value = tokenList.get(3);
					//RayClassInterface mytype = rayClass.rayLang.getClass( mytypeName);
					RayClassInterface ri = null;
					if (value.isDigit()) {
						ri = new RayInteger(Long.parseLong(value.s()));
					} else if (value.isQuote()) {
						ri = new RayString(value.s().substring(1, value.length() - 1)); // TODO: extract to method
					}
					RayVar rv = new RayVar(Visibility.private_, mytypeName, myname);
					rv.setValue(ri);
					variables.put(rv.name, rv);

				} else if (tokenList.equalsPattern("ii=ii(")) {
					
					String varType = tokenList.get(0).s();
					String varName = tokenList.get(1).s();
					RayUtils.assert_(tokenList.get(3).equals(KeyWord.NEW.getLocalText()), tokenList.get(3).s() + " != " + KeyWord.NEW.getLocalText());
					String instanceType = tokenList.get(4).s();

					RaySource parameter2 = rs.getInnerText('(', ')');
					List<RayClassInterface> params = tokenListToParams( variables, parameter2.getSourceTokenUntil());

					RayVar rayVar = makeARayVar(rayClass, varType, varName, instanceType, params);
					
					variables.put(varName, rayVar);
					
					//RayUtils.assert_(rs.getSourceToken().isClosedParentheses(), " missing: )");
					RayUtils.assert_(rs.getSourceToken().isSemicolon(), " missing: ;");
					
				} else if (tokenList.equalsPattern("i.i(")) {
					RayLog.trace("message invocation found: " + tokenList);

					String varName = tokenList.get(0).s();
					String methodName = tokenList.get(2).s();
					RaySource paramSrc = rs.getInnerText('(', ')');
					RayUtils.assert_(rs.getSourceToken().isSemicolon(), " missing: ;");
					TokenList paramTokenList = paramSrc.getSourceTokenUntil();
					List<RayClassInterface> myparams = tokenListToParams(variables, paramTokenList);

					RayVar rayVar = variables.get(varName);
					// check parameter
					if (rayVar == null) {
						rayVar = rayClass.variables.get(varName); // TODO: loop over parents ?
					}
					if (rayVar == null) {
						System.out.println("variable not found: " + varName);
					}

					rayVar.getValue().invoke(methodName, myparams.toArray(new RayClassInterface[0]));

				} else if (tokenList.equalsPattern("ii=i.i(")) {
					String newVarType = tokenList.get(0).s();
					String newVarName = tokenList.get(1).s();
					String existingVarName = tokenList.get(3).s();
					String methodName = tokenList.get(5).s();
					RaySource paramSrc = rs.getInnerText('(', ')');
					RayUtils.assert_(rs.getSourceToken().isSemicolon(), " missing: ;");
					TokenList paramTokenList = paramSrc.getSourceTokenUntil();
					List<RayClassInterface> myparams = tokenListToParams(variables, paramTokenList);

					RayVar rayVar = variables.get(existingVarName);
					// check parameter
					if (rayVar == null) {
						rayVar = rayClass.variables.get(existingVarName); // TODO: loop over parents ?
					}
					if (rayVar == null) {
						System.out.println("variable not found: " + existingVarName);
					}
					
					RayVar rv = new RayVar(Visibility.private_, newVarType, newVarName);
					rv.setValue(rayVar.getValue().invoke(methodName, myparams.toArray(new RayClassInterface[0])));
					variables.put(rv.name, rv);
				} else {
					RayLog.warn("unknown code in line: " + tokenList);
				}

			}
		}
		return null;
	}

	private RayVar makeARayVar(RayClass rayClass, String varType, String varName, String instanceType, List<RayClassInterface> params) {
		RayClassInterface varTypeClass = rayClass.rayLang.getClass( varType);
		if (varTypeClass == null)
			RayUtils.runtimeExcp(varType + " not found");
		
		RayClassInterface instanceTypeClass = rayClass.rayLang.getClass( instanceType);
		
		RayUtils.assert_(instanceTypeClass == varTypeClass, instanceTypeClass + " != " + varTypeClass); // check for inhertiance ? // TODO: check using equals ?
		
		
		Visibility v = Visibility.protected_;
		RayVar rayVar = new RayVar(v, varType, varName);
		rayVar.setValue(instanceTypeClass.getNewInstance(params));
		return rayVar;
	}

	private List<RayClassInterface> tokenListToParams(HashMap<String, RayVar> variables, TokenList paramTokenList) {
		List<RayClassInterface> myparams = RayUtils.newArrayList();
		for (int i = 0; i < paramTokenList.size(); i++) {
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

				RayLog.warn("unknown code in line: " + paramTokenList);
			}
		}
		return myparams;
	}

    @Override
	public String toString() {
		return /*returnType + " " + */parentClass + "." + name;
	}

}
