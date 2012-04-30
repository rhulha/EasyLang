package net.raysforge.rayslang.ide;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.raysforge.easyswing.EasySplitPane;
import net.raysforge.easyswing.EasySwing;
import net.raysforge.easyswing.EasyTextArea;
import net.raysforge.easyswing.EasyTree;
import net.raysforge.rayslang.FileUtils;
import net.raysforge.rayslang.RayClass;
import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RaySource;
import net.raysforge.rayslang.RayUtils;

public class Rlide  {

	private static final String NEW_PROJECT = "newProject";
	private static final String NEW_FILE = "newFile";
	private static final String SAVE_ALL = "saveAll";

	private EasySwing es;
	private EasyTextArea console;
	private JButton addToolBarItem;
	private RayLang rayLang;
	private EasyTree easyTree;
	private final File projectsHome;
	private JTabbedPane tabbedPane;
	private EventDelegator delegator;

	public Rlide(File projectsHome) {

		this.projectsHome = projectsHome;
		ResourceBundle rb = ResourceBundle.getBundle("net.raysforge.rayslang.ide.Rlide");
		
		delegator = new EventDelegator(this);

		es = new EasySwing("RayLang IDE", 800, 600, JFrame.DO_NOTHING_ON_CLOSE);
		es.addWindowListener(delegator);
		
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
		es.addMenuItem(fileMenuItem, rb.getString("MenuItemSaveAll"), SAVE_ALL, delegator);

		addToolBarItem = es.addToolBarItem("LOS");
		addToolBarItem.addActionListener(delegator);
		addToolBarItem.setActionCommand("run");

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
		rayLang = new RayLang();
		rayLang.setOutput(delegator);
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
					tabbedPane.addTab(fileFromTreePath.getName(), null, jTextArea, fileFromTreePath.getPath());
					tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
				}
			}
		}

	}

	public void actionPerformed(ActionEvent e) {

		//System.out.println(e.getActionCommand());

		if (e.getActionCommand().equals("run")) {
			RayLang.instance.unregisterClasses("test");
			JTextArea textArea = (JTextArea) tabbedPane.getSelectedComponent();
			RayClass.parse("test", RayUtils.convertSourceToTokenList(new RaySource(textArea.getText().toCharArray())));
			RayLang.runClass(rayLang.getClass("test"));
		} else if (e.getActionCommand().equals(NEW_PROJECT)) {

			if (new File(projectsHome, "New Project").mkdir())
				easyTree.addNode("New Project");
			else
				JOptionPane.showMessageDialog(null, "'New Project' folder could not be created in: " + projectsHome);

		} else if (e.getActionCommand().equals(SAVE_ALL)) {
			for (int i = 0; i < tabbedPane.getTabCount(); i++) {
				String title = tabbedPane.getTitleAt(i);
				if( title.startsWith("*"))
					tabbedPane.setTitleAt(i, title.substring(1));
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
		new Rlide(projectsHome).start();
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

	public JFrame getJFrame() {
		return es.getFrame();
	}


}
