package com.redrockfowl.exnihilo.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIExNihiloConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {

        NEISieveRecipeHandler sieveRecipeHandler = new NEISieveRecipeHandler();
        API.registerRecipeHandler(sieveRecipeHandler);
        API.registerUsageHandler(sieveRecipeHandler);

        NEIHammerRecipeHandler hammerRecipeHandler = new NEIHammerRecipeHandler();
        API.registerRecipeHandler(hammerRecipeHandler);
        API.registerUsageHandler(hammerRecipeHandler);

        NEICompostRecipeHandler compostRecipeHandler = new NEICompostRecipeHandler();
        API.registerRecipeHandler(compostRecipeHandler);
        API.registerUsageHandler(compostRecipeHandler);

    }

    @Override
    public String getName() {
        return ExNihiloNEI.NAME;
    }

    @Override
    public String getVersion() {
        return ExNihiloNEI.VERSION;
    }

}
