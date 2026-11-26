package net.bittercat.plerocottage.items;

import net.bittercat.plerocottage.entities.PoultryEggProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.registries.RegistryObject;

public final class PoultryEggItem extends BlockItem {
    private final String variant;
    private final RegistryObject<EntityType<PoultryEggProjectile>> projectileType;

    public PoultryEggItem(String variant, RegistryObject<EntityType<PoultryEggProjectile>> projectileType,
                          RegistryObject<Block> placeableBlock) {
        super(placeableBlock.get(), new Item.Properties().stacksTo(16));
        this.variant = variant;
        this.projectileType = projectileType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return super.use(level, player, hand);
        }
        if (projectileType == null) return InteractionResultHolder.pass(player.getItemInHand(hand));
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return super.useOn(context);
        }
        return InteractionResult.PASS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseDuration) {
        if (level.isClientSide) return;
        PoultryEggProjectile projectile = createProjectile(user, level);
        projectile.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 2.0F, 0.0F);
        level.addFreshEntity(projectile);
        level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.EGG_THROW,
            SoundSource.PLAYERS, 1.0F, 1.0F);
        if (user instanceof Player player && !player.getAbilities().instabuild) stack.shrink(1);
        user.releaseUsingItem();
    }

    private PoultryEggProjectile createProjectile(LivingEntity owner, Level level) {
        return new PoultryEggProjectile(projectileType.get(), owner, level, variant);
    }
}
