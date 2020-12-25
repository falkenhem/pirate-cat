package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class EffectsManager {
    private static EffectsManager instance;
    private HashMap<ParticleEffect, DynamicGameObject> dynamicEffectOwners = new HashMap();
    private Array<ParticleEffect> stationaryEffects = new Array<ParticleEffect>();
    private Array<DynamicParticleEffect> dynamicEffects = new Array<>();
    private Array<ParticleEffect> effects = new Array<>();
    private final float effectsScale = 5f;
    ParticleSystem particleSystem;

    public static EffectsManager getInstance(){
        if (instance == null){
            instance = new EffectsManager(GameAssetManager.getInstance().getParticleSystem());
        }
        return instance;
    }

    public EffectsManager(ParticleSystem particleSystem){
        this.particleSystem = particleSystem;
    }

    private void addParticleEffect(ParticleEffect particleEffect, Vector3 pos, Vector3 scale){
        particleEffect.init();
        particleEffect.translate(pos);
        particleEffect.scale(scale);
        particleSystem.add(particleEffect);
        effects.add(particleEffect);
    }

    public void addStationaryParticleEffect(ParticleEffect particleEffect, Vector3 pos, Vector3 scale){
        addParticleEffect(particleEffect, pos, scale);
        stationaryEffects.add(particleEffect);
    }

    public void addDynamicParticleEffect(ParticleEffect particleEffect, Vector3 pos, Vector3 scale,
                                         DynamicGameObject owner, Vector2 relativePositionToOwner){
        addParticleEffect(particleEffect, pos, scale);
        DynamicParticleEffect dynamicParticleEffect = new DynamicParticleEffect(particleEffect,
                owner,relativePositionToOwner);
        dynamicEffects.add(dynamicParticleEffect);
    }

    public void update(){
        for (DynamicParticleEffect effect : dynamicEffects){
            effect.update();
        }
    }

    public void stopEmissionStationaryEffects(){
        for (ParticleEffect effect : effects){
            for (ParticleController controller : effect.getControllers()) {
                Emitter emitter = controller.emitter;
                if (emitter instanceof RegularEmitter) {
                    RegularEmitter reg = (RegularEmitter) emitter;
                    reg.setEmissionMode(RegularEmitter.EmissionMode.EnabledUntilCycleEnd);
                }
            }
        }
    }



}
