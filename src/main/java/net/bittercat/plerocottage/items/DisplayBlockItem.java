package net.bittercat.plerocottage.items;

import java.util.function.Consumer;

import net.bittercat.plerocottage.client.renderer.DisplayItemRenderer;
import net.bittercat.plerocottage.client.renderer.model.DisplayItemGeoModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DisplayBlockItem extends BlockItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ResourceLocation model;
    private final ResourceLocation animation;
    private final ResourceLocation texture;

    public DisplayBlockItem(Block block, Properties properties, ResourceLocation model,
                            ResourceLocation animation, ResourceLocation texture) {
        super(block, properties);
        this.model = model;
        this.animation = animation;
        this.texture = texture;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        
        // 所以模型与渲染器留到真正渲染时再按字段建立，否则它们会拿到 null 的资源路径。
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new DisplayItemRenderer(new DisplayItemGeoModel(model, animation, texture));
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
