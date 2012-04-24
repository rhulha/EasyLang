package net.raysforge.rayslang.ide;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import net.raysforge.easyswing.EasySplitPane;
import net.raysforge.easyswing.EasySwing;
import net.raysforge.easyswing.EasyTextArea;
import net.raysforge.easyswing.EasyTree;
import net.raysforge.rayslang.FileUtils;
import net.raysforge.rayslang.Output;
import net.raysforge.rayslang.RayClass;
import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RaySource;
import net.raysforge.rayslang.RayUtils;

public class Rlide implements ActionListener, Output {

	private EasySwing es;
	private EasyTextArea textArea;
	private EasyTextArea console;
	private JButton addToolBarItem;
	private RayLang rayLang;
	private EasyTree easyTree;
	private final File projectsHome;

	public Rlide(File projectsHome) {

		this.projectsHome = projectsHome;
		ResourceBundle rb = ResourceBundle.getBundle("net.raysforge.rayslang.ide.Rlide");

		es = new EasySwing("RayLang IDE", 800, 600);
		EasySplitPane codeAndConsole = new EasySplitPane(false, 400);
		EasySplitPane treeAndSplitPane = new EasySplitPane(true, 100);
		easyTree = new EasyTree();
		easyTree.getJTree().setShowsRootHandles(false);
		
		traverse( easyTree.getRootNode(), projectsHome);
		
		//easyTree.getJTree().setRootVisible(false);
		treeAndSplitPane.setLeft(easyTree);
		treeAndSplitPane.setRight(codeAndConsole.getSplitPane());
		codeAndConsole.setTop(textArea = new EasyTextArea());
		codeAndConsole.setBottom(console = new EasyTextArea());

		es.getContentPane().add(treeAndSplitPane.getSplitPane(), BorderLayout.CENTER);

		JMenu fileMenuItem = es.addMenuItem(rb.getString("MenuItemFile"));
		es.addMenuItem(fileMenuItem, rb.getString("MenuItemNewProject"), "newProject", this);
		addToolBarItem = es.addToolBarItem("LOS");
		addToolBarItem.addActionListener(this);
		addToolBarItem.setActionCommand("run");

		char[] completeFile = FileUtils.readCompleteFile(new File("raysrc/test/SimpleTest.ray"));
		textArea.appendString(new String(completeFile));
	}

	private void traverse(DefaultMutableTreeNode treeNode, File directory) {
		File[] fileList = directory.listFiles();
		for (File file : fileList) {
			DefaultMutableTreeNode newNode = easyTree.addNode(file.getName(), treeNode);
			if( file.isDirectory())
			{
				traverse( newNode, file);
			} else {
				
			}
		}
		
	}

	private void start() {
		rayLang = new RayLang();
		rayLang.setOutput(this);
		// rayLang.parse(new File("raysrc"));
		es.show();
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

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("run")) {
			RayLang.instance.unregisterClasses("test");
			RayClass.parse("test", RayUtils.convertSourceToTokenList(new RaySource(textArea.getTextArea().getText().toCharArray())));
			RayLang.runClass(rayLang.getClass("test"));
		} else if (e.getActionCommand().equals("newProject")) {
			System.out.println(e.getActionCommand());
			easyTree.addNode("Test");
		}

	}

	@Override
	public void writeln(Object o) {
		console.appendString(o + "\n");

	}
}
