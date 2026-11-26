package net.bittercat.plerocottage.items;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.bittercat.plerocottage.client.renderer.FoodDisplayItemRenderer;
import net.bittercat.plerocottage.client.renderer.model.FoodDisplayItemGeoModel;
import net.bittercat.plerocottage.content.food.DisplayModelResources;
import net.bittercat.plerocottage.content.food.FoodDisplayTransform;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class GeckoDisplayFoodItem extends DisplayFoodItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final DisplayModelResources resources;
    private final FoodDisplayTransform transforms;

    public GeckoDisplayFoodItem(Block block, Properties properties, int useDuration,
                                UseAnim useAnim, Supplier<? extends Item> container,
                                DisplayModelResources resources,
                                FoodDisplayTransform transforms) {
        super(block, properties, useDuration, useAnim, container);
        this.resources = resources;
        this.transforms = transforms;
    }

    public DisplayModelResources resources() {
        return resources;
    }

    public FoodDisplayTransform transforms() {
        return transforms;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new FoodDisplayItemRenderer(
                        new FoodDisplayItemGeoModel(resources));
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0,
            state -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
