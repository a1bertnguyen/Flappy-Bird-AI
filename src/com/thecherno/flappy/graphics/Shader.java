package com.thecherno.flappy.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import com.thecherno.flappy.maths.Matrix4f;
import com.thecherno.flappy.maths.Vector3f;
import com.thecherno.flappy.utils.ShaderUtils;

public class Shader {
	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;

	private final int id;
    private boolean enabled = false;
    private Map<String, Integer> cache = new HashMap<>();

    public Shader(String vertexPath, String fragmentPath) {
        this.id = ShaderUtils.load(vertexPath, fragmentPath);
    }

    private int getUniformLocation(String name) {
        if (cache.containsKey(name)) return cache.get(name);
        int location = glGetUniformLocation(id, name);
        if (location == -1) 
            System.err.println("Uniform not found: " + name);
        cache.put(name, location);
        return location;
    }

    public void setUniform1i(String name, int value) {
        if (!enabled) enable();
        glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform1f(String name, float value) {
        if (!enabled) enable();
        glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform2f(String name, float x, float y) {
        if (!enabled) enable();
        glUniform2f(getUniformLocation(name), x, y);
    }

    public void setUniform3f(String name, Vector3f vec) {
        if (!enabled) enable();
        glUniform3f(getUniformLocation(name), vec.x, vec.y, vec.z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        if (!enabled) enable();
        glUniformMatrix4fv(getUniformLocation(name), false, matrix.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(id);
        enabled = true;
    }

    public void disable() {
        glUseProgram(0);
        enabled = false;
    }

}
