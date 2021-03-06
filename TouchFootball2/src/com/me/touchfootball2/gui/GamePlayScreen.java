package com.me.touchfootball2.gui;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.Sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.touchfootball2.TouchFootball2;
import com.me.touchfootball2.gameplay.Field;
import com.me.touchfootball2.gameplay.Football;
import com.me.touchfootball2.gameplay.GameClock;
import com.me.touchfootball2.gameplay.GameObject;
import com.me.touchfootball2.gameplay.Play;
import com.me.touchfootball2.gameplay.Player;
import com.me.touchfootball2.gameplay.Player.TeamColor;
import com.me.touchfootball2.gameplay.Players;
import com.me.touchfootball2.gameplay.Team;
import com.me.touchfootball2.graphics.Assets;
import com.me.touchfootball2.graphics.Assets.NotifyType;
import com.me.touchfootball2.graphics.Draw;
import com.me.touchfootball2.parsers.PlayParser;
import com.me.touchfootball2.parsers.PlaybookParser;

public class GamePlayScreen extends AbstractScreen implements ScreenLoadable {

	protected TeamColor dColor = TeamColor.RED;
	protected TeamColor oColor = TeamColor.BLUE;
	// PRIVATE MEMBER VARIABLES ///////////////////////////////////////////////
	private float los = -14 * 31;
	private float firstDown = los + 144;
	public static Football football;
	private Field field;
	public static Team offense;
	public static Team defense;
	public GameObject endzone_top;
	public GameObject endzone_bottom;
	private static Players players;

	private Vector3 _touchPoint = new Vector3(0, 0, 0);
	private float rotationSpeed = 1f;
	private CameraController controller;
	private GestureDetector gestureDetector;
	private GameClock gameClock;
	private Label time, offensiveLabel, defensiveLabel, downLabel, quarterLabel;
	private float stateTime;
	
	private int down_n;
	private int distance;
	private String down;

	private Actor touchdown;
	private Actor gameover;
	private Skin skins;

	private enum State {
		COINTOSS, COINTOSSANIMATE, PICKPLAY, WAITING, PLAYING, OVER, PAUSED, TEST, TOUCHDOWN
	}

	private State state = State.PICKPLAY;
	private boolean stateChanged = false;

	private boolean coinchoice = false;
	private Group ui, pickplay;
	private Table cointoss, plays;
	private int sleep = 2;

	private PlaybookParser playbook;
	private String myCurrentPlay;
	private String theirCurrentPlay;

	// CONSTRUCTOR ////////////////////////////////////////////////////////////
	public GamePlayScreen(TouchFootball2 game) {
		super(game);
		Assets.load();
		this.init();
	}
	
	@Override
	public void init () {
		field = new Field();
		players = new Players();
		football = new Football();
		football.neighborhood(players);
	
		_touchPoint = new Vector3();
	
		playbook = new PlaybookParser("xml/Playbook.xml");
	
		endzone_top = new GameObject(0, -field.getHeight() / 2 + 94,
				field.getWidth(), 140);
		endzone_bottom = new GameObject(0, field.getHeight() / 2 - 89,
				field.getWidth(), 140);
	
		touchdown = Assets.notify(NotifyType.TOUCHDOWN, stage);
		gameover = Assets.notify(NotifyType.GAMEOVER, stage);
		camera.zoom = 2.25f;
		
		gameClock = new GameClock(1, 10);
		
		BitmapFont font = new BitmapFont(
				Gdx.files.internal("gameplay/fonts/MonospaceTypewriter.fnt"),
				Gdx.files.internal("gameplay/fonts/MonospaceTypewriter.png"), false);
		down_n = 1;
		down = "1st and 10";
		downLabel = new Label(down, new LabelStyle(font, Color.WHITE));
		quarterLabel = new Label(gameClock.printQuarter()+"", new LabelStyle(font, Color.WHITE));
	}

	@Override
	public void load () {
		// TODO Auto-generated method stub
		
	}

	// MEMBER METHODS /////////////////////////////////////////////////////////
	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.N)) {
			football.possessed(null);
			football.seek(offense.getPlayers(0).position());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.M)) {
			football.possessed(null);
			football.seek(defense.getPlayers(0).position());
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			if (camera.zoom <= 4)
				camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			if (camera.zoom > .0001)
				camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			// if (camera.position.x > 0)
			camera.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			// if (camera.position.x < Gdx.graphics.getWidth())
			camera.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			// if (camera.position.y > 0)
			camera.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			// if (camera.position.y < Gdx.graphics.getHeight())
			camera.translate(0, 3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.rotate(-rotationSpeed, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			camera.rotate(rotationSpeed, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F2))
			sleep += 1;
		if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
			if (sleep > 2)
				sleep -= 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
			game.setScreen(game.screens.mainMenuScreen);
		}
	}

	private void setState(State state) {
		if (this.state != state) {
			this.state = state;
			stateChanged = true;
		} else
			stateChanged = false;
	}

	// OVERRIDDEN METHODS /////////////////////////////////////////////////////
	@Override
	public void show() {
		super.show();

		// players.addPlayers(offense.getPlayers());
		offense = new Team();
		defense = new Team();

		// COIN TOSSER
		// ////////////////////////////////////////////////////////////////////
		
		cointoss = new Table();
		cointoss.setWidth(405);
		cointoss.setHeight(405);
		cointoss.pad(0);
		cointoss.setX(80);
		cointoss.setY(175);
		//NinePatch cointoss_bg = new NinePatch(Assets.cointoss);
		//cointoss.setBackground(cointoss_bg);
		skins.add("cointoss", Assets.cointoss);
		cointoss.setBackground(skins.getDrawable("cointoss"));

		Table heads = new Table();
		//NinePatch quarter_heads_bg = new NinePatch(Assets.quarter_heads);
		heads.setBackground("quarter_heads");
		heads.setWidth(100);
		heads.setHeight(100);
		heads.setX(50);
		heads.setY(150);
		heads.addListener(new ClickListener() {
			public void click(Actor actor, float x, float y) {
				coinchoice = true;
				setState(State.PICKPLAY);
			}
		});
		cointoss.addActor(heads);

		Table tails = new Table();
		//NinePatch quarter_tails_bg = new NinePatch(Assets.quarter_tails);
		tails.setBackground("quarter_tails");
		tails.setWidth(100);
		tails.setHeight(100);
		tails.setX(175);
		tails.setY(150);
		tails.addListener(new ClickListener() {
			public void click(Actor actor, float x, float y) {
				coinchoice = false;
				setState(State.PICKPLAY);
			}
		});
		cointoss.addActor(tails);

		/*FadeOut dis = FadeOut.$(0);
		FadeOut fadeOut = FadeOut.$(1.0f);
		FadeIn fadeIn = FadeIn.$(1.0f);
		Delay delay = Delay.$(fadeOut, 3f);
		Sequence sequence = Sequence.$(dis, fadeIn);
		cointoss.action(sequence);*/
		
		// TOP UI
		// ////////////////////////////////////////////////////////////////////

		Table top = new Table();
		top.setWidth(Gdx.graphics.getWidth());
		top.setHeight(150);
		top.pad(0);
		top.setY(stage.getHeight() - top.getHeight() + 4);
		//NinePatch top_bg = new NinePatch(Assets.top_ui_background);
		top.setBackground("top_ui_background");

		Table top_middle = new Table();
		top_middle.setWidth(top.getWidth() - 300);
		top_middle.setHeight(150);
		top_middle.setX(150);
		top_middle.setY(stage.getHeight() - 110);

		BitmapFont font = new BitmapFont(
				Gdx.files.internal("assets/gameplay/fonts/kaiti.fnt"),
				Gdx.files.internal("assets/gameplay/fonts/kaiti.png"), false);
		time = new Label(gameClock.print(), new LabelStyle(font, Color.WHITE));
		time.setAlignment(1);

		top_middle.row().expand().fill();
		top_middle.add(time).expand().fill().colspan(2);
		
		top_middle.row();
		top_middle.add(downLabel).colspan(1);
		top_middle.add(quarterLabel).colspan(1);

		Table top_left = new Table();
		top_left.setWidth(150);
		top_left.setHeight(top.getHeight() + 15);
		top_left.setX(top.getX());
		top_left.setY(stage.getHeight() - top_left.getHeight() + 3);
		NinePatch top_left_bg = new NinePatch(Assets.top_ui_offense);
		//top_left.setBackground(top_left_bg);

		BitmapFont scoreFont = new BitmapFont(
				Gdx.files.internal("assets/gameplay/fonts/stencil.fnt"),
				Gdx.files.internal("assets/gameplay/fonts/stencil.png"), false);

		Table top_left_content = new Table();
		top_left_content.center();
		top_left_content.setWidth(150);
		top_left_content.setHeight(top.getHeight());
		top_left_content.setX(top.getX() - 5);
		top_left_content.setY(stage.getHeight() - top_left.getHeight() + 25);

		offensiveLabel = new Label(String.format("%02d", offense.score()) + "",
				new LabelStyle(scoreFont, new Color(255f, 170f, 0f, .8f)));
		top_left_content.add(offensiveLabel);

		Table top_right = new Table();
		top_right.setWidth(150);
		top_right.setHeight(top.getHeight() + 15);
		top_right.setX(Gdx.graphics.getWidth() - top_right.getWidth() + 9);
		top_right.setY(stage.getHeight() - top_right.getHeight() + 1);
		NinePatch top_right_bg = new NinePatch(Assets.top_ui_defense);
		//top_right.setBackground(top_right_bg);

		Table top_right_content = new Table();
		top_right_content.center();
		top_right_content.setWidth(150);
		top_right_content.setHeight(top.getHeight());
		top_right_content.setX(Gdx.graphics.getWidth() - 145);
		top_right_content.setY(stage.getHeight() - top_right.getHeight() + 25);

		defensiveLabel = new Label(String.format("%02d", defense.score()) + "",
				new LabelStyle(scoreFont, new Color(255f, 255f, 255f, .8f)));
		top_right_content.add(defensiveLabel);

		ui = new Group();
		ui.addActor(top);
		ui.addActor(top_left);
		ui.addActor(top_left_content);
		ui.addActor(top_middle);
		ui.addActor(top_right);
		ui.addActor(top_right_content);

		// PLAY PICKER
		// ////////////////////////////////////////////////////////////////////

		plays = new Table();
		plays.setWidth(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 9);
		plays.setHeight(Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 4);
		plays.setX(Gdx.graphics.getWidth() / 18);
		plays.setY(Gdx.graphics.getHeight() / 8);
		TextureRegion background = new TextureRegion(new Texture(
				Gdx.files.internal("assets/gameplay/ui/playselect_bg.png")),
				450, 749);
		//plays.setBackground(new NinePatch(background));
		plays.setClip(true);

		// TODO fix this code below
		/*final Button playbook_button = new Button(
				new TextureRegion(
						new Texture(
								Gdx.files
										.internal("assets/buttons/playbook_button_inactive.png")),
						0, 0, 213, 74),
				new TextureRegion(
						new Texture(
								Gdx.files
										.internal("assets/buttons/playbook_button_active.png")),
						0, 0, 213, 74),
				new TextureRegion(
						new Texture(
								Gdx.files
										.internal("assets/buttons/playbook_button_active.png")),
						0, 0, 213, 74));
		playbook_button.x = 2;
		playbook_button.y = plays.height - playbook_button.height - 1;
		playbook_button.setChecked(true);

		final Button menu_button = new Button(new TextureRegion(new Texture(
				Gdx.files.internal("assets/buttons/menu_button_inactive.png")),
				0, 0, 211, 74), new TextureRegion(new Texture(
				Gdx.files.internal("assets/buttons/menu_button_active.png")),
				0, 0, 211, 74), new TextureRegion(new Texture(
				Gdx.files.internal("assets/buttons/menu_button_active.png")),
				0, 0, 211, 74));
		menu_button.setY(plays.getHeight() - menu_button.getHeight() - 1);
		menu_button.setX(plays.getWidth() - menu_button.getWidth());

		playbook_button.addListener(new ClickListener() {
			public void click(Actor actor, float x, float y) {
				playbook_button.setChecked(true);
				menu_button.setChecked(false);
			}
		});
		menu_button.addListener(new ClickListener() {
			public void click(Actor actor, float x, float y) {
				playbook_button.setChecked(false);
				menu_button.setChecked(true);
			}
		});

		plays.addActor(playbook_button);
		plays.addActor(menu_button);

		Table plays_list = new Table();

		final FlickScrollPane plays_select = new FlickScrollPane(plays_list);
		plays_select.width = plays.getWidth();
		plays_select.height = plays.getHeight() - 75;

		final ArrayList<Play> listOfPlays = playbook.getPlays("offense");
		for (int i = 0; i < listOfPlays.size(); i++) {
			final String play = listOfPlays.get(i).getPlayLocation();
			font = new BitmapFont(
					Gdx.files.internal("assets/gameplay/fonts/kaiti-20.fnt"),
					Gdx.files.internal("assets/gameplay/fonts/kaiti-20.png"),
					false);
			NinePatch bg = new NinePatch(
					new TextureRegion(Assets.playbook_item_bg, 0, 0,
							(int) plays_select.width, 64));

			TextButton b = new TextButton(listOfPlays.get(i).getPlayName(),
					new TextButton.TextButtonStyle());

			b.addListener(new ClickListener() {
				public void click(Actor actor, float x, float y) 
				{
					players.clear();
					String location = playbook.getPlayLocation("ManDefense");
					ArrayList<Player> dplayers = null;
					try {
						PlayParser playParse = new PlayParser(location, los, -1);
						dplayers = playParse.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < 11; i++) {
						dplayers.get(i).color(dColor);
						dplayers.get(i).setTeam(defense);
						dplayers.get(i).neighborhood(players);
					}
					defense = new Team(dplayers);

					ArrayList<Player> oplayers = null;
					try {
						PlayParser playParse = new PlayParser(play, los, 1);
						oplayers = playParse.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < 11; i++) {
						oplayers.get(i).neighborhood(players);
						dplayers.get(i).setTeam(offense);
						oplayers.get(i).color(oColor);
					}
					offense = new Team(oplayers);

					// clear players
					players.clear();

					// add new players
					players.addPlayers(offense.getPlayers());
					players.addPlayers(defense.getPlayers());
					
					// set ball to possessed by center at beginning of play
					if( football.possessed() != null ) {
						football.clearPossessed();
					}
					Player has = offense.getPlayerByPosition("center");
					football.possessed(has);
					football.position(has.position());

					setState(State.WAITING);
				}
			});
			plays_list.add(b);
			plays_list.row();
		}

		plays.addActor(plays_select);
*/
		stateTime = 0f;
		stateChanged = true;

		controller = new CameraController(camera);
		gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, controller);
	}

	@Override
	public void render(float delta) {
		camera.update();
		camera.apply(gl);

		batch.begin();
		batch.enableBlending();
		batch.setProjectionMatrix(camera.combined);
		batch.draw(field.skin.getTexture(), -field.getWidth() * .5f,
				-field.getHeight() * .5f, field.getWidth(), field.getHeight(),
				0, 0, 800, 1768, false, false);
		batch.end();

		// DRAW DYNAMIC STUFF HERE
		/*if (state == State.COINTOSS) {
			if (stateChanged) {
				stage.clear();
				stage.addActor(cointoss);
				Gdx.input.setInputProcessor(stage);
				stateChanged = false;
			}
			super.render(delta);
		} else if (state == State.TEST) {
			if (stateChanged) {
				Gdx.input.setInputProcessor(stage);
				stage.clear();
				stage.addActor(plays);
				stateChanged = false;
			}
			stage.draw();
			super.render(delta);
		} else*/ 
		if( state == State.OVER) {
			stage.clear();
			stage.addActor(gameover);
			Assets.notify(gameover);
			super.render(delta);
		}
		else if (state == State.PLAYING || state == State.PICKPLAY
				|| state == State.WAITING || state == State.TOUCHDOWN) {

			if (stateChanged) {
				stage.clear();
				stage.addActor(ui);

				if (state == State.PICKPLAY) {
					stage.addActor(plays);
					Gdx.input.setInputProcessor(stage);
				} else if (state == State.PLAYING) {
					Gdx.input.setInputProcessor(gestureDetector);
				} else if (state == State.TOUCHDOWN) {
					stage.addActor(touchdown);
				}
				stateChanged = false;
			}
			
			if (state == State.TOUCHDOWN) {
				if (Assets.notify(touchdown))
					setState(State.PICKPLAY);
			}
			else if( state == State.PLAYING )
				gameClock.decrease();

			if (Gdx.input.isTouched(0)) {
				camera.unproject(_touchPoint.set(Gdx.input.getX(),
						Gdx.input.getY(), 0));

				/*if (state == State.PLAYING) {
					if (football.possessed() != null) {
						// football.possessed().throwTo(offense.getPlayerByPosition("flanker_right"));
						
						Team qbteam = football.possessed().getTeam();
						Player closest = qbteam.getPlayers(0);
						Vector2 tp = new Vector2(_touchPoint.x, _touchPoint.y);
						Vector2 d, closest_delta = new Vector2(qbteam.getPlayers(0).position().cpy().sub(tp));
						for( Player p : qbteam.getPlayers() ) {
							if( p.getPlayerPosition().equalsIgnoreCase("flanker_right") 
									|| p.getPlayerPosition().equalsIgnoreCase("split_wide_left")
									|| p.getPlayerPosition().equalsIgnoreCase("slot_left")
									|| p.getPlayerPosition().equalsIgnoreCase("split_left")
									|| p.getPlayerPosition().equalsIgnoreCase("split_wide_right")
									|| p.getPlayerPosition().equalsIgnoreCase("split_right")
									|| p.getPlayerPosition().equalsIgnoreCase("slot_right") ) {
								d = p.position().cpy().sub(tp);
								if( d.len() < closest_delta.len() ) {
									closest = p;
									closest_delta.set(d);
								}
							//}
						}
						football.possessed().throwTo(closest);
						football.setIntendedTarget(closest);					
					}
				}
				else*/ if( state == State.WAITING ) {
					setState(State.PLAYING);
				}
			}

			Draw draw = new Draw(camera);
			draw.drawRect(-400, los, 800, 5, Color.BLUE);
			if(down_n == 1)
			{
				firstDown = los + 144;
				draw.drawRect(-400, firstDown, 800, 5, Color.YELLOW);
			}
			else
			{
				draw.drawRect(-400, firstDown, 800, 5, Color.YELLOW);
			}
			draw.drawRect(endzone_top.getX(), endzone_top.getY(), endzone_top.getWidth(),
					endzone_top.getHeight(), new Color(0, 0, 255, .5f));
			draw.drawRect(endzone_bottom.getX(), endzone_bottom.getY(),
					endzone_bottom.getWidth(), endzone_bottom.getHeight(), new Color(255,
							0, 0, .5f));
			
			time.setText(gameClock.print());
			if( gameClock.minutes() == 0 && gameClock.seconds() == 0 ) {
				// quarter over
				gameClock.nextQuarter();
				if( gameClock.quarter() > 4 ) {// game over
					System.out.println("Game over");
					setState(State.OVER);
				}
			}
			
			offensiveLabel.setText(String.format("%02d", offense.score()) + "");
			defensiveLabel.setText(String.format("%02d", defense.score()) + "");

			batch.begin();
			batch.draw(Assets.fieldgoal.getTexture(), -field.getWidth() / 5.5f,
					field.getHeight() / 2 - Assets.fieldgoal.getRegionHeight(),
					Assets.fieldgoal.getRegionWidth(),
					Assets.fieldgoal.getRegionHeight(), 0, 0,
					Assets.fieldgoal.getRegionWidth(),
					Assets.fieldgoal.getRegionHeight(), false, true);
			batch.draw(Assets.fieldgoal, -field.getWidth() / 5.5f,
					-field.getHeight() / 2, Assets.fieldgoal.getRegionWidth(),
					Assets.fieldgoal.getRegionHeight());

			Player po = football.possessed();
			if (po != null) {
				batch.draw(Assets.player_selected, po.getX() - 4, po.getY() + 3, 0, 0,
						34, 34, 1, 1, po.getRotation());
				if( po.tackled() ) 
				{
					football.position(po.position());
					setState(State.PICKPLAY);
					los = football.getY();
					down_n++;
					
					if( football.getY() >= firstDown ){
						down = "1st and 10";
						down_n = 1;
					}
					else if(down_n < 5){
						distance = (int) (firstDown - los)/10;
						down = down_n+" and "+distance;
					}
				}
			}

			for (Player p : defense.getPlayers()) {
				if (state == State.PLAYING) {
					if( p.hasBall() ) {
						
					}
					else
						p.colliding(batch, true);
					
					for (Player q : offense.getPlayers()) {
						if (q.getPlayerPosition()
								.equalsIgnoreCase(p.getBlock())) {
							//p.addTarget(q.position());
							p.setTargetPlayer(q);
						}
					}
					if (po != null && p.targets().size() < 1)
						p.target(po.position());
					p.seek(p.target());
					p.move();
				}
				p.draw(batch, 0);
			}
			for (Player p : offense.getPlayers()) {
				if (state == State.PLAYING) {
					if( p.hasBall() ) {
						if( p.tackled() ) {
							if( football.getY() >= los+144 )
								down = "1st and 10";
							else {
								down_n++;
								los = football.getY();
								if( football.getY() > firstDown ) {
									down_n = 1;
									firstDown = los+144;
								}
								down = down_n+" and "+distance;
								downLabel.setText(down);
							}
							setState(State.PICKPLAY);
						}
					}
					else 
						p.colliding(batch, true);
					// p.avoid(players.getNeighbors());
					p.seek(p.target());

					for (Player q : defense.getPlayers()) {

						if (q.getPlayerPosition()
								.equalsIgnoreCase(p.getBlock())) {
							p.target(q.position());
						}
					}

					p.move();
				}
				p.draw(batch, 180);
			}
			football.seek(football.target());
			football.move();
			if(football.isDead() && state == State.PLAYING) {
				setState(State.PICKPLAY);
				down_n++;
				down = down_n+" and "+distance;
				downLabel.setText(down);				
			}
			float x, y;

			if (football.possessed() != null) {
				x = football.possessed().getX() - camera.position.x;
				y = football.possessed().getY() - camera.position.y;
			} else {
				x = football.position().x - camera.position.x;
				y = football.position().y - camera.position.y;
			}
			//camera.translate(x, y, 0);
			
			if (po == null)
				football.draw(batch);

			// check if in endzone
			if (state == State.PLAYING && football.possessed() != null
					&& football.possessed().position().y > 720) {
				football.possessed().getTeam().touchdown();
				setState(State.TOUCHDOWN);
			}

			batch.end();
			super.render(delta);
		}
		handleInput();

		Table.drawDebug(stage);

		try {
			Thread.sleep(sleep); // ~60FPS
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

}
