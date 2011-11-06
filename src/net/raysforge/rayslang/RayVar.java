package net.raysforge.rayslang;

public class RayVar {

	protected Visibility visibility;
	protected RayClass type;
	protected String name;
	protected String value;
	
	public RayVar(RayClass type, String name, String value) {
		super();
		this.visibility = Visibility.local_;
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public RayVar(Visibility visibility, RayClass type, String name, String value) {
		super();
		this.visibility = visibility;
		this.type = type;
		this.name = name;
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}

	
}
