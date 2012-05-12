package net.raysforge.rayslang.ide;

import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreePath;

import net.raysforge.easyswing.ValueForPathChangedListener;
import net.raysforge.rayslang.Output;

public class EventDelegator implements ActionListener, Output, ValueForPathChangedListener, MouseListener, DocumentListener, WindowListener, AWTEventListener, KeyListener {

	private final EasyIDE easyIDE;

	public EventDelegator(EasyIDE easyIDE) {
		this.easyIDE = easyIDE;
	}

	@Override
	public void windowActivated(WindowEvent we) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent we) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent we) {
		if (easyIDE.checkIfAnyTabIsModified()) {
			int result = JOptionPane.showConfirmDialog(easyIDE.getJFrame(), "You have unsaved changes. Do you really want to exit ?");
			if (result == JOptionPane.YES_OPTION)
			{
				easyIDE.getJFrame().dispose();
			}
		} else {
			easyIDE.getJFrame().dispose();
		}

	}

	@Override
	public void windowDeactivated(WindowEvent we) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent we) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent we) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent we) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changedUpdate(DocumentEvent de) {
		easyIDE.setSelectedTabToModified();
	}

	@Override
	public void insertUpdate(DocumentEvent de) {
		easyIDE.setSelectedTabToModified();
	}

	@Override
	public void removeUpdate(DocumentEvent de) {
		easyIDE.setSelectedTabToModified();
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		easyIDE.mouseClicked(me);

	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

	@Override
	public boolean valueForPathChanged(TreePath path, Object newValue) {
		return easyIDE.valueForPathChanged(path, newValue);
	}

	@Override
	public void writeln(Object o) {
		easyIDE.writeln(o);

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		easyIDE.actionPerformed(ae);

	}

	@Override
	public void eventDispatched(AWTEvent awtEvent) {
		if (awtEvent instanceof KeyEvent) {
			KeyEvent ke = (KeyEvent) awtEvent;
			if( ke.getKeyCode() == 'S' && (ke.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK)  && ke.getID() == KeyEvent.KEY_RELEASED)
				easyIDE.saveSelectedTextArea();
		}
		
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		Object source = ke.getSource();
		if (source instanceof JTextArea) {
			JTextArea textArea = (JTextArea) source;
			if( ke.getKeyChar() == ' ' && ke.getModifiers() == KeyEvent.CTRL_MASK )
			{
				
				Point mcp = textArea.getCaret().getMagicCaretPosition();
				easyIDE.showAutoCompleteBox( textArea, mcp);
			}
		}
		if (source instanceof JList) {
			JList list = (JList) source;
			if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
                final String s = (String) list.getSelectedValue();
                easyIDE.useSelectedListVlaue(s);
            }
		}

	}

	@Override
	public void keyReleased(KeyEvent ke) {
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}

}
