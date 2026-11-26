package net.bittercat.plerocottage.worldgen;

public record WildBush(int chance, WildGround ground) {

    public static WildBush soil(int chance) {
        return new WildBush(chance, WildGround.DIRT);
    }

    public static WildBush endStone(int chance) {
        return new WildBush(chance, WildGround.END_STONE);
    }
}
