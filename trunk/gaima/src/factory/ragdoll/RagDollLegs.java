/*
 * Copyright (c) 2005-2007 jME Physics 2
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of 'jME Physics 2' nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package factory.ragdoll;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Capsule;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.JointAxis;
import com.jmex.physics.PhysicsSpace;

/**
 * Builds a ragdoll with legs looking like this:
 * <pre>
 *           -          lowerTorso
 *          l l         lLeg, rLeg
 * </pre>
 * Use {@link #getRagdollNode()} to obtain a node to attach to your scene.
 * @author reddev
 */
public class RagDollLegs {

    private Node ragdollNode;
    private PhysicsSpace physicsSpace;

    public RagDollLegs( PhysicsSpace physicsSpace ) {
        this.physicsSpace = physicsSpace;
        ragdollNode = new Node();
        buildRagdoll();
    }

    public void buildRagdoll() {
/*        DynamicPhysicsNode lowerTorso = physicsSpace.createDynamicNode();
        lowerTorso.setName("lowerTorso");
        ragdollNode.attachChild( lowerTorso );
        final Capsule lowerTorsoCapsule = new Capsule( "lowerTorso", 9, 9, 9, .3f, 1.5f );
        lowerTorsoCapsule.setModelBound( new BoundingSphere() );
        lowerTorsoCapsule.updateModelBound();
        lowerTorso.attachChild( lowerTorsoCapsule );
        lowerTorso.generatePhysicsGeometry();
        lowerTorso.getLocalTranslation().set( 0, -1.5f, 0 );
*/
        DynamicPhysicsNode lowerTorso = createNodeWithCapsule( "torsolower", .3f, 1.5f, Vector3f.UNIT_Z, 0, -1.5f, 0f );

        DynamicPhysicsNode lLegUpper = createNodeWithCapsule( "lleg", .3f, 1.6f, null, -.75f, -2.5f, 0f );
        DynamicPhysicsNode rLegUpper = createNodeWithCapsule( "rleg", .3f, 1.6f, null, .75f, -2.5f, 0f );


        join( lowerTorso, lLegUpper, new Vector3f( -.75f, 0f, 0 ), Vector3f.UNIT_X, 0f, 1.8f );
        join( lowerTorso, rLegUpper, new Vector3f( .75f, 0f, 0f ), Vector3f.UNIT_X, 0f, 1.8f );

    }

    private DynamicPhysicsNode createNodeWithCapsule( String name, float radius, float height, Vector3f rotate90Axis,
                                                      float x, float y, float z ) {
        DynamicPhysicsNode node = physicsSpace.createDynamicNode();
        node.setName(name);
        final Capsule capsule = new Capsule( name, 9, 9, 9, radius, height );
        capsule.setModelBound( new BoundingBox() );
        capsule.updateModelBound();
        node.attachChild( capsule );
        if ( rotate90Axis != null ) {
            capsule.getLocalRotation().fromAngleAxis( FastMath.PI / 2, rotate90Axis );
        }
        node.generatePhysicsGeometry();
        node.getLocalTranslation().set( x, y, z );
        //node.setMass(100f);
        ragdollNode.attachChild( node );
        return node;
    }

    public void join( DynamicPhysicsNode node1, DynamicPhysicsNode node2, Vector3f anchor, Vector3f direction, float min, float max ) {
        Joint joint = physicsSpace.createJoint();
        joint.attach( node1, node2 );
        joint.setAnchor( anchor );
        JointAxis leftShoulderAxis = joint.createRotationalAxis();
        leftShoulderAxis.setDirection( direction );
        leftShoulderAxis.setPositionMinimum( min );
        leftShoulderAxis.setPositionMaximum( max );
    }

    public Node getRagdollNode() {
        return ragdollNode;
    }

}
