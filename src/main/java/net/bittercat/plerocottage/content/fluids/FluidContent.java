package net.bittercat.plerocottage.content.fluids;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.FluidRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.items.ModFlowingFluid;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.RegistryObject;

public final class FluidContent {
    private static final java.util.List<String> FLUIDS = java.util.List.of(
        "vanilla_ice_cream_fluid",
        "blueberry_ice_cream_fluid",
        "cherries_ice_cream_fluid",
        "starfruit_ice_cream_fluid",
        "chocolate_ice_cream_fluid",
        "jalapeno_ice_cream_fluid",
        "olive_oil",
        "cake_liquid"
    );
    private static final Map<String, RegistryObject<FluidType>> TYPES = new HashMap<>();
    private static final Map<String, RegistryObject<FlowingFluid>> SOURCES = new HashMap<>();
    private static final Map<String, RegistryObject<FlowingFluid>> FLOWING = new HashMap<>();
    private static final Map<String, RegistryObject<LiquidBlock>> BLOCKS = new HashMap<>();
    private static final Map<String, RegistryObject<Item>> BUCKETS = new HashMap<>();

    private FluidContent() {
    }

    public static void bootstrap() {
        FLUIDS.forEach(FluidContent::registerFluid);
        FLUIDS.forEach(FluidContent::registerBlock);
        FLUIDS.forEach(FluidContent::registerBucket);
    }

    private static void registerFluid(String name) {
        TYPES.put(name, FluidRegistry.FLUID_TYPES.register(name + "_type", () -> new TexturedFluidType(name)));
        SOURCES.put(name, FluidRegistry.FLUIDS.register(name, () -> new ModFlowingFluid.Source(name)));
        FLOWING.put(name, FluidRegistry.FLUIDS.register("flowing_" + name,
            () -> new ModFlowingFluid.Flowing(name)));
    }

    private static void registerBlock(String name) {
        BLOCKS.put(name, BlockRegistry.register(name, () -> new LiquidBlock(
            () -> source(name).get(),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).replaceable().noCollission()
                .strength(100.0F).noLootTable())));
    }

    private static void registerBucket(String name) {
        BUCKETS.put(name, ItemRegistry.register(name + "_bucket",
            () -> new BucketItem(source(name).get(),
                new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET))));
    }

    public static RegistryObject<FluidType> type(String name) {
        return TYPES.get(name);
    }

    public static RegistryObject<FlowingFluid> source(String name) {
        return SOURCES.get(name);
    }

    public static RegistryObject<FlowingFluid> flowing(String name) {
        return FLOWING.get(name);
    }

    public static RegistryObject<LiquidBlock> block(String name) {
        return BLOCKS.get(name);
    }

    public static RegistryObject<Item> bucket(String name) {
        return BUCKETS.get(name);
    }

    public static Collection<RegistryObject<Item>> buckets() {
        return BUCKETS.values();
    }

    private static final class TexturedFluidType extends FluidType {
        private final String name;

        private TexturedFluidType(String name) {
            super(Properties.create().canHydrate(true).supportsBoating(true)
                .sound(net.minecraftforge.common.SoundActions.BUCKET_FILL,
                    net.minecraft.sounds.SoundEvents.BUCKET_FILL)
                .sound(net.minecraftforge.common.SoundActions.BUCKET_EMPTY,
                    net.minecraft.sounds.SoundEvents.BUCKET_EMPTY));
            this.name = name;
        }

        @Override
        public void initializeClient(java.util.function.Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                @Override
                public net.minecraft.resources.ResourceLocation getStillTexture() {
                    return new net.minecraft.resources.ResourceLocation(PleroCottage.MODID, "block/" + name + "_still");
                }

                @Override
                public net.minecraft.resources.ResourceLocation getFlowingTexture() {
                    return new net.minecraft.resources.ResourceLocation(PleroCottage.MODID, "block/" + name + "_flow");
                }
            });
        }
    }
}
