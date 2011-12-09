package net.raysforge.rayslang;

public class RayVar {

	private Visibility visibility;
	private String name;
	private RayClassInterface value;
	private final String type;

	public RayVar(String type, String name, RayClassInterface value) {
		this(Visibility.local_, type, name, value);
	}

	public RayVar(Visibility visibility, String type, String name) {
		this(visibility, type, name, null);
	}

	public RayVar(Visibility visibility, String type, String name, RayClassInterface value) {
		super();
		this.visibility = visibility;
		this.type = type;
		this.name = name;
		if (value != null)
			setValue(value);
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RayVar [visibility=" + visibility + ", name=" + name + ", instance=" + value + "]";
	}

	public RayClassInterface getValue() {
		return value;
	}

	public void setValue(RayClassInterface value) {
		RayUtils.assert_(value.getName().equals(type), value.getName() + " != " + type);
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public RayVar copy() {
		return new RayVar(visibility, type, name);
	}

}
