package party.morino.moripaevent.util;

import com.google.inject.Injector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.moripaevent.command.MoripaEventCommand;
import party.morino.moripaevent.command.commands.ReloadCommand;

import java.util.Set;

@DefaultQualifier(NonNull.class)
public final class CloudUtils {

    private CloudUtils() {
        throw new AssertionError();
    }

    private static final Set<Class<? extends MoripaEventCommand>> COMMAND_CLASSES = Set.of(
            ReloadCommand.class
    );

    public static void loadCommands(final Injector injector) {
        for (final var commandClass : COMMAND_CLASSES) {
            var commandInstance = injector.getInstance(commandClass);
            commandInstance.init();
        }
    }
}
