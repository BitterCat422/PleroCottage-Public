package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.blockentities.ColoredBedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class ColoredBedBlock extends BedBlock {
    private final String color;

    public ColoredBedBlock(String color) {
        super(DyeColor.WHITE, BlockBehaviour.Properties.of()
            .mapColor(mapColor(color))
            .strength(0.2F)
            .sound(net.minecraft.world.level.block.SoundType.WOOD)
            .noOcclusion());
        this.color = color;
    }

    public String colorName() {
        return color;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ColoredBedBlockEntity(pos, state);
    }

    private static net.minecraft.world.level.material.MapColor mapColor(String color) {
        return switch (color) {
            case "lavender" -> net.minecraft.world.level.material.MapColor.COLOR_PURPLE;
            case "turquoise" -> net.minecraft.world.level.material.MapColor.DIAMOND;
            default -> net.minecraft.world.level.material.MapColor.SNOW;
        };
    }
}
