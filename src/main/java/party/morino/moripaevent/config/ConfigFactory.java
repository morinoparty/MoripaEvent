package party.morino.moripaevent.config;

import com.google.inject.Inject;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import party.morino.moripaevent.config.serialisation.EnchantmentSerializerConfigurate;
import party.morino.moripaevent.config.serialisation.ItemStackSerializerConfigurate;
import party.morino.moripaevent.config.serialisation.LocaleSerializerConfigurate;
import party.morino.moripaevent.config.serialisation.LocationSerializerConfigurate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

@DefaultQualifier(NonNull.class)
public final class ConfigFactory {

    private final Path dataDirectory;
    private final EnchantmentSerializerConfigurate enchantmentSerializer;
    private final ItemStackSerializerConfigurate itemStackSerializer;
    private final LocaleSerializerConfigurate localeSerializer;
    private final LocationSerializerConfigurate locationSerializer;

    private @Nullable PrimaryConfig primaryConfig = null;

    @Inject
    public ConfigFactory(
            final Path dataDirectory,
            final EnchantmentSerializerConfigurate enchantmentSerializer,
            final ItemStackSerializerConfigurate itemStackSerializer,
            final LocaleSerializerConfigurate localeSerializer,
            final LocationSerializerConfigurate locationSerializer
    ) {
        this.dataDirectory = dataDirectory;
        this.enchantmentSerializer = enchantmentSerializer;
        this.itemStackSerializer = itemStackSerializer;
        this.localeSerializer = localeSerializer;
        this.locationSerializer = locationSerializer;
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
                                    .register(EnchantmentSerializerConfigurate.Enchant.class, this.enchantmentSerializer)
                                    .register(ItemStack.class, this.itemStackSerializer)
                                    .register(Locale.class, this.localeSerializer)
                                    .register(Location.class, this.locationSerializer)
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
