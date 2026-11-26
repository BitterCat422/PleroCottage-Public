package net.bittercat.plerocottage.machine.client.events;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.bittercat.plerocottage.machine.client.particle.OilSplashParticle;
import net.bittercat.plerocottage.machine.client.particle.SteamParticle;
import net.bittercat.plerocottage.machine.client.renderer.*;
import net.bittercat.plerocottage.machine.client.resource.FlatItemPackRepositorySource;
import net.bittercat.plerocottage.machine.client.resource.VanillaBlockPackRepositorySource;
import net.bittercat.plerocottage.machine.client.gui.*;
import net.bittercat.plerocottage.machine.client.recipebook.StockPotRecipeBookCategories;
import net.bittercat.plerocottage.machine.client.recipebook.BlenderRecipeBookCategories;
import net.bittercat.plerocottage.machine.client.recipebook.SaucepanAndWhiskRecipeBookCategories;
import net.bittercat.plerocottage.machine.client.recipebook.OvenRecipeBookCategories;
import net.bittercat.plerocottage.machine.client.recipebook.FryerRecipeBookCategories;
import net.bittercat.plerocottage.machine.client.recipebook.TeapotRecipeBookCategories;
import net.bittercat.plerocottage.machine.client.recipebook.FermenterRecipeBookCategories;
import net.bittercat.plerocottage.machine.client.recipebook.IceCreamMachineRecipeBookCategories;
import net.bittercat.plerocottage.machine.client.recipebook.RecipeBookIconModelProperties;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CookingPotRecipeCompat;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CuttingBoardRecipeCompat;
import net.bittercat.plerocottage.machine.compat.jei.RecipeOrderManager;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.machine.compat.plerocottage.SmokingRecipeManager;
import net.bittercat.plerocottage.init.BlockEntityRegistry;
import net.bittercat.plerocottage.init.MenuRegistry;
import net.bittercat.plerocottage.init.ParticleTypeRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {
    public static void handlerRegisterRecipeBookCategoriesEvent(RegisterRecipeBookCategoriesEvent event) {
        StockPotRecipeBookCategories.register(event);
        BlenderRecipeBookCategories.register(event);
        SaucepanAndWhiskRecipeBookCategories.register(event);
        OvenRecipeBookCategories.register(event);
        FryerRecipeBookCategories.register(event);
        TeapotRecipeBookCategories.register(event);
        FermenterRecipeBookCategories.register(event);
        IceCreamMachineRecipeBookCategories.register(event);
    }

    public static void handlerFMLClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RecipeBookIconModelProperties.register();
            MenuScreens.register(MenuRegistry.ICE_CREAM_MACHINE.get(), IceCreamMachineScreen::new);
            MenuScreens.register(MenuRegistry.FRYER.get(), FryerScreen::new);
            MenuScreens.register(MenuRegistry.OVEN.get(), OvenScreen::new);
            MenuScreens.register(MenuRegistry.TEAPOT.get(), TeapotScreen::new);
            MenuScreens.register(MenuRegistry.FERMENTER.get(), FermenterScreen::new);
            MenuScreens.register(MenuRegistry.BLENDER.get(), BlenderScreen::new);
            MenuScreens.register(MenuRegistry.STOCK_POT.get(), StockPotScreen::new);
            MenuScreens.register(MenuRegistry.SAUCEPAN_AND_WHISK.get(), SaucepanAndWhiskScreen::new);
        });
    }

    public static void handlerEntityRenderersEvent$RegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityRegistry.ICE_CREAM_MACHINE.get(), IceCreamMachineBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.FRYER.get(), FryerBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.OVEN.get(), OvenBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.BLENDER.get(), BlenderBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.CUTTING_BOARD.get(), CuttingBoardBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.SAUCEPAN_AND_WHISK.get(), SaucepanAndWhiskBlockEntityRenderer::new);
    }

    public static void handlerRecipesUpdatedEvent(RecipesUpdatedEvent event) {
        RecipeManager recipeManager = event.getRecipeManager();
        SmokingRecipeManager.init(recipeManager);
        MachineClientConfigSyncEvents.recipesUpdated(recipeManager);
        SlotInputLimitManager.init(recipeManager);
    }

    public static void handlerBuildCreativeModeTabContentsEvent(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
        if (!RecipeOrderManager.TAB_KEY.contains(tabKey)) {
            RecipeOrderManager.TAB_KEY.add(tabKey);
        } else {
            RecipeOrderManager.INITIALIZE = true;
        }
        if (!RecipeOrderManager.INITIALIZE) {
            event.getEntries().forEach(entry -> {
                ItemStack itemStack = entry.getKey();
                if (!RecipeOrderManager.ITEM_ORDER.containsKey(itemStack.getItem())) {
                    RecipeOrderManager.ITEM_ORDER.put(itemStack.getItem(), RecipeOrderManager.INDEX);
                    RecipeOrderManager.INDEX++;
                }
            });
        }
    }

    public static void handlerRegisterParticleProvidersEvent(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypeRegistry.COSY_STEAM.get(), SteamParticle.CosyProvider::new);
        event.registerSpriteSet(ParticleTypeRegistry.OIL_SPLASH.get(), OilSplashParticle.Provider::new);
    }

    public static void handlerAddPackFindersEvent(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            event.addRepositorySource(new VanillaBlockPackRepositorySource());
            event.addRepositorySource(new FlatItemPackRepositorySource());
        }
    }
}
