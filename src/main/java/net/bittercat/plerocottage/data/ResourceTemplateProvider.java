package net.bittercat.plerocottage.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

public final class ResourceTemplateProvider implements DataProvider {
    private final PackOutput output;
    private final Path templateRoot;

    public ResourceTemplateProvider(PackOutput output) {
        this.output = output;
        String resourcesRoot = System.getProperty("plerocottage.resources");
        this.templateRoot = resourcesRoot == null
            ? Path.of("src/main/datagen/resources").toAbsolutePath()
            : Path.of(resourcesRoot).toAbsolutePath().getParent().resolve("datagen/resources");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        if (!Files.isDirectory(templateRoot)) {
            return CompletableFuture.completedFuture(null);
        }

        List<CompletableFuture<?>> writes = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(templateRoot)) {
            paths.filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(path -> {
                    try {
                        JsonElement json = JsonParser.parseString(Files.readString(path));
                        Path relative = templateRoot.relativize(path);
                        writes.add(DataProvider.saveStable(cachedOutput, json,
                            output.getOutputFolder().resolve(relative)));
                    } catch (IOException exception) {
                        throw new IllegalStateException("无法读取数据生成模板：" + path, exception);
                    }
                });
        } catch (IOException exception) {
            throw new IllegalStateException("无法遍历数据生成模板目录：" + templateRoot, exception);
        }
        return CompletableFuture.allOf(writes.toArray(CompletableFuture<?>[]::new));
    }

    @Override
    public String getName() {
        return "Authored resource JSON templates";
    }
}
