package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class CannonBall extends DynamicGameObject {
    private static float healthCannonBall = 10;

    public CannonBall(ModelInstance instance, World world, Vector2 direction, Vector2 inertia, float velocity, float strength){
        super(instance, world, "round", BodyDef.BodyType.DynamicBody, healthCannonBall, strength);
        body.setLinearVelocity(direction.scl(velocity).add(inertia));
        EffectsManager.getInstance().addDynamicParticleEffect(GameAssetManager.getInstance().getEffectByName("smokeTrail"),getPos(),new Vector3(5,5,5),
                this, new Vector2(0,0f));
    }

    @Override
    public void update() {
        super.update();
        float y = getPos().y - 0.05f;
        setPosY(y);
        instance.transform.setTranslation(getPos());
        if (getPos().y < -5) setDestroyed();
    }
}
