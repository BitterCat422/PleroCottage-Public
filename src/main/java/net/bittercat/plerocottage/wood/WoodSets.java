package net.bittercat.plerocottage.wood;

import net.bittercat.plerocottage.blocks.ModCeilingHangingSignBlock;
import net.bittercat.plerocottage.blocks.ModStandingSignBlock;
import net.bittercat.plerocottage.blocks.ModWallHangingSignBlock;
import net.bittercat.plerocottage.blocks.ModWallSignBlock;
import net.bittercat.plerocottage.blocks.StrippablePillarBlock;
import net.bittercat.plerocottage.items.ModBoatItem;
import net.bittercat.plerocottage.content.wood.WoodTypeContent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class WoodSets {

    private WoodSets() {
    }

    public static WoodSet create(DeferredRegister<Block> blocks, DeferredRegister<Item> items,
                                 WoodSetSpec spec) {

        WoodType woodType = spec.woodType();
        String name = spec.name();
        BlockSetType setType = woodType.setType();
        SoundType soundType = spec.soundType();

        String pillarSuffix = spec.stalkStyle() ? "_stalk" : "_log";
        String strippedPillarSuffix = "stripped_" + name + (spec.stalkStyle() ? "_stalk" : "_log");
        RegistryObject<RotatedPillarBlock> strippedLog =
            blocks.register(strippedPillarSuffix, () -> new RotatedPillarBlock(pillar(
                spec.stalkStyle() ? MapColor.PLANT : MapColor.WOOD, soundType)));
        RegistryObject<RotatedPillarBlock> strippedWood = spec.stalkStyle() ? null
            : blocks.register("stripped_" + name + "_wood",
                () -> new RotatedPillarBlock(pillar(MapColor.WOOD, soundType)));
        RegistryObject<RotatedPillarBlock> log = blocks.register(name + pillarSuffix,
            () -> new StrippablePillarBlock(pillar(
                spec.stalkStyle() ? MapColor.PLANT : MapColor.WOOD, soundType), strippedLog::get));
        RegistryObject<RotatedPillarBlock> wood = spec.stalkStyle() ? null
            : blocks.register(name + "_wood",
                () -> new StrippablePillarBlock(pillar(MapColor.WOOD, soundType), strippedWood::get));

        RegistryObject<Block> planks =
            blocks.register(name + "_planks", () -> new Block(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(soundType)
                .ignitedByLava()));

        RegistryObject<StairBlock> stairs = blocks.register(name + "_stairs",
            () -> new StairBlock(planks.get().defaultBlockState(), BlockBehaviour.Properties.copy(planks.get())));

        RegistryObject<SlabBlock> slab = blocks.register(name + "_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(planks.get())));

        RegistryObject<FenceBlock> fence =
            blocks.register(name + "_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(planks.get())
                .forceSolidOn()));

        RegistryObject<FenceGateBlock> fenceGate =
            blocks.register(name + "_fence_gate", () -> new FenceGateBlock(
                BlockBehaviour.Properties.copy(planks.get()).forceSolidOn(), woodType));

        RegistryObject<DoorBlock> door =
            blocks.register(name + "_door", () -> new DoorBlock(BlockBehaviour.Properties.of()
                .mapColor(planks.get().defaultMapColor())
                .strength(3.0F)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .sound(soundType)
                .ignitedByLava(), setType));

        RegistryObject<TrapDoorBlock> trapdoor =
            blocks.register(name + "_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(3.0F)
                .noOcclusion()
                .isValidSpawn((state, level, pos, type) -> false)
                .sound(soundType)
                .ignitedByLava(), setType));

        RegistryObject<PressurePlateBlock> pressurePlate =
            blocks.register(name + "_pressure_plate", () -> new PressurePlateBlock(
                PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of()
                    .mapColor(planks.get().defaultMapColor())
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollission()
                    .strength(0.5F)
                    .sound(soundType)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY), setType));

        RegistryObject<ButtonBlock> button =
            blocks.register(name + "_button", () -> new ButtonBlock(BlockBehaviour.Properties.of()
                .noCollission()
                .strength(0.5F)
                .sound(soundType)
                .pushReaction(PushReaction.DESTROY), setType, 30, true));

        SoundType signSoundType = woodType == WoodTypeContent.CHERRIES_TREE
            ? SoundType.CHERRY_WOOD : soundType;
        SoundType hangingSignSoundType = woodType == WoodTypeContent.CHERRIES_TREE
            ? SoundType.CHERRY_WOOD_HANGING_SIGN
            : soundType == SoundType.BAMBOO_WOOD ? woodType.hangingSignSoundType() : soundType;
        RegistryObject<StandingSignBlock> sign = blocks.register(name + "_sign",
            () -> new ModStandingSignBlock(signProperties(planks.get().defaultMapColor(), signSoundType), woodType));
        RegistryObject<WallSignBlock> wallSign = blocks.register(name + "_wall_sign",
            () -> new ModWallSignBlock(signProperties(planks.get().defaultMapColor(), signSoundType).dropsLike(sign.get()), woodType));
        RegistryObject<CeilingHangingSignBlock> hangingSign = blocks.register(name + "_hanging_sign",
            () -> new ModCeilingHangingSignBlock(signProperties(planks.get().defaultMapColor(),
                hangingSignSoundType), woodType));
        RegistryObject<WallHangingSignBlock> wallHangingSign = blocks.register(name + "_wall_hanging_sign",
            () -> new ModWallHangingSignBlock(signProperties(planks.get().defaultMapColor(),
                hangingSignSoundType)
                .dropsLike(hangingSign.get()), woodType));

        RegistryObject<Item> signItem = items.register(name + "_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), sign.get(), wallSign.get()));

        RegistryObject<Item> hangingSignItem = items.register(name + "_hanging_sign",
            () -> new HangingSignItem(hangingSign.get(), wallHangingSign.get(),
                new Item.Properties().stacksTo(16)));

        String vehicle = spec.raft() ? "raft" : "boat";
        RegistryObject<Item> boatItem = items.register(name + "_" + vehicle,
            () -> new ModBoatItem(name, false, new Item.Properties().stacksTo(1)));
        RegistryObject<Item> chestBoatItem = items.register(name + "_chest_" + vehicle,
            () -> new ModBoatItem(name, true, new Item.Properties().stacksTo(1)));

        registerBlockItem(items, name + pillarSuffix, log);
        if (!spec.stalkStyle()) {
            registerBlockItem(items, name + "_wood", wood);
        }
        registerBlockItem(items, strippedPillarSuffix, strippedLog);
        if (!spec.stalkStyle()) {
            registerBlockItem(items, "stripped_" + name + "_wood", strippedWood);
        }
        registerBlockItem(items, name + "_planks", planks);
        registerBlockItem(items, name + "_stairs", stairs);
        registerBlockItem(items, name + "_slab", slab);
        registerBlockItem(items, name + "_fence", fence);
        registerBlockItem(items, name + "_fence_gate", fenceGate);
        registerBlockItem(items, name + "_door", door);
        registerBlockItem(items, name + "_trapdoor", trapdoor);
        registerBlockItem(items, name + "_pressure_plate", pressurePlate);
        registerBlockItem(items, name + "_button", button);

        return new WoodSet(name, woodType, log, wood, strippedLog, strippedWood, planks, stairs, slab, fence,
            fenceGate, door, trapdoor, pressurePlate, button, sign, wallSign, hangingSign, wallHangingSign,
            signItem, hangingSignItem, boatItem, chestBoatItem, spec.stalkStyle(), spec.raft());
    }

    private static void registerBlockItem(DeferredRegister<Item> items, String name, RegistryObject<? extends Block> block) {
        items.register(name, () -> new net.minecraft.world.item.BlockItem(block.get(), new Item.Properties()));
    }

    private static BlockBehaviour.Properties pillar(MapColor mapColor, SoundType soundType) {
        return BlockBehaviour.Properties.of()
            .mapColor(mapColor)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(soundType)
            .ignitedByLava();
    }

    private static BlockBehaviour.Properties signProperties(MapColor mapColor, SoundType soundType) {
        return BlockBehaviour.Properties.of()
            .mapColor(mapColor)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0F)
            .sound(soundType)
            .ignitedByLava();
    }

}
