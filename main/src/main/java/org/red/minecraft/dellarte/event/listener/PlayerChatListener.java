package org.red.minecraft.dellarte.event.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.red.minecraft.dellarte.entity.A_PlayerImpl;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.entity.A_Player;

public class PlayerChatListener extends DellarteListener {
    @EventHandler
    public void asyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        A_PlayerImpl player = (A_PlayerImpl) CommediaDellarte.getAPlayer(event.getPlayer());

        if (player.getPlayerStatus(A_Player.PlayerStatus.ChatEvent)) {
            player.setPlayerStatus(A_Player.PlayerStatus.ChatEvent, false);
            player.runPlayerChatRunnable(event);
        }
    }
}
