package photoapp.main.windows;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import photoapp.main.Main;
import photoapp.main.storage.ImageData;

public class BigPreview {
    static Table BigPreviewTable;

    public static void create() {
        BigPreviewTable = new Table();
        BigPreviewTable.setSize(
                Gdx.graphics.getWidth() - Main.preferences.getInteger("border") * 2,
                Gdx.graphics.getHeight() - Main.preferences.getInteger("border") * 2);
        BigPreviewTable.setPosition(
                Main.preferences.getInteger("border"),
                Main.preferences.getInteger("border"));

        Main.mainStage.addActor(BigPreviewTable);
    }

    public static void open(String imageName) {
        Main.windowOpen = "BigPreview";
        Main.placeImage(List.of(ImageData.IMAGE_PATH + "/" + imageName),
                "big preview",
                new Vector2(0, 0),
                Main.mainStage,

                (o) -> {
                    clear();
                    ImageEdition.open(imageName, true);
                }, null, null, false, true, false, BigPreviewTable, false);

    }

    public static void reload() {

    }

    public static void clear() {
        BigPreviewTable.clear();
    }

}
