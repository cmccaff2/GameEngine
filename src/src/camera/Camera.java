package camera;

import org.lwjgl.util.vector.Vector3f;

public class Camera {

	protected Vector3f position = new Vector3f(0,0,15);
	protected Vector3f gaze = new Vector3f(0,0,0);
	protected Vector3f up = new Vector3f(0,0,1);
	protected float pitch;
	protected float yaw;
	protected float roll;
	protected float xSensitivity;
	protected float ySensitivity;
	
	public Camera() {
		this.roll = 0;
		this.xSensitivity = (float) 0.25;
		this.ySensitivity = (float) 0.5;
	}
	
	public void move(float dx, float dy, float dz) {
		this.position.x +=dx;
		this.position.y +=dy;
		this.position.z +=dz;
	}
	
	public void rotate(float pitch, float yaw, float roll) {
		this.pitch -= pitch; // Vertical mouse movement
		this.yaw += yaw; // Horizontal mouse movement
		this.roll += roll;
		
		// Limit vertical mouse movement
		if (this.pitch > 90) {
			this.pitch = 90;
		}else if (this.pitch < -90) {
			this.pitch = -90;
		}

		// Wrap pitch
		if (this.yaw > 180) {
			this.yaw = -180;
		}else if (this.yaw < -180) {
			this.yaw = 180;
		}
		
		determineGaze();
	}
	
	
	
	// Determines the direction vector that specifies where the camera is facing based on pitch and yaw
	protected void determineGaze() {
		float yawRad = (float) (this.yaw * Math.PI / 180);
		float pitchRad = (float) (this.pitch * Math.PI / 180);
		
		this.gaze.x = (float) (Math.sin(yawRad) * Math.cos(pitchRad));
		this.gaze.y = (float) -(Math.sin(pitchRad));
		this.gaze.z = (float) -(Math.cos(yawRad) * Math.cos(pitchRad));
	}
	
	// Set the gaze and calculate the correct pitch and yaw to match
	public void setGaze(Vector3f gaze) {
		this.gaze = gaze;
		gaze.normalise();
		
		//todo set pitch and yaw
		
	}

	public Vector3f getPosition() {
		return this.position;
	}
	
	public void setPosition(Vector3f newPos) {
		this.position = newPos;
	}
	
	public void setX(float x) {
		this.position.x = x;
	}
	
	public void setY(float y) {
		this.position.y = y;
		this.position = new Vector3f(position.x, y, position.z);
	}
	
	public void setZ(float z) {
		this.position.z = z;
	}

	public float getPitch() {
		return this.pitch;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getRoll() {
		return this.roll;
	}
	
	public Vector3f getGaze() {
		return this.gaze;
	}
	
	public Vector3f getUp() {
		return this.up;
	}
	
	public Vector3f getDxyz() {
		return this.up;
	}

	public void move(float mouseDX, float mouseDY) {
		// TODO Auto-generated method stub
		
	}
	

	
}
