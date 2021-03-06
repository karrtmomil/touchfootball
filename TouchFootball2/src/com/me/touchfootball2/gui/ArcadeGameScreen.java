package com.me.touchfootball2.gui;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.me.touchfootball2.TouchFootball2;
import com.me.touchfootball2.gameplay.Football;
import com.me.touchfootball2.gameplay.Player;
import com.me.touchfootball2.gameplay.Player.TeamColor;
import com.me.touchfootball2.graphics.Assets;

public class ArcadeGameScreen extends AbstractScreen {

	private ArrayList<Vector3> positions;
	
	private Vector3 point;
	private boolean refresh;
	
	//private Texture splashTexture;
	
	private InputProcessor input;
	private Football football;
	
	// private Field field;
	private TextureRegion field;
	private Sprite sprite;
	private float scrollTimer = 0f;
	
	private Music music;
	
	private int lvlup = 0;
	private double score = 0;
	private Label scoreLabel;
	private double highScore = 0;
	private Label highScoreLabel;
	private BitmapFont scoreFont;
	private double countdown = 10;
	private Label countLabel;
	private int maxPlayers = 5;
	
	Game game;
	Player p;
	ArrayList<Player> defense = new ArrayList<Player>();
	
	/*Playbook playbook;
	ArrayList<Play> plays = new ArrayList<Play>();
	Play currentPlay;*/

	public ArcadeGameScreen(TouchFootball2 game) {
		
		super(game);
		this.game = game;
		camera.zoom = 1.65f;
		scrollTimer = 0f;
		lvlup = 0;
		score = 0;
		//camera.translate(0, -180, 0);
		
		point = new Vector3();
		positions = new ArrayList<Vector3>();
		p = new Player(0, -300,Player.width, Player.height, 0, 5f, 10f, 20, TeamColor.BLUE, null, null);
		//p.angle(180);
		//p.maxVelocity(5);	
		football = new Football();
		football.possessed(p);
		Random ran = new Random(System.nanoTime());
		for(int i = 0; i < 5; i++){
			defense.add(new Player(ran.nextFloat()*360 - 180, 300 + ran.nextFloat()*1000,Player.width, Player.height, 0, 1f, 1f, i, TeamColor.RED, null, null));
		}
		/*playbook = new Playbook(Gdx.files.internal("xml/playbookTest.xml"));
		plays = playbook.plays();
		currentPlay = plays.get(0);*/
	}

	@Override
	public void show() {
		super.show();
		
		camera.zoom = 1.65f;
		scrollTimer = 0f;
		lvlup = 0;
		score = 0;
		//camera.translate(0, -180, 0);
		
		refresh = false;
		
		point = new Vector3();
		positions = new ArrayList<Vector3>();
		p = new Player(0, -300,Player.width, Player.height, 0, 5f, 10f, 20, TeamColor.BLUE, null, null);
		//p.angle(180);
		//p.maxVelocity(5);	
		football = new Football();
		football.possessed(p);
		Random ran = new Random(System.nanoTime());
		defense.clear();
		for(int i = 0; i < maxPlayers; i++){
			defense.add(new Player(ran.nextFloat()*360 - 180, 300 + ran.nextFloat()*300,Player.width, Player.height, 0, 1f, 1f, i, TeamColor.RED, null, null));
		}
		
		Assets.load();
		
		// input = new PlayBookInputProcessor(camera, positions);
		// Gdx.input.setInputProcessor(input);
		
		/*field = new Field(0, 0, 800, 1728);
		field.setSkin(Assets.field);*/
		
		field = new TextureRegion(new Texture(Gdx.files.internal("field-arcade.png")), 0, 200, 800, 1568);
		field.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		sprite = new Sprite(field, 0, 200, field.getRegionWidth(), field.getRegionHeight());
		//sprite.setRegion(0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		sprite.setX(-field.getRegionWidth()/2);
		sprite.setY(-field.getRegionHeight()/2);
		camera.zoom = .75f;
		if (music == null){
			music = Gdx.audio.newMusic(Gdx.files.internal("gameplay/music/Guile_Theme.mp3"));
			music.setVolume(100);
			music.setLooping(true);
			music.play();	
		}
		
		scoreFont = new BitmapFont(
				Gdx.files.internal("gameplay/fonts/stencil.fnt"),
				Gdx.files.internal("gameplay/fonts/stencil.png"), false);
		scoreLabel = new Label(String.format("%2.0f", score),
				new LabelStyle(scoreFont, new Color(255f, 170f, 0f, .8f)));
		highScoreLabel = new Label(String.format("%2.0f", highScore),
				new LabelStyle(scoreFont, new Color(255f, 170f, 0f, .8f)));
		scoreLabel.setX(100);
		scoreLabel.setY(100);
		stage.addActor(scoreLabel);
		stage.addActor(highScoreLabel);
	}
	
	boolean ghostLine = false;
	
	@Override
	public void render(float delta) {
		//System.out.println(music.isPlaying());
		// Draw draw = new Draw(camera);
		
		// First we clear the screen
		GL10 gl = Gdx.graphics.getGL10();
	    gl.glViewport(0, 0, (int)stage.getWidth(), (int)stage.getHeight());
	    // gl.glMatrixMode(gl.GL_PROJECTION);
	    // gl.glPointSize(10);
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    stage.clear();
	    
	    camera.update();
	    camera.apply(gl);
	    super.render(delta);
	    input = new PlayBookInputProcessor(camera, positions);
	    Gdx.input.setInputProcessor(input);
	    
	    scrollTimer += Gdx.graphics.getDeltaTime()*.5;
	    if(scrollTimer >= 1.0f)
	    	scrollTimer = 0.0f;
	    
	    sprite.setV(scrollTimer +1f);
	    sprite.setV2(scrollTimer);
	    
	    batch.enableBlending();
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		sprite.draw(batch);
		
		batch.end();
		batch.begin();
		
		
		Vector2 point = null;
		if( positions.size() == 1 ){
			point = new Vector2(positions.get(0).x,positions.get(0).y);
			p.seek(point);
			p.target(point);
		}
		if(point != null && p.position().cpy().sub(point).len() < 5f){
			p.velocity(0,5);
		}else if (point == null){
			p.velocity(0,5);
			p.target(p.position());
		}
		p.move();
		football.draw(batch);
		p.draw(batch, 180);
		Random ran = new Random(System.nanoTime());
		for (Player q : defense) {
			q.seek(p.position());
			if( q.position().y > 200){
				q.position(q.position().x,q.position().y - p.maxVelocity()/2);
			}else if(q.position().cpy().sub(p.position()).y > -20 ){
				q.position(q.position().x + q.velocity.x,q.position().y + q.velocity.y);
			}else if(q.position().y > -350 ){
				q.position(q.position().x,q.position().y - 10);
			}else {
				q.position(ran.nextFloat()*360 - 180,300 + ran.nextFloat()*1000);
				q.maxAccel(q.maxAcceleration() + .25f);
				q.maxVelocity(q.maxVelocity() +0.5f);
				lvlup++;
				score++;
			}
			if(q.collidesWith(p)){
				p.maxVelocity(0);
				q.maxVelocity(0);/*
				countLabel = new Label(String.format("%2.0f", countdown),
						new LabelStyle(scoreFont, new Color(255f, 170f, 0f, .8f)));
				stage.addActor(countLabel);
				//System.out.println(score);*/
				//pause();
				//Gdx.app.exit();
				//this.show();
				if(score > highScore)
					highScore = score;
				refresh = true;
			}
			q.draw(batch);
		}
		
		
			
		batch.end();
		
		if(lvlup > maxPlayers*2){
			p.maxVelocity(p.maxVelocity() +1f);
			p.maxAccel(p.maxAcceleration() + .5f);
			lvlup -= maxPlayers*2;
		}
		//score = score + 2f/60;
		
			
		// draw.drawLine(0, 0, 0, 100, Color.BLUE);
		
		/*if( positions.size() > 2 ) {
			for( int i = 0; i < positions.size()-1; i++ ) {
				draw.drawLine(positions.get(i).x, positions.get(i).y, positions.get(i+1).x, positions.get(i+1).y, Color.BLUE, 5);
				// positions.remove(positions.get(i));
				// positions.remove(positions.get(i+1));
				positions.remove(0);
				positions.remove(1);
			}
		}*/
		
		/*for( Vector3 v : positions ) 
			draw.drawCircle(v.x, v.y, 2, Color.BLUE);*/
		
		// batch.end();
		
		scoreLabel = new Label(String.format("%2.0f", score),
				new LabelStyle(scoreFont, new Color(255f, 170f, 0f, .8f)));
		//highScoreLabel = new Label(String.format("%2.0f", highScore),
			//	new LabelStyle(scoreFont, new Color(255f, 170f, 0f, .8f)));
		scoreLabel.setText(String.format("%2.0f", score));
		highScoreLabel.setText(String.format("%2.0f", highScore));
		scoreLabel.setX(0);
		scoreLabel.setY(stage.getHeight() - 100);
		if(highScore > 99)
			highScoreLabel.setX(stage.getWidth()- 130);
		highScoreLabel.setX(stage.getWidth() - 90);
		highScoreLabel.setY(stage.getHeight() - 100);
		stage.addActor(scoreLabel);
		stage.addActor(highScoreLabel);
		batch.enableBlending();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		if(refresh){
			//Gdx.app.exit();
			this.show();
		}
		
	}
	
	@Override
	public void resize(int width, int height) {
		//float aspectRatio = Math.max(1.5f, (float) height / (float) width);
		super.resize(width, height);
       // camera = new OrthographicCamera(Gdx.graphics.getWidth() * aspectRatio, Gdx.graphics.getHeight());
	}

	@Override
	public void dispose() {
		super.dispose();
		//splashTexture.dispose();
		//music.dispose();
		//stage.dispose();
		//batch.dispose();
	}
	
}


