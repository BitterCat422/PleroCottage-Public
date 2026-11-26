package net.bittercat.plerocottage.machine.recipes;

public interface CookingTimeRecipe {
    int getMaxCookingTime();

    default boolean isCookingTimeMatch(int cookingTime) {
        return cookingTime >= this.getMaxCookingTime();
    }
}
