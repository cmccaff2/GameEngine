package engineTester;

import java.util.ArrayList;

import entities.Entity;
import entities.Light;
import renderEngine.Loader;
import terrain.Terrain;

public class Level {
	
	protected ArrayList<Entity> entities = new ArrayList<Entity>();
	protected ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	protected Light light;

	public Level(Loader loader) {
		
	}
	
	public ArrayList<Entity> getEntities(){
		return this.entities;
	}
	
	public ArrayList<Terrain> getTerrain(){
		return this.terrains;
	}
	
	public Light getLight() {
		return this.light;
	}
}
