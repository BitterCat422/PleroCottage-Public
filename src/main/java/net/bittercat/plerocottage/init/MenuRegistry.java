package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.bittercat.plerocottage.machine.menus.*;

public final class MenuRegistry {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, PleroCottage.MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPE = MENU_TYPES;
    public static final RegistryObject<MenuType<IceCreamMachineMenu>> ICE_CREAM_MACHINE = MENU_TYPES.register("ice_cream_machine_menu", () -> IForgeMenuType.create(IceCreamMachineMenu::new));
    public static final RegistryObject<MenuType<FryerMenu>> FRYER = MENU_TYPES.register("fryer", () -> IForgeMenuType.create(FryerMenu::new));
    public static final RegistryObject<MenuType<OvenMenu>> OVEN = MENU_TYPES.register("oven", () -> IForgeMenuType.create(OvenMenu::new));
    public static final RegistryObject<MenuType<TeapotMenu>> TEAPOT = MENU_TYPES.register("teapot", () -> IForgeMenuType.create(TeapotMenu::new));
    public static final RegistryObject<MenuType<FermenterMenu>> FERMENTER = MENU_TYPES.register("fermenter", () -> IForgeMenuType.create(FermenterMenu::new));
    public static final RegistryObject<MenuType<BlenderMenu>> BLENDER = MENU_TYPES.register("blender", () -> IForgeMenuType.create(BlenderMenu::new));
    public static final RegistryObject<MenuType<StockPotMenu>> STOCK_POT = MENU_TYPES.register("stock_pot", () -> IForgeMenuType.create(StockPotMenu::new));
    public static final RegistryObject<MenuType<SaucepanAndWhiskMenu>> SAUCEPAN_AND_WHISK = MENU_TYPES.register("saucepan_and_whisk", () -> IForgeMenuType.create(SaucepanAndWhiskMenu::new));

    private MenuRegistry() {
    }
}
