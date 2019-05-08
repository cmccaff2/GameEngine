package camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class FirstPersonCamera extends Camera{
	
	public FirstPersonCamera() {
		this.roll = 0;
		this.xSensitivity = (float) 0.25;
		this.ySensitivity = (float) 0.5;
	}
	
	public void move(float dx, float dy, float dz ) {
		position.x +=dx;
		position.y +=dy;
		position.z +=dz;

		//System.out.println("Yaw:"+this.yaw);
		//System.out.println("Pitch:"+this.pitch);
		//System.out.println("X:"+gaze.x);
		//System.out.println("Y:"+gaze.y);
		//System.out.println("Z:"+gaze.z);
	}
	
	public void rotate(float pitch, float yaw, float roll) {
		this.pitch -= pitch;
		this.yaw += yaw;
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

}
