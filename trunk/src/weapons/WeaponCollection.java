package weapons;


import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Capsule;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.JointAxis;
import com.jmex.physics.PhysicsSpace;

/**
 * Builds different weapons:
 * <pre>
 * <--------|-- Sword
 * ############ Club
 * -------------------- Spear / Wand
 * *________|-- Mace
 * + Attach spikes or boulders
 * </pre>
 * Use {@link #getWeaponNode()} to obtain a node to attach to your scene.
 * Based on SimpleRagDoll class.
 * @author T8TSOSO
 */
public class WeaponCollection {

    private Node weaponNode;
    private PhysicsSpace physicsSpace;
    private DynamicPhysicsNode joinNode;
    public static final char WEAPON_SWORD = 0x1;
    public static final char WEAPON_CLUB = 0x2;
    public static final char WEAPON_SPEAR = 0x3;
    public static final char WEAPON_WAND = 0x4;
    public static final char WEAPON_MACE = 0x5;
    public static final char WEAPON_FRYINGPAN = 0x6;
    
    private static final char NODE_TYPE_CAPSULE = 0x1; 
    private static final char NODE_TYPE_BOX = 0x2;
    private static final char NODE_TYPE_SPHERE = 0x3;
    
    private char weaponType;
    
    public WeaponCollection( PhysicsSpace physicsSpace, DynamicPhysicsNode joinNode ) {
    	this.physicsSpace = physicsSpace;
    	this.joinNode = joinNode;
    }

    public void buildWeapon() {
        DynamicPhysicsNode handle = physicsSpace.createDynamicNode();
        weaponNode.attachChild( handle );
        final Capsule handleCapsule = new Capsule( "handle", 9, 9, 9, .2f, .1f );
        
        handleCapsule.setModelBound( new BoundingBox() );
        handleCapsule.updateModelBound();
        handle.attachChild( handleCapsule );
        handle.generatePhysicsGeometry();
        handle.getLocalTranslation().set( 0f, 0.1f, 0.4f );
        // Handle -1.4901161E-8, Y=0.10000002, Z=0.4
        // Sword [X=-1.4901161E-8, Y=2.4999995, Z=0.4]
        
        DynamicPhysicsNode weaponStem = null;
        switch (weaponType) {
		case WEAPON_SWORD:
			weaponStem = createNode( "sword", NODE_TYPE_CAPSULE, 0.2f, 4.05f, Vector3f.UNIT_Y, 0f, 2.5f, 0.4f );
			break;

		default:
			break;
		}
        
                
        Joint handleJoint = physicsSpace.createJoint();
        handleJoint.attach( joinNode, handle );
        handleJoint.setAnchor(joinNode.getLocalTranslation());
        /*
        JointAxis handleJointAxis = handleJoint.createTranslationalAxis();
        handleJointAxis.setDirection( Vector3f.UNIT_Y );
        handleJointAxis.setPositionMinimum( 0 );
        handleJointAxis.setPositionMaximum( 0 );    
        */
        Joint stemJoint = physicsSpace.createJoint();
        stemJoint.attach( handle, weaponStem );
        stemJoint.setAnchor(handle.getLocalTranslation());
        /*JointAxis neckJointAxis = stemJoint.createTranslationalAxis();
        neckJointAxis.setDirection( Vector3f.UNIT_Y );
        neckJointAxis.setPositionMinimum( 0);
        neckJointAxis.setPositionMaximum( 0 );
        */
    }

    private DynamicPhysicsNode createNode( String name, char type, float radius, float height, Vector3f rotate90Axis,
                                                      float x, float y, float z ) {
        DynamicPhysicsNode node = physicsSpace.createDynamicNode();
        TriMesh stem = null;
        switch (type) {
		case NODE_TYPE_CAPSULE:
			stem = new Capsule( name, 9, 9, 9, radius, height );
			break;

		default:
			break;
		}	
        stem.setModelBound( new BoundingBox() );
        stem.updateModelBound();
        node.attachChild( stem );
        if ( rotate90Axis != null ) {
        	stem.getLocalRotation().fromAngleAxis( FastMath.PI / 2, rotate90Axis );
        }
        node.generatePhysicsGeometry();
        node.getLocalTranslation().set( x, y, z );
        weaponNode.attachChild( node );
        return node;
    }

    private void join( DynamicPhysicsNode node1, DynamicPhysicsNode node2, Vector3f anchor, Vector3f direction, float min, float max ) {
        Joint joint = physicsSpace.createJoint();
        joint.attach( node1, node2 );
        joint.setAnchor( anchor );
        JointAxis leftShoulderAxis = joint.createRotationalAxis();
        leftShoulderAxis.setDirection( direction );
        leftShoulderAxis.setPositionMinimum( min );
        leftShoulderAxis.setPositionMaximum( max );
    }

    public Node getWeaponNode(char inputWeaponType) {
    	// TODO Tarkista, pitää tehdä Node vai liitetäänkö suoraan valittuun nodeen?
        TARKISTA weaponNode = new Node();
        weaponType = inputWeaponType;
        buildWeapon();
        return weaponNode;
    }

}
