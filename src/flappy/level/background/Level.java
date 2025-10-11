package flappy.level.background;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import flappy.AI.BirdBot;
import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.Shader.Shader;
import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.Texture;
import flappy.graphics.VertexArray.VertexArray;
import flappy.input.input;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;
import flappy.level.bird.Bird;
import flappy.level.bird.BirdRenderer;
import flappy.level.pipe.PipeManager;

import static org.lwjgl.glfw.GLFW.*;

public class Level {
    private List<Bird> birds = new ArrayList<>();
    private List<BirdBot> bots = new ArrayList<>();


    private Bird bird;
    private BirdRenderer birdRenderer;
    private PipeManager pipeManager;
    private Background background;
    private VertexArray fade;

    private int xScroll = 0;
    private boolean control = true, reset = false;

    private float time = 0.0f;

    public Level(int size) {
        for (int i = 0; i < size; i++) {
            Bird b = new Bird();
            birds.add(b);
            bots.add(new BirdBot(b));
        }

        birdRenderer = new BirdRenderer();
        pipeManager = new PipeManager();
        background = new Background();
        fade = new VertexArray(6);
    }


    public void update() {
        if (control) {
            xScroll--;
            if (-xScroll % 335 == 0) background.nextMap();
            if (-xScroll > 250 && -xScroll % 120 == 0)
                pipeManager.updatePipes();
        }

        for (Bird bird : birds) {
            if (!bird.isAlive()) continue;
            bird.update();
        }

        for (BirdBot bot : bots) {
            if (!bot.getBird().isAlive()) continue;
            bot.update();
        }

        for (Bird bird : birds) {
            if (!bird.isAlive()) continue;

            if (bird.getY() < -5.625f || bird.getY() > 5.625f) {
                bird.fall();
                bird.kill();
                continue;
            }

            if (pipeManager.checkCollision(bird, xScroll)) {
                bird.fall();
                bird.kill();
            }
        }

        if (birds.stream().noneMatch(Bird::isAlive)) {
            control = false;
        }

        if (!control && input.isKeyDown(GLFW_KEY_SPACE))
            reset = true;
    }


    public void render() {
        float y = birds.get(0).getY();

        background.render(y, xScroll);
        pipeManager.render(y, xScroll);

        for (Bird bird : birds) {
            if (!bird.isAlive()) continue;
            birdRenderer.render(bird);
        }
    }

    public boolean isGameOver() {
        return reset;
    }

}