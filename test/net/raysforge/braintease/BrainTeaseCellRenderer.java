package net.raysforge.braintease;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class BrainTeaseCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 6574887255807141877L;

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		BrainTeaseElement bte = (BrainTeaseElement) value;
		if (bte.answeredCorrect != null) {
			if (bte.answeredCorrect) {
				c.setBackground(Color.green);
			} else {
				c.setBackground(Color.red);
			}
		}
		return c;
	}
}