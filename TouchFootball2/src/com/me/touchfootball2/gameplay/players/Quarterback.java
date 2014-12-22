package com.me.touchfootball2.gameplay.players;

import com.me.touchfootball2.gameplay.Neighborhood;
import com.me.touchfootball2.gameplay.Player;
import com.me.touchfootball2.gameplay.Team;
import com.me.touchfootball2.gui.GamePlayScreen;


public class Quarterback extends Player {

	public Quarterback(float x, float y, float width, float height, float radius, float maxVelocity, float maxAcceleration,	int id, TeamColor color, Team team, Neighborhood neighborhood) {
		super(x, y, width, height, radius, maxVelocity, maxAcceleration, id, color, team, neighborhood);
	}
	public Quarterback(float x, float y, int id, TeamColor color, Team team, Neighborhood all) {
		super(x, y, id, color, team, all);
	}
	public Quarterback(float x, float y, int id) {
		super(x, y, id, null, null, null);
	}
	
	public void handoff()
	{
		//gives qb target if handoff
		if(this.getBlock() != null)
		{
			for(Player q : this.getTeam().getPlayers())
			{
				if(q.playerPosition.equals(this.getBlock()))
				{
					this.target(q.position());
					//players have meet and ball is traded
					if(this.collidesWithinRange(q, 50))
					{
						GamePlayScreen.football.possessed(q);
						GamePlayScreen.football.position(q.position());
						this.targets().remove(0);
					}
				}
				
			}
		}
	}
	
	@Override
	public void move() {
		 this.handoff();
		if(targets().size() > 0 && checkBounds() && position().sub(targets().get(0)).len() > 30f ) {
			
			seek(targets().get(0), true);
			avoid(neighborhood.getNeighbors());
			position(position().add(velocity));
		}else if(targets().size() > 0){
			//System.out.println(target);
			targets().remove(0);
		}else{
			velocity.mul(0);
		}
	}
	
}
