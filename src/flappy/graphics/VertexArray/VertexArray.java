

//
//package flappy.graphics.VertexArray;
//
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL30.*;
//
//import flappy.utils.BufferUtils;
//
//public class VertexArray implements IVertexArray {
//
//    private int count;
//    private int vao, vbo, ibo;
//    private boolean hasIndices;
//
//    public VertexArray(int vertexCount, int texCoordCount, int indexCount) {
//        float[] vertices = {
//            -0.5f, -0.5f, 0.0f,
//             0.5f, -0.5f, 0.0f,
//             0.5f,  0.5f, 0.0f,
//            -0.5f,  0.5f, 0.0f
//        };
//
//        byte[] indices = { 0, 1, 2, 2, 3, 0 };
//        this.count = 6;
//        this.hasIndices = true;
//
//        vao = glGenVertexArrays();
//        glBindVertexArray(vao);
//
//        vbo = glGenBuffers();
//        glBindBuffer(GL_ARRAY_BUFFER, vbo);
//        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
//        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
//        glEnableVertexAttribArray(0);
//
//        ibo = glGenBuffers();
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);
//
//        glBindBuffer(GL_ARRAY_BUFFER, 0);
//        glBindVertexArray(0);
//    }
//
//    @Override
//    public void bind() {
//        glBindVertexArray(vao);
//        glEnableVertexAttribArray(0);
//    }
//
//    @Override
//    public void unbind() {
//        glBindVertexArray(0);
//        glDisableVertexAttribArray(0);
//    }
//
//    @Override
//    public int getCount() {
//        return count;
//    }
//
//    @Override
//    public boolean hasIndices() {
//        return hasIndices;
//    }
//}

//package flappy.graphics.VertexArray;
//
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL30.*;
//
//import flappy.utils.BufferUtils;
//
//public class VertexArray implements IVertexArray {
//
//    private int count;
//    private int vao, vboVertices, vboTexCoords, ibo;
//    private boolean hasIndices;
//
//    public VertexArray(float[] vertices, float[] texCoords, byte[] indices) {
//        count = indices.length;
//        hasIndices = true;
//
//        // --- Tạo VAO ---
//        vao = glGenVertexArrays();
//        glBindVertexArray(vao);
//
//        // --- VBO vị trí ---
//        vboVertices = glGenBuffers();
//        glBindBuffer(GL_ARRAY_BUFFER, vboVertices);
//        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
//        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
//        glEnableVertexAttribArray(0);
//
//        // --- VBO texture coordinate ---
//        vboTexCoords = glGenBuffers();
//        glBindBuffer(GL_ARRAY_BUFFER, vboTexCoords);
//        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(texCoords), GL_STATIC_DRAW);
//        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
//        glEnableVertexAttribArray(1);
//
//        // --- IBO ---
//        ibo = glGenBuffers();
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);
//
//        // --- Dọn ---
//        glBindBuffer(GL_ARRAY_BUFFER, 0);
//        glBindVertexArray(0);
//    }
//
//    @Override
//    public void bind() {
//        glBindVertexArray(vao);
//    }
//
//    @Override
//    public void unbind() {
//        glBindVertexArray(0);
//    }
//
//    @Override
//    public int getCount() {
//        return count;
//    }
//
//    @Override
//    public boolean hasIndices() {
//        return hasIndices;
//    }
//}

package flappy.graphics.VertexArray;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import flappy.utils.BufferUtils;

public class VertexArray implements IVertexArray {

    private int vao, vbo, tbo, ibo;
    private int count;
    private boolean hasIndices;

    public VertexArray(float[] vertices, float[] texCoords, byte[] indices) {
        count = indices.length;
        hasIndices = true;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // VBO cho vị trí (layout=0)
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // VBO cho texCoord (layout=1)
        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(texCoords), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        // IBO
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    @Override
    public void bind() {
        glBindVertexArray(vao);
    }

    @Override
    public void unbind() {
        glBindVertexArray(0);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean hasIndices() {
        return hasIndices;
    }
}



