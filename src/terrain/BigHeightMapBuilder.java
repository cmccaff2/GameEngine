package terrain;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class BigHeightMapBuilder{
	private static final int SIZE = 32;
	private static final float MAX_HEIGHT = 128;
	private static final float MAX_PIXEL_COLOUR = 256*256;	

	public static ArrayList<Terrain> generateTerrain(Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap){
		ArrayList<Terrain> terrains = new ArrayList<Terrain>(); // List of chunks
		
		BufferedImage image = null;
		WritableRaster raster = null;
		Object pixel = null;
		try {
			image = ImageIO.read(new File("res/" + heightMap + ".png"));
			raster = image.getRaster();
		} catch (IOException e) {
			System.out.println("Could not load heightmap");
			e.printStackTrace();
		}
		int gridX = 0;
		int gridY = 0;		
		
		while(gridY * SIZE < image.getHeight()) {
			while(gridX * SIZE < image.getWidth()) {
				//System.out.println(gridX+ " "+gridY);
				int VERTEX_COUNT = SIZE;
				float[][] heights = new float[VERTEX_COUNT][VERTEX_COUNT];	
				int count = VERTEX_COUNT * VERTEX_COUNT;
				float[] vertices = new float[count * 3];
				float[] normals = new float[count * 3];
				float[] textureCoords = new float[count*2];
				int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
				int vertexPointer = 0;
				
				for(int i = 0; i < VERTEX_COUNT; i++){
					for(int j = 0; j < VERTEX_COUNT; j++){
						vertices[vertexPointer*3] = (float)(j)/(VERTEX_COUNT - 1) * SIZE;
						float height = getHeight(j + gridX * (SIZE-1), i + gridY * (SIZE-1), raster);
						//System.out.println(pixel.toString());
						heights[j][i] = height;
						vertices[vertexPointer*3+1] = height;
						vertices[vertexPointer*3+2] = (float)(i)/(VERTEX_COUNT - 1) * SIZE;
						Vector3f normal = calculateNormal(j + gridX * (SIZE-1), i + gridY * (SIZE-1), raster);
						normals[vertexPointer*3] = normal.x;
						normals[vertexPointer*3+1] = normal.y;
						normals[vertexPointer*3+2] = normal.z;
						textureCoords[vertexPointer*2] = (float)(j)/(VERTEX_COUNT - 1);
						textureCoords[vertexPointer*2+1] = (float)(i)/(VERTEX_COUNT - 1);
						vertexPointer++;
					}
				}
				
				int pointer = 0;
				
				for(int gz=0;gz<VERTEX_COUNT-1;gz++){
					for(int gx=0;gx<VERTEX_COUNT-1;gx++){
						int topLeft = (gz*VERTEX_COUNT)+gx;
						int topRight = topLeft + 1;
						int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
						int bottomRight = bottomLeft + 1;
						indices[pointer++] = topLeft;
						indices[pointer++] = bottomLeft;
						indices[pointer++] = topRight;
						indices[pointer++] = topRight;
						indices[pointer++] = bottomLeft;
						indices[pointer++] = bottomRight;
					}
				}
				
				Terrain chunk = new Terrain(gridX - image.getWidth()/SIZE/2 , gridY - image.getHeight()/SIZE/2, loader, texturePack, blendMap);
				chunk.setModel(loader.loadToVAO(vertices, textureCoords, normals, indices));
				chunk.heights = heights;
				terrains.add(chunk);
				
				gridX++;
			}
			gridX = 0;
			gridY++;
		}

		return terrains;
	}
	
	private static Vector3f calculateNormal(int x, int z, WritableRaster image) {
		float heightL = getHeight(x - 1, z, image);
		float heightR = getHeight(x + 1, z, image);
		float heightD = getHeight(x, z - 1, image);
		float heightU = getHeight(x, z + 1, image);
		Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}
	
	
	private static float getHeight(int x, int y, WritableRaster raster) {

		if(x < 0 || x>= raster.getHeight() || y < 0 || y >= raster.getHeight()) {
			return 0;
		}
		
		float height = raster.getSample(x, y, 0);

		height += MAX_PIXEL_COLOUR/2f;
		height /= MAX_PIXEL_COLOUR/2f;
		height *= MAX_HEIGHT;

		return height;
	}
}
