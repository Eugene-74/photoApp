package photoapp.main.windows.Parameter;

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
        infoIsOnList.add("images/selected.png");
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

        ParameterButton.createBrightModeButton();

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
