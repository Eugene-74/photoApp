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
    public static Boolean deleteModeIsOn = false;

    public static void create() {
        createMainTable();
        createImagesTable();

    }

    public static void open() {
        Main.windowOpen = "Main Images";

        createButton();
        createImagesButton(imageI);
    }

    public static void reload() {

        imagesTable.clear();
        createImagesButton(imageI);

        mainTable.clear();
        createButton();

    }

    public static void clearMainImages() {

        MixOfImage.stopLoading();
        mainTable.clear();
        imagesTable.clear();

    }

    public static void load() {
        createImagesButton(imageI);
    }

    public static void createButton() {
        if (!deleteModeIsOn) {
            CommonButton.createAddImagesButton(mainTable);
        }
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
        if (deleteModeIsOn) {

            Main.placeImage(List.of("images/back.png", "images/outline.png"), "basic button",
                    new Vector2(0, 0),
                    Main.mainStage,
                    (o) -> {
                        // ImageEdition.save();
                        deleteModeIsOn = false;
                        reload();

                    }, null, null,
                    true, true, false, mainTable, true);
        } else {
            Main.placeImage(List.of("images/back.png", "images/outline.png"), "basic button",
                    new Vector2(0, 0),
                    Main.mainStage,
                    (o) -> {
                        Main.toReload = "File Chooser";
                        clearMainImages();
                        FileChooser.open();

                    }, null, null,
                    true, true, false, mainTable, true);
        }
        mainTable.row();
        Main.placeImage(List.of("images/delete.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    // clearMainImages();
                    if (deleteModeIsOn) {
                        ImageEdition.save();
                        deleteModeIsOn = false;
                        reload();
                    } else {
                        deleteModeIsOn = true;
                        // createImagesButton(imageI);
                        reload();

                    }

                }, null, null,
                true, true, false, mainTable, true);

        if (deleteModeIsOn) {
            CommonButton.createSaveButton(mainTable);
        }

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
        // imagesTable.clear();
        // System.out.println("first image : " + firstI);
        Integer column = Main.preferences.getInteger("size of main images width")
                / Main.preferences.getInteger("size of main images button");
        firstI = firstI - firstI % column;

        if (Main.imagesData == null) {
            Main.infoTextSet("no image loaded, please add new images", true);
            Main.imagesData = new ArrayList<>();
        } else {
            Main.toReload = "mainImages";
            // System.out.println("create images button");
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
                // System.out.println(
                // "alreday load -- " + Main.isInTable(imagesTable,
                // Main.imagesData.get(imageInteger).getName()));

                if (imageInteger < Main.imagesData.size()) {

                    ImageData imageData = Main.imagesData.get(imageInteger);
                    String imageName = imageData.getName();

                    List<String> placeImageList = new ArrayList<String>();
                    placeImageList.add(ImageData.IMAGE_PATH + "/150/" + imageName);
                    if (deleteModeIsOn) {
                        placeImageList.add("images/redOutline.png");

                    } else {
                        placeImageList.add("images/outline.png");

                    }
                    if (ImageEdition.toDelete.contains(imageData, false)) {
                        placeImageList.add("images/deleted preview.png");
                    }
                    if (deleteModeIsOn) {
                        Main.placeImage(placeImageList,
                                "main images button",
                                new Vector2(0, 0),
                                Main.mainStage,
                                (o) -> {
                                    Integer indexBis = 0;
                                    if (!ImageEdition.toDelete.isEmpty()) {
                                        for (ImageData delet : ImageEdition.toDelete) {
                                            if (delet.equals(imageData)) {

                                                ImageEdition.toDelete.removeIndex(indexBis);
                                                System.out.println("removed");
                                                reload();
                                                return;

                                            }
                                            indexBis += 1;
                                        }
                                    }
                                    ImageEdition.toDelete.add(imageData);
                                    MainImages.reload();
                                }, (o) -> {
                                    if (deleteModeIsOn && Main.isOnClick) {
                                        Integer indexBis = 0;
                                        if (!ImageEdition.toDelete.isEmpty()) {
                                            for (ImageData delet : ImageEdition.toDelete) {
                                                if (delet.equals(imageData)) {

                                                    ImageEdition.toDelete.removeIndex(indexBis);
                                                    System.out.println("removed");
                                                    reload();
                                                    return;

                                                }
                                                indexBis += 1;
                                            }
                                        }
                                        ImageEdition.toDelete.add(imageData);
                                    }
                                }, null, true, true, false, imagesTable, true);
                    } else {

                        Main.placeImage(placeImageList,
                                "main images button",
                                new Vector2(0, 0),
                                Main.mainStage,
                                (o) -> {
                                    clearMainImages();
                                    Main.unLoadAll();
                                    ImageEdition.open(imageName, true);
                                }, null, null, true, true, false, imagesTable, true);
                    }
                    if (index >= column) {
                        imagesTable.row();
                        index = 0;
                    }

                    index += 1;
                }

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
        Integer row = Main.preferences.getInteger("size of main images height")
                / Main.preferences.getInteger("size of main images button");
        imageI -= column;
        if (imageI < 0) {
            imageI += column;
            return;
        }
        imagesTable.clear();
        createImagesButton(imageI);

        for (int i = 0; i < column; i++) {
            int index = imageI - column * 3 + i;
            if (index < Main.imagesData.size()
                    && index >= 0) {

                MixOfImage.loadImage(
                        ImageData.IMAGE_PATH + "/150/" + Main.imagesData.get(index).getName());
            }
        }

        for (int i = 0; i < column; i++) {
            int index = imageI + i + column * row + column * 2;
            if (index < Main.imagesData.size()
                    && index >= 0) {

                Main.unLoadAnImage(
                        ImageData.IMAGE_PATH + "/" + Main.imagesData.get(index).getName());
            }
        }
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

        for (int i = 0; i < column; i++) {
            int index = imageI + i + column * row + column * 2;
            if (index < Main.imagesData.size()
                    && index >= 0) {

                MixOfImage.loadImage(
                        ImageData.IMAGE_PATH + "/150/" + Main.imagesData.get(index).getName());
            }
        }
        // System.out.println("iamgeI : " + imageI);
        for (int i = 0; i < column; i++) {
            int index = imageI - column * 2 + i;
            if (index < Main.imagesData.size()
                    && index >= 0) {

                Main.unLoadAnImage(
                        ImageData.IMAGE_PATH + "/" + Main.imagesData.get(index).getName());
            }
        }
    }
}
