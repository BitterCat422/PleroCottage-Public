package net.bittercat.plerocottage.blocks;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class AbstractFruitLeavesBlock<S extends Enum<S> & StringRepresentable> extends LeavesBlock
    implements BonemealableBlock {

    public record Stages<S extends StringRepresentable>(S unrolled, S fruitless, S leaves, S flowers) {
    }

    public record Settings(MapColor mapColor, SoundType soundType, Supplier<Item> fruit, Supplier<Item> rareFruit,
                           Supplier<Item> sapling, int dropMin, int dropMax, int pickMin, int pickMax,
                           @Nullable Supplier<SimpleParticleType> leafParticle) {

        public boolean canBeRare() {
            return rareFruit != null;
        }
    }

    protected enum DropRoll {
        
        KEEP,
        
        DROP,
        
        CLEAR
    }

    private final EnumProperty<S> stageProperty;
    private final S initialStage;
    private final Stages<S> plan;
    private final Settings settings;

    protected AbstractFruitLeavesBlock(EnumProperty<S> stageProperty, S initialStage, Stages<S> plan,
                                       Settings settings) {
        super(BlockBehaviour.Properties.of()
            .mapColor(settings.mapColor())
            .sound(settings.soundType())
            .strength(0.2F)
            .lightLevel(state -> 0)
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false)
            .ignitedByLava()
            .isSuffocating((state, level, pos) -> false)
            .isViewBlocking((state, level, pos) -> false));
        this.stageProperty = stageProperty;
        this.initialStage = initialStage;
        this.plan = plan;
        this.settings = settings;
        registerDefaultState(stateDefinition.any()
            .setValue(stageProperty, initialStage)
            .setValue(PERSISTENT, false)
            .setValue(DISTANCE, 7)
            .setValue(WATERLOGGED, false));
    }

    public final Settings settings() {
        return settings;
    }

    protected final S stageOf(BlockState state) {
        return state.getValue(stageProperty);
    }

    public final List<BlockState> stageStates() {
        return stageProperty.getPossibleValues().stream()
            .map(stage -> defaultBlockState().setValue(stageProperty, stage))
            .toList();
    }

    public final String stageName(BlockState state) {
        return stageOf(state).getSerializedName();
    }

    public final String plainStageName() {
        return plan.leaves().getSerializedName();
    }

    public final boolean isRareStage(BlockState state) {
        return isRare(stageOf(state));
    }

    public final BlockState unrolledState() {
        return defaultBlockState().setValue(stageProperty, plan.unrolled());
    }

    protected final void setStage(LevelAccessor level, BlockPos pos, S stage) {
        BlockState next = level.getBlockState(pos).setValue(stageProperty, stage);
        level.setBlock(pos, next, 3);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(next));
    }

    @Override
    public final BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        
        return state.setValue(stageProperty, state.getValue(PERSISTENT) ? plan.leaves() : initialStage);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 0;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        Supplier<SimpleParticleType> particle = settings.leafParticle();
        if (particle == null || random.nextInt(10) != 0) {
            return;
        }
        BlockPos below = pos.below();
        if (isFaceFull(level.getBlockState(below).getCollisionShape(level, below), Direction.UP)) {
            return;
        }
        ParticleUtils.spawnParticleBelow(level, pos, random, particle.get());
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }

    protected boolean canPassThrough(Entity entity) {
        return entity instanceof ItemEntity || entity instanceof FallingBlockEntity;
    }

    @Override
    public final VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
                                              CollisionContext context) {
        if (context instanceof EntityCollisionContext entityContext
            && canPassThrough(entityContext.getEntity())) {
            return Shapes.empty();
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    protected final boolean isExposed(BlockGetter level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockState neighbour = level.getBlockState(pos.relative(direction));
            if (neighbour.canBeReplaced() || isOpenNeighbour(neighbour)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isOpenNeighbour(BlockState neighbour) {
        return false;
    }

    protected final void spreadBlossom(ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextDouble() < Config.leavesFlowerDecayChance()) {
            return;
        }
        SimpleWeightedRandomList.Builder<BlockPos> candidates = SimpleWeightedRandomList.builder();
        boolean found = false;
        for (Direction direction : Direction.values()) {
            BlockPos target = pos.relative(direction);
            BlockState targetState = level.getBlockState(target);
            if (targetState.getBlock() != this || targetState.getValue(PERSISTENT)) {
                continue;
            }
            if (stageOf(targetState) != plan.leaves()) {
                continue;
            }
            if (!isExposed(level, target)) {
                continue;
            }
            candidates.add(target, targetState.getValue(DISTANCE) + 2);
            found = true;
        }
        if (!found) {
            return;
        }
        candidates.build().getRandomValue(random).ifPresent(target -> setStage(level, target, plan.flowers()));
    }

    protected static DropRoll rollDrop(RandomSource random) {
        double chance = Config.leavesDropChance();
        if (chance > 0) {
            return random.nextDouble() < chance ? DropRoll.DROP : DropRoll.KEEP;
        }
        return chance == 0 ? DropRoll.KEEP : DropRoll.CLEAR;
    }

    protected final void playPickSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    protected final void giveHarvest(Player player, RandomSource random, Item item, int min, int max) {
        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(item, Mth.nextInt(random, min, max)));
    }

    protected final void dropHarvest(ServerLevel level, BlockPos pos, Item item, int min, int max) {
        Block.popResource(level, pos, new ItemStack(item, Mth.nextInt(level.getRandom(), min, max)));
    }

    protected final void playPickupSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    protected abstract boolean isRipe(S stage);

    protected boolean isRare(S stage) {
        return false;
    }

    protected abstract void grow(LevelAccessor level, BlockPos pos, BlockState state, RandomSource random);

    protected abstract void harvest(ServerLevel level, BlockPos pos, BlockState state, RandomSource random);

    protected abstract boolean harvestByHand(ServerLevel level, BlockPos pos, BlockState state, Player player);

    protected boolean repair(ServerLevel level, BlockPos pos, BlockState state) {
        return false;
    }

    protected double growthChance(S stage) {
        return stage == plan.flowers() ? Config.leavesFruitChance() : Config.leavesBlossomChance();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        if (state.getValue(PERSISTENT)) {
            
            return false;
        }
        
        return stageOf(state) != plan.fruitless() || super.isRandomlyTicking(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (level.getBlockState(pos).getBlock() != this) {
            return;
        }
        state = level.getBlockState(pos);
        if (state.getValue(PERSISTENT)) {
            return;
        }
        if (repair(level, pos, state)) {
            return;
        }
        if (!isExposed(level, pos)) {
            return;
        }

        S stage = stageOf(state);
        if (stage == plan.unrolled()) {
            
            setStage(level, pos, random.nextDouble() < Config.leavesCanFruitChance()
                ? plan.leaves() : plan.fruitless());
            return;
        }
        if (stage == plan.fruitless()) {
            return;
        }
        if (isRipe(stage)) {
            harvest(level, pos, state, random);
            return;
        }
        if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextDouble() < growthChance(stage))) {
            grow(level, pos, state, random);
            ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        if (state.getValue(PERSISTENT)) {
            
            return true;
        }
        S stage = stageOf(state);
        return stage != plan.fruitless() && !isRipe(stage);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (state.getValue(PERSISTENT)) {
            setStage(level, pos, stageOf(state) == plan.flowers() ? plan.leaves() : plan.flowers());
            return;
        }
        grow(level, pos, state, random);
    }

    @Override
    public final InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                       InteractionHand hand, BlockHitResult hit) {
        if (!isRipe(stageOf(state))) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            
            return InteractionResult.SUCCESS;
        }
        if (!harvestByHand((ServerLevel) level, pos, state, player)) {
            
            return InteractionResult.PASS;
        }
        playPickupSound(level, pos);
        return InteractionResult.CONSUME;
    }
}
