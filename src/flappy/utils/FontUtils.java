package flappy.utils;

import org.lwjgl.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
public class FontUtils {
   private static final int BITMAP_W = 512;
   private static final int BITMAP_H = 512;
   private static final int CHAR_START = 32;
   private static final int CHAR_COUNT = 96;
   private int fontTex;
   private STBTTBakedChar.Buffer cdata;
   public FontUtils(String fontPath, float fontSize) {
       try {
           byte[] fontBytes = Files.readAllBytes(Paths.get(fontPath));
           ByteBuffer ttf = BufferUtils.createByteBuffer(fontBytes.length).put(fontBytes);
           ttf.flip();
           // Bản đồ ký tự trắng đen
           ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
           cdata = STBTTBakedChar.malloc(CHAR_COUNT);
           stbtt_BakeFontBitmap(ttf, fontSize, bitmap, BITMAP_W, BITMAP_H, CHAR_START, cdata);
           // ⚙️ Convert sang RGBA (lấy RED làm alpha)
           ByteBuffer rgba = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H * 4);
           for (int i = 0; i < BITMAP_W * BITMAP_H; i++) {
               byte val = bitmap.get(i);
               rgba.put((byte) 255); // R
               rgba.put((byte) 255); // G
               rgba.put((byte) 255); // B
               rgba.put(val);        // A = độ sáng ký tự
           }
           rgba.flip();
           // ⚙️ Tạo texture RGBA đúng chuẩn
           fontTex = glGenTextures();
           glBindTexture(GL_TEXTURE_2D, fontTex);
           glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
           glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, BITMAP_W, BITMAP_H, 0, GL_RGBA, GL_UNSIGNED_BYTE, rgba);
           glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
           glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
           glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
           glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
           // ⚙️ Cấu hình blending trong suốt chuẩn
           glEnable(GL_BLEND);
           glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
           glDisable(GL_ALPHA_TEST); // không cần test alpha thủ công
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   public void drawText(String text, float x, float y) {
       if (text == null || text.isEmpty()) return;
       glEnable(GL_BLEND);
       glEnable(GL_TEXTURE_2D);
       glBindTexture(GL_TEXTURE_2D, fontTex);
       glColor4f(1f, 1f, 1f, 1f); // chữ trắng (đổi màu được)
       FloatBuffer xb = BufferUtils.createFloatBuffer(1);
       xb.put(0, x);
       FloatBuffer yb = BufferUtils.createFloatBuffer(1);
       yb.put(0, y);
       glBegin(GL_QUADS);
       for (int i = 0; i < text.length(); i++) {
           char c = text.charAt(i);
           if (c < CHAR_START || c >= CHAR_START + CHAR_COUNT) continue;
           STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
           stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c - CHAR_START, xb, yb, q, true);
           glTexCoord2f(q.s0(), q.t0()); glVertex2f(q.x0(), q.y0());
           glTexCoord2f(q.s1(), q.t0()); glVertex2f(q.x1(), q.y0());
           glTexCoord2f(q.s1(), q.t1()); glVertex2f(q.x1(), q.y1());
           glTexCoord2f(q.s0(), q.t1()); glVertex2f(q.x0(), q.y1());
           q.free();
       }
       glEnd();
       glDisable(GL_TEXTURE_2D);
       glDisable(GL_BLEND);
   }
   // Tính chiều rộng chuỗi
   public float getTextWidth(String text) {
       float width = 0f;
       float[] xPos = new float[]{0};
       float[] yPos = new float[]{0};
       for (int i = 0; i < text.length(); i++) {
           char c = text.charAt(i);
           if (c < CHAR_START || c >= CHAR_START + CHAR_COUNT) continue;
           STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
           stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c - CHAR_START, xPos, yPos, q, true);
           width = q.x1();
           q.free();
       }
       return width;
   }
   // Tính chiều cao font (chính xác hơn)
   public float getTextHeight() {
       STBTTBakedChar ch = cdata.get('A' - CHAR_START);
       return ch.y1() - ch.y0();
   }
}

