package photoapp.main.windows;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import photoapp.main.CommonButton;
import photoapp.main.Main;
import photoapp.main.graphicelements.MixOfImage;
import photoapp.main.storage.ImageData;

public class MainImages {
    public static Table mainTable;
    public static Table imagesTable;
    public static Integer imageI = 0;
    public static Integer nextToNotLoad = 0;

    public static void createMainWindow() {
        createMainTable();

    }

    public static void openMainImages() {
        Main.windowOpen = "Main Images";

        Main.mainStage.addActor(mainTable);

        CommonButton.createAddImagesButton(mainTable);
        CommonButton.createRefreshButton(mainTable);

        mainTable.row();
        Main.placeImage(List.of("images/next down.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    nextImages();
                }, null, null, true, true, false, mainTable, true);
        Main.placeImage(List.of("images/previous up.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    // System.out.println("next image");
                    previousImages();
                }, null, null,
                true, true, false, mainTable, true);
        mainTable.row();

        Main.placeImage(List.of("images/back.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    Main.toReload = "File Chooser";
                    clearMainImages();
                    FileChooser.openFileChooser();

                }, null, null,
                true, true, false, mainTable, true);

        createImagesTable();
        createImagesButton(imageI);
    }

    public static void clearMainImages() {

        MixOfImage.stopLoading();
        mainTable.clear();
        imagesTable.clear();

    }

    public static void reloadMainImages() {

        imagesTable.clear();
        createImagesButton(imageI);

    }

    private static void createImagesTable() {
        imagesTable = new Table();
        imagesTable.setSize(
                Main.preferences.getInteger("size of main images width"),
                Main.preferences.getInteger("size of main images height"));
        imagesTable.setPosition(
                Main.preferences.getInteger("border"),
                Gdx.graphics.getHeight() - Main.preferences.getInteger("border") - imagesTable.getHeight());

        Main.mainStage.addActor(imagesTable);

    }

    public static void createImagesButton(Integer firstI) {
        // System.out.println("first image : " + firstI);

        if (Main.imagesData == null) {
            Main.infoTextSet("no image loaded, please add new images", true);
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

                if (imageInteger < Main.imagesData.size()) {

                    String imageName = Main.imagesData.get(imageInteger).getName();
                    Main.placeImage(List.of(ImageData.IMAGE_PATH + "/150/" + imageName,
                            "images/outline.png"),
                            "main images button",
                            new Vector2(0, 0),
                            Main.mainStage,
                            (o) -> {
                                Main.unLoadAll();
                                ImageEdition.iniImageEdition(imageName, true);
                            }, null, null, true, true, false, imagesTable, true);
                    if (index >= column) {
                        imagesTable.row();
                        index = 0;
                    }
                }
                index += 1;

            }

        }

    }

    public static void createMainTable() {
        mainTable = new Table();
        mainTable.setSize(
                Gdx.graphics.getWidth() - Main.preferences.getInteger("size of main images width")
                        - Main.preferences.getInteger("border") * 3,
                Gdx.graphics.getHeight() - Main.preferences.getInteger("border") * 2);
        mainTable.setPosition(
                Main.preferences.getInteger("size of main images width") + Main.preferences.getInteger("border") * 2,
                Main.preferences.getInteger("border"));

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
