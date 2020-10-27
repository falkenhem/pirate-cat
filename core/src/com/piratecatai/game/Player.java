package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.Seek;

public class Player extends GameObject{
    boolean moving;
    boolean turnRight;
    boolean turnLeft;
    boolean shooting;
    float pitch;
    float shootCD;
    int shootingFromSide;
    int numCannons;


    public Player(ModelInstance instance, World world){
        super(instance, world, "round", BodyDef.BodyType.DynamicBody);
        pitch = 0;
        shootCD = 0;
        numCannons = 3;
    }

    public void update(float time){
        body.setAngularVelocity(0f);

        if (moving){
            body.setLinearVelocity(MathUtils.cos(body.getAngle()) * 30f,-MathUtils.sin(body.getAngle()) * 30f);

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

        if (shootCD == 0){
            if (shootingFromSide == 0) {
                direction = new Vector2(body.getLinearVelocity().y,-body.getLinearVelocity().x);
                direction.nor();
                travelDirection = body.getLinearVelocity().nor();

                for (int i = 0; i<=numCannons-1; i+=1) {
                    origin = body.getPosition().add(new Vector2(travelDirection.x*0.5f*i,travelDirection.y*0.5f*i));
                    PirateCatAI.shoot(origin,direction,body.getLinearVelocity());
                    if (i == numCannons-1) shooting = false;
                }
            }
            if (shootingFromSide == 1) {
                direction = new Vector2(-body.getLinearVelocity().y,body.getLinearVelocity().x);
                direction.nor();
                travelDirection = body.getLinearVelocity().nor();

                for (int i = 0; i<=numCannons-1; i+=1) {
                    origin = body.getPosition().add(new Vector2(travelDirection.x*0.5f*i,travelDirection.y*0.5f*i));
                    PirateCatAI.shoot(origin,direction,body.getLinearVelocity());
                    if (i == numCannons-1) shooting = false;
                }
            }
        }

    }


}
