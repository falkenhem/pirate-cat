package com.piratecatai.game;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class SteerableEntity implements Steerable {
    Body body;
    float maxLinearSpeed;

    public SteerableEntity(Body body){
        this.body = body;
        this.maxLinearSpeed = ((GameObject)body.getUserData()).getSpeed();
    }

    @Override
    public Vector getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return 10;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {

    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.0001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {

    }

    @Override
    public float getMaxLinearAcceleration() {
        return 10f;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {

    }

    @Override
    public float getMaxAngularSpeed() {
        return 7f;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {

    }

    @Override
    public float getMaxAngularAcceleration() {
        return 2f;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {

    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(getPosition(), orientation);
    }

    @Override
    public float vectorToAngle(Vector vector) {
        Vector2 test;
        test = (Vector2) vector;
        //System.out.println((float)Math.atan2(-test.x, test.y));
        return (float)Math.atan2(-test.x, test.y);
    }

    @Override
    public Vector angleToVector(Vector outVector, float angle) {
        Vector2 test;
        test = (Vector2) outVector;
        test.x = -(float)Math.sin(angle);
        test.y = (float)Math.cos(angle);
        return test;
    }

    @Override
    public Location newLocation() {
        return null;
    }
}
