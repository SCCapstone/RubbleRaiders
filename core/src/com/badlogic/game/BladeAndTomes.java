package com.badlogic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class BladeAndTomes extends ApplicationAdapter {
	SpriteBatch batch;
	Texture playerIcon;
	Texture enemyIcon;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		playerIcon = new Texture("PlayerIcon.jpg");
		enemyIcon = new Texture("EnemyIcon.jpg");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(playerIcon, 0, 0);
		batch.draw(enemyIcon, 10, 10);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerIcon.dispose();
	}
}
