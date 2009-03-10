package com.physicalneuro.neuro;

import com.jme.math.Vector2f;

public interface NeuroMovementInterface {
	
	public void setNeuroMovement(Vector2f movement, Vector2f heading);

	public Vector2f getMovement();
	
	public void setMovement(Vector2f movement);
	
	public Vector2f updateMovement();
	
	public void setDesiredPosition(Vector2f position);
	
	public Vector2f getDesiredPosition();
	
}
