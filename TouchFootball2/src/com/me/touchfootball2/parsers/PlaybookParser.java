package com.me.touchfootball2.parsers;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.me.touchfootball2.gameplay.Play;

public class PlaybookParser 
{
	FileHandle file;
	
	XmlReader reader = new XmlReader();
	XmlReader.Element el;	
	
	public PlaybookParser(String fileName)
	{
		file = Gdx.files.internal(fileName);
		reader = new XmlReader();
		try {
			el = reader.parse(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPlayLocation(String playName)
	{
		XmlReader.Element foo = el.getChildByNameRecursive(playName);
		XmlReader.Element bar = foo.getChildByName("location");
		return bar.getText();
	}
	
	public ArrayList<Play> getPlays(String type)
	{
		XmlReader.Element playType = el.getChildByNameRecursive(type);
		
		ArrayList<Play> plays = new ArrayList<Play>();
		for(int i = 0; i < playType.getChildCount(); i++)
		{
			Play currentPlay = new Play(playType.getChild(i).getText(), 
					playType.getChild(i).getChildByNameRecursive("location").getText());
			plays.add(currentPlay);
		}
		return plays;
	}
	
	
}
