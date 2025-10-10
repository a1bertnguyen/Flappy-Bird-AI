
package flappy.Screen.src;

import static org.lwjgl.opengl.GL11.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import flappy.graphics.Texture.Texture;
import flappy.graphics.Texture.TextureLoader;
import flappy.graphics.VertexArray.IVertexArray;
import flappy.graphics.VertexArray.VertexArray;
import flappy.graphics.VertexArray.Renderer;
import flappy.maths.Matrix4f;
import flappy.graphics.Shader.Shader;
import flappy.graphics.Shader.ShaderManager;

public class MenuScreen {
	
	private Texture background;// menu background
	private IVertexArray bgVao;


    private static class MenuButton {
        String name;
        Texture normal, selected;
        boolean hovered = false;
        float x, y, width, height;

        public MenuButton(String name, String normalPath, String selectedPath, float x, float y) {
            this.name = name;
            this.normal = TextureLoader.load(normalPath);
            this.selected = TextureLoader.load(selectedPath);
            this.x = x;
            this.y = y;
            this.width = normal.getWidth();
            this.height = normal.getHeight();
        }

        public void render(Shader shader) {
            Texture tex = hovered ? selected : normal;
            tex.bind();
//            Matrix4f model = Matrix4f.translate(x, y, 0)
//                    .multiply(Matrix4f.scale(width, height, 1));
            
            Matrix4f model = Matrix4f.translate(x, y + height, 0)
                    .multiply(Matrix4f.scale(width, -height, 1)); //  lật trục Y
            shader.setUniformMat4f("ml_matrix", model);
            vao.bind();
            Renderer.draw(vao);
            vao.unbind();
            tex.unbind();
        }

//        private static final IVertexArray vao = new VertexArray(4, 0, 6);
      
        private static final IVertexArray vao = new VertexArray(
        	    new float[] {
        	        0f, 0f, 0f,
        	        1f, 0f, 0f,
        	        1f, 1f, 0f,
        	        0f, 1f, 0f
        	    },
        	    new float[] {
        	        0f, 0f,
        	        1f, 0f,
        	        1f, 1f,
        	        0f, 1f
        	    },
        	    new byte[] {
        	        0, 1, 2,
        	        2, 3, 0
        	    }
        	);
    }

    private Map<String, MenuButton> buttons = new LinkedHashMap<>();
    private Shader shader;
    private Matrix4f projection;
    private Consumer<String> onClick; // ✅ callback

    private int screenWidth, screenHeight;

    public MenuScreen(int width, int height, Consumer<String> onClick) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.onClick = onClick;

        background = TextureLoader.load("res/menu_bg.png");
        
        bgVao = new VertexArray(
        	    new float[]{
        	        0f, 0f, 0f,
        	        1f, 0f, 0f,
        	        1f, 1f, 0f,
        	        0f, 1f, 0f
        	    },
        	    new float[]{
        	        0f, 0f,
        	        1f, 0f,
        	        1f, 1f,
        	        0f, 1f
        	    },
        	    new byte[]{0, 1, 2, 2, 3, 0}
        	);
        
        
//        shader = ShaderManager.BG;
        shader = ShaderManager.UI;
        
        projection = Matrix4f.orthographic(0, width, 0, height, -1, 1);

        float centerX = width / 2f - 128f;

        buttons.put("intro", new MenuButton("intro", "res/intro.png", "res/selected_intro.png", centerX, 500));
        buttons.put("difficulty", new MenuButton("difficulty", "res/difficulty.png", "res/selected_difficulty.png", centerX, 400));
        buttons.put("skin", new MenuButton("skin", "res/skin.png", "res/selected_skin.png", centerX, 300));
        buttons.put("start", new MenuButton("start", "res/start.png", "res/selected_start.png", centerX, 200));
    }

    public void update(float mouseX, float mouseY, boolean mousePressed) {
        float flippedY = screenHeight - mouseY; // GLFW y ngược
        for (MenuButton b : buttons.values()) {
            boolean inside = (mouseX >= b.x && mouseX <= b.x + b.width &&
                              flippedY >= b.y && flippedY <= b.y + b.height);
            b.hovered = inside;
            if (inside && mousePressed && onClick != null) {
                onClick.accept(b.name);
            }
        }
    }

    public void render() {
        shader.enable();
        shader.setUniformMat4f("pr_matrix", projection);

        glDisable(GL_DEPTH_TEST); // 🔧 tắt depth test cho UI
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // --- Vẽ background ---
        background.bind();

        float bgScaleX = screenWidth;
        float bgScaleY = screenHeight;

//        Matrix4f bgModel = Matrix4f.translate(0, 0, -0.5f)
//                .multiply(Matrix4f.scale(bgScaleX, bgScaleY, 1));
        
        Matrix4f bgModel = Matrix4f.translate(0, screenHeight, -0.5f)
                .multiply(Matrix4f.scale(bgScaleX, -bgScaleY, 1));

        shader.setUniformMat4f("ml_matrix", bgModel);
        Renderer.draw(bgVao);

        background.unbind();

        // --- Vẽ các nút menu ---
        for (MenuButton b : buttons.values()) {
            b.render(shader);
        }

        glDisable(GL_BLEND);
        shader.disable();
        glEnable(GL_DEPTH_TEST); // bật lại nếu game cần
    }

}

