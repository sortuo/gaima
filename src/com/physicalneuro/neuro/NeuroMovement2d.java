package com.physicalneuro.neuro;

	import org.joone.helpers.factory.JooneTools;
import org.joone.net.NeuralNet;

import com.jme.math.Vector2f;
import com.physicalneuro.util.NeuroUtil;

	/**
	 * NeuroMovement with JOONE Helpers.
	 * 
	 * <pre>
	 * Input     Hidden    Output 
	 *             x                                      X                                           
	 *  x          x         x2                           X                                           
	 *             x                                      X                                           
	 *  y          x         y2                           X                                           
	 *                                                    X             
	 * If output is over ratio 0.5, then add 0.5 to position.                               
	 * </pre>
	 * 
	 * @author T8TSOSO
	 */
	public class NeuroMovement2d implements NeuroMovementInterface{
	    
	    // Neuron input for object position
	    private Vector2f	inputPositionVector = new Vector2f (0.5f,1.5f);
	    
	    // Neuron input for object heading
	    private Vector2f	inputHeadingVector = new Vector2f (0.5f,1.5f);

	    // Neuron input for object velocity
	    private double velocity;
	    
	    // Objects desired position
	    private Vector2f	desiredPositionVector = new Vector2f(0.5f,1.0f);
	    
	    // Objects desired velocity
	    private double desiredVelocity = 0;
	    
	    private NeuralNet network;
	     
	    
	    private double[][]	inputPosition;
	    
	    // XOR desired output
	    private double[][]	desiredPosition;
	    
	    private static boolean singleThreadMode = true;
	    /**
	     * Creates a new instance of XOR_using_helpers
	     */
	    public NeuroMovement2d() {
	    }
	    
/*	    public static void main(String[] args) {
	        try {
	        // Create the network: 3 layers with a logistic output layer
	        NeuralNet nnet = JooneTools.create_standard(new int[]{ 2, 6, 2 }, JooneTools.LOGISTIC);
//	            NeuralNet nnet = JooneTools.load("org/joone/samples/engine/helpers/rxor.snet");
	            nnet.getMonitor().setSingleThreadMode(singleThreadMode);
	            NeuroListener listener = new NeuroListener();
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
	            for (int i=0; i < 1; ++i) {
	                double[] output = JooneTools.interrogate(nnet, inputPosition[i]);
	                System.out.print("| "+inputPosition[i][0]+"\t| "+inputPosition[i][1]+"\t| ");
	                System.out.println("\n"+output[0]+", "+output[1]);
	            }
	            
	            // Test the network and prints the rmse
	            double testRMSE = JooneTools.test(nnet, inputPosition, desiredPosition);
	            System.out.println("\nTest error = "+testRMSE);
	        } catch (Exception exc) { exc.printStackTrace(); }
	    }
*/
		@Override
		public void setNeuroMovement(Vector2f movement, Vector2f heading ) {
			this.inputPositionVector = movement;
			this.inputHeadingVector = heading;
			network = JooneTools.create_standard(new int[]{ 2, 6, 2 }, JooneTools.LOGISTIC);
			network.getMonitor().setSingleThreadMode(singleThreadMode);
		}

		@Override
		public Vector2f getMovement() {
			return inputPositionVector;
		}

		@Override
		public void setMovement(Vector2f movement) {
			this.inputPositionVector = movement;
		}

		@Override
		public Vector2f updateMovement() {
			inputPosition =  NeuroUtil.getVector(inputPositionVector);
			desiredPosition = NeuroUtil.getVector(desiredPositionVector);
            double rmse = JooneTools.train(network, inputPosition, desiredPosition,
                    10, 0.01,
                    1, null, false);
            for (int i = 0; i < inputPosition[0].length-1; i++) {
				double[] output = JooneTools.interrogate(network, inputPosition[i]);
				// X
				if (output[0] > 0.5f) {
	            	inputPositionVector.x += 0.0005f;
	            }
				else if (output[0] < 0.5f) {
					inputPositionVector.x -= 0.0005f;
				}
				// Y
				if (output[1] > 0.5f) {
	            	inputPositionVector.y += 0.0005f;
	            }
				else if (output[1] < 0.5f) {
					inputPositionVector.y -= 0.0005f;
				}
			}
            return inputPositionVector;
		}

		@Override
		public void setDesiredPosition(Vector2f position) {
			this.desiredPositionVector = position;
			
		}

		@Override
		public Vector2f getDesiredPosition() {
			return desiredPositionVector;
		}
	}

