package net.bittercat.plerocottage.machine.compat.farmersdelight;

import net.minecraftforge.fml.ModList;
import net.bittercat.plerocottage.machine.PleroCottageMachineConfig;

public class FarmersDelightCompat {
    public static final String MOD_ID = "farmersdelight";

    public static boolean isLoad(){
        return ModList.get().isLoaded(FarmersDelightCompat.MOD_ID);
    }

    public static boolean canCuttingBoardCompat(){
        return PleroCottageMachineConfig.cuttingBoardCompat();
    }

    public static boolean canCookingPotCompat(){
        return PleroCottageMachineConfig.cookingPotCompat();
    }
}
