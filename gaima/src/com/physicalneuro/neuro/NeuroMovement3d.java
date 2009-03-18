package com.physicalneuro.neuro;

	import java.util.Arrays;

import org.joone.helpers.factory.JooneTools;
import org.joone.net.NeuralNet;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
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
	 *             x                                      X             
	 *  z          x         z2                           X
	 *                                             
	 * If output is over ratio 0.5, then add 0.5 to position.                               
	 * </pre>
	 * 
	 * @author T8TSOSO
	 */
	public class NeuroMovement3d {
	    
	    // Neuron input for object position
	    private Vector3f	inputPositionVector;
	    
	    // Neuron input for object heading
	    private Vector3f	inputHeadingVector;

	    // Neuron input for object velocity
	    private double velocity;
	    
	    // Objects desired position
	    private Vector3f	desiredPositionVector;
	    
	    // Objects desired velocity
	    private double desiredVelocity = 0;
	    
	    private NeuralNet network;
	     
	    
	    private double[][]	inputPosition;
	    
	    // XOR desired output
	    private double[][]	desiredPosition;
	    
	    private static boolean singleThreadMode = true;
	    
	    private boolean isNetworkTrained = false;
	    /**
	     * Creates a new instance of XOR_using_helpers
	     */
	    public NeuroMovement3d() {
	    }
	    
		
		public void setNeuroMovement(Vector3f movement, Vector3f heading ) {
			this.inputPositionVector = movement;
			this.inputHeadingVector = heading;
			network = JooneTools.create_standard(new int[]{ 3, 6, 3 }, JooneTools.LOGISTIC);
			network.getMonitor().setSingleThreadMode(singleThreadMode);
		}

		
		public Vector3f getMovement() {
			return inputPositionVector;
		}

		
		public void setMovement(Vector3f movement) {
			this.inputPositionVector = movement;
		}

		
		public Vector3f updateMovement() {
			inputPosition = NeuroUtil.addRow(NeuroUtil.vectorToArray(inputPositionVector),null);
			desiredPosition = NeuroUtil.addRow(NeuroUtil.vectorToArray(desiredPositionVector),null);
			Vector3f outputPosition = new Vector3f();
			if (isNetworkTrained == false) {
				double rmse = JooneTools.train(network, inputPosition, desiredPosition,
                    10, 0.01,
                    1, null, false);
            	isNetworkTrained = true;
			}
            
            for (int i = 0; i < inputPosition.length; i++) {
				double[] output = JooneTools.interrogate(network, inputPosition[i]);
				// X
				if (output[NeuroUtil.X_ARRAY] > 0.5f) {
	            	outputPosition.x += 0.0005f;
	            }
				else if (output[NeuroUtil.X_ARRAY] < 0.5f) {
					outputPosition.x -= 0.0005f;
				}
				// Y
				if (output[NeuroUtil.Y_ARRAY] > 0.5f) {
					outputPosition.y += 0.0005f;
	            }
				else if (output[NeuroUtil.Y_ARRAY] < 0.5f) {
					outputPosition.y -= 0.0005f;
				}
				// Y
				if (output[NeuroUtil.Z_ARRAY] > 0.5f) {
					outputPosition.z += 0.0005f;
	            }
				else if (output[NeuroUtil.Z_ARRAY] < 0.5f) {
					outputPosition.z -= 0.0005f;
				}
			}
            return inputPositionVector;
		}

		
		public void setDesiredPosition(Vector3f position) {
			this.desiredPositionVector = position;
			
		}

		
		public Vector3f getDesiredPosition() {
			return desiredPositionVector;
		}
	}

