package net.bittercat.plerocottage.machine.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class VerticalFilterRecipeBookComponent extends RecipeBookComponent {
    protected abstract ResourceLocation getFilterButtonTexture();

    protected abstract int getFilterButtonTextureX();

    @Override
    public void initVisuals() {
        super.initVisuals();
        StateSwitchingButton originalButton = this.filterButton;
        VerticalFilterButton verticalButton = new VerticalFilterButton(
                originalButton.getX(),
                originalButton.getY(),
                originalButton.isStateTriggered(),
                this.getFilterButtonTexture(),
                this.getFilterButtonTextureX()
        );
        verticalButton.setTooltip(Tooltip.create(originalButton.isStateTriggered()
                ? this.getRecipeFilterName()
                : Component.translatable("gui.recipebook.toggleRecipes.all")));
        this.filterButton = verticalButton;
    }

    private static class VerticalFilterButton extends StateSwitchingButton {
        private final ResourceLocation texture;
        private final int textureX;

        private VerticalFilterButton(int x, int y, boolean enabled, ResourceLocation texture, int textureX) {
            super(x, y, 26, 16, enabled);
            this.texture = texture;
            this.textureX = textureX;
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            int textureY = this.isStateTriggered() ? 32 : 0;
            if (this.isHoveredOrFocused()) {
                textureY += 16;
            }
            graphics.blit(this.texture, this.getX(), this.getY(), this.textureX, textureY, this.width, this.height);
        }
    }
}
