package party.morino.moripaevent.reward;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;
import java.util.UUID;

@DefaultQualifier(NonNull.class)
public final class User {
    private final UUID uuid;
    private boolean received;

    public User(UUID uuid, boolean received) {
        this.uuid = uuid;
        this.received = received;
    }

    public UUID uuid() {
        return uuid;
    }

    public boolean received() {
        return received;
    }

    public void received(boolean received) {
        this.received = received;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (User) obj;
        return Objects.equals(this.uuid, that.uuid) &&
                this.received == that.received;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, received);
    }

    @Override
    public String toString() {
        return "User[" +
                "uuid=" + uuid + ", " +
                "received=" + received + ']';
    }
}
