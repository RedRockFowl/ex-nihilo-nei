package com.redrockfowl.exnihilo.nei;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public enum ModId {

      WoodBarrel(Mod.ExNihilo, Type.BLOCK, "barrel")
    , StoneBarrel(Mod.ExNihilo, Type.BLOCK, "barrel_stone")

    , WoodHammer(Mod.ExNihilo, Type.ITEM, "hammer_wood")
    , StoneHammer(Mod.ExNihilo, Type.ITEM, "hammer_stone")
    , IronHammer(Mod.ExNihilo, Type.ITEM, "hammer_iron")
    , GoldHammer(Mod.ExNihilo, Type.ITEM, "hammer_gold")
    , DiamondHammer(Mod.ExNihilo, Type.ITEM, "hammer_diamond")
    , InvarHammer(Mod.ExAliquo, Type.ITEM, "ExAliquo.InvarHammer")
    , ThaumiumHammer(Mod.ExAliquo, Type.ITEM, "ExAliquo.ThaumHammer")
    , CopperHammer(Mod.ExAliquo, Type.ITEM, "ExAliquo.CopperHammer")
    , TinHammer(Mod.ExAliquo, Type.ITEM, "ExAliquo.TinHammer")
    , SilverHammer(Mod.ExAliquo, Type.ITEM, "ExAliquo.SilverHammer")
    , LeadHammer(Mod.ExAliquo, Type.ITEM, "ExAliquo.LeadHammer")
    , PlatinumHammer(Mod.ExAliquo, Type.ITEM, "ExAliquo.PlatinumHammer")
    , NickelHammer(Mod.ExAliquo, Type.ITEM, "ExAliquo.NickelHammer")
    , AluminumHammer(Mod.ExAliquo, Type.ITEM, "ExAliquo.AluminumHammer")

    , Sieve(Mod.ExNihilo, Type.BLOCK, "sifting_table")
    ;

    public static ModId[] Barrels = new ModId[]
        { WoodBarrel
        , StoneBarrel
        };

    public static ModId[] Hammers = new ModId[]
        { WoodHammer
        , StoneHammer
        , IronHammer
        , GoldHammer
        , DiamondHammer
        , InvarHammer
        , ThaumiumHammer
        , CopperHammer
        , TinHammer
        , SilverHammer
        , LeadHammer
        , PlatinumHammer
        , NickelHammer
        , AluminumHammer
        };

    private final Mod mod;
    private final Type type;
    private final String name;

    private ModId(Mod mod, Type type, String name) {
        this.mod = mod;
        this.type = type;
        this.name = name;
    }

    public int getId() {
        switch (this.type) {
            case BLOCK:
                Block block = getBlock();
                return block != null ? block.blockID : -1;
            case ITEM:
                Item item = getItem();
                return item != null ? item.itemID : -1;
            default:
                return -1;
        }
    }

    public Block getBlock() {
        return GameRegistry.findBlock(this.mod.name, this.name);
    }

    public Item getItem() {
        return GameRegistry.findItem(this.mod.name, this.name);
    }

    private enum Mod {
        ExNihilo("crowley.skyblock"),
        ExAliquo("exaliquo");

        private final String name;

        private Mod(String name) {
            this.name = name;
        }
    }

    private enum Type {
        BLOCK, ITEM;
    }

}
