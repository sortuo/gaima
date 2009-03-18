package com.physicalneuro.util;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

public class NeuroUtil {
	
	public static final int X_ARRAY = 0;
	public static final int Y_ARRAY = 1;
	public static final int Z_ARRAY = 2;
    /**
     * Converts the input Vector2f to double[][] array.
     * @param inputVector
     * @return
     */
    public static double[][] getVector(Vector2f inputVector){
    	double [][] outputVector = new double[][]{{inputVector.x,inputVector.y}};
    	return outputVector;
    }

	public static double[] vectorToArray(Vector3f inputPositionVector) {
		float[] array = inputPositionVector.toArray(new float[3]);
		double [] returnArray = new double[3];
		returnArray[X_ARRAY] = (double)array[X_ARRAY];
		returnArray[Y_ARRAY] = (double)array[Y_ARRAY];
		returnArray[Z_ARRAY] = (double)array[Z_ARRAY];
		return returnArray;
	}
	

	public static double[][] addRow(double[] add, double[][] array) {
		double[][] returnArray;
		// Array row lengths is array.length
		// Array column lengths is array[row].length, column lengths can vary
		if (array == null) {
			returnArray = new double[1][];
			returnArray[0] = add;
			return returnArray;
		}
		int rows = array.length+1;
		System.out.print(rows);
		returnArray = new double[rows][];
		for (int i = 0; i < array.length; i++) {
			double[] ds = array[i];
			returnArray[i]=array[i];
		}
		// Add new row
		returnArray[rows-1] = add;
		return returnArray;
	}
	
	public static void main(String[] args){
		double[][] old = { {1,3,4}, {2,3,5}, {2,3,3}, {12,3,3} };
		
		double[] add = {100,100,100};
		
		double[][]test = addRow(add, old);
		
		for (int r=0; r < test.length; r++) {
		    for (int c=0; c < test[r].length; c++) {
		        System.out.print(" "+test[r][c]);
		    }
		    System.out.println("");
		}
	}
	
}
