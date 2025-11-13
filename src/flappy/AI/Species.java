package flappy.AI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Species {
    public List<BirdBot> players = new ArrayList<>();
    public float averageFitness = 0;
    public float threshold = 1.2f;
    public float benchmarkFitness;
    public Brain benchmarkBrain;
    public BirdBot champion;
    public int staleness = 0;

    public Species(BirdBot player) {
        players.add(player);
        benchmarkFitness = player.getFitness();
        benchmarkBrain = player.getBrain().cloneBrain();
        champion = player.clone();
    }

    public boolean isSimilar(Brain brain) {
        float similarity = weightDifference(benchmarkBrain, brain);
        return similarity < threshold;
    }

    public static float weightDifference(Brain b1, Brain b2) {
        float total = 0f;
        int minSize = Math.min(b1.connections.size(), b2.connections.size());
        for (int i = 0; i < minSize; i++) {
            total += Math.abs(b1.connections.get(i).weight - b2.connections.get(i).weight);
        }
        return total;
    }

    public void add(BirdBot player) {
        players.add(player);
    }

    public void sortByFitness() {
        players.sort(Comparator.comparing(BirdBot::getFitness).reversed());

        if (players.get(0).getFitness() > benchmarkFitness) {
            staleness = 0;
            benchmarkFitness = players.get(0).getFitness();
            champion = players.get(0).clone();
            benchmarkBrain = champion.getBrain().cloneBrain();
        } else {
            staleness++;
        }

    }

    public void calculateAverageFitness() {
        if (players.isEmpty()) {
            averageFitness = 0;
            return;
        }

        float sum = 0f;
        for (BirdBot p : players) {
            sum += p.getFitness();
        }
        averageFitness = sum / players.size();
    }

    public BirdBot makeOffspring() {
        Random rand = new Random();
        BirdBot parent = players.get(rand.nextInt(players.size()));
        BirdBot baby = parent.clone();
        baby.mutate();
        return baby;
    }
}
