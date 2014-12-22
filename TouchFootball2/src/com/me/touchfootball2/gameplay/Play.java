package com.me.touchfootball2.gameplay;

public class Play 
{
	private String playName;
	private String location;
	public Play(String playName, String location)
	{
		this.playName = playName;
		this.location = location;
	}
	
	public String getPlayName()
	{
		return playName;
	}
	
	public String getPlayLocation()
	{
		return location;
	}
}
