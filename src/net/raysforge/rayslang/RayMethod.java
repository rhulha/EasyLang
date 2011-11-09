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

	RayClass returnType;

	RaySource code;

	private final boolean isNative;

	public RayMethod(RayClass parentClass, String name, boolean isNative) {
		RayUtils.assertNotNull(parentClass);
		this.name = name;
		this.parentClass = parentClass;
		this.isNative = isNative;

		parentClass.methods.put(name, this);
	}

	public static RayMethod parse(RayClass parentClass, RayClass type, String name, RaySource rs) {

		RayMethod rm = new RayMethod(parentClass, name, false);

		RaySource parameter = rs.getInnerText('(', ')');
		RayLog.trace("parameter " + parameter);

		Token token = rs.getSourceToken();
		if (!token.isOpenBrace())
			RayUtils.RunExp("missing { " + rs.pos);

		rm.code = rs.getInnerText('{', '}');

		return rm;
	}

	public RayInstance invoke(RayInstance instance, RayInstance... parameter) {

		RayLog.debug("RayMethod.invoke instance: " + name + " " + instance);

		if (instance.isNative()) {
			return instance.nativeClass.invoke(instance.nativeClass, name, parameter);
		}

		//System.out.println(name + " " + instance);
		RayUtils.assert_(!isNative);

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
					RayClass mytype = instance.type.rayLang.getClass("default", mytypeName);
					RayInstance ri = null;
					if (value.isDigit()) {
						ri = new RayInstance(new RayInteger(Long.parseLong(value.s())));
					} else if (value.isQuote()) {
						ri = new RayInstance(new RayString(value.s().substring(1, value.length() - 2)));
					}
					RayVar rv = new RayVar(Visibility.private_, mytype, myname, ri);

					variables.put(rv.name, rv);

				} else if (tokenList.equalsPattern("i.i(")) {
					RayLog.trace("message invocation found: " + tokenList);

					Token varName = tokenList.get(0);
					Token methodName = tokenList.get(2);
					RayVar rayVar = variables.get(varName.s());
					// check parameter
					if (rayVar == null) {
						rayVar = instance.variables.get(varName.s()); // TODO: loop over parents ?
					}

					RayMethod method = rayVar.type.getMethod(methodName.s());
					if (method == null) {
						RayLog.warn("methodName.s() " + rayVar.getInstance() + " " + methodName.s());
						RayUtils.RunExp("fuu");
					}

					RaySource paramSrc = rs.getInnerText('(', ')');
					TokenList paramTokenList = paramSrc.getSourceTokenUntil();
					RayUtils.assert_(rs.getSourceToken().isSemicolon());

					List<RayInstance> myparams = RayUtils.newArrayList();
					for (int i = 0; i < paramTokenList.size(); i++) {
						if (paramTokenList.get(i).isQuote()) {
							RayLog.warn("unsupported");
						} else if (paramTokenList.get(i).isDigit()) {
							String v = paramTokenList.get(i).s();
							RayInstance ri = new RayInstance(new RayInteger(Long.parseLong(v)));
							myparams.add(ri);

						} else if (paramTokenList.get(i).isIdentifier()) {
							RayVar rayVar2 = variables.get(paramTokenList.get(i).s());
							myparams.add(rayVar2.getInstance());
						} else {

							RayLog.warn("unknown code in line: " + paramTokenList);
						}
					}

					method.invoke(rayVar.getInstance(), myparams.toArray(new RayInstance[0]));

				} else {
					RayLog.warn("unknown code in line: " + tokenList);
				}

			}
		}
		return null;
	}

	@Override
	public String toString() {
		return /*returnType + " " + */parentClass + "." + name;
	}

}
