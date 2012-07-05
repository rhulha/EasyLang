package net.raysforge.braintease;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.raysforge.easyswing.EasyList;
import net.raysforge.easyswing.EasySwing;

public class BrainTease implements AWTEventListener, ActionListener {

	private JButton right;
	private JButton wrong;
	private EasyList list;

	public BrainTease() {
		EasySwing es = new EasySwing("BrainTeaser", 800, 600);
		Container cp = es.getContentPane();

		list = new EasyList();
		list.setFontSize(32);
		list.getJList().setCellRenderer(new BrainTeaseCellRenderer());

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

			list.addElement(new BrainTeaseElement("" + a + signs.charAt(sign) + b + '=' + result, correct));

		}

		wrong = new JButton("Wrong");
		right = new JButton("Right");

		JPanel bottom = new JPanel();
		bottom.add(wrong);
		bottom.add(right);

		wrong.addActionListener(this);
		right.addActionListener(this);

		cp.add(list.getScrollPane(), BorderLayout.CENTER);
		cp.add(bottom, BorderLayout.SOUTH);

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
				if (ke.getKeyCode() == KeyEvent.VK_LEFT)
					wrong.doClick();
				if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
					right.doClick();
			}
		}

	}
	
	int currentElement=0;

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean correctTip = e.getSource() == right;
		if( list.getListModel().size() <= currentElement)
			return;
		BrainTeaseElement bte = (BrainTeaseElement) list.get(currentElement++);
		if( ! bte.isCorrect())
		{
			bte.replaceChar('=', '\u2260');
		}
		if (bte.isCorrect() == correctTip)
			bte.answeredCorrect=true;
		else
			bte.answeredCorrect=false;
		list.getJList().repaint();
		list.getJList().ensureIndexIsVisible(currentElement);
		
	}

}
