package net.raysforge.rayslang.ide;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.raysforge.easyswing.EasySplitPane;
import net.raysforge.easyswing.EasySwing;
import net.raysforge.easyswing.EasyTextArea;
import net.raysforge.easyswing.EasyTree;
import net.raysforge.rayslang.EasyLang;
import net.raysforge.rayslang.FileUtils;
import net.raysforge.rayslang.EasyClass;
import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasySource;
import net.raysforge.rayslang.EasyUtils;

public class EasyIDE {

	private static final String NEW_PROJECT = "newProject";
	private static final String NEW_FILE = "newFile";
	private static final String SAVE = "save";
	private static final String SAVE_ALL = "saveAll";
	private static final String RUN = "run";

	private EasySwing es;
	private EasyTextArea console;
	private EasyLang easyLang;
	private EasyTree easyTree;
	private final File projectsHome;
	private JTabbedPane tabbedPane;
	private EventDelegator delegator;
	private JPopupMenu popupMenu = new JPopupMenu();
	private JList list = new JList();

	public EasyIDE(File projectsHome) {

		this.projectsHome = projectsHome;
		ResourceBundle rb = ResourceBundle.getBundle("net.raysforge.rayslang.ide.EasyIDE");

		delegator = new EventDelegator(this);

		es = new EasySwing("EasyLang IDE", 800, 600, JFrame.DO_NOTHING_ON_CLOSE);
		es.addWindowListener(delegator);
		es.addGlobalKeyEventListener(delegator);

		EasySplitPane codeAndConsole = new EasySplitPane(false, 400);
		EasySplitPane treeAndSplitPane = new EasySplitPane(true, 200);
		easyTree = new EasyTree("Projects");
		easyTree.setShowsRootHandles(false);
		easyTree.setEditable();
		easyTree.setValueForPathChangedListener(delegator);

		traverse(easyTree.getRootNode(), projectsHome);

		int row = 0;
		while (row < easyTree.getJTree().getRowCount()) {
			easyTree.getJTree().expandRow(row);
			row++;
		}

		//easyTree.getJTree().setRootVisible(false);
		treeAndSplitPane.setLeft(easyTree);
		treeAndSplitPane.setRight(codeAndConsole.getSplitPane());

		tabbedPane = new JTabbedPane();

		easyTree.getJTree().addMouseListener(delegator);

		codeAndConsole.setTop(tabbedPane);
		codeAndConsole.setBottom(console = new EasyTextArea());

		es.getContentPane().add(treeAndSplitPane.getSplitPane(), BorderLayout.CENTER);

		JMenu fileMenuItem = es.addMenuItem(rb.getString("MenuItemFile"));
		es.addMenuItem(fileMenuItem, rb.getString("MenuItemNewProject"), NEW_PROJECT, delegator);
		es.addMenuItem(fileMenuItem, rb.getString("MenuItemNewFile"), NEW_FILE, delegator);
		es.addMenuItem(fileMenuItem, rb.getString("MenuItemSave"), SAVE, delegator);
		es.addMenuItem(fileMenuItem, rb.getString("MenuItemSaveAll"), SAVE_ALL, delegator);

		es.addToolBarItem(rb.getString("MenuItemNewProject"), NEW_PROJECT, delegator);
		es.addToolBarItem(rb.getString("MenuItemNewFile"), NEW_FILE, delegator);
		es.addToolBarItem(rb.getString("MenuItemSave"), SAVE, delegator);
		es.addToolBarItem(rb.getString("MenuItemSaveAll"), SAVE_ALL, delegator);
		es.addToolBarItem(rb.getString("MenuItemRun"), RUN, delegator);

		list.addKeyListener(delegator);
		
		DefaultListModel defaultListModel = new DefaultListModel();
		list.setModel(defaultListModel);
		defaultListModel.addElement("test2");
		defaultListModel.addElement("test3");
		defaultListModel.addElement("test4");

		popupMenu.setPreferredSize(new Dimension(100, 100));
		popupMenu.add(new JScrollPane(list));

	}

	private void traverse(DefaultMutableTreeNode treeNode, File directory) {
		File[] fileList = directory.listFiles();
		for (File file : fileList) {
			DefaultMutableTreeNode newNode = easyTree.addNode(file.getName(), treeNode);
			if (file.isDirectory()) {
				traverse(newNode, file);
			}
		}
	}

	private void start() {
		easyLang = new EasyLang();
		easyLang.setOutput(delegator);
		// rayLang.parse(new File("raysrc"));
		es.show();
	}

	public void writeln(Object o) {
		console.appendString(o + "\n");

	}

	public boolean valueForPathChanged(TreePath path, Object newValue) {
		File fileFromTreePath = getFileFromTreePath(path);
		boolean renameToSucceeded = fileFromTreePath.renameTo(new File(fileFromTreePath.getParentFile(), newValue.toString()));
		return renameToSucceeded;
	}

	// this method skips the root node
	protected File getFileFromTreePath(TreePath path) {
		File file = projectsHome;

		Object[] path2 = path.getPath();
		for (int i = 1; i < path2.length; i++) {
			Object object = path2[i];
			file = new File(file, object.toString());
		}
		return file;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
			TreePath selectionPath = easyTree.getSelectionPath();
			File fileFromTreePath = getFileFromTreePath(selectionPath);
			if (fileFromTreePath.isFile()) {
				int found = -1;
				for (int i = 0; i < tabbedPane.getTabCount(); i++) {
					String toolTipText = tabbedPane.getToolTipTextAt(i);
					if (toolTipText.equals(fileFromTreePath.getPath())) {
						found = i;
						break;
					}
				}
				if (found >= 0)
					tabbedPane.setSelectedIndex(found);
				else {
					char[] completeFile = FileUtils.readCompleteFile(fileFromTreePath);
					JTextArea jTextArea = new JTextArea(new String(completeFile));
					jTextArea.getDocument().addDocumentListener(delegator);
					jTextArea.addKeyListener(delegator);
					tabbedPane.addTab(fileFromTreePath.getName(), null, jTextArea, fileFromTreePath.getPath());
					tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
				}
			}
		}

	}

	public void actionPerformed(ActionEvent e) {

		//System.out.println(e.getActionCommand());

		if (e.getActionCommand().equals(RUN)) {
			EasyLang.instance.unregisterClasses("test");
			JTextArea textArea = getSelectedTextArea();
			EasyClass.parse("test", EasyUtils.convertSourceToTokenList(new EasySource(textArea.getText().toCharArray())));
			EasyLang.runClass(easyLang.getClass("test"));
		} else if (e.getActionCommand().equals(NEW_PROJECT)) {

			if (new File(projectsHome, "New Project").mkdir())
				easyTree.addNode("New Project");
			else
				JOptionPane.showMessageDialog(null, "'New Project' folder could not be created in: " + projectsHome);

		} else if (e.getActionCommand().equals(SAVE)) {
			saveSelectedTextArea();
		} else if (e.getActionCommand().equals(SAVE_ALL)) {
			for (int i = 0; i < tabbedPane.getTabCount(); i++) {
				String title = tabbedPane.getTitleAt(i);
				if (title.startsWith("*")) {
					FileUtils.writeCompleteFile(getFile(i), getTextArea(i).getText());
					tabbedPane.setTitleAt(i, title.substring(1));
				}
			}
		} else if (e.getActionCommand().equals(NEW_FILE)) {
			if (easyTree.isSelected(1)) {
				File projectFolder = new File(projectsHome, easyTree.getSelectedNode(1).toString());
				try {
					if (new File(projectFolder, "NewFile.easy").createNewFile())
						easyTree.addNode("NewFile.easy", easyTree.getSelectedNode(1));
					else
						JOptionPane.showMessageDialog(null, "'NewFile.easy' could not be created in: " + projectFolder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private JTextArea getSelectedTextArea() {
		return (JTextArea) tabbedPane.getSelectedComponent();
	}

	private JTextArea getTextArea(int index) {
		return (JTextArea) tabbedPane.getTabComponentAt(index);
	}

	private File getFile(int index) {
		return new File(tabbedPane.getToolTipTextAt(index));
	}

	private File getSelectedFile() {
		return new File(tabbedPane.getToolTipTextAt(tabbedPane.getSelectedIndex()));
	}

	public static void main(String[] args) {
		File userHome = new File(System.getProperty("user.home"));
		File projectsHome = new File(userHome, "EasyLangProjects");
		if (projectsHome.exists()) {
			if (!projectsHome.isDirectory()) {
				JOptionPane.showMessageDialog(null, "projectsHome is not a directory: " + projectsHome);
				System.exit(1);
			}
		} else {
			if (!projectsHome.mkdir()) {
				JOptionPane.showMessageDialog(null, "projectsHome could not be created: " + projectsHome);
				System.exit(2);
			}
		}
		new EasyIDE(projectsHome).start();
	}

	public boolean checkIfAnyTabIsModified() {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			if (tabbedPane.getTitleAt(i).startsWith("*"))
				return true;
		}
		return false;
	}

	protected void setSelectedTabToModified() {
		String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		if (!title.startsWith("*"))
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), "*" + title);
	}

	protected void setSelectedTabToSaved() {
		String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		if (title.startsWith("*"))
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), title.substring(1));
	}

	public JFrame getJFrame() {
		return es.getFrame();
	}

	public void saveSelectedTextArea() {
		FileUtils.writeCompleteFile(getSelectedFile(), getSelectedTextArea().getText());
		setSelectedTabToSaved();
	}

	public void showAutoCompleteBox(JTextArea invoker, Point caretPosition) {
		
		EasyLang.instance.unregisterClasses("parse");
		JTextArea textArea = getSelectedTextArea();
		EasyClass.parse("parse", EasyUtils.convertSourceToTokenList(new EasySource(textArea.getText().toCharArray())));
		EasyClassInterface class1 = easyLang.getClass("test");
		//class1.get
		
		
		
		popupMenu.show(invoker, caretPosition.x, caretPosition.y + 16);
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				list.requestFocus();
			}
		});
	}

	public void useSelectedListVlaue(String s) {
		System.out.println(s);
        final int cp = getSelectedTextArea().getCaretPosition();
        try {
			getSelectedTextArea().getDocument().insertString(cp, s, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
        popupMenu.setVisible(false);
		
	}

}
