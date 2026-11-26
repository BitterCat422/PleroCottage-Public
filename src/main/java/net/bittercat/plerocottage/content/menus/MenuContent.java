package net.bittercat.plerocottage.content.menus;

import net.bittercat.plerocottage.init.MenuRegistry;
import net.bittercat.plerocottage.menus.FurnitureMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;

public final class MenuContent {
    public static final RegistryObject<MenuType<FurnitureMenu>> CABINET =
        MenuRegistry.MENU_TYPES.register("cabinet", () -> IForgeMenuType.create(MenuContent::createCabinet));
    public static final RegistryObject<MenuType<FurnitureMenu>> DRAWER =
        MenuRegistry.MENU_TYPES.register("drawer", () -> IForgeMenuType.create(MenuContent::createDrawer));

    private MenuContent() {
    }

    private static FurnitureMenu createCabinet(int id, Inventory inventory, FriendlyByteBuf data) {
        return new FurnitureMenu(CABINET.get(), id, inventory, data, false);
    }

    private static FurnitureMenu createDrawer(int id, Inventory inventory, FriendlyByteBuf data) {
        return new FurnitureMenu(DRAWER.get(), id, inventory, data, true);
    }

    public static void bootstrap() {
    }
}
