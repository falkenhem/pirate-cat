package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class DynamicParticleEffect {
    private ParticleEffect effect;
    private DynamicGameObject owner;
    private Vector2 positionRelativeShip;
    private Vector2 pos;

    public DynamicParticleEffect(ParticleEffect effect, DynamicGameObject owner, Vector2 positionRelativeShip) {
        this.effect = effect;
        this.owner = owner;
        this.positionRelativeShip = positionRelativeShip;
    }

    public void update(){
        Matrix4 transform = new Matrix4();
        Vector2 pos = owner.getWorldPointFromLocalBodyVector(positionRelativeShip, owner.getBody());
        //Vector2 deltaPos = owner.getWorldVelocity();
        Vector3 translation = new Vector3(pos.x,owner.getPos().y,pos.y);
        transform.idt();
        transform.translate(translation);
        effect.setTransform(transform);
        effect.scale(new Vector3(2,2,2));
        //effect.rotate(new Vector3(1,0,0),90);
        //effect.translate(new Vector3(deltaPos.x, 0, deltaPos.y));
    }

    public Vector2 getPos() {
        return pos;
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    public DynamicGameObject getOwner() {
        return owner;
    }

    public Vector2 getPositionRelativeShip() {
        return positionRelativeShip;
    }
}
