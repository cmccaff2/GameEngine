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
	
	public static void main(String[] args) {

		DisplayManager.createDisplay();		
		Mouse.setGrabbed(true);
		
		loader = new Loader();
		
		// Create level
		//RandomLevel level = new RandomLevel(loader);
		DefaultLevel level = new DefaultLevel(loader);
		
		// Create player
		ModelData data = OBJFileLoader.loadOBJ("cube");
		RawModel playerModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel playerTextured = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("Stone")));
		Player player = new Player(playerTextured, new Vector3f(0,0,15), 0, 0, 0, 1);
		
		// Set up camera
		camera = new ThirdPersonCamera(player);
		//FreeCam camera = new FreeCam();
		
		// Get level entities
		ArrayList<Entity> entities = level.getEntities();
		
		// Get level terrain
		//ArrayList<RandomTerrain> terrains = level.getTerrain();
		ArrayList<Terrain> terrains = level.getTerrain();
		
		// Get level Lighting
		Light light = level.getLight();
		
		MasterRenderer renderer = new MasterRenderer();
		while(!Display.isCloseRequested()) {
			
			//game logic
			pollInput(player);
			
			Vector3f playerPosition = player.getPosition();
			float terrainHeight = 0;
			for (Terrain terrain : terrains) { // Calculate height of terrain at player's position
				if(terrain.onTerrain(playerPosition.x, playerPosition.z)) { // If player is on terrain piece
					terrainHeight = terrain.getHeightOfTerrain(playerPosition.x, playerPosition.z);
				}
			}
			
			float mouseDY = Mouse.getDY();
			float mouseDX = Mouse.getDX();
			
			player.move(mouseDX, 1 + terrainHeight);
			camera.move(mouseDX, mouseDY);
			
			// Rendering
			for (Entity entitiy : entities) { // Render entities
				renderer.processEntity(entitiy);
			}
			
			for (Terrain terrain : terrains) { // Render terrain
				terrain.getHeightOfTerrain(player.getPosition().x, player.getPosition().z);
				renderer.processTerrain(terrain);
			}
			
			renderer.processEntity(player);
			
			renderer.render(light, camera); // Render lighting
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
