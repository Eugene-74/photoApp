package photoapp.main.windows;

import java.util.List;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import photoapp.main.Main;

public class EnterValue {
    static TextField txtValue;
    public static Table validationTable;
    public static Table textTable;
    public static Label label;

    public static void enterAValue(final Consumer<Object> after, String name) {
        Main.windowOpen = "EnterValue";

        open(name, after);

    }

    private static void open(String name, final Consumer<Object> after) {
        // TODO the 2 text are on each other
        label.setText(name);

        txtValue.setText("name");
        txtValue.setSize(500, 50);

        textTable.addActor(txtValue);
        textTable.addActor(label);
        label.setAlignment(Align.center);
        label.setPosition(0, 0);

        txtValue.setAlignment(Align.center);
        txtValue.setPosition(-txtValue.getWidth() / 2, -50);

        Main.mainStage.addActor(textTable);
        Main.mainStage.addActor(validationTable);
        Main.placeImage(List.of("images/selected.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    clear();
                    if (after != null) {
                        String value = txtValue.getText();
                        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
                        value = value.replaceAll(characterFilter, "");
                        after.accept(value);
                    }
                }, null, null,
                true, true, false, validationTable, true, true, "validate");
    }

    public static void clear() {
        validationTable.clear();
        textTable.clear();

    }

    public static void createValueTable() {

        validationTable = new Table();
        validationTable.setSize(Main.graphic.getInteger("size of basic button", 150),
                Main.graphic.getInteger("size of basic button", 150));
        validationTable.setPosition(
                Gdx.graphics.getWidth() - Main.graphic.getInteger("border", 25)
                        - Main.graphic.getInteger("size of basic button", 150),
                Main.graphic.getInteger("border", 25));

        textTable = new Table();
        textTable.setSize(Main.graphic.getInteger("size of main image width", 1200) / 4,
                Main.graphic.getInteger("size of main image height", 800) / 4);
        textTable.setPosition(
                Gdx.graphics.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);

        // BitmapFont myFont = new
        // BitmapFont(Gdx.files.internal("bitmapfont/dominican.fnt"));
        // Main.label1Style.font = myFont;
        // Main.label1Style.fontColor = Color.RED;
        txtValue = new TextField("trying", Main.skinTextField);
        label = new Label(" ", Main.skinTextField);

    }

    public static void create() {
        createValueTable();
    }
}
