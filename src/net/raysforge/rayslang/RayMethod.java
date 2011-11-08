package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.raysforge.rayslang.def.RayInteger;

public class RayMethod {

	protected String name;

	protected RayClass parentClass;

	List<RayVar> parameter = new ArrayList<RayVar>();

	RayVar returnType;

	RaySource code;

	private final boolean isNative;

	public RayMethod(RayClass parentClass, String name, boolean isNative) {
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

	public RayClass invoke( RayClass rc, RayClass ... parameter) {
		
		RayLog.debug("RayMethod.invoke this: " + this + ", rc: " + rc);

		if( isNative )
		{
			return parentClass.invokeNative( rc, name, parameter);
		}
		
		
		HashMap<String, RayVar> variables = new HashMap<String, RayVar>();

		RaySource rs = new RaySource(code.src.clone());
		while (true) {
			TokenList tokenList = rs.getSourceTokenUntil(";", "(");
			if (tokenList.isEmpty())
				break;
			else {
				if( tokenList.equalsPattern("ii=v;") )
				{
					RayLog.trace("variable decl. and assignment found: " + tokenList);

					RayClass myrc = parentClass.rayLang.getClass("default", tokenList.get(0)).getNewInstance();
					myrc.setValue( tokenList.get(3).s());
					
					RayVar rv = new RayVar( tokenList.get(1).s(), myrc );

					variables.put( rv.name, rv);
					
				} else if( tokenList.equalsPattern("i.i("))
				{
					RayLog.trace("message invocation found: " + tokenList);
					
					Token varName = tokenList.get(0);
					Token methodName = tokenList.get(2);
					RayVar rayVar = variables.get(varName.s());
					// check parameter
					if( rayVar == null)
					{
						rayVar = parentClass.variables.get(varName.s()); // TODO: loop over parents ?
					}

					RayMethod method = rayVar.reference.getMethod(methodName.s());
					if( method == null)
					{
						System.out.println("methodName.s() " + rayVar.reference +  methodName.s());
						
						RayUtils.RunExp("fuu");
					}
					
					
					RaySource params = rs.getInnerText('(', ')');
					TokenList paramTokenList = params.getSourceTokenUntil();
					RayUtils.assert_(rs.getSourceToken().isSemicolon());
					
					List<RayClass> myparams = RayUtils.newArrayList(); 
					for (int i = 0; i < paramTokenList.size(); i++) {
						if( paramTokenList.get(i).isQuote())
						{
							System.out.println("unsupported");
						} else if (paramTokenList.get(i).isDigit()) {
							String v = paramTokenList.get(i).s();
							RayInteger ri = new RayInteger(parentClass.rayLang);
							ri.setValue(v);
							myparams.add( ri);

						} else {
							RayLog.warn("unknown code in line: " + paramTokenList);
						}
					}

					method.invoke( rayVar.getReference(), myparams.toArray(new RayClass[0]));
					
					
				} else {
					RayLog.warn("unknown code in line: " + tokenList);
				}
					
				
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return /*returnType + " " + */ parentClass + "." + name;
	}

}
