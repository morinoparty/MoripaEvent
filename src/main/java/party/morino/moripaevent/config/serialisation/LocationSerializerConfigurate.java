package party.morino.moripaevent.config.serialisation;

import com.google.inject.Inject;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Objects;

@DefaultQualifier(NonNull.class)
public class LocationSerializerConfigurate implements TypeSerializer<Location> {

    private static final String DELIMITER = ":";
    private final ComponentLogger logger;

    @Inject
    public LocationSerializerConfigurate(final ComponentLogger logger) {
        this.logger = logger;
    }

    @Override
    public Location deserialize(final Type type, final ConfigurationNode node) {

        var value = Objects.requireNonNull(node.getString());
        var tokens = value.split(DELIMITER);

        return new Location(Bukkit.getWorld(tokens[0]), Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
    }

    @Override
    public void serialize(final Type type, final @Nullable Location obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
        } else {
            var world = obj.getWorld() != null
                    ? obj.getWorld().getName()
                    : "null";

            node.set(world + DELIMITER + obj.getBlockX() + DELIMITER + obj.getBlockY() + DELIMITER + obj.getBlockZ());
        }
    }
}
