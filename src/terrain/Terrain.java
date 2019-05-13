package terrain;

import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public abstract class Terrain {
	
	private static final float SIZE = 400;
	
	private float x;
	private float z;
	protected RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;

	
	public Terrain (int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
	}
	


	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	public boolean onTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.getX();
		float terrainY = worldZ - this.getZ();
		
		if (terrainX < SIZE && terrainX > 0 && terrainY < SIZE && terrainY > 0) {
			return true;
		}
		
		return false;
	}

	public abstract float getHeightOfTerrain(float x2, float z2);
	
	public abstract Vector3f getSlopeOfTerrain(float worldX, float worldZ);

}
