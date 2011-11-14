package net.raysforge.rayslang.def;

import java.util.Arrays;

import net.raysforge.easyswing.EasySwing;
import net.raysforge.rayslang.RayClass;
import net.raysforge.rayslang.RayInstance;
import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RayLog;
import net.raysforge.rayslang.RayMethod;

public class RayFrame implements NativeClass {

	EasySwing es = new EasySwing("", 800, 600);

	@Override
	public void register(RayLang rayLang) {

		RayClass rc = new RayClass(rayLang, "Fenster");
		rc.nativeClass = this;

		new RayMethod(rc, "setzeTitel", true);
		new RayMethod(rc, "setzeBreite", true);
		new RayMethod(rc, "setzeHöhe", true);
		new RayMethod(rc, "zeigeAn", true);

	}

	@Override
	public NativeClass getNewInstance() {
		return new RayFrame();
	}

	@Override
	public RayInstance invoke(NativeClass nc, String methodName, RayInstance... parameter) {
		RayFrame rayFrame = (RayFrame) nc;

		RayLog.debug(methodName + " " + Arrays.asList(parameter) + " on " + nc);

		if (methodName.equals("setzeTitel") && (parameter.length == 1)) {
			rayFrame.es.getFrame().setTitle(parameter.toString());
		} else if (methodName.equals("setzeBreite") && (parameter.length == 1)) {
			RayInteger p0 = (RayInteger) parameter[0].nativeClass;
			int height = es.getFrame().getHeight();
			rayFrame.es.getFrame().setSize((int) p0.getIntValue(), height);
		} else if (methodName.equals("setzeBreite") && (parameter.length == 1)) {
			RayInteger p0 = (RayInteger) parameter[0].nativeClass;
			int width = es.getFrame().getWidth();
			rayFrame.es.getFrame().setSize(width, (int) p0.getIntValue());
		} else if (methodName.equals("zeigeAn") && (parameter.length == 0)) {
			rayFrame.es.show();
		}
		return null;
	}

}
