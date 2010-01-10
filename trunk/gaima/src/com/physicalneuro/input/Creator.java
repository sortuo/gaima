package com.physicalneuro.input;

import factory.ragdoll.RagDollLegs;
import factory.ragdoll.SimpleRagDoll;
import gamebase.PhysicalNeuroRun;

import com.jme.bounding.BoundingSphere;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;

/**
 * Creates physical objects.
 * 
 * <pre>
 * Keymappings: 
 * 2 and SPACE Create RagDoll
 * 1 Create RagDoll Legs
 * 0 Create Ball
 * </pre>
 * 
 * @author T8TSOSO
 * 
 */
public class Creator {
	/**
	 * Constructor of the class.
	 * 
	 * @param baseGame 
	 * 			  base for physics space
	 * @param input
	 *            where {@link #getInputHandler()} is attached to
	 * @param rootNode
	 *            root node of the scene
	 * @param physicsSpace
	 *            physics space to create joints in (picked nodes must reside in
	 *            there)
	 */
	public Creator(PhysicalNeuroRun baseGame, InputHandler input, Node rootNode, PhysicsSpace physicsSpace) {
		registerCreateAction(baseGame, input, rootNode, physicsSpace);
	}
	
    private void registerCreateAction(final PhysicalNeuroRun baseGame, InputHandler input, final Node rootNode, PhysicsSpace physicsSpace) {
        InputAction createRagdollAction = new InputAction() {
            public void performAction( InputActionEvent evt ) {
                if ( evt == null || evt.getTriggerPressed() ) {
                    SimpleRagDoll sr2 = new SimpleRagDoll( baseGame.getPhysicsSpace() );
                    sr2.getRagdollNode().getLocalTranslation().y += 5;
                    rootNode.attachChild( sr2.getRagdollNode() );
                    rootNode.updateGeometricState( 0, false );
                    rootNode.updateRenderState();
                    // set the camera to chase ragdoll
                    /*cameraInputHandler = new ChaseCamera( cam, sr2.getRagdollNode());
                    input.addToAttachedHandlers(cameraInputHandler);
                	*/
                }
            }
        };
        
        InputAction createRagdollLegsAction = new InputAction() {
            public void performAction( InputActionEvent evt ) {
                if ( evt == null || evt.getTriggerPressed() ) {
                	RagDollLegs legs = new RagDollLegs( baseGame.getPhysicsSpace() );
                    legs.getRagdollNode().getLocalTranslation().y += 2;
                    rootNode.attachChild( legs.getRagdollNode() );
                    rootNode.updateGeometricState( 0, false );
                    rootNode.updateRenderState();
                }
            }
        };

        InputAction createBallAction = new InputAction() {
            public void performAction( InputActionEvent evt ) {
                if ( evt == null || evt.getTriggerPressed() ) {
                    DynamicPhysicsNode ball = baseGame.getPhysicsSpace().createDynamicNode();
                    Node ballNode = new Node();
                    ballNode.attachChild( ball );
                    final Sphere ballSphere = new Sphere( "ball", 12, 12, 0.5f );
                    ballSphere.setModelBound( new BoundingSphere() );
                    ballSphere.updateModelBound();
                    ball.attachChild( ballSphere );
                    ball.generatePhysicsGeometry();
                    ball.getLocalTranslation().set( 0, .7f, 0 ); 
                    rootNode.attachChild(ballNode); 
                }
            }
        };
        
        input.addAction( createRagdollAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_SPACE, InputHandler.AXIS_NONE, false );
        input.addAction( createRagdollLegsAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_1, InputHandler.AXIS_NONE, false );
        input.addAction( createRagdollAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_2, InputHandler.AXIS_NONE, false );
        createRagdollAction.performAction( null );

        input.addAction( createBallAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_0, InputHandler.AXIS_NONE, false );
    }
}
