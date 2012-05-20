package net.raysforge.rayslang.def;

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.commons.Generics;
import net.raysforge.easyswing.EasySwing;
import net.raysforge.easyswing.eventpanel.EventPanel;
import net.raysforge.easyswing.eventpanel.PaintEvent;
import net.raysforge.easyswing.eventpanel.PaintListener;
import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyLang;
import net.raysforge.rayslang.EasyMethod;
import net.raysforge.rayslang.EasyMethodInterface;

public class EasyFrame implements EasyClassInterface, PaintListener, AWTEventListener {

	EasySwing easySwing = new EasySwing("", 800, 600);
	EasyMethod paintClosure;
	EasyMethod keyEventHandler;
	private EventPanel eventPanel;

	public EasyFrame() {
		eventPanel = easySwing.setEventPanelAsMainContent();
		eventPanel.addPaintListener(this);
		easySwing.addGlobalKeyEventListener(this);
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyFrame(); // parameter.get(0).toString()
	}

	static Map<String, EasyMethodInterface> methods = new HashMap<String, EasyMethodInterface>();

	static {
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Frame.setTitle"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyClassInterface p0 = parameter.get(0);
				EasyFrame ef = (EasyFrame) instance;
				ef.easySwing.getFrame().setTitle(p0.toString());
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Frame.draw"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyFrame ef = (EasyFrame) instance;
				if( closure != null)
				{
					ef.paintClosure = closure;
				} else {
					ef.easySwing.getContentPane().repaint();
					ef.eventPanel.repaint();
				}
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Frame.getWidth"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyFrame ef = (EasyFrame) instance;
				return new EasyInteger(ef.easySwing.getContentPane().getWidth());
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Frame.getHeight"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyFrame ef = (EasyFrame) instance;
				return new EasyInteger(ef.easySwing.getContentPane().getHeight());
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Frame.setWidth"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyFrame ef = (EasyFrame) instance;
				EasyInteger p0 = (EasyInteger) parameter.get(0);
				int height = ef.easySwing.getFrame().getHeight();
				ef.easySwing.getFrame().setSize((int) p0.getIntValue(), height);
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Frame.setHeight"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyFrame ef = (EasyFrame) instance;
				EasyInteger p0 = (EasyInteger) parameter.get(0);
				int width = ef.easySwing.getFrame().getWidth();
				ef.easySwing.getFrame().setSize(width, (int) p0.getIntValue());
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Frame.show"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyFrame ef = (EasyFrame) instance;
				ef.easySwing.show();
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Frame.setKeyEventHandler"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				assertClosure(closure);
				EasyFrame ef = (EasyFrame) instance;
				ef.keyEventHandler = closure;
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Frame.addToolBarItem"), null) {
			@Override
			public EasyClassInterface invoke(final EasyClassInterface instance, final EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				assertClosure(closure);
				EasyFrame ef = (EasyFrame) instance;
				String p0 = parameter.get(0).toString();
				ef.easySwing.addToolBarItem(p0, p0, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent ae) {
						closure.invoke(instance, null, null);
					}
				});
				return null;
			}
		});

	}

	private static void add(NativeMethod nativeMethod) {
		methods.put(nativeMethod.getName(), nativeMethod);
	}

	@Override
	public String getName() {
		return EasyLang.rb.getString("Frame");
	}

	@Override
	public boolean paint(PaintEvent pe) {
		if (paintClosure != null) {
			Graphics g = (Graphics) pe.getSource();
			List<EasyClassInterface> p = Generics.newArrayList();
			p.add(new EasyGraphics(g));
			paintClosure.invoke(null, null, p); // TODO: instance ?
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
			List<EasyClassInterface> p = Generics.newArrayList();
			p.add(new EasyInteger(keyEvent.getKeyCode()));
			keyEventHandler.invoke(null, null, p); // TODO: instance ?
		}
	}

	@Override
	public EasyMethodInterface getMethod(String methodName) {
		return methods.get(methodName);
	}

	@Override
	public Map<String, EasyMethodInterface> getMethods() {
		return methods;
	}

}
