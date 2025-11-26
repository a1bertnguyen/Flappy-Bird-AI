package flappy.level.bird;

import flappy.Screen.ScreenManager;
import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.VertexArray.VertexArray;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*; // <--- DÒNG NÀY RẤT QUAN TRỌNG

public class BirdRenderer {
	 private VertexArray mesh;
	    private Texture texture;
	    private Texture[] skins;


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
	        skins = new Texture[] {
	                TextureLoader.load("res/bird.png"),
	                TextureLoader.load("res/skin-doggogo.png"),
	                TextureLoader.load("res/skin-redbird.png")};
	        texture = skins[ScreenManager.getSelectedSkin()];

	    }

	    public Texture getTexture() {
	        return texture;
	    }

	   	 

	 public void render(Bird bird, float xScroll) { 
	     ShaderManager.BIRD.enable();

	     
	     Matrix4f vw_matrix = Matrix4f.identity(); 
	     ShaderManager.BIRD.setUniformMat4f("vw_matrix", vw_matrix); 
	     
	     
	     ShaderManager.BIRD.setUniformMat4f(
	         "ml_matrix", 
	         Matrix4f.translate(bird.getPosition())
	            .multiply(Matrix4f.rotate(bird.getRotation()))
	     );

	 
	     glActiveTexture(GL_TEXTURE1); 
	     texture.bind();
	  
	     Renderer.draw(mesh);
	    
	     texture.unbind(); 
	     ShaderManager.BIRD.disable();
	 }
	    
	    
	 }
