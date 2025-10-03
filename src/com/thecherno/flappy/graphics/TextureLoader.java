package com.thecherno.flappy.graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import static org.lwjgl.opengl.GL11.*;
import com.thecherno.flappy.utils.BufferUtils;

public class TextureLoader {
	public static Texture load(String path) {
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            int width = image.getWidth();
            int height = image.getHeight();

            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            int[] data = new int[width * height];
            for (int i = 0; i < width * height; i++) {
                int a = (pixels[i] & 0xff000000) >> 24;
                int r = (pixels[i] & 0xff0000) >> 16;
                int g = (pixels[i] & 0xff00) >> 8;
                int b = (pixels[i] & 0xff);
                data[i] = a << 24 | b << 16 | g << 8 | r;
            }

            int id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
            glBindTexture(GL_TEXTURE_2D, 0);

            return new Texture(id, width, height);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load texture: " + path, e);
        }
    }
}
