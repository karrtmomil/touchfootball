package com.me.touchfootball2.gameplay;

import java.util.ArrayList;
import java.util.Collection;

import com.me.touchfootball2.gameplay.players.Locomotion;

public interface Neighborhood {
	Collection<Locomotion> findNearby(Locomotion boid);
	ArrayList<Player> getNeighbors();
    double getRadius();
}
