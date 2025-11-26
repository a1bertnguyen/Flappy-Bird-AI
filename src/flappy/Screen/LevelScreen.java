package flappy.Screen;
import flappy.Screen.Difficulty;
import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.level.background.Background;
import flappy.level.background.Level;
import flappy.main.GameLogic;
import flappy.utils.FontUtils;
import flappy.input.Button;
import flappy.input.input;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.List;

public class LevelScreen implements IScreen {
    private Difficulty selectedDifficulty = Difficulty.EASY;
    int w,h;
    List<Button> buttons;
    private long window;
    private Background background;
    
    public LevelScreen() {
    	this.window=GameLogic.window;
    }
    @Override
    public void init() {
    	int[] width = new int[1], height = new int[1];
	    glfwGetWindowSize(window, width, height);
	    w = width[0];
	    h = height[0];
           
           background=new Background();
           background.init();
           ShaderManager.loadAll();
        
        buttons= new ArrayList<>();
   	    FontUtils font = new FontUtils("res/font/Pacifico-Regular.ttf", 50f);
   	    Texture bgTex = TextureLoader.load("res/button-background.png");
   	    int buttonW = 200;
   	    int buttonH = 100;
   	    float bx = (w - buttonW) / 2f;
   	    float by = (h - buttonH) / 2f;
   	    Button easyButton = new Button(bx-300, by+180, buttonW, buttonH, bgTex, "EASY", font);
   	    Button mediumButton = new Button(bx, by+180, buttonW, buttonH, bgTex, "MEDIUM", font);
   	    Button hardButton = new Button(bx+300, by+180, buttonW, buttonH, bgTex, "HARD", font);
   	    Button selectButton = new Button(bx, by, buttonW, buttonH, bgTex, "SELECT", font);
   	    buttons.add(easyButton);
   	    buttons.add(mediumButton);
   	    buttons.add(hardButton);
   	    buttons.add(selectButton);
    }

  
    @Override
    public void update() {
        glfwPollEvents();
        double mouseX = input.getMouseX();
        double mouseY = input.getMouseY();
        
        // Xử lý logic cho các nút
        for (Button b : buttons) {
            if (b.isHovered((float) mouseX, (float) mouseY) &&
                input.isMouseClicked(GLFW_MOUSE_BUTTON_LEFT)) {
                
                String label = b.getLabel();
                
                if (label.equals("EASY")) {
                    ScreenManager.setSelectedLevel(Difficulty.EASY);
                    System.out.println("EASY chosen (Speed: " + Difficulty.EASY.speed + ")");
                   
                } else if (label.equals("MEDIUM")) {
                    ScreenManager.setSelectedLevel(Difficulty.MEDIUM);
                    System.out.println("MEDIUM chosen (Speed: " + Difficulty.MEDIUM.speed + ")");
                } else if (label.equals("HARD")) {
                    ScreenManager.setSelectedLevel(Difficulty.HARD);
                    System.out.println("HARD chosen (Speed: " + Difficulty.HARD.speed + ")");
                } else if (label.equals("SELECT")) {
                    // Sau khi chọn mức độ, quay lại Menu
                    ScreenManager.changeScreen(ScreenManager.MENU); 
            
                }
            }
        }
        
       
        if (input.isKeyPressed(GLFW_KEY_ESCAPE)) {
            ScreenManager.changeScreen(ScreenManager.MENU);
        }
        
        input.update();
    }
    
    @Override
    public void render() {
    	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // --- Vẽ background ---
        background.render();

        // --- Reset state ---
        glUseProgram(0);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);

        // --- Thiết lập chế độ 2D ---
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, w, h, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // --- Vẽ button ---
        for (Button b : buttons) {
            b.render();
        }

        // --- Dọn state ---
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    @Override
    public void dispose() {

    }
}
