package party.morino.moripaevent.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@DefaultQualifier(NonNull.class)
public class PrimaryConfig {

    private boolean enable = true;

    public boolean isEnable() {
        return this.enable;
    }
}
