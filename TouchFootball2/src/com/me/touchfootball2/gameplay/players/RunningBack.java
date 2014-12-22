package com.me.touchfootball2.gameplay.players;

import com.me.touchfootball2.gameplay.Neighborhood;
import com.me.touchfootball2.gameplay.Player;
import com.me.touchfootball2.gameplay.Team;

public class RunningBack extends Player {

	public RunningBack(float x, float y, float width, float height, float radius, float maxVelocity, float maxAcceleration,	int id, TeamColor color, Team team, Neighborhood neighborhood) {
		super(x, y, width, height, radius, maxVelocity, maxAcceleration, id, color, team, neighborhood);
	}
	public RunningBack(float x, float y, int id, TeamColor color, Team team, Neighborhood all) {
		super(x, y, id, color, team, all);
	}
	public RunningBack(float x, float y, int id) {
		super(x, y, id, null, null, null);
	}

	
	public void move()
	{
		if(targets().size() > 0 && checkBounds() && position().sub(targets().get(0)).len() > 30f ) {
			capVel();
			avoid(this.neighborhood().getNeighbors());
			position(position().add(velocity));
		}else if(targets().size() > 0){
			targets().remove(0);
		}else{
			velocity.mul(0);
		}
	}
}
