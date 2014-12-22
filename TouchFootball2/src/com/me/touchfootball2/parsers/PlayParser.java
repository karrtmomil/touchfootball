package com.me.touchfootball2.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.me.touchfootball2.gameplay.Action;
import com.me.touchfootball2.gameplay.Player;
import com.me.touchfootball2.gameplay.Team;
import com.me.touchfootball2.gameplay.players.Center;
import com.me.touchfootball2.gameplay.players.CornerBack;
import com.me.touchfootball2.gameplay.players.DLinemen;
import com.me.touchfootball2.gameplay.players.FullBack;
import com.me.touchfootball2.gameplay.players.LineBacker;
import com.me.touchfootball2.gameplay.players.OLinemen;
import com.me.touchfootball2.gameplay.players.Quarterback;
import com.me.touchfootball2.gameplay.players.Receiver;
import com.me.touchfootball2.gameplay.players.RunningBack;
import com.me.touchfootball2.gameplay.players.Safety;

public class PlayParser 
{
	
	
	FileHandle file;
	
	XmlReader reader = new XmlReader();
	XmlReader.Element el;
	
	private float los;
	private int inverse;
	private static Vector2 postPosition = new Vector2(0, 1200);
	
	public enum Route
	{
		fly,
		hook,
		seam,
		out_left,
		out_right,
		dropback,
		dline,
		blitz,
		run_outside,
		post
	}
	
	public enum Position
	{
		qb,
		center,
		left_guard,
		right_guard,
		right_tackle,
		left_tackle,
		
		//tight end
		tight_end_right,
		tight_end_left,
		
		//running back
		fullback_i,
		running_back_i,
		full_back_shotgun,
		running_back_shotgun,
		full_back_i_right,
		
		//wide recievers
		flanker_right,
		split_wide_left,
		slot_left,
		split_left,
		split_wide_right,
		split_right,
		slot_right,
		
		
		// DEFENSIVE PLAYERS
		
		left_Dtackle,
		right_Dtackle,
		right_Dend,
		left_Dend,
		weak_lb,
		middle_lb,
		strong_lb,
		right_corner,
		left_corner,
		strong_safety,
		free_safety
		
	}
	
	public PlayParser(String fileName, float los, int inverse) throws IOException
	{
		this.los = los;
		this.inverse = inverse;
		file = Gdx.files.internal(fileName);// new FileHandle(fileName);
		reader = new XmlReader();
		el = reader.parse(file);
	}

		public ArrayList<String> getPosition()
		{
			ArrayList<String> toReturn = new ArrayList<String>();
			
			XmlReader.Element player;
			Array<com.badlogic.gdx.utils.XmlReader.Element> players = el.getChildrenByNameRecursively("position");
			
			for(int i = 0; i < players.size; i++)
			{
				toReturn.add(players.get(i).getText());
			}
			return toReturn;
		}
		
		//
		public ArrayList<Player> load()
		{
			ArrayList<Player> toReturn = new ArrayList<Player>();
			float position[] = null;
			Team team = null;
			
			Array<com.badlogic.gdx.utils.XmlReader.Element> players = el.getChildrenByNameRecursively("player");
			
			for(int i = 0; i < players.size; i++)
			{
				Queue<Action> actions = new ConcurrentLinkedQueue<Action>();
				ArrayList<Vector2> target = new ArrayList<Vector2>();
				String type = null;
				String toBlock = null;
				String playerPosition = null;
				
				com.badlogic.gdx.utils.XmlReader.Element currentPlayer = players.get(i);
				for(int j = 0; j < currentPlayer.getChildCount(); j++)
				{
					
					if(currentPlayer.getChild(j).getName().equalsIgnoreCase("team"))
					{
						team = getTeam();
					}
					else if(currentPlayer.getChild(j).getName().equalsIgnoreCase("position"))
					{
						playerPosition = currentPlayer.getChild(j).getText();
						position = getFloatPosition(Position.valueOf(players.get(i).getChild(j).getText()));
					}
					else if(currentPlayer.getChild(j).getName().equalsIgnoreCase("action"))
					{
						actions.add(getAction(currentPlayer.getChild(j)));
					}
					else if(currentPlayer.getChild(j).getName().equalsIgnoreCase("route"))
					{
						target.addAll(getTarget(currentPlayer.getChild(j), position));
					}
					else if(currentPlayer.getChild(j).getName().equalsIgnoreCase("type"))
					{
						type = currentPlayer.getChild(j).getText();
					}
					else if(currentPlayer.getChild(j).getName().equalsIgnoreCase("block"))
					{
						toBlock = currentPlayer.getChild(j).getText();
					}
				}
				if(type.equalsIgnoreCase("OLinemen"))
				{
					toReturn.add(new OLinemen(position[0], position[1], i));
				}
				else if(type.equalsIgnoreCase("Center"))
				{
					toReturn.add(new Center(position[0], position[1], i));
				}
				else if(type.equalsIgnoreCase("CornerBack"))
				{
					toReturn.add(new CornerBack(position[0], position[1], i));
					//toReturn.get(i).setMaxVelocity(2.5f);
				}
				else if(type.equalsIgnoreCase("DLinemen"))
				{
					toReturn.add(new DLinemen(position[0], position[1], i));
				}
				else if(type.equalsIgnoreCase("FullBack"))
				{
					toReturn.add(new FullBack(position[0], position[1], i));
				}
				else if(type.equalsIgnoreCase("LineBacker"))
				{
					toReturn.add(new LineBacker(position[0], position[1], i));
				}
				else if(type.equalsIgnoreCase("Quarterback"))
				{
					toReturn.add(new Quarterback(position[0], position[1], i));
				}
				else if(type.equalsIgnoreCase("Receiver"))
				{
					toReturn.add(new Receiver(position[0], position[1], i));
				}
				else if(type.equalsIgnoreCase("RunningBack"))
				{
					toReturn.add(new RunningBack(position[0], position[1], i));
				}
				else if(type.equalsIgnoreCase("Safety"))
				{
					toReturn.add(new Safety(position[0], position[1], i));
					//toReturn.get(i).setMaxVelocity(1.5f);
				}
				toReturn.get(i).setPlayerPosition(playerPosition);
				toReturn.get(i).setBLock(toBlock);
				toReturn.get(i).targets(target);
			}
			return toReturn;
		}

		private int getBlock(com.badlogic.gdx.utils.XmlReader.Element currentElement)
		{
			return Integer.parseInt(currentElement.getText());
		}
		
		private ArrayList<Vector2> getTarget(com.badlogic.gdx.utils.XmlReader.Element currentElement, float[] position)
		{
			ArrayList<Vector2> toReturn = new ArrayList<Vector2>();
			
			
			toReturn.addAll(getRoute(Route.valueOf(currentElement.getText()), position));
			
			
			return toReturn;
		}

		public Action getAction(com.badlogic.gdx.utils.XmlReader.Element currentElement)
		{
			Action toReturn = null;
			if(currentElement.getText().equalsIgnoreCase("RunRoute"))
			{
				//ArrayList<Vector2> route = getRoute(Route.valueOf(currentElement.getChildByName("route").getText()));
				//toReturn = new RunRoute(route);
			}
			if(currentElement.getText().equalsIgnoreCase("cover"))
			{
				//TODO
			}
			if(currentElement.getText().equalsIgnoreCase("block"))
			{
				//TODO
			}
			return toReturn;
		}
		
		public Team getTeam() 
		{
			// TODO Auto-generated method stub
			return null;
		}
		
		/**
		 * given a Route from xml returns ArrayList<Vector2> to represent a route
		 * 
		 * @param route
		 * @return
		 */
		public ArrayList<Vector2> getRoute(Route route, float[] position)
		{
			ArrayList<Vector2> toReturn = new ArrayList<Vector2>();
			
			switch(route)
			{
				case fly:
					toReturn.add(new Vector2(position[0] + 0, position[1] + 250));
					break;
				case seam:
					toReturn.add(new Vector2(position[0] + 30, position[1] + 50));
					toReturn.add(new Vector2(position[0], position[1] + 800));
					break;
				case out_left:
					toReturn.add(new Vector2(position[0] + 0, position[1] + 125));
					toReturn.add(new Vector2(position[0] - 150, position[1] + 125));
					break;
				case out_right:
					toReturn.add(new Vector2(position[0] + 0, position[1] + 150));
					toReturn.add(new Vector2(position[0] + 150, position[1] + 125));
					break;
				case dropback:
					toReturn.add(new Vector2(position[0], position[1] - 75));
					break;
				case dline:
					toReturn.add(new Vector2(position[0], position[1] - 75));
					break;
				case run_outside:
					toReturn.add(new Vector2(position[0] + 200, position[1]));
					toReturn.add(new Vector2(position[0] + 200, 1200));
					break;
				case post:
					toReturn.add(new Vector2(position[0] + 0, position[1] + 150));
					toReturn.add(postPosition);
			}
			return toReturn;
		}
		
		
		
		public float[] getFloatPosition(Position pos)
		{
			float xy[] = new float[2];
			
			switch(pos)
			{
				case qb:
					xy[0] = 0;
					xy[1] = los - Player.height * 2.25f;
					break;
				//o-line
				case center:
					xy[0] = 0;
					xy[1] = los - Player.height;
					break;
				
				case right_guard:
					xy[0] = Player.width /2 + Player.width;
					xy[1] = los - Player.height;
					break;
				case left_guard:
					xy[0] = -Player.width - Player.width / 2;
					xy[1] = los - Player.height;
					break;
					
				case left_tackle:
					xy[0] = (-Player.width * 3);
					xy[1] = los - Player.height;
					break;
				case right_tackle:
					xy[0] = Player.width * 3;
					xy[1] = los - Player.height;
					break;
					
					
					//tight end
				case tight_end_right:
					xy[0] = Player.width * 4.5f;
					xy[1] = los - Player.height;
					break;
				case tight_end_left:
					xy[0] = -Player.width * 4.5f;
					xy[1] = los - Player.height;
					break;
					
					
				//running back
				case fullback_i:
					xy[0] = 0;
					xy[1] = los - Player.height * 4;
					break;
				case running_back_i:
					xy[0] = 0;
					xy[1] = los - Player.height * 6;
					break;
				case full_back_shotgun:
					xy[0] = Player.width /2 + Player.width;
					xy[1] = los - Player.height * 5;
					break;
				case running_back_shotgun:
					xy[0] = -Player.width - Player.width / 2;
					xy[1] = los - Player.height * 5;
					break;
				case full_back_i_right:
					xy[0] = 20;
					xy[1] = los - Player.height * 4;
					break;
					
					
				//wide reciever
				case flanker_right:
					xy[0] = 200;
					xy[1] = los  - Player.height * 2;
					break;
				case split_wide_left:
					xy[0] = -300;
					xy[1] = los - Player.height;
					break;
				case slot_left:
					xy[0] = -220;
					xy[1] = los  - Player.height * 2;
					break;
				case split_left:
					xy[0] = -210;
					xy[1] = los - Player.height;
					break;
				case split_wide_right:
					xy[0] = 300;
					xy[1] = los - Player.height;
					break;
				case split_right:
					xy[0] = 232;
					xy[1] = los - Player.height;
					break;
				case slot_right:
					xy[0] = 232;
					xy[1] = los  - Player.height * 2;
					break;
					
				//defense
				case left_Dtackle:
					xy[0] = - Player.width;
					xy[1] = los + Player.height;
					break;
				case right_Dtackle:
					xy[0] = Player.width;
					xy[1] = los + Player.height;
					break;
				case right_Dend:
					xy[0] = Player.width * 3.5f;
					xy[1] = los + Player.height;
					break;
				case left_Dend:
					xy[0] = - Player.width * 3.5f;
					xy[1] = los + Player.height;
					break;
				case weak_lb:
					xy[0] = -Player.width * 3;
					xy[1] = los + Player.height * 4;
					break;
				case middle_lb:
					xy[0] = 0;
					xy[1] = los + Player.height * 4;
					break;
				case strong_lb:
					xy[0] = Player.width * 3;
					xy[1] = los + Player.height * 4;
					break;
				case right_corner:
					xy[0] = 262;
					xy[1] = los + Player.height * 4;
					break;
				case left_corner:
					xy[0] = -270;
					xy[1] = los + Player.height * 4;
					break;
				case strong_safety:
					xy[0] = -Player.width * 4;
					xy[1] = los + 170;
					break;
				case free_safety:
					xy[0] = Player.width * 4;
					xy[1] = los + 170;
					break;
			}
			//xy[1]  = xy[1] * inverse;
			return xy;
		}
}

