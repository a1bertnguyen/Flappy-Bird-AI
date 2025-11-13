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

        if (closest != null) {
            float topDist = Math.max(0, bird.getY() - closest.getTopPipeBottom()) / 500f;
            float pipeDist = Math.max(0, closest.getX() - bird.getX()) / 500f;
            float bottomDist = Math.max(0, closest.getBottomPipeTop() - bird.getY()) / 500f;

            vision[0] = topDist;
            vision[1] = pipeDist;
            vision[2] = bottomDist;
        }
    }

    public void think() {
        if (!bird.isAlive()) return;
        float output = brain.feedForward(vision);
        if (output > 0.73f) {
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
        this.fitness = bird.getLifespan(); // hoặc = fitness++
    }

    public void mutate() {
        brain.mutate();
    }

    public Brain getBrain() {
        return brain;
    }

    public BirdBot clone() {
        BirdBot clone = new BirdBot();
        clone.fitness = this.fitness;
        clone.brain = brain.cloneBrain();
        clone.brain.generateNet();
        return clone;
    }


}
