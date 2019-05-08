package camera;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import toolbox.Maths;

public class ThirdPersonCamera extends Camera{
	private Player focalPoint;
	private float distanceToFocal = 50;
	private float angleAroundPlayer = 0;
	
	public ThirdPersonCamera(Player focalPoint) {
		this.focalPoint = focalPoint;
		
		this.roll = 0;
		this.xSensitivity = (float) 0.25;
		this.ySensitivity = (float) 0.5;
	}
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
	
		this.yaw = 0 - (focalPoint.getRotY() + angleAroundPlayer);
	
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
	}
	
	private void calculatePitch() {
		float mouseY = Mouse.getDY();
		if (Mouse.isButtonDown(1)){
			float pitchChange = mouseY;
			this.pitch-= pitchChange;
		}
		
		if (this.pitch < 0) {this.pitch = 0;}
		if (this.pitch > 90) {this.pitch = 90;}
	}
	
	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(1)){
			float yawChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer-= yawChange;
		}
	}
	
}
