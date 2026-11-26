package net.bittercat.plerocottage.content.effects;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.effects.ModMobEffect;
import net.bittercat.plerocottage.init.PotionEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class MobEffectContent {

    public static final RegistryObject<MobEffect> ROSA_HEDGE = register("rosa_hedge", MobEffectCategory.NEUTRAL, -26215);
    public static final RegistryObject<MobEffect> RUTIN_LEMONENE = register("rutin_lemonene", MobEffectCategory.BENEFICIAL, -13261);
    public static final RegistryObject<MobEffect> BURSTING_BERRY = register("bursting_berry", MobEffectCategory.NEUTRAL, -39271);
    public static final RegistryObject<MobEffect> CHERRY_BLOSSOMS_WEEPING = register("cherry_blossoms_weeping", MobEffectCategory.BENEFICIAL, -32367);
    public static final RegistryObject<MobEffect> MOMENTARY_METEOR = register("momentary_meteor", MobEffectCategory.BENEFICIAL, -2816);
    public static final RegistryObject<MobEffect> SUMMER_HEATWAVE = register("summer_heatwave", MobEffectCategory.BENEFICIAL, -17408);
    public static final RegistryObject<MobEffect> ORIGINAL_EVOLUTION = register("original_evolution", MobEffectCategory.BENEFICIAL, -10592990);
    public static final RegistryObject<MobEffect> MELON_GRAVITY = register("melon_gravity", MobEffectCategory.BENEFICIAL, -7149951);
    public static final RegistryObject<MobEffect> SEA_TOUCH = register("sea_touch", MobEffectCategory.BENEFICIAL, -14572560);
    public static final RegistryObject<MobEffect> HACKED_THORNS = register("hacked_thorns", MobEffectCategory.BENEFICIAL, -3818659);
    public static final RegistryObject<MobEffect> KIWING_WHEREABOUTS = register("kiwing_whereabouts", MobEffectCategory.BENEFICIAL, -7274702);
    public static final RegistryObject<MobEffect> LURKING_DANGER = register("lurking_danger", MobEffectCategory.BENEFICIAL, -13679602);
    public static final RegistryObject<MobEffect> GEM_EXCAVATION = register("gem_excavation", MobEffectCategory.BENEFICIAL, -7267274);
    public static final RegistryObject<MobEffect> POISON_HEAL_MEDICINE = register("poison_heal_medicine", MobEffectCategory.BENEFICIAL, -5291454);
    public static final RegistryObject<MobEffect> HAPPY_AURA = register("happy_aura", MobEffectCategory.BENEFICIAL, -5576);
    public static final RegistryObject<MobEffect> DARK_WRATH = register("dark_wrath", MobEffectCategory.NEUTRAL, -9548946);
    public static final RegistryObject<MobEffect> TIPSY = register("tipsy", MobEffectCategory.NEUTRAL, -26113);
    public static final RegistryObject<MobEffect> INNER_FOCUS = register("inner_focus", MobEffectCategory.NEUTRAL, -6926309);
    public static final RegistryObject<MobEffect> ICE_BARRIER = register("ice_barrier", MobEffectCategory.BENEFICIAL, -2886672);
    public static final RegistryObject<MobEffect> SNOW_VEIL = register("snow_veil", MobEffectCategory.BENEFICIAL, -1);
    public static final RegistryObject<MobEffect> AURORA_VEIL = register("aurora_veil", MobEffectCategory.BENEFICIAL, -1);
    public static final RegistryObject<MobEffect> PURITY = register("purity", MobEffectCategory.BENEFICIAL, -4200571);
    public static final RegistryObject<MobEffect> FRAGRANT = register("fragrant", MobEffectCategory.BENEFICIAL, -4204438);
    public static final RegistryObject<MobEffect> MENTHOL_AWAKENING = register("menthol_awakening", MobEffectCategory.BENEFICIAL, 0x88D04D);
    public static final RegistryObject<MobEffect> WALLNUT_FIRST_AID = register("wallnut_first_aid", MobEffectCategory.BENEFICIAL, -4223446);
    public static final RegistryObject<MobEffect> TRANSMUTATION_ELIXIR = register("transmutation_elixir", MobEffectCategory.NEUTRAL, -5655061);
    public static final RegistryObject<MobEffect> FLATTEN = register("flatten", MobEffectCategory.HARMFUL, 0xE3AA58);

    private static RegistryObject<MobEffect> register(String name, MobEffectCategory category, int color) {
        return PotionEffectRegistry.MOB_EFFECTS.register(name, () -> new ModMobEffect(name, category, color));
    }

    private MobEffectContent() {
    }

    public static void bootstrap() {
    }

}
