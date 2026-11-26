package net.bittercat.plerocottage.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.google.gson.JsonObject;
import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
import net.bittercat.plerocottage.content.items.DyeContent;
import net.bittercat.plerocottage.content.building.ColoredDecorationContent;
import net.bittercat.plerocottage.content.furniture.FurnitureContent;
import net.bittercat.plerocottage.content.food.FoodContent;
import net.bittercat.plerocottage.content.equipment.EquipmentContent;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.content.core.CoreContent;
import net.bittercat.plerocottage.content.items.OrdinaryItemContent;
import net.bittercat.plerocottage.content.blocks.MarbleContent;
import net.bittercat.plerocottage.content.food.PlateContent;
import net.bittercat.plerocottage.content.plants.BlackDirts;
import net.bittercat.plerocottage.content.plants.Bananas;
import net.bittercat.plerocottage.content.plants.Bushes;
import net.bittercat.plerocottage.content.plants.Crops;
import net.bittercat.plerocottage.content.plants.PurpleSugarCanes;
import net.bittercat.plerocottage.content.plants.DoubleCrops;
import net.bittercat.plerocottage.content.plants.Trees;
import net.bittercat.plerocottage.content.plants.Vines;
import net.bittercat.plerocottage.content.plants.Flowers;
import net.bittercat.plerocottage.content.plants.WildRice;
import net.minecraft.tags.ItemTags;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.bittercat.plerocottage.wood.WoodSet;
import net.minecraft.data.PackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public final class RecipeGenerator extends RecipeProvider {
    private final Path resourcesRoot;

    public RecipeGenerator(PackOutput output, Path resourcesRoot) {
        super(output);
        this.resourcesRoot = resourcesRoot;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        RecipeUnlockAdvancements unlocks = new RecipeUnlockAdvancements(resourcesRoot);
        List<GeneratedRecipe> recipes = new ArrayList<>();
        buildRecipes(recipe -> {
            JsonObject json = recipe.serializeRecipe();
            unlocks.add(recipe.getId(), json, recipe.serializeAdvancement());
            recipes.add(new GeneratedRecipe(recipe.getId(), json));
        });

        List<CompletableFuture<?>> writes = new ArrayList<>();
        for (GeneratedRecipe recipe : recipes) {
            writes.add(DataProvider.saveStable(output, recipe.json(), recipePathProvider.json(recipe.id())));
        }
        unlocks.entries().forEach((id, json) ->
            writes.add(DataProvider.saveStable(output, json, advancementPathProvider.json(id))));
        return CompletableFuture.allOf(writes.toArray(CompletableFuture<?>[]::new));
    }

    private record GeneratedRecipe(ResourceLocation id, JsonObject json) {}

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        for (WoodSet wood : WoodSetContent.all()) {
            woodRecipes(consumer, wood);
        }
        furnitureRecipes(consumer);
        for (Trees.PlainEntry tree : Trees.plainTrees()) {
            if (tree.processedKind() == Trees.Processed.COOKED_NUT) {
                cookingRecipes(consumer, tree.harvest().get(), tree.processed().get(),
                    "cooked_" + tree.name(), 0.35F, "has_nut");
            }
        }
        cookingRecipes(consumer, Vines.GRAPE.get(), FoodContent.item("raisins").get(),
            "raisins", 0.35F, "has_grapes");
        cookingRecipes(consumer, Items.SUNFLOWER, FoodContent.item("cooked_sunflower_seeds").get(),
            "cooked_sunflower_seeds", 0.35F, "has_sunflower");
        cookingRecipes(consumer, Trees.CINNISE_TREE.leavesItem().get(),
            OrdinaryItemContent.BAY_LEAVES.get(), "bay_leaves");
        strawberrySeedRecipes(consumer);
        tomatoSeedRecipes(consumer);
        peanutRecipes(consumer);
        mintRecipes(consumer);
        purpleSugarCaneRecipes(consumer);
        grapeTwinPaleteriaRecipe(consumer);
        vineRecipes(consumer);
        flowerRecipes(consumer);
        wildRiceRecipes(consumer);
        blackDirtRecipe(consumer);
        dyeAndBuildingRecipes(consumer);
        wallpaperRecipes(consumer);
        marbleRecipes(consumer);
        plateRecipes(consumer);
        stageFiveRecipes(consumer);
        knifeRecipes(consumer);
        rollingPinRecipes(consumer);
        forkRecipes(consumer);
        chopsticksRecipes(consumer);
    }

    private void grapeTwinPaleteriaRecipe(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FoodContent.item("grape_twin_paleta").get())
            .pattern(" ab")
            .pattern("cbd")
            .pattern(" c ")
            .define('a', Vines.MUSCAT_GRAPE.get())
            .define('b', FoodContent.item("ice_cubes").get())
            .define('c', Items.STICK)
            .define('d', Vines.GRAPE.get())
            .unlockedBy("has_grape", has(Vines.GRAPE.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "grape_twin_paleta_recipe"));
    }

    private void marbleRecipes(Consumer<FinishedRecipe> consumer) {
        Item marble = MarbleContent.MARBLE.get().asItem();
        Item slab = MarbleContent.MARBLE_SLAB.get().asItem();
        Item stairs = MarbleContent.MARBLE_STAIRS.get().asItem();
        Item wall = MarbleContent.MARBLE_WALL.get().asItem();
        Item polished = MarbleContent.POLISHED_MARBLE.get().asItem();
        Item polishedSlab = MarbleContent.POLISHED_MARBLE_SLAB.get().asItem();
        Item polishedStairs = MarbleContent.POLISHED_MARBLE_STAIRS.get().asItem();
        Item polishedWall = MarbleContent.POLISHED_MARBLE_WALL.get().asItem();
        Item brick = MarbleContent.POLISHED_MARBLE_BRICK.get().asItem();
        Item brickSlab = MarbleContent.POLISHED_MARBLE_BRICK_SLAB.get().asItem();
        Item brickStairs = MarbleContent.POLISHED_MARBLE_BRICK_STAIRS.get().asItem();
        Item brickWall = MarbleContent.POLISHED_MARBLE_BRICK_WALL.get().asItem();

        marbleShaped(consumer, "marble_slab_recipe", slab, 6, marble, "aaa");
        marbleShaped(consumer, "marble_stairs_recipe", stairs, 4, marble, "a  ", "aa ", "aaa");
        marbleShaped(consumer, "marble_wall_recipe", wall, 6, marble, "aaa", "aaa");
        marbleStonecut(consumer, "marble_slab_recipe_2", marble, slab, 2);
        marbleStonecut(consumer, "marble_stairs_recipe_2", marble, stairs, 1);
        marbleStonecut(consumer, "marble_wall_recipe_2", marble, wall, 1);
        marbleStonecut(consumer, "polished_marble_recipe", marble, polished, 1);

        marbleShaped(consumer, "polished_marble_slab_recipe", polishedSlab, 6, polished, "aaa");
        marbleShaped(consumer, "polished_marble_stairs_recipe", polishedStairs, 4, polished,
            "a  ", "aa ", "aaa");
        marbleShaped(consumer, "polished_marble_wall_recipe", polishedWall, 6, polished, "aaa", "aaa");
        marbleStonecut(consumer, "polished_marble_slab_recipe_2", polished, polishedSlab, 2);
        marbleStonecut(consumer, "polished_marble_stairs_recipe_2", polished, polishedStairs, 1);
        marbleStonecut(consumer, "polished_marble_wall_recipe_2", polished, polishedWall, 1);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, brick, 4)
            .requires(polished).requires(polished).requires(polished).requires(polished)
            .unlockedBy("has_polished_marble", has(polished))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "polished_marble_brick_recipe_2"));
        marbleStonecut(consumer, "polished_marble_brick_recipe", marble, brick, 1);
        marbleShaped(consumer, "polished_marble_brick_slab_recipe", brickSlab, 6, brick, "aaa");
        marbleShaped(consumer, "polished_marble_brick_stairs_recipe", brickStairs, 4, brick,
            "a  ", "aa ", "aaa");
        marbleShaped(consumer, "polished_marble_brick_wall_recipe", brickWall, 6, brick, "aaa", "aaa");
        marbleStonecut(consumer, "polished_marble_brick_slab_recipe_2", brick, brickSlab, 2);
        marbleStonecut(consumer, "polished_marble_brick_stairs_recipe_2", brick, brickStairs, 1);
        marbleStonecut(consumer, "polished_marble_brick_wall_recipe_2", brick, brickWall, 1);

        shaped(consumer, "bowl_recipe", RecipeCategory.MISC, MarbleContent.BOWL.get().asItem(),
            4, new String[] {"a a", "aaa"}, 'a', polished);
    }

    private void plateRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PlateContent.PLATE_ITEM.get(), 4)
            .pattern("aaa").pattern(" b ")
            .define('a', Items.SMOOTH_QUARTZ_SLAB)
            .define('b', Items.QUARTZ_BLOCK)
            .unlockedBy("has_smooth_quartz_slab", has(Items.SMOOTH_QUARTZ_SLAB))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "plate_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PlateContent.PLATE_ITEM.get(), 4)
            .pattern("aaa").pattern(" b ")
            .define('a', ItemTags.WOODEN_SLABS)
            .define('b', ItemTagProvider.STRIPPED_WOOD)
            .unlockedBy("has_wooden_slab", has(ItemTags.WOODEN_SLABS))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "plate_from_wood_recipe"));
    }

    private void marbleShaped(Consumer<FinishedRecipe> consumer, String id, Item result, int count,
                              Item ingredient, String... pattern) {
        shaped(consumer, id, RecipeCategory.BUILDING_BLOCKS, result, count, pattern, 'a', ingredient);
    }

    private void marbleStonecut(Consumer<FinishedRecipe> consumer, String id, Item input, Item result, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, result, count)
            .unlockedBy("has_material", has(input))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, id));
    }

    private void dyeAndBuildingRecipes(Consumer<FinishedRecipe> consumer) {
        shapeless(consumer, "snow_dye_recipe", RecipeCategory.MISC, DyeContent.dye("snow").get(), 4,
            Items.WHITE_DYE, Items.WHITE_DYE, Items.LIGHT_BLUE_DYE, Items.LIGHT_BLUE_DYE);
        shapeless(consumer, "lavender_dye_recipe", RecipeCategory.MISC, DyeContent.dye("lavender").get(), 1,
            Flowers.item("lavender").get());
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(net.bittercat.plerocottage.content.plants.DragonFruits.CACTUS_ITEM.get()),
                RecipeCategory.MISC, DyeContent.dye("turquoise").get(), 1.0F, 200)
            .unlockedBy("has_dragon_fruit_cactus", has(net.bittercat.plerocottage.content.plants.DragonFruits.CACTUS_ITEM.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "turquoise_dye_recipe"));

        for (String color : List.of("snow", "lavender", "turquoise")) {
            Item dye = DyeContent.dye(color).get();
            Item terracotta = item(color + "_terracotta");
            Item glazed = item(color + "_glazed_terracotta");
            Item powder = item(color + "_concrete_powder");
            Item glass = item(color + "_glass");
            Item pane = item(color + (color.equals("turquoise") ? "_glasspane" : "_glass_pane"));
            Item wool = item(color + "_wool");
            Item carpet = item(color + "_carpet");

            shaped(consumer, color + "_terracotta_recipe", terracotta, 8,
                new String[] {"aaa", "aba", "aaa"}, 'a', Items.TERRACOTTA, 'b', dye);
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(terracotta), RecipeCategory.BUILDING_BLOCKS,
                    glazed, 0.1F, 200)
                .unlockedBy("has_" + color + "_terracotta", has(terracotta))
                .save(consumer, new ResourceLocation(PleroCottage.MODID, color + "_glazed_terracotta_recipe"));
            shapeless(consumer, color + "_concrete_powder_recipe", RecipeCategory.BUILDING_BLOCKS, powder, 8,
                dye, Items.SAND, Items.SAND, Items.SAND, Items.SAND,
                Items.GRAVEL, Items.GRAVEL, Items.GRAVEL, Items.GRAVEL);
            shaped(consumer, color + "_glass_recipe", glass, 8,
                new String[] {"aaa", "aba", "aaa"}, 'a', Items.GLASS, 'b', dye);
            shaped(consumer, color + "_glass_pane_recipe",
                pane, 8, new String[] {"aaa", "aba", "aaa"}, 'a', Items.GLASS_PANE, 'b', dye);
            shapeless(consumer, color + "_glass_pane_recipe_2", RecipeCategory.BUILDING_BLOCKS, pane, 16,
                glass, glass, glass, glass, glass, glass);
            shapeless(consumer, color + "_wool_recipe", RecipeCategory.BUILDING_BLOCKS, wool, 1,
                dye, ItemTags.WOOL);
            shaped(consumer, color + "_carpet_recipe", RecipeCategory.MISC, carpet, 3,
                new String[] {"aa"}, 'a', wool);
            shapeless(consumer, color + "_carpet_from_carpet_recipe", RecipeCategory.BUILDING_BLOCKS, carpet, 1,
                dye, ItemTags.WOOL_CARPETS);
            shapeless(consumer, color + "_bed_recipe", RecipeCategory.DECORATIONS,
                ColoredDecorationContent.bed(color).get().asItem(), 1, Items.WHITE_BED, dye);
            shapeless(consumer, color + "_candle_recipe", RecipeCategory.DECORATIONS,
                ColoredDecorationContent.candle(color).get().asItem(), 1, Items.CANDLE, dye);
            shapeless(consumer, color + "_shulker_box_recipe", RecipeCategory.DECORATIONS,
                ColoredDecorationContent.shulkerBox(color).get().asItem(), 1, Items.SHULKER_BOX, dye);
        }
    }

    private void wallpaperRecipes(Consumer<FinishedRecipe> consumer) {
        shaped(consumer, "wallpaper_block_recipe", item("wallpaper_block"), 1,
            new String[] {"a a", "aba", "a a"}, 'a', Items.PAPER, 'b', Items.BRICKS);
        shaped(consumer, "panelled_wallpaper_block_recipe", item("panelled_wallpaper_block"), 1,
            new String[] {"a a", "aba", "aca"}, 'a', Items.PAPER, 'b', Items.BRICKS, 'c', ItemTags.SLABS);
        for (String color : List.of("white", "light_gray", "gray", "black", "brown", "red", "orange", "yellow",
                "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink",
                "snow", "lavender", "turquoise")) {
            Item dye = color.equals("snow") || color.equals("lavender") || color.equals("turquoise")
                ? DyeContent.dye(color).get() : vanillaDye(color);
            shaped(consumer, color + "_wallpaper_block_recipe", item(color + "_wallpaper_block"), 8,
                new String[] {"aaa", "aba", "aaa"}, 'a', ItemTagProvider.WALLPAPER_BLOCK, 'b', dye);
            shaped(consumer, color + "_panelled_wallpaper_block_recipe",
                item(color + "_panelled_wallpaper_block"), 8,
                new String[] {"aaa", "aba", "aaa"}, 'a', ItemTagProvider.PANELLED_WALLPAPER_BLOCK, 'b', dye);
        }
    }

    private Item vanillaDye(String color) {
        return switch (color) {
            case "white" -> Items.WHITE_DYE;
            case "light_gray" -> Items.LIGHT_GRAY_DYE;
            case "gray" -> Items.GRAY_DYE;
            case "black" -> Items.BLACK_DYE;
            case "brown" -> Items.BROWN_DYE;
            case "red" -> Items.RED_DYE;
            case "orange" -> Items.ORANGE_DYE;
            case "yellow" -> Items.YELLOW_DYE;
            case "lime" -> Items.LIME_DYE;
            case "green" -> Items.GREEN_DYE;
            case "cyan" -> Items.CYAN_DYE;
            case "light_blue" -> Items.LIGHT_BLUE_DYE;
            case "blue" -> Items.BLUE_DYE;
            case "purple" -> Items.PURPLE_DYE;
            case "magenta" -> Items.MAGENTA_DYE;
            case "pink" -> Items.PINK_DYE;
            default -> throw new IllegalArgumentException("未知原版染料：" + color);
        };
    }

    private Item item(String id) {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
            new ResourceLocation(PleroCottage.MODID, id));
    }

    private void shaped(Consumer<FinishedRecipe> consumer, String id, Item result, int count, String[] pattern,
                        Object... keys) {
        shaped(consumer, id, RecipeCategory.BUILDING_BLOCKS, result, count, pattern, keys);
    }

    private void shaped(Consumer<FinishedRecipe> consumer, String id, RecipeCategory category, Item result,
                        int count, String[] pattern, Object... keys) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(category, result, count);
        for (String row : pattern) builder.pattern(row);
        for (int i = 0; i < keys.length; i += 2) {
            if (keys[i + 1] instanceof Item item) builder.define((Character) keys[i], item);
            else builder.define((Character) keys[i], (net.minecraft.tags.TagKey<Item>) keys[i + 1]);
        }
        if (keys[1] instanceof Item item) {
            builder.unlockedBy("has_material", has(item));
        } else {
            builder.unlockedBy("has_material", has((net.minecraft.tags.TagKey<Item>) keys[1]));
        }
        builder.save(consumer, new ResourceLocation(PleroCottage.MODID, id));
    }

    private void shapeless(Consumer<FinishedRecipe> consumer, String id, RecipeCategory category, Item result,
                           int count, Object... ingredients) {
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(category, result, count);
        for (Object ingredient : ingredients) {
            if (ingredient instanceof Item item) builder.requires(item);
            else builder.requires((net.minecraft.tags.TagKey<Item>) ingredient);
        }
        if (ingredients[0] instanceof Item item) {
            builder.unlockedBy("has_material", has(item));
        } else {
            builder.unlockedBy("has_material", has((net.minecraft.tags.TagKey<Item>) ingredients[0]));
        }
        builder.save(consumer, new ResourceLocation(PleroCottage.MODID, id));
    }

    private void stageFiveRecipes(Consumer<FinishedRecipe> consumer) {
        shaped(consumer, "chef_hat_recipe", EquipmentContent.CHEF_HAT.get(),
            new String[] {"aaa", "b b", "c c"}, 'a', Items.WHITE_WOOL, 'b', Items.WHITE_BANNER, 'c', Items.WHITE_DYE);
        shaped(consumer, "pastry_chef_hat_recipe", EquipmentContent.PASTRY_CHEF_HAT.get(),
            new String[] {" a ", "bcd", " e "}, 'a', net.bittercat.plerocottage.content.food.FoodContent.item("cherries").get(),
            'b', Items.PINK_BANNER, 'c', Items.WHITE_BANNER, 'd',
            net.bittercat.plerocottage.content.food.FoodContent.item("checkerboard_cookie").get(), 'e',
            net.bittercat.plerocottage.content.food.FoodContent.item("chocolate").get());
    }

    private void knifeRecipes(Consumer<FinishedRecipe> consumer) {
        knifeRecipe(consumer, "wooden_knife", EquipmentContent.WOODEN_KNIFE.get(), Items.OAK_PLANKS, Items.STICK);
        knifeRecipe(consumer, "stone_knife", EquipmentContent.STONE_KNIFE.get(), Items.COBBLESTONE, Items.FLINT);
        knifeRecipe(consumer, "iron_knife", EquipmentContent.IRON_KNIFE.get(), Items.IRON_INGOT, Items.FLINT);
        knifeRecipe(consumer, "golden_knife", EquipmentContent.GOLDEN_KNIFE.get(), Items.GOLD_INGOT, Items.FLINT);
        knifeRecipe(consumer, "diamond_knife", EquipmentContent.DIAMOND_KNIFE.get(), Items.DIAMOND, Items.GOLD_INGOT);
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(EquipmentContent.DIAMOND_KNIFE.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                EquipmentContent.NETHERITE_KNIFE.get())
            .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "netherite_knife_smithing"));

        sickleRecipe(consumer, "wooden_sickle", EquipmentContent.WOODEN_SICKLE.get(),
            Items.OAK_PLANKS, Items.STICK);
        sickleRecipe(consumer, "stone_sickle", EquipmentContent.STONE_SICKLE.get(),
            Items.COBBLESTONE, Items.FLINT);
        sickleRecipe(consumer, "iron_sickle", EquipmentContent.IRON_SICKLE.get(),
            Items.IRON_INGOT, Items.FLINT);
        sickleRecipe(consumer, "golden_sickle", EquipmentContent.GOLDEN_SICKLE.get(),
            Items.GOLD_INGOT, Items.FLINT);
        sickleRecipe(consumer, "diamond_sickle", EquipmentContent.DIAMOND_SICKLE.get(),
            Items.DIAMOND, Items.GOLD_INGOT);
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(EquipmentContent.DIAMOND_SICKLE.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                EquipmentContent.NETHERITE_SICKLE.get())
            .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "netherite_sickle_smithing"));
    }

    private void rollingPinRecipes(Consumer<FinishedRecipe> consumer) {
        rollingPinRecipe(consumer, "wooden_rolling_pin", EquipmentContent.WOODEN_ROLLING_PIN.get(),
            Ingredient.of(ItemTags.PLANKS), Items.STICK);
        rollingPinRecipe(consumer, "stone_rolling_pin", EquipmentContent.STONE_ROLLING_PIN.get(),
            Ingredient.of(Items.COBBLESTONE), Items.FLINT);
        rollingPinRecipe(consumer, "iron_rolling_pin", EquipmentContent.IRON_ROLLING_PIN.get(),
            Ingredient.of(Items.IRON_INGOT), Items.FLINT);
        rollingPinRecipe(consumer, "golden_rolling_pin", EquipmentContent.GOLDEN_ROLLING_PIN.get(),
            Ingredient.of(Items.GOLD_INGOT), Items.FLINT);
        rollingPinRecipe(consumer, "diamond_rolling_pin", EquipmentContent.DIAMOND_ROLLING_PIN.get(),
            Ingredient.of(Items.DIAMOND), Items.GOLD_INGOT);
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(EquipmentContent.DIAMOND_ROLLING_PIN.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                EquipmentContent.NETHERITE_ROLLING_PIN.get())
            .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "netherite_rolling_pin_smithing"));
    }

    private void rollingPinRecipe(Consumer<FinishedRecipe> consumer, String id, Item result,
                                  Ingredient material, Item handle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result)
            .pattern("aba")
            .define('a', handle).define('b', material)
            .unlockedBy("has_handle", has(handle))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, id + "_recipe"));
    }

    private void forkRecipes(Consumer<FinishedRecipe> consumer) {
        forkStonecut(consumer, "wooden_fork", Ingredient.of(ItemTags.PLANKS), has(ItemTags.PLANKS),
            EquipmentContent.WOODEN_FORK.get());
        forkStonecut(consumer, "stone_fork", Items.COBBLESTONE, EquipmentContent.STONE_FORK.get());
        forkStonecut(consumer, "iron_fork", Items.IRON_INGOT, EquipmentContent.IRON_FORK.get());
        forkStonecut(consumer, "golden_fork", Items.GOLD_INGOT, EquipmentContent.GOLDEN_FORK.get());
        forkStonecut(consumer, "diamond_fork", Items.DIAMOND, EquipmentContent.DIAMOND_FORK.get());
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(EquipmentContent.DIAMOND_FORK.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                EquipmentContent.NETHERITE_FORK.get())
            .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "netherite_fork_smithing"));
    }

    private void forkStonecut(Consumer<FinishedRecipe> consumer, String id, Item input, Item result) {
        forkStonecut(consumer, id, Ingredient.of(input), has(input), result);
    }

    private void forkStonecut(Consumer<FinishedRecipe> consumer, String id, Ingredient input,
                             CriterionTriggerInstance unlock, Item result) {
        SingleItemRecipeBuilder.stonecutting(input, RecipeCategory.TOOLS, result, 1)
            .unlockedBy("has_material", unlock)
            .save(consumer, new ResourceLocation(PleroCottage.MODID, id + "_stonecutting"));
    }

    private void chopsticksRecipes(Consumer<FinishedRecipe> consumer) {
        forkStonecut(consumer, "wooden_chopsticks", Ingredient.of(ItemTags.PLANKS), has(ItemTags.PLANKS),
            EquipmentContent.WOODEN_CHOPSTICKS.get());
        forkStonecut(consumer, "stone_chopsticks", Items.COBBLESTONE, EquipmentContent.STONE_CHOPSTICKS.get());
        forkStonecut(consumer, "iron_chopsticks", Items.IRON_INGOT, EquipmentContent.IRON_CHOPSTICKS.get());
        forkStonecut(consumer, "golden_chopsticks", Items.GOLD_INGOT, EquipmentContent.GOLDEN_CHOPSTICKS.get());
        forkStonecut(consumer, "diamond_chopsticks", Items.DIAMOND, EquipmentContent.DIAMOND_CHOPSTICKS.get());
        forkStonecut(consumer, "bamboo_chopsticks", Items.BAMBOO_PLANKS, EquipmentContent.BAMBOO_CHOPSTICKS.get());
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(EquipmentContent.DIAMOND_CHOPSTICKS.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                EquipmentContent.NETHERITE_CHOPSTICKS.get())
            .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "netherite_chopsticks_smithing"));
    }

    private void knifeRecipe(Consumer<FinishedRecipe> consumer, String id, Item result, Item material, Item handle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result)
            .pattern("  b").pattern(" b ").pattern("a  ")
            .define('a', handle).define('b', material)
            .unlockedBy("has_material", has(material))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, id + "_recipe"));
    }

    private void sickleRecipe(Consumer<FinishedRecipe> consumer, String id, Item result,
                              Item material, Item handle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result)
            .pattern("bbb").pattern(" ab").pattern(" a ")
            .define('a', handle).define('b', material)
            .unlockedBy("has_material", has(material))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, id + "_recipe"));
    }

    private void shaped(Consumer<FinishedRecipe> consumer, String id, Item result, String[] pattern,
                        Object... keys) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result);
        for (String row : pattern) builder.pattern(row);
        for (int i = 0; i < keys.length; i += 2) {
            if (keys[i + 1] instanceof Item item) {
                builder.define((Character) keys[i], item);
            } else {
                builder.define((Character) keys[i], (net.minecraft.tags.TagKey<Item>) keys[i + 1]);
            }
        }
        builder.unlockedBy("has_material", keys[1] instanceof Item item ? has(item) : has(ItemTags.PLANKS))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, id));
    }

    private void furnitureRecipes(Consumer<FinishedRecipe> consumer) {
        for (var furniture : FurnitureContent.all()) {
            Item planks = furnitureIngredient(furniture.name(), false);
            Item trapdoor = furnitureIngredient(furniture.name(), true);
            String name = furniture.name();
            shapedFurniture(consumer, name + "_cabinet", furniture.cabinetItem().get(), planks, trapdoor,
                "aaa", "aba", "aaa");
            shapedFurniture(consumer, name + "_drawer", furniture.drawerItem().get(), planks, trapdoor,
                "aaa", "aba", "aba");
        }
    }

    private Item furnitureIngredient(String name, boolean trapdoor) {
        if (List.of("oak", "spruce", "birch", "jungle", "acacia", "dark_oak",
                    "mangrove", "cherry", "bamboo", "crimson", "warped").contains(name)) {
            String suffix = trapdoor ? "_trapdoor" : "_planks";
            return net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                new ResourceLocation("minecraft", name + suffix));
        }
        WoodSet wood = WoodSetContent.byName(name).orElseThrow(() ->
            new IllegalStateException("家具配方找不到木材套装：" + name));
        return (trapdoor ? wood.trapdoor() : wood.planks()).get().asItem();
    }

    private void shapedFurniture(Consumer<FinishedRecipe> consumer, String recipeName, Item result,
                                Item planks, Item trapdoor, String... pattern) {
        var builder = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
            .pattern(pattern[0]).pattern(pattern[1]).pattern(pattern[2])
            .define('a', planks)
            .define('b', trapdoor)
            .unlockedBy("has_planks", has(planks));
        builder.save(consumer, new ResourceLocation(PleroCottage.MODID, recipeName + "_recipe"));
    }

    private void flowerRecipes(Consumer<FinishedRecipe> consumer) {
        dyeRecipe(consumer, "blue_tulip", Items.BLUE_DYE);
        dyeRecipe(consumer, "hyacinth", Items.PURPLE_DYE);
        dyeRecipe(consumer, "jasmine", Items.WHITE_DYE);
        dyeRecipe(consumer, "orange_blossom", Items.YELLOW_DYE);
        dyeRecipe(consumer, "rose", Items.RED_DYE);
        dyeRecipe(consumer, "saffron", Items.MAGENTA_DYE);
        dyeRecipe(consumer, "sakura", Items.PINK_DYE);
        dyeRecipe(consumer, "semper_augustus", Items.PINK_DYE);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DoubleCrops.COFFEE_BERRY.get(), 2)
            .requires(Flowers.item("coffea_arabica").get())
            .unlockedBy("has_coffea_arabica", has(Flowers.item("coffea_arabica").get()))
            .save(consumer,
                new ResourceLocation(PleroCottage.MODID, "coffee_berry_from_coffea_arabica_recipe"));

        Item orangeLeaves = Trees.ORANGE_TREE.leavesItem().get();
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Flowers.item("orange_blossom").get(), 2)
            .requires(orangeLeaves)
            .requires(Items.BONE_MEAL)
            .unlockedBy("has_orange_leaves", has(orangeLeaves))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "orange_blossom_from_orange_leaves"));
    }

    private void dyeRecipe(Consumer<FinishedRecipe> consumer, String name, Item dye) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, dye, 1)
            .requires(Flowers.item(name).get())
            .unlockedBy("has_" + name, has(Flowers.item(name).get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, name + "_dye_recipe"));
    }

    private void wildRiceRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DoubleCrops.RICE_PANICLE.get())
            .requires(WildRice.ITEM.get())
            .unlockedBy("has_wild_rice", has(WildRice.ITEM.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "rice_panicle_from_wild_rice"));
    }

    private void blackDirtRecipe(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BlackDirts.BLACK_DIRT.get(), 2)
            .requires(Items.DIRT)
            .requires(FoodContent.item("suspicious_mold").get())
            .requires(CoreContent.CINDER.get())
            .requires(Items.DIRT)
            .unlockedBy("has_cinder", has(CoreContent.CINDER.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "black_dirt_recipe"));
    }

    private void vineRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Vines.VINE_STAKE_ITEM.get(), 6)
            .pattern("aba")
            .pattern("aba")
            .pattern("aba")
            .define('a', Items.STICK)
            .define('b', ItemTagProvider.STRIPPED_WOOD)
            .unlockedBy("has_stripped_wood", has(ItemTagProvider.STRIPPED_WOOD))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "vine_stake_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Vines.VINE_ARBOR_ITEM.get(), 8)
            .pattern("aba")
            .pattern("bab")
            .pattern("aba")
            .define('a', Items.STICK)
            .define('b', ItemTagProvider.STRIPPED_WOOD)
            .unlockedBy("has_stripped_wood", has(ItemTagProvider.STRIPPED_WOOD))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "vine_arbor_recipe"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Vines.GRAPE_SEED.get(), 4)
            .requires(Ingredient.of(Vines.GRAPE.get(), Vines.MUSCAT_GRAPE.get()))
            .unlockedBy("has_grape", has(Vines.GRAPE.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "grape_seed_recipe"));
    }

    private void peanutRecipes(Consumer<FinishedRecipe> consumer) {
        cookingRecipes(consumer, Crops.PEANUT.get(), Crops.COOKED_PEANUT.get(), "cooked_peanuts");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Crops.PEANUT.get(), 3)
            .requires(Crops.WILD_PEANUT_ITEM.get())
            .unlockedBy("has_wild_peanut", has(Crops.WILD_PEANUT_ITEM.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "peanut_from_wild_peanut"));
    }

    private void mintRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Crops.MINT.get(), 2)
            .requires(Crops.WILD_MINT_ITEM.get())
            .unlockedBy("has_wild_mint", has(Crops.WILD_MINT_ITEM.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "mint_from_wild_mint"));
    }

    private void purpleSugarCaneRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, OrdinaryItemContent.BROWN_SUGAR.get())
            .requires(PurpleSugarCanes.ITEM.get())
            .unlockedBy("has_purple_sugar_cane", has(PurpleSugarCanes.ITEM.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "brown_sugar_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.PAPER, 4)
            .pattern("aaa")
            .define('a', PurpleSugarCanes.ITEM.get())
            .unlockedBy("has_purple_sugar_cane", has(PurpleSugarCanes.ITEM.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "paper_from_purple_sugar_cane_recipe"));
    }

    private void strawberrySeedRecipes(Consumer<FinishedRecipe> consumer) {
        Item seed = Bushes.STRAWBERRY_BUSH.plantingItem().get();
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, seed, 1)
            .requires(Ingredient.of(Bushes.RASPBERRY_BUSH.fruitItem(), Bushes.BLACKBERRY.get()))
            .unlockedBy("has_berry", has(Bushes.BLACKBERRY.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "strawberry_seed_from_berry"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, seed, 2)
            .requires(Ingredient.of(Bushes.STRAWBERRY.get(), Bushes.PINEBERRY.get()))
            .unlockedBy("has_strawberry", has(Bushes.STRAWBERRY.get()))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "strawberry_seed_from_strawberry"));
    }

    private void tomatoSeedRecipes(Consumer<FinishedRecipe> consumer) {
        Item cherryTomatoes = Bushes.CHERRY_TOMATOES_BUSH.fruitItem();
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DoubleCrops.TOMATO_SEED.get(), 1)
            .requires(cherryTomatoes)
            .unlockedBy("has_cherry_tomatoes", has(cherryTomatoes))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, "tomato_seed_recipe"));
    }

    private void cookingRecipes(Consumer<FinishedRecipe> consumer, Item raw, Item cooked, String name) {
        cookingRecipes(consumer, raw, cooked, name, 0.35F, "has_nut");
    }

    private void cookingRecipes(Consumer<FinishedRecipe> consumer, Item raw, Item cooked, String name,
                                float experience, String unlockName) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(raw), RecipeCategory.FOOD, cooked, experience, 200)
            .unlockedBy(unlockName, has(raw))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, name));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(raw), RecipeCategory.FOOD, cooked, experience, 100)
            .unlockedBy(unlockName, has(raw))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, name + "_from_smoking"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(raw), RecipeCategory.FOOD, cooked, experience, 600)
            .unlockedBy(unlockName, has(raw))
            .save(consumer, new ResourceLocation(PleroCottage.MODID, name + "_from_campfire_cooking"));
    }

    private void woodRecipes(Consumer<FinishedRecipe> consumer, WoodSet wood) {
        String group = wood.name();
        Item log = wood.log().get().asItem();
        Item planks = wood.planks().get().asItem();
        Item stairs = wood.stairs().get().asItem();
        Item slab = wood.slab().get().asItem();
        Item fence = wood.fence().get().asItem();
        Item fenceGate = wood.fenceGate().get().asItem();
        Item door = wood.door().get().asItem();
        Item trapdoor = wood.trapdoor().get().asItem();
        Item pressurePlate = wood.pressurePlate().get().asItem();
        Item button = wood.button().get().asItem();
        Item sign = wood.signItem().get();
        Item hangingSign = wood.hangingSignItem().get();
        Item boat = wood.vehicleItem().get();
        Item chestBoat = wood.chestVehicleItem().get();
        Item strippedLog = wood.strippedLog().get().asItem();

        Ingredient pillar = Ingredient.of(
            ItemTags.create(new ResourceLocation(PleroCottage.MODID, group + "_logs")));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
            .requires(pillar)
            .group(group)
            .unlockedBy("has_log", has(log))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
            .pattern("#  ")
            .pattern("## ")
            .pattern("###")
            .define('#', planks)
            .group(group)
            .unlockedBy("has_planks", has(planks))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
            .pattern("###")
            .define('#', planks)
            .group(group)
            .unlockedBy("has_planks", has(planks))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, fence, 3)
            .pattern("#/#")
            .pattern("#/#")
            .define('#', planks)
            .define('/', Items.STICK)
            .group(group)
            .unlockedBy("has_planks", has(planks))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, fenceGate, 1)
            .pattern("/#/")
            .pattern("/#/")
            .define('#', planks)
            .define('/', Items.STICK)
            .group(group)
            .unlockedBy("has_planks", has(planks))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, door, 3)
            .pattern("##")
            .pattern("##")
            .pattern("##")
            .define('#', planks)
            .group(group)
            .unlockedBy("has_planks", has(planks))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, trapdoor, 2)
            .pattern("###")
            .pattern("###")
            .define('#', planks)
            .group(group)
            .unlockedBy("has_planks", has(planks))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pressurePlate, 1)
            .pattern("##")
            .define('#', planks)
            .group(group)
            .unlockedBy("has_planks", has(planks))
            .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button, 1)
            .requires(planks)
            .group(group)
            .unlockedBy("has_planks", has(planks))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, sign, 3)
            .pattern("###")
            .pattern("###")
            .pattern(" / ")
            .define('#', planks)
            .define('/', Items.STICK)
            .group(group)
            .unlockedBy("has_planks", has(planks))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, hangingSign, 6)
            .pattern(wood.stalkStyle() ? "- -" : "#-#")
            .pattern("###")
            .pattern("###")
            .define('#', strippedLog)
            .define('-', Items.CHAIN)
            .group(group)
            .unlockedBy("has_stripped_log", has(strippedLog))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, boat, 1)
            .pattern("# #")
            .pattern("###")
            .define('#', planks)
            .group(group)
            .unlockedBy("has_planks", has(planks))
            .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, chestBoat, 1)
            .requires(boat)
            .requires(Items.CHEST)
            .group(group)
            .unlockedBy("has_boat", has(boat))
            .save(consumer);
    }

}
