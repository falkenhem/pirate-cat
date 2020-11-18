package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Ship extends DynamicGameObject{
    protected float pitch;
    private HealthBar healthBar;

    public Ship(ModelInstance instanceArg, World world, String typeOfShape, BodyDef.BodyType bodyType, float health,
                EffectsManager effectsManager, GameAssetManager gameAssetManager) {
        super(instanceArg, world, typeOfShape, bodyType, health, effectsManager, gameAssetManager);
        healthBar = new HealthBar(getPos());
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public void update(){
        super.update();
        float percentHealth = getHealth()/getMaxHealth();
        healthBar.update(percentHealth,getPos());
        healthBar.getDecal().lookAt(PirateCatAI.getCam().position,PirateCatAI.getCam().up);
        PirateCatAI.getDecalBatch().add(healthBar.getDecal());
    }
}
