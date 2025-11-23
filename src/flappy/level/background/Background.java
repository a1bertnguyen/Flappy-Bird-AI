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
//Trong Background.java
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*; // <--- DÒNG NÀY PHẢI CÓ

public class Background {
	private VertexArray background;
//	private VertexArray fade;
    private Texture bgTexture;
    private int map = 0;
//    private float time = 0.0f;

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

//    public void update() {
//        time += 0.01f;
//    }

//    public void render(float birdY, int xScroll) {
//        bgTexture.bind();
//        ShaderManager.BG.enable();
//        ShaderManager.BG.setUniform2f("bird", 0, birdY);
//        background.bind();
//
//        for (int i = map; i < map + 4; i++) {
//        	ShaderManager.BG.setUniformMat4f("vw_matrix",
//                Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
//            Renderer.draw(background);
//        }
//
//        ShaderManager.BG.disable();
//        bgTexture.unbind();
//        System.out.println("[BG] render xScroll=" + xScroll);
//        
//
//    }
    
    
   

    public void render(float birdY, float xScroll) {
        // 1. Setup Shader/Texture (Giữ nguyên)
        glActiveTexture(GL_TEXTURE1); 
        bgTexture.bind();
        ShaderManager.BG.enable();
        ShaderManager.BG.setUniform1i("tex", 1); 
        ShaderManager.BG.setUniform2f("bird", 0, birdY);
        background.bind();

        // 2. LOGIC TÍNH TOÁN VÀ VẼ CUỘN NỀN (Khôi phục)
        final float WORLD_TILE_WIDTH = 10.0f;
        final float SCROLL_FACTOR = 0.03f;
        
        // TÍNH TOÁN MAP DỰA TRÊN VỊ TRÍ CUỘN HIỆN TẠI
        int map = (int) Math.floor((-xScroll * SCROLL_FACTOR) / WORLD_TILE_WIDTH);
        
        // Vòng lặp vẽ 4 tile nền
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
     

    // Render nền tĩnh (dùng cho menu)
    public void render() {
    	glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND); // tránh làm mờ background

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-1, 1, -1, 1, -1, 1);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        bgTexture.bind();

        glBegin(GL_QUADS);
        // Đổi chiều texture ở trục Y (0,1) <-> (1,0)
        glTexCoord2f(0, 1); glVertex2f(-1f, -1f);
        glTexCoord2f(1, 1); glVertex2f( 1f, -1f);
        glTexCoord2f(1, 0); glVertex2f( 1f,  1f);
        glTexCoord2f(0, 0); glVertex2f(-1f,  1f);
        glEnd();

        bgTexture.unbind();
    }

    // Chuyển sang map nền kế tiếp
    public void nextMap() {
        map++;
    }
    
    public void reset() {
        map = 0;
    }

}