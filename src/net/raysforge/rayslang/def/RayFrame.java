package net.raysforge.rayslang.def;

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
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

public class RayFrame implements RayClassInterface, PaintListener, AWTEventListener {

	EasySwing easySwing = new EasySwing("", 800, 600);
	RayMethod paintClosure;
	RayMethod keyEventHandler;
	private EventPanel eventPanel;

	public RayFrame() {
		eventPanel = easySwing.setEventPanelAsMainContent();
		eventPanel.addPaintListener(this);
		easySwing.addGlobalKeyEventListener(this);
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayFrame(); // parameter.get(0).toString()
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure, List<RayClassInterface> parameter) {

		RayLog.debug.log(methodName + " " + parameter + " on " + this);
		
		int pc = 0;
		if( parameter != null)
			pc = parameter.size();

		if (methodName.equals("setzeTitel") && (pc == 1)) {
			this.easySwing.getFrame().setTitle(parameter.get(0).toString());
		} else if (methodName.equals("male") && (pc == 0) && closure == null) {
			easySwing.getContentPane().repaint();
			eventPanel.repaint();
		} else if (methodName.equals("holeBreite") && (pc == 0)) {
			return new RayInteger(easySwing.getContentPane().getWidth());
		} else if (methodName.equals("holeHöhe") && (pc == 0)) {
			return new RayInteger(easySwing.getContentPane().getHeight());
		} else if (methodName.equals("setzeBreite") && (pc == 1)) {
			RayInteger p0 = (RayInteger) parameter.get(0);
			int height = easySwing.getFrame().getHeight();
			this.easySwing.getFrame().setSize((int) p0.getIntValue(), height);
		} else if (methodName.equals("setzeHöhe") && (pc == 1)) {
			RayInteger p0 = (RayInteger) parameter.get(0);
			int width = easySwing.getFrame().getWidth();
			this.easySwing.getFrame().setSize(width, (int) p0.getIntValue());
		} else if (methodName.equals("zeigeAn") && (pc == 0)) {
			this.easySwing.show();
		} else if (methodName.equals("setzeTastenDruckBehandler") && parameter == null && closure != null) {
			keyEventHandler = closure;
		} else if (methodName.equals("male") && parameter == null && closure != null) {
			paintClosure = closure;
		} else {
			System.out.println("err");
		}
		return null;
	}

	@Override
	public String getName() {
		return KeyWords.CLASS_FRAME;
	}

	@Override
	public boolean paint(PaintEvent pe) {
		if (paintClosure != null) {
			Graphics g = (Graphics) pe.getSource();
			List<RayClassInterface> p = Lists.newArrayList();
			p.add(new RayGraphics(g));
			paintClosure.invoke(paintClosure.getParentClass(), p);
		}
		return false;
	}

	@Override
	public void eventDispatched(AWTEvent event) {
		if (!(event instanceof KeyEvent))
			return;
		KeyEvent keyEvent = (KeyEvent) event;
		if (keyEvent.getID() != KeyEvent.KEY_PRESSED)
			return;
		if (keyEventHandler != null) {
			List<RayClassInterface> p = Lists.newArrayList();
			p.add(new RayInteger(keyEvent.getKeyCode()));
			keyEventHandler.invoke(keyEventHandler.getParentClass(), p);
		}
	}

}
