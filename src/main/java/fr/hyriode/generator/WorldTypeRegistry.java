package fr.hyriode.generator;

import fr.hyriode.api.world.generation.WorldGenerationType;
import fr.hyriode.generator.impl.TheRunnerHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AstFaster
 * on 20/12/2022 at 15:26
 */
public class WorldTypeRegistry {

    private final Map<WorldGenerationType, WorldTypeHandler> handlers = new HashMap<>();

    public WorldTypeRegistry() {
        this.registerHandler(WorldGenerationType.THE_RUNNER, new TheRunnerHandler());
    }

    public void registerHandler(WorldGenerationType type, WorldTypeHandler handler) {
        this.handlers.put(type, handler);

        System.out.println("Registered '" + type.name() + "' generation type handler.");
    }

    public WorldTypeHandler getHandler(WorldGenerationType type) {
        return this.handlers.get(type);
    }

}
