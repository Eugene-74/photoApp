package photoapp.main.windows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import photoapp.main.Main;
import photoapp.main.graphicelements.MixOfImage;
import photoapp.main.storage.ImageData;

public class FileChooser {

    public static Table fileTable;
    public static Table addFileTable;
    public static Table buttonTable;

    public static void create() {

        setPref();
        createFileTable();
        createButtonTable();

    }

    public static void open() {
        Main.windowOpen = "File Chooser";

        Main.mainStage.addActor(fileTable);
        Main.mainStage.addActor(buttonTable);
        placeFileChooserButton();

    }

    public static void reload() {

    }

    public static void clear() {
        fileTable.clear();
        buttonTable.clear();
    }

    public static void createFileTable() {
        fileTable = new Table();

        fileTable.setSize(Main.preferences.getInteger("size of file table width"),
                Main.preferences.getInteger("size of file table height"));

        fileTable.setPosition(
                Main.preferences.getInteger("border"),
                Gdx.graphics.getHeight() - fileTable.getHeight() - Main.preferences.getInteger("border"));
    }

    public static void createButtonTable() {
        buttonTable = new Table();

        buttonTable.setSize(
                Gdx.graphics.getWidth() - Main.preferences.getInteger("size of file table width")
                        - Main.preferences.getInteger("border") * 3,
                Gdx.graphics.getHeight() - Main.preferences.getInteger("border") * 2);
        buttonTable.setPosition(
                Main.preferences.getInteger("size of main images width") + Main.preferences.getInteger("border") * 2,
                Main.preferences.getInteger("border"));
    }

    public static void setPref() {
        Main.preferences.putInteger("size of file table width", 1200);
        Main.preferences.putInteger("size of file table height", 800);
    }

    public static void openFile(String name) {
        if (name.equals("")) {
            MainImages.open();
            clear();
        }
    }

    public static void placeButton() {
        Main.placeImage(List.of("images/addFile.png", "images/outline.png"),
                "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    addAFile();
                }, null, null,
                true, true, false, buttonTable, true);

        buttonTable.row();

        Main.placeImage(List.of("images/openParameter.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    Parameter.open();
                    clear();
                }, null, null,
                true, true, false, buttonTable, true);
    }

    public static void addAFile() {

        MixOfImage.stopLoading();
        addFileTable = new Table();
        addFileTable.setPosition(0, 0);
        addFileTable.setSize(100, 100);

        Main.mainStage.addActor(addFileTable);

    }

    public static void placeFileChooserButton() {

        placeButton();

        Integer maxByLine = 3;
        Integer index = 0;
        List<String> names = new ArrayList<String>();

        File handle = new File(ImageData.IMAGE_PATH);

        Main.placeImage(List.of("images/allFile.png", "images/outline.png"),
                "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    openFile("");
                }, null, null,
                true, true, false, fileTable, true);

        for (File name : handle.listFiles()) {
            if (name.isDirectory()) {

                if (!name.getName().equals("150")
                        && !name.getName().equals("bin")
                        && !name.getName().equals("peoples")
                        && !name.getName().equals("places")) {

                    names.add(name.toString());
                }
            }
        }

        if (!handle.exists()) {
            handle.mkdirs();
        }
        Integer totalMax = 5;
        Integer i = 0;

        for (String name : names) {
            if (i < totalMax) {
                i += 1;
                List<String> placeList = new ArrayList<>();
                placeList.add("images/file.png");
                placeList.add("images/outline.png");

                Main.placeImage(placeList,
                        "basic button",
                        new Vector2(0, 0),
                        Main.mainStage,
                        (o) -> {
                            openFile(name);
                        }, null, null,
                        true, true, false, fileTable, true);

                index += 1;
                if (index >= maxByLine) {
                    fileTable.row();
                    index = 0;

                }
            }
        }

    }
}