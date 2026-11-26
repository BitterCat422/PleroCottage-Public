package net.bittercat.plerocottage.machine.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.TeapotBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.menus.TeapotMenu;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.machine.network.server.packet.BrewingStartPacket;
import net.bittercat.plerocottage.init.SoundRegistry;

@OnlyIn(Dist.CLIENT)
public class TeapotScreen extends BaseMachineScreen<TeapotMenu> implements RecipeUpdateListener {
    public static final ResourceLocation TEAPOT_GUI = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/teapot_gui.png");
    public static final ResourceLocation TEAPOT_GUI_WIDGET = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/teapot_gui_widget.png");
    public static final String TRANSLATABLE = "block.plerocottage.teapot.tooltip";

    public static final int BUTTON_X_OFFSET = 105;
    public static final int BUTTON_Y_OFFSET = 0;
    public static final int BUTTON_WIDTH = 48;
    public static final int BUTTON_HEIGHT = 48;

    public static final int OUTPUT_X_OFFSET = 0;
    public static final int OUTPUT_Y_OFFSET = 111;
    public static final int OUTPUT_WIDTH = 44;
    public static final int OUTPUT_HEIGHT = 44;

    public static final int MUG_X_OFFSET = 45;
    public static final int MUG_Y_OFFSET = 111;
    public static final int MUG_WIDTH = 78;
    public static final int MUG_HEIGHT = 78;

    public static final int GLASS_X_OFFSET = 123;
    public static final int GLASS_Y_OFFSET = 111;
    public static final int GLASS_WIDTH = 78;
    public static final int GLASS_HEIGHT = 78;

    public static final int OUTPUT_DRINK_X_OFFSET = 0;
    public static final int OUTPUT_DRINK_Y_OFFSET = 0;
    public static final int OUTPUT_DRINK_WIDTH = 54;
    public static final int OUTPUT_DRINK_HEIGHT = 54;

    public static final int TEAPOT_X_OFFSET = 0;
    public static final int TEAPOT_Y_OFFSET = 0;
    public static final int TEAPOT_WIDTH = 104;
    public static final int TEAPOT_HEIGHT = 110;

    public static final int COVER_X_OFFSET = 201;
    public static final int COVER_Y_OFFSET = 0;
    public static final int COVER_WIDTH = 46;
    public static final int COVER_HEIGHT = 46;

    public static final int WATER_X_OFFSET = 154;
    public static final int WATER_Y_OFFSET = 0;
    public static final int WATER_WIDTH = 46;
    public static final int WATER_HEIGHT = 46;

    public static final int MILK_X_OFFSET = 156;
    public static final int MILK_Y_OFFSET = 47;
    public static final int MILK_WIDTH = 42;
    public static final int MILK_HEIGHT = 42;

    public static final int SLOT_X_OFFSET = 13;
    public static final int SLOT_Y_OFFSET = 168;
    public static final int SLOT_WIDTH = 18;
    public static final int SLOT_HEIGHT = 18;

    public TeapotBlockEntity blockEntity;
    public Player player;
    private final TeapotRecipeBookComponent recipeBookComponent = new TeapotRecipeBookComponent(
            new ResourceLocation(PleroCottageMachine.MOD_ID, "textures/gui/recipe_book_buttons.png"),
            3 * 26
    );
    private ImageButton recipeBookButton;
    private ImageButton brewingButton;
    private boolean widthTooNarrow;

    public TeapotScreen(TeapotMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, TEAPOT_GUI);
        this.blockEntity = pMenu.blockEntity;
        this.player = pPlayerInventory.player;
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.recipeBookButton = new ImageButton(
                this.leftPos + 5, this.topPos + 84, 20, 18, 0, 0, 19,
                new ResourceLocation("textures/gui/recipe_button.png"),
                button -> {
                    this.recipeBookComponent.toggleVisibility();
                    this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
                    this.updateWidgetPositions();
                }
        );
        this.addRenderableWidget(this.recipeBookButton);
        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
        this.brewingButton = new ImageButton(
                        this.leftPos + 122,
                        this.topPos + 84,
                        BUTTON_WIDTH,
                        BUTTON_HEIGHT,
                        BUTTON_X_OFFSET,
                        BUTTON_Y_OFFSET,
                        TEAPOT_GUI_WIDGET,
                        pButton -> {
                            Level level = this.blockEntity.getLevel();
                            BlockPos blockPos = this.blockEntity.getBlockPos().below();
                            if (PleroCottageCompat.isOnTeapotHeatBlock(level.getBlockState(blockPos))) {
                                PleroCottageMachinePacket.sendToServer(new BrewingStartPacket(this.blockEntity.getBlockPos()));
                            } else {
                                this.player.displayClientMessage(Component.translatable(TRANSLATABLE), true);
                            }
                        }
                ) {
                    @Override
                    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
                        if (menu.getCookingTime() != 0) return;
                        RenderSystem.enableDepthTest();
                        pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset + (this.isHovered() ? pTextureDifference : 0), pWidth, pHeight, pTextureWidth, pTextureHeight);
                    }
                };
        this.addRenderableWidget(this.brewingButton);
        this.updateWidgetPositions();
        this.updateRecipeBookVisibility();
    }

    private void updateWidgetPositions() {
        if (this.recipeBookButton != null) {
            this.recipeBookButton.setPosition(this.leftPos + 5, this.topPos + 84);
        }
        if (this.brewingButton != null) {
            this.brewingButton.setPosition(this.leftPos + 122, this.topPos + 84);
        }
    }

    private void updateRecipeBookVisibility() {
        boolean brewing = this.menu.getCookingTime() != 0 || this.blockEntity.isRunning;
        if (this.recipeBookButton != null) {
            this.recipeBookButton.visible = !brewing;
            this.recipeBookButton.active = !brewing;
        }
        if (brewing && this.recipeBookComponent.isVisible()) {
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
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        this.renderOutput(pGuiGraphics);
        this.renderTeapot(pGuiGraphics);
        this.renderDrink(pGuiGraphics);
    }

    @Override
    protected void slotClicked(Slot pSlot, int pSlotId, int pMouseButton, ClickType pType) {
        super.slotClicked(pSlot, pSlotId, pMouseButton, pType);
        this.recipeBookComponent.slotClicked(pSlot);
        if (pSlot == null || pType == ClickType.PICKUP_ALL) return;
        if (pSlot.container != this.blockEntity || pSlot.getContainerSlot() != TeapotBlockEntity.CONTAINER) return;
        Level level = this.blockEntity.getLevel();
        BlockPos blockPos = this.blockEntity.getBlockPos();
        level.playLocalSound(blockPos, SoundRegistry.TEAPOT_CUP_PLACE.get(), SoundSource.BLOCKS, 0.15F, 1F, true);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }

    public void renderOutput(GuiGraphics guiGraphics) {
        ItemStack output = this.blockEntity.getItem(TeapotBlockEntity.RESULT);
        ItemStack container = this.blockEntity.getItem(TeapotBlockEntity.CONTAINER);
        guiGraphics.blit(TEAPOT_GUI_WIDGET, this.leftPos + 19, this.topPos + 76, OUTPUT_X_OFFSET, OUTPUT_Y_OFFSET, OUTPUT_WIDTH, OUTPUT_HEIGHT);
        this.renderOutputDrink(guiGraphics, output.isEmpty() ? container : output);
        if (!output.isEmpty()) {
            guiGraphics.blit(TEAPOT_GUI_WIDGET, this.leftPos + 32, this.topPos + 70,
                    SLOT_X_OFFSET, SLOT_Y_OFFSET, SLOT_WIDTH, SLOT_HEIGHT);
        }
    }

    public void renderOutputDrink(GuiGraphics guiGraphics, ItemStack output) {
        if (PleroCottageCompat.isTeapotGuiGlassBottle(output)) {
            guiGraphics.blit(TEAPOT_GUI_WIDGET, this.leftPos + 2, this.topPos + 59, GLASS_X_OFFSET, GLASS_Y_OFFSET, GLASS_WIDTH, GLASS_HEIGHT);
        } else if (PleroCottageCompat.isTeapotGuiMug(output)) {
            guiGraphics.blit(TEAPOT_GUI_WIDGET, this.leftPos + 2, this.topPos + 59, MUG_X_OFFSET, MUG_Y_OFFSET, MUG_WIDTH, MUG_HEIGHT);
        }
        if (PleroCottageCompat.isTeapotGuiGlassBottleMilkTea(output)) {
            this.renderOutputDrinkTexture(guiGraphics, PleroCottageCompat.getTeapotGuiGlassBottleMilkTeaTexture());
        } else if (PleroCottageCompat.isTeapotGuiMugApricotKernel(output)) {
            this.renderOutputDrinkTexture(guiGraphics, PleroCottageCompat.getTeapotGuiMugApricotKernelTexture());
        } else if (PleroCottageCompat.isTeapotGuiMugBlackTea(output)) {
            this.renderOutputDrinkTexture(guiGraphics, PleroCottageCompat.getTeapotGuiMugBlackTeaTexture());
        } else if (PleroCottageCompat.isTeapotGuiMugCocoa(output)) {
            this.renderOutputDrinkTexture(guiGraphics, PleroCottageCompat.getTeapotGuiMugCocoaTexture());
        } else if (PleroCottageCompat.isTeapotGuiMugCoffee(output)) {
            this.renderOutputDrinkTexture(guiGraphics, PleroCottageCompat.getTeapotGuiMugCoffeeTexture());
        } else if (PleroCottageCompat.isTeapotGuiMugGreenTea(output)) {
            this.renderOutputDrinkTexture(guiGraphics, PleroCottageCompat.getTeapotGuiMugGreenTeaTexture());
        } else if (PleroCottageCompat.isTeapotGuiMugChamomile(output)) {
            this.renderOutputDrinkTexture(guiGraphics, PleroCottageCompat.getTeapotGuiMugChamomileTexture());
        } else if (PleroCottageCompat.isTeapotGuiMugMate(output)) {
            this.renderOutputDrinkTexture(guiGraphics, PleroCottageCompat.getTeapotGuiMugMateTexture());
        } else if (PleroCottageCompat.isTeapotGuiMugMint(output)) {
            this.renderOutputDrinkTexture(guiGraphics, PleroCottageCompat.getTeapotGuiMugMintTexture());
        } else if (PleroCottageCompat.isTeapotGuiMugMatcha(output)) {
            this.renderOutputDrinkTexture(guiGraphics, PleroCottageCompat.getTeapotGuiMugMatchaTexture());
        }
        if (!output.isEmpty()) {
            guiGraphics.blit(TEAPOT_GUI_WIDGET, this.leftPos + 32, this.topPos + 89, SLOT_X_OFFSET, SLOT_Y_OFFSET, SLOT_WIDTH, SLOT_HEIGHT);
        }
    }

    public void renderOutputDrinkTexture(GuiGraphics guiGraphics, ResourceLocation texture) {
        guiGraphics.blit(texture, this.leftPos + 14, this.topPos + 71, OUTPUT_DRINK_X_OFFSET, OUTPUT_DRINK_Y_OFFSET, OUTPUT_DRINK_WIDTH, OUTPUT_DRINK_HEIGHT, OUTPUT_DRINK_WIDTH, OUTPUT_DRINK_HEIGHT);
    }

    public void renderTeapot(GuiGraphics guiGraphics) {
        guiGraphics.blit(TEAPOT_GUI_WIDGET, this.leftPos + 72, this.topPos, TEAPOT_X_OFFSET, TEAPOT_Y_OFFSET, TEAPOT_WIDTH, TEAPOT_HEIGHT);
    }

    public void renderDrink(GuiGraphics guiGraphics) {
        ItemStack drink = this.blockEntity.getItem(TeapotBlockEntity.DRINK);
        if (this.menu.getCookingTime() != 0) {
            guiGraphics.blit(TEAPOT_GUI_WIDGET, this.leftPos + 106, this.topPos + 26, COVER_X_OFFSET, COVER_Y_OFFSET, COVER_WIDTH, COVER_HEIGHT);
        }
        if (drink.isEmpty()) return;
        if (PleroCottageCompat.isBottledWater(drink)) {
            guiGraphics.blit(TEAPOT_GUI_WIDGET, this.leftPos + 106, this.topPos + 26, WATER_X_OFFSET, WATER_Y_OFFSET, WATER_WIDTH, WATER_HEIGHT);
        } else if (PleroCottageCompat.isBoxedMilk(drink)) {
            guiGraphics.blit(TEAPOT_GUI_WIDGET, this.leftPos + 108, this.topPos + 28, MILK_X_OFFSET, MILK_Y_OFFSET, MILK_WIDTH, MILK_HEIGHT);
        }
        guiGraphics.blit(TEAPOT_GUI_WIDGET, this.leftPos + 120, this.topPos + 40, SLOT_X_OFFSET, SLOT_Y_OFFSET, SLOT_WIDTH, SLOT_HEIGHT);
    }
}
