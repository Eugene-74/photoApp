package photoapp.main.windows;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import photoapp.main.CommonButton;
import photoapp.main.Main;
import photoapp.main.storage.ImageData;

public class MainImages {
    public static Table mainTable;
    public static Table imagesTable;

    public static void createMainWindow() {
        Main.windowOpen = "Main Images";
        createMainTable();

        CommonButton.createAddImagesButton(mainTable);

        CommonButton.createRefreshButton(mainTable);

        createImagesTable();
        createImagesButton();
    }

    public static void reloadMainImages() {

        imagesTable.clear();
        createImagesButton();

    }

    private static void createImagesTable() {
        imagesTable = new Table();
        imagesTable.setSize(
                Main.preferences.getInteger("size of main images width")
                        - Main.preferences.getInteger("border"),
                Gdx.graphics.getHeight() - Main.preferences.getInteger("border"));
        imagesTable.setPosition(
                Main.preferences.getInteger("border"), Main.preferences.getInteger("border"));

        Main.mainStage.addActor(imagesTable);

    }

    public static void createImagesButton() {
        if (Main.imagesData == null) {
            Main.infoTextSet("no image loaded, please add new images");
            Main.imagesData = new ArrayList<>();
        } else {
            Main.toReload = "mainImages";
            // System.out.println("create images button");
            Integer column = Main.preferences.getInteger("size of main images width")
                    / Main.preferences.getInteger("size of main images button");
            Integer row = Main.preferences.getInteger("size of main images height")
                    / Main.preferences.getInteger("size of main images button");
            // System.out.println(column + "column" + row + " lign");
            // String imageName = "main.jpg";
            Integer index = 0;
            Integer max;
            if (Main.imagesData.size() < column * row) {
                max = Main.imagesData.size();
            } else {
                max = column * row;
            }
            // preIniImagesButton(max, column, row);
            Integer i = 0;
            for (ImageData imageData : Main.imagesData) {
                if (i + 1 <= max) {
                    Main.placeImage(List.of(ImageData.IMAGE_PATH + "/150/" + imageData.getName(),
                            "images/outline.png"),
                            "main images button",
                            new Vector2(0, 0),
                            Main.mainStage,
                            (o) -> {
                                Main.unLoadAll();
                                ImageEdition.iniImageEdition(imageData.getName(), true);
                            }, true, true, false, imagesTable);
                }
                index += 1;
                i += 1;

                if (index >= column) {
                    imagesTable.row();
                    index = 0;
                }
            }
            i = 0;
        }

    }

    // public static void createButton() {
    // Main.infoTextSet("loading ...");
    // Main.toReload = "mainImages";
    // Integer column = Main.preferences.getInteger("size of main images width")
    // / Main.preferences.getInteger("size of main images button");
    // Integer row = Main.preferences.getInteger("size of main images height")
    // / Main.preferences.getInteger("size of main images button");
    // Integer index = 0;
    // Integer max;
    // if (Main.imagesData.size() < column * row) {
    // max = Main.imagesData.size();
    // } else {
    // max = column * row;
    // }
    // Integer i = 0;

    // for (ImageData imageData : Main.imagesData) {
    // if (i + 1 <= max) {

    // Main.placeImage(List.of(ImageData.IMAGE_PATH + "/" + imageData.getName(),
    // "images/outline.png"),
    // "main images button",
    // new Vector2(0, 0),
    // Main.mainStage,
    // (o) -> {
    // ImageEdition.iniImageEdition(imageData.getName(), true);
    // }, true, true, false, imagesTable);
    // Main.infoTextSet("loading");

    // index += 1;
    // if (index >= column) {
    // imagesTable.row();
    // index = 0;
    // }
    // }
    // i += 1;
    // }
    // }

    public static void createMainTable() {
        mainTable = new Table();
        mainTable.setSize(
                Gdx.graphics.getWidth() - Main.preferences.getInteger("size of main images width")
                        - Main.preferences.getInteger("border"),
                Gdx.graphics.getHeight());
        mainTable.setPosition(
                Main.preferences.getInteger("size of main images width") + Main.preferences.getInteger("border"), 0);

        Main.mainStage.addActor(mainTable);
    }
}
