package com.me.touchfootball2.gameplay.players;

import com.badlogic.gdx.math.Vector2;
import com.me.touchfootball2.gameplay.Neighborhood;
import com.me.touchfootball2.gameplay.Player;
import com.me.touchfootball2.gameplay.Team;
import com.me.touchfootball2.gui.GamePlayScreen;


public class Receiver extends Player {
	public Receiver(float x, float y, float width, float height, float radius, float maxVelocity, float maxAcceleration,	int id, TeamColor color, Team team, Neighborhood neighborhood) {
		super(x, y, width, height, radius, maxVelocity, maxAcceleration, id, color, team, neighborhood);
	}
	public Receiver(float x, float y, int id, TeamColor color, Team team, Neighborhood all) {
		super(x, y, id, color, team, all);
	}
	public Receiver(float x, float y, int id) {
		super(x, y, id, null, null, null);
	}
	
	@Override
	public void move() 
	{
		if(targets().size() > 0 && checkBounds() && position().sub(targets().get(0)).len() > 70f
				&& (GamePlayScreen.football.possessed() == null
				|| (GamePlayScreen.football.possessed() != null 
						&& GamePlayScreen.football.possessed().getPlayerPosition().equalsIgnoreCase("qb")
						|| GamePlayScreen.football.possessed().getPlayerPosition().equalsIgnoreCase("running_back"))))
		{
			seek(targets().get(0), true);
			avoid(neighborhood.getNeighbors());
			position(position().add(velocity));
		}
		//if has ball run to end zone
		else if(GamePlayScreen.football.possessed() != null 
				&& GamePlayScreen.football.possessed().equals(this))
		{
			targets().add(new Vector2(new Vector2(0, 1200)));
			seek(targets().get(0), true);
			avoid(neighborhood.getNeighbors());
			position(position().add(velocity));
		}
		else if(targets().size() > 0 && checkBounds() && position().sub(targets().get(0)).len() > 70f)
		{
			seek(targets().get(0), true);
			avoid(neighborhood.getNeighbors());
			position(position().add(velocity));
		}
		else if(targets().size() > 0)
		{
			targets().remove(0);
		}
		else
		{
			int j = 0;
			Vector2 temp = new Vector2(GamePlayScreen.defense.getPlayers().get(0).position().cpy().sub(this.position()));
			for(int i = 0; i < 11; i++)
			{
				Vector2 closest = new Vector2(GamePlayScreen.defense.getPlayers().get(i).position().cpy().sub(this.position()));
				if(closest.len() < temp.len())
				{
					j = i;
				}
			}
			
			targets().add(GamePlayScreen.defense.getPlayers().get(j).position());
			
				seek(targets().get(0), true);
				position(position().add(velocity));
		}
	}
}