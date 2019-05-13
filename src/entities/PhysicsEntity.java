package entities;

import java.util.ArrayList;

import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrain.Terrain;

public class PhysicsEntity extends Entity{
	protected float lastBounceTime = 0;
	
	protected float gravity = 9.8f;
	protected float bounciness = 0.8f;
	protected Vector3f dXYZ = new Vector3f(0,0,0); // Positional momentum
	protected Vector3f drXYZ = new Vector3f(0,0,0); // Rotational momentum
	
	public PhysicsEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
	}
	
	public void tick(ArrayList<Terrain> terrains) {
		float time = DisplayManager.getFrameTimeSeconds();
		
		Vector3f terrainNormal = new Vector3f(0, 1, 0);
		float terrainHeight = 0;
		for (Terrain terrain : terrains) { // Calculate height of terrain at entity's position
			if(terrain.onTerrain(this.position.x, this.position.z)) { // If entity is on terrain piece
				terrainHeight = terrain.getHeightOfTerrain(this.position.x, this.position.z);
				terrainNormal = terrain.getSlopeOfTerrain(this.position.x, this.position.z);
			}
		}
		
		//System.out.println("DX: "+dXYZ.x);
		//System.out.println("DY: "+dXYZ.y);
		//System.out.println("DZ: "+dXYZ.z);
		
		// Gravity
		dXYZ.y -= gravity * time; // Fall downwards at 9.8m/s
		
		
		if (this.position.y - 1.3 < terrainHeight) { // Contact with terrain
			this.position.y = terrainHeight + 1.3f;
			
			bounce(terrainNormal);
			
			/* NEXT: rolling from being on uneven terrain*/
		}else {
			
		}
		
		//Decelerate
		dXYZ.x *= 0.999;
		dXYZ.y *= 0.999;
		dXYZ.z *= 0.999;
		/*if (Math.abs(dXYZ.y) < 0.05) {
			dXYZ.y = 0;
		}
		if (Math.abs(dXYZ.x) < 0.05) {
			dXYZ.x = 0;
		}
		if (Math.abs(dXYZ.z) < 0.05) {
			dXYZ.z = 0;
		}*/
		
		increasePosition(dXYZ.x * time, dXYZ.y * time, dXYZ.z * time);
	}
	
	// Bounce the object off a surface with the given normal
	private void bounce(Vector3f normal) {
		float time = Sys.getTime()/100;
		
		// Calculate bounce direction;
		float dXYZdotNormal = Vector3f.dot(dXYZ, normal);
		Vector3f B = new Vector3f(normal.x * dXYZdotNormal, normal.y * dXYZdotNormal, normal.z * dXYZdotNormal);
		Vector3f Bounce = new Vector3f(dXYZ.x, dXYZ.y, dXYZ.z);
		Vector3f.sub(Bounce, B, Bounce);
		Vector3f.sub(Bounce, B, Bounce);
		
		if (time - this.lastBounceTime > 1) {
			Bounce.x = Bounce.x * this.bounciness; // Lose speed upon contact
			Bounce.y = Bounce.y * this.bounciness;
			Bounce.z = Bounce.z * this.bounciness;
		}
		
		if (Math.abs(Bounce.y) + Math.abs(Bounce.x) + Math.abs(Bounce.z) < 1) {
			Bounce.y = 0;
		}
		this.dXYZ = Bounce;
		this.lastBounceTime = time;
	}
	
	public Vector3f getdXYZ() {
		return this.dXYZ;
	}
	
	public Vector3f getdrXYZ() {
		return this.drXYZ;
	}
	
	public void setdXYZ(Vector3f dXYZ) {
		this.dXYZ = dXYZ;
	}
	
	public void setdrXYZ(Vector3f drXYZ) {
		this.drXYZ = drXYZ;
	}

}
