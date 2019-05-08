package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import camera.Camera;
import camera.FirstPersonCamera;
import camera.ThirdPersonCamera;
import models.TexturedModel;
import renderEngine.DisplayManager;

public class Player extends Entity{
	
	private float run_speed = (float) 20;
	private float jump_height = (float) 20;
	private float xSensitivity = (float) 0.25;
	private float ySensitivity = (float) 0.5;
	
	private Vector3f dXYZ = new Vector3f(0,0,0);
	private Vector3f drXYZ = new Vector3f(0,0,0);
	private Vector3f directionFacing = new Vector3f(0, 0, -1);

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.increasePosition(0, -3, 0);
	}

	public void move() {
		checkKeyboardInput();
		checkMouseInput();
		determineGaze();
		
		float timePassed = DisplayManager.getFrameTimeSeconds();
		
		this.increasePosition(dXYZ.x * timePassed, dXYZ.y * timePassed, dXYZ.z * timePassed);
		this.increaseRotation(0, -drXYZ.y, 0);
		
		System.out.println(directionFacing.y+ " RotY: "+this.getRotY());
		System.out.println(directionFacing.z+ " RotZ: "+this.getRotZ());
	}
	
	void determineGaze() {
		directionFacing.x = -(float) Math.sin(Math.toRadians(this.getRotY()));
		directionFacing.z = -(float) Math.cos(Math.toRadians(this.getRotY()));
	}
	
	private void checkMouseInput() {
		/* Vertical Mouse movement */
		//drXYZ.x = Mouse.getDY() * ySensitivity;
		/* Horizontal Mouse movement */
		drXYZ.y = Mouse.getDX() * xSensitivity;		
	}
	
	private void checkKeyboardInput() {
		Vector3f position = this.getPosition();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.dXYZ.x = run_speed * directionFacing.x;
			this.dXYZ.z = run_speed * directionFacing.z;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.dXYZ.x = -run_speed/2 * directionFacing.x;
			this.dXYZ.z = -run_speed/2 * directionFacing.z;
		}else { // Decelerate
			if (this.getPosition().y <= -3 && (Math.abs(dXYZ.x) > 0.05 || Math.abs(dXYZ.z) > 0.05)) {
				this.dXYZ.x *= 0.75;
				this.dXYZ.z *= 0.75;
			}else if (this.getPosition().y >= -3 && (Math.abs(dXYZ.x) > 0.05 || Math.abs(dXYZ.z) > 0.05)){
				this.dXYZ.x *= 0.99;
				this.dXYZ.z *= 0.99;
			}else {
				this.dXYZ.x = 0;
				this.dXYZ.z = 0;
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.dXYZ.x = run_speed/2 * directionFacing.z;
			this.dXYZ.z = -run_speed/2 * directionFacing.x;
			
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				this.dXYZ.x = (float) ((run_speed/2 * directionFacing.z + run_speed * directionFacing.x) / Math.sqrt(2));
				this.dXYZ.z = (float) ((-run_speed/2 * directionFacing.x + run_speed * directionFacing.z) / Math.sqrt(2));
			}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				this.dXYZ.x = (float) ((run_speed/2 * directionFacing.z + (-run_speed/2) * directionFacing.x) / Math.sqrt(3));
				this.dXYZ.z = (float) ((-run_speed/2 * directionFacing.x + (-run_speed/2) * directionFacing.z) / Math.sqrt(3));
			}
				
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.dXYZ.x = -run_speed/2 * directionFacing.z;
			this.dXYZ.z = run_speed/2 * directionFacing.x;
			
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				this.dXYZ.x = (float) ((-run_speed/2 * directionFacing.z + run_speed * directionFacing.x) / Math.sqrt(2));
				this.dXYZ.z = (float) ((run_speed/2 * directionFacing.x + run_speed * directionFacing.z) / Math.sqrt(2));
			}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				this.dXYZ.x = (float) ((-run_speed/2 * directionFacing.z + (-run_speed/2) * directionFacing.x) / Math.sqrt(3));
				this.dXYZ.z = (float) ((run_speed/2 * directionFacing.x + (-run_speed/2) * directionFacing.z) / Math.sqrt(3));
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && this.getPosition().y == -3) {
			this.dXYZ.y = 1 * jump_height;
		} else if (position.y > -3) {
			this.dXYZ.y -= 0.1;
		} else if (position.y < -3){
			this.dXYZ.y = 0;
			position.y = -3;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.run_speed = (float) 40;
		}else {
			this.run_speed = (float) 20;
		}
	}
	
	public Vector3f getDXYZ() {
		return this.dXYZ;
	}
}
