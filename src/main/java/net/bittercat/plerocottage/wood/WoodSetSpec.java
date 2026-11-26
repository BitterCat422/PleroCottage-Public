package net.bittercat.plerocottage.wood;

import net.bittercat.plerocottage.content.wood.WoodTypeContent;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.WoodType;

public record WoodSetSpec(
    WoodType woodType,
    SoundType soundType,
    boolean stalkStyle,
    boolean raft
) {

    public String name() {
        return WoodTypeContent.pathOf(woodType);
    }

    public static WoodSetSpec standard(WoodType woodType) {
        SoundType sound = woodType == WoodTypeContent.CHERRIES_TREE
            ? SoundType.CHERRY_WOOD : SoundType.WOOD;
        return new WoodSetSpec(woodType, sound, false, false);
    }

    public static WoodSetSpec bamboo(WoodType woodType) {
        return new WoodSetSpec(woodType, SoundType.BAMBOO_WOOD, true, true);
    }
}
