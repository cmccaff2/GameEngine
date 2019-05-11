package engineTester;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import terrain.HeightMapTerrain;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class DefaultLevel extends Level{

	public DefaultLevel(Loader loader) {
		super(loader);
		
		// Initialize stall textured model
		ModelData data = OBJFileLoader.loadOBJ("stall");
		RawModel stallModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel stallTextured = new TexturedModel(stallModel, new ModelTexture(loader.loadTexture("stallTexture")));
		
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
				
		// Make the terrain
		for (int i = 0; i <12; i++) {
			for (int j = 0; j <12; j++) {
				int x = i - 6;
				int y = j - 6;
				HeightMapTerrain newTerrain = new HeightMapTerrain(x, y, loader, texturePack, blendMap, "heightmap");
				terrains.add(newTerrain);
			}
		}
		
		// Create a new stall
		Entity stall = new Entity(stallTextured, new Vector3f(0,0,-5), 0, 180, 0, 1);
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
		}

		// Make lights
		light = new Light(new Vector3f(0, 1000, 1000), new Vector3f(1, 1, 1));
	}
}
