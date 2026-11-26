package net.bittercat.plerocottage.machine.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.entity.player.Player;
import net.bittercat.plerocottage.machine.blockentities.FryerBlockEntity;
import net.bittercat.plerocottage.machine.blockentities.OvenBlockEntity;
import net.bittercat.plerocottage.machine.blockentities.StockPotBlockEntity;
import net.bittercat.plerocottage.machine.client.sound.FryerSoundInstance;
import net.bittercat.plerocottage.machine.client.sound.OvenSoundInstance;
import net.bittercat.plerocottage.machine.client.sound.ShakerSoundInstance;
import net.bittercat.plerocottage.machine.client.sound.StockPotSoundInstance;

public class SoundUtil {
    public static SoundManager soundManager() {
        return Minecraft.getInstance().getSoundManager();
    }

    public static void playShakerSoundInstance(Player player) {
        soundManager().play(new ShakerSoundInstance(player));
    }

    public static void playStockPotSoundInstance(StockPotBlockEntity blockEntity) {
        soundManager().play(new StockPotSoundInstance(blockEntity));
    }

    public static void playFryerSoundInstance(FryerBlockEntity blockEntity) {
        soundManager().play(new FryerSoundInstance(blockEntity));
    }

    public static void playOvenSoundInstance(OvenBlockEntity blockEntity) {
        soundManager().play(new OvenSoundInstance(blockEntity));
    }
}
