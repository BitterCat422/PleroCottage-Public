package net.bittercat.plerocottage.items;

import net.bittercat.plerocottage.client.renderer.model.StageFiveHatModels;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public final class WearableHatItem extends ArmorItem {
    private final String texture;

    public WearableHatItem(String name, int toughness, Properties properties) {
        super(new Material(name, toughness), Type.HELMET, properties);
        this.texture = name.substring(name.indexOf(':') + 1, name.length() - "_helmet".length());
    }

    @Override
    public String getArmorTexture(net.minecraft.world.item.ItemStack stack, net.minecraft.world.entity.Entity entity,
                                  EquipmentSlot slot, String type) {
        return "plerocottage:textures/entities/" + texture + ".png";
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private HumanoidModel<?> model;

            @Override
            @OnlyIn(Dist.CLIENT)
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living,
                    net.minecraft.world.item.ItemStack stack, EquipmentSlot slot, HumanoidModel<?> defaultModel) {
                if (model == null) {
                    ModelLayerLocation layer = switch (texture) {
                        case "chef_hat" -> StageFiveHatModels.CHEF_HAT;
                        case "christmas_hat" -> StageFiveHatModels.CHRISTMAS_HAT;
                        case "miner_hat" -> StageFiveHatModels.MINER_HAT;
                        case "pastry_chef_hat" -> StageFiveHatModels.PASTRY_CHEF_HAT;
                        default -> throw new IllegalStateException("Unknown stage-five hat: " + texture);
                    };
                    ModelPart hatHead = Minecraft.getInstance().getEntityModels().bakeLayer(layer).getChild("head");
                    model = new HumanoidModel<>(new ModelPart(Collections.emptyList(), Map.of(
                        "head", hatHead,
                        "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                }
                model.crouching = living.isShiftKeyDown();
                model.riding = defaultModel.riding;
                model.young = living.isBaby();
                return model;
            }
        });
    }

    private static final class Material implements ArmorMaterial {
        private final String name;
        private final int toughness;

        private Material(String name, int toughness) {
            this.name = name;
            this.toughness = toughness;
        }

        @Override public int getDurabilityForType(Type type) { return 0; }
        @Override public int getDefenseForType(Type type) { return type == Type.HELMET ? 2 : 0; }
        @Override public int getEnchantmentValue() { return 1; }
        @Override public net.minecraft.sounds.SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_LEATHER; }
        @Override public Ingredient getRepairIngredient() { return Ingredient.EMPTY; }
        @Override public String getName() { return name; }
        @Override public float getToughness() { return toughness; }
        @Override public float getKnockbackResistance() { return 0; }
    }
}
