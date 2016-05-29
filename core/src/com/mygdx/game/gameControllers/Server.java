package com.mygdx.game.gameControllers;

import com.mygdx.game.net.*;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {

    private static DatagramSocket serverSocket = null;
    private int serverPort = 4444;
    private Controller controller;

    public Server() {
        try {
            serverSocket = new DatagramSocket(serverPort);
            controller = new Controller(serverSocket);
        } catch (SocketException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Server is started.");
            while (true) {

                TimeUnit.MICROSECONDS.sleep(10);

                controller.setNewData(NetSupport.getPacket2(serverSocket));

                controller.registerNewClient();

                if (controller.getPlayerList().size() >= 2) {

                    controller.updateBullets();

                    controller.updateBots();

                    controller.updatePlayers();

                    controller.notifyClients();

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
