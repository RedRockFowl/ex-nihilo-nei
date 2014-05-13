package com.redrockfowl.exnihilo.nei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Math.min;

public class PoissonBinomialDistribution {

    private List<Float> probabilities;
    private List<Pair<Integer, Float>> pmf;

    public PoissonBinomialDistribution(Collection<Float> probabilities) {
        this.probabilities = new ArrayList<Float>(probabilities);
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
                    product *= min(probability, 1.0f);
                }

                for (float probability : partition.snd) {
                    product *= 1 - min(probability, 1.0f);
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
