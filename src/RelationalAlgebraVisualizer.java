
/**
 * @Author: Ngoc Duy Pham
 * IDnumber: 201066354
 * Email: duy.pham200394@gmail.com
 * Date: 01/04/2018
 * Relational Algebra Visualizer for Final Year Project at University of Liverpool.
 
 -----------------*--RelationalAlgebraVisualizer.java file--*-------------------------------------
 The file create main interface and contain main class for Relational Algebra Visualizer application
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.TitledBorder;
import java.io.StringReader;
import java.io.BufferedReader;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ScannerBuffer;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import java.util.Vector;

public class RelationalAlgebraVisualizer extends JFrame implements ActionListener, KeyPadClient, QueryTreeViewer {

	/*-----------------VARIABLES OF CLASS---------------------*/
	// text area to get query from users
	private JTextArea input;
	// jWebBrowser to display HTML files
	private JWebBrowser display;
	// label to prompt users
	private JLabel prompt;
	// all control buttons
	private JButton converter, highlighter;
	// all panels to contain all component of main interface
	private JPanel inputPanel, controlPanel, outputPanel, mainPanel;
	// main menu bar
	private JMenuBar mBar;
	// menu within menu bar
	private JMenu fileMenu, helpMenu, toolMenu;
	// all items within menus
	private JMenuItem openItem, saveItem, closeItem1, aboutItem, guideItem, closeItem2, searchItem, barItem,
			hideBarItem, closeItem3;
	// file chooser to open and save HTML files
	private JFileChooser fileChooser;
	// variable to check the users have chosen a file or not
	private int fileFlag;
	// counter for saving file
	private int fileID = 1;
	// key pad for main interface
	private KeyPad keyboard;
	// factory to create symbol object for scanner
	private ComplexSymbolFactory symFactory = new ComplexSymbolFactory();
	// the parser to parse the query and create query tree
	private Parser parser;
	// analyzer to analyze the query
	private QueryTreeAnalyser analyser;
	// vector to store all executed operators in correct order
	private Vector<String> table;

	/*------------------METHODS OF CLASS----------------------*/
	public RelationalAlgebraVisualizer() {
		super("Relational Algebra Visualizer");
		Container pane = this.getContentPane();
		mainPanel = new JPanel(new BorderLayout(2, 5));
		TitledBorder title = BorderFactory.createTitledBorder("Ngoc Duy Pham   ID-201066354");
		title.setTitleJustification(TitledBorder.CENTER);
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		title.setTitleFont(new Font("Arial", Font.ITALIC, 13));
		mainPanel.setBorder(title);

		// create input panel by calling initInputPanel()
		this.initInputPanel();
		mainPanel.add(inputPanel, "North");

		// create control panel by calling initControlPanel()
		this.initControlPanel();
		mainPanel.add(controlPanel, "West");

		// create output panel
		display = new JWebBrowser();
		display.setBarsVisible(false);
		outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(BorderFactory.createTitledBorder("Tree Display"));
		outputPanel.add(display, "Center");
		mainPanel.add(outputPanel, "Center");

		pane.add(mainPanel);

		// create all the menus by calling associated methods
		// and add to the main bar menu
		mBar = new JMenuBar();
		this.initFileMenu();
		this.initToolMenu();
		this.initHelpMenu();
		this.setJMenuBar(mBar);

		// create file chooser
		this.initFileChooser();
	}

	// method to ceate input panel
	private void initInputPanel() {
		inputPanel = new JPanel(new BorderLayout(5, 0));
		prompt = new JLabel("Enter Your Query Here ");
		prompt.setFont(new Font("Arial", Font.BOLD, 13));
		inputPanel.add(prompt, "West");
		input = new JTextArea();
		input.setRows(2);
		input.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));
		inputPanel.add(input, "Center");
		JScrollPane scrollBar = new JScrollPane(input);
		scrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		inputPanel.add(scrollBar);
	}

	// method to create control panel
	private void initControlPanel() {
		controlPanel = new JPanel(new BorderLayout(0, 10));
		controlPanel.setBorder(BorderFactory.createTitledBorder("Control Keyboard"));
		JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
		converter = new JButton("Generate Tree");
		converter.setFont(new Font("Arial", Font.BOLD, 12));
		converter.addActionListener(this);
		highlighter = new JButton("Highlight tree");
		highlighter.setFont(new Font("Arial", Font.BOLD, 12));
		highlighter.addActionListener(this);
		buttonPanel.add(converter);
		buttonPanel.add(highlighter);
		keyboard = new KeyPad("./data/keyLabels.txt", this);
		controlPanel.add(keyboard, "Center");
		controlPanel.add(buttonPanel, "South");
	}

	// method to create file menu
	private void initFileMenu() {
		fileMenu = new JMenu("File");
		fileMenu.setForeground(Color.BLUE);
		mBar.add(fileMenu);

		openItem = new JMenuItem("Open");
		openItem.addActionListener(this);
		fileMenu.add(openItem);

		saveItem = new JMenuItem("Save");
		saveItem.addActionListener(this);
		fileMenu.add(saveItem);

		fileMenu.addSeparator();

		closeItem1 = new JMenuItem("Close");
		closeItem1.addActionListener(this);
		fileMenu.add(closeItem1);
	}

	// method to create help menu
	private void initHelpMenu() {
		helpMenu = new JMenu("Help?");
		helpMenu.setForeground(Color.BLUE);
		mBar.add(helpMenu);

		aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);

		guideItem = new JMenuItem("Guideline!");
		guideItem.addActionListener(this);
		helpMenu.add(guideItem);

		helpMenu.addSeparator();

		closeItem2 = new JMenuItem("Close");
		closeItem2.addActionListener(this);
		helpMenu.add(closeItem2);
	}

	// method to create tool menu
	private void initToolMenu() {
		toolMenu = new JMenu("Tools");
		toolMenu.setForeground(Color.BLUE);
		mBar.add(toolMenu);

		searchItem = new JMenuItem("JWebBrowser");
		searchItem.addActionListener(this);
		toolMenu.add(searchItem);

		barItem = new JMenuItem("show Task_Bar");
		barItem.addActionListener(this);
		toolMenu.add(barItem);

		hideBarItem = new JMenuItem("Hide Task_Bar");
		hideBarItem.addActionListener(this);
		toolMenu.add(hideBarItem);

		toolMenu.addSeparator();

		closeItem3 = new JMenuItem("Close");
		closeItem3.addActionListener(this);
		toolMenu.add(closeItem3);
	}

	// method to create file chooser
	public void initFileChooser() {
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Relational Algebra Visualizer");
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("HTML files", "html");
		fileChooser.setFileFilter(fileFilter);
	}

	// method to create scanner buffer for the parser to store all token during the
	// parsing phase
	private ScannerBuffer createScannerBuffer(String query) {
		BufferedReader reader = new BufferedReader(new StringReader(query));
		QueryLexer lexer = new QueryLexer(reader, symFactory);
		ScannerBuffer lexerBuffer = new ScannerBuffer(lexer);
		return lexerBuffer;
	}

	// action performed method for the main interface
	public void actionPerformed(ActionEvent e) {
		Object eventObject = e.getSource();

		if (eventObject == openItem) { // method to open HTML files
			fileFlag = fileChooser.showOpenDialog(null);
			if (fileFlag == fileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file.getName().endsWith(".html")) {
					display.navigate("file:///" + file.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(this, "Could not open this kind of file!", "System Warning",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		} else if (eventObject == saveItem) { // method to save HTML files
			String defaultName = System.getProperty("user.home") + "/new " + (fileID++) + ".html";
			fileChooser.setSelectedFile(new File(defaultName));
			fileFlag = fileChooser.showSaveDialog(null);
			if (fileFlag == fileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file.getName().endsWith(".html")) {
					try {
						BufferedReader reader = new BufferedReader(new FileReader(new File("queryTree.html")));
						PrintWriter writer = new PrintWriter(new FileWriter(file));

						String line = "";
						while ((line = reader.readLine()) != null) {
							writer.println(line);
						}
						reader.close();
						writer.close();
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(this,
							"Type different file name with the extension \".html\", please!", "System Warning",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		} else if (eventObject == searchItem) { // method to open jxBrowser
			display.navigate("http://www.google.com");

		} else if (eventObject == barItem) { // method to open URL bar on the tree display
			if (display.isMenuBarVisible() == false) {
				display.setMenuBarVisible(true);
			}

		} else if (eventObject == hideBarItem) { // method to close URL bar on the tree display
			if (display.isMenuBarVisible() == true) {
				display.setMenuBarVisible(false);
			}

		} else if ((eventObject == closeItem1) || (eventObject == closeItem2) || (eventObject == closeItem3)) { // method
																												// to
																												// close
																												// all
																												// menus
			MenuSelectionManager.defaultManager().clearSelectedPath();

		} else if (eventObject == aboutItem) { // method to open information about the application
			display.navigate("file:///" + System.getProperty("user.dir") + "/data/aboutRAV.html");

		} else if (eventObject == guideItem) { // method to open user manual
			display.navigate("file:///" + System.getProperty("user.dir") + "/data/userManual.html");

		} else if (eventObject == highlighter) { // method to highlight inefficiencies within a query
			if ((analyser == null) && (table != null)) {
				analyser = new QueryTreeAnalyser();
				analyser.showExecutedOperators(table);
				analyser.determineInefficencies(table);

				int location2X = this.getWidth() + this.getX(), location2Y = this.getY();
				analyser.setLocation(location2X, location2Y);

				analyser.setSize(500, 500);

				analyser.setVisible(true);
			} else if ((analyser != null) && (table != null)) {
				analyser.showExecutedOperators(table);
				analyser.determineInefficencies(table);

				int location2X = this.getWidth() + this.getX(), location2Y = this.getY();
				analyser.setLocation(location2X, location2Y);

				analyser.setSize(500, 500);

				analyser.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "Input query and generate query tree first, please!",
						"System Warning", JOptionPane.WARNING_MESSAGE);
			}
		} else { // method to generate a query tree
			ScannerBuffer scanner = this.createScannerBuffer(input.getText());
			parser = new Parser(scanner, symFactory, this);

			try {
				parser.parse();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	// define method body for QueryTreeViewer interface
	public void displayQueryTree(Boolean isError) {
		if (isError == false) {
			display.navigate("file:///" + System.getProperty("user.dir") + "/data/queryTree.html");
			table = parser.getExecutedTable();
		} else {
			JOptionPane.showMessageDialog(this,
					"Invalid characters in the input query or unrecognized format of query!!!", "Syntax Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// define method body for KeyPadClient interface
	public void keyPressCallBack(String newLabel) {
		if (newLabel.equals("Clear*")) {
			input.setText("");
		} else if (newLabel.equals("Quit!")) {
			// close native interface
			NativeInterface.close();
			System.out.println("===$$ The application is closed!");
			System.exit(0);
		} else {
			input.insert(newLabel, input.getCaretPosition());
		}
	}

	/*--------== MAIN METHOD OF RAV APPLICATION ==---------*/
	public static void main(String[] args) {
		// initialize native interface
		NativeInterface.open();

		// TODO Auto-generated method stub
		// method to change the look and feel to windows operating system
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}

		RelationalAlgebraVisualizer visualizer = new RelationalAlgebraVisualizer();

		// method to exit the program when users clicking on close icon on the frame
		visualizer.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// close native interface
				NativeInterface.close();
				System.out.println("===$$ The application is closed!");
				System.exit(0);
			}
		});

		visualizer.setSize(700, 500);

		// set the application appear to the center of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int frameWidth = visualizer.getWidth(), frameHeight = visualizer.getHeight();
		int locationX = (screenSize.width - frameWidth) / 2, locationY = (screenSize.height - frameHeight) / 2;
		visualizer.setLocation(locationX, locationY);

		visualizer.setVisible(true);
		System.out.println("===$$ The application running...");

		// running negative event
		NativeInterface.runEventPump();
	}

}