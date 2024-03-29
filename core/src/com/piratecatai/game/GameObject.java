package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject {
    public Body body;
    private Vector3 pos;
    public ModelInstance instance;
    public final Vector3 center = new Vector3();

    public Vector3 getDimensions() {
        return dimensions;
    }

    public final Vector3 dimensions = new Vector3();

    public static BoundingBox getBounds() {
        return bounds;
    }

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

        this.pos = instance.transform.getTranslation(new Vector3());

        switch (typeOfShape){
            case "round":
                body = PirateCatAI.bodycreator.makeCirclePolyBody(Box2DTranslator.getInstance().worldToBox2DFloat(pos.x)
                        ,Box2DTranslator.getInstance().worldToBox2DFloat(pos.z),Box2DTranslator.getInstance().worldToBox2DFloat(dimensions.x)
                        ,1,bodyType,false);
                break;
            case "rect":
                body = PirateCatAI.bodycreator.makeBoxPolyBody(Box2DTranslator.getInstance().worldToBox2DFloat(pos.x)
                        ,Box2DTranslator.getInstance().worldToBox2DFloat(pos.z),dimensions.x,dimensions.y,1,bodyType,false);
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

        worldVector = Box2DTranslator.getInstance().box2dToWorldVector(worldVector);

        return worldVector;
    }

    public Vector2 getWorldPointFromLocalBodyVector(Vector2 localPoint, Body body){
        //Own method for calculation world coordinates because this.game uses x-z plane and box2d uses the x-y plane
        Vector2 worldPoint;
        worldPoint = new Vector2(body.getWorldVector(localPoint)).scl(1f,-1f);
        //turn vectors an additional 90degrees because model is turned incorrectly FIX THIS! when using proper assets
        worldPoint = new Vector2(worldPoint.y, -worldPoint.x);

        worldPoint = worldPoint.add(body.getPosition());

        worldPoint = Box2DTranslator.getInstance().box2dToWorldVector(worldPoint);

        return worldPoint;
    }

    public Vector3 getPos() {
        return pos;
    }

    public Vector2 getPos2D(){
        Vector2 vector = new Vector2(pos.x,pos.z);
        return vector;
    }
    public Vector3 getPos(Float setY) {
        if (setY != null) return new Vector3(pos.x,setY,pos.z);
        else return pos;
    }

    public void update(){
        setPos(body.getPosition());
    }

    public void setPos(Vector2 posBody){
        pos.x = Box2DTranslator.getInstance().box2dToWorldFloat(posBody.x);
        pos.z = Box2DTranslator.getInstance().box2dToWorldFloat(posBody.y);
    }

    public void setPosY(float posY){
        pos.y = posY;
    }

    public Body getBody() {
        return body;
    }
}
