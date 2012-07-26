package net.raysforge.braintease;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.raysforge.easyswing.EasyList;
import net.raysforge.easyswing.EasySwing;


// add timer to see how long it takes

public class BrainTease implements AWTEventListener {

	private EasyList list;
	int currentElement=0;
	private ScriptEngine engine;

	public BrainTease() {
		
		ScriptEngineManager mgr = new ScriptEngineManager();
	    engine = mgr.getEngineByName("JavaScript");
	    
		EasySwing es = new EasySwing("BrainTeaser", 800, 600);

		list = new EasyList();
		list.setFontSize(32);
		
		Random rand = new Random();

		String signs = "+-x:";

		for (int i = 0; i < 25; i++) {

			int a = rand.nextInt(9)+1;
			int b = rand.nextInt(9)+1;
			int sign = rand.nextInt(4);
			boolean correct = rand.nextBoolean();
			int result = 0;

			if (sign == 0)
				result = a + b;
			if (sign == 1)
				result = a - b;
			if (sign == 2)
				result = a * b;
			if (sign == 3) {
				result = a * b;
			}

			if (!correct) {
				if (rand.nextBoolean())
					result += rand.nextInt(3) + 1;
				else
					result -= rand.nextInt(3) + 1;
			}
			
			if (sign == 3) {
				int temp = result;
				result = a;
				a = temp;
			}

			list.addElement("" + a + signs.charAt(sign) + b + '=' + result);

		}


		es.add(list);

		es.addGlobalKeyEventListener(this);

		es.show();
	}

	public static void main(String[] args) {

		new BrainTease();

	}

	@Override
	public void eventDispatched(AWTEvent event) {
		if (event instanceof KeyEvent) {
			KeyEvent ke = (KeyEvent) event;
			if (ke.getID() == KeyEvent.KEY_PRESSED) {
				if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
					tip(true);
				if (ke.getKeyCode() == KeyEvent.VK_LEFT)
					tip(false);
			}
		}

	}
	
	public void tip(boolean correctTip) {
		if( list.getListModel().size() <= currentElement)
			return;
		String expr = (String) list.get(currentElement);
		Boolean eval=true;
		try {
			eval = (Boolean) engine.eval(expr.replace("=", "=="));
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		if( ! eval)
		{
			list.set(currentElement, expr.replace('=', '\u2260').replace(':', '/').replace('x', '*'));
		}
		if (eval == correctTip)
		{
			list.setBackgroundColor(currentElement, Color.GREEN);
		} else {
			list.setBackgroundColor(currentElement, Color.RED);
		}
		list.getJList().repaint();
		list.getJList().ensureIndexIsVisible(currentElement++);
		
	}

}
