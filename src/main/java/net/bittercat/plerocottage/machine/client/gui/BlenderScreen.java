package net.bittercat.plerocottage.machine.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.BlenderBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.menus.BlenderMenu;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.machine.network.server.packet.BlenderStartPacket;
import net.bittercat.plerocottage.init.SoundRegistry;

@OnlyIn(Dist.CLIENT)
public class BlenderScreen extends BaseMachineScreen<BlenderMenu> implements RecipeUpdateListener {
    public static final ResourceLocation BLENDER_GUI = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/blender_gui.png");
    public static final ResourceLocation BLENDER_GUI_WIDGET_1 = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/blender_gui_widget_1.png");
    public static final ResourceLocation BLENDER_GUI_WIDGET_2 = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/blender_gui_widget_2.png");

    public static final int BUTTON_X_OFFSET = 0;
    public static final int BUTTON_Y_OFFSET = 128;
    public static final int BUTTON_WIDTH = 20;
    public static final int BUTTON_HEIGHT = 21;

    public static final int BLENDER_X_OFFSET = 0;
    public static final int BLENDER_Y_OFFSET = 0;
    public static final int BLENDER_WIDTH = 100;
    public static final int BLENDER_HEIGHT = 128;

    public static final int MAIN_INPUT_X_OFFSET = 101;
    public static final int MAIN_INPUT_Y_OFFSET = 0;
    public static final int MAIN_INPUT_WIDTH = 44;
    public static final int MAIN_INPUT_HEIGHT = 58;

    public static final int BLENDER_RUNNING_X_OFFSET = 0;
    public static final int BLENDER_RUNNING_Y_OFFSET = 0;
    public static final int BLENDER_RUNNING_WIDTH = 100;
    public static final int BLENDER_RUNNING_HEIGHT = 142;

    public static final int CONTAINER_X_OFFSET = 195;
    public static final int CONTAINER_Y_OFFSET = 0;
    public static final int CONTAINER_WIDTH = 26;
    public static final int CONTAINER_HEIGHT = 35;

    public static final int CONTAINER_WINE_X_OFFSET = 221;
    public static final int CONTAINER_WINE_Y_OFFSET = 0;
    public static final int CONTAINER_WINE_WIDTH = 22;
    public static final int CONTAINER_WINE_HEIGHT = 37;

    public static final int OUTPUT_X_OFFSET = 146;
    public static final int OUTPUT_Y_OFFSET = 0;
    public static final int OUTPUT_WIDTH = 48;
    public static final int OUTPUT_HEIGHT = 57;

    public BlenderBlockEntity blockEntity;
    private final BlenderRecipeBookComponent recipeBookComponent = new BlenderRecipeBookComponent(
            ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/recipe_book_buttons.png"),
            7 * 26
    );
    private ImageButton recipeBookButton;
    private ImageButton startButton;
    private boolean widthTooNarrow;

    public BlenderScreen(BlenderMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, BLENDER_GUI);
        this.blockEntity = pMenu.blockEntity;
        this.imageHeight = 232;
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
        this.startButton = new ImageButton(
                        this.leftPos + 114,
                        this.topPos + 117,
                        BUTTON_WIDTH,
                        BUTTON_HEIGHT,
                        BUTTON_X_OFFSET,
                        BUTTON_Y_OFFSET,
                        BLENDER_GUI_WIDGET_1,
                        pButton -> {
                            PleroCottageMachinePacket.sendToServer(new BlenderStartPacket(this.blockEntity.getBlockPos()));
                        }
                ) {
                    @Override
                    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
                        RenderSystem.enableDepthTest();
                        RenderSystem.enableBlend();
                        pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
                        if (this.isHovered()) {
                            pGuiGraphics.setColor(255, 255, 255, 0.225F);
                            pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
                            pGuiGraphics.setColor(1, 1, 1, 1);
                        }
                    }
                };
        this.addRenderableWidget(this.startButton);
        this.updateWidgetPositions();
        this.updateRecipeBookVisibility();
    }

    private void updateWidgetPositions() {
        if (this.recipeBookButton != null) {
            this.recipeBookButton.setPosition(this.leftPos + 5, this.topPos + 62);
        }
        if (this.startButton != null) {
            this.startButton.setPosition(this.leftPos + 114, this.topPos + 117);
        }
    }

    private void updateRecipeBookVisibility() {
        boolean cooking = this.menu.getCookingTime() != 0 || this.blockEntity.isRunning;
        if (this.recipeBookButton != null) {
            this.recipeBookButton.visible = !cooking;
            this.recipeBookButton.active = !cooking;
        }
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
        if (slot != null && slot.index == 10 && type != ClickType.PICKUP_ALL) {
            Level level = this.blockEntity.getLevel();
            BlockPos pos = this.blockEntity.getBlockPos();
            level.playLocalSound(pos, SoundRegistry.BLENDER_CUP_PLACE.get(), SoundSource.BLOCKS, 0.15F, 1F, true);
        }
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
        this.renderBlender(pGuiGraphics);
        this.renderInput(pGuiGraphics);
        this.renderBottle(pGuiGraphics);
        this.renderOutput(pGuiGraphics);
    }

    public void renderBlender(GuiGraphics guiGraphics) {
        int cookingTime = this.menu.getCookingTime();
        if (cookingTime == 0) {
            guiGraphics.blit(BLENDER_GUI_WIDGET_1, this.leftPos + 74, this.topPos + 14, BLENDER_X_OFFSET, BLENDER_Y_OFFSET, BLENDER_WIDTH, BLENDER_HEIGHT);
        } else {
            guiGraphics.blit(BLENDER_GUI_WIDGET_2, this.leftPos + 74, this.topPos, BLENDER_RUNNING_X_OFFSET + BLENDER_RUNNING_WIDTH * ((cookingTime / 2) % 2), BLENDER_RUNNING_Y_OFFSET, BLENDER_RUNNING_WIDTH, BLENDER_RUNNING_HEIGHT);
        }
    }

    public void renderInput(GuiGraphics guiGraphics) {
        if (this.menu.getCookingTime() != 0) return;
        guiGraphics.blit(BLENDER_GUI_WIDGET_1, this.leftPos + 102, this.topPos + 53, MAIN_INPUT_X_OFFSET, MAIN_INPUT_Y_OFFSET, MAIN_INPUT_WIDTH, MAIN_INPUT_HEIGHT);
    }

    public void renderBottle(GuiGraphics guiGraphics) {
        ItemStack bottle = this.blockEntity.getItem(BlenderBlockEntity.CONTAINER);
        if (bottle.isEmpty()) return;
        if (PleroCottageCompat.isDeformedGlassBottle(bottle)) {
            guiGraphics.blit(BLENDER_GUI_WIDGET_1, this.leftPos + 23, this.topPos + 101, CONTAINER_X_OFFSET, CONTAINER_Y_OFFSET, CONTAINER_WIDTH, CONTAINER_HEIGHT);
        } else if (PleroCottageCompat.isDeformedWineBottle(bottle)) {
            guiGraphics.blit(BLENDER_GUI_WIDGET_1, this.leftPos + 25, this.topPos + 99, CONTAINER_WINE_X_OFFSET, CONTAINER_WINE_Y_OFFSET, CONTAINER_WINE_WIDTH, CONTAINER_WINE_HEIGHT);
        }
    }

    public void renderOutput(GuiGraphics guiGraphics) {
        if (this.blockEntity.getItem(BlenderBlockEntity.MAIN_OUTPUT).isEmpty() && this.blockEntity.getItem(BlenderBlockEntity.SECONDARY_OUTPUT).isEmpty())
            return;
        guiGraphics.blit(BLENDER_GUI_WIDGET_1, this.leftPos + 12, this.topPos + 84, OUTPUT_X_OFFSET, OUTPUT_Y_OFFSET, OUTPUT_WIDTH, OUTPUT_HEIGHT);
    }
}
