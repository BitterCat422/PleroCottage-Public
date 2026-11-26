package net.bittercat.plerocottage.client.renderer;

import com.mojang.datafixers.util.Pair;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.entities.ModBoat;
import net.bittercat.plerocottage.entities.ModChestBoat;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

public class ModBoatRenderer extends BoatRenderer {

    private final boolean chest;

    public ModBoatRenderer(EntityRendererProvider.Context context, boolean chest) {
        super(context, chest);
        this.chest = chest;
    }

    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        Pair<ResourceLocation, ListModel<Boat>> original = super.getModelWithLocation(boat);
        String wood = woodOf(boat);
        if (wood == null) {
            return original;
        }
        boolean raft = boat.getVariant() == Boat.Type.BAMBOO;
        ResourceLocation texture = new ResourceLocation(PleroCottage.MODID,
            "textures/entity/" + (raft ? (chest ? "chest_raft/" : "raft/")
                : (chest ? "chest_boat/" : "boat/")) + wood + ".png");
        return Pair.of(texture, original.getSecond());
    }

    private static String woodOf(Boat boat) {
        if (boat instanceof ModBoat modBoat) {
            return modBoat.getWoodName();
        }
        if (boat instanceof ModChestBoat modChestBoat) {
            return modChestBoat.getWoodName();
        }
        return null;
    }
}
