package net.raysforge.rayslang.ide;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JMenu;

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

	public Rlide() {

		ResourceBundle rb = ResourceBundle.getBundle("net.raysforge.rayslang.ide.Rlide");
		
		es = new EasySwing("RayLang IDE", 800, 600);
		EasySplitPane codeAndConsole = new EasySplitPane(false, 400);
		EasySplitPane treeAndSplitPane = new EasySplitPane(true, 100);
		easyTree = new EasyTree();
		treeAndSplitPane.setLeft(easyTree);
		treeAndSplitPane.setRight(codeAndConsole.getSplitPane());
		codeAndConsole.setTop(textArea = new EasyTextArea());
		codeAndConsole.setBottom(console = new EasyTextArea());

		es.getContentPane().add( treeAndSplitPane.getSplitPane(), BorderLayout.CENTER);
		
		JMenu fileMenuItem = es.addMenuItem(rb.getString("MenuItemFile"));
		es.addMenuItem( fileMenuItem, rb.getString("MenuItemNewProject"), "newProject", this);
		addToolBarItem = es.addToolBarItem("LOS");
		addToolBarItem.addActionListener(this);
		addToolBarItem.setActionCommand("run");

		char[] completeFile = FileUtils.readCompleteFile(new File("raysrc/test/SimpleTest.ray"));
		textArea.appendString(new String(completeFile));
	}

	private void start() {
		rayLang = new RayLang();
		rayLang.setOutput(this);
		// rayLang.parse(new File("raysrc"));
		es.show();
	}

	public static void main(String[] args) {
		new Rlide().start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if( e.getActionCommand().equals("run"))
		{
			RayLang.instance.unregisterClasses("test");
			RayClass.parse("test", RayUtils.convertSourceToTokenList(new RaySource(textArea.getTextArea().getText().toCharArray())));
			RayLang.runClass(rayLang.getClass("test"));
		} else if( e.getActionCommand().equals("newProject")) {
			System.out.println(e.getActionCommand());
			easyTree.addNode("Test");
		}

	}

	@Override
	public void writeln(Object o) {
		console.appendString( o + "\n");
		
	}
}
