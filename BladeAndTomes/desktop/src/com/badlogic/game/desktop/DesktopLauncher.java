package com.badlogic.game.desktop;


import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowSizeLimits(1280,720,1920,1080);
		config.setMaximized(true);
		new Lwjgl3Application(new BladeAndTomes(), config);
		// Test Merge
		// add this following bit so that the width and height don't go past a minimum requirement.
	}
}