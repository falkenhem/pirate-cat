package com.piratecatai.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashMap;

public class GameObject {
    public Body body;
    private FixtureDef playerFixture;
    private Vector3 pos;
    public Texture texture;
    public ModelInstance instance;
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    private final static BoundingBox bounds = new BoundingBox();
    protected float speed;
    protected World world;
    SteerableEntity steerable;



    public GameObject(ModelInstance instanceArg, World world, String typeOfShape, BodyDef.BodyType bodyType){
        this.world = world;
        speed = 0f;
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

    }
    public float getSpeed(){
        return speed;
    }

    public Vector2 getWorldVectorFromLocalBodyVector(Vector2 localVector, Body body){
        //Own method for calculation world coordinates because this.game uses x-z plane and box2d uses the x-y plane
        Vector2 worldVector;
        worldVector = new Vector2(body.getWorldVector(localVector)).scl(1f,-1f);
        //turn vectors an additional 90degrees because model is turned incorrectly FIX THIS! when using proper assets
        worldVector = new Vector2(worldVector.y, -worldVector.x);

        return worldVector;
    }

    public Vector2 getWorldPointFromLocalBodyVector(Vector2 localPoint, Body body){
        //Own method for calculation world coordinates because this.game uses x-z plane and box2d uses the x-y plane
        Vector2 worldPoint;
        worldPoint = new Vector2(body.getWorldVector(localPoint)).scl(1f,-1f);
        //turn vectors an additional 90degrees because model is turned incorrectly FIX THIS! when using proper assets
        worldPoint = new Vector2(worldPoint.y, -worldPoint.x);

        worldPoint = worldPoint.add(body.getPosition());

        return worldPoint;
    }

    public Vector3 getPos() {
        return pos;
    }
    public Vector3 getPos(Float setY) {
        if (setY != null) return new Vector3(pos.x,setY,pos.z);
        else return pos;
    }

    public void setPos(Vector2 posBody){
        pos.x = posBody.x;
        pos.z = posBody.y;
    }
}
