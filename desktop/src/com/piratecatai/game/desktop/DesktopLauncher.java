package com.piratecatai.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.piratecatai.game.PirateCatAI;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Title";
		//config.useGL30 = true;
		config.height = 720;
		config.width = 1280;
		new LwjglApplication(new PirateCatAI(), config);
	}
}
