package com.me.touchfootball2.gui;

import com.me.touchfootball2.TouchFootball2;

public class Screens implements ScreenLoadable {

	private TouchFootball2 game;
	
	public MainMenuScreen mainMenuScreen;
	public GamePlayScreen gamePlayScreen;
	public ArcadeGameScreen arcadeGameScreen;
	// public SettingsScreen settingsScreen;
	// public PlayBookScreen playBookScreen;
	// public HelpScreen helpScreen;
	
	public Screens(TouchFootball2 g)
	{
		this.game = g;
		this.init();
	}
	
	@Override
	public void init ()
	{
		this.mainMenuScreen = new MainMenuScreen(game);
		this.gamePlayScreen = new GamePlayScreen(game);
		this.arcadeGameScreen = new ArcadeGameScreen(game);
		// this.settingsScreen = new SettingsScreen(game);
		// this.playBookScreen = new PlayBookScreen(game);
		// this.helpScreen = new HelpScreen(game);
	}

	@Override
	public void load () {
		// TODO Auto-generated method stub
		this.mainMenuScreen.load();
		this.gamePlayScreen.load();
	}
}
