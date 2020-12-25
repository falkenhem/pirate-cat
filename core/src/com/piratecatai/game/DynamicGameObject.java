package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class DynamicGameObject extends GameObject{
    private float health;
    private float maxHealth;
    private boolean destroyed;
    private float strength;
    protected EffectsManager effectsManager;
    protected GameAssetManager gameAssetManager;

    public DynamicGameObject(ModelInstance instanceArg, World world, String typeOfShape, BodyDef.BodyType bodyType,
                             float health, float strength) {
        this(instanceArg,world,typeOfShape,bodyType,health,null,null,strength);
    }

    public DynamicGameObject(ModelInstance instanceArg, World world, String typeOfShape, BodyDef.BodyType bodyType,
                             float health, EffectsManager effectsManager, GameAssetManager gameAssetManager, float strength) {
        super(instanceArg, world, typeOfShape, bodyType);
        this.health = health;
        this.effectsManager = effectsManager;
        this.gameAssetManager = gameAssetManager;
        maxHealth = health;
        this.strength = strength;
    }

    public void update(){
        super.update();
        /*if (getPos().x > PirateCatAI.getMapWidth() || getPos().x < 0) setDestroyed();
        if (getPos().z > PirateCatAI.getMapHeight() || getPos().z < 0) setDestroyed();*/
    }

    public Vector2 getWorldVelocity(){
        Vector2 vector = Box2DTranslator.getInstance().box2dToWorldVector(body.getLinearVelocity());
        return vector;
    }

    public void gotHit(int strength){
        setHealth(10f * strength);
        if (health <= 0) {
            setDestroyed();
        }
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

    protected void setDestroyed(){
        destroyed = true;
    }

    public float getStrength() {
        return strength;
    }
}
