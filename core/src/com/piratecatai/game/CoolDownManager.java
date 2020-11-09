package com.piratecatai.game;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;

public class CoolDownManager<K,V> extends HashMap<String,Float> {

    public void update() {
        for (Entry<String, Float> entry : this.entrySet()) {
            float coolDownLength = entry.getValue();
            if (coolDownLength > 0) coolDownLength = coolDownLength - Gdx.graphics.getDeltaTime();
            entry.setValue(coolDownLength);
        }
    }

    public boolean done(String key) {
        if (this.get(key) <= 0) return true;
        else return false;
    }

    public float getTimeRemaining(String key){
        return this.get(key);
    }
}
