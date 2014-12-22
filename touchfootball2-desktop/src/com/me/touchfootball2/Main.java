package com.me.touchfootball2;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "touchfootball2";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 800;

		new LwjglApplication(new TouchFootball2(), cfg);
	}
}
