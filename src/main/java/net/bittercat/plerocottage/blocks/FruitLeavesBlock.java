package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class FruitLeavesBlock extends AbstractFruitLeavesBlock<FruitLeavesBlock.Stage> {

    public static final EnumProperty<Stage> STAGE = EnumProperty.create("stage", Stage.class);

    private static final Stages<Stage> PLAN =
        new Stages<>(Stage.UNROLLED, Stage.FRUITLESS, Stage.LEAVES, Stage.FLOWERS);

    public FruitLeavesBlock(Settings settings) {
        super(STAGE, Stage.UNROLLED, PLAN, settings);
    }

    // 阶段属性必须在构造期就注册，此时实例字段还没赋值，所以只能由子类用静态的 STAGE 完成。
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
    }

    @Override
    protected boolean isRipe(Stage stage) {
        return stage == Stage.FRUITS || stage == Stage.RARE_FRUITS;
    }

    @Override
    protected boolean isRare(Stage stage) {
        return stage == Stage.RARE_FRUITS;
    }

    @Override
    protected void grow(LevelAccessor level, BlockPos pos, BlockState state, RandomSource random) {
        switch (stageOf(state)) {
            case UNROLLED -> setStage(level, pos, random.nextDouble() < Config.leavesCanFruitChance()
                ? Stage.LEAVES : Stage.FRUITLESS);
            case LEAVES -> setStage(level, pos, Stage.FLOWERS);
            case FLOWERS -> {
                boolean rare = settings().canBeRare() && random.nextDouble() < Config.rareVariantChance();
                setStage(level, pos, rare ? Stage.RARE_FRUITS : Stage.FRUITS);
                if (level instanceof ServerLevel serverLevel) {
                    spreadBlossom(serverLevel, pos, random);
                }
            }
            default -> {
                
            }
        }
    }

    @Override
    protected void harvest(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        switch (rollDrop(random)) {
            case KEEP -> {
                
            }
            case CLEAR -> setStage(level, pos, Stage.LEAVES);
            case DROP -> {
                Settings settings = settings();
                boolean rare = settings.canBeRare() && stageOf(state) == Stage.RARE_FRUITS;
                setStage(level, pos, Stage.LEAVES);
                dropHarvest(level, pos, (rare ? settings.rareFruit() : settings.fruit()).get(),
                    settings.dropMin(), settings.dropMax());
                playPickSound(level, pos);
            }
        }
    }

    @Override
    protected boolean harvestByHand(ServerLevel level, BlockPos pos, BlockState state, Player player) {
        Settings settings = settings();
        boolean rare = settings.canBeRare() && stageOf(state) == Stage.RARE_FRUITS;
        setStage(level, pos, Stage.LEAVES);
        giveHarvest(player, level.getRandom(), (rare ? settings.rareFruit() : settings.fruit()).get(),
            settings.pickMin(), settings.pickMax());
        playPickSound(level, pos);
        return true;
    }

    public enum Stage implements StringRepresentable {
        UNROLLED("unrolled"),
        FRUITLESS("fruitless"),
        LEAVES("leaves"),
        FLOWERS("flowers"),
        FRUITS("fruits"),
        RARE_FRUITS("rare_fruits");

        private final String name;

        Stage(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
