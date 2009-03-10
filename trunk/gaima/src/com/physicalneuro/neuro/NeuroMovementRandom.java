package com.physicalneuro.neuro;

	import org.joone.helpers.factory.JooneTools;
import org.joone.net.NeuralNet;

import com.jme.math.Vector2f;
import com.physicalneuro.util.NeuroUtil;

	/**
	 * Random NeuroMovement with JOONE Helpers.
	 * 
	 * <pre>
	 * Input     Hidden    Output 
	 *             x                                      X                                           
	 *  x          x         x2                           X                                           
	 *             x                                      X                                           
	 *  y          x         y2                           X                                           
	 *                                                    X             
	 * Randomise all movements.                               
	 * </pre>
	 * 
	 * @author T8TSOSO
	 */
	public class NeuroMovementRandom implements NeuroMovementInterface{
	    
	    // Neuron input for object position
	    private Vector2f	inputPositionVector;
	    
	    // Neuron input for object heading
	    private Vector2f	inputHeadingVector;

	    // Neuron input for object velocity
	    private double velocity;
	    
	    // Objects desired position
	    private Vector2f	desiredPositionVector;
	    
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
	    public NeuroMovementRandom() {
	    }

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
                    100, 0.01,
                    50, null, false);
            for (int i = 0; i < inputPosition[0].length-1; i++) {
				double[] output = JooneTools.interrogate(network, inputPosition[i]);
				// X
				if (output[0] > 0.5f) {
	            	inputPositionVector.x += output[0];
	            }
				else if (output[0] < 0.5f) {
					inputPositionVector.x -= output[0];
				}
				// Y
				if (output[1] > 0.5f) {
	            	inputPositionVector.y += output[0];
	            }
				else if (output[1] < 0.5f) {
					inputPositionVector.y -= output[0];
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

