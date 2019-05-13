package toolbox;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import camera.Camera;


public class Maths {
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos,  viewMatrix, viewMatrix);
		
		return viewMatrix;
	}
	
	public static Vector3f rotatePointAroundOrigin(Vector3f point, float rx, float ry, float rz) {
		Matrix4f rotationMatrix = new Matrix4f();
		rotationMatrix.setIdentity();
		
		rotationMatrix.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0));
		rotationMatrix.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0));
		rotationMatrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
		
		Vector4f point4D = new Vector4f(point.x, point.y, point.z, (float) 1.0);
		
		Matrix4f.transform(rotationMatrix, point4D, point4D);
				
		return new Vector3f (point4D.x, point4D.y, point4D.z);
	}
	
	public static Vector3f normalize(Vector3f vector) {
		Vector3f normalized = new Vector3f();
		
		float VectorLength = (float) Math.sqrt(Vector3f.dot(vector, vector));
		normalized.x = vector.x/VectorLength;
		normalized.y = vector.y/VectorLength;
		normalized.z = vector.z/VectorLength;
		
		return normalized;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Vector3f calculateSlope(Vector3f p1, Vector3f p2, Vector3f p3) {
		// Calculate the normal of the triangle face
		Vector3f U = new Vector3f();
		Vector3f.sub(p2, p3, U);
		Vector3f V = new Vector3f();
		Vector3f.sub(p1, p3, V);
		Vector3f normal = new Vector3f();
		Vector3f.cross(U, V, normal);
		normal.normalise(); 
		
		// Reference normal representing up
		Vector3f surfaceNormal = new Vector3f(0, 1, 0);
		
		// Angle between normals
		double angle = Math.acos(Math.toRadians(Vector3f.dot(normal, surfaceNormal)));
		
		return normal;
	}
}


