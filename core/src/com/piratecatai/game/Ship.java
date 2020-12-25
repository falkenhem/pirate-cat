package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Ship extends DynamicGameObject{
    protected float pitch;
    private HealthBar healthBar;
    Vector2[] cannonsLocalPosition;
    protected final int LEFT = 0;
    protected final int RIGHT = 1;

    public Ship(ModelInstance instanceArg, World world, String typeOfShape, BodyDef.BodyType bodyType, float health,
                EffectsManager effectsManager, GameAssetManager gameAssetManager) {
        super(instanceArg, world, typeOfShape, bodyType, health, effectsManager, gameAssetManager,10f);
        healthBar = new HealthBar(getPos());
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public void update(float time){
        super.update();
        instance.transform.setFromEulerAnglesRad(body.getAngle() - MathUtils.PI/2,pitch, PirateCatAI.getAngleFromBodyOnWave(body));
        instance.transform.setTranslation(getPos().x,
                100f*(MathUtils.cos(-getPos().z/100 * 3.0f * 3.1415f + time) * 0.05f *
                        MathUtils.sin(getPos().x/100 * 3.0f * 3.1415f + time))-1f, getPos().z);
        float percentHealth = getHealth()/getMaxHealth();
        healthBar.update(percentHealth,getPos());
        healthBar.getDecal().lookAt(PirateCatAI.getCam().position,PirateCatAI.getCam().up);
        PirateCatAI.getDecalBatch().add(healthBar.getDecal());
    }

    public void shoot(int localDirection, float velocity, float size){
        Vector2 direction;

        switch(localDirection) {
            case LEFT:
                direction = new Vector2(0,-1f);
                PirateCatAI.shoot(getWorldPointFromLocalBodyVector(cannonsLocalPosition[LEFT], body),
                        getWorldVectorFromLocalBodyVector(direction, body),body.getLinearVelocity(), world, velocity, size);
                break;
            case RIGHT:
                direction = new Vector2(0,1f);
                PirateCatAI.shoot(getWorldPointFromLocalBodyVector(cannonsLocalPosition[RIGHT], body),
                        getWorldVectorFromLocalBodyVector(direction, body),body.getLinearVelocity(), world, velocity, size);
                break;

        }

    }
}
