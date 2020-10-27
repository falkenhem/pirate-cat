package com.piratecatai.game;

import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.SingleRayConfiguration;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;


public class NPCship extends GameObject{
    float pitch;
    SteeringAcceleration<Vector2> steering;
    SteeringAcceleration<Vector2> steering2;
    SingleRayConfiguration<Vector2> rayConfig;
    CentralRayWithWhiskersConfiguration<Vector2> rayConfig2;
    Pursue<Vector2> pursue;
    RaycastCollisionDetector<Vector2> raycastCollisionDetector;
    RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance;
    PrioritySteering<Vector2> prioritySteering;
    BlendedSteering<Vector2> blendedSteering;

    public NPCship(ModelInstance instance, World world){
        super(instance, world, "round", BodyDef.BodyType.DynamicBody);

        steering= new SteeringAcceleration<Vector2>(new Vector2());
        steering2= new SteeringAcceleration<Vector2>(new Vector2());
        pitch = 0;
        rayConfig = new SingleRayConfiguration<Vector2>(steerable,30f);
        rayConfig2 = new CentralRayWithWhiskersConfiguration<Vector2>(steerable, 40,
                35, 35 * MathUtils.degreesToRadians);
        raycastCollisionDetector = new Box2dRaycastCollisionDetector(world);
        raycastObstacleAvoidance = new RaycastObstacleAvoidance<>(steerable,rayConfig2,raycastCollisionDetector,10f);
        pursue = new Pursue<Vector2>(steerable,PirateCatAI.player.steerable);
        prioritySteering = new PrioritySteering<Vector2>(steerable, 0.1f)
                .add(raycastObstacleAvoidance)
                .add(pursue);
        blendedSteering = new BlendedSteering<>(steerable)
                .add(raycastObstacleAvoidance, 1f)
                .add(pursue,1f);
    }

    public void update(float time){

        steering = prioritySteering.calculateSteering(steering);

        System.out.println(steering.linear);
        body.setLinearVelocity(steering.linear.x,steering.linear.y);
        turnAndSetPitch();

        instance.transform.setFromEulerAnglesRad(body.getAngle() - MathUtils.PI/2,pitch, PirateCatAI.getAngleFromBodyOnWave(body));
        instance.transform.setTranslation(body.getPosition().x,
                100f*(MathUtils.cos(-body.getPosition().y/100 * 3.0f * 3.1415f + time) * 0.05f *
                        MathUtils.sin(body.getPosition().x/100 * 3.0f * 3.1415f + time))-3f, body.getPosition().y);
    }

    private void turnAndSetPitch(){
        Vector2 direction;
        float angle;
        float totalRotation;

        direction = steering.linear;
        direction.nor();

        angle = steerable.vectorToAngle(direction);

        totalRotation = body.getAngle() + angle;
        if (totalRotation < -MathUtils.PI) totalRotation += 2*MathUtils.PI;
        if (totalRotation > MathUtils.PI) totalRotation -= 2*MathUtils.PI;

        if (totalRotation>0) body.setAngularVelocity(-0.8f);
        if (totalRotation<0) body.setAngularVelocity(0.8f);


        if (body.getAngularVelocity() < 0)
            if (pitch < 0.3f) pitch += 0.01f;

        if (body.getAngularVelocity() > 0)
            if (pitch > -0.3f) pitch -= 0.01f;

        if (body.getAngularVelocity() == 0) {
            if (pitch < 0f) pitch += 0.01;
            if (pitch > 0f) pitch -= 0.01f;
        }
    }
}


