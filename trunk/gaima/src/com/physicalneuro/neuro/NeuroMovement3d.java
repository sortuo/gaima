package com.physicalneuro.neuro;

import java.io.File;
import java.util.Arrays;

import org.joone.engine.FullSynapse;
import org.joone.engine.Layer;
import org.joone.engine.LinearLayer;
import org.joone.engine.Monitor;
import org.joone.engine.SigmoidLayer;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.helpers.factory.JooneTools;
import org.joone.io.FileInputSynapse;
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

	    public NeuroMovement3d() {
	    }
	    
		
		public void setNeuroMovement(Vector3f movement, Vector3f heading ) {
			this.inputPositionVector = movement;
			this.inputHeadingVector = heading;
			network = new NeuralNet();
			configure();
			learn();
		}

		
		public void configure() {
			// create layers
			Layer l_input = new LinearLayer();
			Layer l_hidden = new SigmoidLayer();
			Layer l_output = new SigmoidLayer();
			
			// name them for debugging
		    l_input.setLayerName("input");
		    l_hidden.setLayerName("hidden");
		    l_output.setLayerName("output");
		    
		    // define the number of neurons in each layer
		    l_input.setRows(3);
		    l_hidden.setRows(10);
		    l_output.setRows(1);
			
		    // define the synapse sets
		    FullSynapse synapses_input_2_hidden = new FullSynapse();
		    FullSynapse synapses_hidden_2_output = new FullSynapse();
		    
		    // name the synapse sets
		    synapses_input_2_hidden.setName("input to hidden");
		    synapses_hidden_2_output.setName("hidden to output");
		    
		    /*
		     * Think about this as a synapses with two ends: input and output.  We tell the layers which end to connect to.
		     * I will have to extend this functionality to include reverse connections.
		     */
		    // connect the input and hidden layers
		    l_input.addOutputSynapse(synapses_input_2_hidden);
		    l_hidden.addInputSynapse(synapses_input_2_hidden);
		    
		    // connect the hidden and output layers
		    l_hidden.addOutputSynapse( synapses_hidden_2_output);
		    l_output.addInputSynapse(  synapses_hidden_2_output);
				    
		    // the NeuralNetwork object is used to contain the layers
		    network.addLayer( l_input , NeuralNet.INPUT_LAYER );
		    network.addLayer( l_hidden, NeuralNet.HIDDEN_LAYER );
		    network.addLayer( l_output, NeuralNet.OUTPUT_LAYER );
		    
		    // now we configure a trainer
		    TeachingSynapse trainer = new TeachingSynapse();
		    
		    // trainer needs to know the output to calculate error
		    network.getOutputLayer().addOutputSynapse(trainer);
		    
		    // specify this trainer as the NN's teacher
		    network.setTeacher( trainer );

		}

		public void learn() {
		    // specify the input
		    FileInputSynapse inputStream = new FileInputSynapse();
		    inputStream.setAdvancedColumnSelector("1-3");  		// columns 1 through 3 are the input
		    inputStream.setInputFile( new File( "input.data" ) );
		    
		    // connect the inputStream to the input synapses
		    // input synapses connect 1:1 instead of 1:many
		    network.getInputLayer().addInputSynapse( inputStream );
		    
		    // training set
		    FileInputSynapse samples = new FileInputSynapse();
		    samples.setAdvancedColumnSelector("4-6");					// column 4-6 is the "answer"
		    samples.setInputFile( new File( "input.data"  ) );
		    
		    
		    // give the sample set to the trainer
		    network.getTeacher().setDesired(samples);
		       
		    Monitor monitor = network.getMonitor();
		    monitor.setLearningRate(0.5);
		    monitor.setMomentum(0.3);  // used for backprop

		    // tell the monitor to send notifications back to this app
		    //monitor.addNeuralNetListener(this);
		    
		    // configure the monitor
		    monitor.setTrainingPatterns( 5 );
		    monitor.setTotCicles( 1000 );
		    monitor.setLearning(true);
		    
		    // init the NN
		    network.go();
		    
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
				// Z
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

