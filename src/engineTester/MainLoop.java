package engineTester;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import camera.Camera;
import camera.FirstPersonCamera;
import camera.FreeCam;
import camera.ThirdPersonCamera;
import camera.TopDownCamera;
import entities.Entity;
import entities.Light;
import entities.ObjLoader;
import entities.Player;
import entities.TopDownPlayer;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.HeightMapTerrain;
import terrain.RandomTerrain;
import terrain.Terrain;
import textures.ModelTexture;

public class MainLoop {
	
	private static Loader loader;
	private static Camera camera;
	private static MasterRenderer renderer;
	private static Level level;
	
	public static void main(String[] args) {

		DisplayManager.createDisplay();		
		Mouse.setGrabbed(true);
		
		loader = new Loader();
		
		// Create level
		//RandomLevel level = new RandomLevel(loader);
		level = new DefaultLevel(loader);
		
		// Get player
		Player player = level.getPlayer();
		
		// Set up camera
		camera = new ThirdPersonCamera(player);
		
		// Get level Lighting
		Light light = level.getLight();
		
		// Setup renderer
		renderer = new MasterRenderer();
		

		while(!Display.isCloseRequested()) {
			
			//game logic
			pollInput(player);
			level.tick(camera);
			
			// Rendering
			renderer.render(light, camera); // Render all entities
			DisplayManager.updateDisplay();
			
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				System.exit(0);
			}
		}
		
		renderer.cleanUp();
		loader.cleanUp(); // delete all Vaos and Vbos
		DisplayManager.closeDisplay();

	}
	
	
	private static void pollInput(Player player) {
		if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
			camera = new FirstPersonCamera(player);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_2)) {
			camera = new ThirdPersonCamera(player);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_3)) {
			camera = new FreeCam();
		}
	}

}
