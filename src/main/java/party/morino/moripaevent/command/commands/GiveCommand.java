package party.morino.moripaevent.command.commands;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.moripaevent.command.MoripaEventCommand;
import party.morino.moripaevent.config.ConfigFactory;
import party.morino.moripaevent.reward.RewardManager;

@DefaultQualifier(NonNull.class)
public class GiveCommand extends MoripaEventCommand {

    private final ConfigFactory configFactory;
    private final CommandManager<CommandSender> commandManager;
    private final RewardManager rewardManager;

    @Inject
    public GiveCommand(
            final ConfigFactory configFactory,
            final CommandManager<CommandSender> commandManager,
            final RewardManager rewardManager
    ) {
        this.commandManager = commandManager;
        this.configFactory = configFactory;
        this.rewardManager = rewardManager;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("moripaevent", "me")
                .literal("give")
                .argument(PlayerArgument.of("player"))
                .permission("moripaevent.give")
                .senderType(CommandSender.class)
                .handler(context -> {
                    Player player = context.get("player");
                    this.rewardManager.give(player);
                })
                .build();

        this.commandManager.command(command);
    }
}
