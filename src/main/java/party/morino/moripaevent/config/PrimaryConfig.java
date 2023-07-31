package party.morino.moripaevent.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@DefaultQualifier(NonNull.class)
public final class PrimaryConfig {

    private final boolean enable = true;

    private final Location location = new Location(Bukkit.getWorld("world"), 0, 0, 0);

    private final RewardSettings rewardSettings = new RewardSettings();

    public boolean isEnable() {
        return this.enable;
    }

    public Location location() {
        return this.location;
    }

    public RewardSettings rewardSettings() {
        return this.rewardSettings;
    }
}
