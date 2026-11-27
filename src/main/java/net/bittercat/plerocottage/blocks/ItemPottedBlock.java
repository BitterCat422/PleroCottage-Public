package net.bittercat.plerocottage.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID)
public class ItemPottedBlock extends FlowerPotBlock {
    private static final List<Mapping> MAPPINGS = new ArrayList<>();

    public ItemPottedBlock(Supplier<? extends Block> content, Supplier<? extends Item> item,
                           Supplier<? extends Block> pottedBlock) {
        super(() -> (FlowerPotBlock) Blocks.FLOWER_POT, content,
            BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor((state, level, pos) -> false));
        MAPPINGS.add(new Mapping(item, pottedBlock));
    }

    @Override
    public int getLightBlock(net.minecraft.world.level.block.state.BlockState state, BlockGetter level,
                             BlockPos pos) {
        return 0;
    }

    @SubscribeEvent
    public static void onRightClickPot(PlayerInteractEvent.RightClickBlock event) {
        // 这个事件在旁观模式的判断之前触发
        if (event.getEntity().isSpectator()) {
            return;
        }
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (level.getBlockState(pos).getBlock() != Blocks.FLOWER_POT) {
            return;
        }

        ItemStack stack = event.getItemStack();
        for (Mapping mapping : MAPPINGS) {
            if (stack.getItem() != mapping.item().get()) {
                continue;
            }

            Player player = event.getEntity();
            level.setBlock(pos, mapping.pottedBlock().get().defaultBlockState(), 3);
            player.awardStat(Stats.POT_FLOWER);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            event.setCanceled(true);
            return;
        }
    }

    private record Mapping(Supplier<? extends Item> item, Supplier<? extends Block> pottedBlock) {
    }
}
