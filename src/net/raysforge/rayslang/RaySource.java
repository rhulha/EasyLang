package net.raysforge.rayslang;

public class RaySource
{
    
	protected int pos= 0;
    protected char src[];

    public RaySource(char[] src) {
    	this.src = src;
	}

	public boolean isLetterOrDigit() {
		return Character.isLetterOrDigit( src[pos]);
	}

	public boolean isLetterOrDigitOrBang() {
		return isLetterOrDigit() || src[pos] == '!';
	}

	public boolean more() {
		return pos < src.length;
	}

    public void eatSpacesAndReturns()
    {
        while (pos < src.length && (isDivider(src[pos])))
            pos++;
    }

    public boolean isDivider()
    {
    	return isDivider(src[pos]);
    }
    
    public static boolean isDivider(char c)
    {
        if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
            return true;
        return false;
    }

    
}
