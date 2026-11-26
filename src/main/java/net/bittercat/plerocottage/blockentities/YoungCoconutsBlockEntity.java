package net.bittercat.plerocottage.blockentities;

import net.bittercat.plerocottage.blocks.YoungCoconutsBlock;
import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class YoungCoconutsBlockEntity extends BlockEntity implements GeoBlockEntity {

    private static final String IDLE_ANIMATION = "0";

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int lastStage;

    public YoungCoconutsBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityContent.YOUNG_COCONUTS.get(), pos, state);
        lastStage = state.getValue(YoungCoconutsBlock.BLOCKSTATE);
    }

    public int stage() {
        return getBlockState().getValue(YoungCoconutsBlock.BLOCKSTATE);
    }

    private PlayState idle(AnimationState<YoungCoconutsBlockEntity> state) {
        int stage = stage();
        if (stage != lastStage) {
            lastStage = stage;
            state.getController().forceAnimationReset();
        }
        return state.setAndContinue(RawAnimation.begin().thenLoop(IDLE_ANIMATION));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::idle));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
