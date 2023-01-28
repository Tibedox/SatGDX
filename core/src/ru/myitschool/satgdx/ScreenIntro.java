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

    public ScreenIntro(SatGDX context) {
        c = context;
        imgBG = new Texture("bg.jpg"); // фон
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
            c.setScreen(c.screenGame);
        }

        // отрисовка всей графики
        c.camera.update();
        c.batch.setProjectionMatrix(c.camera.combined);
        c.batch.begin();
        c.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
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
