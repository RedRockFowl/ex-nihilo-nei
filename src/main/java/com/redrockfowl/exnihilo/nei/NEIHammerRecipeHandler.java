package com.redrockfowl.exnihilo.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NEIHammerRecipeHandler extends TemplateRecipeHandler {

    private static RewardRecipes recipes;
    private static List<PositionedStack> hammers;

    @Override
    public TemplateRecipeHandler newInstance() {

        if (hammers == null) {
            hammers = new ArrayList<PositionedStack>(ModId.Hammers.length);
            for (ModId hammer : ModId.Hammers) {
                Item item = hammer.getItem();
                if (item != null) {
                    hammers.add(new PositionedStack(new ItemStack(item, 1), 43, 34, false));
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

        return super.newInstance();

    }

    @Override
    public String getGuiTexture() {
        return new ResourceLocation("redrockfowl", "exnihilo/gui/nei/exnihilo.png").toString();
    }

    @Override
    public String getRecipeName() {
        return "Hammer";
    }

    @Override
    public String getOverlayIdentifier() {
        return "hammerrecipes";
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("hammerrecipes") && getClass() == NEIHammerRecipeHandler.class) {
            for (Map.Entry<RewardRecipe, PoissonBinomialDistribution> entry : recipes.getRecipes().entrySet()) {
                this.arecipes.add(new CachedHammerRecipe(entry.getKey(), entry.getValue()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Map.Entry<RewardRecipe, PoissonBinomialDistribution> entry : recipes.getRecipes().entrySet()) {
            RewardRecipe recipe = entry.getKey();
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.output, result)) {
                this.arecipes.add(new CachedHammerRecipe(recipe, entry.getValue()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {

        for (Map.Entry<RewardRecipe, PoissonBinomialDistribution> entry : recipes.getRecipes().entrySet()) {
            RewardRecipe recipe = entry.getKey();
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.input, ingredient)) {
                this.arecipes.add(new CachedHammerRecipe(recipe, entry.getValue()));
            }
        }

        for (PositionedStack hammer : hammers) {
            if (NEIServerUtils.areStacksSameTypeCrafting(hammer.item, ingredient)) {
                for (Map.Entry<RewardRecipe, PoissonBinomialDistribution> entry : recipes.getRecipes().entrySet()) {
                    CachedHammerRecipe recipe = new CachedHammerRecipe(entry.getKey(), entry.getValue());
                    recipe.other = new PositionedStack(ingredient, 43, 34);
                    this.arecipes.add(recipe);
                }
            }
        }

    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 17), "hammerrecipes", new Object[0]));
    }

    @Override
    public void drawExtras(int idx) {

        CachedHammerRecipe recipe = (CachedHammerRecipe) this.arecipes.get(idx);

        String s;
        if (recipe.distribution.probabilities.size() == 1) {

            int odds = (int) (1.0f / recipe.distribution.probabilities.get(0)) - 1;
            if (odds == 0) { return; }
            s = String.format("1:%d", odds);

        } else {

            int which = cycleticks / 48 % recipe.chances.size();
            double chance = recipe.chances.get(which);
            if (chance == 1.0) { return; }

            recipe.output.item.stackSize = recipe.amounts.get(which);

            int percent = (int) (chance * 100);

            if (percent < 1) {
                s = "<1%";
            } else {
                s = String.format("%2d%%", percent);
            }

        }

        Minecraft.getMinecraft().fontRenderer.drawString("§7" + s, getExtraX(s), 15, 0);

    }

    private static int getExtraX(String s) {
        return 95 - Minecraft.getMinecraft().fontRenderer.getStringWidth(s) / 2;
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int id) {

        CachedHammerRecipe recipe = (CachedHammerRecipe) this.arecipes.get(id);

        if (gui.isMouseOver(recipe.output, id)) {
            if (recipe.distribution.probabilities.size() > 1) {
                currenttip.add(String.format("§7%.2f average", recipe.distribution.average()));
            }
        }

        return currenttip;

    }

    public class CachedHammerRecipe extends CachedRecipe {

        PositionedStack input;
        PositionedStack output;
        PoissonBinomialDistribution distribution;
        ArrayList<Integer> amounts;
        ArrayList<Float> chances;
        PositionedStack other;

        public CachedHammerRecipe(RewardRecipe recipe, PoissonBinomialDistribution distribution) {

            this.input = new PositionedStack(recipe.input, 43, 13, false);
            this.output = new PositionedStack(recipe.output, 119, 24, false);

            this.distribution = distribution;
            Map<Integer, Float> pmf = this.distribution.pmf();

            int n = pmf.size();
            this.amounts = new ArrayList<Integer>(n);
            this.chances = new ArrayList<Float>(n);
            for (Map.Entry<Integer, Float> entry : pmf.entrySet()) {
                amounts.add(entry.getKey());
                chances.add(entry.getValue());
            }

        }

        @Override
        public PositionedStack getIngredient() {
            return input;
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }

        @Override
        public PositionedStack getOtherStack() {
            if (other == null) {
                return hammers.get(cycleticks / 64 % hammers.size());
            }
            return other;
        }

    }

}
