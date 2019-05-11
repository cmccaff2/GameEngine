package renderEngine;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;

//Load 3D models into memory by storing positional data in a vao
public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	//loads vertex positions and creates a vao from them
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		vaos.add(vaoID);
		storeDataInAttributeList(0, 3, positions); // inserts positions into slot 0 of attribute list
		storeDataInAttributeList(1, 2, textureCoords); // inserts positions into slot 0 of attribute list
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID,indices.length); // Divide by 3 since each vertex has an x,y,z
	}
	
	// Load a png texture from file and return its ID
	public int loadTexture(String fileName) {
		Texture texture = null;
		
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.6f);
		}catch(IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	
	
	}
	
	// Delete all vaos and vbos created
	public void cleanUp() {
		for(int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture:textures) {
			GL11.glDeleteTextures(texture);
		}
	}

	// Create an empty vao
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays(); // create the vao
		GL30.glBindVertexArray(vaoID); // activate the vao
		return vaoID;
	}
	
	// Put data in an attribute list of the vao
	// attributeNumber : indexed slot in vao
	// coordinates : dimensions to data
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers(); // Create a vbo
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); // Bind the vbo
		FloatBuffer buffer = storeDataInFloatBuffer(data); // Convert data into a FloatBuffer
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); // Put data in vbo, data is static
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0); // Put vbo into VAO attribute list
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind current vbo
	}
	
	
	// When finished with a vao
	private void unbindVAO() {
		GL30.glBindVertexArray(0); // Unbind the current vao
	}
	
	// Create a vbo to store the indices of vertices in the Vao
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers(); // create the vbo
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID); // bind the vbo
		IntBuffer buffer = storeDataInIntBuffer(indices); // put the indices in an intBuffer
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	// floatbuffer to be stored in the vbo
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip(); // finished writing to buffer
		return buffer;
	}
	
}
