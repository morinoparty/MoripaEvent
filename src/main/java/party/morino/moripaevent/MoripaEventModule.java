package party.morino.moripaevent;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.file.Path;
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

    @Override
    protected void configure() {
        this.bind(MoripaEvent.class).toInstance(this.moripaEvent);
        this.bind(Path.class).toInstance(this.moripaEvent.dataDirectory());
    }
}
