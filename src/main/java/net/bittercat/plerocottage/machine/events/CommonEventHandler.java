package net.bittercat.plerocottage.machine.events;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CookingPotRecipeCompat;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CuttingBoardRecipeCompat;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.machine.compat.plerocottage.SmokingRecipeManager;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.machine.init.VillagerRegistry;

import java.util.List;
import java.util.Map;

public class CommonEventHandler {
    public static void handlerServerStartedEvent(ServerStartedEvent event) {
        RecipeManager recipeManager = event.getServer().getRecipeManager();
        SmokingRecipeManager.init(recipeManager);
        CuttingBoardRecipeCompat.init(recipeManager);
        CookingPotRecipeCompat.init(recipeManager);
        SlotInputLimitManager.init(recipeManager);
    }

    public static void handlerTagsUpdatedEvent(TagsUpdatedEvent event) {
        if (event.getUpdateCause() != TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) return;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        RecipeManager recipeManager = server.getRecipeManager();
        SmokingRecipeManager.init(recipeManager);
        CuttingBoardRecipeCompat.init(recipeManager);
        CookingPotRecipeCompat.init(recipeManager);
        SlotInputLimitManager.init(recipeManager);
    }

    public static void handlerMissingMappingsEvent(MissingMappingsEvent event) {
        Map<ResourceLocation, Item> items = Map.of(
                PleroCottageCompat.getPleroCottageResourceLocation("ice_cream_machine"), ItemRegistry.ICE_CREAM_MACHINE.get(),
                PleroCottageCompat.getPleroCottageResourceLocation("fryer"), ItemRegistry.FRYER.get(),
                PleroCottageCompat.getPleroCottageResourceLocation("oven"), ItemRegistry.OVEN.get()
        );
        Map<ResourceLocation, Block> blocks = Map.of(
                PleroCottageCompat.getPleroCottageResourceLocation("ice_cream_machine"), BlockRegistry.ICE_CREAM_MACHINE.get(),
                PleroCottageCompat.getPleroCottageResourceLocation("fryer"), BlockRegistry.FRYER.get(),
                PleroCottageCompat.getPleroCottageResourceLocation("oven"), BlockRegistry.OVEN.get()
        );
        for (MissingMappingsEvent.Mapping<Item> mapping : event.getAllMappings(ForgeRegistries.ITEMS.getRegistryKey())) {
            ResourceLocation key = mapping.getKey();
            if (items.containsKey(key)) {
                mapping.remap(items.get(key));
            }
        }
        for (MissingMappingsEvent.Mapping<Block> mapping : event.getAllMappings(ForgeRegistries.BLOCKS.getRegistryKey())) {
            ResourceLocation key = mapping.getKey();
            if (blocks.containsKey(key)) {
                mapping.remap(blocks.get(key));
            }
        }
    }

    public static void handlerVillagerTradesEvent(VillagerTradesEvent event) {
        if (event.getType() == VillagerRegistry.ORCHARDIST.get()) {
            noviceTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("orange_seeds"),
                    emerald(5),
                    PleroCottageCompat.getPleroCottageItemStack("lemon_seeds")
            );
            noviceTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("banana_frond"),
                    emerald()
            );
            noviceTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("strawberry", 12),
                    emerald()
            );
            noviceTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("dragon_fruit_cactus_flower"),
                    emerald()
            );
            noviceTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("purple_sugar_canes", 16),
                    emerald()
            );
            apprenticeTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("lemon", 20),
                    PleroCottageCompat.getPleroCottageItemStack("pink_crystal", 5),
                    PleroCottageCompat.getPleroCottageItemStack("lime", 1)
            );
            apprenticeTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("fig", 12),
                    emerald()
            );
            apprenticeTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("pineapple", 3),
                    emerald()
            );
            journeymanTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("garnet", 5),
                    ItemRegistry.BLENDER.get().getDefaultInstance()
            );
            journeymanTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("golden_apple_slice", 6),
                    PleroCottageCompat.getPleroCottageItemStack("topaz", 8),
                    PleroCottageCompat.getPleroCottageItemStack("enchanted_golden_apple_slice")
            );
            expertTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("dragon_fruit", 2),
                    emerald()
            );
            expertTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("peach", 24),
                    PleroCottageCompat.getPleroCottageItemStack("garnet", 5)
            );
            masterTrades(
                    event.getTrades(),
                    Items.APPLE.getDefaultInstance(),
                    emerald(24),
                    PleroCottageCompat.getPleroCottageItemStack("green_apple", 5)
            );
            masterTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("orange"),
                    PleroCottageCompat.getPleroCottageItemStack("topaz", 18),
                    PleroCottageCompat.getPleroCottageItemStack("grapefruit")
            );
            masterTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("strawberry"),
                    PleroCottageCompat.getPleroCottageItemStack("pink_crystal", 5),
                    PleroCottageCompat.getPleroCottageItemStack("pineberry")
            );
        }
        if (event.getType() == VillagerRegistry.CHEF.get()) {
            noviceTrades(
                    event.getTrades(),
                    emerald(5),
                    PleroCottageCompat.getPleroCottageItemStack("miso_soup")
            );
            noviceTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("garnet", 5),
                    PleroCottageCompat.getPleroCottageItemStack("cherries_pie")
            );
            noviceTrades(
                    event.getTrades(),
                    emerald(5),
                    PleroCottageCompat.getPleroCottageItemStack("garnet"),
                    PleroCottageCompat.getPleroCottageItemStack("corn", 5)
            );
            noviceTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("fried_chicken", 3),
                    emerald(1)
            );
            noviceTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("fried_steak", 3),
                    emerald(1)
            );
            apprenticeTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("pink_crystal", 5),
                    ItemRegistry.ICE_CREAM_MACHINE.get().getDefaultInstance()
            );
            apprenticeTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("topaz", 18),
                    ItemRegistry.OVEN.get().getDefaultInstance()
            );
            apprenticeTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("garnet", 12),
                    ItemRegistry.FRYER.get().getDefaultInstance()
            );
            journeymanTrades(
                    event.getTrades(),
                    emerald(4),
                    PleroCottageCompat.getPleroCottageItemStack("croissant")
            );
            journeymanTrades(
                    event.getTrades(),
                    emerald(5),
                    PleroCottageCompat.getPleroCottageItemStack("yogurt")
            );
            journeymanTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("vanilla_chocolate_ice_cream", 3),
                    emerald(10)
            );
            journeymanTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("baguette", 3),
                    emerald(7)
            );
            expertTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("topaz", 5),
                    Items.GOLDEN_APPLE.getDefaultInstance()
            );
            expertTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("topaz", 5),
                    PleroCottageCompat.getPleroCottageItemStack("mozzarella_cheese_slice")
            );
            masterTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("colorful_fruit_parfait"),
                    emerald(20)
            );
            masterTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("pudding_a_la_mode"),
                    PleroCottageCompat.getPleroCottageItemStack("pink_crystal")
            );
        }
        if (event.getType() == VillagerRegistry.BARTENDER.get()) {
            noviceTrades(
                    event.getTrades(),
                    emerald(5),
                    ItemRegistry.FERMENTER.get().getDefaultInstance()
            );
            noviceTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("red_wine"),
                    PleroCottageCompat.getPleroCottageItemStack("garnet")
            );
            noviceTrades(
                    event.getTrades(),
                    emerald(10),
                    PleroCottageCompat.getPleroCottageItemStack("glass_of_whiskey")
            );
            noviceTrades(
                    event.getTrades(),
                    emerald(10),
                    PleroCottageCompat.getPleroCottageItemStack("glass_of_rum")
            );
            noviceTrades(
                    event.getTrades(),
                    emerald(10),
                    PleroCottageCompat.getPleroCottageItemStack("glass_of_vodka")
            );
            apprenticeTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("hurricane_cocktail"),
                    PleroCottageCompat.getPleroCottageItemStack("garnet", 5)
            );
            apprenticeTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("garnet", 3),
                    PleroCottageCompat.getPleroCottageItemStack("grenadine")
            );
            apprenticeTrades(
                    event.getTrades(),
                    emerald(15),
                    PleroCottageCompat.getPleroCottageItemStack("daiquiri")
            );
            apprenticeTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("pink_crystal", 5),
                    PleroCottageCompat.getPleroCottageItemStack("gin")
            );
            journeymanTrades(
                    event.getTrades(),
                    emerald(32),
                    PleroCottageCompat.getPleroCottageItemStack("topaz", 16),
                    PleroCottageCompat.getPleroCottageItemStack("champagne")
            );
            journeymanTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("bellini_base"),
                    PleroCottageCompat.getPleroCottageItemStack("pink_crystal", 12)
            );
            journeymanTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("irish_coffee"),
                    PleroCottageCompat.getPleroCottageItemStack("topaz", 16)
            );
            expertTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("creme_de_cassis"),
                    emerald(24)
            );
            expertTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("umeshu"),
                    PleroCottageCompat.getPleroCottageItemStack("pink_crystal", 12)
            );
            expertTrades(
                    event.getTrades(),
                    emerald(32),
                    PleroCottageCompat.getPleroCottageItemStack("sake")
            );
            masterTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("starfruit_wine"),
                    PleroCottageCompat.getPleroCottageItemStack("brilliant_diamond")
            );
            masterTrades(
                    event.getTrades(),
                    PleroCottageCompat.getPleroCottageItemStack("kir_royal"),
                    PleroCottageCompat.getPleroCottageItemStack("pink_crystal", 16)
            );
            masterTrades(
                    event.getTrades(),
                    emerald(40),
                    PleroCottageCompat.getPleroCottageItemStack("white_wine")
            );
        }
    }

    public static ItemStack emerald() {
        return emerald(1);
    }

    public static ItemStack emerald(int count) {
        return Items.EMERALD.getDefaultInstance().copyWithCount(count);
    }

    public static void noviceTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, ItemStack costA, ItemStack costB, ItemStack result) {
        addTrades(trades, 1, costA, costB, result, 16, 2, 0.05F);
    }

    public static void noviceTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, ItemStack costA, ItemStack result) {
        addTrades(trades, 1, costA, ItemStack.EMPTY, result, 16, 2, 0.05F);
    }

    public static void apprenticeTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, ItemStack costA, ItemStack costB, ItemStack result) {
        addTrades(trades, 2, costA, costB, result, 16, 5, 0.05F);
    }

    public static void apprenticeTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, ItemStack costA, ItemStack result) {
        addTrades(trades, 2, costA, ItemStack.EMPTY, result, 16, 5, 0.05F);
    }

    public static void journeymanTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, ItemStack costA, ItemStack costB, ItemStack result) {
        addTrades(trades, 3, costA, costB, result, 12, 20, 0.05F);
    }

    public static void journeymanTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, ItemStack costA, ItemStack result) {
        addTrades(trades, 3, costA, ItemStack.EMPTY, result, 12, 20, 0.05F);
    }

    public static void expertTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, ItemStack costA, ItemStack costB, ItemStack result) {
        addTrades(trades, 4, costA, costB, result, 12, 20, 0.05F);
    }

    public static void expertTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, ItemStack costA, ItemStack result) {
        addTrades(trades, 4, costA, ItemStack.EMPTY, result, 12, 20, 0.05F);
    }

    public static void masterTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, ItemStack costA, ItemStack costB, ItemStack result) {
        addTrades(trades, 5, costA, costB, result, 12, 20, 0.05F);
    }

    public static void masterTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, ItemStack costA, ItemStack result) {
        addTrades(trades, 5, costA, ItemStack.EMPTY, result, 12, 20, 0.05F);
    }

    public static void addTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, int level, ItemStack costA, ItemStack costB, ItemStack result, int maxUses, int xp, float priceMultiplier) {
        trades.get(level).add((villager, randomSource) -> {
            if (!costB.isEmpty()) {
                return new MerchantOffer(costA, costB, result, maxUses, xp, priceMultiplier);
            } else {
                return new MerchantOffer(costA, result, maxUses, xp, priceMultiplier);
            }
        });
    }
}
