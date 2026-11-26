package net.bittercat.plerocottage.blocks;

import net.minecraft.util.StringRepresentable;

public enum VineSpecies implements StringRepresentable {

    EMPTY("empty"),
    GRAPE("grapes"),
    PASSION_FRUIT("passion_fruit"),
    CUCUMBER("cucumber");

    private final String name;

    VineSpecies(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public String translationKey() {
        return "tips.plerocottage.vine_species." + name;
    }
}
