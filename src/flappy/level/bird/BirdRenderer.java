package flappy.level.bird;

import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.VertexArray.VertexArray;
import flappy.maths.Matrix4f;

public class BirdRenderer {
	 private VertexArray mesh;
	    private Texture texture;

	    public BirdRenderer() {
	        float SIZE = 1.0f;
	        float[] vertices = { 
	            -SIZE/2, -SIZE/2, 0.2f,
	            -SIZE/2,  SIZE/2, 0.2f,
	             SIZE/2,  SIZE/2, 0.2f,
	             SIZE/2, -SIZE/2, 0.2f
	        };
	        byte[] indices = { 0,1,2, 2,3,0 };
	        float[] tcs = { 0,1, 0,0, 1,0, 1,1 };

	        mesh = new VertexArray(vertices, indices, tcs);
	        texture = TextureLoader.load("res/bird.png");
	    }

	    public void render(Bird bird) {
	    	ShaderManager.BIRD.enable();
	    	ShaderManager.BIRD.setUniformMat4f(
	            "ml_matrix", 
	            Matrix4f.translate(bird.getPosition())
	                   .multiply(Matrix4f.rotate(bird.getRotation()))
	        );
	        texture.bind();
	        Renderer.draw(mesh);
	        ShaderManager.BIRD.disable();
	    }
	 }
