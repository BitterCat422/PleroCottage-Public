package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.content.plants.Melons;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public final class HamimelonStemBlock extends StemBlock {
    public HamimelonStemBlock() {
        super((StemGrownBlock) Melons.HAMIMELON.get(), () -> Melons.HAMIMELON_SEED.get(),
            BlockBehaviour.Properties.of().sound(SoundType.CROP).instabreak().noCollission()
                .noOcclusion().randomTicks().pushReaction(PushReaction.DESTROY));
    }
}
