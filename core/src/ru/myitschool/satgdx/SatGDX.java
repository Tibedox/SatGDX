package ru.myitschool.satgdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;

public class SatGDX extends Game {
	public static final int SCR_WIDTH = 1280, SCR_HEIGHT = 720;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font, fontRed, fontLarge;

	ScreenIntro screenIntro;
	ScreenGame screenGame;
	ScreenSettings screenSettings;

	boolean soundOn = true;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();

		generateFont();

		screenIntro = new ScreenIntro(this);
		screenGame = new ScreenGame(this);
		screenSettings = new ScreenSettings(this);
		setScreen(screenIntro);
	}

	void generateFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("konstant.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 60;
		parameter.color = new Color().set(1, 0.9f, 0.4f, 1);
		parameter.borderColor = new Color().set(0, 0, 0, 1);
		parameter.borderWidth = 2;
		parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		font = generator.generateFont(parameter);
		parameter.color = Color.RED;
		fontRed = generator.generateFont(parameter);
		parameter.color = new Color().set(1, 0.9f, 0.4f, 1);
		parameter.size = 80;
		fontLarge = generator.generateFont(parameter);
		generator.dispose();
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		for (int i = 0; i < screenGame.imgMosquito.length; i++) {
			screenGame.imgMosquito[i].dispose();
		}
		screenGame.keyboard.dispose();
	}
}
