package party.morino.moripaevent.config;

import com.google.inject.Inject;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@DefaultQualifier(NonNull.class)
public class ConfigFactory {

    private final Path dataDirectory;

    private @Nullable PrimaryConfig primaryConfig = null;

    @Inject
    public ConfigFactory(
            final Path dataDirectory
    ) {
        this.dataDirectory = dataDirectory;
    }

    public @Nullable PrimaryConfig reloadPrimaryConfig() {
        try {
            this.primaryConfig = this.load(PrimaryConfig.class, "config.conf");
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        return this.primaryConfig;
    }

    public @Nullable PrimaryConfig primaryConfig() {
        if (this.primaryConfig == null) {
            return this.reloadPrimaryConfig();
        }
        return this.primaryConfig;
    }

    public ConfigurationLoader<?> configurationLoader(final Path file) {
        return HoconConfigurationLoader.builder()
                .prettyPrinting(true)
                .defaultOptions(opts -> {
                    final var miniMessageSerializer =
                            ConfigurateComponentSerializer.builder()
                                    .scalarSerializer(MiniMessage.miniMessage())
                                    .outputStringComponents(true)
                                    .build();
                    final var kyoriSerializer =
                            ConfigurateComponentSerializer.configurate();

                    return opts.shouldCopyDefaults(true).serializers(serializerBuilder ->
                            serializerBuilder
                                    .registerAll(miniMessageSerializer.serializers())
                                    .registerAll(kyoriSerializer.serializers())
                    );
                })
                .path(file)
                .build();
    }

    public <T> @Nullable T load(final Class<T> clazz, final String fileName) throws IOException {
        if (!Files.exists(this.dataDirectory)) {
            Files.createDirectories(this.dataDirectory);
        }

        final var file = this.dataDirectory.resolve(fileName);

        final var loader = this.configurationLoader(file);

        try {
            final var node = loader.load();
            final @Nullable T config = node.get(clazz);

            if (!Files.exists(file)) {
                node.set(clazz, config);
                loader.save(node);
            }

            return config;
        } catch (final ConfigurateException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
