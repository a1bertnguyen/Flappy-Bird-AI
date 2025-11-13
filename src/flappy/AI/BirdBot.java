package flappy.AI;

import flappy.level.background.Level;
import flappy.level.bird.Bird;
import flappy.level.pipe.Pipe;

public class BirdBot {
    private Bird bird;
    private Brain brain;
    private float fitness;
    private float[] vision; // [topDist, pipeDist, bottomDist]

    public BirdBot() {
        this.bird = new Bird();
        this.brain = new Brain(3,false);
        this.fitness = 0;
        this.vision = new float[3];
        this.brain.generateNet();

        this.bird.setY((float)(Math.random() * 2 - 1)); // random trong [-1, 1]
    }

    public void update(Level level) {
        if (!bird.isAlive()) return;

        look(level);
        think();

        bird.update();

        if (bird.isAlive()) {
            fitness++;
        }
    }

    public void look(Level level) {
        Pipe closest = level.getClosestPipe(bird);
        if (closest == null) return;

        // Lấy toạ độ dạng "thế giới"
        float birdX = bird.getX();
        float birdY = bird.getY();

        // Lấy mép trên và mép dưới của khe ống
        float gapTop    = closest.getTopPipeBottom();   // chỗ đáy của ống trên
        float gapBottom = closest.getBottomPipeTop();   // chỗ đỉnh của ống dưới

        // 1) Khoảng cách NGANG tới ống (dương khi ống ở phía trước chim)
        float rawPipeDist = closest.getX() - birdX;   // Level.getClosestPipe đã đảm bảo > 0
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
        topDist    = Math.max(-2f, Math.min(2f, topDist));
        bottomDist = Math.max(-2f, Math.min(2f, bottomDist));
        pipeDist   = Math.max(0f,  Math.min(2f, pipeDist));

        vision[0] = topDist;
        vision[1] = pipeDist;
        vision[2] = bottomDist;
    }



    public void think() {
        if (!bird.isAlive()) return;
        float output = brain.feedForward(vision);
        System.out.println("OUTPUT: " + output + " | vision=" +
                vision[0] + "," + vision[1] + "," + vision[2]);
        if (output > 0.7f) {
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
        int passed   = pipesPassed * 100;        // qua ống thưởng
        this.fitness = distance + passed;
    }
    private int pipesPassed = 0;

    public void addPipePass() {
        pipesPassed++;
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



}
