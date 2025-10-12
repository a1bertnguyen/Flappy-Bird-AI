package flappy.Screen;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;
import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.input.input;
import flappy.level.background.Level;
import flappy.level.background.Background;
public class SkinScreen implements IScreen {
   private Texture[] skins;
   private int selected = 0;
   private Background background;
   @Override
   public void init() {
           skins = new Texture[] {
               TextureLoader.load("res/bird.png"),
               TextureLoader.load("res/skin-doggogo.png"),
               TextureLoader.load("res/skin-redbird.png")};
           background=new Background();
           background.init();
           ShaderManager.loadAll();
   }
   @Override
   public void update() {
   	glfwPollEvents();
      
       if (input.isKeyPressed(GLFW_KEY_RIGHT))
           selected = (selected + 1) % skins.length;
       if (input.isKeyPressed(GLFW_KEY_LEFT))
           selected = (selected - 1 + skins.length) % skins.length;
       if (input.isKeyPressed(GLFW_KEY_ENTER)) {
           ScreenManager.setSelectedSkin(selected);
           ScreenManager.changeScreen(ScreenManager.GAME);
       }
       if (input.isKeyPressed(GLFW_KEY_ESCAPE)) {
           ScreenManager.changeScreen(ScreenManager.MENU);
       }
      
       input.update();
   }
   @Override
   public void render() {
   	glClear(GL_COLOR_BUFFER_BIT);
       glEnable(GL_TEXTURE_2D);
       glMatrixMode(GL_PROJECTION);
       glLoadIdentity();
       glOrtho(-1, 1, -1, 1, -1, 1);
       glMatrixMode(GL_MODELVIEW);
       glLoadIdentity();
      
       background.render();
      
       glEnable(GL_BLEND);
       glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
      
       skins[selected].bind();
       glBegin(GL_QUADS);
       glTexCoord2f(0, 1); glVertex2f(-0.2f, -0.2f);
       glTexCoord2f(1, 1); glVertex2f(0.2f, -0.2f);
       glTexCoord2f(1, 0); glVertex2f(0.2f, 0.2f);
       glTexCoord2f(0, 0); glVertex2f(-0.2f, 0.2f);
       glEnd();
   }
   @Override
   public void dispose() {
       for (Texture t : skins)
           t.unbind();
   }
}
