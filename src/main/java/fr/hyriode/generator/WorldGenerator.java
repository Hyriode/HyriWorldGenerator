package fr.hyriode.generator;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.world.generation.WorldGenerationData;
import fr.hyriode.api.world.generation.WorldGenerationType;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.plugin.IPluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 20/12/2022 at 09:54
 */
public class WorldGenerator extends JavaPlugin implements IPluginProvider {

    private static final String PACKAGE = "fr.hyriode.generator";

    private static WorldGenerator instance;

    @Override
    public void onEnable() {
        instance = this;

        System.out.println("Starting WorldGenerator...");

        HyrameLoader.load(this);

        if (HyriAPI.get().getConfig().isDevEnvironment()) {
            final WorldTypeRegistry typeRegistry = new WorldTypeRegistry();
            final WorldTypeHandler handler = typeRegistry.getHandler(WorldGenerationType.THE_RUNNER);

            handler.handle(new WorldGenerationData(WorldGenerationType.THE_RUNNER, 1));
            return;
        }

        final WorldGenerationData data = HyriAPI.get().getWorldGenerationAPI().getData();

        if (data == null) {
            System.err.println("No generation data provided!");
            HyriAPI.get().getServerManager().removeServer(HyriAPI.get().getServer().getName(), null);
            return;
        }

        final WorldTypeRegistry typeRegistry = new WorldTypeRegistry();
        final WorldTypeHandler handler = typeRegistry.getHandler(data.getType());

        if (handler == null) {
            System.err.println("No generation handler registered for '" + data.getType() + "' type!");
            HyriAPI.get().getServerManager().removeServer(HyriAPI.get().getServer().getName(), null);
            return;
        }

        handler.handle(data).whenComplete((unused, throwable) -> {
            // All worlds have been generated, so, we stop the server
            System.out.println("Finished to generate worlds (" + data.getWorlds() + ").");
            HyriAPI.get().getServerManager().removeServer(HyriAPI.get().getServer().getName(), null);
        });
    }

    @Override
    public void onDisable() {

    }

    public static String randomName() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    public static WorldGenerator get() {
        return instance;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }

    @Override
    public String getId() {
        return "gen";
    }

    @Override
    public String[] getCommandsPackages() {
        return new String[] {PACKAGE};
    }

    @Override
    public String[] getListenersPackages() {
        return new String[] {PACKAGE};
    }

    @Override
    public String[] getItemsPackages() {
        return new String[] {PACKAGE};
    }

    @Override
    public String getLanguagesPath() {
        return "/lang/";
    }
}
