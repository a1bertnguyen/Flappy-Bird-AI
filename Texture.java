package flappy.graphics.Texture;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;



import com.thecherno.flappy.utils.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	
	private final int id;
    private final int width;
    private final int height;

    public Texture(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getId() { return id; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

}