package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.List;

// gibt zwei arten token zu holen
// 1. vorher aufr�umen und dan token holen ( returns und spacer weg danach kommt token )
// 2. token holen ( return ist bei zeilen ohne ; auch eins ) danach mist weg machen
// fazit: da ich ohne ; arbeiten k�nnen m�chte muss ich 2. nehmen !
// problem: mac/dos/unix returns...
// problem: steht pos auf letztem oder n�chstem zeichen ?

/**
        RaySource rsrc = new RaySource();
        rsrc.src = ta.getText().toCharArray();
        int i=0;
        while (true)
        {
            i++;
            RayString rs = RayUtils.getSourceToken(rsrc);
            if (rs == null || i>100)
                break;
            RayLog.log("'"+rs+"'");

        }
 */
public class RayUtils {

	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}

	public static void assert_(boolean b) {
		if (!b)
			throw new RuntimeException();
	}

	public static void RunExp(String s) {
		throw new RuntimeException(s);
	}

	/*
	    private RayString getToken2()
	    {
	        int start, end;
	
	        if (src[pos] == ';') // ende
	        {
	            // kill following ; \r \n ' ';
	            while ((pos < src.length) && ((src[pos] == ' ') || (src[pos] == '\t')))
	                pos++;
	        }
	        else if ((pos < src.length) && ((src[pos] == '\n') || (src[pos] == '\r')))
	        {
	
	        }
	        else if (src[pos] == 'a') // var oder ident
	        {
	            // kill following ; \r \n;
	        }
	        else if (src[pos] == '1') // nr anf
	        {
	            // kill following ; \r \n;
	        }
	
	        while ((pos < src.length) && ((src[pos] == '\n') || (src[pos] == '\r')))
	            pos++;
	        int p= pos;
	        while (true)
	        {
	            if (p >= src.length)
	                if (p == pos)
	                    return null;
	                else
	                {
	                    p--;
	                    break;
	                }
	            if (src[p] == ';')
	                break;
	            p++;
	        }
	        int pp= pos;
	        pos= p + 1;
	        //         RayLog.log( pp + " " + pos);
	        return new RayString(src, pp, p);
	    }
	    */
}
