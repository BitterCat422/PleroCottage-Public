package net.bittercat.plerocottage.machine.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.IceCreamMachineBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.menus.IceCreamMachineMenu;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.machine.network.server.packet.IceCreamCraftPacket;
import net.bittercat.plerocottage.init.SoundRegistry;

@OnlyIn(Dist.CLIENT)
public class IceCreamMachineScreen extends BaseMachineScreen<IceCreamMachineMenu> implements RecipeUpdateListener {
    public static final ResourceLocation ICE_CREAM_MACHINE_GUI = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/ice_cream_machine_gui.png");
    public static final ResourceLocation CONE_TEXTURE = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/ice_cream_machine_gui/ice_cream_cone.png");

    public static final int ICE_CREAM_X_OFFSET = 0;
    public static final int ICE_CREAM_Y_OFFSET = 0;
    public static final int ICE_CREAM_WIDTH = 100;
    public static final int ICE_CREAM_HEIGHT = 100;

    public IceCreamMachineBlockEntity blockEntity;
    private final IceCreamMachineRecipeBookComponent recipeBookComponent = new IceCreamMachineRecipeBookComponent(
            ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/recipe_book_buttons.png"),
            0
    );
    private ImageButton recipeBookButton;
    private ImageButton craftButton;
    private boolean widthTooNarrow;
    private boolean awaitingCraftResult;
    private int craftRequestTicks;
    private int craftButtonAnimationTicks;

    public IceCreamMachineScreen(IceCreamMachineMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, ICE_CREAM_MACHINE_GUI);
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
        this.craftButton = new ImageButton(
                        this.leftPos + 31,
                        this.topPos - 8,
                        30,
                        54,
                        178,
                        0,
                        ICE_CREAM_MACHINE_GUI,
                        pButton -> {
                            this.awaitingCraftResult = true;
                            this.craftRequestTicks = 40;
                            PleroCottageMachinePacket.sendToServer(new IceCreamCraftPacket(this.blockEntity.getBlockPos()));
                        }
                ) {
                    @Override
                    public void playDownSound(SoundManager pHandler) {
                    }

                    @Override
                    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
                        RenderSystem.enableDepthTest();
                        RenderSystem.enableBlend();
                        if (craftButtonAnimationTicks > 0) {
                            pGuiGraphics.blit(pTexture, pX, pY + 10, 178, 54, 30, 44, 256, 256);
                        } else {
                            pGuiGraphics.blit(pTexture, pX, pY, 178, 0, 30, 54, 256, 256);
                            if (this.isHovered()) {
                                pGuiGraphics.setColor(255, 255, 255, 0.225F);
                                pGuiGraphics.blit(pTexture, pX, pY, 178, 0, 30, 54, 256, 256);
                                pGuiGraphics.setColor(1, 1, 1, 1);
                            }
                        }
                    }

                    @Override
                    public boolean isActive() {
                        return super.isActive() && !awaitingCraftResult && craftButtonAnimationTicks == 0;
                    }
                };
        this.addRenderableWidget(this.craftButton);
        this.updateWidgetPositions();
    }

    private void updateWidgetPositions() {
        if (this.recipeBookButton != null) {
            this.recipeBookButton.setPosition(this.leftPos + 5, this.topPos + 62);
        }
        if (this.craftButton != null) {
            this.craftButton.setPosition(this.leftPos + 31, this.topPos - 8);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
        if (this.awaitingCraftResult) {
            if (this.menu.getSlot(this.menu.getResultSlotIndex()).hasItem()) {
                this.awaitingCraftResult = false;
                this.craftButtonAnimationTicks = 20;
            } else if (--this.craftRequestTicks <= 0) {
                this.awaitingCraftResult = false;
            }
        } else if (this.craftButtonAnimationTicks > 0) {
            this.craftButtonAnimationTicks--;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
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
        this.renderFluid(pGuiGraphics);
        this.renderFullFluidIndicator(pGuiGraphics);
        this.renderIceCreamItem(pGuiGraphics);
        this.renderOutputSlotDecoration(pGuiGraphics);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
        this.recipeBookComponent.slotClicked(slot);
        if (slot == null || slot.container != this.blockEntity || type == ClickType.PICKUP_ALL) {
            return;
        }
        switch (slot.getSlotIndex()) {
            case IceCreamMachineBlockEntity.CONTAINER_SLOT, IceCreamMachineBlockEntity.OUTPUT_SLOT ->
                    this.blockEntity.getLevel().playLocalSound(this.blockEntity.getBlockPos(),
                            SoundEvents.BAMBOO_SAPLING_PLACE, SoundSource.BLOCKS, 1F, 1.5F, false);
            case IceCreamMachineBlockEntity.LEFT_INPUT_SLOT, IceCreamMachineBlockEntity.RIGHT_INPUT_SLOT ->
                    this.blockEntity.getLevel().playLocalSound(this.blockEntity.getBlockPos(),
                            SoundRegistry.ICE_CREAM_MACHINE_CLANK.get(), SoundSource.BLOCKS, 0.3F, 1F, false);
        }
    }

    public void renderFluid(GuiGraphics pGuiGraphics) {
        FluidTank fluidTank = this.blockEntity.fluidTank;
        if (fluidTank.isEmpty()) return;
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidTank.getFluid().getFluid());
        ResourceLocation textureId = fluidTypeExtensions.getStillTexture();
        TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(textureId);
        SpriteContents contents = textureAtlasSprite.contents();
        int width = contents.width();
        int height = contents.height();
        int count = (int) contents.getUniqueFrames().count();
        int x = this.leftPos + 105;
        int y = this.topPos + 14;
        int renderWidth = 34;
        int renderHeight = 47;
        int scale = renderWidth / width;
        int color = fluidTypeExtensions.getTintColor();
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        pGuiGraphics.pose().pushPose();

        pGuiGraphics.enableScissor(x, y + renderHeight - (renderHeight * fluidTank.getFluidAmount() / fluidTank.getCapacity()), x + renderWidth, y + renderHeight);

        pGuiGraphics.blit(x, y, 0, renderWidth, renderHeight, textureAtlasSprite, r, g, b, alpha);
        pGuiGraphics.disableScissor();
        pGuiGraphics.setColor(1F, 1F, 1F, 1F);
        pGuiGraphics.pose().popPose();
    }

    private void renderFullFluidIndicator(GuiGraphics guiGraphics) {
        if (this.blockEntity.fluidTank.getFluidAmount() == this.blockEntity.fluidTank.getCapacity()) {
            guiGraphics.blit(ICE_CREAM_MACHINE_GUI, this.leftPos + 149, this.topPos + 17,
                    208, 0, 10, 21, 256, 256);
        }
    }

    private void renderOutputSlotDecoration(GuiGraphics guiGraphics) {
        if (!this.blockEntity.getItem(IceCreamMachineBlockEntity.OUTPUT_SLOT).isEmpty()) {
            guiGraphics.blit(ICE_CREAM_MACHINE_GUI, this.leftPos + 37, this.topPos + 85,
                    224, 0, 18, 18, 256, 256);
        }
    }

    public void renderConeAndIceCream(GuiGraphics guiGraphics, ItemStack output, ResourceLocation texture) {
        guiGraphics.blit(CONE_TEXTURE, this.leftPos - 4, this.topPos + 45, ICE_CREAM_X_OFFSET, ICE_CREAM_Y_OFFSET, ICE_CREAM_WIDTH, ICE_CREAM_HEIGHT, ICE_CREAM_WIDTH, ICE_CREAM_HEIGHT);
        if (!PleroCottageCompat.isIceCreamCone(output)) {
            guiGraphics.blit(texture, this.leftPos - 4, this.topPos + 19, ICE_CREAM_X_OFFSET, ICE_CREAM_Y_OFFSET, ICE_CREAM_WIDTH, ICE_CREAM_HEIGHT, ICE_CREAM_WIDTH, ICE_CREAM_HEIGHT);
        }
    }

    public void renderIceCreamItem(GuiGraphics pGuiGraphics) {
        ItemStack output = this.blockEntity.getItem(IceCreamMachineBlockEntity.OUTPUT_SLOT);
        if (output.isEmpty()) {
            output = this.blockEntity.getItem(IceCreamMachineBlockEntity.CONTAINER_SLOT);
        }
        if (output.isEmpty()) return;
        ResourceLocation outputKey = ForgeRegistries.ITEMS.getKey(output.getItem());
        String path = outputKey.withPrefix("textures/gui/ice_cream_machine_gui/").getPath() + ".png";
        ResourceLocation resource1 = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, path);
        ResourceLocation resource2 = ResourceLocation.fromNamespaceAndPath(outputKey.getNamespace(), path);
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager.getResource(resource1).isPresent()) {
            this.renderConeAndIceCream(pGuiGraphics, output, resource1);
        } else if (resourceManager.getResource(resource2).isPresent()) {
            this.renderConeAndIceCream(pGuiGraphics, output, resource2);
        }
    }

}
