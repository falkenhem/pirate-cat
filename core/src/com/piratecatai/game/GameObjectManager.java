package com.piratecatai.game;

import com.badlogic.gdx.utils.Array;

public class GameObjectManager {
    private Array<GameObject> gameObjects = new Array<>();

    public void addGameObject(GameObject gameObject){
        gameObjects.add(gameObject);
    }
}
