package flappy.level.pipe;

import java.util.Random;

import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.Shader.ShaderManager;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;
import flappy.level.bird.Bird;

public class PipeManager {
    private Pipe[] pipes = new Pipe[10];
    private int index = 0;
    private float OFFSET = 5.0f;
    private Random random = new Random();

    public PipeManager() {
        Pipe.create();
        createPipes();
    }

    private void createPipes() {
        for (int i = 0; i < 10; i += 2) {
            pipes[i] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
            pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 11.5f);
            index += 2;
        }
    }

    public void updatePipes() {
        pipes[index % 10] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
        pipes[(index + 1) % 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 11.5f);
        index += 2;
    }

    public void render(float birdY, int xScroll) {
        ShaderManager.PIPE.enable();
        ShaderManager.PIPE.setUniform2f("bird", 0, birdY);
        ShaderManager.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0.0f, 0.0f)));
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
        for (int i = 0; i < 10; i++) {
            float bx = -xScroll * 0.05f;
            float by = bird.getY();
            float px = pipes[i].getX();
            float py = pipes[i].getY();

            float bx0 = bx - bird.getSize() / 2.0f;
            float bx1 = bx + bird.getSize() / 2.0f;
            float by0 = by - bird.getSize() / 2.0f;
            float by1 = by + bird.getSize() / 2.0f;

            float px0 = px;
            float px1 = px + Pipe.getWidth();
            float py0 = py;
            float py1 = py + Pipe.getHeight();

            if (bx1 > px0 && bx0 < px1 && by1 > py0 && by0 < py1)
                return true;
        }
        return false;
    }
}