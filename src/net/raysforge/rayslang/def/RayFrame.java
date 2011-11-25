package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.easyswing.EasySwing;
import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayLog;
import net.raysforge.rayslang.RayMethod;

public class RayFrame implements RayClassInterface {

	EasySwing es = new EasySwing("", 800, 600);
	
	public RayFrame() {
	}
	
	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayFrame(); // parameter.get(0).toString()
	}

	@Override
	public RayClassInterface invoke( String methodName, RayMethod closure, List<RayClassInterface> parameter) {

		RayLog.debug.log(methodName + " " + parameter + " on " + this);

		if (methodName.equals("setzeTitel") && (parameter.size() == 1)) {
			this.es.getFrame().setTitle(parameter.toString());
		} else if (methodName.equals("setzeBreite") && (parameter.size() == 1)) {
			RayInteger p0 = (RayInteger) parameter.get(0);
			int height = es.getFrame().getHeight();
			this.es.getFrame().setSize((int) p0.getIntValue(), height);
		} else if (methodName.equals("setzeBreite") && (parameter.size() == 1)) {
			RayInteger p0 = (RayInteger) parameter.get(0);
			int width = es.getFrame().getWidth();
			this.es.getFrame().setSize(width, (int) p0.getIntValue());
		} else if (methodName.equals("zeigeAn") && (parameter.size() == 0)) {
			this.es.show();
		}
		return null;
	}

	@Override
	public String getName() {
		return KeyWords.CLASS_FRAME;
	}

}
