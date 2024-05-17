package photoapp.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.HdpiMode;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		// Monitor primary = Lwjgl3ApplicationConfiguration.getPrimaryMonitor();

		config.setForegroundFPS(24);
		config.setMaximized(true);

		config.setDecorated(false);
		config.setResizable(false);
		config.setInitialBackgroundColor(Color.BLACK);
		config.setWindowIcon("images/icon.png");

		config.setWindowedMode(700, 500);
		config.setWindowSizeLimits(200, 100, 4000, 2000);

		config.setHdpiMode(HdpiMode.Logical);

		config.setTitle("PhotoApp");

		new Lwjgl3Application(new Main(), config);
	}

	public static void render() {
		Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();

	}
}
