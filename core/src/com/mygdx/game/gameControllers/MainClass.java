package com.mygdx.game.gameControllers;

import com.badlogic.gdx.ApplicationAdapter;

public class MainClass extends ApplicationAdapter {

    public void create() {
        Thread thread = new Thread(new Server());
        thread.start();
    }
}