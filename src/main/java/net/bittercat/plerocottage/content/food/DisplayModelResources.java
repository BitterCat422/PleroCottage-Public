package net.bittercat.plerocottage.content.food;

import java.util.List;
import java.util.Map;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.resources.ResourceLocation;

public record DisplayModelResources(ResourceLocation model, ResourceLocation animation,
                                    ResourceLocation texture, float red, float green, float blue) {
    public static final DisplayModelResources BOWL = new DisplayModelResources(
        resource("geo/bowl.geo.json"),
        resource("animations/bowl.animation.json"),
        resource("textures/block/bowl.png"));
    public static final DisplayModelResources BORSCHT = new DisplayModelResources(
        resource("geo/borscht.geo.json"),
        resource("animations/static_food.animation.json"),
        resource("textures/block/borscht.png"));
    private static final DisplayModelResources BOWL_BLUE = new DisplayModelResources(
        resource("geo/bowl.geo.json"),
        resource("animations/bowl.animation.json"),
        resource("textures/block/bowl_blue.png"));
    private static final DisplayModelResources BOWL_WOOD = new DisplayModelResources(
        resource("geo/bowl.geo.json"),
        resource("animations/bowl.animation.json"),
        resource("textures/block/bowl_wood.png"));

    private static final Map<String, Composition> COMPOSITIONS = Map.ofEntries(
        Map.entry("borscht", new Composition(BORSCHT, List.of(BOWL))),
        Map.entry("fliederbeersuppe", composition("fliederbeersuppe", BOWL)),
        Map.entry("mushroom_potage", composition("mushroom_potage", BOWL)),
        Map.entry("miso_soup", composition("miso_soup", BOWL_WOOD)),
        Map.entry("prawn_and_corn_potage", composition("prawn_and_corn_potage", BOWL)),
        Map.entry("canton_bloom_soup", composition("canton_bloom_soup", BOWL)),
        Map.entry("cosmic_dragon_salad", composition("cosmic_dragon_salad", BOWL)),
        Map.entry("rosa_salad", composition("rosa_salad", BOWL)),
        Map.entry("sea_pearl_salad", composition("sea_pearl_salad", BOWL)),
        Map.entry("melon_salad", composition("melon_salad", BOWL)),
        Map.entry("jewellery_box_salad", composition("jewellery_box_salad", BOWL)),
        Map.entry("primary_colors_salad", composition("primary_colors_salad", BOWL)),
        Map.entry("ochazuke", composition("ochazuke", BOWL_WOOD)),
        Map.entry("suanmeitang", composition("suanmeitang", BOWL_WOOD)),
        Map.entry("cornflakes", composition("cornflakes", BOWL_BLUE)));
    private static final Map<ResourceLocation, Composition> COMPOSITIONS_BY_MODEL =
        COMPOSITIONS.values().stream().collect(java.util.stream.Collectors.toUnmodifiableMap(
            composition -> composition.main().model(), composition -> composition));

    public DisplayModelResources(ResourceLocation model, ResourceLocation animation,
                                 ResourceLocation texture) {
        this(model, animation, texture, 1F, 1F, 1F);
    }

    public record Composition(DisplayModelResources main, List<DisplayModelResources> additions) {
        public Composition {
            additions = List.copyOf(additions);
        }
    }

    public static Composition compositionForBlock(String blockName) {
        return COMPOSITIONS.get(blockName);
    }

    public static Composition compositionForModel(ResourceLocation model) {
        return COMPOSITIONS_BY_MODEL.get(model);
    }

    public static DisplayModelResources forBlock(String blockName) {
        Composition composition = compositionForBlock(blockName);
        return composition != null ? composition.main() : new DisplayModelResources(
            resource("geo/" + blockName + ".geo.json"),
            resource("animations/static_food.animation.json"),
            resource("textures/block/" + blockTextureName(blockName) + ".png"));
    }

    public static String blockTextureName(String blockName) {
        return blockName;
    }

    private static ResourceLocation resource(String path) {
        return new ResourceLocation(PleroCottage.MODID, path);
    }

    private static Composition composition(String modelName, DisplayModelResources bowl) {
        return new Composition(new DisplayModelResources(
            resource("geo/" + modelName + ".geo.json"),
            resource("animations/static_food.animation.json"),
            resource("textures/block/" + modelName + ".png")),
            List.of(bowl));
    }
}
