package net.raysforge.rayslang;

public class RayVar {

	protected Visibility visibility;
	protected RayClass reference;
	protected String name;
	
	public RayVar( String name, RayClass reference) {
		this( Visibility.local_, name, reference);
	}

	public RayVar(Visibility visibility, String name, RayClass reference) {
		super();
		this.visibility = visibility;
		this.name = name;
		this.reference = reference;
	}
	
	public RayClass getReference() {
		return reference;
	}
	
	public void setReference(RayClass reference) {
		this.reference = reference;
	}
	
	@Override
	public String toString() {
		return name + ": " + reference.toString();
	}

	
}
