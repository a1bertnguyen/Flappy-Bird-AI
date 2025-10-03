package com.thecherno.flappy.graphics;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
		public static void draw(IVertexArray vao) {
			vao.bind();
		    if (vao.hasIndices()) {
		        glDrawElements(GL_TRIANGLES, vao.getCount(), GL_UNSIGNED_BYTE, 0);
		    } else {
		        glDrawArrays(GL_TRIANGLES, 0, vao.getCount());
		    }
}
		}
