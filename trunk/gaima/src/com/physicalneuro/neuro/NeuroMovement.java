package com.physicalneuro.neuro;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.PhysicsSpace;

public class NeuroMovement extends NeuroMovement3d {

	private DynamicPhysicsNode movementNode;
	
	private Joint helperJointFrom;
	
	private Joint helperJointTo;

	private DynamicPhysicsNode helperNode;
	
	public DynamicPhysicsNode getObject() {
		return movementNode;
	}

	public void setObject(DynamicPhysicsNode object) {
		this.movementNode = object;
	}

	public void updateNetwork(PhysicsSpace space) {
		if (null == getDesiredPosition()){
			super.setDesiredPosition(movementNode.getLocalTranslation());
		}
		super.setNeuroMovement(movementNode.getLocalTranslation(),/*rotation not in use*/null);
		Vector3f newPosition = super.updateMovement();
		movementNode.addForce(new Vector3f(-1*newPosition.x,-1*newPosition.y,-1*newPosition.z));
		//movementNode.addTorque(new Vector3f(-1*newPosition.x,-1*newPosition.y,0f));
		
/*
		if (null == helperNode){
			helperNode = space.createDynamicNode();
			helperNode.setAffectedByGravity(false);
			helperNode.setActive(true);
			helperNode.setLocalTranslation(new Vector3f(newPosition.x,newPosition.y,0));
		}
		if (null == helperJointFrom){
			helperJointFrom = space.createJoint();
		}
		if (null == helperJointTo){
			helperJointTo = space.createJoint();
			helperJointTo.setSpring(10,2);
		}
		helperJointFrom.setAnchor(movementNode.getLocalTranslation());
		helperJointTo.setAnchor(new Vector3f(newPosition.x,newPosition.y,movementNode.getLocalTranslation().z));
		helperJointFrom.attach(movementNode);
		helperJointTo.attach(helperNode);

		helperJointTo.attach(helperNode, movementNode);
*/
		System.out.println("Neuro update at " + System.currentTimeMillis()
				+ ". Update position " + newPosition.toString());
	}
}
