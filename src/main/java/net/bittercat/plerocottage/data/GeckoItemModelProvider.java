package net.bittercat.plerocottage.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.items.GeckoDisplayFoodItem;
import net.bittercat.plerocottage.content.food.FoodDisplayTransform;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class GeckoItemModelProvider implements DataProvider {
    private final PackOutput output;

    public GeckoItemModelProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> writes = new ArrayList<>();
        for (RegistryObject<Item> entry : ItemRegistry.ITEMS.getEntries()) {
            if (!(entry.get() instanceof GeckoDisplayFoodItem food)) {
                continue;
            }
            String name = entry.getId().getPath();
            JsonObject model = new JsonObject();
            model.addProperty("parent", "minecraft:builtin/entity");
            JsonObject textures = new JsonObject();
            textures.addProperty("particle", PleroCottage.MODID + ":item/" + name);
            model.add("textures", textures);
            model.add("display", display(food.transforms()));

            Path path = output.getOutputFolder().resolve("assets/" + PleroCottage.MODID
                + "/models/item/" + name + ".json");
            writes.add(DataProvider.saveStable(cachedOutput, model, path));
        }
        return CompletableFuture.allOf(writes.toArray(CompletableFuture<?>[]::new));
    }

    private static JsonObject display(FoodDisplayTransform transform) {
        JsonObject display = new JsonObject();
        if (transform == FoodDisplayTransform.CUP) {
            addTransform(display, "thirdperson_righthand",
                vector(0, 90, 55), vector(0, 1, -2.5), vector(0.85, 0.85, 0.85));
            addTransform(display, "thirdperson_lefthand",
                vector(0, 90, 55), vector(0, 0, -1.5), vector(0.85, 0.85, 0.85));
            addTransform(display, "firstperson_righthand",
                vector(0, 145, -10), vector(1, 0.35, 0.8), vector(0.68, 0.68, 0.68));
            addTransform(display, "firstperson_lefthand",
                vector(0, 145, -10), vector(1, 0.35, 0.8), vector(0.68, 0.68, 0.68));
        } else {
            addTransform(display, "thirdperson_righthand",
                vector(0, 90, 55), vector(0, 1.5, -2), vector(0.65, 0.65, 0.65));
            addTransform(display, "thirdperson_lefthand",
                vector(0, 90, 55), vector(0, 1.5, -2), vector(0.65, 0.65, 0.65));
            addTransform(display, "firstperson_righthand",
                vector(10, 125, -15), vector(1, 0.35, 0.8), vector(0.55, 0.55, 0.55));
            addTransform(display, "firstperson_lefthand",
                vector(10, 125, -15), vector(1, 0.35, 0.8), vector(0.55, 0.55, 0.55));
        }
        JsonObject head = new JsonObject();
        head.add("translation", vector(0, 6.25, 0));
        display.add("head", head);
        JsonObject shelf = new JsonObject();
        shelf.add("rotation", vector(0, -135, 15));
        shelf.add("scale", vector(3, 3, 3));
        display.add("on_shelf", shelf);
        return display;
    }

    private static void addTransform(JsonObject display, String name,
                                     JsonArray rotation, JsonArray translation, JsonArray scale) {
        JsonObject transform = new JsonObject();
        transform.add("rotation", rotation);
        transform.add("translation", translation);
        transform.add("scale", scale);
        display.add(name, transform);
    }

    private static JsonArray vector(double x, double y, double z) {
        JsonArray vector = new JsonArray();
        vector.add(x);
        vector.add(y);
        vector.add(z);
        return vector;
    }

    @Override
    public String getName() {
        return "Gecko food item models";
    }
}
