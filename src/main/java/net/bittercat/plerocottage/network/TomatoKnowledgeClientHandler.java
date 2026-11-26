package net.bittercat.plerocottage.network;

import net.bittercat.plerocottage.capability.TomatoKnowledgeProvider;
import net.minecraft.client.Minecraft;

public final class TomatoKnowledgeClientHandler {
    private TomatoKnowledgeClientHandler() {
    }

    public static void handle(boolean hasEatenTomato) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            client.player.getCapability(TomatoKnowledgeProvider.CAPABILITY).ifPresent(knowledge ->
                knowledge.setHasEatenTomato(hasEatenTomato));
        }
    }
}
