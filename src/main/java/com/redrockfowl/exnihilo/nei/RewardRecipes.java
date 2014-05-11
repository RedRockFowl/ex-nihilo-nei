package com.redrockfowl.exnihilo.nei;

import exnihilo.registries.helpers.Compostable;
import exnihilo.registries.helpers.SiftReward;
import exnihilo.registries.helpers.Smashable;

import java.util.HashMap;
import java.util.Map;

public class RewardRecipes {

    private Map<RewardRecipe, PoissonBinomialDistribution> recipes;

    public RewardRecipes() {
        recipes = new HashMap<RewardRecipe, PoissonBinomialDistribution>();
    }

    public void add(SiftReward reward) {
        RewardRecipe recipe = new RewardRecipe(reward);
        if (recipes.containsKey(recipe)) {
            recipes.get(recipe).addRarity(reward.rarity);
        } else {
            recipes.put(recipe, new PoissonBinomialDistribution(reward.rarity));
        }
    }

    public void add(Smashable reward) {
        RewardRecipe recipe = new RewardRecipe(reward);
        if (recipes.containsKey(recipe)) {
            recipes.get(recipe).addProbability(reward.chance);
        } else {
            recipes.put(recipe, new PoissonBinomialDistribution(reward.chance));
        }
    }

    public void add(Compostable compostable) {
        RewardRecipe recipe = new RewardRecipe(compostable);
        if (recipes.containsKey(recipe)) {
            recipes.get(recipe).addProbability(compostable.value);
        } else {
            recipes.put(recipe, new PoissonBinomialDistribution(compostable.value));
        }
    }

    public Map<RewardRecipe, PoissonBinomialDistribution> getRecipes() {
        return recipes;
    }

}
