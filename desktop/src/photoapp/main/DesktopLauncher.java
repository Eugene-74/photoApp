package photoapp.main;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);

		// pas obliger
		config.setDecorated(false);
		config.setResizable(false);
		// jusqu la

		config.setMaximized(true);

		config.setTitle("PhotoApp");

		new Lwjgl3Application(new Main(), config);
	}
}
