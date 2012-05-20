package net.raysforge.rayslang.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.raysforge.rayslang.EasySource;
import net.raysforge.rayslang.Token;
import net.raysforge.rayslang.TokenList;

// gibt zwei arten token zu holen
// 1. vorher aufräumen und dan token holen ( returns und spacer weg danach kommt token )
// 2. token holen ( return ist bei zeilen ohne ; auch eins ) danach mist weg machen
// fazit: da ich ohne ; arbeiten können möchte muss ich 2. nehmen !
// problem: mac/dos/unix returns...
// problem: steht pos auf letztem oder nächstem zeichen ?

public class EasyUtils {
	
	public static TokenList convertSourceToTokenList(File file)
	{
		return convertSourceToTokenList(new EasySource(FileUtils.readCompleteFile(file)));
	}
	
	public static TokenList convertSourceToTokenList(EasySource rs)
	{
		ArrayList<Token> tokens = newArrayList();

		Token t;
		while( (t = rs.getSourceToken()) != null)
		{
			tokens.add(t);
		}
		return new TokenList(tokens);
	}
	

	public static <T> ArrayList<T> newArrayList() {
		return new ArrayList<T>();
	}

	public static <K,V> HashMap<K,V> newHashMap() {
		return new HashMap<K,V>();
	}

	public static void assert_(boolean b) {
		if (!b)
			throw new RuntimeException();
	}

	public static void assert_(boolean b, String msg) {
		if (!b)
			throw new RuntimeException(msg);
	}

	public static void runtimeExcp(String s) {
		throw new RuntimeException(s);
	}

	public static void runtimeExcp(Throwable t) {
		throw new RuntimeException(t);
	}

	public static void assertNotNull(Object o) {
		assert_(o!=null, "object is null");
	}

}
