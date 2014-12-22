package com.me.touchfootball2.gameplay.players;

import com.badlogic.gdx.math.Vector2;
import com.me.touchfootball2.gameplay.Neighborhood;
import com.me.touchfootball2.gameplay.Player;
import com.me.touchfootball2.gameplay.Team;
import com.me.touchfootball2.gui.GamePlayScreen;

public class Center extends Player {

	public Center(float x, float y, float width, float height, float radius, float maxVelocity, float maxAcceleration,	int id, TeamColor color, Team team, Neighborhood neighborhood) {
		super(x, y, width, height, radius, maxVelocity, maxAcceleration, id, color, team, neighborhood);
	}
	public Center(float x, float y, int id, TeamColor color, Team team, Neighborhood all) {
		super(x, y, id, color, team, all);
	}
	public Center(float x, float y, int id) {
		super(x, y, id, null, null, null);
	}
	@Override
	public void move() {
		if( hasBall() ) {
			throwTo(GamePlayScreen.offense.getPlayerByPosition("qb"));
		}
		else 
		{
			if(targets().size() > 0 && checkBounds() && position().sub(targets().get(0)).len() > 30f ) {
				capVel();
				position(position().add(velocity));
			}else if(targets().size() > 0){
				targets().remove(0);
			}else{
				velocity.mul(0);
			}
		}
	}
	@Override
	public void throwTo(Player p) {
		if( hasBall() ) {
			GamePlayScreen.football.clearPossessed();
			// predict future location
			float c = p.position().cpy().dst(position())/GamePlayScreen.football.maxVelocity()/*+15*/;
			Vector2 pos = p.position().cpy().add((p.velocity()).cpy().mul(c));
			GamePlayScreen.football.target(pos);
		}
	}
}
