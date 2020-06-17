/**
 * @Author: Ngoc Duy Pham
 * IDnumber: 201066354
 * Email: duy.pham200394@gmail.com
 * Date: 01/04/2018
 * Relational Algebra Visualizer for Final Year Project at University of Liverpool.
 
 -----------------*--KeyPadClient.java file--*-------------------------------------
 The file contains the interface for the key pad in RVA application 
 */

public abstract interface KeyPadClient {
	
	//method to display key label on the input field.
	public void keyPressCallBack(String newLabel);
	
}