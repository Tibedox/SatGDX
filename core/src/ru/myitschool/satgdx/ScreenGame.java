package ru.myitschool.satgdx;

import static ru.myitschool.satgdx.SatGDX.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class ScreenGame implements Screen {
    SatGDX c;

    Sound[] sndMosq = new Sound[5];
    Texture[] imgMosquito = new Texture[11];
    Texture imgBG;

    Mosquito[] mosq = new Mosquito[5];
    Player[] players = new Player[5];
    Player player;
    InputKeyboard keyboard;

    int frags;
    long timeStart, timeCurrent;
    // состояние игры
    public static final int PLAY_GAME = 0, ENTER_NAME = 1, SHOW_TABLE = 2;
    int stateGame = PLAY_GAME;

    TextButton btnBack;

    public ScreenGame(SatGDX context){
        c = context;
        keyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT, 8);
        btnBack = new TextButton(c.fontRed, "X", SCR_WIDTH-60, SCR_HEIGHT-10);

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
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        gameStart();
    }

    @Override
    public void render(float delta) {
        // касания экрана и клики мышки
        if(Gdx.input.justTouched()){
            c.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            c.camera.unproject(c.touch);
            if(stateGame == SHOW_TABLE) {
                gameStart();
            }
            if(stateGame == PLAY_GAME) {
                for (int i = mosq.length - 1; i >= 0; i--) {
                    if (mosq[i].isAlive) {
                        if (mosq[i].hit(c.touch.x, c.touch.y)) {
                            frags++;
                            sndMosq[MathUtils.random(0, 4)].play();
                            if (frags == mosq.length) stateGame = ENTER_NAME;
                            break;
                        }
                    }
                }
            }
            if(stateGame == ENTER_NAME){
                if(keyboard.endOfEdit(c.touch.x, c.touch.y)){
                    player.name = keyboard.getText();
                    gameOver();
                }
            }
            if(btnBack.hit(c.touch.x, c.touch.y) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
                c.setScreen(c.screenIntro);
            }
        }

        // игровые события
        for (int i = 0; i < mosq.length; i++) {
            mosq[i].move();
        }
        if(stateGame == PLAY_GAME) {
            timeCurrent = TimeUtils.millis() - timeStart;
        }

        // отрисовка всей графики
        c.camera.update();
        c.batch.setProjectionMatrix(c.camera.combined);
        c.batch.begin();
        c.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        for (int i = 0; i < mosq.length; i++) {
            c.batch.draw(imgMosquito[mosq[i].faza], mosq[i].getX(), mosq[i].getY(),
                    mosq[i].width, mosq[i].height, 0, 0, 500, 500, mosq[i].isFlip(), false);
        }
        c.font.draw(c.batch,"FRAGS: "+frags, 10, SCR_HEIGHT-10);
        c.font.draw(c.batch, timeToString(timeCurrent), SCR_WIDTH-180, SCR_HEIGHT-10);
        if(stateGame == ENTER_NAME){
            keyboard.draw(c.batch);
        }
        if(stateGame == SHOW_TABLE){
            c.font.draw(c.batch, tableOfRecordsToString(), SCR_WIDTH/3f, SCR_HEIGHT/4f*3);
        }
        btnBack.font.draw(c.batch, btnBack.text, btnBack.x, btnBack.y);
        c.batch.end();
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
        stateGame = SHOW_TABLE;
        players[players.length-1].name = player.name;
        players[players.length-1].time = timeCurrent;
        sortTableOfRecords();
        saveTableOfRecords();
    }

    void gameStart(){
        stateGame = PLAY_GAME;
        frags = 0;
        // создаём объекты комаров
        for (int i = 0; i < mosq.length; i++) {
            mosq[i] = new Mosquito();
        }
        loadTableOfRecords();
        timeStart = TimeUtils.millis();
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
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
    }

    @Override
    public void dispose() {

    }
}
