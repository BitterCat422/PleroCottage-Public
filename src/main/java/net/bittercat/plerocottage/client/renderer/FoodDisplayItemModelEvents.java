package net.bittercat.plerocottage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.food.DisplayFoodContent;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class FoodDisplayItemModelEvents {
    private FoodDisplayItemModelEvents() {
    }

    @SubscribeEvent
    public static void registerFlatModels(ModelEvent.RegisterAdditional event) {
        for (String name : DisplayFoodContent.geckoSpecialItemModelNames()) {
            event.register(flatModelId(name));
        }
    }

    @SubscribeEvent
    public static void setDisplayModels(ModelEvent.ModifyBakingResult event) {
        var models = event.getModels();
        for (String name : DisplayFoodContent.geckoSpecialItemModelNames()) {
            var itemId = new ModelResourceLocation(new ResourceLocation(PleroCottage.MODID, name), "inventory");
            BakedModel gecko = models.get(itemId);
            BakedModel flat = models.get(flatModelId(name));
            if (gecko != null && gecko.isCustomRenderer() && flat != null) {
                models.put(itemId, new GeckoFoodModel(flat, gecko));
            }
        }
    }

    private static ResourceLocation flatModelId(String name) {
        return new ResourceLocation(PleroCottage.MODID, "item/" + name + "_flat");
    }

    private static final class GeckoFoodModel extends BakedModelWrapper<BakedModel> {
        private final BakedModel gecko;

        private GeckoFoodModel(BakedModel flat, BakedModel gecko) {
            
            super(flat);
            this.gecko = gecko;
        }

        @Override
        public BakedModel applyTransform(ItemDisplayContext context, PoseStack poseStack, boolean leftHand) {
            return switch (context) {
                case GUI, GROUND, FIXED -> super.applyTransform(context, poseStack, leftHand);
                default -> gecko.applyTransform(context, poseStack, leftHand);
            };
        }
    }
}
