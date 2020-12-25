package com.piratecatai.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.utils.Array;

public class Player extends Ship implements InputProcessor {
    boolean moving;
    boolean turnRight;
    boolean turnLeft;
    private Array<Cannon> cannons = new Array<>();
    //private ChargingArrow arrow;
    private Array<ChargingArrow> arrows = new Array<>();
    float gunsCD;
    int numCannons;


    public Player(ModelInstance instance, World world, float health,
                  EffectsManager effectsManager, GameAssetManager gameAssetManager){
        super(instance, world, "round", BodyDef.BodyType.DynamicBody, health, effectsManager, gameAssetManager);
        ((InputMultiplexer)Gdx.input.getInputProcessor()).addProcessor(this);
        pitch = 0;
        gunsCD = 0f;
        numCannons = 1;
        speed = 5;

        cannonsLocalPosition = new Vector2[2];
        cannonsLocalPosition[LEFT] = new Vector2(0,-2f);
        cannonsLocalPosition[RIGHT] = new Vector2(0,2f);

        cannons.add(new Cannon(this, new Vector2(0,-2), LEFT));
        cannons.add(new Cannon(this, new Vector2(0,2), RIGHT));

        arrows.add(new ChargingArrow(this, new Vector2(0, -1), GameAssetManager.getInstance().getModelByName(), cannons.get(0), new ArrowShader()));
        arrows.add(new ChargingArrow(this, new Vector2(0, 1), GameAssetManager.getInstance().getModelByName(), cannons.get(1), new ArrowShader()));
    }

    public void update(float time){
        super.update(time);

        //change to all accessories
        for (ChargingArrow arrow : arrows){
            arrow.update();
        }

        body.setAngularVelocity(0f);

        if (moving){
            body.setLinearVelocity(MathUtils.cos(body.getAngle()- MathUtils.PI/2) * speed
                    ,-MathUtils.sin(body.getAngle()- MathUtils.PI/2) * speed);
        } else body.setLinearDamping(1f);

        turnAndSetPitch();


        for (Cannon cannon : cannons){
            cannon.update();
        }

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

    public Steerable getSteerable() {
        return steerable;
    }

    public Vector2 getFuturePositionWithTime(float time){
        Vector2 futurePosition;
        futurePosition = getPos2D().add(getWorldVelocity().scl(time/ Gdx.graphics.getDeltaTime()));

        return futurePosition;
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public Array<ChargingArrow> getRenderableAccessories(){
        return arrows;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.UP) {
            moving = true;
        }
        if(keycode == Input.Keys.LEFT) {
            turnLeft = true;
        }
        if(keycode == Input.Keys.RIGHT) {
            turnRight = true;
        }
        if(keycode == Input.Keys.Q) {
            for (Cannon cannon : cannons){
                if (cannon.getSideOfShip() == LEFT) cannon.startCharging();
            }
        }
        if(keycode == Input.Keys.E) {
            for (Cannon cannon : cannons){
                if (cannon.getSideOfShip() == RIGHT) cannon.startCharging();
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.RIGHT) {
            turnRight = false;
        }

        if(keycode == Input.Keys.LEFT) {
            turnLeft = false;
        }

        if(keycode == Input.Keys.UP) {
            moving = false;
        }

        if(keycode == Input.Keys.Q) {
            for (Cannon cannon : cannons){
                if (cannon.getSideOfShip() == LEFT) cannon.stopChargingAndShoot();
            }
        }
        if(keycode == Input.Keys.E) {
            for (Cannon cannon : cannons){
                if (cannon.getSideOfShip() == RIGHT) cannon.stopChargingAndShoot();
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
