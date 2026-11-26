package net.bittercat.plerocottage.machine;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraft.world.inventory.RecipeBookType;
import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.machine.events.CommonEventManager;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.init.*;
import software.bernie.geckolib.GeckoLib;

public class PleroCottageMachine {
    public static final String MOD_ID = PleroCottage.MODID;
    public static final RecipeBookType RECIPE_TYPE_ICE_CREAM_MACHINE = RecipeBookType.create("ICE_CREAM_MACHINE");
    public static final RecipeBookType RECIPE_TYPE_STOCK_POT = RecipeBookType.create("STOCK_POT");
    public static final RecipeBookType RECIPE_TYPE_BLENDER = RecipeBookType.create("BLENDER");
    public static final RecipeBookType RECIPE_TYPE_SAUCEPAN_AND_WHISK = RecipeBookType.create("SAUCEPAN_AND_WHISK");
    public static final RecipeBookType RECIPE_TYPE_OVEN = RecipeBookType.create("OVEN");
    public static final RecipeBookType RECIPE_TYPE_FRYER = RecipeBookType.create("FRYER");
    public static final RecipeBookType RECIPE_TYPE_TEAPOT = RecipeBookType.create("TEAPOT");
    public static final RecipeBookType RECIPE_TYPE_FERMENTER = RecipeBookType.create("FERMENTER");

    public static void initialize(IEventBus modEventBus) {
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        CommonEventManager.init(modEventBus, forgeEventBus);
        GeckoLib.initialize();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PleroCottageMachineClient.init(modEventBus, forgeEventBus));
        net.bittercat.plerocottage.machine.init.VillagerRegistry.POT_TYPE.register(modEventBus);
        net.bittercat.plerocottage.machine.init.VillagerRegistry.VILLAGER_PROFESSION.register(modEventBus);
        PleroCottageMachinePacket.register();
    }
}
