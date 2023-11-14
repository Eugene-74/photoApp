package photoapp.main.windows;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import photoapp.main.Main;
import photoapp.main.graphicelements.MixOfImage;

public class Parameter {
    static Table mainTableParameter;
    static Table parameterTable;

    public static void create() {
        createMainTable();
        createParameterTable();
    }

    public static void open() {
        placeParameterButton();
        placeMainParameterButton();
    }

    public static void reload() {
        parameterTable.clear();
        placeParameterButton();

    }

    public static void clear() {

        MixOfImage.stopLoading();
        mainTableParameter.clear();
        parameterTable.clear();

    }

    public static void placeMainParameterButton() {
        Main.placeImage(List.of("images/back.png", "images/outline.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    Main.toReload = "File Chooser";
                    clear();
                    FileChooser.open();

                }, null, null,
                true, true, false, mainTableParameter, true);
    }

    public static void placeParameterButton() {
        List<String> infoIsOnList = new ArrayList<String>();
        infoIsOnList.add("images/infoIsOn.png");
        infoIsOnList.add("images/outline.png");
        if (Main.preferences.getBoolean("infoIsOn", true)) {
            infoIsOnList.add("images/yes.png");
        } else {
            infoIsOnList.add("images/no.png");

        }

        Main.placeImage(infoIsOnList, "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    if (Main.preferences.getBoolean("infoIsOn", true)) {
                        Main.preferences.putBoolean("infoIsOn", false);
                        reload();
                    } else {
                        Main.preferences.putBoolean("infoIsOn", true);
                        reload();

                    }
                    Main.preferences.flush();
                }, null, null,
                true, true, false, parameterTable, true);

        List<String> darkmodeList = new ArrayList<String>();
        darkmodeList.add("images/mode.png");
        darkmodeList.add("images/outline.png");
        if (Main.preferences.getBoolean("option darkmode", false)) {
            darkmodeList.add("images/yes.png");
        } else {
            darkmodeList.add("images/no.png");

        }

        Main.placeImage(darkmodeList, "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    if (Main.preferences.getBoolean("option darkmode", true)) {
                        Main.preferences.putBoolean("option darkmode", false);
                        Main.darkMode = false;
                        reload();
                    } else {
                        Main.preferences.putBoolean("option darkmode", true);
                        Main.preferences.putBoolean("option brightmode", false);
                        Main.darkMode = true;
                        Main.brightMode = false;
                        reload();

                    }
                    Main.preferences.flush();
                }, null, null,
                true, true, false, parameterTable, true);

        List<String> brightmodeList = new ArrayList<String>();
        brightmodeList.add("images/mode.png");
        brightmodeList.add("images/outline.png");
        if (Main.preferences.getBoolean("option brightmode", false)) {
            brightmodeList.add("images/yes.png");
        } else {
            brightmodeList.add("images/no.png");

        }

        Main.placeImage(brightmodeList, "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    if (Main.preferences.getBoolean("option brightmode", true)) {
                        Main.preferences.putBoolean("option brightmode", false);
                        Main.brightMode = false;
                        reload();
                    } else {
                        Main.preferences.putBoolean("option brightmode", true);
                        Main.preferences.putBoolean("option darkmode", false);
                        Main.brightMode = true;
                        Main.darkMode = false;
                        reload();

                    }
                    Main.preferences.flush();
                }, null, null,
                true, true, false, parameterTable, true);

    }

    public static void createMainTable() {
        mainTableParameter = new Table();
        mainTableParameter.setSize(
                Gdx.graphics.getWidth() - Main.preferences.getInteger("size of main images width")
                        - Main.preferences.getInteger("border") * 3,
                Gdx.graphics.getHeight() - Main.preferences.getInteger("border") * 2);
        mainTableParameter.setPosition(
                Main.preferences.getInteger("size of main images width") + Main.preferences.getInteger("border") * 2,
                Main.preferences.getInteger("border"));

        Main.mainStage.addActor(mainTableParameter);
    }

    private static void createParameterTable() {
        parameterTable = new Table();
        parameterTable.setSize(
                Main.preferences.getInteger("size of main images width"),
                Main.preferences.getInteger("size of main images height"));
        parameterTable.setPosition(
                Main.preferences.getInteger("border"),
                Gdx.graphics.getHeight() - Main.preferences.getInteger("border") - parameterTable.getHeight());

        Main.mainStage.addActor(parameterTable);

    }

}
