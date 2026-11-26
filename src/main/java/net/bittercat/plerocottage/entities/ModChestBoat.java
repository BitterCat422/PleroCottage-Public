package net.bittercat.plerocottage.entities;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.entities.EntityContent;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
import net.bittercat.plerocottage.wood.WoodSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ModChestBoat extends ChestBoat {

    private static final String WOOD_TAG = "Wood";
    private static final EntityDataAccessor<String> DATA_WOOD =
        SynchedEntityData.defineId(ModChestBoat.class, EntityDataSerializers.STRING);

    public ModChestBoat(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public ModChestBoat(Level level, double x, double y, double z) {
        this(EntityContent.CHEST_BOAT.get(), level);
        setPos(x, y, z);
        xo = x;
        yo = y;
        zo = z;
    }

    @Override
    public Component getName() {
        if (hasCustomName() || WoodSetContent.byName(getWoodName()).isEmpty()) {
            return super.getName();
        }
        String suffix = WoodSetContent.byName(getWoodName()).map(set -> set.raft()
            ? "_chest_raft" : "_chest_boat").orElse("_chest_boat");
        return Component.translatable("entity." + PleroCottage.MODID + "." + getWoodName() + suffix);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_WOOD, WoodSetContent.all().isEmpty() ? "coconut_tree" : WoodSetContent.all().get(0).name());
    }

    public String getWoodName() {
        return entityData.get(DATA_WOOD);
    }

    public void setWoodName(String name) {
        WoodSetContent.byName(name).ifPresent(wood -> {
            entityData.set(DATA_WOOD, name);
            setVariant(wood.raft() ? Boat.Type.BAMBOO : Boat.Type.OAK);
        });
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString(WOOD_TAG, getWoodName());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(WOOD_TAG)) {
            setWoodName(tag.getString(WOOD_TAG));
        }
    }

    @Override
    public Item getDropItem() {
        return WoodSetContent.byName(getWoodName())
            .map(WoodSet::chestVehicleItem)
            .map(registryObject -> (Item) registryObject.get())
            .orElse(Items.OAK_CHEST_BOAT);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(getDropItem());
    }
}
