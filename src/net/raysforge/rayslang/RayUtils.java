package net.raysforge.rayslang;

// gibt zwei arten token zu holen
// 1. vorher aufräumen und dan token holen ( returns und spacer weg danach kommt token )
// 2. token holen ( return ist bei zeilen ohne ; auch eins ) danach mist weg machen
// fazit: da ich ohne ; arbeiten können möchte muss ich 2. nehmen !
// problem: mac/dos/unix returns...
// problem: steht pos auf letztem oder nächstem zeichen ?

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
            System.out.println("'"+rs+"'");

        }
 */
public class RayUtils
{


    public static String getSourceToken(RaySource rs)
    {
        return getSourceToken(rs, false);
    }

    public static String getSourceToken(RaySource rs, boolean peak)
    {
        rs.eatSpacesAndReturns();
        int start = rs.pos;

        if (!rs.more())
        {
            return null;
        }
        if (rs.isLetterOrDigitOrBang()) // ! like ruby
        {
            while (rs.more() && rs.isLetterOrDigitOrBang() )
                rs.pos++;
        }
        else if (rs.src[rs.pos] == '#') // kommentare ignorieren
        {
            while (rs.pos < rs.src.length && rs.src[rs.pos] != '\n')
                rs.pos++;
            return getSourceToken(rs, peak);
        }
        else if (rs.src[rs.pos] == '"')
        {
            rs.pos++;
            while (rs.pos < rs.src.length && rs.src[rs.pos] != '"')
                rs.pos++;
            if (rs.pos >= rs.src.length)
                throw new RuntimeException("Unclosed String");
            rs.pos++;
        }
        else if (rs.src[rs.pos] == '\'')
        {
            rs.pos++;
            while (rs.pos < rs.src.length && rs.src[rs.pos] != '\'')
                rs.pos++;
            if (rs.src[rs.pos] != '\'')
                throw new RuntimeException("Unclosed String");
            rs.pos++;
        }
        else
            rs.pos++;
        if (peak == true)
        {
            rs.pos = start;
        }
        return new String(rs.src, start, rs.pos-start);
    }

    public static String getToken(RaySource rs)
    {
        rs.eatSpacesAndReturns();
        int a = rs.pos;
        while (rs.pos < rs.src.length)
        {
            if (rs.isDivider())
                break;
            rs.pos++;
        }
        return new String(rs.src, a, rs.pos-a);
    }

    public static void RunExp(String s)
    {
        throw new RuntimeException(s);
    }

    public static void eatSpaces(RaySource rs)
    {
        while (rs.pos < rs.src.length && (rs.src[rs.pos] == ' ' || rs.src[rs.pos] == '\t'))
            rs.pos++;
    }

    // getInnerText
    // holt den inhalt zwischen open und close
    // wobei das erste open bereits von dem aufrufer
    // geholt sein muss.
    public static String getInnerText(RaySource rs, char open, char close)
    {
        StringBuffer code = new StringBuffer();
        int brace_counter = 0;
        while (true)
        {
            if (rs.pos >= rs.src.length)
            {
                return null;
            }
            char c = rs.src[rs.pos];
            if (c == open)
            {
                brace_counter++;
                // code.appendInPlace(parse(rs, hook).src);
                // code.appendInPlace(close);
            }
            if (c == close) //|| src[pos] == close)
            {
                if (brace_counter > 0)
                    brace_counter--;
                else
                {
                    rs.pos++;
                    break;
                }
            }
            code.append(c); // TODO: optimize
            rs.pos++;
        }
        return code.toString();
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
            //         System.out.println( pp + " " + pos);
            return new RayString(src, pp, p);
        }
        */
}
