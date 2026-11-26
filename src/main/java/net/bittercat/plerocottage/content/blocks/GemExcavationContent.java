package net.bittercat.plerocottage.content.blocks;

import net.bittercat.plerocottage.init.BlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public final class GemExcavationContent {
    public static final RegistryObject<Block> ORE_DISPLAY =
        BlockRegistry.register("gem_excavation_ore_display", () -> new Block(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.NONE)
                .strength(-1.0F, 3_600_000.0F)
                .noLootTable()
                .noOcclusion()));

    private GemExcavationContent() {
    }

    public static void bootstrap() {
    }
}
