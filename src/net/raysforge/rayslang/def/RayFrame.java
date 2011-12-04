package net.raysforge.rayslang.def;

import java.awt.Graphics;
import java.util.List;

import net.raysforge.easyswing.EasySwing;
import net.raysforge.easyswing.Lists;
import net.raysforge.easyswing.eventpanel.EventPanel;
import net.raysforge.easyswing.eventpanel.PaintEvent;
import net.raysforge.easyswing.eventpanel.PaintListener;
import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayLog;
import net.raysforge.rayslang.RayMethod;

public class RayFrame implements RayClassInterface, PaintListener {

	EasySwing es = new EasySwing("", 800, 600);
	RayMethod paintClosure;
	
	public RayFrame() {
		EventPanel ep = es.setEventPanelAsMainContent();
		ep.addPaintListener(this);
	}
	
	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayFrame(); // parameter.get(0).toString()
	}

	@Override
	public RayClassInterface invoke( String methodName, RayMethod closure, List<RayClassInterface> parameter) {

		RayLog.debug.log(methodName + " " + parameter + " on " + this);

		if (methodName.equals("setzeTitel") && (parameter.size() == 1)) {
			this.es.getFrame().setTitle(parameter.get(0).toString());
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
		} else if (methodName.equals("male") && parameter == null && closure != null) {
			paintClosure = closure;
		}
		return null;
	}

	@Override
	public String getName() {
		return KeyWords.CLASS_FRAME;
	}

	@Override
	public boolean paint(PaintEvent pe) {
		if( paintClosure != null)
		{
			Graphics g = (Graphics) pe.getSource();
			List<RayClassInterface> p = Lists.newArrayList();
			p.add(new RayGraphics(g));
			paintClosure.invoke(paintClosure.getParentClass(), p);
		}
		return false;
	}

}
