package net.bittercat.plerocottage.blockentities;

import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BananaBlockEntity extends BlockEntity implements GeoBlockEntity {

    private static final String IDLE_ANIMATION = "0";

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private BlockState lastState;

    public BananaBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityContent.bananaType(state.getBlock()), pos, state);
        lastState = state;
    }

    private PlayState idle(AnimationState<BananaBlockEntity> state) {
        BlockState current = getBlockState();
        if (!current.equals(lastState)) {
            lastState = current;
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
