package net.bittercat.plerocottage.data;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import net.bittercat.plerocottage.content.entities.EntityContent;
import net.bittercat.plerocottage.content.food.FoodContent;
import net.bittercat.plerocottage.content.items.OrdinaryItemContent;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public final class PoultryLootTables extends EntityLootSubProvider {
    private final Set<EntityType<?>> knownEntities = new HashSet<>();

    public PoultryLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        output.accept(new ResourceLocation("plerocottage", "entities/mallard_duck"), LootTable.lootTable()
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(FoodContent.item("raw_mallard_duck").get())))
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(OrdinaryItemContent.DUCK_DOWN.get())
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
        output.accept(new ResourceLocation("plerocottage", "entities/turkey"), LootTable.lootTable()
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(FoodContent.item("raw_turkey").get())))
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(OrdinaryItemContent.TURKEY_FEATHER.get())
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
    }

    @Override
    public void generate() {
    }

    @Override
    protected boolean canHaveLootTable(EntityType<?> type) {
        return true;
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return knownEntities.stream();
    }

    @Override
    protected void add(EntityType<?> type, LootTable.Builder builder) {
        super.add(type, new ResourceLocation("plerocottage", "entities/" +
            net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(type).getPath()), builder);
        knownEntities.add(type);
    }
}
