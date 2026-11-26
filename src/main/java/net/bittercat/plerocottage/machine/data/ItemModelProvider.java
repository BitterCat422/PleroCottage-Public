package net.bittercat.plerocottage.machine.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.init.ItemRegistry;

import java.util.List;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput output,ExistingFileHelper existingFileHelper) {
        super(output, PleroCottageMachine.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Machine Item Models: plerocottage";
    }

    @Override
    protected void registerModels() {
        List.of(ItemRegistry.ICE_CREAM_MACHINE, ItemRegistry.OVEN, ItemRegistry.TEAPOT,
                ItemRegistry.BLENDER, ItemRegistry.CUTTING_BOARD, ItemRegistry.STOCK_POT,
                ItemRegistry.SAUCEPAN_AND_WHISK, ItemRegistry.SHAKER, ItemRegistry.COOKTOP).stream()
                .map(RegistryObject::get)
                .forEach(this::basicItem);
        this.fryer();
        this.fermenter();
    }

    private void fryer() {
        ResourceLocation property = ResourceLocation.fromNamespaceAndPath(
                PleroCottageMachine.MOD_ID,
                "recipe_book_icon"
        );
        ItemModelBuilder model = this.withExistingParent("fryer", this.mcLoc("item/generated"))
                .texture("layer0", this.modLoc("item/fryer"));
        model.override()
                .predicate(property, 1.0F)
                .model(this.iconModel("fryer_fast"))
                .end();
        model.override()
                .predicate(property, 2.0F)
                .model(this.iconModel("fryer_slow"))
                .end();
    }

    private void fermenter() {
        ResourceLocation property = ResourceLocation.fromNamespaceAndPath(
                PleroCottageMachine.MOD_ID,
                "recipe_book_icon"
        );
        ItemModelBuilder model = this.withExistingParent("fermenter", this.mcLoc("item/generated"))
                .texture("layer0", this.modLoc("item/fermenter"));
        model.override()
                .predicate(property, 1.0F)
                .model(this.iconModel("fermenter_dim"))
                .end();
        model.override()
                .predicate(property, 2.0F)
                .model(this.iconModel("fermenter_normal"))
                .end();
        model.override()
                .predicate(property, 3.0F)
                .model(this.iconModel("fermenter_bright"))
                .end();
    }

    private ItemModelBuilder iconModel(String name) {
        return this.withExistingParent(name, this.mcLoc("item/generated"))
                .texture("layer0", this.modLoc("item/" + name));
    }
}
