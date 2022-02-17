package com.mygdx.jetpackjoyrideuf3m08;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Character extends Game {

		SpriteBatch batch;
		BitmapFont font;

		int topScore;
		int lastScore;

	public void create() {

		topScore = 0;
		lastScore = 0;

		batch = new SpriteBatch();
			// Use LibGDX's default Arial font.
			font = new BitmapFont();
			this.setScreen(new MainMenuScreen(this));
		}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
	}
}