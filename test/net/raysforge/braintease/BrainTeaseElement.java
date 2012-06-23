package net.raysforge.braintease;

public class BrainTeaseElement {
	
	private String expr;
	private final boolean correct;
	public Boolean answeredCorrect;

	public BrainTeaseElement(String expr, boolean correct) {
		this.expr = expr;
		this.correct = correct;
	}
	
	public String getExpr() {
		return expr;
	}
	
	public void appendToExpression(String text)
	{
		expr+=" "+text;
	}

	public boolean isCorrect() {
		return correct;
	}
	
	@Override
	public String toString() {
		return expr;
	}

	public void replaceChar(char c, char d) {
		expr = expr.replace(c, d);
		
	}
}
