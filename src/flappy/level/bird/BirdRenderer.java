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

//	        mesh = new VertexArray(vertices, indices, tcs);
	        
	        mesh = new VertexArray(vertices, tcs ,indices);
	        
	        texture = TextureLoader.load("res/bird.png");
	    }

//	    public void render(Bird bird) {
//	    	ShaderManager.BIRD.enable();
//	    	ShaderManager.BIRD.setUniformMat4f(
//	            "ml_matrix", 
//	            Matrix4f.translate(bird.getPosition())
//	                   .multiply(Matrix4f.rotate(bird.getRotation()))
//	        );
//	        texture.bind();
//	        Renderer.draw(mesh);
//	        ShaderManager.BIRD.disable();
//	    }
	    
	    // debug đóm sáng sau chim
	    public void render(Bird bird) {
	        ShaderManager.BIRD.enable();

	        // ⚙️ Thiết lập ma trận chiếu (orthographic) cho toàn màn hình
	        ShaderManager.BIRD.setUniformMat4f(
	            "pr_matrix",
	            Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f)
	        );

	        // ⚙️ Ma trận model (dịch + xoay chim)
	        ShaderManager.BIRD.setUniformMat4f(
	            "ml_matrix",
	            Matrix4f.translate(bird.getPosition())
	                    .multiply(Matrix4f.rotate(bird.getRotation()))
	        );

	        // ⚙️ Ma trận view (thường là Identity nếu camera cố định)
	        ShaderManager.BIRD.setUniformMat4f("vw_matrix", Matrix4f.identity());

	        texture.bind();
	        Renderer.draw(mesh);
	        ShaderManager.BIRD.disable();
	    }

	 }
