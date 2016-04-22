package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.lang.ref.SoftReference;

public class MainClass extends ApplicationAdapter {
    SpriteBatch batch;
    public static PlayerTank plTank;
    final int NUMBER_OF_BOTS = 15;
    Array<BotTank> botList = new Array<BotTank>();

    @Override
    public void create() {

        Thread thread = new Thread(new Server());
        thread.start();

        batch = new SpriteBatch();
        plTank = new PlayerTank(new Vector2(0, 0), new Vector2(0, 0));

  /*      for (int i = 0; i < NUMBER_OF_BOTS; i++) {
            botList.add(new BotTank(new Vector2(BotTank.rn.nextInt(800), BotTank.rn.nextInt(600)), new Vector2(BotTank.rn.nextInt(4) - 1.5f, BotTank.rn.nextInt(4) - 1.5f)));
        }    */

    }

    @Override
    public void render() {
        update();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        plTank.draw(batch);
    /*    for (int i = 0; i < botList.size; i++) {
            botList.get(i).draw(batch);
        }   */

        batch.end();

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