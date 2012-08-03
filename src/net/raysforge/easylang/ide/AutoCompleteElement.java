package net.raysforge.easylang.ide;

public class AutoCompleteElement {
	
	public final String name;
	public final ElementType type;
	
	public AutoCompleteElement(String name, ElementType type) {
		super();
		this.name = name;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
