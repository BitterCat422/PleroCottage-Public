package net.bittercat.plerocottage.blocks;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Direction;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class FoodCollisionShapes {
    private static final Map<String, VoxelShape> SHAPES = load();
    private static final Map<String, java.util.List<double[]>> BOXES = loadBoxes();

    private FoodCollisionShapes() {}

    public static VoxelShape get(String blockName) {
        return SHAPES.getOrDefault(blockName, Shapes.block());
    }

    public static VoxelShape get(String blockName, Direction facing) {
        if (facing == Direction.NORTH || !BOXES.containsKey(blockName)) {
            return get(blockName);
        }
        VoxelShape shape = Shapes.empty();
        for (double[] box : BOXES.get(blockName)) {
            double minX = box[0], minZ = box[2], maxX = box[3], maxZ = box[5];
            switch (facing) {
                case EAST -> {
                    minX = 16 - box[5];
                    maxX = 16 - box[2];
                    minZ = box[0];
                    maxZ = box[3];
                }
                case SOUTH -> {
                    minX = 16 - box[3];
                    maxX = 16 - box[0];
                    minZ = 16 - box[5];
                    maxZ = 16 - box[2];
                }
                case WEST -> {
                    minX = box[2];
                    maxX = box[5];
                    minZ = 16 - box[3];
                    maxZ = 16 - box[0];
                }
                default -> {
                }
            }
            shape = Shapes.or(shape, boxShape(minX, box[1], minZ, maxX, box[4], maxZ));
        }
        return shape;
    }

    private static Map<String, VoxelShape> load() {
        Map<String, VoxelShape> result = new HashMap<>();
        try (InputStream stream = FoodCollisionShapes.class.getResourceAsStream(
                "/assets/plerocottage/food_collision_shapes.json")) {
            if (stream == null) {
                return result;
            }
            JsonObject root = JsonParser.parseReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            for (Map.Entry<String, com.google.gson.JsonElement> entry : root.entrySet()) {
                VoxelShape shape = Shapes.empty();
                JsonArray boxes = entry.getValue().getAsJsonArray();
                for (com.google.gson.JsonElement element : boxes) {
                    JsonObject box = element.getAsJsonObject();
                    if (box.has("subtract") && box.get("subtract").getAsBoolean()) {
                        continue;
                    }
                    shape = Shapes.or(shape, boxShape(
                        box.get("minX").getAsDouble(), box.get("minY").getAsDouble(),
                        box.get("minZ").getAsDouble(), box.get("maxX").getAsDouble(),
                        box.get("maxY").getAsDouble(), box.get("maxZ").getAsDouble()));
                }
                result.put(entry.getKey(), shape);
            }
        } catch (RuntimeException | java.io.IOException ignored) {
            // 资源缺失时由调用方使用完整方块作为保底形状。
        }
        return result;
    }

    private static VoxelShape boxShape(double minX, double minY, double minZ,
                                       double maxX, double maxY, double maxZ) {
        return Block.box(Math.min(minX, maxX), Math.min(minY, maxY), Math.min(minZ, maxZ),
            Math.max(minX, maxX), Math.max(minY, maxY), Math.max(minZ, maxZ));
    }

    private static Map<String, java.util.List<double[]>> loadBoxes() {
        Map<String, java.util.List<double[]>> result = new HashMap<>();
        try (InputStream stream = FoodCollisionShapes.class.getResourceAsStream(
                "/assets/plerocottage/food_collision_shapes.json")) {
            if (stream == null) return result;
            JsonObject root = JsonParser.parseReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            for (Map.Entry<String, com.google.gson.JsonElement> entry : root.entrySet()) {
                java.util.List<double[]> boxes = new java.util.ArrayList<>();
                for (com.google.gson.JsonElement element : entry.getValue().getAsJsonArray()) {
                    JsonObject box = element.getAsJsonObject();
                    if (box.has("subtract") && box.get("subtract").getAsBoolean()) continue;
                    boxes.add(new double[] {
                        box.get("minX").getAsDouble(), box.get("minY").getAsDouble(),
                        box.get("minZ").getAsDouble(), box.get("maxX").getAsDouble(),
                        box.get("maxY").getAsDouble(), box.get("maxZ").getAsDouble()
                    });
                }
                result.put(entry.getKey(), java.util.List.copyOf(boxes));
            }
        } catch (RuntimeException | java.io.IOException ignored) {
        }
        return result;
    }
}
