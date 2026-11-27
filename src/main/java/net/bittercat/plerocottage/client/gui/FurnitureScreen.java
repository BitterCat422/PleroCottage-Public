package net.bittercat.plerocottage.client.gui;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.menus.FurnitureMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class FurnitureScreen extends AbstractContainerScreen<FurnitureMenu> {
    private static final ResourceLocation CABINET_TEXTURE =
        new ResourceLocation(PleroCottage.MODID, "textures/screens/cabinet_gui.png");
    private static final ResourceLocation DRAWER_TEXTURE =
        new ResourceLocation(PleroCottage.MODID, "textures/screens/drawer_gui.png");
    private static final ResourceLocation SORT_BUTTON =
        new ResourceLocation(PleroCottage.MODID, "textures/screens/profiles_button.png");
    private final boolean drawer;

    public FurnitureScreen(FurnitureMenu menu, Inventory inventory, Component title, boolean drawer) {
        super(menu, inventory, title);
        this.drawer = drawer;
        imageWidth = 176;
        imageHeight = drawer ? 197 : 187;
    }

    @Override
    protected void init() {
        super.init();
        addSortButton(0, 5);
        if (drawer) {
            addSortButton(1, 54);
        }
    }

    private void addSortButton(int button, int yOffset) {
        addRenderableWidget(new ImageButton(leftPos + 158, topPos + yOffset, 10, 10,
            0, 0, 0, SORT_BUTTON, 10, 10,
            ignored -> minecraft.gameMode.handleInventoryButtonClick(menu.containerId, button)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        ResourceLocation texture = drawer ? DRAWER_TEXTURE : CABINET_TEXTURE;
        graphics.blit(texture, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        Component heading = Component.translatable("title.plerocottage." + (drawer ? "drawer" : "cabinet"));
        Component inventoryHeading = Component.translatable("container.inventory");
        graphics.drawString(font, heading, 8, 6, 0xFF3F3F3F, false);
        graphics.drawString(font, inventoryHeading, 8, drawer ? 103 : 94, 0xFF3F3F3F, false);
    }
}
