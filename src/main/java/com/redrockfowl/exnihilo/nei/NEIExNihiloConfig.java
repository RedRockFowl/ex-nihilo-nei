package com.redrockfowl.exnihilo.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIExNihiloConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {

        API.registerRecipeHandler(new NEISieveRecipeHandler());
        API.registerUsageHandler(new NEISieveRecipeHandler());
        API.registerRecipeHandler(new NEIHammerRecipeHandler());
        API.registerUsageHandler(new NEIHammerRecipeHandler());
        API.registerRecipeHandler(new NEICompostRecipeHandler());
        API.registerUsageHandler(new NEICompostRecipeHandler());

    }

    @Override
    public String getName() {
        return "Ex Nihilo NEI";
    }

    @Override
    public String getVersion() {
        return ExNihiloNEI.VERSION;
    }

}
