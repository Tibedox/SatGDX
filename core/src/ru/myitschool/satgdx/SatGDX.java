package ru.myitschool.satgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Array;
import java.util.Arrays;

public class SatGDX extends ApplicationAdapter {
	public static final int SCR_WIDTH = 1280, SCR_HEIGHT = 720;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font;

	Sound[] sndMosq = new Sound[5];
	Texture[] imgMosquito = new Texture[11];
	Texture imgBG;

	Mosquito[] mosq = new Mosquito[5];
	Player[] players = new Player[5];
	Player player;
	int frags;
	long timeStart, timeCurrent;
	boolean gameOver;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();

		generateFont();

		// создаём объекты звуков
		for (int i = 0; i < sndMosq.length; i++) {
			sndMosq[i] = Gdx.audio.newSound(Gdx.files.internal("komar"+i+".mp3"));
		}

		// создаём объекты изображений
		for (int i = 0; i < imgMosquito.length; i++) {
			imgMosquito[i] = new Texture("mosq"+i+".png");
		}
		imgBG = new Texture("bg.jpg"); // фон

		// создаём игроков
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player("Noname", 0);
		}
		player = new Player("Gamer", 0);

		gameStart();
	}

	void generateFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("konstant.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 60;
		parameter.color = new Color().set(1, 0.9f, 0.4f, 1);
		parameter.borderColor = new Color().set(0, 0, 0, 1);
		parameter.borderWidth = 2;
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	@Override
	public void render () {
		// касания экрана и клики мышки
		if(Gdx.input.justTouched()){
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			if(gameOver) {
				gameStart();
			}
			else {
				for (int i = mosq.length - 1; i >= 0; i--) {
					if (mosq[i].isAlive) {
						if (mosq[i].hit(touch.x, touch.y)) {
							frags++;
							sndMosq[MathUtils.random(0, 4)].play();
							if (frags == mosq.length) gameOver();
							break;
						}
					}
				}
			}
		}

		// игровые события
		for (int i = 0; i < mosq.length; i++) {
			mosq[i].move();
		}
		if(!gameOver) {
			timeCurrent = TimeUtils.millis() - timeStart;
		}

		// отрисовка всей графики
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
		for (int i = 0; i < mosq.length; i++) {
			batch.draw(imgMosquito[mosq[i].faza], mosq[i].getX(), mosq[i].getY(),
					mosq[i].width, mosq[i].height, 0, 0, 500, 500, mosq[i].isFlip(), false);
		}
		font.draw(batch,"FRAGS: "+frags, 10, SCR_HEIGHT-10);
		font.draw(batch, timeToString(timeCurrent), SCR_WIDTH-180, SCR_HEIGHT-10);
		if(gameOver){
			font.draw(batch, tableOfRecordsToString(), SCR_WIDTH/3f, SCR_HEIGHT/4f*3);
		}
		batch.end();
	}

	String timeToString(long time){
		return time/1000/60+":"+time/1000%60/10+time/1000%60%10;
	}

	String tableOfRecordsToString(){
		String s = "";
		for (int i = 0; i < players.length; i++) {
			s += players[i].name + "........" + timeToString(players[i].time) + "\n";
		}
		return s;
	}

	void sortTableOfRecords(){
		for (int i = 0; i < players.length; i++) {
			if(players[i].time == 0) players[i].time = Long.MAX_VALUE;
		}

		boolean flag = true;
		while (flag) {
			flag = false;
			for (int i = 0; i < players.length-1; i++) {
				if(players[i].time > players[i+1].time){
					flag = true;
					Player z = players[i];
					players[i] = players[i+1];
					players[i+1] = z;
				}
			}
		}

		for (int i = 0; i < players.length; i++) {
			if(players[i].time == Long.MAX_VALUE) players[i].time = 0;
		}
	}

	void gameOver(){
		gameOver = true;
		enterPlayerName();
		players[players.length-1].name = player.name;
		players[players.length-1].time = timeCurrent;
		sortTableOfRecords();
		saveTableOfRecords();
	}

	void gameStart(){
		gameOver = false;
		frags = 0;
		// создаём объекты комаров
		for (int i = 0; i < mosq.length; i++) {
			mosq[i] = new Mosquito();
		}
		loadTableOfRecords();
		timeStart = TimeUtils.millis();
	}

	void enterPlayerName(){
		Gdx.input.getTextInput(new Input.TextInputListener(){
			@Override
			public void input (String text) {
				player.name = text;
				gameStart();
			}

			@Override
			public void canceled () {
			}
		}, "Введите имя игрока", player.name, "");
	}

	void saveTableOfRecords(){
		try {
			Preferences pref = Gdx.app.getPreferences("TableOfRecords");
			for (int i = 0; i < players.length; i++) {
				pref.putString("name"+i, players[i].name);
				pref.putLong("time"+i, players[i].time);
			}
			pref.flush();
		} catch (Exception e){
		}
	}

	void loadTableOfRecords(){
		try {
			Preferences pref = Gdx.app.getPreferences("TableOfRecords");
			for (int i = 0; i < players.length; i++) {
				if(pref.contains("name"+i)) {
					players[i].name = pref.getString("name"+i, "null");
				}
				if(pref.contains("time"+i)) {
					players[i].time = pref.getLong("time"+i, 0);
				}
			}
		} catch (Exception e){
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		for (int i = 0; i < imgMosquito.length; i++) {
			imgMosquito[i].dispose();
		}
	}
}
