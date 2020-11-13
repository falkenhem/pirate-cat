package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class DynamicGameObject extends GameObject{
    private float health;
    private float maxHealth;
    private boolean destroyed;

    public DynamicGameObject(ModelInstance instanceArg, World world, String typeOfShape, BodyDef.BodyType bodyType, float health) {
        super(instanceArg, world, typeOfShape, bodyType);
        this.health = health;
        maxHealth = health;
    }

    public void update(){
        setPos(body.getPosition());
        if (getPos().x > PirateCatAI.getMapWidth() || getPos().x < 0) setDestroyed();
        if (getPos().z > PirateCatAI.getMapHeight() || getPos().z < 0) setDestroyed();
    }

    public void gotHit(int strength){
        setHealth(10f * strength);
        if (health <= 0) setDestroyed();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    private void setHealth(float change){
        health -= change;
    }

    public float getHealth(){
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    private void setDestroyed(){
        destroyed = true;
    }
}
