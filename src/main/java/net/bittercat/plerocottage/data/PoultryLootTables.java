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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public final class PoultryLootTables extends EntityLootSubProvider {
    private final Set<EntityType<?>> knownEntities = new HashSet<>();

    public PoultryLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        output.accept(new ResourceLocation("plerocottage", "entities/mallard_duck"),
            poultry(OrdinaryItemContent.DUCK_DOWN.get(), FoodContent.item("raw_mallard_duck").get()));
        output.accept(new ResourceLocation("plerocottage", "entities/turkey"),
            poultry(OrdinaryItemContent.TURKEY_FEATHER.get(), FoodContent.item("raw_turkey").get()));
    }

    /** 写法同原版鸡：羽毛与生肉都受抢夺加成，着火死亡时生肉按熔炉配方变为熟肉。 */
    private static LootTable.Builder poultry(ItemLike feather, ItemLike meat) {
        return LootTable.lootTable()
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(feather)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(meat)
                    .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))));
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
