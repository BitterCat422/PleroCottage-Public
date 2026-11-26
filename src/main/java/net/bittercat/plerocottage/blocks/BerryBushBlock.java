package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;
import java.util.function.Predicate;

import net.bittercat.plerocottage.config.Config;
import net.bittercat.plerocottage.data.BlockTagProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.PlantType;

public class BerryBushBlock extends BushBlock implements BonemealableBlock {

    public static final int MAX_GROWTH_AGE = 4;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_GROWTH_AGE);
    public static final BooleanProperty TRIMMED = BooleanProperty.create("trimmed");
    public static final BooleanProperty RARE = BooleanProperty.create("rare");

    private static final ResourceLocation SICKLES = new ResourceLocation("plerocottage", "tools/sickles");

    public static final Predicate<BlockState> DIRT_SOIL =
        state -> state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND);
    public static final Predicate<BlockState> DIRT_OR_END_STONE_SOIL =
        state -> state.is(BlockTags.DIRT) || state.is(Blocks.END_STONE);
    public static final Predicate<BlockState> FARMLAND_SOIL = state -> state.is(Blocks.FARMLAND);
    public static final Predicate<BlockState> END_STONE_SOIL = state -> state.is(Blocks.END_STONE);
    
    public static final Predicate<BlockState> NETHER_SOIL = state -> state.is(BlockTagProvider.NETHER_PLANTABLE);

    private final Settings settings;

    public BerryBushBlock(Settings settings) {
        this(settings, BlockBehaviour.Properties.of()
            .sound(SoundType.SWEET_BERRY_BUSH)
            .instabreak()
            .noCollission()
            .speedFactor(0.3F)
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .ignitedByLava());
    }

    public BerryBushBlock(Settings settings, BlockBehaviour.Properties properties) {
        super(properties);
        if (settings.maxAge() > MAX_GROWTH_AGE) {
            throw new IllegalArgumentException("Berry bush max age must be " + MAX_GROWTH_AGE
                + " or less, got " + settings.maxAge());
        }
        this.settings = settings;
        registerDefaultState(stateDefinition.any()
            .setValue(AGE, 0)
            .setValue(TRIMMED, false)
            .setValue(RARE, false));
    }

    public Settings settings() {
        return settings;
    }

    public IntegerProperty ageProperty() {
        return AGE;
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.CROP;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, TRIMMED, RARE);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return settings.soil().test(state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos soilPos = pos.below();
        BlockState soil = level.getBlockState(soilPos);
        return soil.canSustainPlant(level, soilPos, Direction.UP, this)
            && (level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape[] shapes = settings.shapes();
        return shapes[Math.min(state.getValue(AGE), shapes.length - 1)];
    }

    private BlockState advanceAge(BlockState state, ServerLevel level) {
        int age = state.getValue(AGE);
        BlockState grown = state.setValue(AGE, Math.min(age + 1, settings.maxAge()));
        if (age == settings.rareCheckAge()) {
            grown = grown.setValue(RARE,
                level.random.nextDouble() <= Config.rareVariantChance());
        }
        return grown;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < settings.maxAge() && !state.getValue(TRIMMED);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        if (state.getValue(AGE) >= settings.maxAge() || state.getValue(TRIMMED)) {
            return;
        }
        if (!ForgeHooks.onCropsGrowPre(level, pos, state,
                random.nextFloat() < settings.growthChance().chance(state, level, pos, random))) {
            return;
        }
        BlockState grown = advanceAge(state, level);
        level.setBlock(pos, grown, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(grown));
        ForgeHooks.onCropsGrowPost(level, pos, state);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(AGE) < settings.maxAge() && !state.getValue(TRIMMED);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (settings.spreadsWildly()) {
            WildSpread.spread(level, pos, state, random, settings.wildSpreadLimit());
            return;
        }
        BlockState grown = advanceAge(state, level);
        level.setBlock(pos, grown, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(grown));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        int age = state.getValue(AGE);

        if (held.is(Items.SHEARS) && age < settings.maxAge()) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(TRIMMED, !state.getValue(TRIMMED)), 2);
                level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
                held.hurtAndBreak(1, player, breaker -> breaker.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (age == settings.maxAge() && !held.is(ItemTags.create(SICKLES))) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(AGE, settings.harvestResetAge())
                    .setValue(TRIMMED, false).setValue(RARE, false), 2);
                // 采摘只给收获物，与原工作区一致：战利品表只管破坏方块，否则"种子一直掉落"的
                
                Item harvested = state.getValue(RARE) && settings.rareDrop() != null
                    ? settings.rareDrop().get()
                    : settings.drop().get();
                int count = Mth.nextInt(level.random, settings.dropMin(), settings.dropMax());
                dropHarvest(level, pos, new ItemStack(harvested, count));
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    protected void dropHarvest(Level level, BlockPos pos, ItemStack stack) {
        Block.popResource(level, pos, stack);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(settings.seed().get());
    }

    public record Settings(VoxelShape[] shapes, Supplier<Item> drop, Supplier<Item> seed, Supplier<Item> rareDrop,
                           int rareCheckAge, int maxAge, int harvestResetAge, int dropMin, int dropMax,
                           GrowthChance growthChance, boolean spreadsWildly, int wildSpreadLimit,
                           Predicate<BlockState> soil, boolean alwaysDropsSeed) {

        public Settings(VoxelShape[] shapes, Supplier<Item> drop, Supplier<Item> seed, Supplier<Item> rareDrop,
                        int rareCheckAge, int maxAge, int harvestResetAge, int dropMin, int dropMax,
                        float growthChance) {
            this(shapes, drop, seed, rareDrop, rareCheckAge, maxAge, harvestResetAge, dropMin, dropMax,
                GrowthChance.constant(growthChance), false, 10, DIRT_SOIL, false);
        }

        public Settings onSoil(Predicate<BlockState> soil) {
            return new Settings(shapes, drop, seed, rareDrop, rareCheckAge, maxAge, harvestResetAge, dropMin,
                dropMax, growthChance, spreadsWildly, wildSpreadLimit, soil, alwaysDropsSeed);
        }

        public Settings alwaysDroppingSeed() {
            return new Settings(shapes, drop, seed, rareDrop, rareCheckAge, maxAge, harvestResetAge, dropMin,
                dropMax, growthChance, spreadsWildly, wildSpreadLimit, soil, true);
        }
    }
}
