package photoapp.main.windows.Parameter;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import photoapp.main.Main;

public class ParameterButton {

    public static void createBrightModeButton() {
        List<String> darkmodeList = new ArrayList<String>();
        darkmodeList.add("images/mode.png");
        darkmodeList.add("images/selected.png");
        if (Main.graphic.getBoolean("option brightmode", false)) {
            darkmodeList.add("images/yes.png");
        } else {
            darkmodeList.add("images/no.png");

        }
        Main.placeImage(darkmodeList, "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    if (Main.graphic.getBoolean("option brightmode", true)) {
                        Main.graphic.putBoolean("option brightmode", false);
                        Main.brightMode = false;
                        Main.iniImage();
                        Parameter.reload();
                    } else {
                        Main.graphic.putBoolean("option brightmode", true);
                        Main.brightMode = true;
                        Main.iniImage();
                        Parameter.reload();

                    }
                    Main.graphic.flush();
                }, null, null,
                true, true, false, Parameter.parameterTable, true, true, "bright mode");
    }
}
