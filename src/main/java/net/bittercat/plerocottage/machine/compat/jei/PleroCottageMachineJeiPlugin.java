package net.bittercat.plerocottage.machine.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeManager;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.client.events.MachineClientConfigSyncEvents;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CookingPotRecipeCompat;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CuttingBoardRecipeCompat;
import net.bittercat.plerocottage.machine.compat.jei.category.*;
import net.bittercat.plerocottage.machine.compat.plerocottage.SmokingRecipeManager;
import net.bittercat.plerocottage.machine.recipes.*;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JeiPlugin
public class PleroCottageMachineJeiPlugin implements IModPlugin {
    private static IJeiRuntime runtime;
    private static boolean registeredWithServerConfig;
    private static boolean initialConfigApplied;
    public static final RecipeType<IceCreamRecipe> ICE_CREAM = RecipeType.create(PleroCottageMachine.MOD_ID, "ice_cream", IceCreamRecipe.class);
    public static final RecipeType<FastFryRecipe> FAST_FRY = RecipeType.create(PleroCottageMachine.MOD_ID, "fast_fry", FastFryRecipe.class);
    public static final RecipeType<SlowFryRecipe> SLOW_FRY = RecipeType.create(PleroCottageMachine.MOD_ID, "slow_fry", SlowFryRecipe.class);
    public static final RecipeType<OvenRecipe> OVEN = RecipeType.create(PleroCottageMachine.MOD_ID, "oven", OvenRecipe.class);
    public static final RecipeType<TeapotRecipe> TEAPOT = RecipeType.create(PleroCottageMachine.MOD_ID, "teapot", TeapotRecipe.class);
    public static final RecipeType<DimFermentationRecipe> DIM_FERMENTATION = RecipeType.create(PleroCottageMachine.MOD_ID, "dim_fermentation", DimFermentationRecipe.class);
    public static final RecipeType<NormalFermentationRecipe> NORMAL_FERMENTATION = RecipeType.create(PleroCottageMachine.MOD_ID, "normal_fermentation", NormalFermentationRecipe.class);
    public static final RecipeType<BrightFermentationRecipe> BRIGHT_FERMENTATION = RecipeType.create(PleroCottageMachine.MOD_ID, "bright_fermentation", BrightFermentationRecipe.class);
    public static final RecipeType<BlenderRecipe> BLENDER = RecipeType.create(PleroCottageMachine.MOD_ID, "blender", BlenderRecipe.class);
    public static final RecipeType<CuttingBoardRecipeWrapper> CUTTING_BOARD = RecipeType.create(PleroCottageMachine.MOD_ID, "cutting_board", CuttingBoardRecipeWrapper.class);
    public static final RecipeType<StockPotRecipe> STOCK_POT = RecipeType.create(PleroCottageMachine.MOD_ID, "stock_pot", StockPotRecipe.class);
    public static final RecipeType<SaucepanAndWhiskRecipe> SAUCEPAN_AND_WHISK = RecipeType.create(PleroCottageMachine.MOD_ID, "saucepan_and_whisk", SaucepanAndWhiskRecipe.class);
    public static final RecipeType<ShakerRecipe> SHAKER = RecipeType.create(PleroCottageMachine.MOD_ID, "shaker", ShakerRecipe.class);

    public static final RecipeType<FastFryRecipe> FRYER_SMOKING = RecipeType.create(PleroCottageMachine.MOD_ID, "fryer_smoking", FastFryRecipe.class);
    public static final RecipeType<OvenRecipe> OVEN_SMOKING = RecipeType.create(PleroCottageMachine.MOD_ID, "oven_smoking", OvenRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new IceCreamRecipeCategory(guiHelper));
        registration.addRecipeCategories(new FastFryRecipeCategory(guiHelper));
        registration.addRecipeCategories(new SlowFryRecipeCategory(guiHelper));
        registration.addRecipeCategories(new OvenRecipeCategory(guiHelper));
        registration.addRecipeCategories(new TeapotRecipeCategory(guiHelper));
        registration.addRecipeCategories(new DimFermentationRecipeCategory(guiHelper));
        registration.addRecipeCategories(new NormalFermentationRecipeCategory(guiHelper));
        registration.addRecipeCategories(new BrightFermentationRecipeCategory(guiHelper));
        registration.addRecipeCategories(new BlenderRecipeCategory(guiHelper));
        registration.addRecipeCategories(new CuttingBoardRecipeCategory(guiHelper));
        registration.addRecipeCategories(new StockPotRecipeCategory(guiHelper));
        registration.addRecipeCategories(new SaucepanAndWhiskRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ShakerRecipeCategory(guiHelper));

        registration.addRecipeCategories(new FryerSmokingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new OvenSmokingRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(BlockRegistry.ICE_CREAM_MACHINE.get(), ICE_CREAM);
        registration.addRecipeCatalyst(BlockRegistry.FRYER.get(), FAST_FRY);
        registration.addRecipeCatalyst(BlockRegistry.FRYER.get(), SLOW_FRY);
        registration.addRecipeCatalyst(BlockRegistry.OVEN.get(), OVEN);
        registration.addRecipeCatalyst(BlockRegistry.TEAPOT.get(), TEAPOT);
        registration.addRecipeCatalyst(BlockRegistry.FERMENTER.get(), DIM_FERMENTATION);
        registration.addRecipeCatalyst(BlockRegistry.FERMENTER.get(), NORMAL_FERMENTATION);
        registration.addRecipeCatalyst(BlockRegistry.FERMENTER.get(), BRIGHT_FERMENTATION);
        registration.addRecipeCatalyst(BlockRegistry.BLENDER.get(), BLENDER);
        registration.addRecipeCatalyst(BlockRegistry.CUTTING_BOARD.get(), CUTTING_BOARD);
        registration.addRecipeCatalyst(BlockRegistry.STOCK_POT.get(), STOCK_POT);
        registration.addRecipeCatalyst(BlockRegistry.SAUCEPAN_AND_WHISK.get(), SAUCEPAN_AND_WHISK);
        registration.addRecipeCatalyst(ItemRegistry.SHAKER.get(), SHAKER);

        registration.addRecipeCatalyst(ItemRegistry.FRYER.get(), FRYER_SMOKING);
        registration.addRecipeCatalyst(ItemRegistry.OVEN.get(), OVEN_SMOKING);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        runtime = null;
        initialConfigApplied = false;
        registeredWithServerConfig = MachineClientConfigSyncEvents.isReady();
        registration.addRecipes(ICE_CREAM, this.getAllRecipesFor(RecipeRegistry.ICE_CREAM_RECIPE_TYPE.get()));
        registration.addRecipes(FAST_FRY, this.getAllRecipesFor(RecipeRegistry.FAST_FRY_RECIPE_TYPE.get()));
        registration.addRecipes(SLOW_FRY, this.getAllRecipesFor(RecipeRegistry.SLOW_FRY_RECIPE_TYPE.get()));
        registration.addRecipes(OVEN, this.getAllRecipesFor(RecipeRegistry.OVEN_RECIPE_TYPE.get()));
        registration.addRecipes(TEAPOT, this.getAllRecipesFor(RecipeRegistry.TEAPOT_RECIPE_TYPE.get()));
        registration.addRecipes(DIM_FERMENTATION, this.getAllRecipesFor(RecipeRegistry.DIM_FERMENTATION_RECIPE_TYPE.get()));
        registration.addRecipes(NORMAL_FERMENTATION, this.getAllRecipesFor(RecipeRegistry.NORMAL_FERMENTATION_RECIPE_TYPE.get()));
        registration.addRecipes(BRIGHT_FERMENTATION, this.getAllRecipesFor(RecipeRegistry.BRIGHT_FERMENTATION_RECIPE_TYPE.get()));
        registration.addRecipes(BLENDER, this.getAllRecipesFor(RecipeRegistry.BLENDER_RECIPE_TYPE.get()));
        List<CuttingBoardRecipeWrapper> cuttingBoardRecipes = Stream.of(
                        this.getAllRecipesFor(RecipeRegistry.CUTTING_BOARD_SINGLE_RECIPE_TYPE.get()),
                        this.getAllRecipesFor(RecipeRegistry.CUTTING_BOARD_MULTIPLE_RECIPE_TYPE.get()),
                        CuttingBoardRecipeCompat.recipes()
                ).flatMap(List::stream)
                .map(CuttingBoardRecipeWrapper::new)
                .collect(Collectors.toList());
        registration.addRecipes(CUTTING_BOARD, cuttingBoardRecipes);
        List<StockPotRecipe> stockPotRecipes = Stream.concat(
                this.getAllRecipesFor(RecipeRegistry.STOCK_POT_RECIPE_TYPE.get()).stream(),
                CookingPotRecipeCompat.recipes().stream()
        ).collect(Collectors.toList());
        registration.addRecipes(STOCK_POT, stockPotRecipes);
        registration.addRecipes(SAUCEPAN_AND_WHISK, this.getAllRecipesFor(RecipeRegistry.SAUCEPAN_AND_WHISK_RECIPE_TYPE.get()));
        registration.addRecipes(SHAKER, this.getAllRecipesFor(RecipeRegistry.SHAKER_RECIPE_TYPE.get()));

        registration.addRecipes(FRYER_SMOKING, SmokingRecipeManager.fastFryRecipes);
        registration.addRecipes(OVEN_SMOKING, SmokingRecipeManager.ovenRecipes);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        completeInitialConfigSync();
    }

    @Override
    public void onRuntimeUnavailable() {
        runtime = null;
        registeredWithServerConfig = false;
        initialConfigApplied = false;
    }

    public static void completeInitialConfigSync() {
        if (runtime == null || initialConfigApplied || !MachineClientConfigSyncEvents.isReady()) {
            return;
        }
        if (!registeredWithServerConfig) {
            
            var recipeManager = runtime.getRecipeManager();
            recipeManager.addRecipes(CUTTING_BOARD, CuttingBoardRecipeCompat.recipes().stream()
                .map(CuttingBoardRecipeWrapper::new).collect(Collectors.toList()));
            recipeManager.addRecipes(STOCK_POT, CookingPotRecipeCompat.recipes());
        }
        initialConfigApplied = true;
    }

    public RecipeManager getRecipeManager() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level != null) {
            return level.getRecipeManager();
        } else {
            throw new NullPointerException("minecraft level must not be null");
        }
    }

    public <T extends BaseRecipe<?>> List<T> sort(List<T> recipes) {
        List<T> newRecipes = new ArrayList<>(recipes);
        newRecipes.sort((a, b) -> {
            boolean x = RecipeOrderManager.ITEM_ORDER.containsKey(a.output.getItem());
            boolean y = RecipeOrderManager.ITEM_ORDER.containsKey(b.output.getItem());
            if (x && y) {
                return Integer.compare(RecipeOrderManager.ITEM_ORDER.get(a.output.getItem()), RecipeOrderManager.ITEM_ORDER.get(b.output.getItem()));
            } else if (x) {
                return -1;
            } else if (y) {
                return 1;
            } else {
                return 0;
            }
        });
        return newRecipes;
    }

    public <C extends Container, T extends BaseRecipe<C>> List<T> getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType<T> recipeType) {
        RecipeManager recipeManager = this.getRecipeManager();
        return this.sort(recipeManager.getAllRecipesFor(recipeType));
    }
}
