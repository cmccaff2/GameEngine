package engineTester;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import camera.Camera;
import entities.Entity;
import entities.Light;
import entities.PhysicsEntity;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import terrain.Terrain;
import textures.ModelTexture;

public abstract class Level {
	
	protected ArrayList<Entity> entities = new ArrayList<Entity>();
	protected ArrayList<PhysicsEntity> physicsEntities = new ArrayList<PhysicsEntity>();
	protected ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	protected Light light;
	protected Player player;
	protected Loader loader;

	public Level(Loader loader) {
		
	}
	
	public abstract void tick(Camera camera);
	
	public ArrayList<Entity> getEntities(){
		return this.entities;
	}
	
	public ArrayList<PhysicsEntity> getPhysicsEntities(){
		return this.physicsEntities;
	}
	
	public ArrayList<Terrain> getTerrain(){
		return this.terrains;
	}
	
	public Light getLight() {
		return this.light;
	}
	
	public void createPlayer(int x, int y, int z) {
		ModelData data = OBJFileLoader.loadOBJ("cube");
		RawModel playerModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		TexturedModel playerTextured = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("Stone")));
		this.player = new Player(playerTextured, new Vector3f(x,y,z), 0, 0, 0, 1);
	}
	
	public Player getPlayer() {
		return this.player;
	}
}
