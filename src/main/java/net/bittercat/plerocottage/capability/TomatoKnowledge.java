package net.bittercat.plerocottage.capability;

import net.minecraft.nbt.CompoundTag;

public final class TomatoKnowledge {
    public static final String ID = "tomato_knowledge";

    private boolean hasEatenTomato;

    public boolean hasEatenTomato() {
        return hasEatenTomato;
    }

    public void setHasEatenTomato(boolean value) {
        hasEatenTomato = value;
    }

    public void copyFrom(TomatoKnowledge other) {
        hasEatenTomato = other.hasEatenTomato;
    }

    public void save(CompoundTag tag) {
        tag.putBoolean(ID, hasEatenTomato);
    }

    public void load(CompoundTag tag) {
        hasEatenTomato = tag.getBoolean(ID);
    }
}
