package net.bittercat.plerocottage.items;

import java.util.List;
import java.util.function.Predicate;

import net.bittercat.plerocottage.entities.ModBoat;
import net.bittercat.plerocottage.entities.ModChestBoat;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ModBoatItem extends Item {

    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

    private final String woodName;
    private final boolean chest;

    public ModBoatItem(String woodName, boolean chest, Item.Properties properties) {
        super(properties);
        this.woodName = woodName;
        this.chest = chest;
    }

    public String getWoodName() {
        return woodName;
    }

    public boolean isChestBoat() {
        return chest;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

        if (hit.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }

        Vec3 view = player.getViewVector(1.0F);
        List<Entity> nearby = level.getEntities(player,
            player.getBoundingBox().expandTowards(view.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
        if (!nearby.isEmpty()) {
            Vec3 eye = player.getEyePosition();
            for (Entity entity : nearby) {
                AABB box = entity.getBoundingBox().inflate(entity.getPickRadius());
                if (box.contains(eye)) {
                    return InteractionResultHolder.pass(stack);
                }
            }
        }

        // 客户端必须按原版规则返回 PASS，才能发送放置数据包。
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        Vec3 location = hit.getLocation();
        Boat boat;
        if (chest) {
            ModChestBoat chestBoat = new ModChestBoat(level, location.x, location.y, location.z);
            chestBoat.setWoodName(woodName);
            boat = chestBoat;
        } else {
            ModBoat modBoat = new ModBoat(level, location.x, location.y, location.z);
            modBoat.setWoodName(woodName);
            boat = modBoat;
        }
        boat.setYRot(player.getYRot());

        if (!level.noCollision(boat, boat.getBoundingBox())) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            level.addFreshEntity(boat);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, location);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
