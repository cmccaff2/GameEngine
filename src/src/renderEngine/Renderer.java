package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

//Render models from vaos
public class Renderer {
	
	private StaticShader shader;
	
	public Renderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		GL11.glEnable(GL11.GL_CULL_FACE); // Disable rendering faces directed away from camera
		GL11.glCullFace(GL11.GL_BACK); // Cull non visible faces
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	// Called every frame to prepare OpenGL to render
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(1, 1, 1, 1); // background color
	}
	
	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model:entities.keySet()) { // For each textured model
			prepareTexturedModel(model); // Prepare the model
			List<Entity> batch = entities.get(model); // Get all entities that use the model
			for (Entity entity:batch) { // Prepare each entity
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // Render the Vao
			}
			unbindTexturedModel();
		}
	}
	
	
	private void prepareTexturedModel(TexturedModel texturedModel) {
		RawModel rawModel = texturedModel.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID()); // Bind the Vao to OpenGL 
		GL20.glEnableVertexAttribArray(0); // enable attribute list where vertices are stored 
		GL20.glEnableVertexAttribArray(1); // enable attribute list where textures are stored 
		GL20.glEnableVertexAttribArray(2); // enable attribute list where normals are stored 
		
		ModelTexture texture = texturedModel.getTexture();
		shader.loadShineVariablers(texture.getShineDamper(), texture.getReflectivity()); // Get reflectivity attributes
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
		
	}
	
	// Unbind texturedModel once finished rendering it
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0); // Disable attribute list 0
		GL20.glDisableVertexAttribArray(1); // Disable attribute list 1
		GL20.glDisableVertexAttribArray(2); // Disable attribute list 2
		GL30.glBindVertexArray(0); // Unbind Vao once done
	}
	
	// Prepare entity for a textured model
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
		
	
	// Render a raw model
	public void render(RawModel model) {
		GL30.glBindVertexArray(model.getVaoID()); // Bind the Vao to OpenGL 
		GL20.glEnableVertexAttribArray(0); // enable attribute list where vertices are stored 
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // Render the Vao
		GL20.glDisableVertexAttribArray(0); // Disable attribute list 0
		GL30.glBindVertexArray(0); // Unbind Vao once done
	}

}
