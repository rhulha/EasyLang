package net.raysforge.easylang.ide;

import java.io.File;
import java.util.HashMap;

import net.raysforge.commons.Generics;
import net.raysforge.easylang.EasyMethod;
import net.raysforge.easylang.TokenList;
import net.raysforge.easylang.Visibility;
import net.raysforge.easylang.utils.EasyLog;
import net.raysforge.easylang.utils.EasyUtils;

public class FindAllVariables {

	public FindAllVariables() {
	}

	HashMap<String, String> vars = Generics.newHashMap();
	
	// if null, show a list of vars in the AutoCompleteBox
	// if filled, show a list of methods in the AutoCompleteBox for this object type
	public String lastObjectType = null;

	public void parse(String className, TokenList tokenList) {
		while (tokenList.remaining() > 0) {

			if (tokenList.startsWithPattern("ii;") || tokenList.startsWithPattern("i[]i;")) {
				parseVar(tokenList, vars);
				tokenList.remove(";");
			} else if (tokenList.startsWithPattern("iii;") || tokenList.startsWithPattern("ii[]i;")) {

				String visibility = tokenList.popString();
				/*Visibility v = */Visibility.valueOf(visibility + "_");
				parseVar(tokenList, vars);
				tokenList.remove(";");
			} else if (tokenList.startsWithPattern("ii=") || tokenList.startsWithPattern("i[]i=")) {
				parseVar(tokenList, vars);
				tokenList.remove("=");
				tokenList.getAndRemoveSourceTokenUntil(false, ";");
			} else if (tokenList.startsWithPattern("iii(")) {
				/* this is a method declaration */

				String visibility = tokenList.popString();
				visibility.hashCode();
				String typeStr = tokenList.popString();
				String methodName = tokenList.popString();
				tokenList.remove("(");

				EasyLog.trace.log("methodName: " + className + "." + methodName);
				EasyMethod em = EasyMethod.parse(className, typeStr, methodName, tokenList);
				TokenList methodCode = em.getCode();
				parse(className, methodCode);
			} else {
				String token = tokenList.popString();
				if( vars.containsKey(token))
					lastObjectType = vars.get(token);
				/*TokenList tokenList2 = */tokenList.getAndRemoveSourceTokenUntil(false, ";", "}");
				//System.out.println(tokenList2);
			}
		}
	}

	public static void main(String[] args) {

		File file = new File("C:\\Coding\\Projekte\\private\\EasyLang\\easysrc\\test\\Sokoban.easy");
		TokenList tokenList = EasyUtils.convertSourceToTokenList(file);

		FindAllVariables fav = new FindAllVariables();
		fav.parse(file.getName(), tokenList);

		System.out.println(fav.vars);
	}

	private void parseVar(TokenList tokenList, HashMap<String, String> vars) {
		String typeStr = tokenList.popString();
		if (tokenList.startsWithPattern("[]")) {
			tokenList.remove("[");
			tokenList.remove("]");
			typeStr += "[]";
		}
		String varName = tokenList.popString();
		vars.put(varName, typeStr);
	}

}
