package net.bittercat.plerocottage.client.connected;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.building.BuildingBlockContent;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ConnectedTextureHandler {
    private static final CTSpriteShiftEntry SNOW = shift("snow_glass", "snow_glass_connected");
    private static final CTSpriteShiftEntry LAVENDER = shift("lavender_glass", "lavender_glass_connected");
    private static final CTSpriteShiftEntry TURQUOISE = shift("turquoise_glass", "turquoise_glass_connected");

    private ConnectedTextureHandler() {
    }

    private static CTSpriteShiftEntry shift(String source, String target) {
        CTSpriteShiftEntry entry = new CTSpriteShiftEntry(FullCTType.SHEET_SIZE);
        entry.set(new ResourceLocation(PleroCottage.MODID, "block/" + source),
            new ResourceLocation(PleroCottage.MODID, "block/" + target));
        return entry;
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        swap(event.getModels(), BuildingBlockContent.block("snow_glass").get(), SNOW, false);
        swap(event.getModels(), BuildingBlockContent.block("snow_glass_pane").get(), SNOW, true);
        swap(event.getModels(), BuildingBlockContent.block("lavender_glass").get(), LAVENDER, false);
        swap(event.getModels(), BuildingBlockContent.block("lavender_glass_pane").get(), LAVENDER, true);
        swap(event.getModels(), BuildingBlockContent.block("turquoise_glass").get(), TURQUOISE, false);
        swap(event.getModels(), BuildingBlockContent.block("turquoise_glasspane").get(), TURQUOISE, true);
    }

    private static void swap(Map<ResourceLocation, BakedModel> models, Block block,
                             CTSpriteShiftEntry shift, boolean pane) {
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
        if (id == null) return;
        for (BlockState state : block.getStateDefinition().getPossibleStates()) {
            ModelResourceLocation model = BlockModelShaper.stateToModelLocation(id, state);
            BakedModel original = models.get(model);
            if (original != null) models.put(model, new CTModel(original, shift, block, pane));
        }
    }
}
