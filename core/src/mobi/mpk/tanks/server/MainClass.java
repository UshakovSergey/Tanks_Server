package mobi.mpk.tanks.server;

import com.badlogic.gdx.ApplicationAdapter;

public class MainClass extends ApplicationAdapter {

    @Override
    public void create() {
        Thread thread = new Thread(new Server());
        thread.start();
    }

    @Override
    public void render() {
    }
}