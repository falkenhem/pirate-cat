package com.piratecatai.game.pathfinding;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.math.Vector2;

public class PixMapOfMap {
    private Pixmap pixmap;

    public PixMapOfMap(){
        pixmap = new Pixmap(1200,1200, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,1,1,1);
        pixmap.fill();
    }

    public void drawCircularObject(Vector2 pos, float radius){
        int x = (int)pos.x;
        int y = (int)pos.y;
        pixmap.setColor(0,0,0,1);
        pixmap.fillCircle(x,y,(int)radius);
    }

    public Pixmap getMap(){
        /*FileHandle fileHandle = new FileHandle("debugExportMap.png");
        PixmapIO.writePNG(fileHandle, pixmap);*/
        return pixmap;


    }

}
