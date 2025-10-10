//package flappy.level.background;
//import java.util.Random;
//import static org.lwjgl.opengl.GL11.GL_BLEND;
//import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
//import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
//import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
//import static org.lwjgl.opengl.GL11.glEnable;
//import static org.lwjgl.opengl.GL11.glDisable;
//import static org.lwjgl.opengl.GL11.glBlendFunc;
//
//import flappy.graphics.VertexArray.Renderer;
//import flappy.graphics.Shader.Shader;
//import flappy.graphics.Shader.ShaderManager;
//import flappy.graphics.Texture.Texture;
//import flappy.graphics.Texture.TextureLoader;
//import flappy.graphics.VertexArray.VertexArray;
//import flappy.input.input;
//import flappy.maths.Matrix4f;
//import flappy.maths.Vector3f;
//import flappy.level.bird.Bird;
//import flappy.level.bird.BirdRenderer;
//import flappy.level.pipe.PipeManager;
//import static org.lwjgl.opengl.GL11.GL_BLEND;
//import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
//import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
//import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
//import static org.lwjgl.opengl.GL11.glEnable;
//import static org.lwjgl.opengl.GL11.glDisable;
//import static org.lwjgl.opengl.GL11.glBlendFunc;
//import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
//import static org.lwjgl.opengl.GL13.glActiveTexture;
//
//import static org.lwjgl.glfw.GLFW.*;
//
//import flappy.level.background.Background;
//public class Level {
//
//	 	private Bird bird;
//	 	private BirdRenderer birdRenderer;
//	    private PipeManager pipeManager;
//	    private Background background;
//	    private VertexArray fade;
//
//	    private int xScroll = 0;
//	    private boolean control = true, reset = false;
//
//	    private float time = 0.0f;
//	    
//	    private Texture gameOverTexture;
//	    private boolean gameOver = false;
//	    
//	    public enum GameState { PLAYING, GAMEOVER }
//
//	    private GameState state = GameState.PLAYING;
//
//	    public Level() {
//	        bird = new Bird();
//	        birdRenderer = new BirdRenderer();
//	        pipeManager = new PipeManager();
//	        background = new Background();
////	        fade=new VertexArray(6);
//	        
//	        gameOverTexture = TextureLoader.load("res/gameover.png"); // load gameover image
//	        
//	        fade = new VertexArray(
//	        	    new float[]{
//	        	        -1f, -1f, 0f,
//	        	         1f, -1f, 0f,
//	        	         1f,  1f, 0f,
//	        	        -1f,  1f, 0f
//	        	    },
//	        	    new float[]{
//	        	        0f, 0f,
//	        	        1f, 0f,
//	        	        1f, 1f,
//	        	        0f, 1f
//	        	    },
//	        	    new byte[]{
//	        	        0, 1, 2,
//	        	        2, 3, 0
//	        	    }
//	        	);   
//	    }
//
//	    
//	    public void update() {
//	        switch (state) {
//	            case PLAYING -> {
//	                xScroll--;
//	                if (-xScroll % 335 == 0) background.nextMap();
//	                if (-xScroll > 250 && -xScroll % 120 == 0)
//	                    pipeManager.updatePipes();
//
//	                bird.update();
//
//	                // Kiểm tra va chạm hoặc rơi
//	                if (bird.getY() < -5.625f || bird.getY() > 5.625f || pipeManager.checkCollision(bird, xScroll)) {
//	                    control = false;
//	                    state = GameState.GAMEOVER;
//	                    bird.fall();
//	                }
//	            }
//
//	            case GAMEOVER -> {
//	                // Chờ nhấn SPACE để reset
//	                if (input.isKeyDown(GLFW_KEY_SPACE)) {
//	                    resetGame();
//	                    state = GameState.PLAYING;
//	                }
//	            }
//	        }
//	    }
//
//	    
//	    private void resetGame() {
//	        bird = new Bird();
//	        birdRenderer = new BirdRenderer();
//	        pipeManager = new PipeManager();
//	        background = new Background();
//	        xScroll = 0;
//	        control = true;
//	    }
//
//
//	    
//	    public void render() {
//	        background.render(bird.getY(), xScroll);
//	        pipeManager.render(bird.getY(), xScroll);
//	        birdRenderer.render(bird);
//
//	        if (state == GameState.GAMEOVER) {
//	            renderGameOver();
//	        }
//	    }
//
//
//
//	    public boolean isGameOver() {
//	        return gameOver;
//	    }
//	
//	    
//	    private void renderGameOver() {
//	        glDisable(GL_DEPTH_TEST);
//	        glEnable(GL_BLEND);
//	        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//
//	        ShaderManager.UI.enable(); // hoặc BG shader cũng được
//	        glActiveTexture(GL_TEXTURE0);
//	        gameOverTexture.bind();
//	        ShaderManager.UI.setUniform1i("tex", 0);
//
//	        // Quad full màn hình hoặc đặt giữa màn hình
//	        int screenWidth = 1280;
//	        int screenHeight = 720;
//
//	        VertexArray vao = new VertexArray(
//	            new float[]{0,0,0, 1,0,0, 1,1,0, 0,1,0},
//	            new float[]{0,0, 1,0, 1,1, 0,1},
//	            new byte[]{0,1,2, 2,3,0}
//	        );
//
//        
//	        Matrix4f model = Matrix4f.translate(
//	                screenWidth / 2f - gameOverTexture.getWidth() / 2f,
//	                screenHeight / 2f + gameOverTexture.getHeight() / 2f, // cộng thay vì trừ
//	                0
//	        ).multiply(Matrix4f.scale(gameOverTexture.getWidth(), -gameOverTexture.getHeight(), 1)); // <== lật Y
//
//	        
//
//	        ShaderManager.UI.setUniformMat4f("ml_matrix", model);
//	        ShaderManager.UI.setUniformMat4f("pr_matrix", Matrix4f.orthographic(0, screenWidth, 0, screenHeight, -1,1));
//
//	        Renderer.draw(vao);
//
//	        gameOverTexture.unbind();
//	        ShaderManager.UI.disable();
//
//	        glDisable(GL_BLEND);
//	        glEnable(GL_DEPTH_TEST);
//	    }
//
//}

// class dưới để thêm score và âm thanh 
package flappy.level.background;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.glfw.GLFW.*;

import flappy.graphics.VertexArray.Renderer;
import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.VertexArray;
import flappy.input.input;
import flappy.maths.Matrix4f;
import flappy.level.bird.Bird;
import flappy.level.bird.BirdRenderer;
import flappy.level.pipe.PipeManager;
import score.ScoreManager;
import audio.SoundManager;

public class Level {

    private Bird bird;
    private BirdRenderer birdRenderer;
    private PipeManager pipeManager;
    private Background background;
    private VertexArray fade;

    private int xScroll = 0;
    private boolean control = true;

    private Texture gameOverTexture;
    private boolean gameOver = false;

    private ScoreManager scoreManager; // ✅ thêm điểm
    private enum GameState { PLAYING, GAMEOVER }
    private GameState state = GameState.PLAYING;
    
    private int gameOverWait = 0; // đếm frame khi Game Over


    public Level() {
        bird = new Bird();
        birdRenderer = new BirdRenderer();
        pipeManager = new PipeManager();
        background = new Background();
        scoreManager = new ScoreManager();
        SoundManager.init(); // ✅ khởi tạo âm thanh

        gameOverTexture = TextureLoader.load("res/gameover.png");

        fade = new VertexArray(
            new float[]{-1f,-1f,0f, 1f,-1f,0f, 1f,1f,0f, -1f,1f,0f},
            new float[]{0f,0f, 1f,0f, 1f,1f, 0f,1f},
            new byte[]{0,1,2, 2,3,0}
        );
    }

    public void update() {
        switch (state) {
            case PLAYING -> {
                xScroll--;
                if (-xScroll % 335 == 0) background.nextMap();
                if (-xScroll > 250 && -xScroll % 120 == 0)
                    pipeManager.updatePipes();

                bird.update();

                // 🎯 Khi chim bay qua ống -> cộng điểm + ting.wav
                if (pipeManager.checkPass(bird, xScroll)) {
                    scoreManager.addScore();
                }

                // 💥 Kiểm tra va chạm hoặc rơi
                if (bird.getY() < -5.625f || bird.getY() > 5.625f || pipeManager.checkCollision(bird, xScroll)) {
                    control = false;
                    state = GameState.GAMEOVER;
                    bird.fall();
                    
                    gameOverWait = 30; // đợi ~0.5 giây nếu 60fps
                    
                     }
            }

            case GAMEOVER -> {
                if (gameOverWait > 0) {
                    gameOverWait--; // đếm ngược
                } else if (input.isKeyDown(GLFW_KEY_SPACE)) {
                    resetGame();
                    state = GameState.PLAYING;
                }
            }

            
//            case GAMEOVER -> {
//                // ⏸️ Dừng game, chờ SPACE
//                if (input.isKeyDown(GLFW_KEY_SPACE)) {
//                    resetGame();
//                    state = GameState.PLAYING;
//                }
//            }
        }
    }

    private void resetGame() {
        bird = new Bird();
        birdRenderer = new BirdRenderer();
        pipeManager = new PipeManager();
        background = new Background();
        scoreManager.reset(); // ✅ reset điểm
        xScroll = 0;
        control = true;
    }

    public void render() {
        background.render(bird.getY(), xScroll);
        pipeManager.render(bird.getY(), xScroll);
        birdRenderer.render(bird);

        scoreManager.render(); // ✅ vẽ điểm

        if (state == GameState.GAMEOVER) {
            renderGameOver();
        }
    }
    
    // ------------thêm vô để chạy sound và score
    
    public boolean isGameOver() {
        return state == GameState.GAMEOVER;
    }

    //--------------------

    private void renderGameOver() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        ShaderManager.UI.enable();
        glActiveTexture(GL_TEXTURE0);
        gameOverTexture.bind();
        ShaderManager.UI.setUniform1i("tex", 0);

        int screenWidth = 1280;
        int screenHeight = 720;

        VertexArray vao = new VertexArray(
            new float[]{0,0,0, 1,0,0, 1,1,0, 0,1,0},
            new float[]{0,0, 1,0, 1,1, 0,1},
            new byte[]{0,1,2, 2,3,0}
        );

        Matrix4f model = Matrix4f.translate(
                screenWidth / 2f - gameOverTexture.getWidth() / 2f,
                screenHeight / 2f + gameOverTexture.getHeight() / 2f,
                0
        ).multiply(Matrix4f.scale(gameOverTexture.getWidth(), -gameOverTexture.getHeight(), 1));

        ShaderManager.UI.setUniformMat4f("ml_matrix", model);
        ShaderManager.UI.setUniformMat4f("pr_matrix", Matrix4f.orthographic(0, screenWidth, 0, screenHeight, -1, 1));

        Renderer.draw(vao);

        gameOverTexture.unbind();
        ShaderManager.UI.disable();

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }
}
