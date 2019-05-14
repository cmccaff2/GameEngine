package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.TerrainShader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class TerrainRenderer {

	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render (List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			if (terrain.getDistance() > 1000000) {
				continue;
			}
			
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // Render the Vao

			unbindTexturedModel();
		}
	}
	
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID()); // Bind the Vao to OpenGL 
		GL20.glEnableVertexAttribArray(0); // enable attribute list where vertices are stored 
		GL20.glEnableVertexAttribArray(1); // enable attribute list where textures are stored 
		GL20.glEnableVertexAttribArray(2); // enable attribute list where normals are stored 
		bindTexture(terrain);
		shader.loadShineVariablers(1, 0); // Get reflectivity attributes
		
		
	}
	
	private void bindTexture(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.getTexturePack();
		
		// bind background texture to 0
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
	
		// Bind r texture to 1
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	
	}
	
	// Unbind texturedModel once finished rendering it
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0); // Disable attribute list 0
		GL20.glDisableVertexAttribArray(1); // Disable attribute list 1
		GL20.glDisableVertexAttribArray(2); // Disable attribute list 2
		GL30.glBindVertexArray(0); // Unbind Vao once done
	}
	
	// Prepare entity for a textured model
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
