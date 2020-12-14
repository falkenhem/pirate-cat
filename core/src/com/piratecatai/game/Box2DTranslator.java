package com.piratecatai.game;

import com.badlogic.gdx.math.Vector2;

public class Box2DTranslator {
    private  static Box2DTranslator instance;
    private final float scale = 10;

    public static Box2DTranslator getInstance(){
        if (instance == null) {
            instance = new Box2DTranslator();
        }
        return instance;
    }

    public Vector2 box2dToWorldCoordinates(Vector2 box2dVector){
        Vector2 worldVector = new Vector2();
        worldVector = box2dVector.scl(scale);
        return worldVector;
    }

    public Vector2 worldToBox2DVector(Vector2 worldVector){
        Vector2 box2DVector = new Vector2();
        box2DVector = worldVector.scl(1/scale);
        return box2DVector;
    }

    public float box2dToWorldFloat(float box2dValue){
        float worldValue;
        worldValue = box2dValue * scale;
        return worldValue;
    }

    public float worldToBox2DFloat(float worldValue){
        float box2DValue;
        box2DValue = worldValue/scale;
        return box2DValue;
    }
}
