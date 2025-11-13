package flappy.AI;

import flappy.level.background.Level;

import java.util.*;

public class Population {
    public List<BirdBot> players = new ArrayList<>();
    public List<Species> speciesList = new ArrayList<>();
    public int size;
    public int generation = 1;

    public Population(int size) {
        this.size = size;
        for (int i = 0; i < size; i++) {
            players.add(new BirdBot());
        }
    }

    public void updateAll(Level level) {
        for (BirdBot p : players) {
            if (p.getBird().isAlive()) {
                p.update(level); // BirdBot tự xử lý look + think + update
            }
        }
    }


    public void naturalSelection() {
        speciate();             // phân chim vào các loài
        calculateFitness();     // tính fitness cho từng chim + avg cho loài
        removeExtinctSpecies(); // xóa loài không còn cá thể

        sortSpeciesByFitness(); // Cập nhật staleness & champion & benchmarkFitness

        removeStaleSpecies();
        sortSpeciesByFitness(); // (tuỳ chọn) Sắp xếp lại sau khi xóa bớt loài

        nextGeneration();      // sinh thế hệ mới
        System.out.println(
                "Gen " + generation +
                        " | Species " + speciesList.size() +
                        " | Best fitness = " + speciesList.get(0).benchmarkFitness +
                        " | Average = " + speciesList.stream().mapToDouble(s -> s.averageFitness).average().orElse(0)
        );

    }


    public void speciate() {
        for (Species s : speciesList) {
            s.players.clear();
        }

        for (BirdBot p : players) {
            boolean added = false;
            for (Species s : speciesList) {
                if (s.isSimilar(p.getBrain())) {
                    s.add(p);
                    added = true;
                    break;
                }
            }
            if (!added) {
                speciesList.add(new Species(p));
            }
        }
    }

    public void calculateFitness() {
        for (BirdBot p : players) {
            p.calculateFitness();
        }
        for (Species s : speciesList) {
            s.calculateAverageFitness();
        }
    }


    public void removeExtinctSpecies() {
        speciesList.removeIf(s -> s.players.isEmpty());
    }

    public void removeStaleSpecies() {
        Iterator<Species> it = speciesList.iterator();
        while (it.hasNext()) {
            Species s = it.next();
            if (s.staleness >= 8 && speciesList.size() > 1) {
                it.remove();
            } else if (s.staleness >= 8) {
                s.staleness = 0;
            }
        }
    }

    public void sortSpeciesByFitness() {
        for (Species s : speciesList) {
            s.sortByFitness();
        }

        speciesList.sort((a, b) -> Float.compare(b.benchmarkFitness, a.benchmarkFitness));
    }

    public void nextGeneration() {
        List<BirdBot> children = new ArrayList<>();

        for (Species s : speciesList) {
            children.add(s.champion.clone());
        }

        int remaining = size - children.size();
        int perSpecies = Math.max(1, remaining / speciesList.size());

        for (Species s : speciesList) {
            for (int i = 0; i < perSpecies; i++) {
                children.add(s.makeOffspring());
            }
        }

        while (children.size() < size) {
            children.add(speciesList.get(0).makeOffspring());
        }

        players = children;
        generation++;
    }

    public boolean allDead() {
        for (BirdBot p : players) {
            if (p.getBird().isAlive()) return false;
        }
        return true;
    }
    public List<BirdBot> getBots() {
        return players;
    }

}
