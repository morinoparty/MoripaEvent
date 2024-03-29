package party.morino.moripaevent.config.serialisation;

import com.google.inject.Inject;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

@DefaultQualifier(NonNull.class)
public class EnchantmentSerializerConfigurate implements TypeSerializer<EnchantmentSerializerConfigurate.Enchant> {

    private static final String ENCHANTMENT = "enchantment";
    private static final String LEVEL = "level";
    private final ComponentLogger logger;

    @Inject
    public EnchantmentSerializerConfigurate(final ComponentLogger logger) {
        this.logger = logger;
    }

    @Override
    public Enchant deserialize(final Type type, final ConfigurationNode node) {
        var enchantmentNode = node.node(ENCHANTMENT);
        var enchantmentKey = NamespacedKey.minecraft(requireNonNull(enchantmentNode.getString()));

        var enchantment = requireNonNull(Enchantment.getByKey(enchantmentKey));
        int amount = node.node(LEVEL).getInt(1);

        return new Enchant(enchantment, amount);
    }

    @Override
    public void serialize(final Type type, final @Nullable Enchant obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
        } else {
            node.node(ENCHANTMENT).set(obj.enchantment());
            node.node(LEVEL).set(obj.level());
        }
    }

    @DefaultQualifier(NonNull.class)
    public record Enchant(Enchantment enchantment, int level) {

    }

}
