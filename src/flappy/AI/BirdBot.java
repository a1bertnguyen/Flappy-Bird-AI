package flappy.AI;

import flappy.level.background.Level;
import flappy.level.bird.Bird;
import flappy.level.pipe.Pipe;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class BirdBot {

    private Bird bird;
    private Brain brain;
    private float fitness;
    private float[] vision; // [topDist, pipeDist, bottomDist, velocity]
    private Set<Pipe> passedPipes;

    public BirdBot() {
        this.bird = new Bird();
        this.brain = new Brain(4, false);
        this.fitness = 0;
        this.vision = new float[4];
        this.passedPipes = Collections.newSetFromMap(new IdentityHashMap<>());
        this.brain.generateNet();

        this.bird.setY((float) (Math.random() * 2 - 1)); // random trong [-1, 1]
    }

    public void update(Level level) {
        if (!bird.isAlive()) {
            return;
        }

        look(level);
        think();

        bird.update();

        if (bird.isAlive()) {
            fitness++;
        }
    }

    public void look(Level level) {
        Pipe closest = level.getClosestPipe(bird);
        if (closest == null) {
            vision[0] = 0;
            vision[1] = 2;
            vision[2] = 0;
            vision[3] = clamp(bird.getVelocity() / 0.2f, -1f, 1f);
            return;
        }

        // Lấy toạ độ dạng "thế giới"
        float birdX = bird.getX();
        float birdY = bird.getY();

        // Lấy mép trên và mép dưới của khe ống
        float gapTop = closest.getTopPipeBottom();   // chỗ đáy của ống trên
        float gapBottom = closest.getBottomPipeTop();   // chỗ đỉnh của ống dưới

        // 1) Khoảng cách NGANG tới ống (dương khi ống ở phía trước chim)
        float rawPipeDist = level.getPipeScreenX(closest) - birdX;   // Level.getClosestPipe đã đảm bảo > 0
        float pipeDist = rawPipeDist / 10.0f;         // scale nhẹ cho mạng dễ học

        // 2) Khoảng cách DỌC tới mép trên khe
        //    > 0: chim nằm DƯỚI mép trên (vẫn còn khoảng trống phía trên)
        //    < 0: chim đã vượt qua (đầu chạm mép trên)
        float rawTopDist = gapTop - birdY;
        float topDist = rawTopDist / 5.0f;

        // 3) Khoảng cách DỌC tới mép dưới khe
        //    > 0: chim nằm TRÊN mép dưới (vẫn còn khoảng trống phía dưới)
        //    < 0: chim đã vượt qua (chân chạm mép dưới)
        float rawBottomDist = birdY - gapBottom;
        float bottomDist = rawBottomDist / 5.0f;

        // Clamp cho an toàn (giới hạn -2..2 để tránh saturate sigmoid)
        topDist = clamp(topDist, -2f, 2f);
        bottomDist = clamp(bottomDist, -2f, 2f);
        pipeDist = clamp(pipeDist, 0f, 2f);

        vision[0] = topDist;
        vision[1] = pipeDist;
        vision[2] = bottomDist;
        vision[3] = clamp(bird.getVelocity() / 0.2f, -1f, 1f);
    }

    public void think() {
        if (!bird.isAlive()) {
            return;
        }
        float output = brain.feedForward(vision);
        if (output > 0.5f) {
            bird.jump();
        }
    }

    public Bird getBird() {
        return bird;
    }

    public float getFitness() {
        return fitness;
    }

    public void calculateFitness() {
        int distance = bird.getLifespan();       // bay xa hơn
        int passed = pipesPassed * 100;        // qua ống thưởng
        this.fitness = distance + passed;
    }
    private int pipesPassed = 0;

    public boolean hasPassedPipe(Pipe pipe) {
        return passedPipes.contains(pipe);
    }

    public void addPipePass(Pipe pipe) {
        if (passedPipes.add(pipe)) {
            pipesPassed++;
        }
    }

    public int getPipesPassed() {
        return pipesPassed;
    }

    public void mutate() {
        brain.mutate();
    }

    public Brain getBrain() {
        return brain;
    }

    public BirdBot clone() {
        BirdBot clone = new BirdBot();
        clone.fitness = 0;                 // luôn reset về 0 cho thế hệ mới
        clone.brain = brain.cloneBrain();
        clone.brain.generateNet();
        return clone;
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

}
