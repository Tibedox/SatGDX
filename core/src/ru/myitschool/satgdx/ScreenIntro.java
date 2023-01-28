package ru.myitschool.satgdx;

import static ru.myitschool.satgdx.SatGDX.SCR_HEIGHT;
import static ru.myitschool.satgdx.SatGDX.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class ScreenIntro implements Screen {
    SatGDX c;

    Texture imgBG;

    TextButton btnPlay, btnExit;

    public ScreenIntro(SatGDX context) {
        c = context;
        imgBG = new Texture("bg.jpg"); // фон
        btnPlay = new TextButton(c.font, "ИГРАТЬ", 800, 500);
        btnExit = new TextButton(c.font, "ВЫХОД", 800, 400);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
// касания экрана и клики мышки
        if(Gdx.input.justTouched()){
            c.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            c.camera.unproject(c.touch);
            if(btnPlay.hit(c.touch.x, c.touch.y)) {
                c.setScreen(c.screenGame);
            }
            if(btnExit.hit(c.touch.x, c.touch.y)) {
                Gdx.app.exit();
            }
        }

        // отрисовка всей графики
        c.camera.update();
        c.batch.setProjectionMatrix(c.camera.combined);
        c.batch.begin();
        c.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        c.font.draw(c.batch, btnPlay.text, btnPlay.x, btnPlay.y);
        c.font.draw(c.batch, btnExit.text, btnExit.x, btnExit.y);
        c.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
