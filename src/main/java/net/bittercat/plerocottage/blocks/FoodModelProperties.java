package net.bittercat.plerocottage.blocks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public record FoodModelProperties(int rotationMode, boolean enablePitch, boolean waterloggable,
                                  String transparencyType) {
    private static final FoodModelProperties DEFAULT =
        new FoodModelProperties(0, false, false, "SOLID");
    private static final Map<String, FoodModelProperties> PROPERTIES = load();

    public static FoodModelProperties get(String blockName) {
        return PROPERTIES.getOrDefault(blockName, DEFAULT);
    }

    private static Map<String, FoodModelProperties> load() {
        Map<String, FoodModelProperties> result = new HashMap<>();
        try (InputStream stream = FoodModelProperties.class.getResourceAsStream(
                "/assets/plerocottage/food_model_properties.json")) {
            if (stream == null) {
                return result;
            }
            JsonObject root = JsonParser.parseReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            for (Map.Entry<String, com.google.gson.JsonElement> entry : root.entrySet()) {
                JsonObject value = entry.getValue().getAsJsonObject();
                result.put(entry.getKey(), new FoodModelProperties(
                    value.get("rotationMode").getAsInt(),
                    value.get("enablePitch").getAsBoolean(),
                    value.get("waterloggable").getAsBoolean(),
                    value.get("transparencyType").getAsString()));
            }
        } catch (RuntimeException | IOException ignored) {
            // 资源不可用时采用不旋转、非透明的默认设置。
        }
        return result;
    }
}
