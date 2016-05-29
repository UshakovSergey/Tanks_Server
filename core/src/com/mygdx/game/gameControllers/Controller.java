package com.mygdx.game.gameControllers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.interfaces.Observer;
import com.mygdx.game.net.*;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Random;

public class Controller implements Observer {

    private DatagramSocket serverSocket;
    private final int NUMBER_OF_BOTS = 10;
    private ArrayList<NetBotTank> botList = new ArrayList<NetBotTank>();
    private ArrayList<NetBullet> bulletsSet = new ArrayList<NetBullet>();
    private ArrayList<Player> playerList = new ArrayList<Player>();
    private ArrayList<NetPlayerTank> playerTankSet = new ArrayList<NetPlayerTank>();
    private Random rn = new Random();
    private static int playerID = 0;
    private Packet newData;
    private int tankTextureSize = 32;

    public Controller(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.createBots();
    }

       public void setNewData(Packet newData) {
        this.newData = newData;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    private void createBots() {
        for (int i = 0; i < NUMBER_OF_BOTS; i++) {
            botList.add(new NetBotTank(new Vector2(rn.nextInt(480), rn.nextInt(480)), new Vector2(rn.nextInt(4) - 1.5f, 0), 0, 0));
        }
    }

    public void registerNewClient() {

        if (newData.getMsg().trim().equals("ready")) {
            playerID++;
            playerList.add(new Player(newData.getClientPort(), newData.getClientAddress(), playerID));
            playerTankSet.add(new NetPlayerTank(new Vector2(0, 0), 0, playerID));

            NetSupport.sendPacket(serverSocket, newData.getClientAddress(), newData.getClientPort(), new Packet("", playerID, 0, 0, 0, null, null));
            System.out.println("Player #" + playerID + " is ready");
        }
    }

    public void updateBulletsPosition() {

        for (int i = 0; i < bulletsSet.size(); i++) {

            bulletsSet.get(i).getPosition().add(bulletsSet.get(i).getSpeed());
            if (bulletsSet.get(i).getPosition().x > Gdx.graphics.getWidth() ||
                    bulletsSet.get(i).getPosition().x < 0 ||
                    bulletsSet.get(i).getPosition().y > Gdx.graphics.getHeight() ||
                    bulletsSet.get(i).getPosition().y < 0)
                bulletsSet.get(i).setBulletOutOffScreen(true);
        }
    }

    public void removeOffScreenBullets() {
        for (int i = 0; i < bulletsSet.size(); i++) {
            if (bulletsSet.get(i).isBulletOutOffScreen()) bulletsSet.remove(i);
        }
    }

    public void addBullets() {
        bulletsSet.addAll(newData.getStore());
    }

    public void setBulletsStore() {
        newData.setStore(bulletsSet);
    }

    public void updateBotsPosition() {

        for (int i = 0; i < botList.size(); i++) {


            float prevX = botList.get(i).getPosition().x;
            float prevY = botList.get(i).getPosition().y;


            botList.get(i).getPosition().add(botList.get(i).getSpeed());

            if (botList.get(i).getPosition().x < 0 || botList.get(i).getPosition().x > Gdx.graphics.getWidth() - tankTextureSize) {
                botList.get(i).getPosition().x -= botList.get(i).getSpeed().x;
            }
            if (botList.get(i).getPosition().y < 0 || botList.get(i).getPosition().y > Gdx.graphics.getHeight() - tankTextureSize) {
                botList.get(i).getPosition().y -= botList.get(i).getSpeed().y;
            }

            botList.get(i).setTurnTime(botList.get(i).getTurnTime() + Gdx.graphics.getDeltaTime());

            if (botList.get(i).getTurnTime() > 0.5) {
                if (Math.abs(prevX - botList.get(i).getPosition().x) < 0.001 && Math.abs(prevY - botList.get(i).getPosition().y) < 0.001) {
                    botList.get(i).getSpeed().rotate(90 * rn.nextInt(3));
                }
                botList.get(i).setTurnTime(0);
            }

        }
    }

    public void setDeadBots() {

        for (int i = 0; i < botList.size(); i++) {
            for (int j = 0; j < bulletsSet.size(); j++) {
                if (botList.get(i).getPosition().cpy().add(tankTextureSize / 2, tankTextureSize / 2).sub(bulletsSet.get(j).getPosition()).len() < tankTextureSize / 2) {
                    botList.get(i).setAlive(false);
                    bulletsSet.get(j).setBulletOutOffScreen(true);
                }
            }
        }
    }

    public void killBots() {
        for (int i = 0; i < botList.size(); i++) {
            if (!botList.get(i).isAlive()) {
                botList.get(i).setDestroyDebounce(botList.get(i).getDestroyDebounce() + Gdx.graphics.getDeltaTime());
                botList.get(i).setAlive(false);
                botList.get(i).setSpeed(new Vector2(0, 0));
                if (botList.get(i).getDestroyDebounce() > 0.1) {
                    botList.remove(i);
                }
            }
        }
    }

    public void setBotList() {
        newData.setBotSet(botList);
    }

    public void updatePlayerPosition() {
        for (int i = 0; i < playerTankSet.size(); i++) {
            if (newData.getPlayerId() == playerTankSet.get(i).getPlayerID()) {
                playerTankSet.get(i).setPosition(new Vector2(newData.getPosX(), newData.getPosY()));
                playerTankSet.get(i).setRotateAngle(newData.getRotateAngle());
            }
        }
    }

    public void setPlayerList() {
        newData.setPlayerList(playerTankSet);
    }

    public void notifyClients() {
        int plID = newData.getPlayerId();
        for (int i = 0; i < playerList.size(); i++) {
            if (plID != playerList.get(i).getPlayerId()) {
                NetSupport.sendPacket(serverSocket, playerList.get(i).getPlayerAddress(), playerList.get(i).getPlayerPort(), newData);
            }
        }
    }

    public void updateBullets() {
        this.addBullets();
        this.updateBulletsPosition();
        this.removeOffScreenBullets();
        this.setBulletsStore();
    }

    public void updateBots() {
        this.updateBotsPosition();
        this.setDeadBots();
        this.killBots();
        this.setBotList();
    }

    public void updatePlayers() {
        this.updatePlayerPosition();
        this.setPlayerList();
    }
}
