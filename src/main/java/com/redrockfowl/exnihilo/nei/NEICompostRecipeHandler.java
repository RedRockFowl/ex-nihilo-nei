package com.redrockfowl.exnihilo.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import exnihilo.registries.CompostRegistry;
import exnihilo.registries.helpers.Compostable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class NEICompostRecipeHandler extends TemplateRecipeHandler {

    private static List<Compostable> compostables;
    private static List<PositionedStack> barrels;

    @Override
    public TemplateRecipeHandler newInstance() {

        if (barrels == null) {
            barrels = new ArrayList<PositionedStack>(ModId.Barrels.length+3);
            for (ModId barrel : ModId.Barrels) {
                Item item = barrel.getItem();
                if (item != null) {
                    if (barrel == ModId.WoodBarrel) {
                        for (int i = 0 ; i < 4; i++) {
                            barrels.add(new PositionedStack(new ItemStack(item, 1, i), 43, 34, false));
                        }
                    } else {
                        barrels.add(new PositionedStack(new ItemStack(item, 1), 43, 34, false));
                    }
                }
            }
        }

        if (compostables == null) {
            compostables = new ArrayList<Compostable>(CompostRegistry.entries.size());
            for (Compostable compostable : CompostRegistry.entries.values()) {
                if (compostable.id != 0) {
                    compostables.add(compostable);
                }
            }
        }

        return super.newInstance();

    }

    @Override
    public String getGuiTexture() {
        return new ResourceLocation(ExNihiloNEI.MODID, "gui/nei/exnihilo.png").toString();
    }

    @Override
    public String getRecipeName() {
        return Utils.translate("compost.recipe");
    }

    @Override
    public String getOverlayIdentifier() {
        return "compostrecipes";
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("compostrecipes") && getClass() == NEICompostRecipeHandler.class) {
            for (Compostable compostable : compostables) {
                this.arecipes.add(new CachedCompostRecipe(compostable));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
            if (NEIServerUtils.areStacksSameTypeCrafting(new ItemStack(Block.dirt), result)) {
                for (Compostable compostable : compostables) {
                    this.arecipes.add(new CachedCompostRecipe(compostable));
                }
            }
        }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {

        for (Compostable compostable : compostables) {
            if (NEIServerUtils.areStacksSameTypeCrafting(new ItemStack(compostable.id, 0, compostable.meta), ingredient)) {
                this.arecipes.add(new CachedCompostRecipe(compostable));
            }
        }

        for (PositionedStack barrel : barrels) {
            if (NEIServerUtils.areStacksSameTypeCrafting(barrel.item, ingredient)) {
                for (Compostable compostable : compostables) {
                    this.arecipes.add(new CachedCompostRecipe(compostable));
                }
            }
        }

    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(84, 23, 24, 17), "compostrecipes", new Object[0]));
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int id) {
        CachedCompostRecipe recipe = (CachedCompostRecipe) this.arecipes.get(id);
        if (gui.isMouseOver(recipe.input, id)) {
            String amount = String.format("%.2f", recipe.amount);
            amount = Utils.removeTrailingZeros(amount);
            String required = Utils.translate("required.amount", amount);
            currenttip.add("§7" + required);
        }
        return currenttip;
    }

    public class CachedCompostRecipe extends TemplateRecipeHandler.CachedRecipe {

        PositionedStack input;
        PositionedStack output;
        float amount;

        public CachedCompostRecipe(Compostable compostable) {
            amount = 1 / compostable.value;
            input = new PositionedStack(new ItemStack(compostable.id, 1, compostable.meta), 43, 13, false);
            output = new PositionedStack(new ItemStack(Block.dirt), 119, 24, false);
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
            return barrels.get(cycleticks / 64 % barrels.size());
        }

    }
}
