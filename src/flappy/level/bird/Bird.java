package flappy.level.bird;

import flappy.level.Updateable;
import flappy.maths.Vector3f;

public class Bird implements Updateable {

    private Vector3f position = new Vector3f();
    private float delta = 0.0f;
    private float rot;
    private static final float SIZE = 1.0f;
    private boolean isAlive = true;
    private int lifespan = 0;

    @Override
    public void update() {
        // Trọng lực
        position.y += delta;
        delta += 0.01f;
        if (delta > 0.2f) {
            delta = 0.2f;
        }

        // Xoay chim theo vận tốc
        rot = -delta * 90.0f;

        lifespan++;
    }

    public void jump() {
        delta = -0.15f;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill() {
        isAlive = false;
    }

    public float getY() {
        return position.y;
    }

    public float getX() {
        return position.x;
    }

    public void setY(float y) {
        position.y = y;
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

    public int getLifespan() {
        return lifespan;
    }

    public void fall() {
        delta = 0.2f;
        rot = -90f;
    }

}
