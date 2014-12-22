package com.me.touchfootball2;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
	@Override
	   public void onCreate (Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
	      config.useAccelerometer = false;
	      config.useCompass = false;
	      config.useGL20 = false;
	      initialize(new TouchFootball2(), config);
	   }
/*
 * old override 	(non-Javadoc)
 * @see android.app.Activity#onCreate(android.os.Bundle)
 *
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        initialize(new TouchFootball2old(), cfg);
    }
    */
}