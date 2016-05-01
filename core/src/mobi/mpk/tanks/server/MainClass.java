package mobi.mpk.tanks.server;

import com.badlogic.gdx.ApplicationAdapter;

public class MainClass extends ApplicationAdapter {
//    SpriteBatch batch;
 //   public static PlayerTank plTank;
 //   final int NUMBER_OF_BOTS = 15;
 //   Array<BotTank> botList = new Array<BotTank>();

    @Override
    public void create() {

        Thread thread = new Thread(new Server());
        thread.start();

    //    batch = new SpriteBatch();

    }

    @Override
    public void render() {
     /*   update();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

       // plTank.draw(batch);
        for (int i = 0; i < botList.size; i++) {
            botList.get(i).draw(batch);
        }

        batch.end();  */

    }


    public void update() {

   /*     plTank.update();
        for (int i = 0; i < botList.size; i++) {
            botList.get(i).update();
            if (plTank.store.size() > 0) {
                for (int j = 0; j < plTank.store.size(); j++) {
                    if (botList.get(i).Position.cpy().add(botList.get(i).tankTextureSize / 2, botList.get(i).tankTextureSize / 2).sub(plTank.store.get(j).getBulletPosition()).len() < botList.get(i).tankTextureSize / 2 && !plTank.store.get(j).isDestroyed()) {
                        botList.get(i).destroy();
                        plTank.store.get(j).destroy();
                    }
                }
            }
        }

        if (botList.size > 0) {
            for (int i = 0; i < botList.size; i++) {
                if (!botList.get(i).isAlive()) {
                    botList.get(i).destroyDebounce += Gdx.graphics.getDeltaTime();
                    if (botList.get(i).destroyDebounce > 0.4) {
                        botList.removeIndex(i);
                    }
                }
            }
        } */
    }
}