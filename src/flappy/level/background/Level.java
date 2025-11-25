 


package flappy.level.background;

import flappy.Screen.Difficulty;
import flappy.Screen.ScreenManager;
import flappy.audio.SoundManager;
import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.VertexArray.VertexArray;
import flappy.input.input;
import flappy.level.bird.Bird;
import flappy.level.bird.BirdRenderer;
import flappy.level.pipe.PipeManager;
import flappy.maths.Matrix4f;
import flappy.score.ScoreManager;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Level {

    private enum GameState { PLAYING, GAMEOVER }

    private final Bird bird;
    private final BirdRenderer birdRenderer;
    private final PipeManager pipeManager;
    private final Background background;
    private final Difficulty difficulty;
    private final ScoreManager scoreManager;

    private float xScroll = 0f;
    private GameState state = GameState.PLAYING;

    private boolean flapPressed = false;
    private boolean canReset = false;
    private int gameOverWait = 0;

    private final Texture gameOverTexture;
    private final VertexArray gameOverVAO;

    public Level(Difficulty difficulty) {
        this.difficulty = difficulty;

        bird = new Bird();
        birdRenderer = new BirdRenderer();
        pipeManager = new PipeManager(difficulty.pipeGap);
        background = new Background();
        scoreManager = new ScoreManager();

        background.init();
        SoundManager.init();
        SoundManager.playBackground();

        gameOverTexture = TextureLoader.load("res/gameover.png");
        gameOverVAO = new VertexArray(
                new float[]{0,0,0, 1,0,0, 1,1,0, 0,1,0},
                new byte[]{0,1,2, 2,3,0},
                new float[]{0,0, 1,0, 1,1, 0,1}
        );
    }

    public void update() {
        glfwPollEvents();
        switch (state) {
            case PLAYING -> updatePlaying();
            case GAMEOVER -> updateGameOver();
        }
        input.update();
    }

    private void updatePlaying() {

        boolean down = input.isKeyDown(GLFW_KEY_SPACE);

        if (down && !flapPressed) {
            flapPressed = true;
            bird.flap();
            SoundManager.playFlap();
        } else if (!down) flapPressed = false;

        // ---------------------------
        // CUỘN NỀN
        // ---------------------------
        xScroll -= difficulty.speed;

        // Tiling nền
        if (-xScroll >= difficulty.backgroundLength) {
            xScroll += difficulty.backgroundLength;
            background.nextMap();
        }

        float worldX = -xScroll;

        // ---------------------------
        // PIPE UPDATE CHUẨN  
        // ---------------------------
        pipeManager.update(worldX);

        bird.update();

        // ---------------------------
        // TÍNH ĐIỂM
        // ---------------------------
        if (pipeManager.checkPass(bird, xScroll)) {
            scoreManager.addScore();
            SoundManager.playTing();
        }

        // ---------------------------
        // VA CHẠM
        // ---------------------------
        boolean out = (bird.getY() < -5.6f || bird.getY() > 5.6f);
        boolean hit = pipeManager.checkCollision(bird, xScroll);

        if (out || hit) {
            state = GameState.GAMEOVER;
            SoundManager.stopBackground();
            SoundManager.playGameOver();
            gameOverWait = 10;
        }
    }

    private void updateGameOver() {
        bird.update();

        if (gameOverWait > 0) { gameOverWait--; return; }

        if (!input.isKeyDown(GLFW_KEY_SPACE)) canReset = true;

        if (canReset && input.isKeyDown(GLFW_KEY_SPACE)) {
            ScreenManager.changeScreen(ScreenManager.GAME);
        }
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        background.render(bird.getY(), xScroll);
        pipeManager.render(bird.getY(), xScroll);
        birdRenderer.render(bird, xScroll);
        scoreManager.render();

        if (state == GameState.GAMEOVER)
            renderGameOver();
    }

    private void renderGameOver() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        ShaderManager.UI.enable();

        glActiveTexture(GL_TEXTURE0);
        gameOverTexture.bind();
        ShaderManager.UI.setUniform1i("tex", 0);

        Matrix4f model = Matrix4f.translate(500, 400, 0)
                .multiply(Matrix4f.scale(300, -150, 1));

        ShaderManager.UI.setUniformMat4f("ml_matrix", model);
        ShaderManager.UI.setUniformMat4f("pr_matrix",
                Matrix4f.orthographic(0, 1280, 0, 720, -1, 1));

        Renderer.draw(gameOverVAO);

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }
}
