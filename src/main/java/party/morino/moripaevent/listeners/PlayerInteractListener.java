package party.morino.moripaevent.listeners;

import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.moripaevent.config.ConfigFactory;
import party.morino.moripaevent.reward.RewardManager;

@DefaultQualifier(NonNull.class)
public class PlayerInteractListener implements Listener {

    private final ConfigFactory configFactory;
    private final RewardManager rewardManager;

    @Inject
    public PlayerInteractListener(
            final ConfigFactory configFactory,
            final RewardManager rewardManager
    ) {
        this.configFactory = configFactory;
        this.rewardManager = rewardManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getInteractionPoint() == null) {
            return;
        }

        if (!event.getInteractionPoint().toBlockLocation().equals(this.configFactory.primaryConfig().location())) {
            return;
        }

        var received = this.rewardManager.give(event.getPlayer());
        if (received) {
            var message = MiniMessage.miniMessage().deserialize("<blue>報酬を受け取りました！");
            event.getPlayer().sendMessage(message);
        } else {
            var message = MiniMessage.miniMessage().deserialize("<red>インベントリに空きスロットがないので報酬が受け取れませんでした");
            event.getPlayer().sendMessage(message);
        }
    }
}
