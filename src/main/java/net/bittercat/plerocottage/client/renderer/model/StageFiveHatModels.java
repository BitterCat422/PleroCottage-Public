package net.bittercat.plerocottage.client.renderer.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public final class StageFiveHatModels {
    public static final ModelLayerLocation CHEF_HAT = layer("chef_hat");
    public static final ModelLayerLocation CHRISTMAS_HAT = layer("christmas_hat");
    public static final ModelLayerLocation MINER_HAT = layer("miner_hat");
    public static final ModelLayerLocation PASTRY_CHEF_HAT = layer("pastry_chef_hat");

    private StageFiveHatModels() {
    }

    public static LayerDefinition createChefHat() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(0, 17).addBox(-4.5F, -9.5F, -4.5F, 9, 3, 9, new CubeDeformation(0.05F))
            .texOffs(0, 0).addBox(-5.5F, -15.5F, -5.5F, 11, 6, 11, new CubeDeformation(0.05F)),
            PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition createChristmasHat() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition head = mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-5, -9, -5, 10, 2, 10)
            .texOffs(0, 12).addBox(-4, -11, -4, 8, 2, 8), PartPose.ZERO);
        head.addOrReplaceChild("pom_a", CubeListBuilder.create().texOffs(24, 12)
                .addBox(-2.5F, -5, -1, 3, 3, 3),
            PartPose.offsetAndRotation(1, -13.5F, 2.25F, -0.7854F, 0, 0));
        head.addOrReplaceChild("pom_b", CubeListBuilder.create().texOffs(22, 22)
                .addBox(-2.5F, -5, -1, 4, 4, 3),
            PartPose.offsetAndRotation(0.5F, -13.5F, 0.25F, -0.7854F, 0, 0));
        head.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(0, 22)
                .addBox(-4, -5, -1, 6, 5, 5),
            PartPose.offsetAndRotation(1, -10.5F, -1.5F, -0.3927F, 0, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition createMinerHat() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-5.5F, -7.5F, -7.5F, 11, 1, 13)
            .texOffs(0, 14).addBox(-4.5F, -11.5F, -4.5F, 9, 4, 9)
            .texOffs(25, 16).addBox(-1.5F, -12.5F, -5.5F, 3, 5, 11)
            .texOffs(0, 0).addBox(-1, -9.5F, -6.5F, 2, 2, 1), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition createPastryChefHat() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition head = mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(20, 22).addBox(-1, -16.8802F, -0.5774F, 3, 3, 0)
            .texOffs(0, 0).addBox(-3.5F, -11.8802F, -4.0774F, 7, 3, 7)
            .texOffs(0, 10).addBox(-3, -8.8802F, -4.0774F, 6, 2, 6),
            PartPose.offsetAndRotation(0, 0, 0, -0.1309F, 0, 0));
        head.addOrReplaceChild("detail_a", CubeListBuilder.create().texOffs(0, 24)
                .addBox(-1, -2, -1, 2, 2, 1),
            PartPose.offsetAndRotation(2, -9.8802F, -5.5774F, -0.48F, 0, 0.7854F));
        head.addOrReplaceChild("detail_b", CubeListBuilder.create().texOffs(0, 18)
                .addBox(-6, -3, -1, 7, 3, 3, new CubeDeformation(0.1F)),
            PartPose.offsetAndRotation(2.5F, -8.8802F, -5.4774F, -0.7854F, 0, 0));
        head.addOrReplaceChild("detail_c", CubeListBuilder.create().texOffs(20, 18)
                .addBox(-1, -2, -1, 2, 2, 2),
            PartPose.offsetAndRotation(0, -11.8802F, -0.5774F, 0, -0.7854F, 0));
        return LayerDefinition.create(mesh, 32, 32);
    }

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(new ResourceLocation("plerocottage", name), "main");
    }
}
