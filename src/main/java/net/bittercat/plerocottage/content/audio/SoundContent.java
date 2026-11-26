package net.bittercat.plerocottage.content.audio;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.init.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

public final class SoundContent {
    public static final RegistryObject<SoundEvent> DUCK_AMBIENT = sound("entity.duck.ambient");
    public static final RegistryObject<SoundEvent> DUCK_HURT = sound("entity.duck.hurt");
    public static final RegistryObject<SoundEvent> DUCK_DEATH = sound("entity.duck.death");
    public static final RegistryObject<SoundEvent> DUCK_STEP = sound("entity.duck.step");
    public static final RegistryObject<SoundEvent> TURKEY_AMBIENT = sound("entity.turkey.ambient");
    public static final RegistryObject<SoundEvent> TURKEY_HURT = sound("entity.turkey.hurt");
    public static final RegistryObject<SoundEvent> TURKEY_DEATH = sound("entity.turkey.death");
    public static final RegistryObject<SoundEvent> TURKEY_STEP = sound("entity.turkey.step");
    public static final RegistryObject<SoundEvent> ROLLING_PIN_FLATTEN = sound("item.rolling_pin.flatten");
    public static final RegistryObject<SoundEvent> TRANSMUTATION_DAZZLING_GLEAM =
        sound("effect.transmutation.dazzling_gleam");
    public static final RegistryObject<SoundEvent> MOMENTARY_METEOR_STARDUST_SHATTER =
        sound("effect.momentary_meteor.stardust_shatter");

    private SoundContent() {
    }

    private static RegistryObject<SoundEvent> sound(String id) {
        return SoundRegistry.SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(
            new net.minecraft.resources.ResourceLocation(PleroCottage.MODID, id)));
    }

    public static void bootstrap() {
    }
}
