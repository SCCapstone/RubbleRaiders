package com.badlogic.game;

import com.badlogic.game.creatures.Player;
import com.badlogic.game.screens.MainMenu;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BladeAndTomes extends Game {
	ShapeRenderer shapeRenderer;
	SpriteBatch batch;
	BitmapFont font;
	public Stage stageInstance;

	//Defining general text button look
	public TextButton.TextButtonStyle generalTextButtonStyle;
	TextureRegion generalTextButtonUpRegion;
	TextureRegion generalTextButtonDownRegion;
	Texture generalTextButtonUpState;
	Texture generalTextButtonDownState;

	public TextField.TextFieldStyle generalTextFieldStyle;

	public Player player;

	/**
	 * Creates and initializes all objects and variables for the main project before moving the program to
	 * the first screen.
	 */
	@Override
	public void create () {


		player = new Player();

		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();

		//Sets Scene2D instance
		stageInstance = new Stage(new ScreenViewport());

		//Sets upstate and downstate textures for texture Buttons
		generalTextButtonUpState = new Texture(Gdx.files.internal("Text_Button_Up_State.jpg"));
		generalTextButtonDownState = new Texture(Gdx.files.internal("Text_Button_Down_State.jpg"));

		//Sets up the region to be used
		generalTextButtonUpRegion = new TextureRegion(generalTextButtonUpState);
		generalTextButtonDownRegion = new TextureRegion(generalTextButtonDownState);

		//Defines the style to be used in the text buttons
		generalTextButtonStyle = new TextButton.TextButtonStyle ();
		generalTextButtonStyle.up = new TextureRegionDrawable(generalTextButtonUpRegion);
		generalTextButtonStyle.down = new TextureRegionDrawable(generalTextButtonDownRegion);
		generalTextButtonStyle.font = font;
		generalTextButtonStyle.fontColor = new Color(0f, 0f, 0f, 1f);

		//Defines the style to be used for the text field
		generalTextFieldStyle = new TextField.TextFieldStyle();
		generalTextFieldStyle.font = font;
		generalTextFieldStyle.fontColor = new Color(0f,0f,0f,1f);
		generalTextFieldStyle.background = new TextureRegionDrawable(generalTextButtonUpRegion);

		// need to create main menu screen
		setScreen (new MainMenu(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stageInstance.act(Gdx.graphics.getDeltaTime());
		stageInstance.draw();
	}


	/**
	 * Sets the stage viewport to the height and width of the window.
	 * @param width
	 * @param height
	 */
	@Override
	public void resize (int width, int height) {
		stageInstance.getViewport().update(width, height, true);
	}

	/**
	 * Disposes of all instances that take space up in memory and require rendering
	 * including Scene2D, batch, and textures
	 */
	@Override
	public void dispose () {
		stageInstance.dispose();
		batch.dispose();
		shapeRenderer.dispose();
	}
}
