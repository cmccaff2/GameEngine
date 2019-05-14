package terrain;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class Terrain {
	
	private static final float SIZE = 32;
	
	private float x;
	private float z;
	private double distance;
	protected RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	protected float[][] heights;

	
	public Terrain (int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
	}
	
	public static float getSize() {
		return SIZE;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
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
	
	public void setModel(RawModel model) {
		this.model = model;
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

	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.getX();
		float terrainZ = worldZ - this.getZ();
		float gridSquareSize = SIZE / ((float)heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize); // Find which grid of the terrain this point falls on
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		
		float xCoord = (terrainX % gridSquareSize)/gridSquareSize; // Find where on the grid the point is from (0,0) to (1,1)
		float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
		float height;
		
		if (xCoord <= (1-zCoord)) {
			height = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			height = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}

		return height;

	}
	
	public Vector3f getSlopeOfTerrain(float worldX, float worldZ) {
		int terrainX = (int) (worldX - this.getX());
		int terrainZ = (int) (worldZ - this.getZ());
		float gridSquareSize = SIZE / ((float)heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize); // Find which grid of the terrain this point falls on
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return new Vector3f(0,0,0);
		}
		
		float xCoord = (terrainX % gridSquareSize)/gridSquareSize; // Find where on the grid the point is from (0,0) to (1,1)
		float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;

		Vector3f slope;
		
		if (xCoord <= (1-zCoord)) { // Top left triangle
			slope = Maths.calculateSlope(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1));
		} else { // Bottom right triangle
			slope = Maths.calculateSlope(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1));
		}

		return slope;

	}

}
