package flappy.level.bird;

import static org.lwjgl.glfw.GLFW.*;

import flappy.graphics.Shader.Shader;
import flappy.graphics.Texture.Texture;
import flappy.graphics.VertexArray.VertexArray;
import flappy.input.input;
import flappy.maths.Matrix4f;
import flappy.maths.Vector3f;
import flappy.level.Updateable;

public class Bird implements Updateable{
	private Vector3f position = new Vector3f();
    private float delta = 0.0f;
    private float rot;
    private float SIZE = 1.0f;

    
    @Override
    public void update() {
        position.y -= delta;
        if (input.isKeyPressed(GLFW_KEY_SPACE)) 
            delta = -0.15f;
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