package org.red.library.game;

import org.red.library.entity.A_Player;

import java.util.List;

public interface JoinPlayerGame {
    GameResultMessage joinPlayer(A_Player player);

    GameResultMessage unJoinPlayer(A_Player player);

    List<A_Player> getJoinPlayers(A_Player player);
}
