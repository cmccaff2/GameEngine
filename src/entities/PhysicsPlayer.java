package entities;

import java.util.ArrayList;

import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrain.Terrain;

public class PhysicsPlayer extends Player{

	protected float lastBounceTime = 0;
	
	protected float gravity = 9.8f;
	protected float bounciness = 0.8f;
	protected float friction = 0.95f;
	
	public PhysicsPlayer(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
	}
	
	public void move(float mouseDX, float terrainHeight) {
		checkKeyboardInput(terrainHeight);
		checkMouseInput(mouseDX);
		determineGaze();
		
		float timePassed = DisplayManager.getFrameTimeSeconds();
		
		this.increaseRotation(0, drXYZ.y, 0);
		
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
		
		if (this.position.y - 1 < terrainHeight) { // Contact with terrain
			if(Math.abs(dXYZ.x) + Math.abs(dXYZ.y) + Math.abs(dXYZ.z) > 1) {
				bounce(terrainNormal, time);
			}else {
				dXYZ.y = 0;
				
				System.out.println(Vector3f.dot(new Vector3f(terrainNormal.x, 0, terrainNormal.z), new Vector3f(terrainNormal.x, 0, terrainNormal.z)));
				if (Vector3f.dot(new Vector3f(terrainNormal.x, 0, terrainNormal.z), new Vector3f(terrainNormal.x, 0, terrainNormal.z)) > 0.1) {
					dXYZ.x += gravity * terrainNormal.x * time * friction;
					dXYZ.z += gravity * terrainNormal.z * time * friction;
				}else {
					dXYZ.x = 0;
					dXYZ.z = 0;
				}

			}
			
			roll(terrainNormal);
			
			//dXYZ.x -= gravity * terrainNormal.x * time;
			//dXYZ.y -= gravity * terrainNormal.y * time;
			//dXYZ.z += gravity * terrainNormal.z * time * friction;
			/* NEXT: rolling from being on uneven terrain*/
			
			this.position.y = terrainHeight + 1;

		}
		dXYZ.y -= gravity * time; // Fall downwards at 9.8m/s

		//Decelerate
		dXYZ.x *= 0.999;
		dXYZ.y *= 0.999;
		dXYZ.z *= 0.999;
		
		drXYZ.x *= 0.999;
		drXYZ.y *= 0.999;
		drXYZ.z *= 0.999;
		
		increasePosition(dXYZ.x * time, dXYZ.y * time, dXYZ.z * time);
		increaseRotation(drXYZ.x * time, drXYZ.y * time, drXYZ.z * time);

	}
	
	// Bounce the object off a surface with the given normal
	private void bounce(Vector3f normal, float time) {
		float bounceTime = Sys.getTime()/100;
		
		// Calculate bounce direction;
		float dXYZdotNormal = Vector3f.dot(dXYZ, normal);
		Vector3f B = new Vector3f(normal.x * dXYZdotNormal, normal.y * dXYZdotNormal, normal.z * dXYZdotNormal);
		Vector3f Bounce = new Vector3f(dXYZ.x, dXYZ.y, dXYZ.z);
		Vector3f.sub(Bounce, B, Bounce);
		Vector3f.sub(Bounce, B, Bounce);

		if (bounceTime - this.lastBounceTime > 1) {
			Bounce.x = Bounce.x * this.bounciness; // Lose speed upon contact
			Bounce.y = Bounce.y * this.bounciness;
			Bounce.z = Bounce.z * this.bounciness;
		}
		
		/*if (Math.abs(Bounce.y) < 1) {
			Bounce.y = 0;
		}*/
		
		this.dXYZ = Bounce;
		this.lastBounceTime = bounceTime;
	}
	
	private void roll(Vector3f normal) {
		
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
