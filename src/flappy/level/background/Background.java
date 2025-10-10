package flappy.level.background;

import flappy.graphics.Shader.Shader;
import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.Texture.Texture;
import flappy.graphics.VertexArray.VertexArray;
import flappy.graphics.VertexArray.Renderer;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;

import static org.lwjgl.opengl.GL11.*;   // GL_DEPTH_TEST, GL_BLEND, GL_SRC_ALPHA, ...
import static org.lwjgl.opengl.GL13.*;   // GL_TEXTURE0

public class Background {
	private VertexArray background;
//	private VertexArray fade;
    private Texture bgTexture;
    private int map = 0;
//    private float time = 0.0f;

    public Background() {
        float[] vertices = new float[] {
        		       		
        			    -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
        			    -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
        			     10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
        			     10.0f, -10.0f * 9.0f / 16.0f, 0.0f
        		
        };
        byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
        float[] tcs = new float[] { 0,1, 0,0, 1,0, 1,1 };

//        fade = new VertexArray(6);
        background = new VertexArray(vertices,tcs, indices);
        bgTexture = TextureLoader.load("res/bg.jpeg");
    }


    
    
    //mới thêm vô
    public void nextMap() {
        map++;
    }
   
    
    public void render(float birdY, int xScroll) {
        glDisable(GL_DEPTH_TEST); // tắt depth test

        ShaderManager.BG.enable();

        glActiveTexture(GL_TEXTURE0);
        bgTexture.bind();
        ShaderManager.BG.setUniform1i("tex", 0); // texture unit
        ShaderManager.BG.setUniform2f("bird", 0f, 0.5f); // tạm để background hiển thị
        ShaderManager.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(0,0,0)));
        ShaderManager.BG.setUniformMat4f("ml_matrix", Matrix4f.identity()); // quan trọng!

        background.bind();
        Renderer.draw(background);
        background.unbind();

        bgTexture.unbind();
        ShaderManager.BG.disable();

        glEnable(GL_DEPTH_TEST); // bật lại depth test cho chim + pipe
    }
    
}