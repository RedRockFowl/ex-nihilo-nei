package com.redrockfowl.exnihilo.nei;

import codechicken.nei.NEIServerUtils;
import exnihilo.registries.helpers.Compostable;
import exnihilo.registries.helpers.SiftReward;
import exnihilo.registries.helpers.Smashable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class RewardRecipe {

    protected ItemStack input;
    protected ItemStack output;

    RewardRecipe(SiftReward reward) {
        input = new ItemStack(reward.sourceID, 1, reward.sourceMeta);
        output = new ItemStack(reward.id, 1, reward.meta);
    }

    RewardRecipe(Smashable reward) {
        input = new ItemStack(reward.sourceID, 1, reward.sourceMeta);
        output = new ItemStack(reward.id, 1, reward.meta);
    }

    RewardRecipe(Compostable compostable) {
        input = new ItemStack(compostable.id, 1, compostable.meta);
        output = new ItemStack(Block.dirt);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        RewardRecipe other = (RewardRecipe) obj;
        return NEIServerUtils.areStacksSameType(this.input, other.input) && NEIServerUtils.areStacksSameType(this.output, other.output);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + input.itemID;
        result = result * prime + input.getItemDamage();
        result = result * prime + output.itemID;
        result = result * prime + output.getItemDamage();
        return result;
    }

}
