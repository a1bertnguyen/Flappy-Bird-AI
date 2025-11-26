package flappy.level.background;

import flappy.graphics.Shader.Shader;
import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.Texture.Texture;
import flappy.graphics.VertexArray.VertexArray;
import flappy.graphics.VertexArray.Renderer;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;
import static org.lwjgl.opengl.GL11.*;
 
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;  

public class Background {
	private VertexArray background;
 
    private Texture bgTexture;
    private int map = 0;
 

    public Background() {
    }
    
    public void init() {
    	float[] vertices = new float[] {
                -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
                -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
                  0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
                  0.0f, -10.0f * 9.0f / 16.0f, 0.0f
            };
            byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
            float[] tcs = new float[] { 0,1, 0,0, 1,0, 1,1 };

 
            background = new VertexArray(vertices, indices, tcs);
            bgTexture = TextureLoader.load("res/bg.jpeg");
    }

 
     
   

    public void render(float birdY, float xScroll) {
     
        glActiveTexture(GL_TEXTURE1); 
        bgTexture.bind();
        ShaderManager.BG.enable();
        ShaderManager.BG.setUniform1i("tex", 1); 
        ShaderManager.BG.setUniform2f("bird", 0, birdY);
        background.bind();

       
        final float WORLD_TILE_WIDTH = 10.0f;
        final float SCROLL_FACTOR = 0.03f;
        
       
        int map = (int) Math.floor((-xScroll * SCROLL_FACTOR) / WORLD_TILE_WIDTH);
        
        
        for (int i = map; i < map + 4; i++) {
            ShaderManager.BG.setUniformMat4f("vw_matrix",
                Matrix4f.translate(new Vector3f(i * WORLD_TILE_WIDTH + xScroll * SCROLL_FACTOR, 0.0f, 0.0f)));
            Renderer.draw(background);
        }
        // ------------------------------------

        ShaderManager.BG.disable();
        glActiveTexture(GL_TEXTURE0); 
        bgTexture.unbind();
        System.out.println("[BG] render xScroll=" + xScroll);
    }
     

     
    public void render() {
    	glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);  

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-1, 1, -1, 1, -1, 1);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        bgTexture.bind();

        glBegin(GL_QUADS);
      
        glTexCoord2f(0, 1); glVertex2f(-1f, -1f);
        glTexCoord2f(1, 1); glVertex2f( 1f, -1f);
        glTexCoord2f(1, 0); glVertex2f( 1f,  1f);
        glTexCoord2f(0, 0); glVertex2f(-1f,  1f);
        glEnd();

        bgTexture.unbind();
    }

 
    public void nextMap() {
        map++;
    }
    
    public void reset() {
        map = 0;
    }

}