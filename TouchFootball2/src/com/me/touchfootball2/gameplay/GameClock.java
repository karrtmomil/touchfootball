package com.me.touchfootball2.gameplay;

import java.util.Date;

public class GameClock {
	int minutes;
	int seconds;
	int quarter;
	
	long time = new Date().getTime();
	
	public GameClock(int minutes, int seconds) {
		this.minutes = minutes;
		this.seconds = seconds;
		this.quarter = 1;
	}
	
	public void set(int minutes, int seconds) {
		minutes(minutes);
		seconds(seconds);
	}
	
	public int minutes() {
		return minutes;
	}
	
	public void minutes(int minutes) {
		this.minutes = minutes;
	}
	
	public int seconds() {
		return seconds;
	}
	
	public void seconds(int seconds) {
		this.seconds = seconds;
	}
	
	public void decrease() {
		long time_now = new Date().getTime();
		if( time_now-time >= 1000 ) {
			if( seconds-- < 1 ) {
				seconds = 59;
				minutes--;
			}

			time = time_now;
		}
	}
	
	public String print() {
		return String.format("%02d", minutes)+":"+String.format("%02d", seconds);
	}
	
	public void nextQuarter() {
		++quarter;
	}
	
	
	
	public int quarter() {
		return quarter;
	}
	
	public String printQuarter() {
		String x = "1st";
		if( quarter == 2 )
			x = "2nd";
		else if( quarter == 3 )
			x = "3rd";
		else if( quarter == 4 )
			x = "4th";
		return new String(x+" quarter");
	}
}
