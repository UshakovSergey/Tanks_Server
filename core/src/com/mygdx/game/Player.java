package com.mygdx.game;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: US
 * Date: 21.04.16
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class Player {

    private int playerPort;
    private InetAddress playerAddress = null;
    private DatagramSocket socket = null;

    public Player(int playerPort, InetAddress playerAddress, DatagramSocket socket) {
        this.playerPort = playerPort;
        this.playerAddress = playerAddress;
        this.socket = socket;
    }

    public int getPlayerPort() {
        return playerPort;
    }

    public InetAddress getPlayerAddress() {
        return playerAddress;
    }
}
