package photoapp.main.windows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import photoapp.main.CommonButton;
import photoapp.main.Main;
import photoapp.main.storage.ImageData;

public class FileChooser {

    public static Table fileTable;
    public static Table addFileTable;
    // public static Table buttonTable;

    public static void create() {

        createFileTable();
        // createButtonTable();

    }

    public static void open() {
        Main.windowOpen = "FileChooser";

        Main.mainStage.addActor(fileTable);
        // Main.mainStage.addActor(Main.mainTable);
        placeFileChooserButton();

    }

    public static void reload() {
        clear();
        open();
    }

    public static void clear() {
        fileTable.clear();
        Main.mainTable.clear();
    }

    public static void createFileTable() {
        fileTable = new Table();

        fileTable.setSize(Main.graphic.getInteger("size of full width"),
                Main.graphic.getInteger("size of full height"));

        fileTable.setPosition(
                Main.graphic.getInteger("border"),
                Gdx.graphics.getHeight() - fileTable.getHeight() - Main.graphic.getInteger("border"));
    }

    // public static void createButtonTable() {
    // buttonTable = new Table();

    // buttonTable.setSize(
    // Gdx.graphics.getWidth() - Main.graphic.getInteger("size of full width")
    // - Main.graphic.getInteger("border") * 3,
    // Gdx.graphics.getHeight() - Main.graphic.getInteger("border") * 2);
    // buttonTable.setPosition(
    // Main.graphic.getInteger("size of full width") +
    // Main.graphic.getInteger("border") * 2,
    // Main.graphic.getInteger("border"));
    // }

    public static void openFile(String name) {
        Main.imagesData = new ArrayList<ImageData>();
        ImageData.openDataOfImages(name);
        MainImages.open();
        clear();
    }

    public static void placeButton() {
        Main.placeImage(List.of("images/addFile.png"),
                "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    FileChooser.clear();
                    EnterValue.enterAValue((p) -> {
                        addAFile(EnterValue.txtValue.getText());
                        Main.openWindow = true;
                        Main.windowOpen = "FileChooser";

                    }, "enter the file name : ");
                }, null, null,
                true, true, false, Main.mainTable, true, true, "add a file");

        Main.placeImage(List.of("images/openParameter.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    Parameter.open();
                    clear();
                }, null, null,
                true, true, false, Main.mainTable, true, true, "open parameter");

        CommonButton.createExport(Main.mainTable, null, "export all files");
    }

    public static void addAFile(String name) {
        if (!Main.fileData.containsKey(name)) {
            Main.fileData.put(name, 0);
            Main.infoTextSet("file created succefuly", true);
        } else {
            Main.infoTextSet("this file already exist", true);

        }
    }

    public static void placeFileChooserButton() {
        try {

            placeButton();

            Integer column = Main.graphic.getInteger("size of full width")
                    / Main.graphic.getInteger("size of full button");
            Integer row = Main.graphic.getInteger("size of full height")
                    / Main.graphic.getInteger("size of full button");
            // Integer maxByLine;

            Integer maxByLine = row;
            Integer index = 0;
            List<String> names = new ArrayList<String>();

            Main.placeImage(List.of("images/allFile.png"),
                    "full button",
                    new Vector2(0, 0),
                    Main.mainStage,
                    (o) -> {
                        openFile(null);
                    }, null, null,
                    true, true, false, fileTable, true, true, "allFile");

            File handle = new File(ImageData.IMAGE_PATH);

            if (!handle.exists())

            {
                handle.mkdirs();
            }
            for (Entry<String, Integer> entry : Main.entriesSortedByValues(Main.fileData, true)) {
                names.add(entry.getKey());
            }
            Integer totalMax;
            if (names.size() < column * row) {
                totalMax = names.size();
            } else {
                totalMax = column * row;
            }
            Integer i = 0;

            for (String name : names) {
                if (i < totalMax) {
                    i += 1;
                    List<String> placeList = new ArrayList<>();
                    placeList.add("images/file.png");
                    placeList.add("images/outline.png");

                    Main.placeImage(placeList,
                            "full button",
                            new Vector2(0, 0),
                            Main.mainStage,
                            (o) -> {
                                openFile(name);
                                if (Main.placeData.get(name) != null) {
                                    Main.fileData.put(name, Main.placeData.get(name) + 1);
                                } else {
                                    Main.fileData.put(name, 0);
                                }
                            }, null, null,
                            true, true, false, fileTable, true, true, name);

                    index += 1;
                    if (index >= maxByLine) {
                        fileTable.row();
                        index = 0;

                    }
                }
            }
        } catch (

        Exception e) {
            Gdx.app.error("placeFileChooserButton", " -Error " + e);
        } finally {
        }

    }

    public static void render() {
    }
}