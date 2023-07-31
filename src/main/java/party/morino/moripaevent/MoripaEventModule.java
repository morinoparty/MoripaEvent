package party.morino.moripaevent;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.moripaevent.gson.UUIDSerializer;

import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Function;

@DefaultQualifier(NonNull.class)
public class MoripaEventModule extends AbstractModule {

    private final MoripaEvent moripaEvent;

    public MoripaEventModule(
            final MoripaEvent moripaEvent
    ) {
        this.moripaEvent = moripaEvent;
    }

    @Provides
    @Singleton
    public CommandManager<CommandSender> commandManager() {
        final PaperCommandManager<CommandSender> commandManager;
        try {
            commandManager = new PaperCommandManager<>(
                    moripaEvent,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );
        } catch (final Exception exception) {
            throw new RuntimeException("Failed to initialize command manager.", exception);
        }
        commandManager.registerAsynchronousCompletions();
        return commandManager;
    }

    @Provides
    @Singleton
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(UUID.class, new UUIDSerializer())
                .setPrettyPrinting().create();
    }

    @Provides
    @Singleton
    public @Nullable Economy economy() {
        if (MoripaEvent.vaultLoaded()) {
            final @Nullable RegisteredServiceProvider<Economy> rsp = this.moripaEvent.getServer().getServicesManager().getRegistration(Economy.class);
            return rsp == null
                    ? null
                    : rsp.getProvider();
        } else {
            return null;
        }
    }

    @Override
    protected void configure() {
        this.bind(MoripaEvent.class).toInstance(this.moripaEvent);
        this.bind(Server.class).toInstance(this.moripaEvent.getServer());
        this.bind(Path.class).toInstance(this.moripaEvent.dataDirectory());
        this.bind(ComponentLogger.class).toInstance(this.moripaEvent.logger());
    }
}
