package net.bittercat.plerocottage.machine.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.OvenBlockEntity;
import net.bittercat.plerocottage.machine.menus.OvenMenu;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.machine.network.server.packet.BakeItemCraftPacket;
import net.bittercat.plerocottage.machine.network.server.packet.SyncOvenTimeOrTemperaturePacket;

@OnlyIn(Dist.CLIENT)
public class OvenScreen extends BaseMachineScreen<OvenMenu> implements RecipeUpdateListener {
    public static final ResourceLocation OVEN_GUI = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/oven_gui.png");
    public static final ResourceLocation OVEN_GUI_WIDGET = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/oven_gui_widget.png");
    public static final int BUTTON_X_OFFSET = 160;
    public static final int BUTTON_Y_OFFSET = 0;
    public static final int BUTTON_WIDTH = 18;
    public static final int BUTTON_HEIGHT = 18;
    public static final int DOOR_BUTTON_X_OFFSET = 0;
    public static final int DOOR_BUTTON_Y_OFFSET = 160;
    public static final int DOOR_BUTTON_WIDTH = 46;
    public static final int DOOR_BUTTON_HEIGHT = 52;
    public static final int DOOR_X_OFFSET = 0;
    public static final int DOOR_Y_OFFSET = 0;
    public static final int DOOR_WIDTH = 144;
    public static final int DOOR_HEIGHT = 108;
    public static final int INPUT_X_OFFSET = 0;
    public static final int INPUT_Y_OFFSET = 109;
    public static final int INPUT_WIDTH = 90;
    public static final int INPUT_HEIGHT = 38;
    public static final int OUTPUT_X_OFFSET = 48;
    public static final int OUTPUT_Y_OFFSET = 160;
    public static final int OUTPUT_WIDTH = 46;
    public static final int OUTPUT_HEIGHT = 52;
    public static final int CONTAINER_X_OFFSET = 96;
    public static final int CONTAINER_Y_OFFSET = 109;

    public OvenBlockEntity blockEntity;
    private final OvenRecipeBookComponent recipeBookComponent = new OvenRecipeBookComponent(
            ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/recipe_book_buttons.png"),
            1 * 26
    );
    private ImageButton recipeBookButton;
    private ImageButton temperatureButton;
    private ImageButton timeButton;
    private ImageButton doorButton;
    private boolean widthTooNarrow;

    public OvenScreen(OvenMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, OVEN_GUI);
        this.blockEntity = menu.blockEntity;
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.recipeBookButton = this.addRenderableWidget(new ImageButton(
                this.leftPos + 5, this.topPos + 62, 20, 18, 0, 0, 19,
                new ResourceLocation("textures/gui/recipe_button.png"), button -> {
                    this.recipeBookComponent.toggleVisibility();
                    this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
                    this.updateWidgetPositions();
                }));
        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
        this.temperatureButton = this.addRenderableWidget(this.createKnob(false));
        this.timeButton = this.addRenderableWidget(this.createKnob(true));
        this.doorButton = this.addRenderableWidget(new ImageButton(
                this.leftPos + 129, this.topPos + 84, DOOR_BUTTON_WIDTH, DOOR_BUTTON_HEIGHT,
                DOOR_BUTTON_X_OFFSET, DOOR_BUTTON_Y_OFFSET, OVEN_GUI_WIDGET,
                button -> PleroCottageMachinePacket.sendToServer(new BakeItemCraftPacket(this.blockEntity.getBlockPos()))) {
            @Override
            public void renderTexture(GuiGraphics graphics, ResourceLocation texture, int x, int y, int u, int v,
                                      int delta, int width, int height, int textureWidth, int textureHeight) {
                if (menu.getCookingTime() == 0 && !menu.getSlot(OvenMenu.OUTPUT_MENU_SLOT).hasItem()) {
                    RenderSystem.enableDepthTest();
                    RenderSystem.enableBlend();
                    graphics.blit(texture, x, y, u, v, width, height, textureWidth, textureHeight);
                    if (this.isHovered()) {
                        graphics.setColor(1.0F, 1.0F, 1.0F, 0.225F);
                        graphics.blit(texture, x, y, u, v, width, height, textureWidth, textureHeight);
                        graphics.setColor(1, 1, 1, 1);
                    }
                }
            }

            @Override
            public boolean isActive() {
                return super.isActive() && menu.getCookingTime() == 0
                        && !menu.getSlot(OvenMenu.OUTPUT_MENU_SLOT).hasItem();
            }

            @Override
            protected boolean isValidClickButton(int button) {
                return super.isValidClickButton(button)
                        && menu.getCookingTime() == 0
                        && !menu.getSlot(OvenMenu.OUTPUT_MENU_SLOT).hasItem();
            }
        });
        this.updateWidgetPositions();
        this.updateRecipeBookVisibility();
    }

    private ImageButton createKnob(boolean time) {
        return new ImageButton(this.leftPos + (time ? 128 : 30), this.topPos + 7,
                BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_X_OFFSET, BUTTON_Y_OFFSET, BUTTON_HEIGHT,
                OVEN_GUI_WIDGET, button -> {
                    if (this.menu.getCookingTime() != 0) return;
                    int state = time ? this.menu.getMaxCookingTime().state : this.menu.getTemperature().state;
                    PleroCottageMachinePacket.sendToServer(new SyncOvenTimeOrTemperaturePacket(
                            this.blockEntity.getBlockPos(), time ? 1 : 0, state >= 3 ? 0 : state + 1));
                }) {
            @Override
            public void playDownSound(SoundManager soundManager) {
            }

            @Override
            public void renderTexture(GuiGraphics graphics, ResourceLocation texture, int x, int y, int u, int v,
                                      int delta, int width, int height, int textureWidth, int textureHeight) {
                RenderSystem.enableDepthTest();
                int state = time ? menu.getMaxCookingTime().state : menu.getTemperature().state;
                if (time && menu.getCookingTime() != 0) state -= menu.getCookingTime() / 100;
                graphics.blit(texture, x, y, u, v + delta * state, width, height, textureWidth, textureHeight);
            }
        };
    }

    private void updateWidgetPositions() {
        this.recipeBookButton.setPosition(this.leftPos + 5, this.topPos + 62);
        if (this.temperatureButton != null) this.temperatureButton.setPosition(this.leftPos + 30, this.topPos + 7);
        if (this.timeButton != null) this.timeButton.setPosition(this.leftPos + 128, this.topPos + 7);
        if (this.doorButton != null) this.doorButton.setPosition(this.leftPos + 129, this.topPos + 84);
    }

    private void updateRecipeBookVisibility() {
        boolean cooking = this.menu.getCookingTime() != 0 || this.blockEntity.isRunning;
        this.recipeBookButton.visible = !cooking;
        this.recipeBookButton.active = !cooking;
        if (cooking && this.recipeBookComponent.isVisible()) {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            this.updateWidgetPositions();
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
        this.updateRecipeBookVisibility();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.updateRecipeBookVisibility();
        this.renderBackground(graphics);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(graphics, partialTick, mouseX, mouseY);
            this.recipeBookComponent.render(graphics, mouseX, mouseY, partialTick);
        } else {
            this.recipeBookComponent.render(graphics, mouseX, mouseY, partialTick);
            super.render(graphics, mouseX, mouseY, partialTick);
            if (this.recipeBookButton.visible) {
                this.recipeBookComponent.renderGhostRecipe(graphics, this.leftPos, this.topPos, false, partialTick);
            }
        }
        this.recipeBookComponent.renderTooltip(graphics, this.leftPos, this.topPos, mouseX, mouseY);
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible())
                && super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        }
        return (this.widthTooNarrow && this.recipeBookComponent.isVisible())
                || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int x, int y, int button) {
        boolean outside = mouseX < x || mouseY < y
                || mouseX >= x + this.imageWidth || mouseY >= y + this.imageHeight;
        return outside && this.recipeBookComponent.hasClickedOutside(
                mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, button);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
        this.recipeBookComponent.slotClicked(slot);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTick, mouseX, mouseY);
        this.renderTemperature(graphics);
        this.renderMaxCookingTime(graphics);
        if (this.menu.getCookingTime() != 0) {
            this.renderRunning(graphics);
        } else {
            this.renderInputSlot(graphics);
            this.renderOutputSlot(graphics);
            this.renderContainerSlot(graphics);
        }
    }

    public void renderTemperature(GuiGraphics graphics) {
        graphics.drawString(this.font, this.menu.getTemperature().temperatureName, this.leftPos + 70, this.topPos + 11, -1);
    }

    public void renderMaxCookingTime(GuiGraphics graphics) {
        int cookingTime = this.menu.getCookingTime();
        String name = cookingTime != 0
                ? this.menu.getMaxCookingTime().second - cookingTime / 20 + "s"
                : this.menu.getMaxCookingTime().secondName;
        graphics.drawString(this.font, name, this.leftPos + 94, this.topPos + 11, -1);
    }

    public void renderRunning(GuiGraphics graphics) {
        graphics.blit(OVEN_GUI_WIDGET, this.leftPos + 16, this.topPos + 29, DOOR_X_OFFSET, DOOR_Y_OFFSET, DOOR_WIDTH, DOOR_HEIGHT);
    }

    public void renderInputSlot(GuiGraphics graphics) {
        graphics.blit(OVEN_GUI_WIDGET, this.leftPos + 44, this.topPos + 57, INPUT_X_OFFSET, INPUT_Y_OFFSET, INPUT_WIDTH, INPUT_HEIGHT);
    }

    public void renderOutputSlot(GuiGraphics graphics) {
        if (this.menu.getSlot(OvenMenu.OUTPUT_MENU_SLOT).hasItem()) {
            graphics.blit(OVEN_GUI_WIDGET, this.leftPos + 129, this.topPos + 84, OUTPUT_X_OFFSET, OUTPUT_Y_OFFSET, OUTPUT_WIDTH, OUTPUT_HEIGHT);
        }
    }

    public void renderContainerSlot(GuiGraphics graphics) {
        graphics.blit(OVEN_GUI_WIDGET, this.leftPos + 80, this.topPos + 98,
                CONTAINER_X_OFFSET, CONTAINER_Y_OFFSET, 18, 18);
    }
}
