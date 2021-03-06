package com.me.touchfootball2.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameAnimation {
	private Animation walkAnimation;
	private Texture walkSheet;
	private TextureRegion[] walkFrames;
	private TextureRegion currentFrame;

	// private int width;
	// private int height;
	
	private float stateTime;

	public GameAnimation(String file, int cols, int rows, float time) {
		walkSheet = new Texture(Gdx.files.internal(file));
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth()/cols, walkSheet.getHeight()/rows);
		// width = walkSheet.getWidth()/cols;
		// height = walkSheet.getHeight()/rows;
		walkFrames = new TextureRegion[cols * rows];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkAnimation = new Animation(time, walkFrames);
		stateTime = 0f;
	}
	
	public GameAnimation(String file, int width, int height, int cols, int rows, float time) {
		walkSheet = new Texture(Gdx.files.internal(file));
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				width, height);
		// this.width = width;
		// this.height = height;
		walkFrames = new TextureRegion[cols * rows];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkAnimation = new Animation(time, walkFrames);
		stateTime = 0f;
	}
	
	public void draw(SpriteBatch spriteBatch) {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		spriteBatch.draw(currentFrame, 0, 0);
	}
	
	public void draw(SpriteBatch spriteBatch, int x, int y) {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		spriteBatch.draw(currentFrame, x, y);
	}
	
	public void draw(SpriteBatch spriteBatch, int x, int y, int width, int height) {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		spriteBatch.draw(currentFrame, x, y, width, height);
	}
}