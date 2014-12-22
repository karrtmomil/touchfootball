package com.me.touchfootball2.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class Assets {
	
	public static TextureRegion offensive_player ;
	public static TextureRegion defensive_player ;
	public static TextureRegion player_selected  ;
	public static TextureRegion player_colliding ;
	public static TextureRegion field            ;
	public static TextureRegion white_line       ;
	public static TextureRegion fieldgoal        ;
	public static TextureRegion football         ;
	public static TextureRegion cointoss         ;
	public static TextureRegion quarter_heads    ;
	public static TextureRegion quarter_tails    ;
	public static TextureRegion top_ui_background;
	public static TextureRegion top_ui_offense   ;
	public static TextureRegion top_ui_defense   ;
	public static TextureRegion playbook_item_bg ;
	
	//public static TextureRegion wheel;
	//public static TextureRegion nub;
	
	private static Texture loadTexture (String file) {
		Texture ret = new Texture(Gdx.files.internal(file));
		ret.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return ret;
	}
	
	private static TextureRegion loadTextureRegion (String file) {
		Texture tex = loadTexture(file);
		return new TextureRegion(tex);
	}
	
	private static TextureRegion loadTextureRegion (String file, int width, int height) {
		Texture tex = loadTexture(file);
		return new TextureRegion(tex, width, height);
	}
	
	private static TextureRegion loadTextureRegion (String file, int x, int y, int width, int height) {
		Texture tex = loadTexture(file);
		return new TextureRegion(tex, x, y, width, height);
	}

	public static void load () {
		offensive_player  = loadTextureRegion("gameplay/players/blue.png", 32, 32);
		defensive_player  = loadTextureRegion("gameplay/players/red.png", 32, 32);
		player_selected   = loadTextureRegion("gameplay/players/selected.png", 32, 32);
		player_colliding  = loadTextureRegion("gameplay/players/colliding.png", 32, 32);
		field             = loadTextureRegion("field.png", 800, 1768);
		white_line        = loadTextureRegion("line-white.jpg", 16, 16);
		fieldgoal         = loadTextureRegion("gameplay/players/fieldgoal.png", 289, 39);
		football          = loadTextureRegion("gameplay/players/football.png", 528, 256);
		cointoss          = loadTextureRegion("gameplay/notifications/cointoss.png");
		quarter_heads     = loadTextureRegion("gameplay/players/coin.png", 0, 0, 285, 285);
		quarter_tails     = loadTextureRegion("gameplay/players/coin.png", 285, 0, 285, 285);
		top_ui_background = loadTextureRegion("gameplay/ui/top-bg2.png", 1024, 118);
		top_ui_offense    = loadTextureRegion("gameplay/ui/offense2.png", 128, 128);
		top_ui_defense    = loadTextureRegion("gameplay/ui/offense2.png", 128, 128);
		playbook_item_bg  = loadTextureRegion("gameplay/ui/playbook-item-bg.png", 480, 64);
		// wheel          = new TextureRegion(loadTexture("assets/gameplay/ui/wheel.png"), 512, 512);
		// nub            = new TextureRegion(loadTexture("assets/gameplay/ui/nub.png"), 64, 64);
	}

	public static enum NotifyType {
		TOUCHDOWN,
		FIELDGOAL,
		GAMEOVER
	}
	
	private static float time = 0;
	
	public static Actor notify(NotifyType n, Stage stage) {
		time = 0;
		Group group = new Group();
		group.setX(Gdx.graphics.getWidth()/2-Gdx.graphics.getWidth()/2.9f);
		group.setY(Gdx.graphics.getHeight()/2);
		String image_asset = "";
		if (n == NotifyType.TOUCHDOWN) {
			image_asset = "gameplay/notifications/touchdown.png";
		}
		else if (n == NotifyType.FIELDGOAL) {
			image_asset = "gameplay/notifications/fieldgoal.png";
		}
		else if(n == NotifyType.GAMEOVER) {
			image_asset = "gameplay/notifications/gameover.png";
		}
		Image image = new Image(loadTexture(image_asset));
		group.addActor(image);
		/*
		FadeOut dis = FadeOut.$(0);
		FadeOut fadeOut = FadeOut.$(1.0f);
		FadeIn fadeIn = FadeIn.$(1.0f);
		Delay delay = Delay.$(fadeOut, 3f);
		Sequence sequence = Sequence.$(dis, fadeIn, delay);
		group.action(sequence);*/
		stage.addActor(group);
		return group;
	}
	
	public static boolean notify(Actor actor) {
		actor.act(Gdx.graphics.getDeltaTime());
		time += Gdx.graphics.getDeltaTime();
		return time >= 2.5f;
	}
}