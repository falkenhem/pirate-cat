package com.piratecatai.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;

public class GameAssetManager extends AssetManager {
    private final AssetManager assetManager = new AssetManager();
    private ParticleSystem particleSystem;
    //private ParticleEffect currentEffects;

    public void loadParticleEffects(Camera cam){
        particleSystem = new ParticleSystem();
        PointSpriteParticleBatch pointSpriteBatch = new PointSpriteParticleBatch();
        pointSpriteBatch.setCamera(PirateCatAI.getCam());
        BillboardParticleBatch billboardParticleBatch = new BillboardParticleBatch();
        billboardParticleBatch.setCamera(cam);

        particleSystem.add(pointSpriteBatch);
        particleSystem.add(billboardParticleBatch);
        ParticleEffectLoader loader = new ParticleEffectLoader(new InternalFileHandleResolver());
        ParticleEffectLoader.ParticleEffectLoadParameter loadParam =
                new ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.getBatches());
        assetManager.setLoader(ParticleEffect.class, loader);
        assetManager.setLoader(ParticleEffect.class, loader);
        assetManager.load("betterExplosion", ParticleEffect.class, loadParam);

        //Change for loading bar later
        assetManager.finishLoading();
        /*currentEffects=assetManager.get("betterExplosion",ParticleEffect.class).copy();
        currentEffects.init();
        particleSystem.add(currentEffects);*/
    }

    public ParticleSystem getParticleSystem(){
        return particleSystem;
    }

    public ParticleEffect getEffectByName(String name){
        ParticleEffect effect = assetManager.get(name, ParticleEffect.class).copy();
        return effect;
    }

    //public ParticleEffect getCurrentEffects() {return currentEffects;}

}
