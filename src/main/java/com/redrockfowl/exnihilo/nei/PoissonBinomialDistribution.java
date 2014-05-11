package com.redrockfowl.exnihilo.nei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

public class PoissonBinomialDistribution {

    List<Integer> rarities;
    List<Float> probabilities;
    private Map<Integer, Float> pmf;

    private PoissonBinomialDistribution() {
        rarities = new ArrayList<Integer>();
        probabilities = new ArrayList<Float>();
    }

    public PoissonBinomialDistribution(int rarity) {
        this();
        addRarity(rarity);
    }

    public PoissonBinomialDistribution(float probability) {
        this();
        addProbability(probability);
    }

    public void addMultiplier(float multiplier) {
        this.pmf = null;
        for (int i = 0; i < probabilities.size(); i++) {
            probabilities.set(i, probabilities.get(i) + multiplier);
        }
    }

    public void addProbability(float probability) {
        pmf = null;
        probabilities.add(probability);
        rarities.add(round(1 / probability));
    }

    public void addRarity(int rarity) {
        pmf = null;
        rarities.add(rarity);
        probabilities.add(1.0f / rarity);
    }

    public Map<Integer, Float> pmf() {

        if (pmf != null) {
            return pmf;
        }

        HashMap<Integer, Float> map = new HashMap<Integer, Float>(probabilities.size());

        for (int k = 0; k <= probabilities.size(); k++) {

            Partition<Float> partitions = new Partition<Float>(probabilities, k);

            float sum = 0.0f;
            for (Pair<Collection<Float>, Collection<Float>> partition : partitions) {

                float product = 1.0f;

                for (float probability : partition.fst) {
                    product *= probability;
                }

                for (float probability : partition.snd) {
                    product *= 1 - probability;
                }

                sum += product;

            }

            if (sum != 0.0) {
                map.put(k, sum);
            }

        }

        pmf = map;
        return map;

    }

    public float average() {
        float average = 0.0f;
        for (Map.Entry<Integer, Float> entry : pmf().entrySet()) {
            average += entry.getKey() * entry.getValue();
        }
        return average;
    }

}
