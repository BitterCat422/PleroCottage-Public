package net.bittercat.plerocottage.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.bittercat.plerocottage.PleroCottage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TomatoKnowledgeProvider implements ICapabilityProvider,
    INBTSerializable<CompoundTag> {
    public static final Capability<TomatoKnowledge> CAPABILITY =
        CapabilityManager.get(new CapabilityToken<>() {});
    public static final ResourceLocation ID =
        new ResourceLocation(PleroCottage.MODID, TomatoKnowledge.ID);

    private final TomatoKnowledge knowledge = new TomatoKnowledge();
    private final LazyOptional<TomatoKnowledge> optional = LazyOptional.of(() -> knowledge);

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(TomatoKnowledge.class);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        return capability == CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        knowledge.save(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        knowledge.load(tag);
    }

    public void invalidate() {
        optional.invalidate();
    }
}
