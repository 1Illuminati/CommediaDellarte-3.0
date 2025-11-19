package org.red.minecraft.dellarte.library.game;

import org.bukkit.potion.PotionEffectType;

public class GameResultMessage {
    private final AbstractGame game;
    private final String message;
    private final String header;

    public GameResultMessage(AbstractGame game, String message) {
        this(game, message, ResultMessageHeader.INFO);
    }

    public GameResultMessage(AbstractGame game, String message, String header) {
        this.game = game;
        this.message = message;
        this.header = header;
    }

    public GameResultMessage(AbstractGame game, String message, ResultMessageHeader header) {
        this(game, message, header.header);
    }

    public AbstractGame getGame() {
        return this.game;
    }

    public String getMessage() {
        return this.message;
    }

    public String getHeader() {
        return this.header;
    }

    public String getResultMessage() {
        return String.format("[%s] Game: %s, ID: %s\n%s", this.header, this.game.getGameName().toString(), this.game.getGameID(), this.message);
    }

    public enum ResultMessageHeader {
        ERROR("ERROR"),
        INFO("INFO"),
        WARING("WARING"),
        START("START"),
        STOP("STOP");

        public final String header;
        ResultMessageHeader(String header) {
            this.header = header;
        }
    }
}
