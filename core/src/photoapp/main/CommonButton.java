package photoapp.main;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.drew.lang.annotations.Nullable;

import photoapp.main.windows.ImageEdition.ImageEdition;
import photoapp.main.windows.LoadImage.LoadImage;

public class CommonButton {
    public static void createAddImagesButton(Table table) {
        Main.placeImage(List.of(Main.imageParam.getString("add images")), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    Main.clear();
                    LoadImage.open();
                    // Main.reload(false);
                }, null, null,
                true, true, false, table, true, true, "add images");
    }

    public static void createRefreshButton(Table table) {
        Main.placeImage(List.of(Main.imageParam.getString("refresh")), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {

                    Main.reload(false);
                }, null, null,
                true, true, false, table, true, true, "refresh");
    }

    public static void createSaveButton(Table table) {
        Main.placeImage(List.of(Main.imageParam.getString("save")), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    ImageEdition.save();
                }, null, null,
                true, true, false, table, true, true, "save");
    }

    public static void createBack(Table table) {
        Main.placeImage(List.of(Main.imageParam.getString("back")), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    CommonFunction.back();
                }, null, null,
                true, true, false, table, true, true, "back");
    }

    public static void createExport(Table table, @Nullable String fileName, String desciption) {

        Main.placeImage(List.of(Main.imageParam.getString("export")), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    Main.infoTextSet("export start", true);
                    LoadImage.exportImages(fileName);
                }, null, null,
                true, true, false, table, true, true, desciption);
    }
}