package camera;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;

public class ThirdPersonCamera extends Camera{
	private Entity focalPoint;
	private float distanceToFocal = 50;
	private float angleAroundPlayer = 0;
	
	public ThirdPersonCamera(Entity focalPoint) {
		super();
		this.focalPoint = focalPoint;
		this.pitch = 20;
	}
	
	public void move(float mouseDX, float mouseDY) {
		calculateZoom();
		calculatePitch(mouseDY);
		calculateAngleAroundPlayer(mouseDX);
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
	
		this.yaw = 0 - (focalPoint.getRotY() - angleAroundPlayer);
	
	}
	
	private void calculateCameraPosition(float hDistance, float vDistance) {
		Vector3f playerPosition = focalPoint.getPosition();
		
		float theta = -focalPoint.getRotY() + angleAroundPlayer;
		float offsetX = (float) (hDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (hDistance * Math.cos(Math.toRadians(theta)));
		position.x = playerPosition.x - offsetX;
		position.z = playerPosition.z + offsetZ;
		position.y = playerPosition.y + vDistance;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceToFocal * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceToFocal * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceToFocal -= zoomLevel;
		if (distanceToFocal < 5) {
			distanceToFocal = 5;
		}
	}
	
	private void calculatePitch(float mouseDY) {
		if (Mouse.isButtonDown(1)){
			float pitchChange = mouseDY;
			this.pitch-= pitchChange;
		}
				
		if (this.pitch < 0) {this.pitch = 0;}
		if (this.pitch > 90) {this.pitch = 90;}
	}
	
	private void calculateAngleAroundPlayer(float mouseDX) {
		if (Mouse.isButtonDown(0)){
			float yawChange = mouseDX * 0.3f;
			angleAroundPlayer+= yawChange;
		}
	}
	
}
