package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Island extends GameObject{

    public Island(ModelInstance instance, World world){
        super(instance, world, "round", BodyDef.BodyType.StaticBody);

    }

    public void update(){
        super.update();
        instance.transform.setTranslation(getPos().x,-10f,
                getPos().z);
    }
}
