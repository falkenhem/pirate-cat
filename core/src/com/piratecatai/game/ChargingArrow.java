package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ChargingArrow extends ShipAccessory{
    private ModelInstance instance;
    private Ship owner;
    private boolean render;
    private Cannon cannon;
    private ArrowShader arrowShader;
    private Vector2 positionRelativeShip;

    public ChargingArrow(Ship owner, Vector2 positionRelativeShip, Model model, Cannon cannon, ArrowShader arrowShader) {
        super(owner, positionRelativeShip);
        render = false;
        this.cannon = cannon;
        this.owner = owner;
        this.arrowShader = arrowShader;
        this.arrowShader.init();
        this.positionRelativeShip = positionRelativeShip;
        instance = new ModelInstance(model);
    }

    @Override
    public void update() {
        super.update();
        instance.transform.setFromEulerAnglesRad(owner.body.getAngle() + positionRelativeShip.angleRad(),0,0);
        instance.transform.setTranslation(getPosition().x,0f,getPosition().y);
        if (cannon.isCharging()) {
            render = true;
        } else {
            render = false;
            arrowShader.reset();
        }
    }

    public ArrowShader getArrowShader() {
        return arrowShader;
    }

    public boolean shouldRender() {
        return render;
    }

    public ModelInstance getInstance() {
        return instance;
    }
}
