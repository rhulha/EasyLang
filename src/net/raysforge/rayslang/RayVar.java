package net.raysforge.rayslang;

public class RayVar {

	Visibility visibility;
	RayClass type;
	String name;
	String value;
	
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
	
	@Override
	public String toString() {
		return value;
	}

	
}
