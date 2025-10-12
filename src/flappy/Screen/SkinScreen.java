package flappy.Screen;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.*;


import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.input.Button;
import flappy.input.input;
import flappy.level.background.Level;
import flappy.main.GameLogic;
import flappy.utils.FontUtils;
import flappy.level.background.Background;
public class SkinScreen implements IScreen {
   private Texture[] skins;
   private int selected = 0;
   private Background background;
   private int w,h;
   private List<Button> buttons;
   private long window;
   
   public SkinScreen() {
	   window=GameLogic.window;
   }
   
   @Override
   public void init() {
	   int[] width = new int[1], height = new int[1];
	    glfwGetWindowSize(window, width, height);
	    w = width[0];
	    h = height[0];
           skins = new Texture[] {
               TextureLoader.load("res/bird.png"),
               TextureLoader.load("res/skin-doggogo.png"),
               TextureLoader.load("res/skin-redbird.png")};
           background=new Background();
           background.init();
           ShaderManager.loadAll();
        
        buttons= new ArrayList<>();
   	    FontUtils font = new FontUtils("res/font/Pacifico-Regular.ttf", 65f);
   	    Texture bgTex = TextureLoader.load("res/button-background.png");
   	    int buttonW = 200;
   	    int buttonH = 100;
   	    float bx = (w - buttonW) / 2f;
   	    float by = (h - buttonH) / 2f;
   	    Button nextButton = new Button(bx+300, by+180, buttonW, buttonH, bgTex, "NEXT", font);
   	    Button backButton = new Button(bx-300, by+180, buttonW, buttonH, bgTex, "BACK", font);
   	    Button selectButton = new Button(bx, by+180, buttonW, buttonH, bgTex, "SELECT", font);
   	    buttons.add(nextButton);
   	    buttons.add(backButton);
   	    buttons.add(selectButton);
   }
   @Override
   public void update() {
	   glfwPollEvents();     
       double mouseX = input.getMouseX();
       double mouseY = input.getMouseY();
       for (Button b : buttons) {
           if (b.isHovered((float) mouseX, (float) mouseY) &&
               input.isMouseClicked(GLFW_MOUSE_BUTTON_LEFT)) {
		           	String label = b.getLabel();
		           	if (label.equals("NEXT")) {
		           		selected = (selected + 1) % skins.length;
		               } 
		           	if (label.equals("BACK")) {
		           		selected = (selected - 1 + skins.length) % skins.length;
		               } 
		           	if (label.equals("SELECT")) {
		           		ScreenManager.setSelectedSkin(selected);
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

	    // --- TẮT SHADER + RESET TRẠNG THÁI ---
	    glUseProgram(0);
	    glBindVertexArray(0);
	    glBindTexture(GL_TEXTURE_2D, 0);

	    // --- Chuyển sang fixed pipeline ---
	    glDisable(GL_DEPTH_TEST);
	    glEnable(GL_TEXTURE_2D);
	    glEnable(GL_BLEND);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	    // Dùng hệ tọa độ pixel để dễ căn nút
	    glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    glOrtho(0, w, h, 0, -1, 1);
	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();

	    // --- Vẽ skin ở giữa màn ---
	    skins[selected].bind();
	    float size = 200f;
	    float sx = w / 2f - size / 2f;
	    float sy = h / 2f - size / 2f;
	    glBegin(GL_QUADS);
	        glTexCoord2f(0, 1); glVertex2f(sx, sy + size);
	        glTexCoord2f(1, 1); glVertex2f(sx + size, sy + size);
	        glTexCoord2f(1, 0); glVertex2f(sx + size, sy);
	        glTexCoord2f(0, 0); glVertex2f(sx, sy);
	    glEnd();
	    skins[selected].unbind();

	    // --- Vẽ button ---
	    for (Button b : buttons)
	        b.render();

	    glDisable(GL_TEXTURE_2D);
	    glDisable(GL_BLEND);
	    glEnable(GL_DEPTH_TEST);
   }
   @Override
   public void dispose() {
       for (Texture t : skins)
           t.unbind();
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
