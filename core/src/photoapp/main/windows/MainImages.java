package photoapp.main.windows;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;

import photoapp.main.CommonButton;
import photoapp.main.Main;
import photoapp.main.graphicelements.MixOfImage;
import photoapp.main.storage.ImageData;

public class MainImages {
    static String fileName = "MainImages";
    public static Table imagesTable;
    public static Integer imageI = 0;
    public static Integer lastImageI = 0;

    public static Integer nextToNotLoad = 0;
    public static Boolean selectModeIsOn = false;
    public static ArrayList<ImageData> selectedList = new ArrayList<ImageData>();
    public static boolean change = true;

    // public static ArrayList<String> imagesWithPoorQuality = new
    // ArrayList<String>();
    // public static ArrayList<String> imagesWithGoodQuality = new
    // ArrayList<String>();
    public static Boolean imageWithGoodQuality = false;
    static public Long lastImageChange = (long) 0;
    static Integer max = 0;

    // public static float lastx = 0;
    // public static float lasty = 0;
    // public static float x = 0;
    // public static float y = 0;
    static String imageOver = "";
    static boolean over1 = false;
    static boolean over2 = false;
    static Consumer lastOnclick = null;
    static String lastImageName = "";
    static List<String> lastPlaceImageList = null;

    public static void create() {
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

        Main.mainTable.clear();
        createButton();

    }

    public static void clear() {
        Gdx.app.log(fileName, "clear");

        MixOfImage.stopLoading();
        Main.mainTable.clear();
        imagesTable.clear();

    }

    public static void load() {
        Gdx.app.log(fileName, "load");

        createImagesButton(imageI, false);
    }

    public static void createButton() {
        if (!selectModeIsOn) {
            CommonButton.createAddImagesButton(Main.mainTable);
        }
        CommonButton.createRefreshButton(Main.mainTable);

        Main.placeImage(List.of("images/next down.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    nextImages();
                }, null, null, true, true, false, Main.mainTable, true, true, "next images");
        Main.placeImage(List.of("images/previous up.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    previousImages();
                }, null, null,
                true, true, false, Main.mainTable, true, true, "previous images");
        if (selectModeIsOn) {

            Main.placeImage(List.of("images/delete.png"), "basic button",
                    new Vector2(0, 0),
                    Main.mainStage,
                    (o) -> {
                        deleteImagesOfList(selectedList);
                        selectModeIsOn = false;
                        reload();

                    }, null, null,
                    true, true, false, Main.mainTable, true, true, "delete selected images");
            Main.placeImage(List.of("images/love.png"), "basic button",
                    new Vector2(0, 0),
                    Main.mainStage,
                    (o) -> {
                        loveImagesOfList(selectedList);
                        selectModeIsOn = false;
                        reload();

                    }, null, null,
                    true, true, false, Main.mainTable, true, true, "love selected images");

        } else {

            Main.placeImage(List.of("images/isSelected.png"), "basic button",
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
                    true, true, false, Main.mainTable, true, true, "select images");
        }
        CommonButton.createExport(Main.mainTable, null, "export all the images");

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
                Main.graphic.getInteger("size of main images width"),
                Main.graphic.getInteger("size of main images height"));
        imagesTable.setPosition(
                Main.graphic.getInteger("border"),
                Gdx.graphics.getHeight() - Main.graphic.getInteger("border") - imagesTable.getHeight()
                        - Main.graphic.getInteger("size of infoLabel width"));

        Main.mainStage.addActor(imagesTable);

    }

    public static void createImagesButton(Integer firstI, Boolean isFirstLoading) {
        // if (imageWithGoodQuality) {
        // }
        Integer column = Main.graphic.getInteger("size of main images width")
                / Main.graphic.getInteger("size of main images button");
        Integer row = Main.graphic.getInteger("size of main images height")
                / Main.graphic.getInteger("size of main images button");

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
                            && MixOfImage.manager.isLoaded(ImageData.IMAGE_PATH + "/100/" + imageData.getName())
                            || isFirstLoading) {
                        MixOfImage.toPlaceList.remove(imageData.getName());

                        List<String> placeImageList = new ArrayList<String>();

                        placeImageList.add(ImageData.IMAGE_PATH + "/" + imageName);

                        if (selectModeIsOn) {
                            placeImageList.add("images/red outline.png");

                        } else {
                            placeImageList.add(Main.imageParam.getString("outline"));
                        }
                        if (selectedList.contains(imageData) && selectModeIsOn) {
                            placeImageList.add("images/isSselected.png");
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
                                    }, null, true, true, false, imagesTable, false, true, "");
                        } else {

                            List<String> placeImageListOver = new ArrayList<String>(placeImageList);
                            placeImageListOver.add(Main.imageParam.getString("over"));
                            placeImageButton(placeImageList, imageName, "enter", placeImageListOver, (o) -> {
                                clear();
                                Main.unLoadAll();
                                ImageEdition.open(imageName, true);
                            }, null, null);
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

    private static void placeImageButton(List<String> placeImageList, String imageName, String mode,
            List<String> placeImageListOver,
            Consumer<Object> onClicked, final Consumer<Object> onEnter, final Consumer<Object> onExit) {
        List<String> place;
        if (mode.equals("enter")) {
            place = new ArrayList<String>(placeImageList);
        } else {
            place = new ArrayList<String>(placeImageListOver);
        }
        ;

        Main.placeImage(place,
                "main images button",
                new Vector2(0, 0),
                Main.mainStage, (click) -> {

                    if (onClicked != null) {
                        onClicked.accept(null);
                    }
                },
                (enter) -> {

                    // // if (lastImageChange != null && !lastImageName.equals(imageName) &&
                    // // lastImageChange != null) {
                    // // }

                    // System.out.println("enter : " + imageName);
                    // // over2 = false;
                    // // if (!over1) {
                    // if (!lastImageName.equals(imageName)) {
                    // if (!lastImageName.equals("")) {

                    // placeImageButton(lastPlaceImageList, lastImageName, "enter",
                    // lastPlaceImageList,
                    // (click) -> {
                    // clear();
                    // Main.unLoadAll();
                    // ImageEdition.open(imageName, true);
                    // }, onEnter,
                    // onExit);
                    // }
                    // placeImageButton(placeImageList, imageName, "exit", placeImageListOver,
                    // onClicked, onEnter,
                    // onExit);

                    if (onEnter != null) {
                        onEnter.accept(null);
                    }
                    // }
                    // lastImageName = imageName;
                    // lastPlaceImageList = new ArrayList<String>(placeImageList);

                }, (exit) -> {

                    // System.out.println("exit bis : " + imageName);
                    // if (!over2) {
                    // System.out.println("change");
                    // placeImageButton(placeImageList, imageName, "enter", placeImageListOver,
                    // onClicked, onEnter,
                    // onExit);

                    if (onExit != null) {
                        onExit.accept(null);
                    }
                    // over1 = false;
                    // }
                    // over2 = false;
                    // imageOver = "";

                    // }
                    System.out.println("exit");
                }, true, true, false, imagesTable, false, true, "");

        // else if (mode.equals("exit")) {
        // Main.placeImage(placeImageListOver,
        // "main images button",
        // new Vector2(0, 0),
        // Main.mainStage,
        // (click) -> {
        // System.out.println("test");
        // // onOver = false;

        // if (onClicked != null) {
        // onClicked.accept(null);
        // }

        // }, (enter) -> {
        // // System.out.println("will be out");
        // // onOver = true;
        // }, (exit) -> {
        // // marche pas bien !!!
        // System.out.println("exit nothing : " + imageName);
        // placeImageButton(placeImageList, imageName, "enter", placeImageListOver,
        // onClicked, onEnter,
        // onExit);

        // if (onExit != null) {
        // onExit.accept(null);
        // }
        // // --
        // // if (onOver) {
        // // if (window.equals(Main.windowOpen)) {

        // // }

        // // } else {
        // // onOver = false;
        // // }

        // }, true, true, false, imagesTable, false, true, "");
        // }
    }

    public static void previousImages() {
        imageWithGoodQuality = false;

        Integer column = Main.graphic.getInteger("size of main images width")
                / Main.graphic.getInteger("size of main images button");

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
        Integer column = Main.graphic.getInteger("size of main images width")
                / Main.graphic.getInteger("size of main images button");

        imageI += column;
        if (imageI >= Main.imagesData.size()) {
            imageI -= column;
            return;
        }
        imagesTable.clear();
        createImagesButton(imageI, true);

        Main.checkToUnload(null);

    }

    public static void render() {
        if (TimeUtils.millis() - lastImageChange > 600) {
            lastImageChange = TimeUtils.millis();
            if (imageI.equals(lastImageI)) {
                if (change) {

                    change = false;

                    for (Integer i = 0; i < max; i++) {
                        Integer imageInteger = imageI + i;
                        ImageData imageData = Main.imagesData.get(imageInteger);
                        String imageName = imageData.getName();
                        change = false;

                        if (!MixOfImage.manager.isLoaded(ImageData.IMAGE_PATH + "/"
                                + MixOfImage.squareSize.get(0) + "/" + imageName)
                                && !MixOfImage.isOnLoading.contains(ImageData.IMAGE_PATH + "/"
                                        + MixOfImage.squareSize.get(0) + "/" + imageName)) {
                            MixOfImage.loadImage(ImageData.IMAGE_PATH + "/"
                                    + MixOfImage.squareSize.get(0) + "/" + imageName, false,
                                    false);
                            change = true;

                        }

                    }
                    reload();
                }

            } else {
                change = true;
            }
            lastImageI = imageI;

        }
    }
}
