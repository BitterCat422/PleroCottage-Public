package net.bittercat.plerocottage.machine.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.StockPotBlockEntity;
import net.bittercat.plerocottage.machine.menus.StockPotMenu;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.machine.network.server.packet.StockPotStartPacket;
import net.bittercat.plerocottage.machine.network.server.packet.StockPotStopPacket;

@OnlyIn(Dist.CLIENT)
public class StockPotScreen extends BaseMachineScreen<StockPotMenu> implements RecipeUpdateListener {
    public static final ResourceLocation STOCK_POT_GUI = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/stock_pot_gui.png");
    public static final ResourceLocation STOCK_POT_GUI_WIDGET = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/stock_pot_gui_widget.png");
    public static final String TRANSLATABLE = "block.plerocottage.stock_pot.tooltip";

    public static final int BUTTON_X_OFFSET = 166;
    public static final int BUTTON_Y_OFFSET = 182;
    public static final int BUTTON_WIDTH = 90;
    public static final int BUTTON_HEIGHT = 74;

    public static final int MAIN_INPUT_X_OFFSET = 25;
    public static final int MAIN_INPUT_Y_OFFSET = 0;
    public static final int MAIN_INPUT_WIDTH = 78;
    public static final int MAIN_INPUT_HEIGHT = 60;

    public static final int RESULT_SLOT_X_OFFSET = 224;
    public static final int RESULT_SLOT_Y_OFFSET = 0;
    public static final int RESULT_SLOT_WIDTH = 26;
    public static final int RESULT_SLOT_HEIGHT = 26;

    public static final int SPOON_X_OFFSET = 112;
    public static final int SPOON_Y_OFFSET = 0;
    public static final int SPOON_WIDTH = 95;
    public static final int SPOON_HEIGHT = 50;

    public static final int SECONDARY_INPUT_X_OFFSET = 192;
    public static final int SECONDARY_INPUT_Y_OFFSET = 112;
    public static final int SECONDARY_INPUT_WIDTH = 40;
    public static final int SECONDARY_INPUT_HEIGHT = 40;

    public static final int RUNNING_X_OFFSET = 0;
    public static final int RUNNING_Y_OFFSET = 64;
    public static final int RUNNING_WIDTH = 176;
    public static final int RUNNING_HEIGHT = 120;

    public static final int RUNNING_BAR_X_OFFSET = 32;
    public static final int RUNNING_BAR_Y_OFFSET = 192;
    public static final int RUNNING_BAR_WIDTH = 101;
    public static final int RUNNING_BAR_HEIGHT = 18;

    public StockPotBlockEntity blockEntity;
    public Player player;
    private final StockPotRecipeBookComponent recipeBookComponent = new StockPotRecipeBookComponent(
            ResourceLocation.fromNamespaceAndPath(
                    PleroCottageMachine.MOD_ID,
                    "textures/gui/recipe_book_buttons.png"
            ),
            2 * 26
    );
    private boolean widthTooNarrow;
    private ImageButton recipeBookButton;
    private ImageButton startButton;
    private ImageButton stopButton;
    private Rect2i recipeBookRect;
    private Rect2i containerRect;
    private Rect2i outputRect;

    public StockPotScreen(StockPotMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, STOCK_POT_GUI);
        this.blockEntity = pMenu.blockEntity;
        this.player = pPlayerInventory.player;
        this.imageHeight = 232;
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.recipeBookButton = new ImageButton(
                this.leftPos + 5,
                this.height / 2 - 49,
                20,
                18,
                0,
                0,
                19,
                new ResourceLocation("textures/gui/recipe_button.png"),
                button -> {
                    this.recipeBookComponent.toggleVisibility();
                    this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
                    this.updateWidgetPositions();
                }
        );
        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
        this.startButton =
                new ImageButton(
                        this.leftPos + 1,
                        this.topPos + 10,
                        BUTTON_WIDTH,
                        BUTTON_HEIGHT,
                        BUTTON_X_OFFSET,
                        BUTTON_Y_OFFSET,
                        STOCK_POT_GUI_WIDGET,
                        pButton -> {
                            if (this.blockEntity.isOnStockPotHeatBlock()) {
                                PleroCottageMachinePacket.sendToServer(new StockPotStartPacket(this.blockEntity.getBlockPos()));
                            } else {
                                this.player.displayClientMessage(Component.translatable(TRANSLATABLE), true);
                            }
                        }
                ) {
                    private boolean isOverSlot(double mouseX, double mouseY) {
                        return recipeBookRect.contains((int) mouseX, (int) mouseY)
                                || containerRect.contains((int) mouseX, (int) mouseY)
                                || outputRect.contains((int) mouseX, (int) mouseY);
                    }

                    @Override
                    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                        this.isHovered = this.isHovered && !this.isOverSlot(mouseX, mouseY);
                        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
                    }

                    @Override
                    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
                        if (menu.getCookingTime() == 0) {
                            RenderSystem.enableDepthTest();
                            RenderSystem.enableBlend();
                            pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
                            if (this.isHovered()) {
                                pGuiGraphics.setColor(255, 255, 255, 0.225F);
                                pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
                                pGuiGraphics.setColor(1, 1, 1, 1);
                            }
                        }
                    }

                    @Override
                    protected boolean clicked(double pMouseX, double pMouseY) {
                        return super.clicked(pMouseX, pMouseY)
                                && menu.getCookingTime() == 0
                                && !this.isOverSlot(pMouseX, pMouseY);
                    }

                    @Override
                    public boolean isActive() {
                        return super.isActive() && menu.getCookingTime() == 0;
                    }
                };
        this.addRenderableWidget(this.startButton);
        this.addRenderableOnly((gui, mouseX, mouseY, partialTick) ->
                gui.blit(STOCK_POT_GUI_WIDGET, this.leftPos + 81, this.topPos, SPOON_X_OFFSET, SPOON_Y_OFFSET, SPOON_WIDTH, SPOON_HEIGHT)
        );
        this.addRenderableOnly((gui, mouseX, mouseY, partialTick) -> this.renderInput(gui));
        this.stopButton =
                new ImageButton(
                        this.leftPos + 1,
                        this.topPos + 21,
                        RUNNING_WIDTH,
                        RUNNING_HEIGHT,
                        RUNNING_X_OFFSET,
                        RUNNING_Y_OFFSET,
                        STOCK_POT_GUI_WIDGET,
                        pButton -> {
                            PleroCottageMachinePacket.sendToServer(new StockPotStopPacket(this.blockEntity.getBlockPos()));
                        }
                ){
                    @Override
                    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
                        if (menu.getCookingTime() != 0) {
                            RenderSystem.enableDepthTest();
                            RenderSystem.enableBlend();
                            pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
                            if (this.isHovered()) {
                                pGuiGraphics.setColor(255, 255, 255, 0.225F);
                                pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
                                pGuiGraphics.setColor(1, 1, 1, 1);
                            }
                            renderRunning(pGuiGraphics);
                        }
                    }

                    @Override
                    protected boolean clicked(double pMouseX, double pMouseY) {
                        return super.clicked(pMouseX, pMouseY) && menu.getCookingTime() != 0;
                    }

                    @Override
                    public boolean isActive() {
                        return super.isActive() && menu.getCookingTime() != 0;
                    }
                };
        this.addRenderableWidget(this.stopButton);
        this.addRenderableWidget(this.recipeBookButton);
        this.addRenderableOnly((gui, mouseX, mouseY, partialTick) -> this.renderOutputSlotLayer(gui));
        this.updateWidgetPositions();
        this.updateRecipeBookVisibility();
    }

    private void updateRecipeBookVisibility() {
        boolean cooking = this.blockEntity.isRunning || this.menu.getCookingTime() != 0;
        this.recipeBookButton.visible = !cooking;
        this.recipeBookButton.active = !cooking;
        if (cooking && this.recipeBookComponent.isVisible()) {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            this.updateWidgetPositions();
        }
    }

    private void updateWidgetPositions() {
        if (this.recipeBookButton != null) {
            this.recipeBookButton.setPosition(this.leftPos + 5, this.height / 2 - 49);
        }
        if (this.startButton != null) {
            this.startButton.setPosition(this.leftPos + 1, this.topPos + 10);
        }
        if (this.stopButton != null) {
            this.stopButton.setPosition(this.leftPos + 1, this.topPos + 21);
        }
        this.recipeBookRect = new Rect2i(this.leftPos + 5, this.height / 2 - 49, 20, 18);
        this.containerRect = new Rect2i(this.leftPos + 79, this.topPos + 68, 18, 18);
        this.outputRect = new Rect2i(this.leftPos + 75, this.topPos + 40, RESULT_SLOT_WIDTH, RESULT_SLOT_HEIGHT);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
        this.updateRecipeBookVisibility();
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        this.updateRecipeBookVisibility();
        this.renderBackground(gui);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(gui, partialTick, mouseX, mouseY);
            this.recipeBookComponent.render(gui, mouseX, mouseY, partialTick);
        } else {
            this.recipeBookComponent.render(gui, mouseX, mouseY, partialTick);
            super.render(gui, mouseX, mouseY, partialTick);
            if (this.recipeBookButton.visible) {
                this.recipeBookComponent.renderGhostRecipe(gui, this.leftPos, this.topPos, false, partialTick);
            }
        }
        this.recipeBookComponent.renderTooltip(gui, this.leftPos, this.topPos, mouseX, mouseY);
    }

    private void renderOutputSlotLayer(GuiGraphics gui) {
        if (!this.blockEntity.getItem(StockPotBlockEntity.RESULT).isEmpty()) {
            gui.blit(STOCK_POT_GUI_WIDGET, this.leftPos + 75, this.topPos + 40, RESULT_SLOT_X_OFFSET, RESULT_SLOT_Y_OFFSET, RESULT_SLOT_WIDTH, RESULT_SLOT_HEIGHT);
        }
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible())
                && super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, buttonId)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        }
        return (this.widthTooNarrow && this.recipeBookComponent.isVisible())
                || super.mouseClicked(mouseX, mouseY, buttonId);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int x, int y, int buttonId) {
        boolean outside = mouseX < x || mouseY < y
                || mouseX >= x + this.imageWidth
                || mouseY >= y + this.imageHeight;
        return outside && this.recipeBookComponent.hasClickedOutside(
                mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, buttonId
        );
    }

    @Override
    protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType clickType) {
        super.slotClicked(slot, mouseX, mouseY, clickType);
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

    public void renderInput(GuiGraphics pGuiGraphics) {
        if (this.menu.getCookingTime() != 0) return;
        pGuiGraphics.blit(STOCK_POT_GUI_WIDGET, this.leftPos + 49, this.topPos + 68, MAIN_INPUT_X_OFFSET, MAIN_INPUT_Y_OFFSET, MAIN_INPUT_WIDTH, MAIN_INPUT_HEIGHT);
        pGuiGraphics.blit(STOCK_POT_GUI_WIDGET, this.leftPos + 99, this.topPos + 6, SECONDARY_INPUT_X_OFFSET, SECONDARY_INPUT_Y_OFFSET, SECONDARY_INPUT_WIDTH, SECONDARY_INPUT_HEIGHT);
    }

    public void renderRunning(GuiGraphics pGuiGraphics) {
        int cookingTime = this.menu.getCookingTime();
        if (cookingTime == 0) return;
        int x = this.leftPos + 37;
        int y = this.topPos + 29;
        int maxCookingTime = this.menu.getMaxCookingTime();
        pGuiGraphics.enableScissor(x, y, x + RUNNING_BAR_WIDTH * cookingTime / maxCookingTime, y + RUNNING_BAR_HEIGHT);
        pGuiGraphics.blit(STOCK_POT_GUI_WIDGET, x, y, RUNNING_BAR_X_OFFSET, RUNNING_BAR_Y_OFFSET, RUNNING_BAR_WIDTH, RUNNING_BAR_HEIGHT);
        pGuiGraphics.disableScissor();
    }
}
