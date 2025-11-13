package flappy.level.background;

import java.util.List;

import flappy.AI.BirdBot;
import flappy.graphics.VertexArray.VertexArray;
import flappy.graphics.Shader.ShaderManager;
import flappy.input.input;
import flappy.level.bird.Bird;
import flappy.level.bird.BirdRenderer;
import flappy.level.pipe.Pipe;
import flappy.level.pipe.PipeManager;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Level {

    private BirdRenderer birdRenderer;
    private PipeManager pipeManager;
    private Background background;
    private VertexArray fade;

    private int xScroll = 0;
    private boolean control = true, reset = false;

    private List<BirdBot> bots;

    public Level(List<BirdBot> bots) {
        this.bots = bots;
        this.birdRenderer = new BirdRenderer();
        this.pipeManager = new PipeManager();
        this.background = new Background();
        this.fade = new VertexArray(6);
    }
    public Pipe getClosestPipe(Bird bird) {
        return pipeManager.getClosestPipe(bird);
    }


    public void update() {
        if (control) {
            xScroll--;
            if (-xScroll % 335 == 0) background.nextMap();
            if (-xScroll > 250 && -xScroll % 120 == 0)
                pipeManager.updatePipes();
        }

        for (BirdBot bot : bots) {
            if (!bot.getBird().isAlive()) continue;

            bot.update(this);
            pipeManager.checkPassed(bot.getBird(), bot);



            if (bot.getBird().getY() < -5.625f || bot.getBird().getY() > 5.625f) {
                System.out.println("DEAD: Out of bounds | y = " + bot.getBird().getY());
                bot.getBird().fall();
                bot.getBird().kill();
            }

            if (pipeManager.checkCollision(bot.getBird(), xScroll)) {
                System.out.println("DEAD: Hit pipe | pipeX=" + bot.getBird().getX());
                bot.getBird().fall();
                bot.getBird().kill();
            }
        }

        if (bots.stream().noneMatch(b -> b.getBird().isAlive())) {
            control = false;
            reset = true;
        }




    }

    public void render() {
        float y = getRepresentativeY(); // chim còn sống để lấy tọa độ Y

        background.render(y, xScroll);
        pipeManager.render(y, xScroll);

        for (BirdBot bot : bots) {
            if (!bot.getBird().isAlive()) continue;
            birdRenderer.render(bot.getBird());
        }
    }
    private float getRepresentativeY() {
        for (BirdBot bot : bots) {
            if (bot.getBird().isAlive()) {
                return bot.getBird().getY();
            }
        }
        return 0.0f; // fallback nếu tất cả chim đã chết
    }



    public boolean isGameOver() {
        return reset;
    }

    public void resetLevel(List<BirdBot> newBots) {
        this.bots = newBots;
        this.pipeManager = new PipeManager();
        this.xScroll = 0;
        this.control = true;
        this.reset = false;
        this.background = new Background();
    }

    private float[] calculateVision(BirdBot bot) {
        return new float[] {0.5f, 1f, 0.5f};
    }

    public float getXScroll() {
        return xScroll;
    }
}
