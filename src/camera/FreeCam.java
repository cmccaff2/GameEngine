package camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class FreeCam extends Camera{
	private float speed = (float) 0.5;
	
	public FreeCam() {
		this.roll = 0;
		this.xSensitivity = (float) 0.25;
		this.ySensitivity = (float) 0.5;
	}
	
	public void move() {
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.x += speed * gaze.x;
			position.y += speed * gaze.y;
			position.z += speed * gaze.z;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.x -= speed * gaze.x;
			position.y -= speed * gaze.y;
			position.z -= speed * gaze.z;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x += speed * gaze.z;
			position.z += -speed * gaze.x;
				
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += -speed * gaze.z;
			position.z += speed * gaze.x;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.y += speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			position.y -= speed;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			this.pitch = 0;
			this.yaw = 0;
		}
		
		/*if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.run_speed = (float) 0.5;
		}else {
			this.run_speed = (float) 0.25;
		}
		
		/* Vertical Mouse movement */
		this.pitch -= Mouse.getDY() * ySensitivity;
		
		/* Horizontal Mouse movement */
		this.yaw += Mouse.getDX() * xSensitivity;
		
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
}
