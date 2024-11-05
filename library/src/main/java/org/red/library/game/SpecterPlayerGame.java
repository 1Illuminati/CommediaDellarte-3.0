package org.red.library.game;

import org.red.library.entity.A_Player;

import java.util.List;

public interface SpecterPlayerGame {
    GameResultMessage joinSpecterPlayer(A_Player player);

    GameResultMessage unJoinSpecterPlayer(A_Player player);

    List<A_Player> getJoinSpecterPlayers(A_Player player);

}
