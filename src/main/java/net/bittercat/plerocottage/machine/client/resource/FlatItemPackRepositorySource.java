package net.bittercat.plerocottage.machine.client.resource;

import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Path;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class FlatItemPackRepositorySource implements RepositorySource {
    private static final String DIR_NAME = "plerocottage-flat_items";
    private static final String PACK_NAME = "flat_items";
    private final Pack pack;

    public FlatItemPackRepositorySource() {
        Pack.ResourcesSupplier supplier = name -> {
            IModFile file = ModList.get().getModFileById(PleroCottageMachine.MOD_ID).getFile();
            return new PathPackResources(file.getFileName(), true, file.getFilePath()) {
                @Override
                protected Path resolve(String... paths) {
                    String[] newPaths = new String[paths.length + 1];
                    newPaths[0] = DIR_NAME;
                    System.arraycopy(paths, 0, newPaths, 1, paths.length);
                    return file.findResource(newPaths);
                }
            };
        };
        this.pack = Pack.create(
            PACK_NAME,
            Component.translatable("pack.plerocottage.flat_items"),
            false,
            supplier,
            Pack.readPackInfo(DIR_NAME, supplier),
            PackType.CLIENT_RESOURCES,
            Pack.Position.TOP,
            false,
            PackSource.BUILT_IN
        );
    }

    @Override
    public void loadPacks(Consumer<Pack> onLoad) {
        onLoad.accept(this.pack);
    }
}
