package com.piratecatai.game;

import com.badlogic.gdx.math.Vector2;

public class ShipAccessory {
    private Ship owner;
    private Vector2 positionRelativeShip;
    private Vector2 pos;

    public ShipAccessory(Ship owner, Vector2 positionRelativeShip) {
        this.owner = owner;
        this.positionRelativeShip = positionRelativeShip;
    }

    public void update(){
        updatePos();
    }

    public Vector2 getPosition(){
        return pos;
    }


    public void updatePos() {
        Vector2 pos = owner.getWorldPointFromLocalBodyVector(positionRelativeShip, owner.getBody());
        this.pos = pos;
    }

}
