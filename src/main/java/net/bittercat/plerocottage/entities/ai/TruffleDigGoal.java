package net.bittercat.plerocottage.entities.ai;

import java.util.EnumSet;

import net.bittercat.plerocottage.content.plants.BlackDirts;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class TruffleDigGoal extends Goal {

    private static final int SEARCH_RANGE = 10;

    private static final int DIG_TIME = 40;

    private static final double REACH_DISTANCE_SQ = 4.0D;

    private final Pig pig;
    private BlockPos target;
    private int digTimer;
    private int searchCooldown;

    private static final int MAX_APPROACH_TICKS = 200;
    private int approachTicks;

    public TruffleDigGoal(Pig pig) {
        this.pig = pig;
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (pig.isBaby() || pig.isLeashed() || pig.isVehicle()) {
            return false;
        }
        if (searchCooldown > 0) {
            searchCooldown--;
            return false;
        }
        searchCooldown = 40 + pig.getRandom().nextInt(40);
        target = findNearestTruffle();
        return target != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (target == null || !pig.level().getBlockState(target).is(BlackDirts.BLACK_DIRT_TRUFFLE.get())) {
            return false;
        }
        return pig.distanceToSqr(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D) < 256.0D;
    }

    @Override
    public void start() {
        digTimer = 0;
        approachTicks = 0;
        moveToTarget();
    }

    @Override
    public void tick() {
        if (target == null) {
            return;
        }
        
        double dx = pig.getX() - (target.getX() + 0.5D);
        double dz = pig.getZ() - (target.getZ() + 0.5D);
        if (dx * dx + dz * dz > REACH_DISTANCE_SQ) {
            digTimer = 0;
            // 走不到的松露（隔着围栏等）不能一直占着移动，过一阵放弃并晚些再找
            if (++approachTicks > MAX_APPROACH_TICKS) {
                target = null;
                searchCooldown = 400;
                return;
            }
            if (pig.getNavigation().isDone()) {
                moveToTarget();
            }
            return;
        }

        pig.getLookControl().setLookAt(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D);
        pig.getNavigation().stop();
        digTimer++;
        if (digTimer % 10 == 0) {
            pig.level().playSound(null, pig.blockPosition(), SoundEvents.PIG_AMBIENT, SoundSource.NEUTRAL,
                0.6F, 1.2F);
        }
        if (digTimer >= DIG_TIME) {
            dig();
            stop();
        }
    }

    @Override
    public void stop() {
        target = null;
        digTimer = 0;
        pig.getNavigation().stop();
    }

    private void dig() {
        if (!(pig.level() instanceof ServerLevel level)) {
            return;
        }
        if (!level.getBlockState(target).is(BlackDirts.BLACK_DIRT_TRUFFLE.get())) {
            return;
        }
        level.setBlock(target, BlackDirts.BLACK_DIRT.get().defaultBlockState(), 3);
        level.levelEvent(null, 2001, target,
            Block.getId(BlackDirts.BLACK_DIRT_TRUFFLE.get().defaultBlockState()));

        float yaw = pig.getYRot();
        double offsetX = -Math.sin(Math.toRadians(yaw)) * (pig.getBbWidth() / 2.0D + 0.2D);
        double offsetZ = Math.cos(Math.toRadians(yaw)) * (pig.getBbWidth() / 2.0D + 0.2D);
        ItemEntity drop = new ItemEntity(level, pig.getX() + offsetX, pig.getY(), pig.getZ() + offsetZ,
            new ItemStack(BlackDirts.TRUFFLE.get(), Mth.nextInt(level.random, 1, 2)));
        drop.setPickUpDelay(10);
        level.addFreshEntity(drop);

        level.playSound(null, target, SoundEvents.ROOTED_DIRT_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F);
        level.playSound(null, pig.blockPosition(), SoundEvents.PIG_AMBIENT, SoundSource.NEUTRAL, 1.0F, 0.8F);
    }

    private void moveToTarget() {
        pig.getNavigation().moveTo(target.getX() + 0.5D, target.getY() + 1.0D, target.getZ() + 0.5D, 1.0D);
    }

    private BlockPos findNearestTruffle() {
        BlockPos origin = pig.blockPosition();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        BlockPos closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (int dx = -SEARCH_RANGE; dx <= SEARCH_RANGE; dx++) {
            for (int dz = -SEARCH_RANGE; dz <= SEARCH_RANGE; dz++) {
                if (dx * dx + dz * dz > closestDistance) {
                    continue;
                }
                for (int dy = -2; dy <= 2; dy++) {
                    cursor.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (!pig.level().getBlockState(cursor).is(BlackDirts.BLACK_DIRT_TRUFFLE.get())) {
                        continue;
                    }
                    double distance = pig.distanceToSqr(cursor.getX() + 0.5D, cursor.getY(), cursor.getZ() + 0.5D);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closest = cursor.immutable();
                        if (closestDistance <= REACH_DISTANCE_SQ) {
                            return closest;
                        }
                    }
                }
            }
        }
        return closest;
    }
}
