package net.bittercat.plerocottage.events;

import java.util.List;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blocks.BerryBushBlock;
import net.bittercat.plerocottage.blocks.DoubleCropBlock;
import net.bittercat.plerocottage.blocks.HangingFruitBlock;
import net.bittercat.plerocottage.blocks.StackableCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID)
public final class SickleInteractHandler {
    private static final ResourceLocation SICKLES = new ResourceLocation(PleroCottage.MODID, "tools/sickles");
    private static final ResourceLocation SICKLES_5X = new ResourceLocation(PleroCottage.MODID, "tools/sickles_5x");
    private static final ResourceLocation NOT_HARVESTABLE = new ResourceLocation(PleroCottage.MODID, "not_harvestable");
    private static final ResourceLocation SICKLE_DESTROY = new ResourceLocation(PleroCottage.MODID, "sickle_destroy");

    private SickleInteractHandler() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack tool = event.getItemStack();
        if (!tool.is(ItemTags.create(SICKLES))) {
            return;
        }
        BlockPos clicked = event.getPos();
        BlockState clickedState = event.getLevel().getBlockState(clicked);
        if (!isHarvestable(clickedState)) {
            return;
        }
        if (event.getLevel().isClientSide) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        int radius = tool.is(ItemTags.create(SICKLES_5X)) ? 2 : 1;
        if (clickedState.getBlock() instanceof DoubleCropBlock
                && clickedState.getValue(DoubleCropBlock.HALF) == DoubleBlockHalf.UPPER) {
            clicked = clicked.below();
            clickedState = event.getLevel().getBlockState(clicked);
        }
        BlockPos forward = forwardOffset(event.getEntity().getYRot(), radius);
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                harvestAt(event.getLevel(), clicked.offset(forward.getX() + dx, 0, forward.getZ() + dz),
                    clickedState.getBlock(), event.getEntity(), tool);
            }
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static boolean isHarvestable(BlockState state) {
        if (state.is(BlockTags.create(NOT_HARVESTABLE))) {
            return false;
        }
        if (state.is(BlockTags.create(SICKLE_DESTROY))) {
            return true;
        }
        Block block = state.getBlock();
        if (block instanceof HangingFruitBlock || block instanceof StackableCropBlock) {
            return true;
        }
        if (block instanceof BerryBushBlock bush) {
            return state.getValue(BerryBushBlock.AGE) >= bush.settings().maxAge();
        }
        if (block instanceof DoubleCropBlock crop) {
            return state.getValue(DoubleCropBlock.AGE) >= crop.settings().maxAge();
        }
        IntegerProperty age = ageProperty(state);
        return age != null && state.getValue(age).equals(age.getPossibleValues().stream().max(Integer::compareTo).orElse(0));
    }

    private static void harvestAt(net.minecraft.world.level.Level level, BlockPos pos, Block expectedBlock,
                                  Player player, ItemStack tool) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.create(NOT_HARVESTABLE))) {
            return;
        }
        if (state.getBlock() != expectedBlock) {
            return;
        }

        if (state.is(BlockTags.create(SICKLE_DESTROY))) {
            drop(serverLevel, pos, Block.getDrops(state, serverLevel, pos, null));
            serverLevel.removeBlock(pos, false);
            playHarvestSound(serverLevel, pos);
            damageTool(tool, player);
            return;
        }
        if (state.getBlock() instanceof HangingFruitBlock || state.getBlock() instanceof StackableCropBlock) {
            drop(serverLevel, pos, Block.getDrops(state, serverLevel, pos, null));
            serverLevel.removeBlock(pos, false);
            playHarvestSound(serverLevel, pos);
            damageTool(tool, player);
            return;
        }

        if (state.getBlock() instanceof BerryBushBlock bush) {
            if (state.getValue(BerryBushBlock.AGE) < bush.settings().maxAge()) {
                return;
            }
            var settings = bush.settings();
            boolean rare = state.getValue(BerryBushBlock.RARE) && settings.rareDrop() != null;
            var dropItem = rare ? settings.rareDrop().get() : settings.drop().get();
            int count = net.minecraft.util.Mth.nextInt(level.random, settings.dropMin(), settings.dropMax());
            level.setBlock(pos, state.setValue(BerryBushBlock.AGE, settings.harvestResetAge())
                .setValue(BerryBushBlock.TRIMMED, false).setValue(BerryBushBlock.RARE, false), 2);
            drop(serverLevel, pos, List.of(new ItemStack(dropItem, count)));
            playHarvestSound(serverLevel, pos);
            damageTool(tool, player);
            return;
        }

        if (state.getBlock() instanceof DoubleCropBlock crop) {
            if (state.getValue(DoubleCropBlock.HALF) != DoubleBlockHalf.LOWER
                    || state.getValue(DoubleCropBlock.AGE) < crop.settings().maxAge()) {
                return;
            }
            drop(serverLevel, pos, Block.getDrops(state, serverLevel, pos, null));
            BlockPos upper = pos.above();
            BlockState upperState = level.getBlockState(upper);
            level.setBlock(pos, state.setValue(DoubleCropBlock.AGE, crop.getHarvestAge()), 2);
            if (upperState.is(expectedBlock)) {
                level.setBlock(upper, upperState.setValue(DoubleCropBlock.AGE, crop.getHarvestAge()), 2);
            }
            playHarvestSound(serverLevel, pos);
            damageTool(tool, player);
            return;
        }

        IntegerProperty age = ageProperty(state);
        if (age != null && state.getValue(age).equals(age.getPossibleValues().stream().max(Integer::compareTo).orElse(0))) {
            drop(serverLevel, pos, Block.getDrops(state, serverLevel, pos, null));
            level.setBlock(pos, state.setValue(age, age.getPossibleValues().stream().min(Integer::compareTo).orElse(0)), 3);
            playHarvestSound(serverLevel, pos);
            damageTool(tool, player);
        }
    }

    private static IntegerProperty ageProperty(BlockState state) {
        return state.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty age ? age : null;
    }

    private static BlockPos forwardOffset(float yaw, int radius) {
        if (yaw >= -45 && yaw < 45) return new BlockPos(0, 0, radius);
        if (yaw >= 45 && yaw < 135) return new BlockPos(-radius, 0, 0);
        if (yaw >= 135 || yaw < -135) return new BlockPos(0, 0, -radius);
        return new BlockPos(radius, 0, 0);
    }

    private static void drop(ServerLevel level, BlockPos pos, List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                item.setPickUpDelay(10);
                level.addFreshEntity(item);
            }
        }
    }

    private static void playHarvestSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void damageTool(ItemStack tool, Player player) {
        if (!player.getAbilities().instabuild && tool.isDamageableItem()) {
            tool.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(eventHand(player, tool)));
        }
    }

    private static net.minecraft.world.InteractionHand eventHand(Player player, ItemStack tool) {
        return player.getMainHandItem() == tool
            ? net.minecraft.world.InteractionHand.MAIN_HAND : net.minecraft.world.InteractionHand.OFF_HAND;
    }
}
