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
 * Editor class for physics and jme objects.
 * 
 * <pre>
 * TODO Text input handler for file names 
 * TODO Save object to file 
 * TODO Load object from file 
 * TODO Add parent and child objects 
 * TODO Add Joints 
 * TODO Group objects 
 * TODO Edit Breakable joints
 * TODO Edit Scale additions
 * </pre>
 * 
 * @author T8TSOSO
 * 
 */
public class Editor {
	/**
	 * root node of the scene - used for picking.
	 */
	private final Node rootNode;
	/**
	 * helper no the picked node is joined to.
	 */
	private DynamicPhysicsNode myNode;
	/**
	 * joint to link myNode and picked node.
	 */
	private final Joint joint;
	/**
	 * joint to fix myNode in the world.
	 */
	private final Joint worldJoint;
	
	private final PhysicsSpace physicsSpace;
	
	private Editor.PickAction pickAction;
	private Editor.MoveAction moveAction;
	private Editor.EditAction editAction;
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
	public Editor(PhysicalNeuroRun baseGame, InputHandler input, Node rootNode, PhysicsSpace physicsSpace) {
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
	public Editor(PhysicalNeuroRun baseGame, InputHandler input, Node rootNode, PhysicsSpace physicsSpace,
			boolean allowRotation) {
		this.baseGame = baseGame;
		this.physicsSpace = physicsSpace;
		this.inputHandler = new InputHandler();
		input.addToAttachedHandlers(this.inputHandler);
		this.rootNode = rootNode;
		joint = physicsSpace.createJoint();
		if (allowRotation) {
			joint.createRotationalAxis().setDirection(Vector3f.UNIT_X);
			joint.createRotationalAxis().setDirection(Vector3f.UNIT_Y);
			joint.createRotationalAxis().setDirection(Vector3f.UNIT_Z);
		}
		joint.setSpring(2000, 200);
		myNode = physicsSpace.createDynamicNode();
		myNode.setName("Physics Picker Helper Node");
		myNode.setAffectedByGravity(false);
		worldJoint = physicsSpace.createJoint();
		activatePhysicsPicker();
	}

	/**
	 * @return the input handler for this picker
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}

	private InputHandler inputHandler;

	private DynamicPhysicsNode picked;

	private final Vector2f mousePosition = new Vector2f();

	private void activatePhysicsPicker() {
		pickAction = new PickAction();
		inputHandler.addAction(pickAction, InputHandler.DEVICE_MOUSE, 0,
				InputHandler.AXIS_NONE, false);

		moveAction = new MoveAction();
		inputHandler.addAction(moveAction, InputHandler.DEVICE_MOUSE,
				InputHandler.BUTTON_NONE, InputHandler.AXIS_ALL, false);

		editAction = new EditAction();
		inputHandler.addAction(editAction, InputHandler.DEVICE_KEYBOARD,
				InputHandler.BUTTON_ALL, InputHandler.BUTTON_ALL, false);

	}

	private void release() {
		//picked = null;
		joint.detach();
		worldJoint.detach();
		myNode.setActive(false);
	}

	private final Vector3f pickedScreenPos = new Vector3f();
	private final Vector3f pickedWorldOffset = new Vector3f();

	private void attach(DynamicPhysicsNode node) {
		DisplaySystem.getDisplaySystem().getScreenCoordinates(
				node.getWorldTranslation(), pickedScreenPos);
		DisplaySystem.getDisplaySystem().getWorldCoordinates(mousePosition,
				pickedScreenPos.z, pickedWorldOffset);

		picked = node;
		node.localToWorld(node.getCenterOfMass(myNode.getLocalTranslation()),
				myNode.getLocalTranslation());
		pickedWorldOffset.subtractLocal(myNode.getLocalTranslation());
		myNode.setActive(true);
		worldJoint.setAnchor(myNode.getLocalTranslation());
		worldJoint.attach(myNode);
		joint.attach(myNode, node);
		joint.setAnchor(new Vector3f());
	}

	public void delete() {
		inputHandler.removeAction(pickAction);
		inputHandler.removeAction(moveAction);
		myNode.setActive(false);
		myNode.removeFromParent();
		joint.detach();
		joint.setActive(false);
		worldJoint.detach();
		worldJoint.setActive(false);
		picked = null;
	}

	public DynamicPhysicsNode getPickedNode() {
		return picked;
	}

	private final Ray pickRay = new Ray();

	/**
	 * @return true if picking is done with the visuals, false if done with the
	 *         physics representation
	 * @see #setPickModeVisual(boolean)
	 */
	public boolean isPickModeVisual() {
		return pickModeVisual;
	}

	/**
	 * The mode used for picking: visual picking uses the bounding volumes and
	 * visual meshes of scene elements to determine what was clicked by the
	 * user, the physical mode uses the actual physical representations.
	 * 
	 * @param pickModeVisual
	 *            true to switch to visual mode, false to switch to physical
	 *            mode
	 */
	public void setPickModeVisual(boolean pickModeVisual) {
		this.pickModeVisual = pickModeVisual;
	}

	private boolean pickModeVisual = true;

	private class PickAction extends InputAction {

		public void performAction(InputActionEvent evt) {
			if (evt.getTriggerPressed()) {
				DisplaySystem.getDisplaySystem().getWorldCoordinates(
						mousePosition, 0, pickRay.origin);
				DisplaySystem.getDisplaySystem().getWorldCoordinates(
						mousePosition, 0.3f, pickRay.direction);
				pickRay.direction.subtractLocal(pickRay.origin)
						.normalizeLocal();

				if (pickModeVisual) {
					pickVisual();
				} else {
					pickPhysical();
				}
			} else {
				release();
			}
		}
	}

	private final TrianglePickResults pickResults = new TrianglePickResults();

	private void pickVisual() {
		pickResults.clear();
		pickResults.setCheckDistance(true);
		rootNode.findPick(pickRay, pickResults);
		loopResults: for (int i = 0; i < pickResults.getNumber(); i++) {
			PickData data = pickResults.getPickData(i);
			if (data.getTargetTris() != null && data.getTargetTris().size() > 0) {
				Spatial target = data.getTargetMesh().getParentGeom();
				while (target != null) {
					if (target instanceof DynamicPhysicsNode) {
						DynamicPhysicsNode picked = (DynamicPhysicsNode) target;
						attach(picked);
						break loopResults;
					}
					target = target.getParent();
				}
			}
		}
	}

	private PhysicsRay physicsRay;
	private PhysicsCollisionGeometry nearestPickedGeom;
	private float minPickDistance;

	private void pickPhysical() {
		if (physicsRay == null) {
			final StaticPhysicsNode pickNode = myNode.getSpace()
					.createStaticNode();
			pickNode.setActive(false);
			physicsRay = pickNode.createRay("pickRay");
			pickHandler = new InputHandler();
			pickHandler.addAction(new PhysicsPickAction(), physicsRay
					.getCollisionEventHandler(), false);
		}
		physicsRay.getLocalTranslation().set(pickRay.getOrigin());
		physicsRay.getLocalScale().set(pickRay.getDirection()).multLocal(1000);
		physicsRay.updateWorldVectors();

		nearestPickedGeom = null;
		minPickDistance = Float.POSITIVE_INFINITY;
		pickHandler.setEnabled(true);
		myNode.getSpace().pick(physicsRay);
		pickHandler.update(0);
		pickHandler.setEnabled(false);
		physicsRay.getLocalScale().set(1, 1, 1);

		if (nearestPickedGeom != null) {
			attach((DynamicPhysicsNode) nearestPickedGeom.getPhysicsNode());
		}
	}

	private class MoveAction extends InputAction {
		private final Vector3f anchor = new Vector3f();

		public void performAction(InputActionEvent evt) {

			switch (evt.getTriggerIndex()) {
			case 0:
				mousePosition.x = evt.getTriggerPosition()
						* DisplaySystem.getDisplaySystem().getWidth();
				break;
			case 1:
				mousePosition.y = evt.getTriggerPosition()
						* DisplaySystem.getDisplaySystem().getHeight();
				break;
			case 2:
				// move into z direction with the wheel
				if (evt.getTriggerDelta() > 0) {
					pickedScreenPos.z += (1 - pickedScreenPos.z) / 10;
				} else {
					pickedScreenPos.z = (10 * pickedScreenPos.z - 1) / 9;
				}
				break;
			}

			if (picked != null) {
				DisplaySystem.getDisplaySystem().getWorldCoordinates(
						mousePosition, pickedScreenPos.z, anchor);
				myNode.getLocalTranslation().set(
						anchor.subtractLocal(pickedWorldOffset));
				worldJoint.setAnchor(myNode.getLocalTranslation());
				worldJoint.attach(myNode);
			}
		}
	}

	private class PhysicsPickAction extends InputAction {
		public void performAction(InputActionEvent evt) {
			final ContactInfo info = (ContactInfo) evt.getTriggerData();
			PhysicsCollisionGeometry other = info.getGeometry1() == physicsRay ? info
					.getGeometry2()
					: info.getGeometry1();
			if (!other.getPhysicsNode().isStatic()
					&& info.getPenetrationDepth() < minPickDistance) {
				minPickDistance = info.getPenetrationDepth();
				nearestPickedGeom = other;
			}
		}
	}

	/**
	 * Editor actions to move and resize object. Keyboard inputs: + Scale raise
	 * - Scale lower . Parent scale raise , Parent scale lower s Save parent
	 * object to object.sav
	 * 
	 * @author T8TSOSO
	 * 
	 */
	private class EditAction extends InputAction {

		public void performAction(InputActionEvent evt) {
			char editChar = evt.getTriggerCharacter();
			System.out.println("MoveAction Case " + editChar);
			if (editChar == '<'){
				baseGame.cameraPower(isEditorEnabled ? Boolean.TRUE : Boolean.FALSE);
				isEditorEnabled = isEditorEnabled ? Boolean.FALSE : Boolean.TRUE;
				System.out.println("Editor "+ isEditorEnabled);
			}
			if (picked != null && isEditorEnabled) {
				System.out.println("ScaleAction ");
				// TODO KeyInput.KEY_?
				switch (editChar) {
				case '+':
					getPickedNode().getLocalScale().addLocal(0.005f, 0.005f,
							0.005f);
					break;

				case '-':
					getPickedNode().getLocalScale().addLocal(-0.005f, -0.005f,
							-0.005f);
					break;

				case '.':
					getPickedNode().getParent().getLocalScale().addLocal(
							0.005f, 0.005f, 0.005f);
					break;

				case ',':
					getPickedNode().getParent().getLocalScale().addLocal(
							-0.005f, -0.005f, -0.005f);
					break;

				case 'S':
					// TODO Save parent object to file
					// getPickedNode().getParent().getLocalScale().addLocal(-0.005f,-0.005f,-0.005f);
					break;
					
				case '1':
					// Attach sword-weapon
					WeaponCollection collection = new WeaponCollection(physicsSpace);
					collection.getWeaponNode(WeaponCollection.WEAPON_SWORD,getPickedNode());
					collection.joinWeapon(getPickedNode(), new Vector3f(0,0,0), new Vector3f(0.01f,0.01f,0.01f), 0, 0);
					// no child attachment needed
					//getPickedNode().attachChild(sword);
					//getPickedNode().updateGeometricState( 0, false );
					//getPickedNode().updateModelBound();
					//getPickedNode().generatePhysicsGeometry();
					//getPickedNode().updateRenderState();
					break;
					
				case 'w':
					getPickedNode().getLocalTranslation().y += 0.1f;
					//getPickedNode().updateModelBound();
					//getPickedNode().generatePhysicsGeometry();
					break;
					
				case 's':
					getPickedNode().getLocalTranslation().y -= 0.1f;
					break;
					
				case 'a':
					getPickedNode().getLocalTranslation().x -= 0.1f;
					break;
					
				case 'd':
					getPickedNode().getLocalTranslation().x += 0.1f;
					break;
					
				case 'r':
					getPickedNode().getLocalTranslation().z -= 0.1f;
					break;
					
				case 'f':
					getPickedNode().getLocalTranslation().z += 0.1f;
					break;
					
				case 'n':
					baseGame.addNeuroContact(getPickedNode());
					break;
					
				case '?':
					System.out.println("Node name: "+getPickedNode());
					break;

				// Apply force and move to a position
				case '\'':
					getPickedNode().addForce(new Vector3f(4000,10000,0));
					//getPickedNode().addTorque(new Vector3f(0,5000,0));
					//getPickedNode().getLocalTranslation().set(new Vector3f(0,10,0));
					//worldJoint.setAnchor(getPickedNode().getLocalTranslation());
					//worldJoint.attach(getPickedNode());
					break;

				default:
					break;
				}
				System.out.println("LocalTranslation: " + getPickedNode().getLocalTranslation());

			}

		}
	}

}
