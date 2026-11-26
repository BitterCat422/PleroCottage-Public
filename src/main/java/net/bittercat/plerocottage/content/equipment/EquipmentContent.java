package net.bittercat.plerocottage.content.equipment;

import net.bittercat.plerocottage.items.TieredToolItem;
import net.bittercat.plerocottage.items.AnimalKnifeItem;
import net.bittercat.plerocottage.items.TieredSwordItem;
import net.bittercat.plerocottage.items.RollingPinItem;
import net.bittercat.plerocottage.items.WearableHatItem;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

public final class EquipmentContent {
    public static final RegistryObject<Item> WOODEN_SICKLE = sickle("wooden_sickle", Tiers.WOOD, 6, -3.4F, 3, 200);
    public static final RegistryObject<Item> STONE_SICKLE =
        sickle("stone_sickle", Tiers.STONE, 7, -3.4F, 3, 0, Items.COBBLESTONE);
    public static final RegistryObject<Item> IRON_SICKLE = sickle("iron_sickle", Tiers.IRON, 6, -3.3F, 3);
    public static final RegistryObject<Item> GOLDEN_SICKLE = sickle("golden_sickle", Tiers.GOLD, 6, -3.2F, 5);
    public static final RegistryObject<Item> DIAMOND_SICKLE = sickle("diamond_sickle", Tiers.DIAMOND, 5, -3.2F, 5);
    public static final RegistryObject<Item> NETHERITE_SICKLE =
        sickle("netherite_sickle", Tiers.NETHERITE, 5, -3.2F, 5);
    public static final RegistryObject<Item> WOODEN_ROLLING_PIN =
        rollingPin("wooden_rolling_pin", Tiers.WOOD, null, 200);
    public static final RegistryObject<Item> STONE_ROLLING_PIN =
        rollingPin("stone_rolling_pin", Tiers.STONE, Items.COBBLESTONE, 0);
    public static final RegistryObject<Item> IRON_ROLLING_PIN =
        rollingPin("iron_rolling_pin", Tiers.IRON, Items.IRON_INGOT, 0);
    public static final RegistryObject<Item> GOLDEN_ROLLING_PIN =
        rollingPin("golden_rolling_pin", Tiers.GOLD, Items.GOLD_INGOT, 0);
    public static final RegistryObject<Item> DIAMOND_ROLLING_PIN =
        rollingPin("diamond_rolling_pin", Tiers.DIAMOND, Items.DIAMOND, 0);
    public static final RegistryObject<Item> NETHERITE_ROLLING_PIN =
        rollingPin("netherite_rolling_pin", Tiers.NETHERITE, Items.NETHERITE_INGOT, 0);

    public static final RegistryObject<Item> WOODEN_FORK =
        fork("wooden_fork", Tiers.WOOD, 2, null, 200);
    public static final RegistryObject<Item> STONE_FORK =
        fork("stone_fork", Tiers.STONE, 3, Items.COBBLESTONE, 0);
    public static final RegistryObject<Item> IRON_FORK =
        fork("iron_fork", Tiers.IRON, 4, Items.IRON_INGOT, 0);
    public static final RegistryObject<Item> GOLDEN_FORK =
        fork("golden_fork", Tiers.GOLD, 2, Items.GOLD_INGOT, 0);
    public static final RegistryObject<Item> DIAMOND_FORK =
        fork("diamond_fork", Tiers.DIAMOND, 5, Items.DIAMOND, 0);
    public static final RegistryObject<Item> NETHERITE_FORK =
        fork("netherite_fork", Tiers.NETHERITE, 6, Items.NETHERITE_INGOT, 0);
    public static final RegistryObject<Item> WOODEN_CHOPSTICKS =
        swordTool("wooden_chopsticks", Tiers.WOOD, 2, null, 200);
    public static final RegistryObject<Item> STONE_CHOPSTICKS =
        swordTool("stone_chopsticks", Tiers.STONE, 3, Items.COBBLESTONE, 0);
    public static final RegistryObject<Item> IRON_CHOPSTICKS =
        swordTool("iron_chopsticks", Tiers.IRON, 4, Items.IRON_INGOT, 0);
    public static final RegistryObject<Item> GOLDEN_CHOPSTICKS =
        swordTool("golden_chopsticks", Tiers.GOLD, 2, Items.GOLD_INGOT, 0);
    public static final RegistryObject<Item> DIAMOND_CHOPSTICKS =
        swordTool("diamond_chopsticks", Tiers.DIAMOND, 5, Items.DIAMOND, 0);
    public static final RegistryObject<Item> NETHERITE_CHOPSTICKS =
        swordTool("netherite_chopsticks", Tiers.NETHERITE, 6, Items.NETHERITE_INGOT, 0);
    public static final RegistryObject<Item> BAMBOO_CHOPSTICKS =
        swordTool("bamboo_chopsticks", Tiers.IRON, 2, Items.BAMBOO_PLANKS, 0);

    public static final RegistryObject<Item> WOODEN_KNIFE =
        knife("wooden_knife", Tiers.WOOD, 6, -3.2F, Items.OAK_PLANKS, 200);
    public static final RegistryObject<Item> STONE_KNIFE =
        knife("stone_knife", Tiers.STONE, 7, -3.2F, Items.COBBLESTONE, 0);
    public static final RegistryObject<Item> IRON_KNIFE =
        knife("iron_knife", Tiers.IRON, 6, -3.1F, Items.IRON_INGOT, 0);
    public static final RegistryObject<Item> GOLDEN_KNIFE =
        knife("golden_knife", Tiers.GOLD, 6, -3.0F, Items.GOLD_INGOT, 0);
    public static final RegistryObject<Item> DIAMOND_KNIFE =
        knife("diamond_knife", Tiers.DIAMOND, 5, -3.0F, Items.DIAMOND, 0);
    public static final RegistryObject<Item> NETHERITE_KNIFE =
        knife("netherite_knife", Tiers.NETHERITE, 5, -3.0F, Items.NETHERITE_INGOT, 0);

    public static final RegistryObject<Item> CHEF_HAT = hat("chef_hat_helmet", 4);
    public static final RegistryObject<Item> CHRISTMAS_HAT = hat("christmas_hat_helmet", 4);
    public static final RegistryObject<Item> MINER_HAT = hat("miner_hat_helmet", 4);
    public static final RegistryObject<Item> PASTRY_CHEF_HAT = hat("pastry_chef_hat_helmet", 0);

    private EquipmentContent() {}

    public static void registerContent() {
        
    }

    private static RegistryObject<Item> hat(String id, int toughness) {
        return ItemRegistry.register(id, () -> new WearableHatItem(id, toughness, new Item.Properties()));
    }

    private static RegistryObject<Item> rollingPin(String id, Tier tier, Item repairMaterial, int burnTime) {
        return ItemRegistry.register(id, () -> new RollingPinItem(tier, repairMaterial, burnTime));
    }

    private static RegistryObject<Item> fork(String id, Tier tier, int attackDamage,
                                             Item repairMaterial, int burnTime) {
        return swordTool(id, tier, attackDamage, repairMaterial, burnTime);
    }

    private static RegistryObject<Item> swordTool(String id, Tier tier, int attackDamage,
                                                  Item repairMaterial, int burnTime) {
        return ItemRegistry.register(id, () -> new TieredSwordItem(tier, attackDamage, repairMaterial, burnTime));
    }

    private static RegistryObject<Item> knife(String id, Tier tier, int attackDamage, float attackSpeed,
                                              Item repairMaterial, int burnTime) {
        return ItemRegistry.register(id, () -> new AnimalKnifeItem(tier, attackDamage, attackSpeed,
            new Item.Properties().durability(tier.getUses()), repairMaterial, burnTime));
    }

    private static RegistryObject<Item> sickle(String id, Tier tier, int attackDamage, float attackSpeed, int range) {
        return sickle(id, tier, attackDamage, attackSpeed, range, 0);
    }

    private static RegistryObject<Item> sickle(String id, Tier tier, int attackDamage, float attackSpeed,
                                               int range, int burnTime) {
        return ItemRegistry.register(id, () -> new TieredToolItem(tier, attackDamage, attackSpeed,
            new Item.Properties().durability(tier.getUses()), range, burnTime));
    }

    private static RegistryObject<Item> sickle(String id, Tier tier, int attackDamage, float attackSpeed,
                                               int range, int burnTime, Item repairMaterial) {
        return ItemRegistry.register(id, () -> new TieredToolItem(tier, attackDamage, attackSpeed,
            new Item.Properties().durability(tier.getUses()), range, burnTime, repairMaterial));
    }

}
