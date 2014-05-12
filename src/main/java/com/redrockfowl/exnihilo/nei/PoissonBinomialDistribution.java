package com.redrockfowl.exnihilo.nei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoissonBinomialDistribution {

    List<Float> probabilities;
    private Map<Integer, Float> pmf;

    private PoissonBinomialDistribution() {
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

    public void addProbability(float probability) {
        pmf = null;
        probabilities.add(probability);
    }

    public void addRarity(int rarity) {
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
