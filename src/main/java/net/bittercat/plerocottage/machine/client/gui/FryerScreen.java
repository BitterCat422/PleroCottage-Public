package net.bittercat.plerocottage.machine.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.FryerBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.menus.FryerMenu;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.machine.network.server.packet.FryingStartPacket;
import net.bittercat.plerocottage.machine.network.server.packet.StopFryingPacket;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class FryerScreen extends BaseMachineScreen<FryerMenu> implements RecipeUpdateListener {
    public static final ResourceLocation FRYER_GUI = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/fryer_gui.png");
    public static final ResourceLocation FRYER_GUI_WIDGET = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/fryer_gui_widget.png");

    public static final int BUTTON_X_OFFSET = 174;
    public static final int BUTTON_Y_OFFSET = 0;
    public static final int BUTTON_WIDTH = 18;
    public static final int BUTTON_HEIGHT = 18;

    public static final int HOB_ON_X_OFFSET = 0;
    public static final int HOB_ON_Y_OFFSET = 0;
    public static final int HOB_ON_WIDTH = 84;
    public static final int HOB_ON_HEIGHT = 132;

    public static final int HOB_IN_X_OFFSET = 0;
    public static final int HOB_IN_Y_OFFSET = 133;
    public static final int HOB_IN_WIDTH = 84;
    public static final int HOB_IN_HEIGHT = 102;

    public static final int OIL_X_OFFSET = 85;
    public static final int OIL_Y_OFFSET = 0;
    public static final int OIL_WIDTH = 88;
    public static final int OIL_HEIGHT = 78;

    public static final int SLOT_X_OFFSET = 85;
    public static final int SLOT_Y_OFFSET = 79;
    public static final int SLOT_WIDTH = 52;
    public static final int SLOT_HEIGHT = 52;

    public static final int FRYING_X_OFFSET = 192;
    public static final int FRYING_Y_OFFSET = 0;
    public static final int FRYING_WIDTH = 32;
    public static final int FRYING_HEIGHT = 32;

    public FryerBlockEntity blockEntity;
    public Rect2i fluidTankRect;
    private final FryerRecipeBookComponent recipeBookComponent = new FryerRecipeBookComponent(
            new ResourceLocation(PleroCottageMachine.MOD_ID, "textures/gui/recipe_book_buttons.png"),
            6 * 26
    );
    private ImageButton recipeBookButton;
    private ImageButton stopButton;
    private ImageButton startButton;
    private boolean widthTooNarrow;

    public FryerScreen(FryerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, FRYER_GUI);
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
        this.fluidTankRect = new Rect2i(this.leftPos + 140, this.topPos + 56, 18, 72);
        this.stopButton =
                new ImageButton(
                        this.leftPos + 116,
                        this.topPos + 4,
                        BUTTON_WIDTH,
                        BUTTON_HEIGHT,
                        BUTTON_X_OFFSET,
                        BUTTON_Y_OFFSET,
                        FRYER_GUI_WIDGET,
                        pButton -> {
                            this.blockEntity.isRunning = false;
                            PleroCottageMachinePacket.sendToServer(new StopFryingPacket(this.blockEntity.getBlockPos()));
                        }
                ) {
                    @Override
                    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
                        RenderSystem.enableDepthTest();
                        int CookingTime = FryerScreen.this.menu.getCookingTime();
                        int i = CookingTime / 20;
                        if (CookingTime == 0 || i > 8) {
                            pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
                        } else {
                            i++;
                            pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset + pTextureDifference * (i > 8 ? 0 : i), pWidth, pHeight, pTextureWidth, pTextureHeight);
                        }
                    }
                };
        this.addRenderableWidget(this.stopButton);
        this.startButton =
                new ImageButton(
                        this.leftPos + 140,
                        this.topPos + 4,
                        BUTTON_WIDTH,
                        BUTTON_HEIGHT,
                        BUTTON_X_OFFSET,
                        BUTTON_Y_OFFSET,
                        FRYER_GUI_WIDGET,
                        pButton -> {
                            this.blockEntity.startRunning();
                            PleroCottageMachinePacket.sendToServer(new FryingStartPacket(this.blockEntity.getBlockPos()));
                        }
                ) {
                    @Override
                    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
                        RenderSystem.enableDepthTest();
                        pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
                    }
                };
        this.addRenderableWidget(this.startButton);
        this.updateWidgetPositions();
        this.updateRecipeBookVisibility();
    }

    private void updateWidgetPositions() {
        this.recipeBookButton.setPosition(this.leftPos + 5, this.topPos + 62);
        this.stopButton.setPosition(this.leftPos + 116, this.topPos + 4);
        this.startButton.setPosition(this.leftPos + 140, this.topPos + 4);
        this.fluidTankRect = new Rect2i(this.leftPos + 140, this.topPos + 56, 18, 72);
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
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.updateRecipeBookVisibility();
        this.renderBackground(pGuiGraphics);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
            this.recipeBookComponent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        } else {
            this.recipeBookComponent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            if (this.recipeBookButton.visible) {
                this.recipeBookComponent.renderGhostRecipe(pGuiGraphics, this.leftPos, this.topPos, false, pPartialTick);
            }
            this.renderFluidTooltip(pGuiGraphics, pMouseX, pMouseY);
        }
        this.recipeBookComponent.renderTooltip(pGuiGraphics, this.leftPos, this.topPos, pMouseX, pMouseY);
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
        int cookingTime = this.menu.getCookingTime();
        FluidTank fluidTank = this.blockEntity.fluidTank;
        if (cookingTime != 0 || this.blockEntity.isRunning || !fluidTank.isEmpty()) {
            pGuiGraphics.blit(FRYER_GUI_WIDGET, this.leftPos + 23, this.topPos + 47, OIL_X_OFFSET, OIL_Y_OFFSET, OIL_WIDTH, OIL_HEIGHT);
        }
        this.renderFluid(pGuiGraphics, fluidTank);
        if (cookingTime != 0) {
            this.renderRunning(pGuiGraphics, cookingTime);
        } else {
            this.renderStop(pGuiGraphics);
        }
    }

    public void renderRunning(GuiGraphics guiGraphics, int cookingTime) {
        guiGraphics.blit(FRYER_GUI_WIDGET, this.leftPos + 25, this.topPos + 35, HOB_IN_X_OFFSET, HOB_IN_Y_OFFSET, HOB_IN_WIDTH, HOB_IN_HEIGHT);
        guiGraphics.blit(FRYER_GUI_WIDGET, this.leftPos + 51, this.topPos + 60, FRYING_X_OFFSET, FRYING_Y_OFFSET + FRYING_HEIGHT * (cookingTime % 8 / 2), FRYING_WIDTH, FRYING_HEIGHT);
    }

    public void renderStop(GuiGraphics guiGraphics) {
        guiGraphics.blit(FRYER_GUI_WIDGET, this.leftPos + 25, this.topPos + 5, HOB_ON_X_OFFSET, HOB_ON_Y_OFFSET, HOB_ON_WIDTH, HOB_ON_HEIGHT);
        guiGraphics.blit(FRYER_GUI_WIDGET, this.leftPos + 41, this.topPos + 21, SLOT_X_OFFSET, SLOT_Y_OFFSET, SLOT_WIDTH, SLOT_HEIGHT);
    }

    public void renderFluid(GuiGraphics pGuiGraphics, FluidTank fluidTank) {
        if (fluidTank.isEmpty()) return;
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidTank.getFluid().getFluid());
        ResourceLocation textureId = fluidTypeExtensions.getStillTexture();
        TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(textureId);
        SpriteContents contents = textureAtlasSprite.contents();
        int width = contents.width();
        int height = contents.height();

        int count = (int) contents.getUniqueFrames().count();
        int x = this.leftPos + 141;
        int y = this.topPos + 57;
        int renderWidth = 16;
        int renderHeight = height * 4 + 6;
        int scissorY = y + renderHeight - (renderHeight * fluidTank.getFluidAmount() / fluidTank.getCapacity());
        
        if (fluidTank.getFluidAmount() >= 3000) {
            scissorY -= 2;
        } else if (fluidTank.getFluidAmount() >= 2000) {
            scissorY -= 1;
        } else if (fluidTank.getFluidAmount() >= 1000) {
            scissorY -= 1;
        }
        int color = fluidTypeExtensions.getTintColor();
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        pGuiGraphics.enableScissor(x, scissorY, x + width, y + renderHeight);
        for (int i = 0; i < 4; i++) {
            pGuiGraphics.blit(x, y + (height + 2) * i, 0, width, height, textureAtlasSprite, r, g, b, alpha);

        }
        pGuiGraphics.disableScissor();
        pGuiGraphics.setColor(1F, 1F, 1F, 1F);
    }

    public void renderFluidTooltip(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        FluidTank fluidTank = this.blockEntity.fluidTank;
        if (!fluidTankRect.contains(pMouseX, pMouseY) || fluidTank.isEmpty() || !this.menu.getCarried().isEmpty())
            return;
        Component fluidName = fluidTank.getFluid().getDisplayName();
        if (PleroCottageCompat.isOilFluid(fluidTank.getFluid())) {
            fluidName = Component.translatable("fluid.plerocottage.olive_oil");
        }
        List<Component> fluidTooltips = List.of(fluidName, Component.literal(fluidTank.getFluidAmount() + "mB"));
        pGuiGraphics.renderComponentTooltip(this.font, fluidTooltips, pMouseX, pMouseY);
    }
}
