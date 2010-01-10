package com.physicalneuro.control;

import gamebase.PhysicalNeuroRun;

import com.jme.input.InputHandler;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;

public class Controller {

	public PhysicalNeuroRun baseGame;
	public InputHandler input;
	public Node rootNode;
	public PhysicsSpace physicsSpace;

	/**
	 * Constructor of the class.
	 * 
	 * @param baseGame
	 *            base for physics space
	 * @param input
	 *            where {@link #getInputHandler()} is attached to
	 * @param rootNode
	 *            root node of the scene
	 * @param physicsSpace
	 *            physics space to create joints in (picked nodes must reside in
	 *            there)
	 */
	public Controller(PhysicalNeuroRun baseGame, InputHandler input,
			Node rootNode, PhysicsSpace physicsSpace) {
		this.baseGame = baseGame;
		this.input = input;
		this.rootNode = rootNode;
		this.physicsSpace = physicsSpace;
	}
	
	public void updateWalker(DynamicPhysicsNode lLeg, DynamicPhysicsNode rLeg){
		lLeg.addForce(lLeg.getLocalTranslation().cross(new Vector3f(0,1f,0)));
		rLeg.addForce(rLeg.getLocalTranslation().cross(new Vector3f(0,1f,0)));
	}
	
	public void updateControllers(){
		
	}
}
