package chalmers.eda397g1.Events;

/**
 * Created by julius on 20.04.17.
 */

public class PlayerJoinedLobbyEvent extends Event {
    private String playerName;
    public PlayerJoinedLobbyEvent(){
        // TODO: parse data
    }

    public String getPlayerName(){
        return playerName;
    }
}
