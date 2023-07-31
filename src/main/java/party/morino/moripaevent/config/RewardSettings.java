package party.morino.moripaevent.config;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Set;

@ConfigSerializable
@DefaultQualifier(NonNull.class)
public final class RewardSettings {

    private final double payAmount = 0;

    private final Set<ItemStack> items = Set.of(new ItemStack(Material.PLAYER_HEAD));

    public double payAmount() {
        return this.payAmount;
    }

    public Set<ItemStack> items() {
        return this.items;
    }
}
