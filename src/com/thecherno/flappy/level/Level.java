	package com.thecherno.flappy.level;

import java.util.Random;

import com.thecherno.flappy.graphics.Renderer;
import com.thecherno.flappy.graphics.Shader;
import com.thecherno.flappy.graphics.ShaderManager;
import com.thecherno.flappy.graphics.Texture;
import com.thecherno.flappy.graphics.VertexArray;
import com.thecherno.flappy.input.Input;
import com.thecherno.flappy.maths.Matrix4f;
import com.thecherno.flappy.maths.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Level {

	 	private Bird bird;
	 	private BirdRenderer birdRenderer;
	    private PipeManager pipeManager;
	    private Background background;
	    private VertexArray fade;

	    private int xScroll = 0;
	    private boolean control = true, reset = false;

	    private float time = 0.0f;

	    public Level() {
	        bird = new Bird();
	        birdRenderer = new BirdRenderer();
	        pipeManager = new PipeManager();
	        background = new Background();
	        fade=new VertexArray(6);
	    }

	    public void update() {
	        if (control) {
	            xScroll--;
	            if (-xScroll % 335 == 0) background.nextMap();
	            if (-xScroll > 250 && -xScroll % 120 == 0)
	                pipeManager.updatePipes();
	        }

	        bird.update();
	        
	        if	(bird.getY() < -5.625f || bird.getY() > 5.625f) {
	        	control=false;
	        }
	        
	        if (control && pipeManager.checkCollision(bird, xScroll)) {
	            bird.fall();
	            control = false;
	        }

	        if (!control && Input.isKeyDown(GLFW_KEY_SPACE))
	            reset = true;

//	        background.update();
	    }

	    public void render() {
	    	background.render(bird.getY(), xScroll);
	        pipeManager.render(bird.getY(), xScroll);
	        birdRenderer.render(bird);
	  	    }

	    public boolean isGameOver() {
	        return reset;
	    }
	
}
