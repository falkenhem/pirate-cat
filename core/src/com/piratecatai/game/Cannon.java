package com.piratecatai.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Cannon {
    private Ship owner;
    private Vector2 positionRelativeShip;
    private int sideOfShip;
    private float charge;
    private boolean charging;
    private boolean shooting;
    protected final int LEFT = 0;
    protected final int RIGHT = 1;


    public Cannon(Ship owner, Vector2 positionRelativeShip, int sideOfShip) {
        this.owner = owner;
        this.positionRelativeShip = positionRelativeShip;
        this.sideOfShip = sideOfShip;
        charge = 0;
        charging = false;
        shooting = false;
    }

    private void resetCharge(){
        charge = 0;
    }

    public void update(){
        if (charging) charge += Gdx.graphics.getDeltaTime();
        if (shooting) {
            shoot();
            shooting = false;
        }
    }

    private float getVelocityFromCharge(){
        float velocity;

        if (charge > 2){
            velocity = 100f;
        } else velocity = 20f;

        System.out.println(velocity);

        return velocity;
    }

    private void shoot(){
        owner.shoot(sideOfShip, getVelocityFromCharge(), 4f);
        resetCharge();
    }

    public void startCharging(){
        charging = true;
    }

    public void stopChargingAndShoot(){
        charging = false;
        shooting = true;
    }

    public Ship getOwner() {
        return owner;
    }

    public Vector2 getPositionRelativeShip() {
        return positionRelativeShip;
    }

    public int getSideOfShip() {
        return sideOfShip;
    }
}