package com.thecherno.flappy.level;

import static org.lwjgl.glfw.GLFW.*;

import com.thecherno.flappy.graphics.Shader;
import com.thecherno.flappy.graphics.Texture;
import com.thecherno.flappy.graphics.VertexArray;
import com.thecherno.flappy.input.Input;
import com.thecherno.flappy.maths.Matrix4f;
import com.thecherno.flappy.maths.Vector3f;

public class Bird implements Updatable{
	private Vector3f position = new Vector3f();
    private float delta = 0.0f;
    private float rot;
    private float SIZE = 1.0f;

    
    @Override
    public void update() {
        position.y -= delta;
        if (Input.isKeyDown(GLFW_KEY_SPACE)) 
            delta = -0.1f;
        else
            delta += 0.01f;

        rot = -delta * 90.0f;
    }

    public void fall() {
        delta = -0.1f;
    	}

    public float getY() {
		return position.y;
		}
    
    public float getX() {
		return position.x;
		}
    
    public float getRotation() { 
    	return rot; 
    	}
    
    public Vector3f getPosition() { 
    	return position; 
    	}
    
    public float getSize() { 
    	return SIZE; 
    	}
}
