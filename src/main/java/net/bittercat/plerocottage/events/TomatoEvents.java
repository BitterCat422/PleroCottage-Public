package net.bittercat.plerocottage.events;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.capability.TomatoKnowledgeProvider;
import net.bittercat.plerocottage.content.food.FoodContent;
import net.bittercat.plerocottage.content.plants.DoubleCrops;
import net.bittercat.plerocottage.network.NetworkHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID)
public final class TomatoEvents {
    private TomatoEvents() {
    }

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            TomatoKnowledgeProvider provider = new TomatoKnowledgeProvider();
            event.addCapability(TomatoKnowledgeProvider.ID, provider);
            event.addListener(provider::invalidate);
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(TomatoKnowledgeProvider.CAPABILITY).ifPresent(oldKnowledge ->
            event.getEntity().getCapability(TomatoKnowledgeProvider.CAPABILITY).ifPresent(newKnowledge ->
                newKnowledge.copyFrom(oldKnowledge)));
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            sync(player);
        }
    }

    @SubscribeEvent
    public static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            sync(player);
        }
    }

    @SubscribeEvent
    public static void changedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            sync(player);
        }
    }

    @SubscribeEvent
    public static void finished(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !isTomato(event.getItem())) {
            return;
        }
        player.getCapability(TomatoKnowledgeProvider.CAPABILITY).ifPresent(knowledge -> {
            if (!knowledge.hasEatenTomato()) {
                knowledge.setHasEatenTomato(true);
                player.level().playSound(null, player.blockPosition(),
                    net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP,
                    net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
                NetworkHandler.syncTomatoKnowledge(player, true);
            }
        });
    }

    @Mod.EventBusSubscriber(modid = PleroCottage.MODID, value = Dist.CLIENT)
    public static final class Client {
        private Client() {
        }

        @SubscribeEvent
        public static void tooltip(ItemTooltipEvent event) {
            if (!isTomato(event.getItemStack()) || event.getEntity() == null) {
                return;
            }
            boolean known = event.getEntity().getCapability(TomatoKnowledgeProvider.CAPABILITY)
                .map(knowledge -> knowledge.hasEatenTomato()).orElse(false);
            List<Component> tooltip = event.getToolTip();
            if (known) {
                tooltip.add(Component.translatable("tips.plerocottage.tomato_tips_after"));
            } else {
                tooltip.add(Component.translatable("effect.minecraft.poison")
                    .append(" ? (??:??)")
                    .withStyle(ChatFormatting.RED));
            }
        }
    }

    private static boolean isTomato(ItemStack stack) {
        return stack.is(DoubleCrops.TOMATO.get())
            || stack.is(FoodContent.item("tomato_slice").get());
    }

    private static void sync(ServerPlayer player) {
        player.getCapability(TomatoKnowledgeProvider.CAPABILITY)
            .ifPresent(knowledge -> NetworkHandler.syncTomatoKnowledge(player, knowledge.hasEatenTomato()));
    }
}
