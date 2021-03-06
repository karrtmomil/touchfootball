package com.me.touchfootball2.gameplay;

import java.util.ArrayList;
import java.util.Collection;

import com.me.touchfootball2.gameplay.players.Locomotion;

public class Team implements Neighborhood {
	// public enum TeamType { OFFENSE, DEFENSE };
	protected ArrayList<Player> players;
	// protected TeamType side;
	private int score;
	public Team() {
		this(new ArrayList<Player>());
	}
	public Team(ArrayList<Player> players) {
		score = 0;
		this.players = players;
		for( Player p : players ) {
			p.setTeam(this);
		}
	}
	public void clear() {
		players.clear();
	}
	public Player add(Player p) {
		return addPlayer(p);
	}
	public Player addPlayer(Player p) {
		players.add(p);
		return p;
	}
	public void addPlayers(ArrayList<Player> players) {
		this.players.addAll(players);
	}
	public void addPlayers(Team team) {
		addPlayers(team.getPlayers());
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public Player get(int i) {
		return getPlayers().get(i);
	}
	public Player getPlayers(int i) {
		return getPlayers().get(i);
	}
	public int score() {
		return score;
	}
	public void score(int n) {
		score = n;
	}
	public void touchdown() {
		score(score()+7);
	}
	public void fieldgoal() {
		score(score()+3);
	}
	public void fieldgoalafter() {
		score(score()+1);
	}
	public void twopointconversion() {
		score(score()+2);
	}
	public Player getPlayerByPosition(String pos) {
		for (Player p : getPlayers()) {
			if (p.getPlayerPosition().equalsIgnoreCase(pos)) {
				return p;
			}
		}
		return null;
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
