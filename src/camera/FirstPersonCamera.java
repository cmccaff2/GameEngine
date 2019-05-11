package camera;

import entities.Entity;

public class FirstPersonCamera extends Camera{
	private Entity focalPoint;

	public FirstPersonCamera(Entity focalPoint) {
		super();
		this.focalPoint = focalPoint;
	}
	
	public void move(float mouseDX, float mouseDY){
		// Set position to that of the entity focused on
		this.position = focalPoint.getPosition();
		
		// Rotate camera view to match direction of entity
		super.rotate(mouseDY * this.ySensitivity, 0, 0);
		this.yaw = -focalPoint.getRotY();
	}

}
