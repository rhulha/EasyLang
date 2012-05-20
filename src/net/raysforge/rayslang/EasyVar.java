package net.raysforge.rayslang;

import net.raysforge.rayslang.utils.EasyUtils;

public class EasyVar {

	private Visibility visibility;
	private String name;
	private EasyClassInterface value;
	private final String type;

	public EasyVar(String type, String name, EasyClassInterface value) {
		this(Visibility.local_, type, name, value);
	}

	public EasyVar(Visibility visibility, String type, String name) {
		this(visibility, type, name, null);
	}

	public EasyVar(Visibility visibility, String type, String name, EasyClassInterface value) {
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
		return "EasyVar [visibility=" + visibility + ", name=" + name + ", instance=" + value + "]";
	}

	public EasyClassInterface getValue() {
		return value;
	}

	public void setValue(EasyClassInterface value) {
		EasyUtils.assert_(value.getName().equals(type), value.getName() + " != " + type);
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public EasyVar copy() {
		return new EasyVar(visibility, type, name);
	}

}
