package factory.other;

import java.util.Random;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;

public class NodeBuilder {
	
	private static final Vector3f ALIGN_X = new Vector3f(0.5f,5,5);
	private static final Vector3f ALIGN_Z = new Vector3f(5,5,0.5f);
	
    public static void addFloor(PhysicsSpace space, Node rootNode) {
        StaticPhysicsNode staticNode = space.createStaticNode();

        Box floorBox = new Box( "floor", new Vector3f(), 100, 1, 100 );
        floorBox.setModelBound( new BoundingBox() );
        floorBox.updateModelBound();
        staticNode.attachChild( floorBox );

        rootNode.attachChild( staticNode );

        staticNode.getLocalTranslation().set( 0, -10, 0 );
        staticNode.updateGeometricState( 0, false );

        staticNode.generatePhysicsGeometry();
    }


    public static void addWall(PhysicsSpace space, Node rootNode, Vector3f position, Vector3f rotation, boolean isStatic) {
    	// Create a static or dynamic node
        PhysicsNode node = (isStatic ? space.createStaticNode():space.createDynamicNode());

        Box wallBox = new Box( "wall", new Vector3f(), rotation.x, rotation.y,rotation.z );
        wallBox.setModelBound( new BoundingBox() );
        wallBox.updateModelBound();
        node.attachChild( wallBox );

        rootNode.attachChild( node );

        node.getLocalTranslation().set( position );
        node.updateGeometricState( 0, false );

        node.generatePhysicsGeometry();
    }

    public static void addLabyrinth1(PhysicsSpace space, Node rootNode) {
     NodeBuilder.addWall(space, rootNode, new Vector3f(+10,-5,0), ALIGN_X,true);     
     NodeBuilder.addWall(space, rootNode, new Vector3f(+10,-5,0), ALIGN_Z,true);     
     NodeBuilder.addWall(space, rootNode, new Vector3f(-10,-5,0), ALIGN_Z,true);
    int randomX = 0;
    int randomZ = 0;
    
    Vector3f align = ALIGN_X;
    for (int i = 0; i < 10; i++) {
    	randomX = (int)(Math.random()*50-25);
    	randomZ = (int)(Math.random()*50-25);
    	if (i % 2 ==0){
    		align = ALIGN_X;
    	}
    	else{
    		align = ALIGN_Z;
    	}
    	System.out.println("RND X "+randomX+ " Z "+randomZ);
        NodeBuilder.addWall(space, rootNode, new Vector3f(randomX,-5,randomZ), align,true);     
		
	}
    
    }

}
