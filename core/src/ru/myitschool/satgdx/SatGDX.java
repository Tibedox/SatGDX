package ru.myitschool.satgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

public class SatGDX extends ApplicationAdapter {
	public static final int SCR_WIDTH = 1920, SCR_HEIGHT = 1080;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font;
	Sound[] sndMosq = new Sound[5];

	Texture[] imgMosquito = new Texture[11];
	Texture imgBG;

	Mosquito[] mosq = new Mosquito[50];
	int frags;
	long timeStart, timeEnd, timeCurrent;
	
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
		imgBG = new Texture("bg.jpg");

		// создаём объекты комаров
		for (int i = 0; i < mosq.length; i++) {
			mosq[i] = new Mosquito();
		}
		timeStart = TimeUtils.millis();
	}

	void generateFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("konstant.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 80;
		parameter.color = Color.CORAL;
		parameter.borderColor = new Color().set(0, 0, 0, 0.5f);
		parameter.borderWidth = 4;
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	@Override
	public void render () {
		// касания экрана
		if(Gdx.input.justTouched()){
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			for (int i = mosq.length-1; i >= 0; i--) {
				if(mosq[i].isAlive) {
					if (mosq[i].hit(touch.x, touch.y)) {
						frags++;
						sndMosq[MathUtils.random(0,4)].play();
						break;
					}
				}
			}
		}

		// игровые события
		for (int i = 0; i < mosq.length; i++) {
			mosq[i].move();
		}
		long t = TimeUtils.millis()-timeStart;
		String timeStr = t/1000/60+":"+t/1000%60/10+t/1000%60%10;

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
		font.draw(batch, timeStr, SCR_WIDTH-180, SCR_HEIGHT-10);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		for (int i = 0; i < imgMosquito.length; i++) {
			imgMosquito[i].dispose();
		}
	}
}
