package com.redrockfowl.exnihilo.nei;

import codechicken.nei.PositionedStack;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftReward;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.Rectangle;
import java.util.ArrayList;

public class NEISieveRecipeHandler extends NEIRewardRecipeHandler {

    public NEISieveRecipeHandler() {

        outputId = "sieverecipes";

        if (others == null) {
            others = new ArrayList<PositionedStack>(4);
            Item sieve = ModId.Sieve.getItem();
            if (sieve != null) {
                for (int i = 0; i < 4; i++) {
                    others.add(new PositionedStack(new ItemStack(sieve, 1, i), 43, 34, false));
                }
            }
        }

        if (recipes == null) {
            recipes = new RewardRecipes();
            for (SiftReward reward : SieveRegistry.rewards) {
                if (reward.sourceID != 0 && reward.id != 0) {
                    recipes.add(reward);
                }
            }
        }

    }

    @Override
    public String getRecipeName() {
        return Utils.translate("sieve.recipe");
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 17), "sieverecipes"));
    }

}
