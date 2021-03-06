package com.me.touchfootball2.gameplay;

import java.util.ArrayList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.touchfootball2.gui.GamePlayScreen;

public class Player extends DynamicGameObject {
	/* ************************ VARIABLES ************************ */
	public static float width = 26;
	public static float height = 26;

	private Team team;
	private TeamColor color;

	private int id;
	private float angle;

	private boolean hasBall = false;
	protected DynamicGameObject targetPlayer = null;

	private Animation walkAnimation;
	private Texture walkSheet;
	private Sprite walkFrameSprites[];
	private TextureRegion currentFrame;
	float stateTime;
	private Vector2 desiredVelocity;
	private Vector2 steeringForce;

	protected Queue<Action> actions;
	private String toBlock = null;
	public String playerPosition;

	public static enum TeamColor {
		RED, ORANGE, YELLOW, GREEN, BLUE, TEAL, PURPLE
	}

	/**
	 * The various behaviors available to each player, seek and flee.
	 */
	public static enum Behavior {
		SEEK, FLEE, PURSUE, MOVE
	};

	/* ************************ CONSTRUCTORS ********************* */

	/**
	 * 
	 * @param x
	 * @param y
	 * @param i
	 * @param neighborhood
	 * @param actions
	 */
	public Player(float x, float y, int i, Team team, Neighborhood neighborhood) {
		this(x, y, width, height, -1, 2, (float) 1, i, TeamColor.BLUE,
				team, neighborhood);
	}

	/**
	 * Constructor to create a player instance
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @param t
	 *            which team the player belongs to
	 * @param id
	 *            the unique id of the player
	 * @param color
	 *            the color of the player
	 */
	public Player(float x, float y, int id, TeamColor color,
			Team team, Neighborhood neighborhood) {
		this(x, y, width, height, -1, 2, 1f, id, color, team, neighborhood);
	}

	public Player(Player p) {
		this(p.getX(), p.getY(), Player.width, Player.height, -1, p.maxVelocity,
				p.maxAcceleration, p.id, p.color, p.team, p.neighborhood);
	}

	/**
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @param width
	 *            width of the player
	 * @param height
	 *            height of the player
	 * @param radius
	 *            radius of the player
	 * @param maxVelocity
	 *            max speed the player can move
	 * @param maxAcceleration
	 *            the max acceleration fo the player
	 * @param team
	 *            the team the player belongs too
	 * @param id
	 *            the unique player id
	 */
	public Player(float x, float y, float width, float height, float radius,
			float maxVelocity, float maxAcceleration, int id, TeamColor color, Team team,
			Neighborhood neighborhood) {
		super(x, y, width, height);
		this.id = id;
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		this.mass = 250;
		this.neighborhood = neighborhood;
		this.angle = 90;
		toBlock = null;
		color(color);
	}

	// MOVE LATER
	public void draw(SpriteBatch spriteBatch) {
		draw(spriteBatch, 0f);
	}

	public void draw(SpriteBatch spriteBatch, float angle) {
		walkAnimation = new Animation((float) .3f / velocity.len(),
				walkFrameSprites);
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = walkAnimation.getKeyFrame(stateTime, true);

		Sprite d = new Sprite(currentFrame);
		if (velocity.len() != 0)
			this.angle = (float) Math.toDegrees(Math.atan2(velocity.y,
					velocity.x)) + 90;
		else
			this.angle = angle;
		
		d.setRotation(this.angle);
		d.setPosition(position().x, position().y);
		d.draw(spriteBatch);
	}

	/* ************************ GETTERS AND SETTERS ************************ */

	public void setBLock(String toBlock2) {
		this.toBlock = toBlock2;
	}

	public String getBlock() {
		return toBlock;
	}

	public TeamColor color() {
		return color;
	}

	public void color(TeamColor c) {
		color = c;
		if (color == TeamColor.BLUE || color == null)
			walkSheet = new Texture(
					Gdx.files.internal("gameplay/players/blue.png"));
		else if (color == TeamColor.RED)
			walkSheet = new Texture(
					Gdx.files.internal("gameplay/players/red.png"));
		else if(color == TeamColor.ORANGE)
			walkSheet = new Texture(
					Gdx.files.internal("gameplay/players/orange.png"));
		else if(color == TeamColor.YELLOW)
			walkSheet = new Texture(
					Gdx.files.internal("gameplay/players/yellow.png"));
		else if(color == TeamColor.TEAL)
			walkSheet = new Texture(
					Gdx.files.internal("gameplay/players/teal.png"));
		else if(color == TeamColor.GREEN)
			walkSheet = new Texture(
					Gdx.files.internal("gameplay/players/green.png"));
		else if(color == TeamColor.PURPLE)
			walkSheet = new Texture(
					Gdx.files.internal("gameplay/players/purple.png"));

		TextureRegion[][] tmp = TextureRegion.split(walkSheet, 26,
				walkSheet.getHeight());
		walkFrameSprites = new Sprite[4];
		for (int i = 0; i < 4; i++) {
			Sprite pl = new Sprite(tmp[0][i]);
			pl.rotate90(true);
			walkFrameSprites[i] = pl;
		}
		walkAnimation = new Animation(1f, walkFrameSprites);
	}

	/**
	 * Gets the player's team
	 * 
	 * @return the player's team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Sets the player's team
	 * 
	 * @param t
	 *            the player's team to be set
	 */
	public void setTeam(Team t) {
		team = t;
	}

	/**
	 * Gets the player's id
	 * 
	 * @return the player's id
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets the player's id
	 * 
	 * @param i
	 *            the player's id to be set
	 */
	public void setID(int i) {
		id = i;
	}

	public boolean hasBall() {
		return hasBall;
	}

	public void hasBall(boolean t) {
		hasBall = t;
	}

	public void addAction(Action a) {
		actions.add(a);
	}

	public boolean nextAction() {
		return actions.remove(0);
	}

	/**
	 * 
	 */
	public boolean equals(Object p) {
		Player o = (Player) p;
		if (p != null)
			return id == o.id;
		return false;
	}

	/**
	 * 
	 * @param dx
	 * @param dy
	 * @param players
	 * @return
	 */
	public DynamicGameObject playerAt(float dx, float dy,
			ArrayList<Player> players) {
		for (Player p : players) {
			if (dx < p.getX() + getWidth() * 3
					&& dx > p.getX() - getWidth() * 2
					&& dy < p.getY() + getHeight() * 3
					&& dy > p.getY() - getHeight() * 2)
				if (p != this) {
					return p;
				}
		}
		return null;
	}

	/**
	 * 
	 * @param dx
	 * @param dy
	 * @param players
	 * @return
	 */
	public DynamicGameObject playerAt(int dx, int dy, ArrayList<Player> players) {
		return playerAt((float) dx, (float) dy, players);
	}

	public DynamicGameObject playerAt(Vector2 v, ArrayList<Player> players) {
		return playerAt(v.x, v.y, players);
	}

	private Vector2 desiredVelocity(Vector2 target) {
		Vector2 v = target.cpy().sub(position()).nor().mul(maxVelocity);
		return v;
	}

	public Vector2 project(Vector2 vector, Vector2 onto) {
		double hypotenuse = vector.len();
		double adjacent = hypotenuse * Math.cos(angle(onto, vector));
		return onto.cpy().nor().mul((float) adjacent);
	}

	public double angle(Vector2 v1, Vector2 v2) {
		return Math.acos(v1.nor().dot(v2.nor()));
	}

	public Vector2 unitOf(Vector2 v1) {
		double magnitude = v1.len();
		return v1.mul((float) (1 / magnitude));
	}

	public void seek(float x, float y) {
		seek(new Vector2(x, y));
	}

	public void seek(float x, float y, boolean arrive) {
		seek(new Vector2(x, y), arrive);
	}

	public void seek(Vector2 pl) {
		seek(pl, false);
	}

	public void seek(Vector2 pl, boolean arrive) {
		if (!position().epsilonEquals(pl, maxVelocity)) {
			desiredVelocity = desiredVelocity(pl);
			steeringForce = desiredVelocity.cpy().sub(velocity);

			velocity.add(steeringForce);

			// The following seems to make the animation jittery
			/*
			 * if( velocity.len() > v){ velocity.nor().mul(v + maxAcceleration);
			 * }
			 */

			if (arrive && targets().size() == 1)
				arrive(pl);
		} else
			velocity(new Vector2(0, 0));
	}

	protected void flee(DynamicGameObject pl) {
		flee(pl.position());
	}

	protected void flee(Vector2 pl) {
		Vector2 desiredVelocity = desiredVelocity(pl);
		Vector2 steeringForce = desiredVelocity.mul(-1).sub(velocity);
		steeringForce.nor().mul(maxAcceleration);
		velocity.add(steeringForce);
	}

	protected void arrive(Vector2 pl) {
		float stopDist = 20;

		Vector2 targetOffset = pl.cpy().sub(position());
		float distance = targetOffset.len();
		if (distance <= stopDist) {
			float rampedSpeed = maxVelocity * (distance / stopDist);
			Vector2 desiredVelocity = targetOffset.cpy().mul(
					rampedSpeed / distance);
			Vector2 steeringForce = desiredVelocity.cpy().sub(velocity);
			velocity.add(steeringForce);
		}
	}

	public void pursue(DynamicGameObject pl) {
		targetPlayer = pl;
		float c = (float) 0.25;
		Vector2 target2 = null;
		float d = pl.position().sub(this.position()).len();
		float T = d * c;
		target2 = pl.position().add(pl.velocity.cpy().mul(T));
		System.out.println("Target2: " + target2);
		seek(target2);
		capVel();
	}

	protected void evade(DynamicGameObject pl) {
		float d = pl.position().dst(position());
		float T = d / this.maxVelocity;
		Vector2 target = pl.position().add(pl.velocity.cpy().mul(T / 2));

		Vector2 desiredVelocity = desiredVelocity(target);
		Vector2 steeringForce = desiredVelocity.mul(-1).sub(velocity);
		steeringForce.nor().mul(maxAcceleration);
		velocity.add(steeringForce);
	}

	/*
	 * protected void avoid(float distance, Neighborhood players) { double
	 * closest = 100; Vector2 avoid = null;
	 * 
	 * for( DynamicGameObject p : neighborhood.getNeighbors() ) { if( !equals(p)
	 * ) { Vector2 otherVector = p.position().sub(position());
	 * 
	 * System.out.println("POSITION: "+position());
	 * 
	 * Vector2 projection = project(otherVector, velocity);
	 * System.out.println("PROJECTION: "+projection);
	 * 
	 * Vector2 intersect = position().add(projection);
	 * System.out.println("INTERSECT: "+intersect);
	 * 
	 * System.out.println("P.POSITION: "+p.position());
	 * 
	 * Vector2 ortho = p.position().sub(intersect);
	 * System.out.println("ORTHO: "+ortho);
	 * 
	 * if( ortho.len() < distance && projection.len() < closest ) { closest =
	 * projection.len(); avoid = ortho.cpy().nor().mul(-1 * velocity.len());
	 * System.out.println(avoid);
	 * System.out.println("HERE -------------------------------------------------_"
	 * ); } } }
	 * 
	 * if( avoid == null ) { avoid = new Vector2(0, 0); }
	 * 
	 * velocity.add(avoid); }
	 */

	public void avoid(ArrayList<Player> players) {
		/*// float c = (float) 5;
		int deflect = 45;
		float v = velocity.len();
		// Vector2 nextPos = position.cpy().add((velocity).cpy().mul(c));
		// DynamicGameObject avoid = playerAt(nextPos, players);
		Player avoid = null;
		for (Player p : players) {
			if (avoid == null
					&& p.position().cpy().sub(this.position()).len() < 144
					&& p.color() != this.color() && p.y - this.y > -10) {
				avoid = p;
			} else if (avoid != null
					&& p.position().cpy().sub(this.position()).len() < avoid
							.position().cpy().sub(this.position()).len()
					&& p.y - this.y > -10) {
				avoid = p;
			}
		}

		if (avoid != null && avoid != targetPlayer
				&& avoid.position().y - this.position().y > 1) {
			if ((avoid.position().sub(position())).cpy().crs(velocity.cpy()) >= 0
					&& x < 0) {
				velocity.add(velocity.cpy().rotate(deflect).nor()
						.mul((velocity.len())));
				velocity.nor().mul(v);
				capVel();
			} else {
				velocity.add(velocity.cpy().rotate(-deflect).nor()
						.mul(velocity.len()));
				velocity.nor().mul(v);
				capVel();
			}
		} else if (avoid != null) {
			// flee(avoid);
		}
		float tempX = x + velocity.x;
		float tempY = y + velocity.y;
		if (tempX < -410) {
			this.seek(-1 * (x + 410) - 410, tempY);
		} else if (tempX > 440) {
			this.seek(-1 * (x - 440) + 440, tempY);
		}
		
		return avoid != null;*/
	}

	public void move() {
		if (targets().size() > 0 && checkBounds()
				&& position().sub(targets().get(0)).len() > 30f) {
			capVel();
			position(position().add(velocity));
		} else {
			velocity.mul(0);
		}
	}

	public void move2() {
		position(position().add(velocity));
		if( position().epsilonEquals(target(), maxVelocity)) {
			targetReached();
		}
	}

	protected void capVel() {
		if (velocity.len() > maxVelocity) {
			velocity.nor().mul(maxVelocity);
		}
	}

	public String getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(String playerPosition) {
		this.playerPosition = playerPosition;
	}

	public void throwTo(Player p) {
		if (hasBall()) {
			GamePlayScreen.football.clearPossessed();
			// predict future location
			float c = p.position().dst(position())
					/ GamePlayScreen.football.maxVelocity() + 15;
			Vector2 pos = p.position().add((p.velocity).cpy().mul(c));
			GamePlayScreen.football.seek(pos);
		}
	}

	public void setTargetPlayer(DynamicGameObject targetPlayer) {
		this.targetPlayer = targetPlayer;
	}

	public boolean tackled() {
		return hasBall() && colliding(null, false);
	}
}
