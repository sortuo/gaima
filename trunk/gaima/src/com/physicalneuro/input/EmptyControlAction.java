package com.physicalneuro.input;

import gamebase.PhysicalNeuroRun;
import factory.weapons.WeaponCollection;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.intersection.PickData;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.PhysicsCollisionGeometry;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.geometry.PhysicsRay;

/**
 * Actions for game control.
 * 
 * <pre>

 * </pre>
 * 
 * @author T8TSOSO
 * 
 */
public class EmptyControlAction {
	/**
	 * root node of the scene - used for picking.
	 */
	private final Node rootNode;
	/**
	 * helper no the picked node is joined to.
	 */
	private DynamicPhysicsNode myNode;
	
	private final PhysicsSpace physicsSpace;
	
	private EmptyControlAction.PickAction pickAction;
	private EmptyControlAction.MoveAction moveAction;
	private EmptyControlAction.ControlAction Action;
	private InputHandler pickHandler;
	private PhysicalNeuroRun baseGame;
	private Boolean isEditorEnabled = true; 
	
	/**
	 * Constructor of the class.
	 * 
	 * @param input
	 *            where {@link #getInputHandler()} is attached to
	 * @param rootNode
	 *            root node of the scene - used for picking
	 * @param physicsSpace
	 *            physics space to create joints in (picked nodes must reside in
	 *            there)
	 */
	public EmptyControlAction(PhysicalNeuroRun baseGame, InputHandler input, Node rootNode, PhysicsSpace physicsSpace) {
		this(baseGame, input, rootNode, physicsSpace, false);
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param input
	 *            where {@link #getInputHandler()} is attached to
	 * @param rootNode
	 *            root node of the scene - used for picking
	 * @param physicsSpace
	 *            physics space to create joints in (picked nodes must reside in
	 *            there)
	 * @param allowRotation
	 *            true to allow rotation of the picked object
	 */
	public EmptyControlAction(PhysicalNeuroRun baseGame, InputHandler input, Node rootNode, PhysicsSpace physicsSpace,
			boolean allowRotation) {
		this.baseGame = baseGame;
		this.physicsSpace = physicsSpace;
		this.inputHandler = new InputHandler();
		input.addToAttachedHandlers(this.inputHandler);
		this.rootNode = rootNode;
	}

	/**
	 * @return the input handler for this picker
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}

	private InputHandler inputHandler;

	private final Vector2f mousePosition = new Vector2f();

	private class PickAction extends InputAction {

		public void performAction(InputActionEvent evt) {
			if (evt.getTriggerPressed()) {}

		}
	}

	private class MoveAction extends InputAction {

		public void performAction(InputActionEvent evt) {

		}
	}

	/**
	 * Game control actions
	 * 
	 * @author T8TSOSO
	 * 
	 */
	private class ControlAction extends InputAction {

		public void performAction(InputActionEvent evt) {
			char editChar = evt.getTriggerCharacter();
			System.out.println("MoveAction Case " + editChar);

			if (isEditorEnabled) {
				switch (editChar) {
				default:
					break;
				}

			}

		}
	}

}
