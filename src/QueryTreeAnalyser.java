/**
 * @Author: Ngoc Duy Pham
 * IDnumber: 201066354
 * Email: duy.pham200394@gmail.com
 * Date: 01/04/2018
 * Relational Algebra Visualizer for Final Year Project at University of Liverpool.
 
 -----------------*--QueryTreeAnalyser.java file--*-------------------------------------
 The file create class to analyze a relational algebra query based its query tree 
 */
 
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;
import java.util.ListIterator;


public class QueryTreeAnalyser extends JFrame {

/*-----------------VARIABLES OF CLASS---------------------*/    
	//panels contain executed operator and result of analysis
	private JPanel operatorPanel, resultPanel;
	//text areas to display executed operators and result of analysis
	private JTextArea display1, display2;
	//scroll panes
	private JScrollPane scroller1, scroller2;

/*------------------METHODS OF CLASS----------------------*/
	public QueryTreeAnalyser() {
		super("Query Tree Analyser");
		Container pane = this.getContentPane();
		pane.setLayout(new GridLayout(1, 2, 0, 2));
		this.initOperatorPanel();
		this.initResultPanel();
		pane.add(operatorPanel);
		pane.add(resultPanel);
	}

	//method initialize operators' panel
	private void initOperatorPanel() {
		operatorPanel = new JPanel(new BorderLayout());
		operatorPanel.setBorder(BorderFactory.createTitledBorder("List Of Executed Operators"));
		display1 = new JTextArea();
		display1.setEditable(false);
		display1.setLineWrap(true);
		display1.setWrapStyleWord(true);
		display1.setBackground(Color.YELLOW);
		display1.setForeground(Color.BLUE);
		display1.setFont(new Font("Arinal Unicode MS", Font.BOLD, 14));
		operatorPanel.add(display1, "Center");
		scroller1 = new JScrollPane(display1);
		operatorPanel.add(scroller1);
	}

	//method to initialize result panel
	private void initResultPanel() {
		resultPanel = new JPanel(new BorderLayout());
		resultPanel.setBorder(BorderFactory.createTitledBorder("Inefficencies Within Query"));
		display2 = new JTextArea();
		display2.setEditable(false);
		display2.setLineWrap(true);
		display2.setWrapStyleWord(true);
		display2.setBackground(Color.BLUE);
		display2.setFont(new Font("Arinal Unicode MS", Font.BOLD, 14));
		resultPanel.add(display2, "Center");
		scroller2 = new JScrollPane(display2);
		resultPanel.add(scroller2);
	}

	//method to display executed operators
	public void showExecutedOperators(Vector<String> table) {
		display1.setText("");
		display1.append("Start->");

		ListIterator<String> iterator = table.listIterator();
		int index = 0;
		while (iterator.hasNext()) {
			index++;
			display1.append("\n          |\n          |\n        " + index + ": " + iterator.next());
		}
	}

	//method to analyze the query
	public void determineInefficencies(Vector<String> table) {
		display2.setText("");
        display2.setForeground(Color.RED);
        
		//variable to show query not contain any inefficiencies
		boolean flag = true;
		
		//loop through the vector storing all executed operator in correct order
		//and check if select and project operators are executed after binary operators
        //if it is the case, show the warning on this operator		
		for (int i = 1; i < table.size(); i++) {
			String operator = table.elementAt(i);
            
			//checking select operator
			if (operator.substring(0, 1).equals("σ")) {
				if (table.elementAt(i - 1).equals("⋃")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Union!!!☹\n\n");
					flag = false;
				}
				if (table.elementAt(i - 1).equals("⋂")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Intersection!!!☹\n\n");
					flag = false;
				}
				if (table.elementAt(i - 1).equals("x")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Production!!!☹\n\n");
					flag = false;
				}
				if (table.elementAt(i - 1).substring(0, 1).equals("⋊")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Right Join!!!☹\n\n");
					flag = false;
				}
				if (table.elementAt(i - 1).substring(0, 1).equals("⋉")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Left Join!!!☹\n\n");
					flag = false;
				}
				if (table.elementAt(i - 1).substring(0, 1).equals("⋈")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Join!!!☹\n\n");
					flag = false;
				}
			}

			//checking project operator
			if (operator.substring(0, 1).equals("π")) {
				if (table.elementAt(i - 1).equals("⋃")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Union!!!☹\n\n");
					flag = false;
				}
				if (table.elementAt(i - 1).equals("⋂")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Intersection!!!☹\n\n");
					flag = false;
				}
				if (table.elementAt(i - 1).equals("x")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Production!!!☹\n\n");
					flag = false;
				}
				if (table.elementAt(i - 1).substring(0, 1).equals("⋊")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Right Join!!!☹\n\n");
					flag = false;
				}
				if (table.elementAt(i - 1).substring(0, 1).equals("⋉")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Left Join!!!☹\n\n");
					flag = false;
				}
				if (table.elementAt(i - 1).substring(0, 1).equals("⋈")) {
					display2.append("@--Operation " + (i + 1) + " has been executed after Join!!!☹\n\n");
					flag = false;
				}
			}
		}
        
		//no such bad operators, show positive notification to users
		if (flag == true) {
			display2.setForeground(Color.GREEN);
			display2.append("✔--Query might not contain any inefficencies!☻");
		}
	}

}