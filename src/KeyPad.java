/**
 * @Author: Ngoc Duy Pham
 * IDnumber: 201066354
 * Email: duy.pham200394@gmail.com
 * Date: 01/04/2018
 * Relational Algebra Visualizer for Final Year Project at University of Liverpool.
 
 -----------------*--KeyPad.java file--*-------------------------------------
 The file create key pad panel for RVA application 
 */

import java.util.Scanner;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class KeyPad extends JPanel implements ActionListener {

/*-----------------VARIABLES OF CLASS---------------------*/
    //variable of KeyPadClient interface
	private KeyPadClient kpClient;
	//array store all key's labels
	private String labels[];
	//array store default key's labels
	private static final String DEFAULT_LABELS[] = { "σ", "π", "⋃", "⋂", "⋊", "⋉", "⋈", "x", "Clear*", "Quit!" };
	//array store all keys
	private JButton buttons[];
	//counter for total of label
	private int labelSum;

/*------------------METHODS OF CLASS----------------------*/
	public KeyPad(String fileName, KeyPadClient kpClient) {
		
		this.kpClient = kpClient;
		this.setLayout(new GridLayout(5, 2, 1, 1));
        
		//Read label from the file into the labels array
		//and create button which are obtained within buttons array
		try {
			File labelFile = new File(fileName);

			if (labelFile.exists()) {
				Scanner firstStream = new Scanner(labelFile, "utf-8");
				while (firstStream.hasNextLine()) {
					labelSum += 1;
					firstStream.next();
				}

				labels = new String[labelSum];

				Scanner secondStream = new Scanner(labelFile, "utf-8");
				for (int i = 0; i < labels.length; i++) {
					labels[i] = secondStream.next();
				}

				buttons = new JButton[labelSum];
				for (int i = 0; i < labelSum; i++) {
					buttons[i] = new JButton(labels[i]);
					buttons[i].setFont(new Font("Arial Unicode MS", Font.BOLD, 12));
					buttons[i].addActionListener(this);
					this.add(buttons[i]);
				}
				
			} else {   //if the file is not there, create buttons based on default label array
				buttons = new JButton[DEFAULT_LABELS.length];
				for (int i = 0; i < DEFAULT_LABELS.length; i++) {
					buttons[i] = new JButton(DEFAULT_LABELS[i]);
					buttons[i].setFont(new Font("Arial Unicode MS", Font.BOLD, 12));
					buttons[i].addActionListener(this);
					this.add(buttons[i]);
				}
			}

		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}

	//action performed method to call abstract method from KeyPadClient interface
	public void actionPerformed(ActionEvent e) {
		String keylabel = ((JButton) e.getSource()).getText();
		kpClient.keyPressCallBack(keylabel);
	}
}