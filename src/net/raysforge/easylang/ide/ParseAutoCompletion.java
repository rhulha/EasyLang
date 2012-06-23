package net.raysforge.easylang.ide;

import java.util.ArrayList;

import net.raysforge.commons.Generics;
import net.raysforge.easylang.EasySource;
import net.raysforge.easylang.Token;
import net.raysforge.easylang.TokenList;
import net.raysforge.easylang.utils.EasyUtils;

// find root variable: e.g.: myvar.getCoolStuff(myothervar.getOtherStuff()).print();


public class ParseAutoCompletion {

	private final TokenList tokenList;

	public Token rootVar; // e.g.: myvar
	Token partial; // e.g.: getCoo in myvar.getCoo
	public final ArrayList<String> methodChainNames = Generics.newArrayList(); // e.g.: print, getCoolStuff

	public ParseAutoCompletion(TokenList tokenList) {
		this.tokenList = tokenList;
	}


	public void parse() {
		TokenList tokenListReverse = tokenList.reverse();

		Token last = null;

		boolean firstIteration=true;
		
		while (tokenListReverse.hasMore()) {
			Token current = tokenListReverse.pop();
			if (current.isClosedParentheses()) {
				tokenListReverse.getSubList(')', '(');
			} else if (current.isIdentifier()) {
				if(firstIteration)
					partial = current;
				else
					last = current;
			} else if (current.isQuote()) {
				last = current;
			} else if (current.isDot()) {
				if(last != null)
					methodChainNames.add(last.toString());
			} else {
				//System.err.println(current);
				break;
			}
			firstIteration=false;
		}

		rootVar = last == null ? partial : last;

	}

	public static void main(String[] args) {
		
		String text = "yourvar.setCoolStuff(yourothervar.setOtherStuff()).display();\nmyvar.getCoolStuff(myothervar.getOtherStuff()).prin";
		//text = "\"Hello\".schreib";
		TokenList tokenList = EasyUtils.convertSourceToTokenList(new EasySource(text.toCharArray()));
		ParseAutoCompletion pac = new ParseAutoCompletion(tokenList);
		pac.parse();
		pac.debug();
	}


	public void debug() {
		System.out.println("rootVarName: "+rootVar);
		System.out.println("partialString: "+partial);
		System.out.println("methodChainNames: "+methodChainNames);
	}

}
