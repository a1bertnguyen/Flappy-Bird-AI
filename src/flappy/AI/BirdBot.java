
package flappy.AI;
import flappy.level.bird.Bird;

public class BirdBot {
    private Bird bird;
    private long nextJump;

    public BirdBot(Bird bird) {
        this.bird = bird;
        scheduleNextJump();
    }
    private void scheduleNextJump() {
        nextJump = System.currentTimeMillis() + (long)(Math.random() * 400 + 300);
    }
    public void update() {
        if (System.currentTimeMillis() >= nextJump) {
            bird.jump();
            scheduleNextJump();
        }
    }
    public Bird getBird() {
        return bird;
    }
}

