package levels;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import camera.Camera;
import entities.Entity;
import entities.Light;
import entities.PhysicsEntity;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.BigHeightMapBuilder;
import terrain.HeightMapTerrain;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class DefaultLevel extends Level{
	private TexturedModel texturedSphere;
	private boolean Fdown = false;
	private boolean Gdown = false;
	private boolean Hdown = false;
	
	public DefaultLevel(Loader loader) {
		super(loader);
		
		// Initialize stall textured model
		ModelData data = OBJFileLoader.loadOBJ("stall");
		RawModel stallModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel stallTextured = new TexturedModel(stallModel, new ModelTexture(loader.loadTexture("stallTexture")));
		
		// Initialize sphere textured model
		 data = OBJFileLoader.loadOBJ("sphere");
		RawModel sphereModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		this.texturedSphere = new TexturedModel(sphereModel, new ModelTexture(loader.loadTexture("minecraft_dirt")));
		
		// Initialize shiny dragon textured model
		data = OBJFileLoader.loadOBJ("dragon");
		RawModel dragonModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel dragonTextured = new TexturedModel(dragonModel, new ModelTexture(loader.loadTexture("stallTexture")));
		ModelTexture dragonTexture = dragonTextured.getTexture();
		dragonTexture.setShineDamper(30); // Reflectivity
		dragonTexture.setReflectivity(1); // Reflectivity
		
		// Initialize tree textured model
		data = OBJFileLoader.loadOBJ("trunk");
		RawModel treeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel treeTextured = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("stone")));
		
		// Initialize grass textured model
		data = OBJFileLoader.loadOBJ("free_grass");
		RawModel grassModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("free_grass"));
		grassTexture.setTransparency(true);
		grassTexture.setFakeLighting(true);
		TexturedModel grassTextured = new TexturedModel(grassModel, grassTexture );
		
		// Initialize grass2 textured model
		data = OBJFileLoader.loadOBJ("grassModel2");
		RawModel grassModel2 = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		ModelTexture grassTexture2 = new ModelTexture(loader.loadTexture("grassModelTexture2"));
		grassTexture2.setTransparency(true);
		grassTexture2.setFakeLighting(true);
		TexturedModel grassTextured2 = new TexturedModel(grassModel2, grassTexture2 );
		
		// Initialize fern textured model
		data = OBJFileLoader.loadOBJ("fern");
		RawModel fernModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		grassTexture.setTransparency(true);
		grassTexture.setFakeLighting(true);
		TexturedModel fernTextured = new TexturedModel(fernModel, fernTexture );
		
		//*********TERRAIN TEXTURES *********\\
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("Stone"));
			
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		//***********************************\\
				

		terrains = BigHeightMapBuilder.generateTerrain(loader, texturePack, blendMap, "largeTerrain");
		// Create a new stall
		/*Entity stall = new Entity(stallTextured, new Vector3f(0,0,-5), 0, 180, 0, 1);
		entities.add(stall);
		
		// Create a new dragon
		Entity dragon = new Entity(dragonTextured, new Vector3f(0,5,-5), 0, 0, 0, 1);
		entities.add(dragon);
		
		// Create trees
		for (int i = 0; i <30; i++) {
			for (int j = 0; j <30; j++) {
				float x = i*100 - 700 + (float)(Math.random() * 100);
				float y = 0;
				float z = j*100 - 700 + (float)(Math.random() * 100);
				
				for (Terrain terrain : terrains) {
					if (terrain.onTerrain(x, z)) {
						y = (float) (terrain.getHeightOfTerrain(x, z) - 0.5);
					}
				}
				entities.add(new Entity(treeTextured, new Vector3f(x,y,z), -90, 0, (float)(Math.random() * 180), (float) 0.05));
			}
		}
		
		// Create grasses
		/*for (int i = 0; i <70; i++) {
			for (int j = 0; j <70; j++) {
				float x = i*10 - 400 + (float)(Math.random() * 100);
				float y = 0;
				float z = j*10 - 400 + (float)(Math.random() * 100);
				
				for (Terrain terrain : terrains) {
					if (terrain.onTerrain(x, z)) {
						y = (float) (terrain.getHeightOfTerrain(x, z) - 0.1);
					}
				}
				
				entities.add(new Entity(grassTextured, new Vector3f(x,y,z), 0, (float)(Math.random() * 180), 0, (float) 0.5));
			}
		}
		
		// Create grasses 2
		for (int i = 0; i <70; i++) {
			for (int j = 0; j <70; j++) {
				float x = i*10 - 400 + (float)(Math.random() * 100);
				float y = 0;
				float z = j*10 - 400 + (float)(Math.random() * 100);
				
				for (Terrain terrain : terrains) {
					if (terrain.onTerrain(x, z)) {
						y = (float) (terrain.getHeightOfTerrain(x, z) - 0.1);
					}
				}
				
				entities.add(new Entity(grassTextured2, new Vector3f(x,y,z), 0, (float)(Math.random() * 180), 0, (float) 0.5));
			}
		}
		
		// Create ferns
		for (int i = 0; i <70; i++) {
			for (int j = 0; j <70; j++) {
				float x = i*10 - 400 + (float)(Math.random() * 100);
				float y = 0;
				float z = j*10 - 400 + (float)(Math.random() * 100);
				
				for (Terrain terrain : terrains) {
					if (terrain.onTerrain(x, z)) {
						y = (float) (terrain.getHeightOfTerrain(x, z) - 0.1);
					}
				}
				
				entities.add(new Entity(fernTextured, new Vector3f(x,y,z), 0, (float)(Math.random() * 180), 0, (float) 0.5));
			}
		}*/

		// Make lights
		light = new Light(new Vector3f(0, 1000, 1000), new Vector3f(1, 1, 1));
		
		// Initialize a player entity
		createPlayer(1273, 298, 133);

		for (Entity entity : entities) { // Add entities to the renderer
			MasterRenderer.processEntity(entity);
		}
		
		for (Terrain terrain : terrains) {
			MasterRenderer.processTerrain(terrain);
		}
		
		MasterRenderer.processEntity(player);
	}

	@Override
	public void tick(Camera camera) {
		/*Player Movement*/
		Vector3f playerPosition = player.getPosition();
		float terrainHeight = -80;
		for (Terrain terrain : terrains) { // Calculate height of terrain at player's position
			if(terrain.onTerrain(playerPosition.x, playerPosition.z)) { // If player is on terrain piece
				terrainHeight = terrain.getHeightOfTerrain(playerPosition.x, playerPosition.z);
			}
		}
		calculateTerrainDistances(playerPosition);
		
		float mouseDY = Mouse.getDY();
		float mouseDX = Mouse.getDX();
		player.move(mouseDX, player.getScale() + terrainHeight);
		player.tick();
		camera.move(mouseDX, mouseDY);
		/*******************/

		System.out.println("X: "+playerPosition.x);
		System.out.println("Y: "+playerPosition.y);
		System.out.println("Z: "+playerPosition.z);
		
		/* Input */
		if (Keyboard.isKeyDown(Keyboard.KEY_F) && !Fdown) { // Spawn sphere
			Fdown = true;
			fireBall(0, 0, 0);
		}else if (!Keyboard.isKeyDown(Keyboard.KEY_F)){
			Fdown = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_G) && !Gdown) { // Spawn sphere
			Gdown = true;
			fireBall(50, 0, 50);
		}else if (!Keyboard.isKeyDown(Keyboard.KEY_G)){
			Gdown = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_H) && !Hdown) { // Spawn sphere
			Hdown = true;
			createBallAtHeight(terrainHeight);
		}else if (!Keyboard.isKeyDown(Keyboard.KEY_H)){
			Hdown = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) { // Clear spheres
			physicsEntities.clear();
		}
		/*********/
		
		/* Level Physics */
		for (PhysicsEntity physEntity : physicsEntities) {
			physEntity.tick(terrains);
		}
		
		
		/****************/
	}
	
	public void calculateTerrainDistances(Vector3f playerPosition) {
		float terrainSize = Terrain.getSize();
		for (Terrain terrain : terrains) { // Calculate height of terrain at player's position
			float centreX = terrain.getX() + terrainSize/2;
			float centreZ = terrain.getZ() + terrainSize/2;
			terrain.setDistance(Math.pow(playerPosition.x - centreX, 2) + Math.pow(playerPosition.z - centreZ, 2));
		}
	}
	
	public PhysicsEntity createSphere(float x, float y, float z) {
		return new PhysicsEntity(this.texturedSphere, new Vector3f(x, y, z), 0, 0, 0, 0.05f);
	}
	
	public void fireBall(float xSpeed, float ySpeed, float zSpeed) {
		Vector3f position = player.getPosition();
		Vector3f gaze = player.getGaze();

		PhysicsEntity sphere = createSphere(position.x + gaze.x * 6, position.y + 8, position.z + gaze.z * 6);
		sphere.setdXYZ(new Vector3f(gaze.x * xSpeed, gaze.y * ySpeed, gaze.z * zSpeed));
		physicsEntities.add(sphere);
		MasterRenderer.processEntity(sphere);
	}
	
	public void createBallAtHeight(float height) {
		Vector3f position = player.getPosition();
		Vector3f gaze = player.getGaze();

		PhysicsEntity sphere = createSphere(position.x + gaze.x * 6, height, position.z + gaze.z * 6);
		physicsEntities.add(sphere);
		MasterRenderer.processEntity(sphere);
	}
}
