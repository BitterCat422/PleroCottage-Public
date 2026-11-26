package net.bittercat.plerocottage.wood;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.registries.RegistryObject;

public record WoodSet(
    String name,
    WoodType woodType,
    RegistryObject<RotatedPillarBlock> log,
    RegistryObject<RotatedPillarBlock> wood,
    RegistryObject<RotatedPillarBlock> strippedLog,
    RegistryObject<RotatedPillarBlock> strippedWood,
    RegistryObject<Block> planks,
    RegistryObject<StairBlock> stairs,
    RegistryObject<SlabBlock> slab,
    RegistryObject<FenceBlock> fence,
    RegistryObject<FenceGateBlock> fenceGate,
    RegistryObject<DoorBlock> door,
    RegistryObject<TrapDoorBlock> trapdoor,
    RegistryObject<PressurePlateBlock> pressurePlate,
    RegistryObject<ButtonBlock> button,
    RegistryObject<StandingSignBlock> sign,
    RegistryObject<WallSignBlock> wallSign,
    RegistryObject<CeilingHangingSignBlock> hangingSign,
    RegistryObject<WallHangingSignBlock> wallHangingSign,
    RegistryObject<Item> signItem,
    RegistryObject<Item> hangingSignItem,
    RegistryObject<Item> vehicleItem,
    RegistryObject<Item> chestVehicleItem,
    boolean stalkStyle,
    boolean raft
) {

    public String blockTexture(String suffix) {
        return name + suffix;
    }
}
