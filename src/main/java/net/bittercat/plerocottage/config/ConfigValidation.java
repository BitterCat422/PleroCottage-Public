package net.bittercat.plerocottage.config;

import java.nio.charset.StandardCharsets;
import java.util.List;
import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public final class ConfigValidation {
    public static final int MAX_LIST_ENTRIES = 4096;
    public static final int MAX_STRING_LENGTH = 8192;
    
    public static final int MAX_LIST_BYTES = 192 * 1024;

    private ConfigValidation() {
    }

    public static boolean idList(Object value, String path) {
        return validateList(value, path, false);
    }

    public static boolean hatchingList(Object value, String path) {
        return validateList(value, path, true);
    }

    private static boolean validateList(Object value, String path, boolean mappings) {
        if (value == null) {
            return false;
        }
        if (!(value instanceof List<?> list) || !fitsNetworkLimits(list)) {
            PleroCottage.LOGGER.error("配置 {} 必须是列表，且最多 {} 项、每项 {} 字符、编码后最多 {} 字节",
                path, MAX_LIST_ENTRIES, MAX_STRING_LENGTH, MAX_LIST_BYTES);
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            String text = (String) list.get(i);
            if (mappings ? !isHatchingMapping(text) : !isFullId(text)) {
                PleroCottage.LOGGER.error("配置 {} 的第 {} 项格式无效，必须使用{}",
                    path, i + 1, mappings ? "完整物品 ID = 完整实体类型 ID" : "完整 ID（namespace:path）");
                return false;
            }
        }
        return true;
    }

    public static boolean isFullId(String text) {
        ResourceLocation id = ResourceLocation.tryParse(text);
        return id != null && id.toString().equals(text) && !id.getPath().isEmpty();
    }

    public static boolean isHatchingMapping(String text) {
        int separator = text.indexOf('=');
        return separator > 0 && separator == text.lastIndexOf('=')
            && isFullId(text.substring(0, separator).trim())
            && isFullId(text.substring(separator + 1).trim());
    }

    public static boolean fitsNetworkLimits(List<?> list) {
        if (list.size() > MAX_LIST_ENTRIES) {
            return false;
        }
        int size = varIntSize(list.size());
        for (Object entry : list) {
            if (!(entry instanceof String text) || text.length() > MAX_STRING_LENGTH) {
                return false;
            }
            int bytes = text.getBytes(StandardCharsets.UTF_8).length;
            size += varIntSize(bytes) + bytes;
            if (size > MAX_LIST_BYTES) {
                return false;
            }
        }
        return true;
    }

    private static int varIntSize(int value) {
        int size = 1;
        while ((value & ~0x7F) != 0) {
            size++;
            value >>>= 7;
        }
        return size;
    }

    public static void checkHatchingRegistries() {
        for (String mapping : Config.INCUBATOR_HATCHING_MAPPINGS.get()) {
            String[] ids = mapping.split("=", 2);
            ResourceLocation itemId = ResourceLocation.tryParse(ids[0].trim());
            ResourceLocation entityId = ResourceLocation.tryParse(ids[1].trim());
            if (!ForgeRegistries.ITEMS.containsKey(itemId)
                || ForgeRegistries.ITEMS.getValue(itemId) == Items.AIR
                || !ForgeRegistries.ENTITY_TYPES.containsKey(entityId)) {
                PleroCottage.LOGGER.warn("孵化映射 {} 被忽略：物品或实体类型不存在，或物品为空气", mapping);
            }
        }
    }
}
