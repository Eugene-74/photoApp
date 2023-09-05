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
    public static Integer imageI = 0;
    public static Integer nextToNotLoad = 0;

    public static void createMainWindow() {
        Main.windowOpen = "Main Images";
        createMainTable();

        CommonButton.createAddImagesButton(mainTable);

        CommonButton.createRefreshButton(mainTable);

        mainTable.row();
        Main.placeImage(List.of("images/next down.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    nextImages();
                }, true, true, false, mainTable, true);
        Main.placeImage(List.of("images/previous up.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    // System.out.println("next image");
                    previousImages();
                },
                true, true, false, mainTable, true);

        createImagesTable();
        createImagesButton(imageI);
    }

    public static void reloadMainImages() {

        imagesTable.clear();
        createImagesButton(imageI);

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

    public static void createImagesButton(Integer firstI) {
        // System.out.println("first image : " + firstI);

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
            Integer max;
            if (Main.imagesData.size() < column * row) {
                max = Main.imagesData.size();
            } else {
                max = column * row;
            }
            Integer index = 1;

            for (int i = 0; i < max; i++) {
                Integer imageInteger = firstI + i;
                // System.out.println("image Int first" + imageInteger);

                if (imageInteger < Main.imagesData.size()) {

                    // if (imageInteger == Main.imagesData.size()) {
                    // Integer maxColumn = Main.imagesData.size() % column;
                    // nextToNotLoad = maxColumn;
                    // max -= column - nextToNotLoad;
                    // // imageI -= column - nextToNotLoad;
                    // System.out.println(column);
                    // System.out.println("max to not load " + nextToNotLoad);

                    // }
                    // if (nextToNotLoad != 0) {
                    // nextToNotLoad -= 1;
                    // if (nextToNotLoad == 0) {
                    // imagesTable.row();
                    // index = 1;

                    // }

                    // }

                    // System.out.println("image int : " + imageInteger);
                    String imageName = Main.imagesData.get(imageInteger).getName();
                    Main.placeImage(List.of(ImageData.IMAGE_PATH + "/150/" + imageName,
                            "images/outline.png"),
                            "main images button",
                            new Vector2(0, 0),
                            Main.mainStage,
                            (o) -> {
                                Main.unLoadAll();
                                ImageEdition.iniImageEdition(imageName, true);
                            }, true, true, false, imagesTable, true);
                    if (index >= column) {
                        imagesTable.row();
                        index = 0;
                    }
                }
                index += 1;
                // }
                // else {
                // nextToNotLoad -= 1;
                // index += 1;

                // if (nextToNotLoad == 0) {
                // index = 1;
                // System.out.println("row NOW");
                // imagesTable.row();

                // }
                // }

            }
            // for (ImageData imageData : Main.imagesData) {
            // if (i + 1 <= max) {
            // Main.placeImage(List.of(ImageData.IMAGE_PATH + "/150/" + imageData.getName(),
            // "images/outline.png"),
            // "main images button",
            // new Vector2(0, 0),
            // Main.mainStage,
            // (o) -> {
            // Main.unLoadAll();
            // ImageEdition.iniImageEdition(imageData.getName(), true);
            // }, true, true, false, imagesTable);
            // }
            // index += 1;
            // i += 1;

            // if (index >= column) {
            // imagesTable.row();
            // index = 0;
            // }
            // }
            // i = 0;
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

    public static void previousImages() {
        Integer column = Main.preferences.getInteger("size of main images width")
                / Main.preferences.getInteger("size of main images button");
        imageI -= column;
        if (imageI < 0) {
            imageI += column;
            return;
        }
        imagesTable.clear();
        createImagesButton(imageI);
    }

    public static void nextImages() {
        Integer column = Main.preferences.getInteger("size of main images width")
                / Main.preferences.getInteger("size of main images button");
        Integer row = Main.preferences.getInteger("size of main images height")
                / Main.preferences.getInteger("size of main images button");
        imageI += column;
        if (imageI + column * (row - 1) >= Main.imagesData.size()) {
            imageI -= column;
            return;
        }
        imagesTable.clear();
        createImagesButton(imageI);
    }
}
