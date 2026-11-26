package net.bittercat.plerocottage.client;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ItemPropertyRegistry {

    private ItemPropertyRegistry() {
    }

    @SubscribeEvent
    public static void registerItemProperties(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Item wrappedCandy = ForgeRegistries.ITEMS.getValue(
                new ResourceLocation(PleroCottage.MODID, "wrapped_candy"));
            ItemProperties.register(wrappedCandy,
                new ResourceLocation(PleroCottage.MODID, "wrapped_candy_texture_number"),
                (stack, level, entity, seed) -> (stack.getMaxStackSize() - stack.getCount()) % 4);
            registerStackTextureProperty("rainbow_chocolate_candies");
            registerStackTextureProperty("eyeball_jelly");
        });
    }

    private static void registerStackTextureProperty(String itemName) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(PleroCottage.MODID, itemName));
        ItemProperties.register(item,
            new ResourceLocation(PleroCottage.MODID, itemName + "_texture_number"),
            (stack, level, entity, seed) -> (stack.getMaxStackSize() - stack.getCount()) % 4);
    }
}
