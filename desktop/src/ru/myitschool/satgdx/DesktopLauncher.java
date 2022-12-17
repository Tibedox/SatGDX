package ru.myitschool.satgdx;

import static ru.myitschool.satgdx.SatGDX.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.myitschool.satgdx.SatGDX;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Sat GDX");
		config.setWindowedMode(SCR_WIDTH, SCR_HEIGHT);
		new Lwjgl3Application(new SatGDX(), config);
	}
}
