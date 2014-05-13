package com.redrockfowl.exnihilo.nei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PoissonBinomialDistribution {

    private List<Float> probabilities;
    private List<Pair<Integer, Float>> pmf;

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

    public List<Pair<Integer, Float>> pmf() {

        if (pmf != null) {
            return pmf;
        }

        ArrayList<Pair<Integer, Float>> list = new ArrayList<Pair<Integer, Float>>(probabilities.size());

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
                list.add(new Pair<Integer, Float>(k, sum));
            }

        }

        pmf = list;
        return list;

    }

    public float mean() {
        float mean = 0.0f;
        for (Pair<Integer, Float> pair : pmf()) {
            mean += pair.fst * pair.snd;
        }
        return mean;
    }

}
