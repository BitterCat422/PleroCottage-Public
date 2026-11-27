package net.bittercat.plerocottage.machine.util;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.level.block.entity.BlockEntity;
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
    private static final Map<BlockEntity, SoundInstance> LOOPS = new WeakHashMap<>();

    public static SoundManager soundManager() {
        return Minecraft.getInstance().getSoundManager();
    }

    public static void playShakerSoundInstance(Player player) {
        soundManager().play(new ShakerSoundInstance(player));
    }

    private static void playLoop(BlockEntity blockEntity, SoundInstance instance) {
        LOOPS.put(blockEntity, instance);
        soundManager().play(instance);
    }

    /** 循环音效是否还在播放：资源重载或离得太远时它会被停掉，而不经过实例自己的 tick。 */
    public static boolean isLoopActive(BlockEntity blockEntity) {
        SoundInstance instance = LOOPS.get(blockEntity);
        return instance != null && soundManager().isActive(instance);
    }

    public static void playStockPotSoundInstance(StockPotBlockEntity blockEntity) {
        playLoop(blockEntity, new StockPotSoundInstance(blockEntity));
    }

    public static void playFryerSoundInstance(FryerBlockEntity blockEntity) {
        playLoop(blockEntity, new FryerSoundInstance(blockEntity));
    }

    public static void playOvenSoundInstance(OvenBlockEntity blockEntity) {
        playLoop(blockEntity, new OvenSoundInstance(blockEntity));
    }
}
