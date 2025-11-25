 


package flappy.level.bird;

import static org.lwjgl.glfw.GLFW.*;

import flappy.input.input;
import flappy.maths.Vector3f;
import flappy.level.Updateable;

public class Bird implements Updateable {

    private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
    private float delta = 0.0f;
    private float rot;
    private float SIZE = 1.0f;

    // ⭐ Tham số mới cho chuyển động mượt EASY
    private final float FLAP_STRENGTH = -0.11f;   // mạnh hơn số âm = bay lên mạnh, nên giảm cho EASY
    private final float GRAVITY = 0.007f;         // rơi rất nhẹ -> giống Flappy Bird gốc
    private final float MAX_DELTA = 0.25f;        // tránh rơi quá nhanh

    @Override
    public void update() {

        // ⭐ Bird movement
        position.y -= delta;

        if (input.isKeyPressed(GLFW_KEY_SPACE)) {
            delta = FLAP_STRENGTH;         // lực vỗ cánh
        } else {
            delta += GRAVITY;              // trọng lực nhẹ → rơi chậm
        }

        // ⭐ Giới hạn tốc độ rơi để không quá nhanh
        if (delta > MAX_DELTA)
            delta = MAX_DELTA;

        // ⭐ Độ xoay mượt, không quá mạnh
        rot = -delta * 70.0f;
    }

    public void flap() {
        delta = FLAP_STRENGTH;
    }

    public void fall() {
        delta = 0.10f;   // không dùng nhiều, chỉ khi gameover
    }

    public void reset() {
        this.position.x = 0.0f;
        this.position.y = 0.0f;
        this.delta = 0;
        this.rot = 0;
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
