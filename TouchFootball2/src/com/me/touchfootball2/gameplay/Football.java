package com.me.touchfootball2.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Football extends DynamicGameObject {
	private Player possessedBy;
	private Player intendedTarget;
	float z = 30;
	private static float minHeight = 20;
	private static float maxHeight = 100;
	
	// private Animation walkAnimation;
	private Texture walkSheet;
	private Sprite walkFrameSprites[];
	// private TextureRegion currentFrame;
	// private float stateTime;
	private float half;
	
	public Football(float x, float y, float width, float height) {
		super(x, y, width, height);
		maxAcceleration = .1f;
		maxVelocity = 10f;
		
		walkSheet = new Texture(Gdx.files.internal("gameplay/players/football.png"));

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth(), walkSheet.getHeight()/2);
        walkFrameSprites = new Sprite[2];
        for( int i = 0; i < 2; i++ ) {
	       	 Sprite pl = new Sprite(tmp[i][0]);
	       	 walkFrameSprites[i] = pl;
        }
        // walkAnimation = new Animation(1f, walkFrameSprites);
	}
	public Football()
	{
		this(0, 0, 512, 428);
	}
	
	public void draw(SpriteBatch spriteBatch) {
		// stateTime += Gdx.graphics.getDeltaTime();
		// currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		
		Sprite d = new Sprite(walkSheet, 0, 0, (int)z, (int)(z/1.7734375));
		float angle = (float) Math.toDegrees(Math.atan2(velocity.y, velocity.x));
		d.setSize(z, (float)(z/1.7734375));
		d.setRegion(0, 0, 512, 256);
		d.setPosition(getX(), getY());
		d.rotate(angle);
		d.draw(spriteBatch);
	}
	
	public void move() {
		if( possessedBy == null ) {
			position(position().add(velocity));
			
			Player p = null;
			for( DynamicGameObject pl : neighborhood.getNeighbors() ) {
				if( !equals(pl) && collidesWith(pl) ) {
					p = (Player) pl; 
					break;
				}
			}
			if( possessedBy == null && p != null && catchable ) {
				possessed(p);
				targets().get(0).set(p.position());
			}
			
			float dist = targets().get(0).cpy().sub(position()).len();
			float scale = 400;
			if(dist >= half){
				z = maxHeight*(2*half/scale) - (maxHeight*(2*half/scale) - minHeight)*(dist - half)/half;
			}else{
				z = (maxHeight*(2*half/scale) - minHeight)*dist/half + minHeight;
				if (z < minHeight){
					z = minHeight;
				}
			}
		}
		else {
			position(possessedBy.position());
		}
	}
	
	private boolean catchable = false;
	public void seek(Vector2 pl) {
		if(pl != targets().get(0) && targets().get(0) != null){
			targets().set(0, pl);
			half = targets().get(0).cpy().sub(position()).len()/2;
		}
		velocity.set(pl.cpy().sub(position()).nor().mul(maxVelocity));
		float stopDist = 30;		
		Vector2 targetOffset = pl.cpy().sub(position());
		float distance = targetOffset.len();
		if(distance <= stopDist){
			float rampedSpeed = maxVelocity * (distance / stopDist);
			Vector2 desiredVelocity = targetOffset.cpy().mul(rampedSpeed/distance);
			Vector2 steeringForce = desiredVelocity.cpy().sub(velocity);
			velocity.add(steeringForce);
		}
		
		if( position().dst(targets().get(0)) < stopDist && Math.round(velocity.len()) != 0 )
			catchable = true;
		else
			catchable = false;
	}
	
	public Player possessed() {
		return possessedBy;
	}
	public void possessed(Player p) {
		if( possessedBy != null ) 
			possessedBy.hasBall(false);
		p.hasBall(true);
		possessedBy = p;
	}
	public void clearPossessed() {
		if( possessedBy != null )
			possessedBy.hasBall(false);
		possessedBy = null;
	}
	public Player getIntendedTarget() {
		return intendedTarget;
	}
	public void setIntendedTarget(Player intendedTarget) {
		this.intendedTarget = intendedTarget;
	}
	public boolean isDead() {
		return possessedBy == null && velocity.len() < .001f && position().cpy().sub(target()).len() < .001f;
	}
}
