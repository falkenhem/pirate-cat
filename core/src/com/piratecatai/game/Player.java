package com.piratecatai.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.ai.steer.Steerable;

public class Player extends Ship{
    boolean moving;
    boolean turnRight;
    boolean turnLeft;
    boolean shooting;
    float gunsCD;
    int shootingFromSide;
    int numCannons;


    public Player(ModelInstance instance, World world, float health){
        super(instance, world, "round", BodyDef.BodyType.DynamicBody, health);
        pitch = 0;
        gunsCD = 0f;
        numCannons = 2;
    }

    public void update(float time){
        super.update();

        body.setAngularVelocity(0f);

        if (moving){
            body.setLinearVelocity(MathUtils.cos(body.getAngle()) * 40f,-MathUtils.sin(body.getAngle()) * 40f);
        } else body.setLinearDamping(1f);

        turnAndSetPitch();

        instance.transform.setFromEulerAnglesRad(body.getAngle(),pitch, PirateCatAI.getAngleFromBodyOnWave(body));
        instance.transform.setTranslation(body.getPosition().x,
                100f*(MathUtils.cos(-body.getPosition().y/100 * 3.0f * 3.1415f + time) * 0.05f *
                        MathUtils.sin(body.getPosition().x/100 * 3.0f * 3.1415f + time))-3f, body.getPosition().y);

        if (shooting) shoot();
    }

    private void turnAndSetPitch(){
        if(!turnRight && !turnLeft) {
            if (pitch < 0f) pitch += 0.01;
            if (pitch > 0f) pitch -= 0.01f;
        }

        if (turnRight){
            body.setAngularVelocity(-0.8f);
            if (pitch < 0.3f) pitch += 0.01f;
        }
        if (turnLeft){
            body.setAngularVelocity(0.8f);
            if (pitch > -0.3f) pitch -= 0.01f;
        }
    }

    private void shoot(){
        Vector2 direction;
        Vector2 origin;
        Vector2 travelDirection;

        //change to use worldVector and final int for sides

        if (gunsCD == 0){
            if (shootingFromSide == 0) {
                //change so you can shoot from stand-still, this is dumb :)
                direction = new Vector2(body.getLinearVelocity().y,-body.getLinearVelocity().x);
                direction.nor();
                travelDirection = body.getLinearVelocity().nor();

                for (int i = 0; i<=numCannons-1; i+=1) {
                    origin = body.getPosition().add(new Vector2(travelDirection.x*0.3f*i,travelDirection.y*0.3f*i));
                    PirateCatAI.shoot(origin,direction,body.getLinearVelocity());
                    if (i == numCannons-1) shooting = false;
                }
            }
            if (shootingFromSide == 1) {
                direction = new Vector2(-body.getLinearVelocity().y,body.getLinearVelocity().x);
                direction.nor();
                travelDirection = body.getLinearVelocity().nor();

                for (int i = 0; i<=numCannons-1; i+=1) {
                    origin = body.getPosition().add(new Vector2(travelDirection.x*0.3f*i,travelDirection.y*0.3f*i));
                    PirateCatAI.shoot(origin,direction,body.getLinearVelocity());
                    if (i == numCannons-1) shooting = false;
                }
            }
        }

    }

    public Steerable getSteerable() {
        return steerable;
    }

    public Vector2 getFuturePositionWithTime(float time){
        Vector2 futurePosition;
        futurePosition = body.getPosition().add(body.getLinearVelocity().scl(time/ Gdx.graphics.getDeltaTime()));

        return futurePosition;
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    private void debugWorldPoint(){
        Vector2 localPoint;
        Vector2 worldPoint;

        localPoint = new Vector2(100,0);
        worldPoint = body.getWorldPoint(localPoint);

        PirateCatAI.addDebugInstance(worldPoint);

    }


}
