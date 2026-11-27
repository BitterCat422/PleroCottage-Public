package net.bittercat.plerocottage.machine.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.SaucepanAndWhiskBlockEntity;
import net.bittercat.plerocottage.machine.menus.SaucepanAndWhiskMenu;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.machine.network.server.packet.TrySaucepanAndWhiskCraftPacket;
import net.bittercat.plerocottage.machine.recipes.SaucepanAndWhiskRecipe;

@OnlyIn(Dist.CLIENT)
public class SaucepanAndWhiskScreen extends BaseMachineScreen<SaucepanAndWhiskMenu> implements RecipeUpdateListener {
    public static final ResourceLocation SAUCEPAN_AND_WHISK_GUI = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/saucepan_and_whisk_gui.png");
    public static final ResourceLocation SAUCEPAN_AND_WHISK_GUI_WIDGET = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/saucepan_and_whisk_gui_widget.png");

    public static final int BUTTON_X_OFFSET = 0;
    public static final int BUTTON_Y_OFFSET = 0;
    public static final int BUTTON_WIDTH = 126;
    public static final int BUTTON_HEIGHT = 107;

    public static final int WHISK_X_OFFSET = 158;
    public static final int WHISK_Y_OFFSET = 0;
    public static final int WHISK_WIDTH = 98;
    public static final int WHISK_HEIGHT = 112;

    public static final int OUTPUT_SLOT_X_OFFSET = 0;
    public static final int OUTPUT_SLOT_Y_OFFSET = 128;
    public static final int OUTPUT_SLOT_WIDTH = 26;
    public static final int OUTPUT_SLOT_HEIGHT = 26;

    public static final int SLOT_X_OFFSET = 0;
    public static final int SLOT_Y_OFFSET = 0;
    public static final int SLOT_WIDTH = 112;
    public static final int SLOT_HEIGHT = 101;

    public static final int HAS_HEAT_X_OFFSET = 0;
    public static final int HAS_HEAT_Y_OFFSET = 115;
    public static final int HAS_HEAT_WIDTH = 16;
    public static final int HAS_HEAT_HEIGHT = 11;

    public static final int NO_HEAT_X_OFFSET = 0;
    public static final int NO_HEAT_Y_OFFSET = 102;
    public static final int NO_HEAT_WIDTH = 12;
    public static final int NO_HEAT_HEIGHT = 12;

    public static final int STIRS_X_OFFSET = 113;
    public static final int STIRS_Y_OFFSET = 0;
    public static final int STIRS_WIDTH = 14;
    public static final int STIRS_HEIGHT = 73;

    public static final int STIRS_0_X_OFFSET = 128;
    public static final int STIRS_0_Y_OFFSET = 48;
    public static final int STIRS_0_WIDTH = 10;
    public static final int STIRS_0_HEIGHT = 17;

    public static final int STIRS_1_X_OFFSET = 128;
    public static final int STIRS_1_Y_OFFSET = 28;
    public static final int STIRS_1_WIDTH = 10;
    public static final int STIRS_1_HEIGHT = 19;

    public static final int STIRS_2_X_OFFSET = 128;
    public static final int STIRS_2_Y_OFFSET = 0;
    public static final int STIRS_2_WIDTH = 10;
    public static final int STIRS_2_HEIGHT = 27;

    public SaucepanAndWhiskBlockEntity blockEntity;
    public ImageButton whiskButton;
    private final SaucepanAndWhiskRecipeBookComponent recipeBookComponent = new SaucepanAndWhiskRecipeBookComponent(
            ResourceLocation.fromNamespaceAndPath(
                    PleroCottageMachine.MOD_ID,
                    "textures/gui/recipe_book_buttons.png"
            ),
            5 * 26
    );
    private ImageButton recipeBookButton;
    private boolean widthTooNarrow;
    public Rect2i mainInputRect;
    public Rect2i secondaryInputRect;
    public Rect2i outputRect;
    public boolean whiskStir = false;

    public SaucepanAndWhiskScreen(SaucepanAndWhiskMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, SAUCEPAN_AND_WHISK_GUI);
        this.blockEntity = pMenu.blockEntity;
        this.imageHeight = 236;
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth) + 2;
        this.recipeBookButton = new ImageButton(
                this.leftPos + 5,
                this.topPos + 72,
                20,
                18,
                0,
                0,
                19,
                new ResourceLocation("textures/gui/recipe_button.png"),
                button -> {
                    this.recipeBookComponent.toggleVisibility();
                    this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth) + 2;
                    this.updateWidgetPositions();
                }
        );
        this.addRenderableWidget(this.recipeBookButton);
        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
        this.mainInputRect = new Rect2i(
                this.leftPos + 60,
                this.topPos + 66,
                56,
                38
        );
        this.secondaryInputRect = new Rect2i(
                this.leftPos + 4,
                this.topPos + 3,
                42,
                62
        );
        this.outputRect = new Rect2i(
                this.leftPos + 79,
                this.topPos + 23,
                18,
                40
        );
        this.whiskButton = this.addWidget(
                new ImageButton(
                        this.leftPos + 25,
                        this.topPos + 3,
                        BUTTON_WIDTH,
                        BUTTON_HEIGHT,
                        BUTTON_X_OFFSET,
                        BUTTON_Y_OFFSET,
                        SAUCEPAN_AND_WHISK_GUI_WIDGET,
                        pButton -> {
                            boolean flag = false;
                            for (SaucepanAndWhiskRecipe recipe : blockEntity.getAllRecipe()) {
                                if (recipe.matches(blockEntity)) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (flag) {
                                BlockPos blockPos = this.blockEntity.getBlockPos();
                                whiskStir = !whiskStir;
                                PleroCottageMachinePacket.sendToServer(new TrySaucepanAndWhiskCraftPacket(blockPos));
                            } else {
                                this.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                            }
                        }
                ) {
                    private boolean onWhisk(double pMouseX, double pMouseY) {
                        return !mainInputRect.contains((int) pMouseX, (int) pMouseY)
                                && !secondaryInputRect.contains((int) pMouseX, (int) pMouseY)
                                && !outputRect.contains((int) pMouseX, (int) pMouseY);
                    }

                    private void setColor(GuiGraphics guiGraphics) {
                        if (this.isHovered()) {
                            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.225F);
                        }
                    }

                    private void resetColor(GuiGraphics guiGraphics) {
                        if (this.isHovered()) {
                            guiGraphics.setColor(1, 1, 1, 1);
                        }
                    }

                    @Override
                    public void playDownSound(SoundManager pHandler) {
                    }

                    @Override
                    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
                        this.isHovered = this.isHovered && this.onWhisk(pMouseX, pMouseY);
                        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
                    }

                    @Override
                    protected boolean clicked(double pMouseX, double pMouseY) {
                        return super.clicked(pMouseX, pMouseY) && this.onWhisk(pMouseX, pMouseY);
                    }

                    @Override
                    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
                        RenderSystem.enableDepthTest();
                        RenderSystem.enableBlend();
                        if (whiskStir) {
                            int yOffset = WHISK_Y_OFFSET + WHISK_HEIGHT;
                            pGuiGraphics.blit(pTexture, SaucepanAndWhiskScreen.this.leftPos + 2, SaucepanAndWhiskScreen.this.topPos - 7, WHISK_X_OFFSET, yOffset, WHISK_WIDTH, WHISK_HEIGHT);
                            this.setColor(pGuiGraphics);
                            pGuiGraphics.blit(pTexture, SaucepanAndWhiskScreen.this.leftPos + 2, SaucepanAndWhiskScreen.this.topPos - 7, WHISK_X_OFFSET, yOffset, WHISK_WIDTH, WHISK_HEIGHT);
                            this.resetColor(pGuiGraphics);
                        } else {
                            pGuiGraphics.blit(pTexture, SaucepanAndWhiskScreen.this.leftPos + 72, SaucepanAndWhiskScreen.this.topPos - 7, WHISK_X_OFFSET, WHISK_Y_OFFSET, WHISK_WIDTH, WHISK_HEIGHT);
                            this.setColor(pGuiGraphics);
                            pGuiGraphics.blit(pTexture, SaucepanAndWhiskScreen.this.leftPos + 72, SaucepanAndWhiskScreen.this.topPos - 7, WHISK_X_OFFSET, WHISK_Y_OFFSET, WHISK_WIDTH, WHISK_HEIGHT);
                            this.resetColor(pGuiGraphics);
                        }
                    }
                }
        );
        this.updateWidgetPositions();
        this.updateRecipeBookVisibility();
    }

    private void updateWidgetPositions() {
        if (this.recipeBookButton != null) {
            this.recipeBookButton.setPosition(this.leftPos + 5, this.topPos + 72);
        }
        if (this.whiskButton != null) {
            this.whiskButton.setPosition(this.leftPos + 25, this.topPos + 3);
        }
        this.mainInputRect = new Rect2i(this.leftPos + 60, this.topPos + 66, 56, 38);
        this.secondaryInputRect = new Rect2i(this.leftPos + 4, this.topPos + 3, 42, 62);
        this.outputRect = new Rect2i(this.leftPos + 79, this.topPos + 23, 18, 40);
    }

    private void updateRecipeBookVisibility() {
        boolean stirring = this.menu.getStirsCount() != 0;
        if (this.recipeBookButton != null) {
            this.recipeBookButton.visible = !stirring;
            this.recipeBookButton.active = !stirring;
        }
        if (stirring && this.recipeBookComponent.isVisible()) {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth) + 2;
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
        pGuiGraphics.blit(SAUCEPAN_AND_WHISK_GUI, this.leftPos - 17, this.topPos, 17, 0, 208, 236);
        this.whiskButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderSlot(pGuiGraphics);
        this.renderHeat(pGuiGraphics);
        this.renderStirsCount(pGuiGraphics);
    }

    public void renderSlot(GuiGraphics pGuiGraphics) {
        pGuiGraphics.blit(SAUCEPAN_AND_WHISK_GUI_WIDGET, this.leftPos + 4, this.topPos + 3, SLOT_X_OFFSET, SLOT_Y_OFFSET, SLOT_WIDTH, SLOT_HEIGHT);
        if (this.blockEntity.getItem(SaucepanAndWhiskBlockEntity.RESULT).isEmpty()) {
            return;
        }
        pGuiGraphics.blit(
                SAUCEPAN_AND_WHISK_GUI_WIDGET,
                this.leftPos + 75,
                this.topPos + 19,
                OUTPUT_SLOT_X_OFFSET,
                OUTPUT_SLOT_Y_OFFSET,
                OUTPUT_SLOT_WIDTH,
                OUTPUT_SLOT_HEIGHT
        );
    }

    public void renderHeat(GuiGraphics pGuiGraphics) {
        if (this.blockEntity.isOnHeatBlock()) {
            pGuiGraphics.blit(SAUCEPAN_AND_WHISK_GUI_WIDGET, this.leftPos + 26, this.topPos + 13, HAS_HEAT_X_OFFSET, HAS_HEAT_Y_OFFSET, HAS_HEAT_WIDTH, HAS_HEAT_HEIGHT);
        } else {
            pGuiGraphics.blit(SAUCEPAN_AND_WHISK_GUI_WIDGET, this.leftPos + 27, this.topPos + 13, NO_HEAT_X_OFFSET, NO_HEAT_Y_OFFSET, NO_HEAT_WIDTH, NO_HEAT_HEIGHT);
        }
    }

    public void renderStirsCount(GuiGraphics pGuiGraphics) {
        pGuiGraphics.blit(SAUCEPAN_AND_WHISK_GUI_WIDGET, this.leftPos + 158, this.topPos + 64, STIRS_X_OFFSET, STIRS_Y_OFFSET, STIRS_WIDTH, STIRS_HEIGHT);
        int stirsCount = this.menu.getStirsCount() / 2;
        int[][] stirsLevel = {
                {160, 116, STIRS_0_X_OFFSET, STIRS_0_Y_OFFSET, STIRS_0_WIDTH, STIRS_0_HEIGHT},
                {160, 95, STIRS_1_X_OFFSET, STIRS_1_Y_OFFSET, STIRS_1_WIDTH, STIRS_1_HEIGHT},
                {160, 66, STIRS_2_X_OFFSET, STIRS_2_Y_OFFSET, STIRS_2_WIDTH, STIRS_2_HEIGHT}
        };
        for (int i = 0; i < stirsCount && i < stirsLevel.length; i++) {
            pGuiGraphics.blit(
                    SAUCEPAN_AND_WHISK_GUI_WIDGET,
                    this.leftPos + stirsLevel[i][0],
                    this.topPos + stirsLevel[i][1],
                    stirsLevel[i][2],
                    stirsLevel[i][3],
                    stirsLevel[i][4],
                    stirsLevel[i][5]
            );
        }
    }
}
