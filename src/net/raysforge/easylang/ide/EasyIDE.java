package net.raysforge.easylang.ide;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.raysforge.easylang.EasyClass;
import net.raysforge.easylang.EasyClassInterface;
import net.raysforge.easylang.EasyLang;
import net.raysforge.easylang.EasyMethodInterface;
import net.raysforge.easylang.EasySource;
import net.raysforge.easylang.TokenList;
import net.raysforge.easylang.utils.EasyUtils;
import net.raysforge.easylang.utils.FileUtils;
import net.raysforge.easyswing.EasySplitPane;
import net.raysforge.easyswing.EasySwing;
import net.raysforge.easyswing.EasyTextArea;
import net.raysforge.easyswing.EasyTree;

public class EasyIDE {

	private static final String NEW_PROJECT = "newProject";
	private static final String NEW_FILE = "newFile";
	private static final String SAVE = "save";
	private static final String SAVE_ALL = "saveAll";
	private static final String RUN = "run";
	private static final String CLOSE_TAB = "closeTab";

	private EasySwing es;
	private EasyTextArea console;
	private EasyLang easyLang;
	private EasyTree easyTree;
	private final File projectsHome;
	private JTabbedPane tabbedPane;
	private EventDelegator delegator;
	private JPopupMenu autoCompletePopupMenu = new JPopupMenu();
	private JList autoCompleteList = new JList();
	private DefaultListModel autoCompleteListModel;

	public EasyIDE(File projectsHome) {
		this.projectsHome = projectsHome;

		rb = ResourceBundle.getBundle("net.raysforge.easylang.ide.EasyIDE");

		String language = Locale.getDefault().getLanguage();
		System.out.println(language);

		FileUtils.delete(projectsHome); // for testing only
		
		if (projectsHome.exists()) {
			if (!projectsHome.isDirectory()) {
				JOptionPane.showMessageDialog(null, "projectsHome is not a directory: " + projectsHome);
				System.exit(1);
			}
		} else {
			if (!projectsHome.mkdir()) {
				JOptionPane.showMessageDialog(null, "projectsHome could not be created: " + projectsHome);
				System.exit(2);
			} else {
				try {
					createExampleFiles(projectsHome, language);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		delegator = new EventDelegator(this);

		es = new EasySwing("EasyLang IDE", 800, 600, JFrame.DO_NOTHING_ON_CLOSE);
		es.addWindowListener(delegator);
		es.addGlobalKeyEventListener(delegator);

		EasySplitPane codeAndConsole = new EasySplitPane(false, 400);
		EasySplitPane treeAndSplitPane = new EasySplitPane(true, 200);
		easyTree = new EasyTree(rb.getString("Projects"));
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

		JMenu templatesMenuItem = es.addMenuItem(rb.getString("Templates"));
		es.addMenuItem(templatesMenuItem, "start", "start", delegator);

		es.addToolBarItem(rb.getString("MenuItemSave"), SAVE, delegator);
		es.addToolBarItem(rb.getString("MenuItemSaveAll"), SAVE_ALL, delegator);
		es.addToolBarItem(rb.getString("MenuItemCloseTab"), CLOSE_TAB, delegator);
		es.addToolBarItem(rb.getString("MenuItemRun"), RUN, delegator);

		autoCompleteList.addKeyListener(delegator);
		autoCompleteList.addMouseListener(delegator);

		autoCompleteListModel = new DefaultListModel();
		autoCompleteList.setModel(autoCompleteListModel);

		autoCompletePopupMenu.setPreferredSize(new Dimension(400, 200));
		autoCompletePopupMenu.add(new JScrollPane(autoCompleteList));

	}

	private void createExampleFiles(File projectsHome, String language) throws IOException {
		String baseDir = "/net/raysforge/easylang/examples/"+language+"/";
		
		File examplesHome = new File(projectsHome, rb.getString("Examples"));
		if (examplesHome.mkdir()) {
			InputStream examplesList = EasyIDE.class.getResourceAsStream(baseDir+"list.txt");
			BufferedReader r = new BufferedReader( new InputStreamReader(examplesList));
			String s;
			while ( (s = r.readLine() ) != null)
			{
				createExampleFile(examplesHome, baseDir, s);
			}
			//createExampleFile(examplesHome, "sokoban/Sokoban.easy");
		}
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
		// easyLang.parse(new File("easysrc"));
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
		Object source = e.getSource();
		if (source instanceof JTree) {
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
				TreePath selectionPath = easyTree.getSelectionPath();
				if (selectionPath == null)
					return;

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
						EasyTextArea eta = new EasyTextArea();
						eta.setText(new String(completeFile));
						eta.getDocument().addDocumentListener(delegator);
						eta.addKeyListener(delegator);
						tabbedPane.addTab(fileFromTreePath.getName(), null, eta.getScrollPane(), fileFromTreePath.getPath());
						tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
					}
				}
			}
		} else if (source instanceof JList) {
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
				useSelectedListValue();
			}
		}

	}

	public void actionPerformed(ActionEvent e) {

		//System.out.println(e.getActionCommand());

		if (e.getActionCommand().equals(RUN)) {
			console.setText("");
			EasyLang.instance.unregisterClasses("test");
			JTextArea textArea = getSelectedTextArea();
			if( textArea == null)
				return;
			EasyClass.parse("test", EasyUtils.convertSourceToTokenList(new EasySource(textArea.getText().toCharArray())));
			try {
				EasyLang.runClass(easyLang.getClass("test"));
			} catch (Exception exc) {
				writeln(exc.getMessage());
				exc.printStackTrace();
			}
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
		} else if (e.getActionCommand().equals(CLOSE_TAB)) {
			//tabbedPane.getSelectedComponent();
			int selectedIndex = tabbedPane.getSelectedIndex();
			if (selectedIndex == -1)
				return;
			if (tabbedPane.getTitleAt(selectedIndex).startsWith("*")) {
				int result = JOptionPane.showConfirmDialog(getJFrame(), rb.getString("UnsavedChanges"));
				if (result == JOptionPane.NO_OPTION) {
					tabbedPane.removeTabAt(selectedIndex);
				} else if (result == JOptionPane.YES_OPTION) {
					saveSelectedTextArea();
					tabbedPane.removeTabAt(selectedIndex);
				}
			} else {
				tabbedPane.removeTabAt(selectedIndex);
			}
		} else if (e.getActionCommand().equals("start")) {
			JTextArea selectedTextArea = getSelectedTextArea();
			if( selectedTextArea == null)
				return;
			selectedTextArea.insert("publik nichts start()\n{\n}\n", selectedTextArea.getCaretPosition());
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
		Component selectedComponent = tabbedPane.getSelectedComponent();
		if (selectedComponent == null)
			return null;
		return (JTextArea) ((JScrollPane) selectedComponent).getViewport().getView();
	}

	private JTextArea getTextArea(int index) {
		return (JTextArea) tabbedPane.getTabComponentAt(index);
	}

	private File getFile(int index) {
		return new File(tabbedPane.getToolTipTextAt(index));
	}

	private File getSelectedFile() {
		int selectedIndex = tabbedPane.getSelectedIndex();
		if (selectedIndex == -1)
			return null;
		return new File(tabbedPane.getToolTipTextAt(selectedIndex));
	}

	private static void createExampleFile(File examplesHome, String jarBaseDir, String filename) {
		try {
			InputStream resourceAsStream = EasyIDE.class.getResourceAsStream( jarBaseDir + filename);
			ReadableByteChannel rbc = Channels.newChannel(resourceAsStream);
			FileOutputStream fos = new FileOutputStream(new File(examplesHome, filename));
			FileChannel channel = fos.getChannel();
			channel.transferFrom(rbc, 0, Integer.MAX_VALUE);
			fos.close();
			resourceAsStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		File selectedFile = getSelectedFile();
		if (selectedFile == null)
			return;
		FileUtils.writeCompleteFile(selectedFile, getSelectedTextArea().getText());
		setSelectedTabToSaved();
	}

	private static String partial = "";
	public final ResourceBundle rb;

	public void showAutoCompleteBox(JTextArea invoker, Point caretPosition) {
		autoCompleteListModel.clear();

		String text = null;
		try {
			text = invoker.getText(0, invoker.getCaretPosition());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		FindAllVariables fav = new FindAllVariables();
		//JTextArea textArea = getSelectedTextArea();
		TokenList tokenList = EasyUtils.convertSourceToTokenList(new EasySource(text.toCharArray()));
		TokenList tokenListCopy = tokenList.copy();
		fav.parse("parse", tokenList);
		tokenList = null;

		//EasySource reverseSource = new EasySource(new StringBuffer(text).reverse().toString().toCharArray());
		//String token = reverseSource.getToken();

		ParseAutoCompletion pac = new ParseAutoCompletion(tokenListCopy);
		pac.parse();
		pac.debug();

		if (pac.partial != null)
			partial = pac.partial.s();
		else
			partial = "";

		if (pac.rootVar != null && !pac.rootVar.equals(pac.partial)) {
			if (pac.rootVar.isQuote()) {
				EasyClassInterface class1 = easyLang.getClass(EasyLang.rb.getString("String"));
				if (class1 != null) {
					Map<String, EasyMethodInterface> methods = class1.getMethods();
					for (String key : methods.keySet()) {
						if (key.startsWith(pac.partial.s()))
							autoCompleteListModel.addElement(new AutoCompleteElement(key, ElementType.METHOD));
					}
				}
			} else if (fav.vars.containsKey(pac.rootVar.s())) {
				String objectType = fav.vars.get(pac.rootVar.s());
				EasyClassInterface class1 = easyLang.getClass(objectType);
				if (class1 != null) {
					Map<String, EasyMethodInterface> methods = class1.getMethods();
					for (String key : methods.keySet()) {
						String s = "";
						if (pac.partial != null)
							s = pac.partial.s();
						if (key.startsWith(s))
							autoCompleteListModel.addElement(new AutoCompleteElement(key, ElementType.METHOD));
					}
				}

			}
		} else {
			String s = "";
			if (pac.partial != null)
				s = pac.partial.s();
			HashMap<String, EasyClassInterface> classes = EasyLang.instance.getClasses();
			for (String name : classes.keySet()) {
				if (name.startsWith(s))
					autoCompleteListModel.addElement(new AutoCompleteElement(name, ElementType.CLASS));
			}

			for (String key : fav.vars.keySet()) {
				if (key.startsWith(s))
					autoCompleteListModel.addElement(new AutoCompleteElement(key, ElementType.VARIABLE));
			}
		}

		autoCompletePopupMenu.show(invoker, caretPosition.x, caretPosition.y + 16);
		autoCompleteList.setSelectedIndex(0);
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				autoCompleteList.requestFocus();
			}
		});
	}

	public void useSelectedListValue() {
		AutoCompleteElement ace = (AutoCompleteElement) autoCompleteList.getSelectedValue();
		if (ace == null)
			return;
		String s = ace.name;
		if (ace.type == ElementType.CLASS)
			s = ace.name + " " + ace.name.toLowerCase() + " = " + EasyLang.rb.getString("new") + " " + ace.name + "();";
		if (ace.type == ElementType.METHOD)
			s = ace.name + "();";
		int cp = getSelectedTextArea().getCaretPosition();
		try {
			s = s.substring(partial.length()); // for example if one auto completes this: 'x.min', then we only want to insert the 'us' => minus.
			getSelectedTextArea().getDocument().insertString(cp, s, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		autoCompletePopupMenu.setVisible(false);

	}

	public void saveAllTextAreas() {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			if (tabbedPane.getTitleAt(i).startsWith("*")) {
				String text = ((JTextArea) ((JScrollPane) tabbedPane.getComponent(i)).getViewport().getView()).getText();
				File file = new File(tabbedPane.getToolTipTextAt(i));
				FileUtils.writeCompleteFile(file, text);
			}
		}
	}

	public static void main(String[] args) {
		File userHome = new File(System.getProperty("user.home"));
		File projectsHome = new File(userHome, "EasyLangProjects");

		new EasyIDE(projectsHome).start();
	}

}
