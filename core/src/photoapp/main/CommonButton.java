package photoapp.main;

import com.badlogic.gdx.math.Vector2;
import java.util.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import photoapp.main.Main;

public class CommonButton {
    public static void createAddImagesButton(Table table) {
        Main.placeImage(List.of("images/add images.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    Main.openFile();
                    Main.reload(false);
                },
                true, true, false, table);
    }

    public static void createRefreshButton(Table table) {
        Main.placeImage(List.of("images/refresh.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    // infoTextSet("test");

                    Main.reload(false);
                },
                true, true, false, table);
    }

}