package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class CannonBall extends DynamicGameObject {
    private static float healthCannonBall = 10;

    public CannonBall(ModelInstance instance, World world, Vector2 direction, Vector2 inertia){
        super(instance, world, "round", BodyDef.BodyType.DynamicBody, healthCannonBall);
        body.setLinearVelocity(direction.scl(300f).add(inertia));
    }


}
