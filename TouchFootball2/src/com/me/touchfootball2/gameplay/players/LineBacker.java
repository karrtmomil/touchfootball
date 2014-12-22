package com.me.touchfootball2.gameplay.players;

import com.me.touchfootball2.gameplay.Neighborhood;
import com.me.touchfootball2.gameplay.Player;
import com.me.touchfootball2.gameplay.Team;

public class LineBacker extends Player {

	public LineBacker(float x, float y, float width, float height, float radius, float maxVelocity, float maxAcceleration,	int id, TeamColor color, Team team, Neighborhood neighborhood) {
		super(x, y, width, height, radius, maxVelocity, maxAcceleration, id, color, team, neighborhood);
	}
	public LineBacker(float x, float y, int id, TeamColor color, Team team, Neighborhood all) {
		super(x, y, id, color, team, all);
	}
	public LineBacker(float x, float y, int id) {
		super(x, y, id, null, null, null);
	}
	
	@Override
	public void move()
	{
		/*if( checkBounds()  ) {
			//pursue();
			
			position(position().add(velocity));
		}else if(getTargets().size() > 0 && position().sub(getTargets().get(0)).len() <= .000001f ){
			getTargets().remove(0);
		}else{
			velocity.mul(0);
		}*/
		if(targets().size() > 0 && checkBounds() && position().sub(targets().get(0)).len() > 70f ) {
			//System.out.println(velocity.len());
			seek(targets().get(0), true);
			//avoid(neighborhood.getNeighbors());

			//avoid(neighborhood.getNeighbors());
			
			position(position().add(velocity));
		}else if(targets().size() > 0){
			//System.out.println(target);
			targets().remove(0);
		}else{
			velocity.mul(0);
		}
	}
}
