 


package flappy.level.pipe;

import java.util.Random;

import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.Shader.ShaderManager;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;
import flappy.level.bird.Bird;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class PipeManager {

    private Pipe[] pipes = new Pipe[10];

//    private final float PIPE_SPACING_X = 3.0f;      // Khoảng cách giữa cột
    
    
    private final float PIPE_SPACING_X = 5.5f;   // khoảng cách dễ, hợp lý nhất
    private final float PIPE_GAP_Y = 12.5f;         // Khoảng cách trên – dưới
    private float nextPipeX = 3.0f;                 // X ban đầu của cột đầu tiên

    private int index = 0;
    private final Random random = new Random();

    public PipeManager(int gap) {
        Pipe.create();
        createInitialPipes();
    }

    // ---------------------------
    // TẠO 10 CỘT BAN ĐẦU
    // ---------------------------
    private void createInitialPipes() {
        nextPipeX = 3.0f;
        index = 0;

        for (int i = 0; i < 10; i += 2) {
            float y = random.nextFloat() * 4f;

            pipes[i] = new Pipe(nextPipeX, y);                      // Top pipe
            pipes[i + 1] = new Pipe(nextPipeX, y - PIPE_GAP_Y);     // Bottom pipe

            nextPipeX += PIPE_SPACING_X;
            index += 2;
        }
    }

    // ---------------------------
    // SPAWN CỘT MỚI KHI NỀN CUỘN
    // ---------------------------
    public void update(float worldX) {

        // worldX là -xScroll từ Level
        // Khi worldX vượt nextPipeX → spawn pipe mới
        while (worldX >= nextPipeX - 10.0f) {   // spawn trước xa để không hụt frame
            spawnPipe();
        }
    }

    private void spawnPipe() {
        int nextIndex = index % 10;

        float y = random.nextFloat() * 4f;

        pipes[nextIndex] = new Pipe(nextPipeX, y);
        pipes[(nextIndex + 1) % 10] = new Pipe(nextPipeX, y - PIPE_GAP_Y);

        nextPipeX += PIPE_SPACING_X;
        index += 2;
    }

    // ---------------------------
    // RENDER
    // ---------------------------
    public void render(float birdY, float xScroll) {

        ShaderManager.PIPE.enable();
        ShaderManager.PIPE.setUniform2f("bird", 0, birdY);

        // Đây là offset CHUẨN (không nhân 0.05)
        ShaderManager.PIPE.setUniformMat4f(
                "vw_matrix",
                Matrix4f.translate(new Vector3f(xScroll, 0, 0))
        );

        glActiveTexture(GL_TEXTURE1);
        ShaderManager.PIPE.setUniform1i("tex", 1);

        Pipe.getTexture().bind();
        Pipe.getMesh().bind();

        for (int i = 0; i < pipes.length; i++) {
            ShaderManager.PIPE.setUniform1i("top", (i % 2 == 0) ? 1 : 0);
            ShaderManager.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
            Renderer.draw(Pipe.getMesh());
        }

        Pipe.getMesh().unbind();
        Pipe.getTexture().unbind();
        glActiveTexture(GL_TEXTURE0);
        ShaderManager.PIPE.disable();
    }

    // ---------------------------
    // COLLISION
    // ---------------------------
    public boolean checkCollision(Bird bird, float xScroll) {

        float birdX = bird.getX();
        float birdY = bird.getY();
        float half = bird.getSize() / 2f;

        float bx0 = birdX - half;
        float bx1 = birdX + half;
        float by0 = birdY - half;
        float by1 = birdY + half;

        for (Pipe pipe : pipes) {

            float px0 = pipe.getX() + xScroll;
            float px1 = px0 + Pipe.getWidth();
            float py0 = pipe.getY();
            float py1 = py0 + Pipe.getHeight();

            if (bx1 > px0 && bx0 < px1 && by1 > py0 && by0 < py1)
                return true;
        }
        return false;
    }

    // ---------------------------
    // PASS SCORE
    // ---------------------------
    public boolean checkPass(Bird bird, float xScroll) {

        float birdX = bird.getX();

        for (int i = 0; i < 10; i += 2) {   // chỉ xét cột trên
            Pipe pipe = pipes[i];

            float px = pipe.getX() + xScroll;
            float rightEdge = px + Pipe.getWidth();

            if (!pipe.isPassed() && birdX > rightEdge) {
                pipe.setPassed(true);
                return true;
            }
        }
        return false;
    }

    // ---------------------------
    // RESET GAME
    // ---------------------------
    public void reset() {
        for (Pipe pipe : pipes) pipe = null;
        createInitialPipes();
    }
}


 