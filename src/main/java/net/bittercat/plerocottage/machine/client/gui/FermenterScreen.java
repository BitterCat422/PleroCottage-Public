package net.bittercat.plerocottage.machine.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.FermenterBlockEntity;
import net.bittercat.plerocottage.machine.menus.FermenterMenu;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.machine.network.server.packet.FermentationStartPacket;
import net.bittercat.plerocottage.machine.network.server.packet.FermentationStopPacket;

@OnlyIn(Dist.CLIENT)
public class FermenterScreen extends BaseMachineScreen<FermenterMenu> implements RecipeUpdateListener {
    public static final ResourceLocation FERMENTER_GUI = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/fermenter_gui.png");
    public static final ResourceLocation FERMENTER_GUI_WIDGET = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/fermenter_gui_widget.png");

    public static final int START_BUTTON_X_OFFSET = 50;
    public static final int START_BUTTON_Y_OFFSET = 133;
    public static final int START_BUTTON_WIDTH = 30;
    public static final int START_BUTTON_HEIGHT = 18;

    public static final int STOP_BUTTON_X_OFFSET = 81;
    public static final int STOP_BUTTON_Y_OFFSET = 133;
    public static final int STOP_BUTTON_WIDTH = 35;
    public static final int STOP_BUTTON_HEIGHT = 20;

    public static final int DIM_CHECK_X_OFFSET = 118;
    public static final int DIM_CHECK_Y_OFFSET = 133;
    public static final int DIM_CHECK_WIDTH = 16;
    public static final int DIM_CHECK_HEIGHT = 15;

    public static final int NORMAL_CHECK_X_OFFSET = 118;
    public static final int NORMAL_CHECK_Y_OFFSET = 148;
    public static final int NORMAL_CHECK_WIDTH = 16;
    public static final int NORMAL_CHECK_HEIGHT = 15;

    public static final int BRIGHT_CHECK_X_OFFSET = 118;
    public static final int BRIGHT_CHECK_Y_OFFSET = 163;
    public static final int BRIGHT_CHECK_WIDTH = 16;
    public static final int BRIGHT_CHECK_HEIGHT = 15;

    public static final int RUNNING_X_OFFSET = 0;
    public static final int RUNNING_Y_OFFSET = 0;
    public static final int RUNNING_WIDTH = 124;
    public static final int RUNNING_HEIGHT = 132;

    public static final int DONE_X_OFFSET = 125;
    public static final int DONE_Y_OFFSET = 0;
    public static final int DONE_WIDTH = 124;
    public static final int DONE_HEIGHT = 132;

    public static final int RUNNING_TIPS_X_OFFSET = 0;
    public static final int RUNNING_TIPS_Y_OFFSET = 133;
    public static final int RUNNING_TIPS_WIDTH = 49;
    public static final int RUNNING_TIPS_HEIGHT = 58;

    public static final int DONE_TIPS_X_OFFSET = 0;
    public static final int DONE_TIPS_Y_OFFSET = 191;
    public static final int DONE_TIPS_WIDTH = 49;
    public static final int DONE_TIPS_HEIGHT = 58;

    public FermenterBlockEntity blockEntity;
    private final FermenterRecipeBookComponent recipeBookComponent = new FermenterRecipeBookComponent(
            ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/recipe_book_buttons.png"),
            4 * 26
    );
    private ImageButton recipeBookButton;
    private ImageButton startButton;
    private ImageButton stopButton;
    private boolean widthTooNarrow;

    public FermenterScreen(FermenterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, FERMENTER_GUI);
        this.blockEntity = pMenu.blockEntity;
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.recipeBookButton = new ImageButton(this.leftPos + 5, this.topPos + 62, 20, 18, 0, 0, 19,
                new ResourceLocation("textures/gui/recipe_button.png"), button -> {
                    this.recipeBookComponent.toggleVisibility();
                    this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
                    this.updateWidgetPositions();
                });
        this.addRenderableWidget(this.recipeBookButton);
        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
        this.addRenderableWidget(
                this.startButton = new ImageButton(
                        this.leftPos + 142,
                        this.topPos + 115,
                        START_BUTTON_WIDTH,
                        START_BUTTON_HEIGHT,
                        START_BUTTON_X_OFFSET,
                        START_BUTTON_Y_OFFSET,
                        FERMENTER_GUI_WIDGET,
                        pButton -> {
                            PleroCottageMachinePacket.sendToServer(new FermentationStartPacket(this.blockEntity.getBlockPos()));
                        }
                ) {
                    @Override
                    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
                        if (!this.isActive()) return;
                        RenderSystem.enableDepthTest();
                        pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset + (this.isHovered() ? pTextureDifference : 0), pWidth, pHeight, pTextureWidth, pTextureHeight);
                    }

                    @Override
                    protected boolean isValidClickButton(int pButton) {
                        return super.isValidClickButton(pButton) && menu.getCookingTime() == 0;
                    }

                    @Override
                    public boolean isActive() {
                        return super.isActive() && menu.getCookingTime() == 0;
                    }
                }
        );

        this.addRenderableWidget(
                this.stopButton = new ImageButton(
                        this.leftPos + 139,
                        this.topPos + 114,
                        STOP_BUTTON_WIDTH,
                        STOP_BUTTON_HEIGHT,
                        STOP_BUTTON_X_OFFSET,
                        STOP_BUTTON_Y_OFFSET,
                        FERMENTER_GUI_WIDGET,
                        pButton -> {
                            PleroCottageMachinePacket.sendToServer(new FermentationStopPacket(this.blockEntity.getBlockPos()));
                        }
                ) {
                    @Override
                    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
                        if (!this.isActive()) return;
                        RenderSystem.enableDepthTest();
                        pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset + (this.isHovered() ? pTextureDifference : 0), pWidth, pHeight, pTextureWidth, pTextureHeight);
                    }

                    @Override
                    protected boolean isValidClickButton(int pButton) {
                        return super.isValidClickButton(pButton) && menu.getCookingTime() != 0;
                    }

                    @Override
                    public boolean isActive() {
                        return super.isActive() && menu.getCookingTime() != 0;
                    }
                }
        );
        this.updateWidgetPositions();
        this.updateRecipeBookVisibility();
    }

    private void updateWidgetPositions() {
        if (this.recipeBookButton != null) {
            this.recipeBookButton.setPosition(this.leftPos + 5, this.topPos + 62);
        }
        if (this.startButton != null) {
            this.startButton.setPosition(this.leftPos + 142, this.topPos + 115);
        }
        if (this.stopButton != null) {
            this.stopButton.setPosition(this.leftPos + 139, this.topPos + 114);
        }
    }

    private void updateRecipeBookVisibility() {
        boolean running = this.menu.getCookingTime() != 0 || this.blockEntity.isRunning;
        this.recipeBookButton.visible = !running;
        this.recipeBookButton.active = !running;
        if (running && this.recipeBookComponent.isVisible()) {
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
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        this.renderLightState(pGuiGraphics);
        this.renderRunning(pGuiGraphics);
        this.renderRunningTip(pGuiGraphics);
        this.renderCookingTime(pGuiGraphics);
    }

    public void renderLightState(GuiGraphics guiGraphics) {
        switch (this.blockEntity.getLightState()) {
            case DIM -> this.renderDimLightState(guiGraphics);
            case NORMAL -> this.renderNormalLightState(guiGraphics);
            case BRIGHT -> this.renderBrightLightState(guiGraphics);
        }
    }

    public void renderDimLightState(GuiGraphics guiGraphics) {
        guiGraphics.blit(FERMENTER_GUI_WIDGET, this.leftPos + 117, this.topPos + 24, DIM_CHECK_X_OFFSET, DIM_CHECK_Y_OFFSET, DIM_CHECK_WIDTH, DIM_CHECK_HEIGHT);
    }

    public void renderNormalLightState(GuiGraphics guiGraphics) {
        guiGraphics.blit(FERMENTER_GUI_WIDGET, this.leftPos + 137, this.topPos + 24, NORMAL_CHECK_X_OFFSET, NORMAL_CHECK_Y_OFFSET, NORMAL_CHECK_WIDTH, NORMAL_CHECK_HEIGHT);
    }

    public void renderBrightLightState(GuiGraphics guiGraphics) {
        guiGraphics.blit(FERMENTER_GUI_WIDGET, this.leftPos + 157, this.topPos + 24, BRIGHT_CHECK_X_OFFSET, BRIGHT_CHECK_Y_OFFSET, BRIGHT_CHECK_WIDTH, BRIGHT_CHECK_HEIGHT);
    }

    public void renderRunning(GuiGraphics guiGraphics) {
        if (this.menu.getCookingTime() != 0) {
            guiGraphics.blit(FERMENTER_GUI_WIDGET, this.leftPos + 2, this.topPos + 5, RUNNING_X_OFFSET, RUNNING_Y_OFFSET, RUNNING_WIDTH, RUNNING_HEIGHT);
        } else if (!this.blockEntity.getItem(FermenterBlockEntity.OUTPUT).isEmpty()) {
            guiGraphics.blit(FERMENTER_GUI_WIDGET, this.leftPos + 2, this.topPos + 5, DONE_X_OFFSET, DONE_Y_OFFSET, DONE_WIDTH, DONE_HEIGHT);
        }
    }

    public void renderRunningTip(GuiGraphics guiGraphics) {
        if (this.menu.getCookingTime() != 0) {
            guiGraphics.blit(FERMENTER_GUI_WIDGET, this.leftPos + 126, this.topPos + 40, RUNNING_TIPS_X_OFFSET, RUNNING_TIPS_Y_OFFSET, RUNNING_TIPS_WIDTH, RUNNING_TIPS_HEIGHT);
        } else if (!this.blockEntity.getItem(FermenterBlockEntity.OUTPUT).isEmpty()) {
            guiGraphics.blit(FERMENTER_GUI_WIDGET, this.leftPos + 126, this.topPos + 40, DONE_TIPS_X_OFFSET, DONE_TIPS_Y_OFFSET, DONE_TIPS_WIDTH, DONE_TIPS_HEIGHT);
        }
    }

    public void renderCookingTime(GuiGraphics guiGraphics) {
        int cookingTime = this.menu.getCookingTime();
        int maxCookingTime = this.menu.getMaxCookingTime();
        if (cookingTime != 0) {
            String text = maxCookingTime - cookingTime + 1 + "s";
            guiGraphics.drawString(
                    this.font,
                    text,
                    this.leftPos + 153 - this.font.width(text) / 2,
                    this.topPos + 77,
                    -12840447,
                    false
            );
        }
    }
}
