package org.red.minecraft.dellarte.library.game;

import java.util.List;

import org.red.minecraft.dellarte.library.entity.A_Player;

public interface SpecterPlayerGame {
    GameResultMessage joinSpecterPlayer(A_Player player);

    GameResultMessage unJoinSpecterPlayer(A_Player player);

    List<A_Player> getJoinSpecterPlayers(A_Player player);

}
