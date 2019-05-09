package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

public class TopDownPlayer extends Player{

	public TopDownPlayer(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.jump_height = 10;
		this.run_speed =  10;
	}
	
	public void move(float mouseDX) {
		checkKeyboardInput();
		checkMouseInput(mouseDX);
		determineGaze();
		
		float timePassed = DisplayManager.getFrameTimeSeconds();
		
		this.increasePosition(dXYZ.x * timePassed, dXYZ.y * timePassed, dXYZ.z * timePassed);
		this.increaseRotation(0, -drXYZ.y, 0);
	}

	
	private void checkKeyboardInput() {
		Vector3f position = this.getPosition();
		
		// z movement
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			if (directionFacing.z < 0) { // Facing top of screen
				this.dXYZ.z += run_speed * -Math.abs(directionFacing.z) - run_speed/2 * Math.abs(directionFacing.x);
			}else { // Facing bottom of screen
				this.dXYZ.z += run_speed/2 * -Math.abs(directionFacing.z) - run_speed/2 * Math.abs(directionFacing.x);
			}
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			if (directionFacing.z < 0) { // Facing top
				this.dXYZ.z += run_speed/2 * Math.abs(directionFacing.z) + run_speed/2 * Math.abs(directionFacing.x);
			}else { // Facing bottom
				this.dXYZ.z += run_speed * Math.abs(directionFacing.z) + run_speed/2 * Math.abs(directionFacing.x);
			}
		}
		
		// x movement
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (directionFacing.x < 0) {
				this.dXYZ.x += -run_speed/2 * Math.abs(directionFacing.z) - run_speed * Math.abs(directionFacing.x);
			}else {
				this.dXYZ.x += -run_speed/2 * Math.abs(directionFacing.z) - run_speed/2 * Math.abs(directionFacing.x);
			}	
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if (directionFacing.x > 0) {
				this.dXYZ.x += +run_speed/2 * Math.abs(directionFacing.z) +  run_speed * Math.abs(directionFacing.x);
			}else {
				this.dXYZ.x += +run_speed/2 * Math.abs(directionFacing.z) +  run_speed/2 * Math.abs(directionFacing.x);
			}
		}
		
		if (this.getPosition().y <= 1 && (Math.abs(dXYZ.z) > 0.05 || Math.abs(dXYZ.x) > 0.05)) { // Moving and on ground
			this.dXYZ.z *= 0.75;
			this.dXYZ.x *= 0.75;
		}else if (this.getPosition().y > 1 && (Math.abs(dXYZ.z) > 0.05 || Math.abs(dXYZ.x) > 0.05)){ // Moving and in air
			this.dXYZ.z *= 0.99;
			this.dXYZ.x *= 0.99;
		}else { // Not moving
			this.dXYZ.z = 0;
			this.dXYZ.x = 0;
		}
		
		// Jumping
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && this.getPosition().y == 1) {
			this.dXYZ.y = 1 * jump_height;
		} else if (position.y > 1) {
			this.dXYZ.y -= 0.1;
		} else if (position.y < 1){
			this.dXYZ.y = 0;
			position.y = 1;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.run_speed = (float) 10;
		}else {
			this.run_speed = (float) 5;
		}
	}
}
