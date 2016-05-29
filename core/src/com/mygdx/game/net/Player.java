package com.mygdx.game.net;
import java.net.InetAddress;

public class Player {

    private int playerPort;
    private InetAddress playerAddress = null;
    private int playerId = -1;

    public Player(int playerPort, InetAddress playerAddress, int playerId) {
        this.playerPort = playerPort;
        this.playerAddress = playerAddress;
        this.playerId = playerId;
    }

    public int getPlayerPort() {
        return playerPort;
    }

    public InetAddress getPlayerAddress() {
        return playerAddress;
    }

    public int getPlayerId() {
        return playerId;
    }

}
