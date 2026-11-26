package net.bittercat.plerocottage.content.plants;

import net.bittercat.plerocottage.blocks.BlockRef;
import net.bittercat.plerocottage.blocks.PurpleSugarCaneCropBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public final class PurpleSugarCanes {
    private static final BlockRef CROP_BLOCK = new BlockRef();

    public static final RegistryObject<Item> ITEM = ItemRegistry.register("purple_sugar_canes",
        () -> new PurpleSugarCaneItem(CROP_BLOCK.get()));

    public static final RegistryObject<PurpleSugarCaneCropBlock> CROP =
        BlockRegistry.register("purple_sugar_canes",
            () -> new PurpleSugarCaneCropBlock(ITEM::get));

    private PurpleSugarCanes() {
    }

    public static void bootstrap() {
        CROP_BLOCK.fill(CROP);
        PlantCatalog.markFlatIcon(ITEM);
        PlantCatalog.addCompostable(ITEM, 0.65F);
        
    }

    private static final class PurpleSugarCaneItem extends ItemNameBlockItem {
        private PurpleSugarCaneItem(Block block) {
            super(block, FoodDefinition.get("purple_sugar_canes").itemProperties());
        }

        @Override
        public int getUseDuration(ItemStack stack) {
            return FoodDefinition.get("purple_sugar_canes").useDuration();
        }

        @Override
        public net.minecraft.world.item.UseAnim getUseAnimation(ItemStack stack) {
            return FoodDefinition.get("purple_sugar_canes").useAnim();
        }
    }
}
