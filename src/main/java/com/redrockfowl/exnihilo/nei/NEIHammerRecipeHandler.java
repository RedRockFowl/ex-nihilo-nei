package com.redrockfowl.exnihilo.nei;

import codechicken.nei.PositionedStack;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.Rectangle;
import java.util.ArrayList;

public class NEIHammerRecipeHandler extends NEIRewardRecipeHandler {

    public NEIHammerRecipeHandler() {

        outputId = "hammerrecipes";

        if (others == null) {
            others = new ArrayList<PositionedStack>(ModId.Hammers.length);
            for (ModId hammer : ModId.Hammers) {
                Item item = hammer.getItem();
                if (item != null) {
                    others.add(new PositionedStack(new ItemStack(item, 1), 43, 34, false));
                }
            }
        }

        if (recipes == null) {
            recipes = new RewardRecipes();
            for (Smashable reward : HammerRegistry.rewards) {
                if (reward.sourceID != 0 && reward.id != 0) {
                    recipes.add(reward);
                }
            }
        }

    }

    @Override
    public String getRecipeName() {
        return Utils.translate("hammer.recipe");
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 17), "hammerrecipes"));
    }

}
