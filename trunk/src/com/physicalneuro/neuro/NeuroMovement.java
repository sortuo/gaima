package com.physicalneuro.neuro;

	import org.joone.helpers.factory.JooneTools;
import org.joone.net.NeuralNet;

import com.jme.math.Vector3f;

	/**
	 * NeuroMovement with JOONE Helpers.
	 * @author T8TSOSO
	 */
	public class NeuroMovement {
	    
	    // Neuron input for object position
	    private static Vector3f	inputPositionVector = new Vector3f (0.5f,1.5f,2.5f);
	    
	    // Neuron input for object heading
	    private static Vector3f	inputHeadingVector = new Vector3f (0.5f,1.5f,2.5f);

	    // Neuron input for object velocity
	    private double velocity;
	    
	    // Objects desired position
	    private static Vector3f	desiredPositionVector = new Vector3f(1.0f,1.0f,1.0f);
	    
	    // Objects desired velocity
	    private double desiredVelocity = 0;
	     
	    /**
	     * Converts the input Vector3f to double[][] array.
	     * @param inputVector
	     * @return
	     */
	    public static double[][] getVector(Vector3f inputVector){
	    	double [][] outputVector = new double[][]{{inputVector.x},{inputVector.y}, {inputVector.z}};
	    	return outputVector;
	    }
	    
	    private static double[][]	inputPosition = getVector(inputPositionVector);
	    
	    // XOR desired output
	    private static double[][]	desiredPosition = getVector(desiredPositionVector);
	    
	    private static boolean singleThreadMode = true;
	    /**
	     * Creates a new instance of XOR_using_helpers
	     */
	    public NeuroMovement() {
	    }
	    
	    public static void main(String[] args) {
	        try {
	        // Create the network: 3 layers with a logistic output layer
	        NeuralNet nnet = JooneTools.create_standard(new int[]{ 1, 6, 1 }, JooneTools.LOGISTIC);
//	            NeuralNet nnet = JooneTools.load("org/joone/samples/engine/helpers/rxor.snet");
	            nnet.getMonitor().setSingleThreadMode(singleThreadMode);
	            // Train the network for 5000 epochs, or until rmse reaches 0.01.
	            // Outputs the results every 200 epochs on the stardard output
	            double rmse = JooneTools.train(nnet, inputPosition, desiredPosition,
	                    5000, 0.01,
	                    1, System.out, false);
	            
	            // Waits in order to avoid the interlacing of the rows displayed
	            try { Thread.sleep(50); } catch (InterruptedException doNothing) { }
	            
	            // Interrogate the network and prints the results
	            System.out.println("Last RMSE = "+rmse);
	            System.out.println("\nResults:");
	            System.out.println("|Inp 1\t|Inp 2\t|Output");
	            for (int i=0; i < 3; ++i) {
	                double[] output = JooneTools.interrogate(nnet, inputPosition[i]);
	                System.out.print("| "+inputPosition[i][0]+"\t| "+inputPosition[i][0]+"\t| ");
	                System.out.println(output[0]);
	            }
	            
	            // Test the network and prints the rmse
	            double testRMSE = JooneTools.test(nnet, inputPosition, desiredPosition);
	            System.out.println("\nTest error = "+testRMSE);
	        } catch (Exception exc) { exc.printStackTrace(); }
	    }
	}

