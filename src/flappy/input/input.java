package flappy.input;

import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;

public class input extends GLFWKeyCallback {
	
    private static boolean[] keys = new boolean[65536];
    private static boolean[] lastKeys = new boolean[65536];

    private static boolean[] mouseButtons = new boolean[16];
    private static boolean[] lastMouseButtons = new boolean[16];

    private static double mouseX, mouseY;

    // Xử lý bàn phím
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key >= 0 && key < keys.length)
            keys[key] = action != GLFW_RELEASE;
    }

    // Callback vị trí chuột
    public static GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double xpos, double ypos) {
            mouseX = xpos;
            mouseY = ypos;
        }
    };

    // Callback click chuột
    public static GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            if (button >= 0 && button < mouseButtons.length)
                mouseButtons[button] = action != GLFW_RELEASE;
        }
    };

    // Getter phím
    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

    public static boolean isKeyPressed(int keycode) {
        return keys[keycode] && !lastKeys[keycode];
    }

    // Getter chuột
    public static boolean isMouseDown(int button) {
        return mouseButtons[button];
    }

    public static boolean isMouseClicked(int button) {
        return mouseButtons[button] && !lastMouseButtons[button];
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    // Cập nhật trạng thái phím và chuột
    public static void update() {
        for (int i = 0; i < keys.length; i++)
            lastKeys[i] = keys[i];
        for (int i = 0; i < mouseButtons.length; i++)
            lastMouseButtons[i] = mouseButtons[i];
    }
}

