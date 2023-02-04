package ru.myitschool.satgdx;

import static ru.myitschool.satgdx.SatGDX.SCR_HEIGHT;
import static ru.myitschool.satgdx.SatGDX.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    SatGDX c;

    Texture imgBG;

    TextButton btnSound, btnBack;

    public ScreenSettings(SatGDX context) {
        c = context;
        imgBG = new Texture("winter2.jpg"); // фон
        btnSound = new TextButton(c.fontLarge, "ЗВУК ВКЛ", 800, 500);
        btnBack = new TextButton(c.fontLarge, "НАЗАД", 800, 400);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    // касания экрана и клики мыши
        if(Gdx.input.justTouched()){
            c.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            c.camera.unproject(c.touch);
            if(btnBack.hit(c.touch.x, c.touch.y)) {
                c.setScreen(c.screenIntro);
            }
            if(btnSound.hit(c.touch.x, c.touch.y)) {
                c.soundOn = !c.soundOn;
                if(c.soundOn) btnSound.text = "ЗВУК ВКЛ";
                else btnSound.text = "ЗВУК ВЫКЛ";
            }
        }

        // отрисовка всей графики
        c.camera.update();
        c.batch.setProjectionMatrix(c.camera.combined);
        c.batch.begin();
        c.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnSound.font.draw(c.batch, btnSound.text, btnSound.x, btnSound.y);
        btnBack.font.draw(c.batch, btnBack.text, btnBack.x, btnBack.y);
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
