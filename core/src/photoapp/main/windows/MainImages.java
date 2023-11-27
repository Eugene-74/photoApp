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
    static String fileName = "MainImages";
    public static Table mainTable;
    public static Table imagesTable;
    public static Integer imageI = 0;
    public static Integer lastImageI = 0;

    public static Integer nextToNotLoad = 0;
    public static Boolean selectModeIsOn = false;
    public static ArrayList<ImageData> selectedList = new ArrayList<ImageData>();

    // public static ArrayList<String> imagesWithPoorQuality = new
    // ArrayList<String>();
    // public static ArrayList<String> imagesWithGoodQuality = new
    // ArrayList<String>();
    public static Boolean imageWithGoodQuality = false;
    static public Long lastImageChange = (long) 0;

    public static void create() {
        createMainTable();
        createImagesTable();

    }

    public static void open() {
        Gdx.app.log(fileName, "open");

        Main.windowOpen = "MainImages";

        createButton();
        createImagesButton(imageI, true);
    }

    public static void reload() {
        Gdx.app.log(fileName, "reload");

        if (MixOfImage.toPlaceList != null && !MixOfImage.toPlaceList.isEmpty()) {

            MixOfImage.toPlaceList.clear();
        }

        imagesTable.clear();
        createImagesButton(imageI, true);

        mainTable.clear();
        createButton();

    }

    public static void clear() {
        Gdx.app.log(fileName, "clear");

        MixOfImage.stopLoading();
        mainTable.clear();
        imagesTable.clear();

    }

    public static void load() {
        Gdx.app.log(fileName, "load");

        createImagesButton(imageI, false);
    }

    public static void createButton() {
        if (!selectModeIsOn) {
            CommonButton.createAddImagesButton(mainTable);
        }
        CommonButton.createRefreshButton(mainTable);

        mainTable.row();
        Main.placeImage(List.of("images/next down.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    nextImages();
                }, null, null, true, true, false, mainTable, true, "next images");
        Main.placeImage(List.of("images/previous up.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    previousImages();
                }, null, null,
                true, true, false, mainTable, true, "previous images");
        mainTable.row();
        if (selectModeIsOn) {

            Main.placeImage(List.of("images/delete.png", "images/outline.png"), "basic button",
                    new Vector2(0, 0),
                    Main.mainStage,
                    (o) -> {
                        deleteImagesOfList(selectedList);
                        selectModeIsOn = false;
                        reload();

                    }, null, null,
                    true, true, false, mainTable, true, "delete the selected images");
            Main.placeImage(List.of("images/love.png", "images/outline.png"), "basic button",
                    new Vector2(0, 0),
                    Main.mainStage,
                    (o) -> {
                        loveImagesOfList(selectedList);
                        selectModeIsOn = false;
                        reload();

                    }, null, null,
                    true, true, false, mainTable, true, "love the selected images");
            mainTable.row();
            CommonButton.createBack(mainTable);

        } else {
            CommonButton.createBack(mainTable);

            Main.placeImage(List.of("images/selected.png", "images/outline.png"), "basic button",
                    new Vector2(0, 0),
                    Main.mainStage,
                    (o) -> {
                        if (selectModeIsOn) {
                            selectModeIsOn = false;
                        } else {
                            selectModeIsOn = true;
                        }
                        reload();

                    }, null, null,
                    true, true, false, mainTable, true, "select images");
        }

    }

    public static void loveImagesOfList(ArrayList<ImageData> list) {
        ArrayList<ImageData> toRemove = new ArrayList<ImageData>();
        for (ImageData imageData : list) {
            if (imageData.getLoved()) {
                imageData.setLoved(false);
            } else {
                imageData.setLoved(true);
            }
            toRemove.add(imageData);
        }
        for (ImageData imageData : toRemove) {
            list.remove(imageData);
        }
        reload();
    }

    public static void deleteImagesOfList(ArrayList<ImageData> list) {
        ArrayList<ImageData> toRemove = new ArrayList<ImageData>();
        for (ImageData imageData : list) {
            if (ImageEdition.toDelete.contains(imageData, true)) {
                for (ImageData delet : ImageEdition.toDelete) {
                    Integer index = 0;
                    if (delet.equals(imageData)) {

                        ImageEdition.toDelete.removeIndex(index);

                    }
                    index += 1;
                }
            } else {
                ImageEdition.toDelete.add(imageData);
            }
            toRemove.add(imageData);
        }
        for (ImageData imageData : toRemove) {
            list.remove(imageData);
        }
        reload();
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

    public static void createImagesButton(Integer firstI, Boolean isFirstLoading) {
        if (imageWithGoodQuality) {
            System.out.println("good quality");
        }
        Integer column = Main.preferences.getInteger("size of main images width")
                / Main.preferences.getInteger("size of main images button");
        Integer row = Main.preferences.getInteger("size of main images height")
                / Main.preferences.getInteger("size of main images button");

        imageI = firstI - firstI % column;
        if (Main.imagesData != null && firstI + column * row > Main.imagesData.size()) {
            imageI = Main.imagesData.size() - Main.imagesData.size() % column - column * (row - 1);
        }
        if (Main.imagesData != null && Main.imagesData.size() < row * column) {
            imageI = 0;
        }

        if (Main.imagesData == null) {
            Main.infoTextSet("no image loaded, please add new images", true);
            Main.imagesData = new ArrayList<>();
        } else {

            Integer max;
            if (Main.imagesData.size() < column * row) {
                max = Main.imagesData.size();
            } else {
                max = column * row;
            }
            Integer index = 1;

            for (int i = 0; i < max; i++) {
                Integer imageInteger = imageI + i;

                if (imageInteger < Main.imagesData.size() && imageInteger >= 0) {

                    ImageData imageData = Main.imagesData.get(imageInteger);
                    String imageName = imageData.getName();

                    if (MixOfImage.toPlaceList.contains(imageData.getName())
                            && MixOfImage.manager.isLoaded(ImageData.IMAGE_PATH + "/150/" + imageData.getName())
                            || isFirstLoading) {
                        MixOfImage.toPlaceList.remove(imageData.getName());

                        List<String> placeImageList = new ArrayList<String>();
                        if (imageWithGoodQuality) {
                            System.out.println("good quality");

                            placeImageList.add(ImageData.IMAGE_PATH + "/150/" + imageName);
                            if (selectModeIsOn) {
                                placeImageList.add("images/redOutline.png");

                            } else {
                                placeImageList.add("images/outline.png");
                            }
                            if (selectedList.contains(imageData) && selectModeIsOn) {
                                placeImageList.add("images/selected.png");
                            }
                            if (imageData.getLoved()) {
                                placeImageList.add("images/loved preview.png");
                            }
                            if (ImageEdition.toDelete.contains(imageData, true)) {
                                placeImageList.add("images/deleted preview.png");
                            }
                            if (selectModeIsOn) {
                                Main.placeImage(placeImageList,
                                        "main images button",
                                        new Vector2(0, 0),
                                        Main.mainStage,

                                        (o) -> {

                                            if (selectModeIsOn) {

                                                if (selectedList.contains(imageData)) {
                                                    selectedList.remove(imageData);
                                                } else {
                                                    selectedList.add(imageData);
                                                }
                                                reload();

                                            }

                                        }, (o) -> {
                                            if (imageData != Main.lastImageData) {

                                                Main.lastImageData = imageData;
                                                if (selectModeIsOn && Main.isOnClick) {
                                                    if (selectedList.contains(imageData)) {
                                                        selectedList.remove(imageData);
                                                    } else {
                                                        selectedList.add(imageData);
                                                    }
                                                    reload();
                                                }
                                            }
                                        }, null, true, true, false, imagesTable, true, "");
                            } else {

                                Main.placeImage(placeImageList,
                                        "main images button",
                                        new Vector2(0, 0),
                                        Main.mainStage,
                                        (o) -> {
                                            clear();
                                            Main.unLoadAll();
                                            ImageEdition.open(imageName, true);
                                        }, null, null, true, true, false, imagesTable, true, "");
                            }
                        } else {
                            if (MixOfImage.manager.isLoaded(ImageData.IMAGE_PATH + "/150/" + imageName)) {
                                placeImageList.add(ImageData.IMAGE_PATH + "/150/" + imageName);
                            } else {
                                placeImageList.add("images/loading button.png");
                            }

                            placeImageList.add("images/outline.png");

                            Main.placeImage(placeImageList,
                                    "main images button",
                                    new Vector2(0, 0),
                                    Main.mainStage,
                                    null, null, null, true, true, false, imagesTable, true, "");
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
        imageWithGoodQuality = false;

        Integer column = Main.preferences.getInteger("size of main images width")
                / Main.preferences.getInteger("size of main images button");

        imageI -= column;
        if (imageI < 0) {
            imageI += column;
            return;
        }
        imagesTable.clear();
        createImagesButton(imageI, true);

        Main.checkToUnload(null);

    }

    public static void nextImages() {
        imageWithGoodQuality = false;
        Integer column = Main.preferences.getInteger("size of main images width")
                / Main.preferences.getInteger("size of main images button");

        imageI += column;
        if (imageI >= Main.imagesData.size()) {
            imageI -= column;
            return;
        }
        imagesTable.clear();
        createImagesButton(imageI, true);

        Main.checkToUnload(null);

    }
}
