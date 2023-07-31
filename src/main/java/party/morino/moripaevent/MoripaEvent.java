package party.morino.moripaevent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.moripaevent.config.ConfigFactory;
import party.morino.moripaevent.util.CloudUtils;
import party.morino.moripaevent.util.ListenerUtils;

import java.nio.file.Path;
import java.util.Objects;

@DefaultQualifier(NonNull.class)
public class MoripaEvent extends JavaPlugin {

    private final ComponentLogger logger;
    private final Path dataDirectory;
    private final Injector injector;

    public MoripaEvent(
            final ComponentLogger logger,
            final Path dataDirectory
    ) {
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        this.injector = Guice.createInjector(new MoripaEventModule(this));
    }

    public static boolean vaultLoaded() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    @Override
    public void onLoad() {
        var config = this.injector.getInstance(ConfigFactory.class);
        if (!Objects.requireNonNull(config.primaryConfig()).isEnable()) {
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {

        // commands
        CloudUtils.loadCommands(this.injector);

        // listeners
        ListenerUtils.loadListeners(this.injector, this);
    }

    public ComponentLogger logger() {
        return this.logger;
    }

    public Path dataDirectory() {
        return this.dataDirectory;
    }
}
