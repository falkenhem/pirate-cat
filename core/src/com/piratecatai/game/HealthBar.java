package com.piratecatai.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;

public class HealthBar {
    private Decal decal;

    public HealthBar(Vector3 pos){
        Pixmap pixmap = new Pixmap(12, 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(0,1,0.3f,0);
        pixmap.fill();
        Texture pixMapTex = new Texture(pixmap);
        pixmap.dispose();
        TextureRegion textureRegion = new TextureRegion(pixMapTex);
        decal = Decal.newDecal(textureRegion, false);
        decal.setPosition(pos);
    }

    public Decal getDecal() {
        return decal;
    }

    public void update(float percentHealth, Vector3 pos){
        decal.setScaleX(percentHealth);
        decal.setPosition(new Vector3(pos.x, 20f, pos.z));
    }
}
