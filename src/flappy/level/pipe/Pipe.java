package flappy.level.pipe;

import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.VertexArray;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;
import flappy.level.GameObject;
public class Pipe implements GameObject{

	private Vector3f position = new Vector3f();
	private Matrix4f ml_matrix;
	
	private static float width = 1.5f, height = 8.0f;
	private static Texture texture;
	private static VertexArray mesh;
	
	private boolean passed = false;
	
	public boolean isPassed() { 
		       return passed; 
		       }
	
	public void setPassed(boolean passed) { 
		      this.passed = passed; 
		      }
	
	public static void create() {
		float[] vertices = new float[] {
			0.0f, 0.0f, 0.1f,
			0.0f, height, 0.1f,
			width, height, 0.1f,
			width, 0.0f, 0.1f
		};
			
		byte[] indices = new byte[] {
			0, 1, 2,
			2, 3, 0
		};
		
		float[] tcs = new float[] {
			0, 1,
			0, 0,
			1, 0,
			1, 1
		};
		
		mesh = new VertexArray(vertices, tcs, indices);
		texture = TextureLoader.load("res/pipe.png");		
	}
	
	public Pipe(float x, float y) {
		position.x = x;
		position.y = y;
		ml_matrix = Matrix4f.translate(position);
	}
	
	@Override
	public float getX() {
		return position.x;
	}
	@Override
	public float getY() {
		return position.y;
	}
	
	public Matrix4f getModelMatrix() {
		return ml_matrix;
	}
	
	public static VertexArray getMesh() {
		return mesh;
	}
	
	public static Texture getTexture() {
		return texture;
	}
	
	public static float getWidth() {
		return width;
	}
	
	public static float getHeight() {
		return height;
	}
}