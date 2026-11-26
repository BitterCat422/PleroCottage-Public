package net.bittercat.plerocottage.data;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

final class RecipeUnlockAdvancements {
    private final Set<ResourceLocation> recipes = new HashSet<>();
    private final Set<ResourceLocation> manualAdvancements = new HashSet<>();
    private final Set<ResourceLocation> manualRewards = new HashSet<>();
    private final Map<ResourceLocation, JsonObject> advancements = new LinkedHashMap<>();

    RecipeUnlockAdvancements(Path resourcesRoot) {
        Path data = resourcesRoot.resolve("data").resolve(PleroCottage.MODID);
        readJsonFiles(data.resolve("advancements"), (id, json) -> {
            manualAdvancements.add(id);
            JsonObject rewards = GsonHelper.getAsJsonObject(json, "rewards", new JsonObject());
            for (JsonElement recipe : GsonHelper.getAsJsonArray(rewards, "recipes", new JsonArray())) {
                manualRewards.add(new ResourceLocation(recipe.getAsString()));
            }
        });
        readJsonFiles(data.resolve("recipes"), (id, json) -> add(id, json, null));
    }

    void add(ResourceLocation id, JsonObject recipe, JsonObject advancement) {
        if (!recipes.add(id)) {
            throw new IllegalStateException("配方 ID 重复：" + id);
        }

        ResourceLocation type = new ResourceLocation(GsonHelper.getAsString(recipe, "type"));
        String directory = type.getNamespace() + "_" + type.getPath().replace('/', '_');
        String path = id.getPath();
        if (!path.startsWith(directory + "/")) {
            path = directory + "/" + path;
        }
        ResourceLocation advancementId = new ResourceLocation(id.getNamespace(), "recipes/" + path);

        if (resultItem(id, recipe) == null) {
            return;
        }
        if (advancement == null) {
            Set<JsonObject> materials = allMaterials(recipe);
            if (materials.isEmpty()) {
                PleroCottage.LOGGER.warn("配方 {} 没有可用于解锁的物品原料，跳过解锁进度", id);
                return;
            }
            advancement = createAdvancement(id, materials);
        }
        if (recipe.has("conditions")) {
            
            advancement.add("conditions", recipe.get("conditions").deepCopy());
        }
        if (manualAdvancements.contains(advancementId) || manualRewards.contains(id)
            || advancements.putIfAbsent(advancementId, advancement) != null) {
            throw new IllegalStateException("配方解锁进度重复：" + id + " -> " + advancementId);
        }
    }

    Map<ResourceLocation, JsonObject> entries() {
        return advancements;
    }

    private static Set<JsonObject> allMaterials(JsonObject recipe) {
        Set<JsonObject> materials = new LinkedHashSet<>();
        for (String key : List.of("ingredient", "ingredients", "mainInput", "input", "secondaryInput", "key", "container")) {
            JsonElement input = recipe.get(key);
            if (input == null) {
                continue;
            }
            if (key.equals("key")) {
                input.getAsJsonObject().asMap().values().forEach(slot -> addMaterialPredicates(slot, materials));
            } else {
                addMaterialPredicates(input, materials);
            }
        }
        return materials;
    }

    private static void addMaterialPredicates(JsonElement element, Set<JsonObject> predicates) {
        if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                addMaterialPredicates(child, predicates);
            }
        } else if (element.isJsonObject()) {
            JsonObject ingredient = element.getAsJsonObject();
            JsonObject predicate = new JsonObject();
            if (ingredient.has("item")) {
                JsonArray items = new JsonArray();
                items.add(new ResourceLocation(GsonHelper.getAsString(ingredient, "item")).toString());
                predicate.add("items", items);
            } else if (ingredient.has("tag")) {
                predicate.addProperty("tag", new ResourceLocation(GsonHelper.getAsString(ingredient, "tag")).toString());
            }
            if (!predicate.entrySet().isEmpty()) {
                predicates.add(predicate);
            }
        }
    }

    private static String resultItem(ResourceLocation id, JsonObject recipe) {
        JsonElement result = recipe.has("result") ? recipe.get("result") : recipe.get("output");
        if (result == null) {
            result = recipe.get("results");
        }
        if (result != null && result.isJsonArray() && result.getAsJsonArray().size() > 0) {
            result = result.getAsJsonArray().get(0);
        }
        if (result != null && result.isJsonPrimitive() && result.getAsJsonPrimitive().isString()) {
            return new ResourceLocation(result.getAsString()).toString();
        }
        if (result != null && result.isJsonObject()) {
            JsonObject stack = result.getAsJsonObject();
            if (stack.has("item")) {
                return new ResourceLocation(GsonHelper.getAsString(stack, "item")).toString();
            }
            if (stack.has("fluid")) {
                return null;
            }
        }
        throw new IllegalStateException("无法识别配方主产物：" + id);
    }

    private static JsonObject createAdvancement(ResourceLocation id, Set<JsonObject> materials) {
        JsonObject advancement = JsonParser.parseString("""
            {
              "parent": "minecraft:recipes/root",
              "criteria": {
                "has_the_recipe": {
                  "trigger": "minecraft:recipe_unlocked",
                  "conditions": { "recipe": "%s" }
                }
              },
              "requirements": [["has_the_recipe"]],
              "rewards": { "recipes": ["%s"] },
              "sends_telemetry_event": false
            }
            """.formatted(id, id)).getAsJsonObject();
        JsonObject criteria = advancement.getAsJsonObject("criteria");
        JsonArray requirements = advancement.getAsJsonArray("requirements").get(0).getAsJsonArray();
        int index = 0;
        for (JsonObject material : materials) {
            String name = materials.size() == 1 ? "has_material" : "has_material_" + ++index;
            JsonObject criterion = new JsonObject();
            criterion.addProperty("trigger", "minecraft:inventory_changed");
            JsonArray items = new JsonArray();
            items.add(material);
            JsonObject conditions = new JsonObject();
            conditions.add("items", items);
            criterion.add("conditions", conditions);
            criteria.add(name, criterion);
            requirements.add(name);
        }
        return advancement;
    }

    private static void readJsonFiles(Path root, BiConsumer<ResourceLocation, JsonObject> accept) {
        try (Stream<Path> files = Files.walk(root)) {
            for (Path file : files.filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith(".json")).sorted().toList()) {
                String relative = root.relativize(file).toString().replace('\\', '/');
                try (var reader = Files.newBufferedReader(file)) {
                    ResourceLocation id = new ResourceLocation(PleroCottage.MODID,
                        relative.substring(0, relative.length() - ".json".length()));
                    accept.accept(id, JsonParser.parseReader(reader).getAsJsonObject());
                } catch (RuntimeException exception) {
                    throw new IllegalStateException("处理资源失败：" + file, exception);
                }
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("读取资源目录失败：" + root, exception);
        }
    }
}
