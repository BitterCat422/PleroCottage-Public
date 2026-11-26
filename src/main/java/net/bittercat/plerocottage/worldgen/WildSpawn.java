package net.bittercat.plerocottage.worldgen;

public record WildSpawn(int spawn, WildGround ground) {

    public static WildSpawn soil(int spawn) {
        return new WildSpawn(spawn, WildGround.SOIL);
    }

    public static WildSpawn dirt(int spawn) {
        return new WildSpawn(spawn, WildGround.DIRT);
    }

    public static WildSpawn sand(int spawn) {
        return new WildSpawn(spawn, WildGround.SAND);
    }

    public static WildSpawn nether(int spawn) {
        return new WildSpawn(spawn, WildGround.NETHER);
    }

    public static WildSpawn endStone(int spawn) {
        return new WildSpawn(spawn, WildGround.END_STONE);
    }

    public static WildSpawn endAir(int spawn) {
        return new WildSpawn(spawn, WildGround.END_AIR);
    }
}
