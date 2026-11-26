package net.bittercat.plerocottage.content.wood;

import java.util.ArrayList;
import java.util.List;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class WoodTypeContent {

    private static final List<WoodType> ALL = new ArrayList<>();

    public static final WoodType COCONUT_TREE = create("coconut_tree");
    public static final WoodType KIWIFRUIT_TREE = create("kiwifruit_tree");
    public static final WoodType ELDERBERRY_TREE = create("elderberry_tree");
    public static final WoodType DURIAN_TREE = create("durian_tree");
    public static final WoodType AVOCADO_TREE = create("avocado_tree");
    public static final WoodType OLIVE_TREE = create("olive_tree");
    public static final WoodType MANGO_TREE = create("mango_tree");
    public static final WoodType FIG_TREE = create("fig_tree");
    public static final WoodType POMEGRANATE_TREE = create("pomegranate_tree");
    public static final WoodType STARFRUIT_TREE = create("starfruit_tree");
    public static final WoodType HAWTHORN_TREE = create("hawthorn_tree");
    public static final WoodType CHERRIES_TREE = create("cherries_tree");

    public static final WoodType ROSACEAE_TREE = create("rosaceae_tree");
    public static final WoodType RUTACEAE_TREE = create("rutaceae_tree");
    public static final WoodType NUT_TREE = create("nut_tree");

    public static final WoodType ALPINE_TREE = create("juniper");
    public static final WoodType HOLLY_TREE = create("holly_tree");
    public static final WoodType SCOTS_PINE = create("scots_pine");
    public static final WoodType MYRICLAVE_TREE = create("myriclave_tree");
    public static final WoodType CINNISE_TREE = create("cinnise_tree");
    public static final WoodType BANANA_TREE = create("banana_tree");

    private WoodTypeContent() {
    }

    private static WoodType create(String path) {
        String id = PleroCottage.MODID + ":" + path;
        if (path.equals("banana_tree")) {
            WoodType bamboo = WoodType.BAMBOO;
            WoodType woodType = WoodType.register(new WoodType(id, BlockSetType.BAMBOO,
                bamboo.soundType(), bamboo.hangingSignSoundType(),
                bamboo.fenceGateClose(), bamboo.fenceGateOpen()));
            ALL.add(woodType);
            return woodType;
        }
        BlockSetType blockSetType = path.equals("cherries_tree")
            ? BlockSetType.CHERRY : BlockSetType.register(new BlockSetType(id));
        WoodType woodType = path.equals("cherries_tree")
            ? WoodType.register(new WoodType(id, blockSetType, SoundType.CHERRY_WOOD,
                SoundType.CHERRY_WOOD_HANGING_SIGN, SoundEvents.CHERRY_WOOD_FENCE_GATE_CLOSE,
                SoundEvents.CHERRY_WOOD_FENCE_GATE_OPEN))
            : WoodType.register(new WoodType(id, blockSetType));
        ALL.add(woodType);
        return woodType;
    }

    public static List<WoodType> all() {
        return List.copyOf(ALL);
    }

    public static void bootstrap() {
    }

    public static String pathOf(WoodType woodType) {
        return new ResourceLocation(woodType.name()).getPath();
    }
}
