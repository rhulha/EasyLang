package net.raysforge.rayslang;


public interface RayClassInterface {

	public String getName();
	public RayClassInterface invoke( String methodName, RayClassInterface... parameter);
	public RayClassInterface getNewInstance();

}
