package net.raysforge.rayslang;

public class RayField {

	protected Visibility visibility;
	protected RayClass type;
	protected String name;

	public RayField(Visibility visibility, RayClass type, String name) {
		super();
		this.visibility = visibility;
		this.type = type;
		this.name = name;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public RayClass getType() {
		return type;
	}

	public void setType(RayClass type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
