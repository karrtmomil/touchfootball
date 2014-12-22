package com.me.touchfootball2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.me.touchfootball2.TouchFootball2;


public class MainMenuScreen extends AbstractScreen implements ScreenLoadable {
	private Texture splashTexture;
	
	private Texture playButtonTexture;
	private Texture arcadeButtonTexture;
	// private Texture settingsButtonTexture;
	// private Texture playbookButtonTexture;
	// private Texture helpButtonTexture;
	private Texture quitButtonTexture;
	
	private TextureRegion splashTextureRegion;
	
	private TextureRegion playButtonTextureRegion;
	private TextureRegion arcadeButtonTextureRegion;
	// private TextureRegion settingsButtonTextureRegion;
	// private TextureRegion playbookButtonTextureRegion;
	// private TextureRegion helpButtonTextureRegion;
	private TextureRegion quitButtonTextureRegion;
	private Skin skins;
	
	private Table menu;
	
	private Button play;
	private Button arcade;
	// private Button settings;
	// private Button playbook;
	// private Button help;
	private Button quit;
	
	public MainMenuScreen(TouchFootball2 game) {
		super(game);
		this.init();
	}
	
	public void init() {
		// Initialize Textures
		splashTexture = new Texture(Gdx.files.internal("main-menu.png"));
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		splashTextureRegion = new TextureRegion(splashTexture, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

		playButtonTexture = new Texture(Gdx.files.internal("buttons/button-play.png"));
		arcadeButtonTexture = new Texture(Gdx.files.internal("buttons/button-arcade.png"));
		// settingsButtonTexture = new Texture(Gdx.files.internal("assets/buttons/button-settings.png"));
		// playbookButtonTexture = new Texture(Gdx.files.internal("assets/buttons/button-playbook.png"));
		// helpButtonTexture = new Texture(Gdx.files.internal("assets/buttons/button-help.png"));
		quitButtonTexture = new Texture(Gdx.files.internal("buttons/button-quit.png"));
		
		playButtonTextureRegion = new TextureRegion(playButtonTexture, 0, 0, 215, 95);
		arcadeButtonTextureRegion = new TextureRegion(arcadeButtonTexture, 0, 0, 215, 95);
		// settingsButtonTextureRegion = new TextureRegion(settingsButtonTexture, 0, 0, 215, 95);
		// playbookButtonTextureRegion = new TextureRegion(playbookButtonTexture, 0, 0, 215, 95);
		// helpButtonTextureRegion = new TextureRegion(helpButtonTexture, 0, 0, 215, 95);
		quitButtonTextureRegion = new TextureRegion(quitButtonTexture, 0, 0, 215, 95);
		skins = new Skin();
		skins.add("play",playButtonTextureRegion);
		skins.add("arcade", arcadeButtonTextureRegion);
		skins.add("quit", quitButtonTextureRegion);
		skins.add("background", splashTextureRegion);
		
		// Initialize Buttons
		play = new Button(skins.getDrawable("play"));
		arcade = new Button(skins.getDrawable("arcade"));
		// settings = new Button(settingsButtonTextureRegion);
		// playbook = new Button(playbookButtonTextureRegion);
		// help = new Button(helpButtonTextureRegion);
		quit = new Button(skins.getDrawable("quit"));
	}
	
	public void load() {
		// Load Button Functions
		// Can only be done after all screens have been initialized
		// i.e. MainMenu, nor any other Screen, can call load() on itself
		
		play.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(game.screens.gamePlayScreen);
				return false;
			}
			
		});
		arcade.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(game.screens.arcadeGameScreen);
				return false;
			}
			
		});
		// settings.setClickListener(new ButtonListener(game, game.screens.settingsScreen));
		// playbook.setClickListener(new ButtonListener(game, game.screens.playbookScreen));
		// help.setClickListener(new ButtonListener(game, game.screens.helpScreen));

		quit.addListener(new EventListener() {
			

			@Override
			public boolean handle(Event tap) {
				//Gdx.app.exit();
				return false;
			}
		});

		// Initialize Main Menu
		menu = new Table();
		menu.setWidth(stage.getWidth());
		menu.setHeight(stage.getHeight());
		menu.setX(0);
		menu.setY(0);
		menu.setBackground(skins.getDrawable("background"));
		menu.padTop(0);
		menu.add(play);
		// menu.row();
		// menu.add(settings);
		menu.row();
		menu.add(arcade);
		// menu.add(playbook);
		// menu.row();
		// menu.add(help);
		menu.row();
		menu.add(quit);
	}
	
	@Override
	public void show() {
		super.show();
		stage.addActor(menu);
	}

	@Override
	public void render(float delta) {
		camera.update();
		camera.apply(gl);
		super.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
		splashTexture.dispose();
	}
}
