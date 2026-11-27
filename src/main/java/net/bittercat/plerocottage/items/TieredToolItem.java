package net.bittercat.plerocottage.items;

import net.bittercat.plerocottage.events.AttackCharge;
import java.util.List;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.util.Mth;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class TieredToolItem extends SwordItem {
    private final int range;
    private final int burnTime;
    private final Item repairMaterial;

    public TieredToolItem(Tier tier, int attackDamage, float attackSpeed, Properties properties, int range) {
        this(tier, attackDamage, attackSpeed, properties, range, 0);
    }

    public TieredToolItem(Tier tier, int attackDamage, float attackSpeed, Properties properties,
                          int range, int burnTime) {
        this(tier, attackDamage, attackSpeed, properties, range, burnTime, null);
    }

    public TieredToolItem(Tier tier, int attackDamage, float attackSpeed, Properties properties,
                          int range, int burnTime, Item repairMaterial) {
        super(tier, attackDamage, attackSpeed, properties);
        this.range = range;
        this.burnTime = burnTime;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return repairMaterial != null ? repair.is(repairMaterial) : super.isValidRepairItem(stack, repair);
    }

    @Override
    public int getBurnTime(ItemStack stack, RecipeType<?> recipeType) {
        return burnTime;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (range > 0 && attacker instanceof Player player && !player.level().isClientSide) {
            if (result && player.level() instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
                double sweepX = -Mth.sin(player.getYRot() * ((float) Math.PI / 180.0F));
                double sweepZ = Mth.cos(player.getYRot() * ((float) Math.PI / 180.0F));
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                    player.getX() + sweepX, player.getY(0.5), player.getZ() + sweepZ,
                    0, sweepX, 0.0, sweepZ, 0.0);
            }
            for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class,
                    target.getBoundingBox().inflate(range / 2.0),
                    entity -> entity != player && entity != target && entity.isAlive()
                        && (!(entity instanceof TamableAnimal animal) || !animal.isTame()))) {
                float cooldown = AttackCharge.of(player);
                entity.hurt(player.damageSources().playerAttack(player),
                    (float) (player.getAttributeValue(
                        net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
                        * (0.2F + 0.8F * cooldown * cooldown)));
            }
        }
        return result;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return !(range > 0 && toolAction == ToolActions.SWORD_SWEEP)
            && super.canPerformAction(stack, toolAction);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return !(range > 0 && enchantment == Enchantments.SWEEPING_EDGE)
            && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip,
                                net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (range == 3 || range == 5) {
            tooltip.add(Component.translatable("tips.plerocottage.sickle_action"));
        }
    }
}
