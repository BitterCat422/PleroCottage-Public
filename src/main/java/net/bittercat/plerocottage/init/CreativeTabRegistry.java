package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.ModList;

public final class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PleroCottage.MODID);

    public enum Tab {
        MAIN,
        BUILDING,
        NATURAL,
        PROCESSED,
        MACHINE
    }

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("plerocottage",
        () -> tab(CreativeTabDefinition.Tab.MAIN, "plerocottage",
            net.bittercat.plerocottage.content.core.CoreContent.MAIN_ICON).build());

    public static final RegistryObject<CreativeModeTab> BUILDING =
        CREATIVE_MODE_TABS.register("plerocottage_building",
            () -> tab(CreativeTabDefinition.Tab.BUILDING, "plerocottage_building",
                net.bittercat.plerocottage.content.core.CoreContent.BUILDING_ICON)
                .withTabsBefore(MAIN.getId()).build());

    public static final RegistryObject<CreativeModeTab> NATURAL =
        CREATIVE_MODE_TABS.register("plerocottage_natural_products",
            () -> tab(CreativeTabDefinition.Tab.NATURAL, "plerocottage_natural_products",
                net.bittercat.plerocottage.content.core.CoreContent.NATURAL_PRODUCTS_ICON)
                .withTabsBefore(BUILDING.getId()).build());

    public static final RegistryObject<CreativeModeTab> PROCESSED =
        CREATIVE_MODE_TABS.register("plerocottage_processed_products",
            () -> tab(CreativeTabDefinition.Tab.PROCESSED, "plerocottage_processed_products",
                net.bittercat.plerocottage.content.core.CoreContent.PROCESSED_PRODUCTS_ICON)
                .withTabsBefore(NATURAL.getId()).build());

    public static final RegistryObject<CreativeModeTab> MACHINE = 
        CREATIVE_MODE_TABS.register("plerocottage_machine",
            () -> tab(CreativeTabDefinition.Tab.MACHINE, "plerocottage_machine",
                net.bittercat.plerocottage.content.core.CoreContent.MACHINE_ICON)
                .withTabsBefore(PROCESSED.getId()).build());

    private CreativeTabRegistry() {
    }

    private static CreativeModeTab.Builder tab(CreativeTabDefinition.Tab tab, String id,
                                               RegistryObject<Item> icon) {
        return CreativeModeTab.builder()
            .title(Component.translatable("item_group." + PleroCottage.MODID + "." + id))
            .icon(() -> new ItemStack(icon.get()))
            .displayItems((parameters, output) -> {
                for (CreativeTabDefinition.Entry entry : CreativeTabDefinition.ENTRIES) {
                    if (entry.tab() != tab) {
                        continue;
                    }
                    if (entry.requiredModId() != null && !ModList.get().isLoaded(entry.requiredModId())) {
                        continue;
                    }
                    ResourceLocation itemId = new ResourceLocation(PleroCottage.MODID, entry.itemId());
                    Item item = BuiltInRegistries.ITEM.get(itemId);
                    if (item == net.minecraft.world.item.Items.AIR) {
                        throw new IllegalStateException("创造模式物品栏引用了未知物品：" + itemId);
                    }
                    output.accept(new ItemStack(item));
                }
            });
    }
}
