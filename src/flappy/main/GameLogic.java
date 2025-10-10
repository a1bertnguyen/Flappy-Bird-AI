

package flappy.main;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import flappy.graphics.Shader.ShaderManager;
import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.input.input;
import flappy.level.background.GameState;
import flappy.level.background.Level;
import flappy.maths.Matrix4f;
import flappy.Screen.src.MenuScreen; // ✅ thêm import

public class GameLogic implements IGameLogic {

    private int width = 1280;
    private int height = 720;

    private long window;
    private Level level;
    private input input;

    private MenuScreen menu;     // ✅ thêm
    private boolean inMenu = true; // ✅ thêm: bắt đầu ở menu
    
    private Texture gameOverTexture;
    private boolean gameOver = false;
    
    
    private GameState state = GameState.MENU; // hoặc GameState.PLAYING khi bắt đầu


    
    @Override     
    public void init() {
        // Khởi tạo GLFW
        if (!glfwInit()) {
            System.err.println("Could not initialize GLFW!");
            return;
        }

        // Cấu hình cửa sổ
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        window = glfwCreateWindow(width, height, "Flappy Bird", NULL, NULL);
        if (window == NULL) {
            System.err.println("Could not create GLFW window!");
            return;
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        // Input
        glfwSetKeyCallback(window, new input());

        // **Bắt buộc:** tạo OpenGL context
        glfwMakeContextCurrent(window);
        GL.createCapabilities(); // ⚠️ tạo capabilities trước khi gọi lệnh OpenGL nào
        glfwShowWindow(window);

        // Cài đặt OpenGL
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glActiveTexture(GL_TEXTURE0);

        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        // **Load shader và texture sau khi context active**
        ShaderManager.loadAll();
        gameOverTexture = TextureLoader.load("res/gameover.png"); // ✅ bây giờ an toàn

        // Thiết lập pr_matrix
        Matrix4f pr_matrix = Matrix4f.orthographic(
                -10.0f, 10.0f,
                -10.0f * 9.0f / 16.0f,
                10.0f * 9.0f / 16.0f,
                -1.0f, 1.0f
        );

        ShaderManager.BG.setUniformMat4f("pr_matrix", pr_matrix);
        ShaderManager.BG.setUniform1i("tex", 0);
        ShaderManager.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
        ShaderManager.BIRD.setUniform1i("tex", 0);
        ShaderManager.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
        ShaderManager.PIPE.setUniform1i("tex", 0);

        // Khởi tạo menu
        menu = new MenuScreen(width, height, this::onMenuAction);

        // Game chưa bắt đầu
        level = null;

        // Input handler
        input = new input();
    }

    
    
    // ✅ Gọi khi chọn menu (callback)
    private void onMenuAction(String action) {
    	
    	System.out.println("Clicked action: " + action); // debug
    	
        if (action.equalsIgnoreCase("start")) {
            System.out.println("Menu: Start Game");
            inMenu = false;
            level = new Level(); // khởi tạo game thật
        } else {
            System.out.println("Clicked menu button: " + action);
        }
    }

    @Override
    public void update() {
        glfwPollEvents();

        if (inMenu) {
            double[] mx = new double[1];
            double[] my = new double[1];
            glfwGetCursorPos(window, mx, my);
            boolean mousePressed = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS;
            menu.update((float) mx[0], (float) my[0], mousePressed);
        } else {
            level.update();
            input.update();

        }
              
    }

    @Override
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (inMenu) {
            menu.render();
        } else {
        	
        	
            level.render();
        }

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
