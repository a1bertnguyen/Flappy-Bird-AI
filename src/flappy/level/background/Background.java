package flappy.level.background;

import flappy.graphics.Shader.Shader;
import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.VertexArray.VertexArray;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;
import static org.lwjgl.opengl.GL11.*; 

public class Background {

    private VertexArray background;
    private Texture bgTexture;
    private int map = 0;

    public Background() {
    }

    // Khởi tạo vertices, texture coordinates và indices
    public void init() {
        float[] vertices = new float[]{
                -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
                -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
                 0.0f,   10.0f * 9.0f / 16.0f, 0.0f,
                 0.0f,  -10.0f * 9.0f / 16.0f, 0.0f
        };

        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        background = new VertexArray(vertices, indices, tcs);
        bgTexture = TextureLoader.load("res/bg.jpeg");
    }

    // Render nền có scroll theo birdY và xScroll
    public void render(float birdY, int xScroll) {
        bgTexture.bind();
        ShaderManager.BG.enable();

        // Truyền giá trị bird vào shader (nếu cần dùng trong hiệu ứng)
        ShaderManager.BG.setUniform2f("bird", 0, birdY);

        background.bind();

        // Vẽ 4 tấm nền liên tiếp để tạo hiệu ứng cuộn
        for (int i = map; i < map + 4; i++) {
            ShaderManager.BG.setUniformMat4f(
                "vw_matrix",
                Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f))
            );
            Renderer.draw(background);
        }

        ShaderManager.BG.disable();
        bgTexture.unbind();
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
}