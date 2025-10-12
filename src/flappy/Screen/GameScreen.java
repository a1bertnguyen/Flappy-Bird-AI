package flappy.Screen;

import flappy.level.background.Level;
import flappy.input.input;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import flappy.graphics.Shader.ShaderManager;
import flappy.maths.Matrix4f;
public class GameScreen implements IScreen{
	 private Level level;
	 private float startDelay = 2.0f; // chờ 2 giây
	 private float elapsed = 0f;
	 private boolean started = false;
	
	    @Override
	    public void init() {
	    	glEnable(GL_DEPTH_TEST);
			glActiveTexture(GL_TEXTURE1);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			System.out.println("OpenGL: " + glGetString(GL_VERSION));
			
			
			Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
			ShaderManager.BG.setUniformMat4f("pr_matrix", pr_matrix);
			ShaderManager.BG.setUniform1i("tex", 1);
			
			ShaderManager.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
			ShaderManager.BIRD.setUniform1i("tex", 1);
			
			ShaderManager.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
			ShaderManager.PIPE.setUniform1i("tex", 1);
	        level = new Level();
	    }
	    @Override
	    public void update() {
	        glfwPollEvents();
	        if (!started) {
	            elapsed += 0.016f;
	            if (elapsed >= startDelay) {
	                started = true;
	            }
	            return;
	        }
	       
	        level.update();
	        if (level.isGameOver()) {
	            level = new Level();
	        }
	       
	        input.update();
	    }
	    @Override
	    public void render() {
	        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	        level.render();
	    }
	    @Override
	    public void dispose() {
	        // Nếu cần dọn tài nguyên ở đây
	    }
}
