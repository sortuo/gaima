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
package gamebase;

import factory.other.NodeBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.AbstractGame;
import com.jme.input.MouseInput;
import com.jme.math.Vector3f;
import com.jme.scene.Text;
import com.physicalneuro.input.Creator;
import com.physicalneuro.input.Editor;

/**
 * GAIMA Graphical Artificial Intelligence MAnagement
 * 
 */
public class PhysicalNeuroRun extends PhysicalNeuroGameHandler {
 
    protected void simpleInitGame() {
        NodeBuilder.addFloor(getPhysicsSpace(), rootNode);
        NodeBuilder.addLabyrinth1(getPhysicsSpace(), rootNode);
        configurePhysicsPicker();
        adjustCameraView();
        new Creator(this, input, rootNode, getPhysicsSpace());

        Text label2 = Text.createDefaultTextLabel( "instructions", "Hit [space] to drop a ragdoll. [v] to toggle physics view." );
        label2.setLocalTranslation( 0, 20, 0 );
        fpsNode.attachChild( label2 );
        
        // Turn Physics off
        //super.setPhysicsSpeed(0);
    }


    private void configurePhysicsPicker() {
        cameraInputHandler.setEnabled( false );
        new Editor(this, input, rootNode, getPhysicsSpace(), true );
        MouseInput.get().setCursorVisible( true );
    }

    private void adjustCameraView() {
        cam.getLocation().y += 10;
        cam.getLocation().z += 10;
        cam.lookAt( new Vector3f(), new Vector3f( 0, 0, -1 ) );
    }
    
    public void cameraPower(boolean onoff){
    	cameraInputHandler.setEnabled( onoff );
    }

    public static void main( String[] args ) {
        Logger.getLogger( "" ).setLevel( Level.WARNING );
        final PhysicalNeuroRun app = new PhysicalNeuroRun();
        app.setDialogBehaviour(AbstractGame.ALWAYS_SHOW_PROPS_DIALOG);
        new Thread() {
            @Override
            public void run() {
                app.start();
            }
        }.start();
    }
}


