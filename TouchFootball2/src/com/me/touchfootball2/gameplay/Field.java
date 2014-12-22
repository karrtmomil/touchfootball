package com.me.touchfootball2.gameplay;

// import com.badlogic.gdx.math.Rectangle;
import com.me.touchfootball2.graphics.Assets;

public class Field extends GameObject {
	// private Rectangle endzone_top; // TODO: someday this should be Field's job, not GamePlayScreen's
	// private Rectangle endzone_bottom;
	
	public Field(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.setSkin(Assets.field);
		/*
		endzone_top = new Rectangle(-this.getWidth() / 2,
				-this.getHeight() / 2 + 24, this.getWidth(), 140);
		endzone_bottom = new Rectangle(-this.getWidth() / 2,
				this.getHeight() / 2 - 140 - 19, this.getWidth(), 140);
		*/
	}
	public Field() {
		this(0, 0, 800, 1768);
	}
}
