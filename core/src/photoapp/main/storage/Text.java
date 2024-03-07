package photoapp.main.storage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import photoapp.main.Main;

public class Text {
    public static void openText(String language) {

        FileHandle handle = Gdx.files.internal(ImageData.TEXT_PATH + language + ".csv");

        if (!handle.exists()) {
            handle = Gdx.files.internal(ImageData.TEXT_PATH + "en" + ".csv");
        }

        InputStream textes = handle.read();
        @SuppressWarnings("resource")
        String textesString = new BufferedReader(new InputStreamReader(textes))
                .lines().collect(Collectors.joining("\n"));
        if (textesString.equals("") || textesString.equals("\n")) {
            return;
        }
        String[] textesInfo = textesString.split("\n");
        for (String texteInfo : textesInfo) {
            String[] texte = texteInfo.split(":");
            Main.graphic.putString("text " + texte[0], texte[1]);
        }

    }

}
