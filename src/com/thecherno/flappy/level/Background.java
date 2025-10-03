package com.thecherno.flappy.level;

import com.thecherno.flappy.graphics.Shader;
import com.thecherno.flappy.graphics.ShaderManager;
import com.thecherno.flappy.graphics.Texture;
import com.thecherno.flappy.graphics.TextureLoader;
import com.thecherno.flappy.graphics.VertexArray;
import com.thecherno.flappy.graphics.Renderer;
import com.thecherno.flappy.maths.Matrix4f;
import com.thecherno.flappy.maths.Vector3f;

public class Background {
	private VertexArray background;
//	private VertexArray fade;
    private Texture bgTexture;
    private int map = 0;
//    private float time = 0.0f;

    public Background() {
        float[] vertices = new float[] {
            -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
            -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
              0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
              0.0f, -10.0f * 9.0f / 16.0f, 0.0f
        };
        byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
        float[] tcs = new float[] { 0,1, 0,0, 1,0, 1,1 };

//        fade = new VertexArray(6);
        background = new VertexArray(vertices, indices, tcs);
        bgTexture = TextureLoader.load("res/bg.jpeg");
    }

//    public void update() {
//        time += 0.01f;
//    }

    public void render(float birdY, int xScroll) {
        bgTexture.bind();
        ShaderManager.BG.enable();
        ShaderManager.BG.setUniform2f("bird", 0, birdY);
        background.bind();

        for (int i = map; i < map + 4; i++) {
        	ShaderManager.BG.setUniformMat4f("vw_matrix",
                Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
            Renderer.draw(background);
        }

        ShaderManager.BG.disable();
        bgTexture.unbind();

    }

    public void nextMap() {
        map++;
    }
}
