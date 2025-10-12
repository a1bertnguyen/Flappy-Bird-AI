package flappy.Screen;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ScreenManager {
	   private static IScreen currentScreen;
	   // Danh sách các ID screen (cho dễ đọc)
	   public static final int MENU = 0;
	   public static final int GAME = 1;
	   public static final int SKIN = 2;
	   public static final int LEVEL = 3;
	   private static int selectedSkin = 0;
	   private static int selectedLevel = 0;
	   public static void changeScreen(int screenID) {
	       if (currentScreen != null) {
	           currentScreen.dispose();
	       }
	       switch (screenID) {
	           case MENU -> currentScreen = new MenuScreen();
	           case GAME -> currentScreen = new GameScreen();
	           case SKIN -> currentScreen = new SkinScreen();
//	            case LEVEL -> currentScreen = new LevelScreen();
	       }
	       currentScreen.init();
	       
	   }
	   public static void update() {
	       if (currentScreen != null)
	           currentScreen.update();
	   }
	   public static void render() {
	       if (currentScreen != null)
	           currentScreen.render();
	   }
	   public static IScreen getCurrentScreen() {
	       return currentScreen;
	   }
	   // --- Dữ liệu chia sẻ ---
	   public static int getSelectedSkin() { return selectedSkin; }
	   public static void setSelectedSkin(int skin) { selectedSkin = skin; }
	   public static int getSelectedLevel() { return selectedLevel; }
	   public static void setSelectedLevel(int level) { selectedLevel = level; }
	}

