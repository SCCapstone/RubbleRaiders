package com.badlogic.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.game.BladeAndTomes;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 800;
		config.width = 800;
		config.title = "Blades & Tomes";
		config.resizable = false;
		config.forceExit = false;
		new LwjglApplication(new BladeAndTomes(), config);
	}
}
