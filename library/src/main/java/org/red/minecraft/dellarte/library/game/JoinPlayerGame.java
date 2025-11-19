package org.red.minecraft.dellarte.library.game;

import java.util.List;

import org.red.minecraft.dellarte.library.entity.A_Player;

public interface JoinPlayerGame {
    GameResultMessage joinPlayer(A_Player player);

    GameResultMessage unJoinPlayer(A_Player player);

    List<A_Player> getJoinPlayers(A_Player player);
}
