package camera;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import renderEngine.DisplayManager;

public class TopDownCamera extends Camera{
	
	private Player focalPoint;
	private float distanceToFocal = 50;
	
	public TopDownCamera(Player focalPoint) {
		super();
		this.focalPoint = focalPoint;
		this.pitch = 90;
		this.yaw = 0;

	}
	
	public void move() {
		calculateZoom();
		calculatePlayerFacing();
		float verticalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(verticalDistance);
		
	}
	
	private void calculateCameraPosition(float vDistance) {
		Vector3f playerPosition = focalPoint.getPosition();
		
		position.x = playerPosition.x;
		position.z = playerPosition.z;
		position.y = playerPosition.y + vDistance;
	}
	
	
	private float calculateVerticalDistance() {
		return distanceToFocal;
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceToFocal -= zoomLevel;
		if (distanceToFocal < 5) {
			distanceToFocal = 5;
		}
	}
	
	private void calculatePlayerFacing() {
		float xFromPlayer = Mouse.getX() - DisplayManager.getWidth()/2;
		float yFromPlayer = Mouse.getY() - DisplayManager.getHeight()/2;
		
		if (yFromPlayer > 0) {
			focalPoint.setRotY(-(float) Math.toDegrees(Math.atan(xFromPlayer/yFromPlayer)));
		}else {
			focalPoint.setRotY(-(float) (-90 - 90 + Math.toDegrees(Math.atan(xFromPlayer/yFromPlayer))));
		}
	}
	
}
