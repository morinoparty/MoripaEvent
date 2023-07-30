package party.morino.moripaevent.util;

import com.google.inject.Injector;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Set;

import static org.bukkit.Bukkit.getServer;

@DefaultQualifier(NonNull.class)
public final class ListenerUtils {

    private ListenerUtils() {
        throw new AssertionError();
    }

    private static final Set<Class<? extends Listener>> LISTENER_CLASSES = Set.of(

    );

    public static void loadListeners(
            final Injector injector,
            final Plugin plugin
    ) {
        for (final var listenerClass : LISTENER_CLASSES) {
            var listenerInstance = injector.getInstance(listenerClass);
            getServer().getPluginManager().registerEvents(listenerInstance, plugin);
        }
    }
}
