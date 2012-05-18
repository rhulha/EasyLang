package net.raysforge.rayslang.ide;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

public class SmartSense implements KeyListener {

    private JList jList;
    private O3DJSParser o3dParser;
    private JPopupMenu jPopupMenu;
    private JTextPane jt;

    public SmartSense(JTextPane jt, JList jList, O3DJSParser o3dParser, JPopupMenu jPopupMenu) {
        this.jList = jList;
        this.o3dParser = o3dParser;
        this.jPopupMenu = jPopupMenu;
        this.jt = jt;
    }

    public void keyTyped(KeyEvent e) {
        if (e.getSource() == jList) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                final String s = (String) jList.getSelectedValue();
                final int cp = jt.getCaretPosition();
                new ExceptionConverter() {

                    @Override
                    public void run() throws Exception {
                        jt.getDocument().insertString(cp, s, null);
                    }
                }.start();

                jPopupMenu.setVisible(false);
            }
        }
        if (' ' == e.getKeyChar() && e.getModifiers() == KeyEvent.CTRL_MASK) {
            Point mcp = jt.getCaret().getMagicCaretPosition();
            JSObject myJsObject = o3dParser.getJsObject();
            String identifier = "";
            ArrayList<String> reverseIdentifierNames = new ArrayList<String>();
            try {
                int cp = jt.getCaretPosition();
                while (cp > 0) {
                    char c = jt.getText(--cp, 1).charAt(0);
                    if (c == '.' || cp == 0) {
                        if (cp == 0) {
                            identifier = c + identifier;
                        }
                        if (identifier.length() > 0) {
                            reverseIdentifierNames.add(identifier);
                        }
                        identifier = "";
                    } else if (!Character.isLetter(c) && !Character.isDigit(c)) {
                        if (identifier.length() > 0) {
                            reverseIdentifierNames.add(identifier);
                        }
                        break;
                    } else {
                        identifier = c + identifier;
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(O3DIDEView.class.getName()).log(Level.SEVERE, null, ex);
            }
            Collections.reverse(reverseIdentifierNames);
            for (String id : reverseIdentifierNames) {
                //System.out.println(id+".");
                if (id.length() > 0) {
                    myJsObject = myJsObject.getObject(id);
                }
            }

            DefaultListModel dlm = (DefaultListModel) jList.getModel();
            dlm.clear();
            Set<String> keySet = myJsObject.getChildren().keySet();
            for (String key : keySet) {

                dlm.addElement(key); // TODO: make JList colorful, see DiceWars for example
                // so we can differentiate functions and objects
                // also add the func parameters soon
            }
            if (mcp == null) {
                mcp = new Point(0, 0);
            }
            jPopupMenu.show(jt, mcp.x, mcp.y + 16);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    jList.requestFocus();
                }
            });
        }

    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
