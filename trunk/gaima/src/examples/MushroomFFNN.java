package examples;
/**
 * @author Sebastian Wilson
 * 
 * @description Using the data set from http://archive.ics.uci.edu/ml/datasets/Mushroom I've built a FFNN
 * on a numerical version of the data set.  The nodes use a sigmoidal activation function and the network is trained
 * by backprop using RMSE.
 * 
 * @date 1 June 2009
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.joone.engine.*;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.io.FileInputSynapse;
import org.joone.net.*;



public class MushroomFFNN implements NeuralNetListener {
	
	private NeuralNet g_nn;
	
	public final static String INPUT_DATASET = "mushroom_numerical.data";
	public final static String TRAINING_DATASET = "mushroom_numerical.data";
	public final static String SAVED_NETWORK = "mushroom.ser";
	
	public final static int INPUT_DATA_SIZE = 3000;
	public final static int TRAINING_DATA_SIZE = 3000;
	
	public final static int TOTAL_TRAINING_ITERATIONS = 10000;
	

	/**
	 * 
	 */
	public static void main(String[] args) {
		
		MushroomFFNN tmp = new MushroomFFNN();
		
		NeuralNet NN;
				
		NeuralNetLoader loader = new NeuralNetLoader( MushroomFFNN.SAVED_NETWORK ); 
		NN = loader.getNeuralNet();
				
		if( NN == null ) {
			NN = new NeuralNet();
		} 
		
		tmp.configure( NN );
		tmp.learn();
	}

	
	/**
	 *  
	 */
	public void configure( NeuralNet p_nn ){
		g_nn = p_nn;
		configure();
	}
	
	public void configure(  ) {
		
		// create layers
		Layer l_input = new LinearLayer();
		Layer l_hidden = new SigmoidLayer();
		Layer l_output = new SigmoidLayer();
		
		// name them for debugging
	    l_input.setLayerName("input");
	    l_hidden.setLayerName("hidden");
	    l_output.setLayerName("output");
	    
	    // define the number of neurons in each layer
	    l_input.setRows(22);
	    l_hidden.setRows(10);  // TODO futz with this
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
	    g_nn.addLayer( l_input , NeuralNet.INPUT_LAYER );
	    g_nn.addLayer( l_hidden, NeuralNet.HIDDEN_LAYER );
	    g_nn.addLayer( l_output, NeuralNet.OUTPUT_LAYER );
	    
	    // now we configure a trainer
	    TeachingSynapse trainer = new TeachingSynapse();
	    
	    // trainer needs to know the output to calculate error
	    g_nn.getOutputLayer().addOutputSynapse(trainer);
	    
	    // specify this trainer as the NN's teacher
	    g_nn.setTeacher( trainer );
	}
	
	public void learn() {
	    // specify the input
	    FileInputSynapse inputStream = new FileInputSynapse();
	    inputStream.setAdvancedColumnSelector("2-23");  		// columns 2 through 23 are the input
	    inputStream.setInputFile( new File( INPUT_DATASET ) );
	    
	    // connect the inputStream to the input synapses
	    // input synapses connect 1:1 instead of 1:many
	    g_nn.getInputLayer().addInputSynapse( inputStream );
	    
	    // training set
	    FileInputSynapse samples = new FileInputSynapse();
	    samples.setAdvancedColumnSelector("1");					// column 1 is the "answer"
	    samples.setInputFile( new File( TRAINING_DATASET ) );
	    
	    
	    // give the sample set to the trainer
	    g_nn.getTeacher().setDesired(samples);
	       
	    Monitor monitor = g_nn.getMonitor();
	    monitor.setLearningRate(0.5);
	    monitor.setMomentum(0.3);  // used for backprop

	    // tell the monitor to send notifications back to this app
	    monitor.addNeuralNetListener(this);
	    
	    // configure the monitor
	    monitor.setTrainingPatterns( INPUT_DATA_SIZE );
	    monitor.setTotCicles( TOTAL_TRAINING_ITERATIONS );
	    monitor.setLearning(true);
	    
	    // init the NN
	    g_nn.go();
	    
	}
	
	public void saveNeuralNet( String pstr_filename )
	{
		try{
			FileOutputStream fo_stream = new FileOutputStream( pstr_filename );
			ObjectOutputStream out = new ObjectOutputStream( fo_stream );
			out.writeObject( g_nn );
			out.close();
			
			
		} catch( FileNotFoundException e ) {
			
		} catch( IOException e ) {
		
		}
	}

	public void cicleTerminated(NeuralNetEvent e) {
		Monitor m = (Monitor)e.getSource();
		int lint_cycle = m.getCurrentCicle();
		if( lint_cycle % 100 == 0 ){
			System.out.println( lint_cycle + "\t- " + m.getGlobalError() );
			saveNeuralNet( SAVED_NETWORK );
		}
		
	}

	public void errorChanged(NeuralNetEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void netStarted(NeuralNetEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void netStopped(NeuralNetEvent e) {
		System.out.println( "netStopped: finished" );
		
	}

	public void netStoppedError(NeuralNetEvent e, String error) {
		System.err.println( "netStoppedError: there was an error" );
		
	}

}

