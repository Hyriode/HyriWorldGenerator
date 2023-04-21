package fr.hyriode.generator;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.world.generation.WorldGenerationData;
import fr.hyriode.api.world.generation.WorldGenerationType;
import fr.hyriode.generator.WorldGenerator;
import fr.hyriode.hyrame.world.generator.HyriWorldGenerator;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AstFaster
 * on 20/12/2022 at 15:27
 */
public interface WorldTypeHandler {

    default CompletableFuture<Void> handle(WorldGenerationData data) {
        final CompletableFuture<Void> result = new CompletableFuture<>();
        final AtomicInteger generatedWorlds = new AtomicInteger(0);

        this.process(data, generatedWorlds, result);

        return result;
    }

    default void process(WorldGenerationData data, AtomicInteger generatedWorlds, CompletableFuture<Void> result) {
        final String worldName = WorldGenerator.randomName();

        this.generateWorld(worldName).whenComplete((world, throwable) -> Bukkit.getScheduler().runTaskLater(WorldGenerator.get(), () -> {
            HyriAPI.get().getWorldGenerationAPI().addWorld(world.getUID(), data.getType(), worldName); // Upload the world

            System.out.println(generatedWorlds.incrementAndGet() + "/" + data.getWorlds() + " worlds generated.");

            if (generatedWorlds.get() < data.getWorlds()) { // Are there more worlds to generate ?
                this.process(data, generatedWorlds, result);
            } else {
                result.complete(null);
            }
        }, 5 * 20L));
    }

    CompletableFuture<World> generateWorld(String name);

}