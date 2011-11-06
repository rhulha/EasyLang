package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		System.out.println("parameter " + parameter);

		Token token = rs.getSourceToken();
		if (!token.isOpenBrace())
			RayUtils.RunExp("missing { " + rs.pos);

		rm.code = rs.getInnerText('{', '}');

		return rm;
	}

	public RayVar invoke( RayVar instance, RayVar ... parameter) {
		
		System.out.println("RayMethod.invoke this: " + this + ", instance: " + instance);

		if( isNative )
		{
			return parentClass.invokeNative( instance, name, parameter);
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
					System.out.println("variable decl. and assignment found: " + tokenList);

					RayClass rc = parentClass.rayLang.getClass("default", tokenList.get(0));
					
					RayVar rv = new RayVar(rc, tokenList.get(1).s(), tokenList.get(3).s());

					variables.put( rv.name, rv);
					
				} else if( tokenList.equalsPattern("i.i("))
				{
					System.out.println("message invocation found: " + tokenList);
					
					Token varName = tokenList.get(0);
					Token methodName = tokenList.get(2);
					RayVar rayVar = variables.get(varName.s());
					RayMethod method = rayVar.type.getMethod(methodName.s());
					RayVar rv = variables.get(varName.s());
					// check parameter
					if( rv == null) 
						rv = parentClass.variables.get(varName.s()); // TODO: loop over parents ?
					
					
					RaySource params = rs.getInnerText('(', ')');
					TokenList paramTokenList = params.getSourceTokenUntil();
					RayUtils.assert_(rs.getSourceToken().isSemicolon());
					
					List<RayVar> myparams = RayUtils.newArrayList(); 
					for (int i = 0; i < paramTokenList.size(); i++) {
						if( paramTokenList.get(i).isValue())
						{
							String v = paramTokenList.get(i).s();
							myparams.add( new RayVar(parentClass, "unknown", v));

						} else {
							System.out.println("unknown code in line: " + paramTokenList);
						}
					}

					method.invoke( rv, myparams.toArray(new RayVar[0]));
					
					
				} else {
					System.out.println("unknown code in line: " + tokenList);
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
