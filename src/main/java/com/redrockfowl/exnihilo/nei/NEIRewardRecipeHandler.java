package com.redrockfowl.exnihilo.nei;


import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

public abstract class NEIRewardRecipeHandler extends TemplateRecipeHandler {

    protected RewardRecipes recipes;
    protected List<PositionedStack> others;
    protected String outputId;

    @Override
    public String getGuiTexture() {
        return new ResourceLocation("redrockfowl", "exnihilo/gui/nei/exnihilo.png").toString();
    }

    @Override
    public String getOverlayIdentifier() {
        return outputId;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.outputId)) {
            for (Map.Entry<RewardRecipe, List<Pair<Float, Float>>> entry : recipes.getRecipes().entrySet()) {
                List<Pair<Float, Float>> pairs = entry.getValue();
                List<Float> probabilities = new ArrayList<Float>(pairs.size());
                for (Pair<Float, Float> pair : pairs) {
                    probabilities.add(pair.fst);
                }
                this.arecipes.add(new CachedRewardRecipe(entry.getKey(), new PoissonBinomialDistribution(probabilities)));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Map.Entry<RewardRecipe, List<Pair<Float, Float>>> entry : recipes.getRecipes().entrySet()) {
            RewardRecipe recipe = entry.getKey();
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.output, result)) {
                List<Pair<Float, Float>> pairs = entry.getValue();
                List<Float> probabilities = new ArrayList<Float>(pairs.size());
                for (Pair<Float, Float> pair : pairs) {
                    probabilities.add(pair.fst);
                }
                this.arecipes.add(new CachedRewardRecipe(recipe, new PoissonBinomialDistribution(probabilities)));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {

        for (Map.Entry<RewardRecipe, List<Pair<Float, Float>>> entry : recipes.getRecipes().entrySet()) {
            RewardRecipe recipe = entry.getKey();
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.input, ingredient)) {
                List<Pair<Float, Float>> pairs = entry.getValue();
                List<Float> probabilities = new ArrayList<Float>(pairs.size());
                for (Pair<Float, Float> pair : pairs) {
                    probabilities.add(pair.fst);
                }
                this.arecipes.add(new CachedRewardRecipe(recipe, new PoissonBinomialDistribution(probabilities)));
            }
        }

        for (PositionedStack other : others) {

            if (NEIServerUtils.areStacksSameTypeCrafting(other.item, ingredient)) {

                int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, ingredient);

                for (Map.Entry<RewardRecipe, List<Pair<Float, Float>>> entry : recipes.getRecipes().entrySet()) {

                    List<Pair<Float, Float>> pairs = entry.getValue();
                    List<Float> probabilities = new ArrayList<Float>(pairs.size());
                    for (Pair<Float, Float> pair : pairs) {
                        probabilities.add(pair.fst + fortune * pair.snd);
                    }

                    CachedRewardRecipe recipe = new CachedRewardRecipe(entry.getKey(), new PoissonBinomialDistribution(probabilities));
                    recipe.other = new PositionedStack(ingredient, 43, 34);
                    this.arecipes.add(recipe);

                }

            }

        }

    }

    @Override
    public void drawExtras(int idx) {

        CachedRewardRecipe recipe = (CachedRewardRecipe) this.arecipes.get(idx);

        String s;
        if (recipe.pmf.size() == 2 && recipe.pmf.get(0).fst == 0 && recipe.pmf.get(1).fst == 1) {

            float probability = recipe.pmf.get(1).snd;
            if (probability == 1.0f) {
                return;
            } else {
                Fraction odds = Fraction.getFraction((1.0f - probability) / probability);
                s = String.format("%d:%d", odds.getNumerator(), odds.getDenominator());
            }

        } else {

            int which = cycleticks / 48 % recipe.pmf.size();
            float probability = recipe.pmf.get(which).snd;
            if (probability == 1.0) { return; }

            recipe.output.item.stackSize = recipe.pmf.get(which).fst;

            int percent = round(probability * 100);

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

        CachedRewardRecipe recipe = (CachedRewardRecipe) this.arecipes.get(id);

        if (gui.isMouseOver(recipe.output, id)) {

            if (recipe.pmf.size() > 2 ||
                    (recipe.pmf.size() == 2 && recipe.pmf.get(0).fst != 0 && recipe.pmf.get(1).fst != 1)) {
                String mean = String.format("%.2f", recipe.mean);
                mean = Utils.removeTrailingZeros(mean);
                currenttip.add("§7" + Utils.translate("mean", mean));
            }

        }

        return currenttip;

    }

    public class CachedRewardRecipe extends CachedRecipe {

        PositionedStack input;
        PositionedStack output;
        PositionedStack other;
        List<Pair<Integer, Float>> pmf;
        float mean;

        public CachedRewardRecipe(RewardRecipe recipe, PoissonBinomialDistribution distribution) {

            this.input = new PositionedStack(recipe.input, 43, 13, false);
            this.output = new PositionedStack(recipe.output, 119, 24, false);
            this.pmf = distribution.pmf();
            this.mean = distribution.mean();

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
                return others.get(cycleticks / 64 % others.size());
            }
            return other;
        }

    }

}
