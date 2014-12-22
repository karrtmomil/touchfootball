package com.me.touchfootball2.gui;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.me.touchfootball2.graphics.Draw;

public class PlayBookInputProcessor implements InputProcessor {

	OrthographicCamera camera;
	Draw draw;
	ArrayList<Vector3> positions;
	int index = 0;
	
	public PlayBookInputProcessor(OrthographicCamera camera, ArrayList<Vector3> p) {
		this.camera = camera;
		draw = new Draw(camera);
		positions = p;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	ArrayList<Vector3> temp = new ArrayList<Vector3>();

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		Vector3 point = new Vector3(x, y, 0);
		camera.unproject(point);
		positions.clear();
		positions.add(point);
		
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		// index += 1;
		/*Vector3 point = new Vector3(x, y, 0);
		camera.unproject(point);		
		positions.add(point);*/
		
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		Vector3 point = new Vector3(x, y, 0);
		camera.unproject(point);
		if( positions.size() > 0 )
			positions.remove(positions.size()-1);
		positions.add(point);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

}
