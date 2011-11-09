package net.raysforge.rayslang;

public class RayVar {

	protected Visibility visibility;
	protected RayClass type;
	protected String name;
	private RayInstance instance;

	public RayVar(Visibility visibility, RayClass type, String name, RayInstance instance) {
		super();
		this.visibility = visibility;
		this.type = type;
		this.name = name;
		this.instance = instance;
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

	public RayInstance getInstance() {
		return instance;
	}

	public void setInstance(RayInstance instance) {
		this.instance = instance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RayVar [visibility=" + visibility + ", type=" + type + ", name=" + name + ", instance=" + instance + "]";
	}

	
	

}
