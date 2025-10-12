package flappy.Screen;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.IVertexArray;
import flappy.graphics.VertexArray.VertexArray;
import flappy.input.Button;
import flappy.input.input;
import flappy.main.GameLogic;
import flappy.graphics.VertexArray.Renderer;
import flappy.maths.Matrix4f;
import flappy.utils.FontUtils;
import flappy.graphics.Shader.Shader;
import flappy.graphics.Shader.ShaderManager;

public class MenuScreen implements IScreen{
	
	private Texture background;// menu background
	private IVertexArray bgVao;
		private long window;
	private List<Button> buttons=new ArrayList<>();
	private float time = 0f;
	int w,h;
	  
        private static final IVertexArray vao = new VertexArray(
        	    new float[] {
        	        0f, 0f, 0f,
        	        1f, 0f, 0f,
        	        1f, 1f, 0f,
        	        0f, 1f, 0f
        	    },
        	    new byte[] {
        	        0, 1, 2,
        	        2, 3, 0
        	    },
        	    new float[] {
        	        0f, 0f,
        	        1f, 0f,
        	        1f, 1f,
        	        0f, 1f
        	    }
        	);

    private Shader shader;
    private Matrix4f projection;


    public MenuScreen() {
	   	window=GameLogic.window;
	   }

    @Override
    public void init() {
    	 background = TextureLoader.load("res/menu_bg.png");

    	    int[] width = new int[1], height = new int[1];
    	    glfwGetWindowSize(window, width, height);
    	    w = width[0];
    	    h = height[0];

    	    shader = ShaderManager.UI;
    	    projection = Matrix4f.orthographic(0, w, 0, h, -1, 1);

    	    FontUtils font = new FontUtils("res/font/Pacifico-Regular.ttf", 96f);
    	    Texture bgTex = TextureLoader.load("res/button-background.png");
    	    int buttonW = 300;
    	    int buttonH = 120;
    	    float bx = (w - buttonW) / 2f;
    	    float by = (h - buttonH) / 2f;
    	    Button playButton = new Button(bx, by-120, buttonW, buttonH, bgTex, "START", font);
    	    Button skinButton = new Button(bx, by, buttonW, buttonH, bgTex, "SKIN", font);
    	    buttons.add(playButton);
    	    buttons.add(skinButton);

    	    bgVao = new VertexArray(
    	        new float[]{0f, 0f, 0f, 1f, 0f, 0f, 1f, 1f, 0f, 0f, 1f, 0f},
    	        new byte[]{0, 1, 2, 2, 3, 0},
    	        new float[]{0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f}
    	    );
    }
    @Override
    public void update() {
    	glfwPollEvents();
        input.update();
        double mouseX = input.getMouseX();
        double mouseY = input.getMouseY();
        for (Button b : buttons) {
            if (b.isHovered((float) mouseX, (float) mouseY) &&
                input.isMouseDown(GLFW_MOUSE_BUTTON_LEFT)) {
            	String label = b.getLabel();
            	if (label.equals("START")) {
                    ScreenManager.changeScreen(ScreenManager.GAME);
                } 
            	if (label.equals("SKIN")) {
                    ScreenManager.changeScreen(ScreenManager.SKIN);
                } 
            }
        }

    }
    @Override
    public void render() {
    	// Dùng shader cho background
        shader.enable();
        shader.setUniformMat4f("pr_matrix", projection);

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // --- Background ---
        background.bind();
        Matrix4f bgModel = Matrix4f.translate(0, h, 0)
                .multiply(Matrix4f.scale(w, -h, 1));
        shader.setUniformMat4f("ml_matrix", bgModel);
        Renderer.draw(bgVao);
        background.unbind();

        // ❌ TẮT SHADER để quay lại fixed pipeline
        shader.disable();

        // --- Setup fixed-function để vẽ button ---
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, w, h, 0, -1, 1); // (0,0) góc trái trên
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // --- Buttons ---
        for (Button b : buttons)
            b.render();

        glDisable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);

        // --- Kết thúc ---
        glEnable(GL_DEPTH_TEST);
	    }
    
    @Override
    public void dispose() {
    	   glUseProgram(0);
	       glBindTexture(GL_TEXTURE_2D, 0);
	       glDisable(GL_BLEND);
	       glDisable(GL_DEPTH_TEST);
	       glMatrixMode(GL_PROJECTION);
	       glLoadIdentity();
	       glOrtho(-1, 1, -1, 1, -1, 1);
	       glMatrixMode(GL_MODELVIEW);
	       glLoadIdentity();

	       // Cực kỳ quan trọng: clear màn hình trước khi init
	       glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    }

