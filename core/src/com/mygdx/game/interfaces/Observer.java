package com.mygdx.game.interfaces;

public interface Observer {
    void registerNewClient();
    void notifyClients();
    //void removeClient(); TBD  ....
}
