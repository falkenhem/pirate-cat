package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Island extends GameObject{

    public Island(ModelInstance instance, World world){
        super(instance, world, "round", BodyDef.BodyType.StaticBody);

    }
}
