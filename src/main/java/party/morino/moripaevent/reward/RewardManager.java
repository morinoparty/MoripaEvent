package party.morino.moripaevent.reward;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.moripaevent.config.ConfigFactory;
import party.morino.moripaevent.gson.JsonFileManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Singleton
@DefaultQualifier(NonNull.class)
public class RewardManager {

    private final Path usersFile;
    private final ComponentLogger logger;
    private final Gson gson;
    private final ConfigFactory configFactory;
    private final Economy economy;

    private @MonotonicNonNull JsonFileManager<Set<User>> fileManager;
    private Set<User> users = new HashSet<>();

    @Inject
    public RewardManager(
            final Path dataDirectory,
            final ComponentLogger logger,
            final Gson gson,
            final ConfigFactory configFactory,
            final Economy economy
    ) {
        this.usersFile = dataDirectory.resolve("users.json");
        this.logger = logger;
        this.gson = gson;
        this.configFactory = configFactory;
        this.economy = economy;

        this.init();
    }

    private void init() {
        try {

            if (!Files.exists(this.usersFile)) {
                Files.createFile(this.usersFile);
            }

            this.fileManager = new JsonFileManager<>(this.gson);

            var setType = new TypeToken<Set<User>>() {
            }.getType();
            @Nullable Set<User> userSets = this.fileManager.readJsonFromFile(usersFile, setType);
            if (userSets != null) {
                this.users = userSets;
            }

        } catch (IOException e) {
            this.logger.error("users.jsonの読み込みに失敗しました: ", e);
        }
    }

    public Optional<User> user(Player player) {
        return this.users.stream()
                .filter(user -> user.uuid().equals(player.getUniqueId()))
                .findFirst();
    }

    public Set<User> users() {
        return users;
    }

    public boolean isReceived(Player player) {
        var optionalUser = this.user(player);
        return optionalUser
                .map(User::received)
                .orElse(false);
    }

    // TODO uncord
    public boolean give(Player player) {

        if (this.isReceived(player)) {
            this.logger.info(player.getName() + "はすでに報酬を受け取っています");
            return false;
        }

        // アイテム
        var items = this.configFactory.primaryConfig().rewardSettings().items();
        var inventory = player.getInventory();
        var emptySlot = Arrays.stream(inventory.getStorageContents())
                .filter(Objects::isNull)
                .count();

        // お金
        var payAmount = this.configFactory.primaryConfig().rewardSettings().payAmount();
        this.logger.info(Component.text(payAmount));

        // チェック
        if (emptySlot <= items.size() || !this.economy.hasAccount(player)) {
            return false;
        }

        // アイテム与える
        items.forEach(reward -> {
            player.getInventory().addItem(reward);
            this.logger.info(player.name().color(NamedTextColor.GOLD)
                    .append(Component.text("に"))
                    .append(reward.displayName())
                    .append(Component.text("を与えました")));
            player.sendMessage(reward.displayName()
                    .append(Component.text("をゲットした").color(NamedTextColor.WHITE)));
        });

        // お金与える
        var result = this.economy.depositPlayer(player, payAmount);
        if (result.transactionSuccess()) {
            this.logger.info(player.name().color(NamedTextColor.GOLD)
                    .append(Component.text("に"))
                    .append(Component.text(result.amount))
                    .append(Component.text("を与えました")));
            player.sendMessage(Component.text(economy.format(result.amount))
                    .append(Component.text("をゲットした")));
        } else {
            this.logger.error(player.name().color(NamedTextColor.GOLD)
                    .append(Component.text("に"))
                    .append(Component.text(result.amount))
                    .append(Component.text("を与えるの失敗した！")));
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>報酬の受け取りに失敗しました。すたっふまでご連絡ください"));
        }

        // ファイル書き込み
        this.write(player, true);
        return true;
    }

    private void write(Player player, boolean received) {
        // データが存在し、受取情報も同一であればリターン
        if (this.isReceived(player) == received) return;

        // データが存在するなら上書き
        var optionalUser = this.user(player);
        if (optionalUser.isPresent()) {
            optionalUser.get().received(received);

            // データが存在しないなら追加
        } else {
            this.users.add(new User(player.getUniqueId(), received));
        }

        // users.jsonに書き込み
        try {
            this.fileManager.writeJsonToFile(users, this.usersFile);
        } catch (IOException e) {
            this.logger.error("users.jsonの書き込みに失敗しました: ", e);
        }
    }
}
