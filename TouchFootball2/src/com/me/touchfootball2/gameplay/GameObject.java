package com.me.touchfootball2.gameplay;

import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameObject extends Actor {
	public TextureRegion skin;

	public float radius;

	public UUID uuid;

	public void setSkin (TextureRegion texture) {
		this.skin = texture;
		super.setWidth(skin.getRegionWidth());
		super.setHeight(skin.getRegionHeight());
	}

	public GameObject (float x, float y, float width, float height) {
		super.setWidth(width);
		super.setHeight(height);
		super.setX(x - width / 2);
		super.setY(y - height / 2);
		this.radius = (width >= height) ? (width / 2f) : (height / 2f);
		super.setRotation(0);
		this.uuid = UUID.randomUUID();
	}

	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		batch.setColor(1, 1, 1, parentAlpha);
		batch.draw(skin, getX(), getY(), getWidth(), getHeight());
	}

	public Actor hit (float x, float y) {
		return x > 0 && x < getWidth() && y > 0 && y < getHeight() ? this : null;
	}

	public float getX () {
		return super.getX();
	}

	public void setX (float x) {
		super.setX(x);
	}

	public float getY () {
		return super.getY();
	}

	public void setY (float y) {
		super.setY(y);
	}

	public float getWidth () {
		return super.getWidth();
	}

	public void setWidth (float w) {
		super.setWidth(w);
	}

	public float getHeight () {
		return super.getHeight();
	}

	public void setHeight (float h) {
		super.setHeight(h);
	}

	public float radius () {
		return this.radius;
	}

	public void radius (float r) {
		this.radius = r;
	}

	public Vector2 position () {
		return new Vector2(super.getX(), super.getY());
	}

	public void position (Vector2 v) {
		super.setX(v.x);
		super.setY(v.y);
	}

	public void position (float x, float y) {
		super.setX(x);
		super.setY(y);
	}
	
	public float rotation () {
		return super.getRotation();
	}

	public void rotation (float r) {
		super.setRotation((r % 360));
	}
	public float angle () {
		return super.getRotation();
	}

	public void angle (float a) {
		super.setRotation((a % 360));
	}
	
	public boolean intersects (GameObject other) {
		// if (other != null) {
			return (int) this.getX()+this.getWidth() >= other.getX() && (int) this.getX() <= other.getX()+other.getWidth() && (int) this.getY()+this.getHeight() >= other.getY() && (int) this.getY() <= other.getY()+other.getHeight();
		//}
		//return false;
	}

	/*public boolean intersects (GameObject other) {
		if (other != null) {
			float deltaX = other.x - this.x;
			float deltaY = other.y - this.y;
			float radii = this.radius + other.radius;
			
			// a^2 + b^2 = c^2
			float c2 = (deltaX * deltaX) + (deltaY * deltaY);
			
			// Compare squares (Java's "*" is faster than its "/")
			float radii2 = radii * radii;
			return c2 <= radii2;
		}
		return false;
	}*/

	@Override
	public boolean equals(Object other) {
		GameObject obj = (GameObject) other;
		if (other != null)
			return uuid == obj.uuid;
		return false;
	}
}
