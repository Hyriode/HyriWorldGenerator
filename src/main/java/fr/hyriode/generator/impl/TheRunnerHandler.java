package fr.hyriode.generator.impl;

import fr.hyriode.generator.WorldGenerator;
import fr.hyriode.generator.WorldTypeHandler;
import fr.hyriode.hyrame.world.generator.HyriWorldGenerator;
import fr.hyriode.hyrame.world.generator.HyriWorldSettings;
import org.bukkit.World;

import java.util.concurrent.CompletableFuture;

/**
 * Created by AstFaster
 * on 20/12/2022 at 15:31
 */
public class TheRunnerHandler implements WorldTypeHandler {

    @Override
    public CompletableFuture<World> generateWorld(String name) {
        final CompletableFuture<World> future = new CompletableFuture<>();
        final HyriWorldGenerator worldGenerator = new HyriWorldGenerator(WorldGenerator.get(), new HyriWorldSettings(name), 1000, future::complete);

        HyriWorldGenerator.COMMON_PATCHED_BIOMES.forEach(worldGenerator::patchBiomes);

        worldGenerator.start();

        return future;
    }

}
