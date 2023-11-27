package photoapp.main;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import photoapp.main.windows.ImageEdition;

public class CommonButton {
    public static void createAddImagesButton(Table table) {
        Main.placeImage(List.of("images/add images.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    Main.openFile();
                    Main.reload(false);
                }, null, null,
                true, true, false, table, true, "add images");
    }

    public static void createRefreshButton(Table table) {
        Main.placeImage(List.of("images/refresh.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {

                    Main.reload(false);
                }, null, null,
                true, true, false, table, true, "refresh");
    }

    public static void createSaveButton(Table table) {
        Main.placeImage(List.of("images/save.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    ImageEdition.save();
                }, null, null,
                true, true, false, table, true, "save");
    }

    public static void createBack(Table table) {
        Main.placeImage(List.of("images/back.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    CommonFunction.back();
                }, null, null,
                true, true, false, table, true, "back");
    }
}