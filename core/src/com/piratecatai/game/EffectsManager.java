package com.piratecatai.game;

import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class EffectsManager {
    private HashMap<ParticleEffect, DynamicGameObject> dynamicEffects = new HashMap();
    private Array<ParticleEffect> stationaryEffects = new Array<ParticleEffect>();
    private final float effectsScale = 5f;
    ParticleSystem particleSystem;

    public EffectsManager(ParticleSystem particleSystem){
        this.particleSystem = particleSystem;
    }

    public void addStationaryParticleEffect(ParticleEffect particleEffect, Vector3 pos){
        particleEffect.init();
        particleEffect.translate(pos);
        particleEffect.scale(effectsScale,effectsScale,effectsScale);
        particleSystem.add(particleEffect);
        stationaryEffects.add(particleEffect);

    }

    public void stopEmissionStationaryEffects(){
        for (ParticleEffect effect : stationaryEffects){
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
