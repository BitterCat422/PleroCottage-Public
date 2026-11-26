package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.content.building.BuildingBlockContent;
import net.bittercat.plerocottage.content.building.ColoredDecorationContent;
import net.bittercat.plerocottage.content.blocks.MarbleContent;
import net.bittercat.plerocottage.content.blocks.GemExcavationContent;
import net.bittercat.plerocottage.content.blocks.OreBlockContent;
import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.bittercat.plerocottage.content.audio.SoundContent;
import net.bittercat.plerocottage.content.equipment.EquipmentContent;
import net.bittercat.plerocottage.content.effects.MobEffectContent;
import net.bittercat.plerocottage.content.fluids.FluidContent;
import net.bittercat.plerocottage.content.core.CoreContent;
import net.bittercat.plerocottage.content.entities.PoultryContent;
import net.bittercat.plerocottage.content.entities.EntityContent;
import net.bittercat.plerocottage.content.food.DisplayFoodContent;
import net.bittercat.plerocottage.content.food.FoodBlockContent;
import net.bittercat.plerocottage.content.food.FoodContent;
import net.bittercat.plerocottage.content.food.StackableFoodContent;
import net.bittercat.plerocottage.content.food.PlateContent;
import net.bittercat.plerocottage.content.food.PopcornBucketContent;
import net.bittercat.plerocottage.content.food.DigestiveTabletsContent;
import net.bittercat.plerocottage.content.food.SeasoningContent;
import net.bittercat.plerocottage.content.furniture.FurnitureContent;
import net.bittercat.plerocottage.content.items.DyeContent;
import net.bittercat.plerocottage.content.items.OrdinaryItemContent;
import net.bittercat.plerocottage.content.menus.MenuContent;
import net.bittercat.plerocottage.content.particles.ParticleContent;
import net.bittercat.plerocottage.content.plants.PottedPlantContent;
import net.bittercat.plerocottage.content.worldgen.FeatureContent;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
import net.bittercat.plerocottage.content.wood.WoodTypeContent;
import net.bittercat.plerocottage.content.plants.PlantBootstrap;

public final class ModContent {
    private static boolean bootstrapped;

    private ModContent() {
    }

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        EquipmentContent.registerContent();
        CoreContent.bootstrap();
        SoundContent.bootstrap();
        ParticleContent.bootstrap();
        MenuContent.bootstrap();
        FeatureContent.bootstrap();
        EntityContent.bootstrap();
        MobEffectContent.bootstrap();
        WoodTypeContent.bootstrap();
        WoodSetContent.bootstrap();
        FurnitureContent.bootstrap();
        PlateContent.bootstrap();
        PlantBootstrap.bootstrap();
        FoodContent.bootstrap();
        StackableFoodContent.bootstrap();
        PopcornBucketContent.bootstrap();
        DigestiveTabletsContent.bootstrap();
        FoodBlockContent.bootstrap();
        DisplayFoodContent.bootstrap();
        OrdinaryItemContent.bootstrap();
        OreBlockContent.bootstrap();
        GemExcavationContent.bootstrap();
        SeasoningContent.bootstrap();
        PottedPlantContent.bootstrap();
        DyeContent.bootstrap();
        BuildingBlockContent.bootstrap();
        ColoredDecorationContent.bootstrap();
        MarbleContent.bootstrap();
        FluidContent.bootstrap();
        PoultryContent.bootstrap();
        BlockEntityContent.bootstrap();
    }
}
