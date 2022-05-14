package com.badlogic.astar.screens;

import com.badlogic.astar.autoTiler.AutoTiler;
import com.badlogic.astar.entities.Enemy;
import com.badlogic.astar.entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Play implements Screen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap currentmap;


    private Player player;
    private Enemy enemy;


    private AutoTiler autoTiler;
    private static final int MAP_WIDTH = 16;
    private static final int MAP_HEIGHT = 12;



    @Override
    public void show() {

        /*renderer = new OrthogonalTiledMapRenderer(map);*/

        camera = new OrthographicCamera();
        viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);


        // Auto generate a new map
        autoTiler = new AutoTiler(MAP_WIDTH, MAP_HEIGHT, Gdx.files.internal("tileset.json"));
        map = autoTiler.generateMap();
        

        //setup map renderer
        final float unitScale = 1f / Math.max(autoTiler.getTileWidth(), autoTiler.getTileHeight());
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);

        player = new Player(new Sprite(new Texture("images/Bot Wheel/PlayerSingle.png")), (TiledMapTileLayer) map.getLayers().get(0));
        player.setPosition(2,2);
        player.setScale(0.5f * unitScale);

        enemy = new Enemy(new Sprite(new Texture("images/Texture/Enemy.png")), (TiledMapTileLayer) map.getLayers().get(0));
        enemy.setPosition(6 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 26)* player.getCollisionLayer().getTileHeight());


        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render map
        viewport.apply(true);
        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();
        player.draw(renderer.getBatch());
        enemy.draw(renderer.getBatch());
        renderer.getBatch().end();

        /*camera.position.set(player.getVelocity(), 0);
        camera.update();*/
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        player.getTexture().dispose();
        enemy.getTexture().dispose();
    }
}
