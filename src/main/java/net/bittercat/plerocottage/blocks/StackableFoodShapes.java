package net.bittercat.plerocottage.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

final class StackableFoodShapes {
    private StackableFoodShapes() {
    }

    private static final java.util.Map<String, VoxelShape> CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    static VoxelShape get(String name, int state, Direction facing) {
        return CACHE.computeIfAbsent(name + "|" + state + "|" + facing.getName(), key -> compute(name, state, facing));
    }

    private static VoxelShape compute(String name, int state, Direction facing) {
        double[][] boxes = name.equals("baguette")
            ? baguetteBoxes(state) : muffinBoxes(state);
        VoxelShape shape = Shapes.empty();
        for (double[] box : boxes) {
            shape = Shapes.or(shape, rotate(box, facing));
        }
        return shape;
    }

    private static double[][] muffinBoxes(int state) {
        double[][] first = {
            {5, 3.5, 5, 11, 5.5, 11},
            {5.5, 5, 5.5, 10.5, 7, 10.5},
            {5.5, 0, 5.5, 10.5, 3, 10.5},
            {5.4, 2.4, 5.4, 10.6, 3.6, 10.6}
        };
        double[][] second = {
            {9, 3.5, 5, 15, 5.5, 11},
            {9.5, 5, 5.5, 14.5, 7, 10.5},
            {9.5, 0, 5.5, 14.5, 3, 10.5},
            {9.4, 2.4, 5.4, 14.6, 3.6, 10.6},
            {1, 3.5, 5, 7, 5.5, 11},
            {1.5, 5, 5.5, 6.5, 7, 10.5},
            {1.5, 0, 5.5, 6.5, 3, 10.5},
            {1.4, 2.4, 5.4, 6.6, 3.6, 10.6}
        };
        double[][] third = {
            {8.71086, 3.5, 8.71985, 14.71086, 5.5, 14.71985},
            {9.21086, 5, 9.21985, 14.21086, 7, 14.21985},
            {9.21086, 0, 9.21985, 14.21086, 3, 14.21985},
            {9.11086, 2.4, 9.11985, 14.31086, 3.6, 14.31985},
            {7.21086, 3.5, 1.21985, 13.21086, 5.5, 7.21985},
            {7.71086, 5, 1.71985, 12.71086, 7, 6.71985},
            {7.71086, 0, 1.71985, 12.71086, 3, 6.71985},
            {7.61086, 2.4, 1.61985, 12.81086, 3.6, 6.81985},
            {1.21086, 3.5, 6.21985, 7.21086, 5.5, 12.21985},
            {1.71086, 5, 6.71985, 6.71086, 7, 11.71985},
            {1.71086, 0, 6.71985, 6.71086, 3, 11.71985},
            {1.61086, 2.4, 6.61985, 6.81086, 3.6, 11.81985}
        };
        double[][] fourth = {
            {8.71086, 3.5, 8.71985, 14.71086, 5.5, 14.71985},
            {9.21086, 5, 9.21985, 14.21086, 7, 14.21985},
            {9.21086, 0, 9.21985, 14.21086, 3, 14.21985},
            {9.11086, 2.4, 9.11985, 14.31086, 3.6, 14.31985},
            {7.21086, 3.5, 1.21985, 13.21086, 5.5, 7.21985},
            {7.71086, 5, 1.71985, 12.71086, 7, 6.71985},
            {7.71086, 0, 1.71985, 12.71086, 3, 6.71985},
            {7.61086, 2.4, 1.61985, 12.81086, 3.6, 6.81985},
            {1.21086, 3.5, 6.21985, 7.21086, 5.5, 12.21985},
            {1.71086, 5, 6.71985, 6.71086, 7, 11.71985},
            {1.71086, 0, 6.71985, 6.71086, 3, 11.71985},
            {1.61086, 2.4, 6.61985, 6.81086, 3.6, 11.81985},
            {5, 10.5, 5, 11, 12.5, 11},
            {5.5, 12, 5.5, 10.5, 14, 10.5},
            {5.5, 7, 5.5, 10.5, 10, 10.5},
            {5.4, 9.4, 5.4, 10.6, 10.6, 10.6}
        };
        return switch (state) {
            case 1 -> second;
            case 2 -> third;
            case 3 -> fourth;
            default -> first;
        };
    }

    private static double[][] baguetteBoxes(int state) {
        double[][] first = {{6.2, 0, 0, 9.8, 2.02, 16}};
        double[][] second = {
            {3.2, 0, 0, 6.8, 2.02, 16},
            {9.2, 0, 0, 12.8, 2.02, 16}
        };
        double[][] third = {
            {3.2, 0, 0, 6.8, 2.02, 16},
            {9.2, 0, 0, 12.8, 2.02, 16},
            {6.2, 2, 0, 9.8, 4.02, 16}
        };
        return switch (state) {
            case 1 -> second;
            case 2 -> third;
            default -> first;
        };
    }

    private static VoxelShape rotate(double[] box, Direction facing) {
        double minX = box[0];
        double minZ = box[2];
        double maxX = box[3];
        double maxZ = box[5];
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
        return Block.box(minX, box[1], minZ, maxX, box[4], maxZ);
    }
}
