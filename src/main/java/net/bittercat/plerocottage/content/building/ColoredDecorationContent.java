package net.bittercat.plerocottage.content.building;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.bittercat.plerocottage.blocks.ColoredBedBlock;
import net.bittercat.plerocottage.blocks.ColoredShulkerBoxBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.items.ColoredBedItem;
import net.bittercat.plerocottage.items.ColoredShulkerBoxItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

public final class ColoredDecorationContent {
    public static final List<String> COLORS = List.of("snow", "lavender", "turquoise");

    private static final Map<String, RegistryObject<Block>> BEDS = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> CANDLES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> CANDLE_CAKES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Block>> SHULKER_BOXES = new LinkedHashMap<>();
    private static boolean bootstrapped;

    private ColoredDecorationContent() {
    }

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        for (String color : COLORS) {
            String bedId = color + "_bed";
            RegistryObject<Block> bed = BlockRegistry.register(bedId, () -> new ColoredBedBlock(color));
            ItemRegistry.register(bedId, () -> new ColoredBedItem(bed.get(), new Item.Properties()));
            BEDS.put(color, bed);

            String candleId = color + "_candle";
            RegistryObject<Block> candle = BlockRegistry.register(candleId,
                () -> new CandleBlock(BlockBehaviour.Properties.copy(Blocks.CANDLE)));
            ItemRegistry.registerBlockItem(candleId, candle);
            CANDLES.put(color, candle);

            String candleCakeId = color + "_candle_cake";
            RegistryObject<Block> candleCake = BlockRegistry.register(candleCakeId,
                () -> new CandleCakeBlock(candle.get(), BlockBehaviour.Properties.copy(Blocks.WHITE_CANDLE_CAKE)));
            CANDLE_CAKES.put(color, candleCake);

            String shulkerId = color + "_shulker_box";
            RegistryObject<Block> shulker = BlockRegistry.register(shulkerId,
                () -> new ColoredShulkerBoxBlock(color));
            ItemRegistry.register(shulkerId, () -> new ColoredShulkerBoxItem(shulker.get(), new Item.Properties()));
            SHULKER_BOXES.put(color, shulker);
        }
    }

    public static RegistryObject<Block> bed(String color) {
        return BEDS.get(color);
    }

    public static RegistryObject<Block> candle(String color) {
        return CANDLES.get(color);
    }

    public static RegistryObject<Block> candleCake(String color) {
        return CANDLE_CAKES.get(color);
    }

    public static RegistryObject<Block> shulkerBox(String color) {
        return SHULKER_BOXES.get(color);
    }

    public static Block[] beds() {
        return BEDS.values().stream().map(RegistryObject::get).toArray(Block[]::new);
    }

    public static Block[] shulkerBoxes() {
        return SHULKER_BOXES.values().stream().map(RegistryObject::get).toArray(Block[]::new);
    }

    public static void registerCauldronInteractions(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (String color : COLORS) {
                CauldronInteraction.WATER.put(ColoredDecorationContent.shulkerBox(color).get().asItem(),
                    ColoredDecorationContent::washShulkerBox);
            }
        });
    }

    private static InteractionResult washShulkerBox(BlockState state, Level level, BlockPos pos, Player player,
                                                     InteractionHand hand, ItemStack stack) {
        if (!level.isClientSide) {
            ItemStack washed = new ItemStack(Items.SHULKER_BOX);
            if (stack.hasTag()) {
                washed.setTag(stack.getTag().copy());
            }
            player.setItemInHand(hand, washed);
            player.awardStat(Stats.CLEAN_SHULKER_BOX);
            LayeredCauldronBlock.lowerFillLevel(state, level, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
