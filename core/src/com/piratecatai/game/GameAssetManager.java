package com.piratecatai.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.piratecatai.game.pathfinding.PixMapOfMap;

public class GameAssetManager extends AssetManager {
    private ParticleSystem particleSystem;
    private Array<Vector2> islandLocations;
    private Array<GameObject> gameObjects = new Array<>();
    ModelLoader modelLoader = new G3dModelLoader(new JsonReader());

    public void loadModels(World world, PixMapOfMap pixMapOfMap){
        islandLocations = new Array<>();
        islandLocations.add(new Vector2(100,200),new Vector2(800,800), new Vector2(550,300), new Vector2(250,840));
        Model model = modelLoader.loadModel(Gdx.files.internal("island.g3dj"));

        for (Vector2 pos : islandLocations){
            ModelInstance islandInstance = new ModelInstance(model);
            islandInstance.transform.setTranslation(pos.x,0,pos.y);
            Island island = new Island(islandInstance, world);
            gameObjects.add(island);
            pixMapOfMap.drawCircularObject(pos,island.getDimensions().x/2);
        }
    }

    public Array<GameObject> getGameObjects(){
        return gameObjects;
    }

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
        setLoader(ParticleEffect.class, loader);
        setLoader(ParticleEffect.class, loader);
        load("blastWave", ParticleEffect.class, loadParam);
        load("betterExplosion", ParticleEffect.class, loadParam);
        //Change for loading bar later
        finishLoading();

    }

    public ParticleSystem getParticleSystem(){
        return particleSystem;
    }

    public ParticleEffect getEffectByName(String name){
        ParticleEffect effect = get(name, ParticleEffect.class).copy();
        return effect;
    }

}
