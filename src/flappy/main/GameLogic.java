package flappy.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;


import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import flappy.Screen.ScreenManager;
import flappy.graphics.Shader.ShaderManager;
import flappy.input.input;


public class GameLogic implements IGameLogic {

    private int width = 1280;
    private int height = 720;
    public static long window;

    @Override
    public void init() {
        // --- 1. Khởi tạo GLFW ---
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

        // --- 2. Đặt cửa sổ giữa màn hình ---
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
            window,
            (vidmode.width() - width) / 2,
            (vidmode.height() - height) / 2
        );

        // --- 3. Callback bàn phím ---
        glfwSetKeyCallback(window, new input());
        glfwSetCursorPosCallback(window, input.cursorPosCallback);
        glfwSetMouseButtonCallback(window, input.mouseButtonCallback);

        // --- 4. Context & OpenGL ---
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities();

        // --- 5. Gọi màn hình đầu tiên ---
        ShaderManager.loadAll();
        ScreenManager.changeScreen(ScreenManager.MENU);
        // nếu có menu: ScreenManager.changeScreen(ScreenManager.MENU);
    }

    @Override
    public void update() {
        ScreenManager.update();
    }

    @Override
    public void render() {
        ScreenManager.render();
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