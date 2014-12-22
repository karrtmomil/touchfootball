package com.me.touchfootball2.gameplay;

import java.util.ArrayList;
import java.util.Collection;

import com.me.touchfootball2.gameplay.players.Locomotion;

public class Players implements Neighborhood {

	ArrayList<Player> players = new ArrayList<Player>();
	public Players() {
		
	}
	public Players(ArrayList<Player> players) {
		this.players = players;
	}
	public void add(Player p) {
		players.add(p);
	}
	public void addPlayers(ArrayList<Player> players) {
		this.players.addAll(players);
	}
	public void clear() {
		players.clear();
	}
	
	@Override
	public Collection<Locomotion> findNearby(Locomotion boid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Player> getNeighbors() {
		return players;
	}

	@Override
	public double getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

}
