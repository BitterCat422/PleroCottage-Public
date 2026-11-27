package net.bittercat.plerocottage.data;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.content.fluids.FluidContent;
import net.bittercat.plerocottage.items.BlockFoodItem;
import net.bittercat.plerocottage.items.ColoredBedItem;
import net.bittercat.plerocottage.items.ColoredShulkerBoxItem;
import net.bittercat.plerocottage.items.DigestiveTabletsItem;
import net.bittercat.plerocottage.content.building.ColoredDecorationContent;
import net.bittercat.plerocottage.content.plants.PlantCatalog;
import net.bittercat.plerocottage.content.food.DisplayFoodContent;
import net.bittercat.plerocottage.content.food.DisplayModelResources;
import net.bittercat.plerocottage.content.food.GlassFoodModels;
import net.bittercat.plerocottage.items.GeckoDisplayFoodItem;
import net.bittercat.plerocottage.items.PoultryEggItem;
import net.bittercat.plerocottage.items.SeasoningItem;
import net.minecraft.data.PackOutput;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public final class ModelProvider extends ItemModelProvider {
    private static final Set<String> STAGED_SEASONINGS = Set.of(
        "caviar", "chamoy", "grenadine", "jalapeno_powder", "mixed_spice",
        "orange_blossom_water", "rice_vinegar", "rosa_water", "salt_shaker");

    private static final Map<String, String> FOOD_TEXTURE_OVERRIDES = Map.ofEntries(
        Map.entry("blueberry_cherries_ice_cream", "blueberry_and_cherries_ice_cream"),
        Map.entry("chef_hat_helmet", "chef_hat"),
        Map.entry("christmas_hat_helmet", "christmas_hat"),
        Map.entry("creativity_mechanism", "creativity_mechanism_beat"),
        Map.entry("durian_mile_crepe_cake", "durian_mile_crepe_cake"),
        Map.entry("durian_mile_crepe_cake_slice", "durian_mile_crepe_cake_slice"),
        Map.entry("enchanted_golden_apple_slice", "golden_apple_slice"),
        Map.entry("mugwort", "mugwort"),
        Map.entry("fried_tempura", "fried_tenpura"),
        Map.entry("graham_cracker", "graham_cracker"),
        Map.entry("caramel_chocolate_cake", "caramel_chocolate_cake"),
        Map.entry("chorus_flower_jelly_cake", "chorus_flower_jelly_cake"),
        Map.entry("chamomile_tea", "chamomile_tea"),
        Map.entry("nether_wart_soul_cake", "nether_wart_soul_cake"),
        Map.entry("pavlova_cake", "pavlova_cake"),
        Map.entry("strawberry_shortcake", "strawberry_shortcake"),
        Map.entry("sweet_berry_cake", "sweet_berry_cake"),
        Map.entry("hawaiian_pizza_slice", "hawaii_pizza_slice"),
        Map.entry("jasmine_tea", "jasmine_tea"),
        Map.entry("mint_tea", "mint_tea"),
        Map.entry("vanilla_pizza", "vanilla_pizza"),
        Map.entry("pepperoni_pizza", "pepperoni_pizza"),
        Map.entry("supreme_pizza", "supreme_pizza"),
        Map.entry("seafood_pizza", "seafood_pizza"),
        Map.entry("durian_pizza", "durian_pizza"),
        Map.entry("hawaiian_pizza", "hawaii_pizza"),
        Map.entry("cheesy_mushroom_bacon_pizza", "cheesy_mushroom_bacon_pizza"),
        Map.entry("emmental_cheese_wheel", "emmental_cheese_wheel"),
        Map.entry("cheddar_cheese_wheel", "cheddar_cheese_wheel"),
        Map.entry("mozzarella_cheese_wheel", "mozzarella_cheese_wheel"),
        Map.entry("durian_flesh", "durian_flesh"),
        Map.entry("miner_hat_helmet", "miner_hat"),
        Map.entry("pastry_chef_hat_helmet", "pastry_chef_hat"),
        Map.entry("prawn_and_corn_potage", "prawn_and_corn_potage"),
        Map.entry("starfruit_jalapeno_ice_cream", "starfruit_and_jalapeno_ice_cream"),
        Map.entry("surimi_crab_stick", "sirimi_crab_stick"),
        Map.entry("surimi_crab_stick_nigiri", "sirimi_crab_stick_nigiri"),
        Map.entry("vanilla_cake_slice", "cake_slice"),
        Map.entry("vanilla_chocolate_ice_cream", "vanilla_and_chocolate_ice_cream")
        ,Map.entry("vanilla_ice_cream_fluid_bucket", "vanilla_ice_cream_bucket")
        ,Map.entry("blueberry_ice_cream_fluid_bucket", "blueberry_ice_cream_bucket")
        ,Map.entry("cherries_ice_cream_fluid_bucket", "cherries_ice_cream_bucket")
        ,Map.entry("starfruit_ice_cream_fluid_bucket", "starfruit_ice_cream_bucket")
        ,Map.entry("chocolate_ice_cream_fluid_bucket", "chocolate_ice_cream_bucket")
        ,Map.entry("jalapeno_ice_cream_fluid_bucket", "jalapeno_ice_cream_bucket")
        ,Map.entry("olive_oil_bucket", "olive_oil_bucket")
        ,Map.entry("cake_liquid_bucket", "cake_liquid")
    );

    public ModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PleroCottage.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        for (RegistryObject<Item> entry : ItemRegistry.ITEMS.getEntries()) {
            if (entry.equals(ItemRegistry.SHAKER)) {
                continue;
            }
            Item item = entry.get();
            String itemName = entry.getId().getPath();
            if (DisplayFoodContent.hasSpecialItemModel(itemName)) {
                validateSpecialFoodResources(itemName, item);
            }
            if (GlassFoodModels.contains(itemName)) {
                var texture = modLoc("item/" + itemName);
                var flat = new ItemModelBuilder(texture, existingFileHelper)
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                    .texture("layer0", texture);
                separateTransformFoodItem(itemName, modLoc("custom/" + itemName + "_item_3d"), flat);
                continue;
            }
            if (item instanceof GeckoDisplayFoodItem) {
                geckoDisplayFoodFlatModel(itemName);
                continue;
            }
            if (DisplayFoodContent.usesJavaSpecialItemModel(itemName)) {
                javaDisplayFoodItem(itemName);
                continue;
            }
            if (item instanceof SpawnEggItem) {
                getBuilder(entry.getId().getPath())
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/template_spawn_egg")));
                continue;
            }
            if (entry.getId().getPath().equals("wrapped_candy")) {
                wrappedCandy();
                continue;
            }
            if (item instanceof ColoredBedItem) {
                getBuilder(entry.getId().getPath())
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/template_bed")))
                    .texture("particle", modLoc("block/"
                        + entry.getId().getPath().replace("_bed", "_wool")));
                continue;
            }
            if (item instanceof ColoredShulkerBoxItem) {
                getBuilder(entry.getId().getPath())
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/template_shulker_box")))
                    .texture("particle", modLoc("block/" + entry.getId().getPath()));
                continue;
            }
            if (ColoredDecorationContent.COLORS.stream()
                .anyMatch(color -> entry.getId().getPath().equals(color + "_candle"))) {
                singleTexture(entry.getId().getPath(), mcLoc("item/generated"), "layer0",
                    modLoc("item/" + entry.getId().getPath()));
                continue;
            }
            if (entry.getId().getPath().equals("rainbow_chocolate_candies")) {
                stackCountFood("rainbow_chocolate_candies", "rainbow_chocolate_candies_texture_number",
                    1, 2, 3, 4);
                continue;
            }
            if (entry.getId().getPath().equals("eyeball_jelly")) {
                stackCountFood("eyeball_jelly", "eyeball_jelly_texture_number", 4, 3, 2, 1);
                continue;
            }
            if (entry.getId().getPath().equals("popcorn_bucket")) {
                popcornBucket("popcorn_bucket", "popcorn_bucket", "popcorn_bucket_2", "popcorn_bucket_3");
                continue;
            }
            if (entry.getId().getPath().equals("salted_popcorn_bucket")) {
                popcornBucket("salted_popcorn_bucket", "salted_popcorn_bucket",
                    "salted_popcorn_bucket_2", "salted_popcorn_bucket_3");
                continue;
            }
            if (item instanceof DigestiveTabletsItem) {
                durableFood(entry.getId().getPath(), DigestiveTabletsItem.MAX_USES);
                continue;
            }
            if (item instanceof SeasoningItem && STAGED_SEASONINGS.contains(itemName)) {
                seasoningItem(itemName);
                continue;
            }
            String texture = FOOD_TEXTURE_OVERRIDES.get(entry.getId().getPath());
            if (texture != null) {
                singleTexture(entry.getId().getPath(), mcLoc("item/generated"), "layer0",
                    modLoc("item/" + texture));
                continue;
            }
            if (entry.getId().getPath().endsWith("_knife") || entry.getId().getPath().endsWith("_sickle")
                || entry.getId().getPath().endsWith("_rolling_pin")
                || entry.getId().getPath().endsWith("_fork")
                || entry.getId().getPath().endsWith("_chopsticks")) {
                getBuilder(entry.getId().getPath())
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/handheld")))
                    .texture("layer0", modLoc("item/" + entry.getId().getPath()));
                continue;
            }
            if (PlantCatalog.usesFlatIcon(entry) || itemName.equals("corn_seeds")
                || item instanceof PoultryEggItem) {
                
                basicItem(item);
                continue;
            }
            if (entry.getId().getPath().equals("winter_rose")) {
                basicItem(item);
                continue;
            }
            
            if (item instanceof BlockFoodItem) {
                basicItem(item);
                continue;
            }
            if (itemName.equals("banana_frond_block") || itemName.equals("banana_tree_bunch")) {
                String bananaTexture = itemName.equals("banana_frond_block")
                    ? "banana_frond_sapling"
                    : "banana_bunch";
                singleTexture(itemName, mcLoc("item/generated"), "layer0", modLoc("item/" + bananaTexture));
                continue;
            }
            if (item instanceof BlockItem && !(item instanceof SignItem)) {
                continue;
            }
            basicItem(item);
        }
        for (RegistryObject<Item> entry : FluidContent.buckets()) {
            String name = entry.getId().getPath();
            String texture = FOOD_TEXTURE_OVERRIDES.get(name);
            singleTexture(name, mcLoc("item/generated"), "layer0",
                modLoc("item/" + (texture == null ? name : texture)));
        }
    }

    private void validateSpecialFoodResources(String name, Item item) {
        requireFoodAsset(name, modLoc("textures/item/" + name + ".png"));
        if (GlassFoodModels.contains(name)) {
            requireFoodAsset(name, modLoc("models/custom/" + name + "_item_3d.json"));
            requireFoodAsset(name, modLoc("models/custom/" + name + "_content.json"));
            requireFoodAsset(name, modLoc("models/custom/glass_parts/" + GlassFoodModels.glassModel(name) + ".json"));
            requireFoodAsset(name, modLoc("textures/block/" + name + ".png"));
            requireFoodAsset(name, modLoc("textures/block/glasses.png"));
            if ("klein_bottle_5x6".equals(GlassFoodModels.glassModel(name))) {
                requireFoodAsset(name, modLoc("textures/block/klein_bottle.png"));
            }
        } else if (item instanceof GeckoDisplayFoodItem gecko) {
            requireGeckoAssets(name, gecko.resources());
            var composition = DisplayModelResources.compositionForModel(gecko.resources().model());
            if (composition != null) {
                composition.additions().forEach(part -> requireGeckoAssets(name, part));
            }
        } else {
            requireFoodAsset(name, modLoc("models/custom/" + name + "_item_3d.json"));
        }

        String packModel = "plerocottage-flat_items/assets/" + PleroCottage.MODID + "/models/item/" + name + ".json";
        requireFoodAsset(name, packModel);
    }

    private void requireGeckoAssets(String name, DisplayModelResources resources) {
        requireFoodAsset(name, resources.model());
        requireFoodAsset(name, resources.animation());
        requireFoodAsset(name, resources.texture());
    }

    private void requireFoodAsset(String name, ResourceLocation resource) {
        requireFoodAsset(name, "assets/" + resource.getNamespace() + "/" + resource.getPath());
    }

    private void requireFoodAsset(String name, String resourcePath) {
        String resourceRoot = System.getProperty("plerocottage.resources");
        Path mainResources = resourceRoot == null ? null : Path.of(resourceRoot).resolve(resourcePath);
        Path templateResources = (resourceRoot == null
            ? Path.of("src/main/datagen/resources")
            : Path.of(resourceRoot).toAbsolutePath().getParent().resolve("datagen/resources"))
            .resolve(resourcePath);
        if ((mainResources == null || !Files.isRegularFile(mainResources))
            && !Files.isRegularFile(templateResources)) {
            throw new IllegalStateException("Missing special food model resource for " + name + ": " + resourcePath);
        }
    }

    private void javaDisplayFoodItem(String name) {
        separateTransformFoodItem(name, modLoc("custom/" + name + "_item_3d"));
    }

    private void geckoDisplayFoodFlatModel(String name) {
        getBuilder(name + "_flat")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + name));
    }

    private void separateTransformFoodItem(String name, ResourceLocation baseParent) {
        var flat = getBuilder(name + "_flat")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + name));
        ResourceLocation location = modLoc("item/" + name);
        var flatReference = new ItemModelBuilder(location, existingFileHelper).parent(flat);
        separateTransformFoodItem(name, baseParent, flatReference);
    }

    private void separateTransformFoodItem(String name, ResourceLocation baseParent, ItemModelBuilder flat) {
        ResourceLocation location = modLoc("item/" + name);
        var base = new ItemModelBuilder(location, existingFileHelper)
            .parent(new ModelFile.UncheckedModelFile(baseParent));
        getBuilder(name)
            .guiLight(BlockModel.GuiLight.FRONT)
            .texture("particle", modLoc("item/" + name))
            .customLoader(SeparateTransformsModelBuilder::begin)
            .base(base)
            .perspective(ItemDisplayContext.GUI, flat)
            .perspective(ItemDisplayContext.GROUND, flat)
            .perspective(ItemDisplayContext.FIXED, flat);
    }

    private void wrappedCandy() {
        var model = getBuilder("wrapped_candy")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/wrapped_candy_3"));
        ResourceLocation predicate = new ResourceLocation(PleroCottage.MODID, "wrapped_candy_texture_number");
        for (int variant = 0; variant < 4; variant++) {
            String name = "wrapped_candy_" + variant;
            model.override()
                .predicate(predicate, variant)
                .model(singleTexture(name, mcLoc("item/generated"), "layer0", modLoc("item/" + name)))
                .end();
        }
    }

    private void stackCountFood(String name, String predicateName, int... textureNumbers) {
        var model = getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + name + "_1"));
        ResourceLocation predicate = new ResourceLocation(PleroCottage.MODID, predicateName);
        for (int variant = 0; variant < textureNumbers.length; variant++) {
            String variantName = name + "_" + variant;
            model.override()
                .predicate(predicate, variant)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/" + variantName)))
                .end();
            getBuilder(variantName)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/" + name + "_" + textureNumbers[variant]));
        }
    }

    private void popcornBucket(String name, String baseTexture, String middleTexture, String finalTexture) {
        var model = getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + baseTexture));
        model.override()
            .predicate(new ResourceLocation("minecraft", "damage"), 0.33F)
            .model(new ModelFile.UncheckedModelFile(modLoc("item/" + name + "_0")))
            .end();
        model.override()
            .predicate(new ResourceLocation("minecraft", "damage"), 0.66F)
            .model(new ModelFile.UncheckedModelFile(modLoc("item/" + name + "_1")))
            .end();
        getBuilder(name + "_0")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + middleTexture));
        getBuilder(name + "_1")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + finalTexture));
    }

    private void durableFood(String name, int states) {
        var model = getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + name + "_0"));
        ResourceLocation predicate = new ResourceLocation("minecraft", "damage");
        for (int state = 1; state < states; state++) {
            String variantName = name + "_" + state;
            model.override()
                .predicate(predicate, (float) state / states)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/" + variantName)))
                .end();
            getBuilder(variantName)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/" + variantName));
        }
    }

    private void seasoningItem(String name) {
        ItemModelBuilder model = getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + name));
        ResourceLocation damage = new ResourceLocation("minecraft", "damage");
        String[] textures = {"a", "b", "c"};
        for (int stage = 0; stage < textures.length; stage++) {
            String variant = name + "_" + stage;
            model.override()
                .predicate(damage, (stage + 1) * 0.25F)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/" + variant)))
                .end();
            singleTexture(variant, mcLoc("item/generated"), "layer0",
                modLoc("item/" + name + "_" + textures[stage]));
        }
    }
}

