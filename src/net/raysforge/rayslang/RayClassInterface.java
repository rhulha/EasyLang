package net.raysforge.rayslang;


public interface RayClassInterface {

	public String getName();
	public RayInstance invoke( RayInstance... params);
	public RayInstance getNewInstance();

}
