/**
 * @Author: Ngoc Duy Pham
 * IDnumber: 201066354
 * Email: duy.pham200394@gmail.com
 * Date: 01/04/2018
 * Relational Algebra Visualizer for Final Year Project at University of Liverpool.
 
 -----------------*--QueryTreeViewer.java file--*-------------------------------------
 The file contains interface to display query tree in RVA application 
 */

public abstract interface QueryTreeViewer {
	
	//method to display query tree in RAV application
    public void displayQueryTree(Boolean isError);
    
}