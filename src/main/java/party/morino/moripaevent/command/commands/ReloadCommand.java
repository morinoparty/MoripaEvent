package party.morino.moripaevent.command.commands;

import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.moripaevent.command.MoripaEventCommand;
import party.morino.moripaevent.config.ConfigFactory;

@DefaultQualifier(NonNull.class)
public class ReloadCommand extends MoripaEventCommand {

    final ConfigFactory configFactory;
    final CommandManager<CommandSender> commandManager;

    @Inject
    public ReloadCommand(
            final ConfigFactory configFactory,
            final CommandManager<CommandSender> commandManager
    ) {
        this.commandManager = commandManager;
        this.configFactory = configFactory;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("moripaevent", "me")
                .literal("reload")
                .permission("moripaevent.reload")
                .senderType(CommandSender.class)
                .handler(handler -> {
                    this.configFactory.reloadPrimaryConfig();
                    // TODO: message
                    handler.getSender().sendMessage(Component.text("Config reloaded"));
                })
                .build();

        this.commandManager.command(command);
    }
}
