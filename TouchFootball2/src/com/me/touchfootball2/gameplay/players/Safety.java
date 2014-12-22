package com.me.touchfootball2.gameplay.players;

import com.me.touchfootball2.gameplay.Neighborhood;
import com.me.touchfootball2.gameplay.Player;
import com.me.touchfootball2.gameplay.Team;
import com.me.touchfootball2.gui.GamePlayScreen;

public class Safety extends Player {

	public Safety(float x, float y, float width, float height, float radius, float maxVelocity, float maxAcceleration,	int id, TeamColor color, Team team, Neighborhood neighborhood) {
		super(x, y, width, height, radius, maxVelocity, maxAcceleration, id, color, team, neighborhood);
	}
	public Safety(float x, float y, int id, TeamColor color, Team team, Neighborhood all) {
		super(x, y, id, color, team, all);
	}
	public Safety(float x, float y, int id) {
		super(x, y, id, null, null, null);
	}

	
	@Override
	public void move()
	{
		if(targets().size() > 0 && checkBounds() && !colliding(null, false)  && position().sub(targets().get(0)).len() > 70f
				&& GamePlayScreen.football.possessed() != null
				&& GamePlayScreen.football.possessed().getPlayerPosition().equalsIgnoreCase("qb")) 
		{
			pursue(targetPlayer);
			position(position().add(velocity));
		}else if(GamePlayScreen.football.getIntendedTarget() != null)
		{
			pursue(GamePlayScreen.football.getIntendedTarget());
			position(position().add(velocity));
		}
		else if(GamePlayScreen.football.possessed() != null)
		{
			pursue(GamePlayScreen.football.possessed());
			position(position().add(velocity));
		}
	}
}
