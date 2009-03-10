package com.physicalneuro.neuro;

import org.joone.engine.NeuralNetEvent;
import org.joone.engine.NeuralNetListener;

public class NeuroListener implements NeuralNetListener {

	@Override
	public void cicleTerminated(NeuralNetEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void errorChanged(NeuralNetEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void netStarted(NeuralNetEvent e) {
		//System.out.print("NET STARTED: "+e.toString()+";"+e.getNeuralNet().toString());

	}

	@Override
	public void netStopped(NeuralNetEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void netStoppedError(NeuralNetEvent e, String error) {
		// TODO Auto-generated method stub

	}

}
