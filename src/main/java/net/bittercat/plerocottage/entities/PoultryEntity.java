package net.bittercat.plerocottage.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.bittercat.plerocottage.content.entities.EntityContent;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.content.core.CoreContent;
import net.bittercat.plerocottage.content.items.OrdinaryItemContent;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.bittercat.plerocottage.content.entities.PoultryContent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class PoultryEntity extends Animal implements GeoEntity {
    private final String species;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int eggTimer;
    private static final int MAX_EGG_LAY_TIME = 9000;
    private static final int SEARCH_START_THRESHOLD = 2700;
    private static final int SEARCH_INTERVAL = 60;

    public PoultryEntity(EntityType<? extends Animal> type, Level level, String species) {
        super(type, level);
        this.species = species;
        this.xpReward = 2;
        this.eggTimer = MAX_EGG_LAY_TIME;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(ItemTags.create(new ResourceLocation("forge", "seeds"))), false));
        goalSelector.addGoal(4, new FollowParentGoal(this, 0.8D));
        goalSelector.addGoal(5, new FindNearestNestGoal(this, 1.2D));
        goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8D));
        goalSelector.addGoal(7, new RandomSwimmingGoal(this, 1.0D, 40));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide && !isBaby() && eggTimer > 0) {
            eggTimer--;
        } else if (!level().isClientSide && !isBaby()) {
            ItemStack egg = switch (species) {
                case "mallard_duck" -> new ItemStack(PoultryContent.MALLARD_EGG.get());
                case "turkey" -> new ItemStack(PoultryContent.TURKEY_EGG.get());
                default -> new ItemStack(PoultryContent.MALLARD_EGG.get());
            };
            BlockPos below = BlockPos.containing(getX(), getY() - 0.1D, getZ());
            BlockEntity nest = level().getBlockEntity(below);
            boolean placed = nest != null && level().getBlockState(below).is(CoreContent.HAY_NEST.get())
                && nest.getCapability(ForgeCapabilities.ITEM_HANDLER).map(handler -> {
                    if (!(handler instanceof IItemHandlerModifiable editable)) return false;
                    for (int slot = 0; slot < Math.min(3, editable.getSlots()); slot++) {
                        if (editable.getStackInSlot(slot).isEmpty()) {
                            editable.setStackInSlot(slot, egg);
                            nest.setChanged();
                            return true;
                        }
                    }
                    return false;
                }).orElse(false);
            float pitch = 0.8F + random.nextFloat() * 0.4F;
            if (!placed) {
                spawnAtLocation(egg);
                level().playSound(null, blockPosition(), SoundEvents.CHICKEN_EGG,
                    SoundSource.NEUTRAL, 0.5F, pitch);
            } else {
                level().playSound(null, below, SoundEvents.CHICKEN_EGG,
                    SoundSource.NEUTRAL, 0.5F, pitch);
            }
            eggTimer = MAX_EGG_LAY_TIME;
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        PoultryEntity child = switch (species) {
            case "mallard_duck" -> EntityContent.MALLARD_DUCK.get().create(level);
            case "turkey" -> EntityContent.TURKEY.get().create(level);
            default -> EntityContent.MALLARD_DUCK.get().create(level);
        };
        return child;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.create(new ResourceLocation("forge", "seeds")));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !source.is(DamageTypes.FALL) && super.hurt(source, amount);
    }

    @Override
    public SoundEvent getAmbientSound() {
        String sound = species.equals("turkey") ? "entity.turkey.ambient" : "entity.duck.ambient";
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("plerocottage", sound));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        String sound = species.equals("turkey") ? "entity.turkey.hurt" : "entity.duck.hurt";
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("plerocottage", sound));
    }

    @Override
    protected SoundEvent getDeathSound() {
        String sound = species.equals("turkey") ? "entity.turkey.death" : "entity.duck.death";
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("plerocottage", sound));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        String sound = species.equals("turkey") ? "entity.turkey.step" : "entity.duck.step";
        playSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("plerocottage", sound)),
            0.15F, 1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D)
            .add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    public String species() {
        return species;
    }

    public int eggTimer() {
        return eggTimer;
    }

    public String textureName() {
        return isBaby() ? "baby_" + species : species;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 4, state -> {
            String prefix = species.equals("mallard_duck") ? "mallard_duck" : species;
            String animation = !onGround() ? "air_flying"
                : state.isMoving() ? "ground_walk" : "ground_idle";
            return state.setAndContinue(RawAnimation.begin().thenLoop("animation." + prefix + "." + animation));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("PoultryEggTimer", eggTimer);
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("PoultryEggTimer")) eggTimer = tag.getInt("PoultryEggTimer");
    }

    private static final class FindNearestNestGoal extends Goal {
        private final PoultryEntity poultry;
        private final double speed;
        private BlockPos targetNest;
        private int searchCooldown;
        private static final int SEARCH_RANGE = 16;

        private FindNearestNestGoal(PoultryEntity poultry, double speed) {
            this.poultry = poultry;
            this.speed = speed;
        }

        @Override
        public boolean canUse() {
            if (poultry.isBaby() || poultry.eggTimer <= 0 || poultry.eggTimer > SEARCH_START_THRESHOLD) {
                return false;
            }
            if (searchCooldown > 0) {
                searchCooldown--;
                return false;
            }
            BlockPos below = poultry.blockPosition().below();
            if (poultry.level().getBlockState(below).is(CoreContent.HAY_NEST.get())) {
                return false;
            }
            targetNest = findNearestNest();
            if (targetNest == null) {
                searchCooldown = SEARCH_INTERVAL;
                return false;
            }
            searchCooldown = 20;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return targetNest != null && !poultry.isBaby() && poultry.eggTimer > 0
                && poultry.level().getBlockState(targetNest)
                .is(CoreContent.HAY_NEST.get());
        }

        @Override
        public void start() {
            moveToNest();
        }

        @Override
        public void tick() {
            if (poultry.distanceToSqr(targetNest.getX() + 0.5, targetNest.getY(),
                targetNest.getZ() + 0.5) < 1.0D) {
                poultry.teleportTo(targetNest.getX() + 0.5, targetNest.getY() + 0.25,
                    targetNest.getZ() + 0.5);
                fillFeatherSlots();
                poultry.eggTimer = 1;
                stop();
            } else {
                moveToNest();
            }
        }

        private void moveToNest() {
            poultry.getNavigation().moveTo(targetNest.getX() + 0.5, targetNest.getY(),
                targetNest.getZ() + 0.5, speed);
        }

        private void fillFeatherSlots() {
            BlockEntity blockEntity = poultry.level().getBlockEntity(targetNest);
            if (blockEntity == null) return;
            ItemStack feather = poultry.species.equals("mallard_duck")
                ? new ItemStack(OrdinaryItemContent.DUCK_DOWN.get())
                : poultry.species.equals("turkey")
                    ? new ItemStack(OrdinaryItemContent.TURKEY_FEATHER.get())
                    : new ItemStack(Items.FEATHER);
            blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                if (!(handler instanceof IItemHandlerModifiable editable)) return;
                for (int slot = 3; slot < Math.min(6, editable.getSlots()); slot++) {
                    if (editable.getStackInSlot(slot).isEmpty()) {
                        editable.setStackInSlot(slot, feather.copy());
                    }
                }
                blockEntity.setChanged();
            });
        }

        @Override
        public void stop() {
            poultry.getNavigation().stop();
            targetNest = null;
        }

        private BlockPos findNearestNest() {
            BlockPos origin = poultry.blockPosition();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            BlockPos closest = null;
            double closestDistance = Double.MAX_VALUE;
            for (int dx = -SEARCH_RANGE; dx <= SEARCH_RANGE; dx++) {
                for (int dz = -SEARCH_RANGE; dz <= SEARCH_RANGE; dz++) {
                    for (int dy = -4; dy <= 4; dy++) {
                        mutable.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                        if (poultry.level().getBlockState(mutable)
                            .is(CoreContent.HAY_NEST.get())
                            && hasEmptyEggSlot(mutable)) {
                            double distance = poultry.distanceToSqr(mutable.getX() + 0.5,
                                mutable.getY() + 1.0, mutable.getZ() + 0.5);
                            if (distance < closestDistance) {
                                closestDistance = distance;
                                closest = mutable.immutable();
                            }
                        }
                    }
                }
            }
            return closest;
        }

        private boolean hasEmptyEggSlot(BlockPos pos) {
            BlockEntity blockEntity = poultry.level().getBlockEntity(pos);
            if (blockEntity == null) return false;
            return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).map(handler -> {
                for (int slot = 0; slot < Math.min(3, handler.getSlots()); slot++) {
                    if (handler.getStackInSlot(slot).isEmpty()) return true;
                }
                return false;
            }).orElse(false);
        }
    }
}
