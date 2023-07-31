package party.morino.moripaevent.command.commands;

import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.moripaevent.command.MoripaEventCommand;
import party.morino.moripaevent.config.ConfigFactory;
import party.morino.moripaevent.reward.RewardManager;
import party.morino.moripaevent.reward.User;

import java.util.StringJoiner;

@DefaultQualifier(NonNull.class)
public class ListCommand extends MoripaEventCommand {

    private final ConfigFactory configFactory;
    private final CommandManager<CommandSender> commandManager;
    private final RewardManager rewardManager;
    private final Server server;

    @Inject
    public ListCommand(
            final ConfigFactory configFactory,
            final CommandManager<CommandSender> commandManager,
            final RewardManager rewardManager,
            final Server server
    ) {
        this.commandManager = commandManager;
        this.configFactory = configFactory;
        this.rewardManager = rewardManager;
        this.server = server;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("moripaevent", "me")
                .literal("list")
                .permission("moripaevent.list")
                .senderType(CommandSender.class)
                .handler(context -> {
                    var joiner = new StringJoiner(", ");
                    this.rewardManager.users().stream()
                            .map(User::uuid)
                            .map(this.server::getOfflinePlayer)
                            .map(OfflinePlayer::getName)
                            .forEach(joiner::add);
                    context.getSender().sendMessage(MiniMessage.miniMessage().deserialize("<st>                        </st>報酬受け取った人<st>                        </st>"));
                    context.getSender().sendMessage(Component.text(joiner.toString()));
                    context.getSender().sendMessage(MiniMessage.miniMessage().deserialize("<st>                                                                    </st>"));
                })
                .build();

        this.commandManager.command(command);
    }
}
