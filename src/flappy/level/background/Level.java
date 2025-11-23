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
	import static org.lwjgl.opengl.GL30.*;
	
	public class Level {
	
	    private enum GameState { PLAYING, GAMEOVER }
	
	    // Core game objects
	    private final Bird bird;
	    private final BirdRenderer birdRenderer;
	    private final PipeManager pipeManager;
	    private final Background background;
	    private final Difficulty difficulty;
	    private final ScoreManager scoreManager;
	
	    // Game Over UI
	    private final Texture gameOverTexture;
	    private final VertexArray gameOverVAO;
	
	 
	    
	 
	     private float xScroll = 0.0f; // SỬ DỤNG FLOAT
	     
	    private GameState state = GameState.PLAYING;
	    private boolean flapPressed = false;
	    private boolean canReset = false;
	    private int gameOverWait = 0;
	    private boolean justReset = false;
	
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
	                new float[]{0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0},
	                new byte[]{0, 1, 2, 2, 3, 0},
	                new float[]{0, 0, 1, 0, 1, 1, 0, 1}
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
	        boolean spaceDown = input.isKeyDown(GLFW_KEY_SPACE);

	        if (spaceDown && !flapPressed) {
	            flapPressed = true;
	            bird.flap();
	            SoundManager.playFlap();
	        } else if (!spaceDown) {
	            flapPressed = false;
	        }

	         
	        xScroll -= difficulty.speed; 
	       
	        if (-xScroll >= difficulty.backgroundLength) { 
	            xScroll += difficulty.backgroundLength; 
	            background.nextMap(); 
	        }

	     
	        float scrollValue = -xScroll;
	        
	     
	        if (scrollValue > 250 && (int)scrollValue % difficulty.pipeGap == 0) { 
	             pipeManager.updatePipes();
	        }
	        
	        bird.update();

	      
	        if (pipeManager.checkPass(bird, xScroll)) {
	            scoreManager.addScore();
	            SoundManager.playTing();
	        }
	        
	        boolean outOfBounds = bird.getY() < -5.625f || bird.getY() > 5.625f;
	        boolean hitPipe = pipeManager.checkCollision(bird, xScroll);

	        if (outOfBounds || hitPipe) {
	            state = GameState.GAMEOVER;
	            gameOverWait = 8;
	            SoundManager.stopBackground();
	            SoundManager.playGameOver();
	        }
	    }
	    
	    
	    
	
	    private void updateGameOver() {
	        bird.update();  
	
	        if (gameOverWait > 0) {
	            gameOverWait--;
	            return;
	        }
	
	        if (!input.isKeyDown(GLFW_KEY_SPACE)) {
	            canReset = true;
	        }
	
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
	        
	 
	        if (state == GameState.GAMEOVER && !justReset) {
	            renderGameOver();
	        }
	        
	        if (justReset) justReset = false;
	     
	       
	    }
	
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
	
	        Matrix4f model = Matrix4f.translate(
	                screenWidth / 2f - gameOverTexture.getWidth() / 2f,
	                screenHeight / 2f + gameOverTexture.getHeight() / 2f,
	                0
	        ).multiply(Matrix4f.scale(
	                gameOverTexture.getWidth(),
	                -gameOverTexture.getHeight(),
	                1
	        ));
	
	        ShaderManager.UI.setUniformMat4f("ml_matrix", model);
	        ShaderManager.UI.setUniformMat4f("pr_matrix", Matrix4f.orthographic(0, screenWidth, 0, screenHeight, -1, 1));
	
	        Renderer.draw(gameOverVAO);
	
	        gameOverTexture.unbind();
	        ShaderManager.UI.disable();
	
	        glDisable(GL_BLEND);
	        glEnable(GL_DEPTH_TEST);
	    }
	
	    public boolean isGameOver() {
	        return state == GameState.GAMEOVER;
	    }
	}
