package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.bittercat.plerocottage.blocks.LavenderBlock;
import net.bittercat.plerocottage.blocks.RegisteredPinkPetalsBlock;
import net.bittercat.plerocottage.blocks.SimpleFlowerBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public final class Flowers {

    private static final List<String> GENERATED_NAMES =
        List.of("coffea_arabica", "galanthus_nivalis", "hyacinth", "jasmine", "orange_blossom", "radde");
    private static final List<String> PETAL_NAMES =
        List.of("chamomiles", "wild_roses", "dianthuses", "unlucky_clovers", "lucky_clovers");
    private static final Map<String, RegistryObject<Block>> BLOCKS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> ITEMS = new LinkedHashMap<>();

    public static RegistryObject<Block> block(String name) {
        return BLOCKS.get(name);
    }

    public static RegistryObject<Item> item(String name) {
        return ITEMS.get(name);
    }

    public static Map<String, RegistryObject<Block>> blocks() {
        return Map.copyOf(BLOCKS);
    }

    public static List<String> generatedNames() {
        return GENERATED_NAMES;
    }

    public static List<String> petalNames() {
        return PETAL_NAMES;
    }

    private Flowers() {
    }

    public static void bootstrap() {
        register("blue_tulip", true);
        register("camelia", true);
        register("coffea_arabica", true);
        register("galanthus_nivalis", false);
        register("hyacinth", true);
        register("jasmine", true);
        register("lavender", true);
        register("licorice", true);
        register("orange_blossom", true);
        register("radde", false);
        register("rose", true);
        register("saffron", true);
        register("sakura", true);
        register("semper_augustus", true);
        
        register("winter_rose", false);
        for (String name : PETAL_NAMES) {
            registerPetals(name);
        }
        PlantCatalog.addCompostable(item("coffea_arabica"), 0.65F);
        PlantCatalog.addCompostable(item("lavender"), 0.65F);
        PlantCatalog.addCompostable(item("hyacinth"), 0.65F);
        PlantCatalog.addCompostable(item("blue_tulip"), 0.65F);
        PlantCatalog.addCompostable(item("semper_augustus"), 0.65F);
        PlantCatalog.addCompostable(item("camelia"), 0.65F);
        PlantCatalog.addCompostable(item("jasmine"), 0.65F);
        PlantCatalog.addCompostable(item("saffron"), 0.65F);
        PlantCatalog.addCompostable(item("licorice"), 0.65F);
        PlantCatalog.addCompostable(item("sakura"), 0.65F);
        PlantCatalog.addCompostable(item("rose"), 0.65F);
        PlantCatalog.addCompostable(item("orange_blossom"), 0.65F);
    }

    private static void registerPetals(String name) {
        RegistryObject<Block> block = BlockRegistry.register(name,
            () -> new RegisteredPinkPetalsBlock(name.equals("unlucky_clovers")));
        RegistryObject<Item> item = ItemRegistry.registerBlockItem(name, block);
        BLOCKS.put(name, block);
        ITEMS.put(name, item);
        PlantCatalog.addCompostable(item, 0.3F);
    }

    public static String petalStemTexture(String name) {
        return name.equals("unlucky_clovers") ? "unlucky_clovers_stem" : "flower_petals_stem";
    }

    private static BlockBehaviour.Properties properties(MapColor color) {
        return BlockBehaviour.Properties.of().mapColor(color).sound(SoundType.GRASS)
            .instabreak().noCollission().offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.DESTROY);
    }

    private static void register(String name, boolean creative) {
        RegistryObject<Block> block = BlockRegistry.register(name, () -> switch (name) {
            case "lavender" -> new LavenderBlock();
            case "coffea_arabica" ->
                new FlowerBlock(() -> MobEffects.MOVEMENT_SPEED, 100, properties(MapColor.PLANT));
            case "licorice" -> new SimpleFlowerBlock(100, properties(MapColor.PLANT),
                Block.box(5, 0, 5, 11, 12, 11));
            case "jasmine", "orange_blossom" ->
                new FlowerBlock(() -> MobEffects.MOVEMENT_SPEED, 100, properties(MapColor.PLANT));
            case "camelia", "rose", "saffron", "sakura" ->
                new FlowerBlock(() -> MobEffects.MOVEMENT_SPEED, 100, properties(MapColor.PLANT));
            case "blue_tulip" ->
                new FlowerBlock(() -> MobEffects.MOVEMENT_SPEED, 0, properties(MapColor.PLANT));
            case "galanthus_nivalis", "radde" ->
                new FlowerBlock(() -> MobEffects.MOVEMENT_SPEED, 0, properties(MapColor.WOOL));
            case "semper_augustus", "winter_rose" ->
                new FlowerBlock(() -> MobEffects.MOVEMENT_SPEED, 0, properties(
                    name.equals("winter_rose") ? MapColor.QUARTZ : MapColor.PLANT));
            default -> new FlowerBlock(() -> MobEffects.MOVEMENT_SPEED, 0, properties(MapColor.PLANT));
        });
        RegistryObject<Item> item = ItemRegistry.register(name,
            () -> new BlockItem(block.get(), new Item.Properties()));
        BLOCKS.put(name, block);
        ITEMS.put(name, item);
        if (creative) {
            
        }
    }
}
