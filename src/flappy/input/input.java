package flappy.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class input extends GLFWKeyCallback {
	
	public static boolean[] keys = new boolean[65536];
	private static boolean[] lastKeys = new boolean[65536];

	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW.GLFW_RELEASE; 
	}
	
	
	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public static boolean isKeyPressed(int key) {
        return keys[key] && !lastKeys[key];
    }
	
	public static void update() {
        for (int i = 0; i < keys.length; i++) {
            lastKeys[i] = keys[i];
        }
    }
}

