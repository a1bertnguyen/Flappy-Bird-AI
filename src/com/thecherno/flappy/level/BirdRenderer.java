package com.thecherno.flappy.level;

import com.thecherno.flappy.graphics.ShaderManager;
import com.thecherno.flappy.graphics.Texture;
import com.thecherno.flappy.graphics.TextureLoader;
import com.thecherno.flappy.graphics.Renderer;
import com.thecherno.flappy.graphics.VertexArray;
import com.thecherno.flappy.maths.Matrix4f;

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
