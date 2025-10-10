package score;

import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.VertexArray;
import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.Shader.ShaderManager;
import flappy.maths.Matrix4f;
import audio.SoundManager;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class ScoreManager {

    private int score = 0;
    private Texture[] digits;
    private VertexArray vao;

    public ScoreManager() {
        // Tải số 0–9
        digits = new Texture[10];
        for (int i = 0; i < 10; i++) {
            digits[i] = TextureLoader.load("res/numbers/" + i + ".png");
        }

        // Tạo hình vuông để vẽ số
        vao = new VertexArray(
            new float[]{
                0f, 0f, 0f,
                1f, 0f, 0f,
                1f, 1f, 0f,
                0f, 1f, 0f
            },
            new float[]{
                0f, 0f,
                1f, 0f,
                1f, 1f,
                0f, 1f
            },
            new byte[]{0, 1, 2, 2, 3, 0}
        );
    }

    public void addScore() {
        score++;
        SoundManager.playTing(); // Gọi âm thanh mỗi khi tăng điểm
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }

    public void render() {
        ShaderManager.UI.enable();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        ShaderManager.UI.setUniformMat4f("pr_matrix", Matrix4f.orthographic(0, 1280, 0, 720, -1, 1));

        String s = String.valueOf(score);
        float digitWidth = 32;
        float x = 20;
        float y = 680;

        for (int i = 0; i < s.length(); i++) {
            int d = s.charAt(i) - '0';
            digits[d].bind();
            Matrix4f model = Matrix4f.translate(x + i * (digitWidth + 4), y, 0)
                    .multiply(Matrix4f.scale(digitWidth,- digitWidth, 1));
            ShaderManager.UI.setUniformMat4f("ml_matrix", model);
            Renderer.draw(vao);
            digits[d].unbind();
        }

        ShaderManager.UI.disable();
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }
}
