package com.redrockfowl.exnihilo.nei;

import exnihilo.registries.helpers.SiftReward;
import exnihilo.registries.helpers.Smashable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardRecipes {

    private Map<RewardRecipe, List<Pair<Float, Float>>> recipes;

    public RewardRecipes() {
        recipes = new HashMap<RewardRecipe, List<Pair<Float, Float>>>();
    }

    public void add(SiftReward reward) {
        RewardRecipe recipe = new RewardRecipe(reward);
        float probability = 1.0f / reward.rarity;
        Pair<Float, Float> pair = new Pair<Float, Float>(probability, 0.0f);
        if (recipes.containsKey(recipe)) {
            recipes.get(recipe).add(pair);
        } else {
            List<Pair<Float, Float>> list = new ArrayList<Pair<Float, Float>>();
            list.add(pair);
            recipes.put(recipe, list);
        }
    }

    public void add(Smashable reward) {
        RewardRecipe recipe = new RewardRecipe(reward);
        Pair<Float, Float> pair = new Pair<Float, Float>(reward.chance, reward.luckMultiplier);
        if (recipes.containsKey(recipe)) {
            recipes.get(recipe).add(pair);
        } else {
            List<Pair<Float, Float>> list = new ArrayList<Pair<Float, Float>>();
            list.add(pair);
            recipes.put(recipe, list);
        }
    }

    public Map<RewardRecipe, List<Pair<Float, Float>>> getRecipes() {
        return recipes;
    }

}
