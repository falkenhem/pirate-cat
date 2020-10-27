package com.piratecatai.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class BodyCreator {
    private static World world;
    private static BodyCreator thisInstance;
    public static final int STEEL = 0;
    public static final int WOOD = 1;
    public static final int RUBBER = 2;
    public static final int STONE = 3;

    private BodyCreator(World world){
        this.world = world;
    }

    public static BodyCreator getInstance(World world){
        if(thisInstance == null){
            thisInstance = new BodyCreator(world);
        }
        return thisInstance;
    }

    public static FixtureDef makeFixture(int material, Shape shape){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        switch(material){
            case STEEL:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.3f;
                fixtureDef.restitution = 0.1f;
                break;
            case WOOD:
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.7f;
                fixtureDef.restitution = 0.3f;
                break;
            case RUBBER:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0f;
                fixtureDef.restitution = 1f;
                break;
            case STONE:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.9f;
                fixtureDef.restitution = 0.01f;
                break;
            default:
                fixtureDef.density = 7f;
                fixtureDef.friction = 0.5f;
                fixtureDef.restitution = 0.3f;
        }

        return fixtureDef;
    }

    public static Body makeCirclePolyBody(float posx, float posy, float radius, int material,
                                          BodyDef.BodyType bodyType, boolean fixedRotation){
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        Body boxBody = world.createBody(boxBodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius / 2);
        boxBody.createFixture(makeFixture(material, circleShape));
        circleShape.dispose();
        return boxBody;
    }

    /** with dynamic body and fixed rotation */
    public static Body makeCirclePolyBody(float posx, float posy, float radius, int material){
        return makeCirclePolyBody(posx, posy,
                radius, material,
                BodyDef.BodyType.DynamicBody, true);
    }

    public static Body makeBoxPolyBody(float posx, float posy,
                                       float width, float height,
                                       int material, BodyDef.BodyType bodyType, boolean fixedRotation){

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width/2, height/2);
        boxBody.createFixture(makeFixture(material, poly));
        poly.dispose();

        return boxBody;
    }

    /** body with without fixed rotation */
    public static Body makeBoxPolyBody(float posx, float posy,
                                       float width, float height,
                                       int material, BodyDef.BodyType bodyType){
        return makeBoxPolyBody(posx, posy, width, height, material, bodyType, false);
    }

    public static Body makePolygonShapeBody(Vector2[] vertices, float posx, float posy,
                                            int material, BodyDef.BodyType bodyType){
        BodyDef polyBodyDef = new BodyDef();
        polyBodyDef.type = bodyType;
        polyBodyDef.position.x = posx;
        polyBodyDef.position.y = posy;
        Body polyBody = world.createBody(polyBodyDef);

        PolygonShape polygon = new PolygonShape();
        polygon.set(vertices);
        polyBody.createFixture(makeFixture(material, polygon));

        return polyBody;
    }


}
