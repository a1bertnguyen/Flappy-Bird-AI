 
package flappy.score;

import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.VertexArray;
import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.Shader.ShaderManager;
import flappy.maths.Matrix4f;
import flappy.audio.SoundManager;

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
            new byte[]{0, 1, 2, 2, 3, 0},
            new float[]{
                    0f, 0f,
                    1f, 0f,
                    1f, 1f,
                    0f, 1f
                }
        );
    }

    // ✅ ĐÃ BỎ COMMENT VÀ ĐƯA VÀO HOẠT ĐỘNG
    public void addScore() {
        score++;
        // Đảm bảo SoundManager.playTing() tồn tại và hoạt động
        SoundManager.playTing(); 
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }
    // ✅ KẾT THÚC CÁC PHƯƠNG THỨC BỊ COMMENT

    
    public void render() {
        ShaderManager.UI.enable();
        glDisable(GL_DEPTH_TEST);
        
        // ==========================================================
        // FIX 1: THIẾT LẬP SHADER BINDING (Khắc phục lỗi màu đen)
        // Cần thiết lập uniform sampler để shader biết đọc texture từ đâu
        glActiveTexture(GL_TEXTURE0); 
        ShaderManager.UI.setUniform1i("tex", 0); 
        // ==========================================================
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        ShaderManager.UI.setUniformMat4f("pr_matrix", Matrix4f.orthographic(0, 1280, 0, 720, -1, 1));

        String s = String.valueOf(score);
        float digitWidth = 32;
        float x = 20;
        float y = 680; // Tọa độ Y mong muốn cho ĐỈNH TRÊN

        for (int i = 0; i < s.length(); i++) {
            int d = s.charAt(i) - '0';
            digits[d].bind();
            
            // ==========================================================
            // FIX 2: CHỈNH LÝ TỌA ĐỘ Y (Khắc phục lỗi dịch chuyển)
            // Bù trừ cho scale âm bằng cách dịch chuyển lên thêm digitWidth
            Matrix4f model = Matrix4f.translate(x + i * (digitWidth + 4), y + digitWidth, 0)
                    .multiply(Matrix4f.scale(digitWidth,- digitWidth, 1));
            // ==========================================================
            
            ShaderManager.UI.setUniformMat4f("ml_matrix", model);
            Renderer.draw(vao);
            digits[d].unbind();
        }

        ShaderManager.UI.disable();
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }
}