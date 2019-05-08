package engineTester;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import camera.Camera;
import camera.FreeCam;
import camera.ThirdPersonCamera;
import entities.Entity;
import entities.Light;
import entities.ObjLoader;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.Terrain;
import textures.ModelTexture;

public class MainLoop {
	
	private static Loader loader;
	
	public static void main(String[] args) {

		DisplayManager.createDisplay();		
		Mouse.setGrabbed(true);
		
		loader = new Loader();
		
		// Create level
		DefaultLevel level = new DefaultLevel(loader);
		
		// Create player
		ModelData data = OBJFileLoader.loadOBJ("cube");
		RawModel playerModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel playerTextured = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("Stone")));
		Player player = new Player(playerTextured, new Vector3f(0,0,15), 0, 0, 0, 1);
		
		// Set up camera
		ThirdPersonCamera camera = new ThirdPersonCamera(player);
		//FreeCam camera = new FreeCam();
		
		// Get level entities
		ArrayList<Entity> entities = level.getEntities();
		
		// Get level terrain
		ArrayList<Terrain> terrains = level.getTerrain();
		
		// Get level Lighting
		Light light = level.getLight();
		
		MasterRenderer renderer = new MasterRenderer();
		while(!Display.isCloseRequested()) {

			//game logic
			player.move();
			camera.move();
			
			// Rendering
			for (Entity entitiy : entities) { // Render entities
				renderer.processEntity(entitiy);
			}
			
			for (Terrain terrain : terrains) { // Render terrain
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
	
	
	/*public static Entity buildPlane(int x, int y, int z) {
		// Create a rectangle made from 2 triangles
				float[] vertices = {			
						-0.5f,0.5f,0,	
						-0.5f,-0.5f,0,	
						0.5f,-0.5f,0,	
						0.5f,0.5f,0,		
				};
				
				float[] textureCoords = {
						0,0,
						0,1,
						1,1,
						1,0									
				};
				
				int[] indices = {
						0,1,3,	
						3,1,2,	
				};
				
				RawModel model = loader.loadToVAO(vertices, textureCoords, indices); // load vertices into a vao to create a model
				ModelTexture texture = new ModelTexture(loader.loadTexture("minecraft_dirt"));
				TexturedModel texturedModel = new TexturedModel(model,texture);
				
				Entity plane = new Entity(texturedModel, new Vector3f(x,y,z),0,0,0,1);
				return plane;
	}*/

}
