package com.piratecatai.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject {
    public Body body;
    private FixtureDef playerFixture;
    public Vector3 pos;
    public Texture texture;
    public ModelInstance instance;
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public boolean destroyed;
    private final static BoundingBox bounds = new BoundingBox();
    SteerableEntity steerable;



    public GameObject(ModelInstance instanceArg, World world, String typeOfShape, BodyDef.BodyType bodyType){
        destroyed = false;
        instance = instanceArg;
        instance.calculateBoundingBox(bounds);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);

        pos = instance.transform.getTranslation(new Vector3());

        switch (typeOfShape){
            case "round":
                body = PirateCatAI.bodycreator.makeCirclePolyBody(pos.x,pos.z,dimensions.x,1,bodyType,false);
                break;
            case "rect":
                body = PirateCatAI.bodycreator.makeBoxPolyBody(pos.x,pos.z,dimensions.x,dimensions.y,1,bodyType,false);
                break;
        }

        body.setUserData(this);

        steerable = new SteerableEntity(body);


        /*BodyDef bodyDef = new BodyDef();
        switch (bodyType){
            case "static":
                bodyDef.type = BodyDef.BodyType.StaticBody;
                break;
            case "dynamic":
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                break;
        }
        bodyDef.position.set(pos.x,pos.z);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        switch (typeOfShape){
            case "round":
                shape.setAsBox(dimensions.x/2,dimensions.z/2);
                break;
            case "rect":
                shape.setAsBox(dimensions.x/2,dimensions.z/2);
                break;
        }

        //shape.setAsBox(dimensions.x,dimensions.y);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        body.createFixture(fixtureDef);

        body.setUserData(this);

        shape.dispose();
        */
    }


}