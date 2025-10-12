package flappy.input;

import flappy.graphics.Texture.Texture;
import flappy.utils.FontUtils;

import static org.lwjgl.opengl.GL11.*;

public class Button {
    private float x, y, width, height;
    private Texture texture;
    private String label;
    private FontUtils font;
    private boolean hovered = false;

    public Button(float x, float y, float width, float height, Texture texture, String label, FontUtils font) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.label = label;
        this.font = font;
    }

    public boolean isHovered(float mouseX, float mouseY) {
        hovered = mouseX >= x && mouseX <= x + width &&
                  mouseY >= y && mouseY <= y + height;
        return hovered;
    }

    public void render() {
        // Nếu hover thì làm nút sáng hơn
        if (hovered)
            glColor4f(1f, 1f, 1f, 1f);
        else
            glColor4f(0.9f, 0.9f, 0.9f, 1f);

        // Vẽ nền nút
        glEnable(GL_TEXTURE_2D);
        texture.bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(x, y);
        glTexCoord2f(1, 0); glVertex2f(x + width, y);
        glTexCoord2f(1, 1); glVertex2f(x + width, y + height);
        glTexCoord2f(0, 1); glVertex2f(x, y + height);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);

        // Vẽ chữ ở giữa nút
        if (font != null && label != null && !label.isEmpty()) {
            float textWidth = font.getTextWidth(label);
            float textHeight = font.getTextHeight();
            
            // canh giữa chữ trong nút
            float textX = x + (width - textWidth) / 2f;
            float textY = y + (height + textHeight) / 2f - textHeight * 0.3f;

            // Chữ có thể sáng hơn khi hover
            if (hovered)
                glColor4f(1f, 1f, 0.8f, 1f);
            else
                glColor4f(1f, 1f, 1f, 1f);

            font.drawText(label, textX, textY);
        }

        glDisable(GL_BLEND);
    }
    
    public String getLabel() {
    	return label;
    }
}