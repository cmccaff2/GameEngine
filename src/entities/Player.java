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
	
	protected float run_speed = (float) 20;
	protected float jump_height = (float) 20;
	protected float xSensitivity = (float) 0.25;
	
	protected Vector3f dXYZ = new Vector3f(0,0,0);
	protected Vector3f drXYZ = new Vector3f(0,0,0);
	protected Vector3f directionFacing = new Vector3f(0, 0, -1);

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.increasePosition(0, 1, 0);
	}

	public void move(float mouseDX, float terrainHeight) {
		checkKeyboardInput(terrainHeight);
		checkMouseInput(mouseDX);
		determineGaze();
		
		float timePassed = DisplayManager.getFrameTimeSeconds();
		
		this.increasePosition(dXYZ.x * timePassed, dXYZ.y * timePassed, dXYZ.z * timePassed);
		this.increaseRotation(0, drXYZ.y, 0);
		
		if (super.position.y > terrainHeight) {
			this.dXYZ.y -= 0.2;
		} else if (super.position.y < terrainHeight){
			this.dXYZ.y = 0;
			position.y = terrainHeight;
		}
	}
	
	void determineGaze() {
		directionFacing.x = -(float) Math.sin(Math.toRadians(this.getRotY()));
		directionFacing.z = -(float) Math.cos(Math.toRadians(this.getRotY()));
	}
	
	protected void checkMouseInput(float mouseDX) {
		drXYZ.y = -mouseDX * xSensitivity;		
	}
	
	private void checkKeyboardInput(float terrainHeight) {
		//Vector3f position = this.getPosition();
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.dXYZ.x = run_speed * directionFacing.x;
			this.dXYZ.z = run_speed * directionFacing.z;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.dXYZ.x = -run_speed/2 * directionFacing.x;
			this.dXYZ.z = -run_speed/2 * directionFacing.z;
		}else { // Decelerate
			if (this.getPosition().y <= terrainHeight && (Math.abs(dXYZ.x) > 0.05 || Math.abs(dXYZ.z) > 0.05)) {
				this.dXYZ.x *= 0.75;
				this.dXYZ.z *= 0.75;
			}else if (this.getPosition().y > terrainHeight && (Math.abs(dXYZ.x) > 0.05 || Math.abs(dXYZ.z) > 0.05)){
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
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && this.getPosition().y <= terrainHeight + 0.1 && this.getPosition().y >= terrainHeight - 0.1) {
			this.dXYZ.y = 1 * jump_height;
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
	
	public Vector3f getGaze() {
		return this.directionFacing;
	}
}
