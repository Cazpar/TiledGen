package com.badlogic.astar.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Sprite {
    /** the movement velocity **/
    private Vector2 velocity = new Vector2();
    private float speed = 60 * 2, increment;

    private TiledMapTileLayer collisionLayer;
    private String blockedKey = "blocked";


    public Enemy(Sprite sprite, TiledMapTileLayer collisionLayer){
        super(sprite);
        this.collisionLayer = collisionLayer;
    }
    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }
    public void update(float deltaTime) {

        //save old position before update
        float oldX = getX(), oldY = getY();
        boolean collidedX = false, collidedY = false;



        //move on x
        setX(getX() + velocity.x * deltaTime);

        //calculate the increment for step in #collidesLeft() and #collidesRight
        increment = collisionLayer.getTileWidth();
        increment = getWidth() < increment ? getWidth() / 2 : increment / 2;


        if (velocity.x < 0){
            collidedX = collidesLeft();
        } else if (velocity.x > 0){
            collidedX = collidesRight();
        }

        //react to x collision
        if (collidedX){
            setX(oldX);
            velocity.x = 0;
        }

        // move on y
        setY(getY() + velocity.y * deltaTime);

        // calculate the increment for step in #collidesBottom() and #collidesTop()
        increment = collisionLayer.getTileHeight();
        increment = getHeight() < increment ? getHeight() / 2 : increment / 2;

        if(velocity.y < 0) // going down
            collidedY = collidesBottom();
        else if(velocity.y > 0) // going up
            collidedY = collidesTop();

        //react to y collision
        if (collidedY){
            setY(oldY);
            velocity.y = 0;
        }
    }
    private boolean isCellBlocked(float x, float y) {
        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
    }

    public boolean collidesRight() {
        for(float step = 0; step <= getHeight(); step += increment)
            if(isCellBlocked(getX() + getWidth(), getY() + step))
                return true;
        return false;
    }

    public boolean collidesLeft() {
        for(float step = 0; step <= getHeight(); step += increment)
            if(isCellBlocked(getX(), getY() + step))
                return true;
        return false;
    }

    public boolean collidesTop() {
        for(float step = 0; step <= getWidth(); step += increment)
            if(isCellBlocked(getX() + step, getY() + getHeight()))
                return true;
        return false;

    }

    public boolean collidesBottom() {
        for(float step = 0; step <= getWidth(); step += increment)
            if(isCellBlocked(getX() + step, getY()))
                return true;
        return false;
    }
}
