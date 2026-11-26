package net.bittercat.plerocottage.items;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class BlockFoodItem extends BlockItem {

    private final int useDuration;
    private final boolean itemName;
    private final UseAnim useAnimation;
    private final Supplier<? extends Item> craftingRemainder;
    private final boolean placeWhenSneakingOnly;
    private final boolean placeSingleBlockWhenSneaking;

    public BlockFoodItem(Block block, Properties properties, int useDuration) {
        this(block, properties, useDuration, false);
    }

    public BlockFoodItem(Block block, Properties properties, int useDuration, boolean itemName) {
        this(block, properties, useDuration, itemName, UseAnim.EAT, null, false);
    }

    public BlockFoodItem(Block block, Properties properties, int useDuration, boolean itemName,
                         UseAnim useAnimation,
                         Supplier<? extends Item> craftingRemainder,
                         boolean placeWhenSneakingOnly) {
        this(block, properties, useDuration, itemName, useAnimation, craftingRemainder,
            placeWhenSneakingOnly, false);
    }

    public BlockFoodItem(Block block, Properties properties, int useDuration, boolean itemName,
                         UseAnim useAnimation,
                         Supplier<? extends Item> craftingRemainder,
                         boolean placeWhenSneakingOnly,
                         boolean placeSingleBlockWhenSneaking) {
        super(block, properties);
        this.useDuration = useDuration;
        this.itemName = itemName;
        this.useAnimation = useAnimation;
        this.craftingRemainder = craftingRemainder;
        this.placeWhenSneakingOnly = placeWhenSneakingOnly;
        this.placeSingleBlockWhenSneaking = placeSingleBlockWhenSneaking;
    }

    @Override
    public String getDescriptionId() {
        return itemName ? getOrCreateDescriptionId() : super.getDescriptionId();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useDuration;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return useAnimation;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return craftingRemainder != null;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return craftingRemainder == null ? ItemStack.EMPTY : new ItemStack(craftingRemainder.get());
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        ItemStack result = super.finishUsingItem(stack, level, consumer);
        if (craftingRemainder != null) {
            ItemStack remainder = new ItemStack(craftingRemainder.get());
            if (stack.isEmpty()) {
                return remainder;
            }
            if (consumer instanceof Player player && !player.getAbilities().instabuild
                && !player.getInventory().add(remainder)) {
                player.drop(remainder, false);
            }
        }
        return result;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (placeWhenSneakingOnly && (context.getPlayer() == null || !context.getPlayer().isShiftKeyDown())) {
            return InteractionResult.PASS;
        }
        if (placeSingleBlockWhenSneaking) {
            return placeSingleBlock(context);
        }
        return super.useOn(context);
    }

    private InteractionResult placeSingleBlock(UseOnContext context) {
        if (context.getPlayer() == null || !context.getPlayer().isShiftKeyDown()
            || context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        BlockPos targetPos = context.getClickedPos().relative(context.getClickedFace());
        if (!level.getBlockState(targetPos.below()).isFaceSturdy(level, targetPos.below(), Direction.UP)
            || !level.getBlockState(targetPos).canBeReplaced()) {
            return InteractionResult.PASS;
        }
        BlockState state = getBlock().defaultBlockState();
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            state = state.setValue(BlockStateProperties.HORIZONTAL_FACING,
                context.getPlayer().getDirection().getOpposite());
        }
        if (!level.isClientSide) {
            level.setBlock(targetPos, state, 3);
            if (!context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
            level.playSound(null, targetPos, state.getSoundType().getPlaceSound(),
                SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        ConsumableFoodSupport.appendFoodEffects(stack, tooltip);
    }
}
