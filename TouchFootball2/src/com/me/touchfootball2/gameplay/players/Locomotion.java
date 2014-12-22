package com.me.touchfootball2.gameplay.players;

import com.badlogic.gdx.math.Vector2;

public interface Locomotion 
{
    public void move(); 
    public Vector2 position();
    public Vector2 velocity();
    public float getMaxVelocity();
    public float getMaxAcceleration();
}
