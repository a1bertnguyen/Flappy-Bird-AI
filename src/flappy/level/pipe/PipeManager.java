package flappy.level.pipe;

import java.util.Random;

import flappy.AI.BirdBot;
import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.Shader.ShaderManager;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;
import flappy.level.bird.Bird;

public class PipeManager {

    private Pipe[] pipes = new Pipe[10];
    private int index = 0;
    private static final float OFFSET = 5.0f;
    private static final float PIPE_SCROLL_SPEED = 0.05f;
    private Random random = new Random();

    public PipeManager() {
        Pipe.create();
        createPipes();
    }

    private void createPipes() {
        for (int i = 0; i < 10; i += 2) {
            pipes[i] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
            pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - Pipe.getPairOffset());
            index += 2;
        }
    }

    public void updatePipes() {
        Pipe top = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
        Pipe bottom = new Pipe(top.getX(), top.getY() - Pipe.getPairOffset());

        pipes[index % 10] = top;
        pipes[(index + 1) % 10] = bottom;

        index += 2;
    }

    public void checkPassed(Bird bird, BirdBot bot, int xScroll) {
        // X world của chim
        float birdX = bird.getX();

        // Kiểm tra các ống "trên" (i, i+1 là cặp trên/dưới)
        for (int i = 0; i < 10; i += 2) {
            Pipe p = pipes[i];
            float pipeRight = getScreenX(p, xScroll) + Pipe.getWidth();

            // Nếu ống chưa được tính điểm và đã hoàn toàn đi qua bên trái chim
            // (mép phải của ống < X của chim)
            if (pipeRight < birdX && !bot.hasPassedPipe(p)) {
                bot.addPipePass(p);
            }
        }
    }

    public void render(float birdY, int xScroll) {
        ShaderManager.PIPE.enable();
        ShaderManager.PIPE.setUniform2f("bird", 0, birdY);
        // Keep render, collision, AI vision, and scoring in the same pipe coordinate space.
        float offset = getScrollOffset(xScroll);
        ShaderManager.PIPE.setUniformMat4f(
                "vw_matrix",
                Matrix4f.translate(new Vector3f(offset, 0.0f, 0.0f))
        );

        Pipe.getTexture().bind();
        Pipe.getMesh().bind();

        for (int i = 0; i < 10; i++) {
            ShaderManager.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
            ShaderManager.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
            Renderer.draw(Pipe.getMesh());
        }

        Pipe.getMesh().unbind();
        Pipe.getTexture().unbind();
    }

    public boolean checkCollision(Bird bird, int xScroll) {
        // Bird AABB (screen/world giống nhau vì bird không dùng xScroll trong render)
        float bx0 = bird.getX() - bird.getSize() / 2.0f;
        float bx1 = bird.getX() + bird.getSize() / 2.0f;
        float by0 = bird.getY() - bird.getSize() / 2.0f;
        float by1 = bird.getY() + bird.getSize() / 2.0f;

        for (Pipe p : pipes) {
            // Pipe X on screen, synced with render and AI vision.
            float px = getScreenX(p, xScroll);
            float py = p.getY();

            float px0 = px;
            float px1 = px + Pipe.getWidth();
            float py0 = py;
            float py1 = py + Pipe.getHeight();

            if (bx1 > px0 && bx0 < px1 && by1 > py0 && by0 < py1) {
                return true;
            }
        }

        return false;
    }

    public Pipe getClosestPipe(Bird bird, int xScroll) {
        Pipe closest = null;
        float minDist = Float.MAX_VALUE;
        for (int i = 0; i < 10; i += 2) {
            Pipe p = pipes[i];
            float pipeLeft = getScreenX(p, xScroll);
            float dist = pipeLeft + Pipe.getWidth() - bird.getX();
            if (dist > 0 && dist < minDist) {
                minDist = dist;
                closest = p;
            }
        }
        return closest;
    }

    public float getScreenX(Pipe pipe, int xScroll) {
        return pipe.getX() + getScrollOffset(xScroll);
    }

    private float getScrollOffset(int xScroll) {
        return xScroll * PIPE_SCROLL_SPEED;
    }

}
