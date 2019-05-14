package camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class FreeCam extends Camera{
	private float speed = (float) 0.5;
	
	public FreeCam() {
		super();
	}
	
	public void move(float mouseDX, float mouseDY) {
		//System.out.println("Gaze z: "+gaze.z);
		//System.out.println("Gaze.x: "+gaze.x);
		//System.out.println("Pitch: "+this.pitch);
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
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.speed = (float) 0.5;
		}else {
			this.speed = (float) 0.25;
		}
		
		
		super.rotate(mouseDY * ySensitivity, mouseDX * xSensitivity, 0);

	}
}
