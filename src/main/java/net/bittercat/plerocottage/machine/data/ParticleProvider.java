package net.bittercat.plerocottage.machine.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ParticleDescriptionProvider;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.init.ParticleTypeRegistry;

public class ParticleProvider extends ParticleDescriptionProvider {
    public ParticleProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions() {
        this.spriteSet(
                ParticleTypeRegistry.COSY_STEAM.get(),
                ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "steam"),
                12,
                false
        );
        this.spriteSet(
                ParticleTypeRegistry.OIL_SPLASH.get(),
                ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "oil_splash"),
                4,
                false
        );
    }
}
