package org.red.compatibility.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.red.library.CommediaDellarte;
import org.red.library.user.A_OfflinePlayer;

public class A_PapiPlayer extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "aplayer";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Inok";
    }

    @Override
    public @NotNull String getVersion() {
        return "3.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        A_OfflinePlayer aOfflinePlayer = CommediaDellarte.getAOfflinePlayer(player);
        String[] split = identifier.replace("aplayer", "").split("\\.");
        if (split.length == 0) return null;
        return null;
    }
}
