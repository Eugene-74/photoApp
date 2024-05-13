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
        Main.windowOpen = "Parameter";
        placeParameterButton();
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

    public static void placeParameterButton() {
        List<String> infoIsOnList = new ArrayList<String>();
        infoIsOnList.add("images/infoIsOn.png");
        infoIsOnList.add("images/outline.png");
        if (Main.graphic.getBoolean("infoIsOn", true)) {
            infoIsOnList.add("images/yes.png");
        } else {
            infoIsOnList.add("images/no.png");

        }

        Main.placeImage(infoIsOnList, "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    if (Main.graphic.getBoolean("infoIsOn", true)) {
                        Main.graphic.putBoolean("infoIsOn", false);
                        reload();
                    } else {
                        Main.graphic.putBoolean("infoIsOn", true);
                        reload();

                    }
                    Main.graphic.flush();
                }, null, null,
                true, true, false, parameterTable, true, true, "info");

        List<String> darkmodeList = new ArrayList<String>();
        darkmodeList.add("images/mode.png");
        darkmodeList.add("images/outline.png");
        if (Main.graphic.getBoolean("option darkmode", false)) {
            darkmodeList.add("images/yes.png");
        } else {
            darkmodeList.add("images/no.png");

        }

        Main.placeImage(darkmodeList, "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    if (Main.graphic.getBoolean("option darkmode", true)) {
                        Main.graphic.putBoolean("option darkmode", false);
                        Main.darkMode = false;
                        Main.iniImage();

                        reload();
                    } else {
                        Main.graphic.putBoolean("option darkmode", true);
                        Main.graphic.putBoolean("option brightmode", false);
                        Main.darkMode = true;
                        Main.brightMode = false;
                        Main.iniImage();

                        reload();

                    }
                    Main.graphic.flush();
                }, null, null,
                true, true, false, parameterTable, true, true, "dark mode");

        // List<String> brightmodeList = new ArrayList<String>();
        // brightmodeList.add("images/mode.png");
        // brightmodeList.add("images/outline.png");
        // if (Main.graphic.getBoolean("option brightmode", false)) {
        // brightmodeList.add("images/yes.png");
        // } else {
        // brightmodeList.add("images/no.png");

        // }

        // Main.placeImage(brightmodeList, "basic button",
        // new Vector2(0, 0),
        // Main.mainStage,
        // (o) -> {
        // if (Main.graphic.getBoolean("option brightmode", true)) {
        // Main.graphic.putBoolean("option brightmode", false);
        // Main.brightMode = false;
        // reload();
        // } else {
        // Main.graphic.putBoolean("option brightmode", true);
        // Main.graphic.putBoolean("option darkmode", false);
        // Main.brightMode = true;
        // Main.darkMode = false;
        // reload();

        // }
        // Main.graphic.flush();
        // }, null, null,
        // true, true, false, parameterTable, true, true, "bright mode");

    }

    public static void createMainTable() {
        mainTableParameter = new Table();
        mainTableParameter.setSize(
                Gdx.graphics.getWidth() - Main.graphic.getInteger("size of main images width")
                        - Main.graphic.getInteger("border") * 3,
                Gdx.graphics.getHeight() - Main.graphic.getInteger("border") * 2);
        mainTableParameter.setPosition(
                Main.graphic.getInteger("size of main images width") + Main.graphic.getInteger("border") * 2,
                Main.graphic.getInteger("border"));

        Main.mainStage.addActor(mainTableParameter);
    }

    private static void createParameterTable() {
        parameterTable = new Table();
        parameterTable.setSize(
                Main.graphic.getInteger("size of main images width"),
                Main.graphic.getInteger("size of main images height"));
        parameterTable.setPosition(
                Main.graphic.getInteger("border"),
                Gdx.graphics.getHeight() - Main.graphic.getInteger("border") - parameterTable.getHeight());

        Main.mainStage.addActor(parameterTable);

    }

}
