package flappy.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;


import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import flappy.graphics.Shader.ShaderManager;
import flappy.input.input;
import flappy.level.background.Level;
import flappy.maths.Matrix4f;

public class GameLogic implements IGameLogic {
	
	private int width = 1280;
	private int height = 720;
	
    private long window;
    private Level level;
    private input input;

    @Override
    public void init() {
    	if (!glfwInit()) {
			System.err.println("Could not initialize GLFW!");
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "Flappy", NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create GLFW window!");
			return;
		}
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(
		    window,
		    (vidmode.width() - width) / 2,
		    (vidmode.height() - height) / 2
		);
		
		glfwSetKeyCallback(window, new input());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		
 		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		ShaderManager.loadAll();
		
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
		level.update();
		input.update();
		if (level.isGameOver()) {
			level = new Level();
		}
    }

    @Override
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();
        glfwSwapBuffers(window);
    }

    @Override
    public void cleanup() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    @Override
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }
}