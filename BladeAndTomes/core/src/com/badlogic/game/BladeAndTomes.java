package com.badlogic.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class BladeAndTomes extends Game {
	SpriteBatch batch;
	Texture img,img2;
	private int windowHight = 800,
					windowWidth = 800;
	boolean showFirstImage;


	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("PlayerIcon.jpg");
		img2 = new Texture("EnemyIcon.jpg");
		showFirstImage = false;

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0.1f, 0.2f, 1);
		batch.begin();
		if(showFirstImage){
		batch.draw(img, windowWidth/2, windowHight/2);
			showFirstImage =false;
		}
		else {
			batch.draw(img2, windowWidth/2, windowHight/2-70);
			showFirstImage =true;
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
