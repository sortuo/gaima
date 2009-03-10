package com.physicalneuro.util;

import com.jme.math.Vector2f;

public class NeuroUtil {
	
    /**
     * Converts the input Vector2f to double[][] array.
     * @param inputVector
     * @return
     */
    public static double[][] getVector(Vector2f inputVector){
    	double [][] outputVector = new double[][]{{inputVector.x,inputVector.y}};
    	return outputVector;
    }
	

}
