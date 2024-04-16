package photoapp.main.windows;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import photoapp.main.Main;
import photoapp.main.storage.ImageData;

public class LocationEdition {
    static Table mainTable, validationTable, textTable;
    static Label label;
    static TextField latitude1, latitude2, latitude3, latitude4, longitude1, longitude2, longitude3, longitude4;
    static ArrayList<String> date = new ArrayList<String>();
    static boolean isLatitude1, isLatitude2, isLatitude3, isLatitude4, isLongitude1, isLongitude2, isLongitude3,
            isLongitude4;

    public static void create() {
        mainTable = new Table();
        mainTable.setSize(100, 100);

        mainTable.setPosition(0, 0);

        Main.mainStage.addActor(mainTable);

        validationTable = new Table();
        validationTable.setSize(Main.graphic.getInteger("size of basic button", 150),
                Main.graphic.getInteger("size of basic button", 150));
        validationTable.setPosition(
                Gdx.graphics.getWidth() - Main.graphic.getInteger("border", 25)
                        - Main.graphic.getInteger("size of basic button", 150),
                Main.graphic.getInteger("border", 25));

        textTable = new Table();
        textTable.setSize(200,
                200);
        textTable.setPosition(
                Gdx.graphics.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);

        latitude1 = new TextField("00", Main.skinTextField);
        latitude2 = new TextField("00", Main.skinTextField);
        latitude3 = new TextField("00,00", Main.skinTextField);
        latitude4 = new TextField("S", Main.skinTextField);
        longitude1 = new TextField("00", Main.skinTextField);
        longitude2 = new TextField("00", Main.skinTextField);
        longitude3 = new TextField("00,00", Main.skinTextField);
        longitude4 = new TextField("W", Main.skinTextField);

        label = new Label(" ", Main.skinTextField);
    }

    public static void open() {
        Main.clear();
        Main.windowOpen = "LocationEdition";

        // CommonButton.createBack(mainTable);

        label.setText("location : ");
        ImageData imageData = ImageData.getImageDataIfExist(ImageEdition.theCurrentImageName);
        // 48° 50' 54,97"_N:2° 24' 19,24"_E

        Boolean minusLat = false;
        Boolean minusLon = false;
        String coord = imageData.getCoords();
        // System.out.println("ccord : " + coord);

        String[] latt;
        String[] lonn;

        if (!coord.equals("")) {

            String[] coords = coord.split(":");

            latt = coords[0].split("_");
            if (coords[0].endsWith("S")) {
                minusLat = true;
            }
            String[] latitude = latt[0].replace("-", "").replace("°", "").replace("'", "")
                    .replace("\"", "").replace(",", ".").split(" ");
            // Float lat = Math.abs(Float.parseFloat(latitude[0])) +
            // Math.abs(Float.parseFloat(latitude[1]) / 60)
            // + Math.abs(Float.parseFloat(latitude[2]) / 3600);

            if (coords[1].endsWith("W")) {
                minusLon = true;
            }
            lonn = coords[1].split("_");
            String[] longitude = lonn[0].replace("-", "").replace("°", "").replace("'", "")
                    .replace("\"", "").replace(",", ".").split(" ");
            // Float lon = Math.abs(Float.parseFloat(longitude[0]))
            // + Math.abs(Float.parseFloat(longitude[1]) / 60)
            // + Math.abs(Float.parseFloat(longitude[2]) / 3600);

            String latLettre = "";
            if (minusLat) {
                latLettre = "S";
                // lat = -lat;
            } else {
                latLettre = "N";
            }
            // String coo = latitude[0] + "%C2%B0" + latitude[1] + "'" + latitude[2] + "%22"
            // + lettre;
            String lonLettre = "";

            if (minusLon) {
                lonLettre = "W";
                // lon = -lon;
            } else {
                lonLettre = "E";
            }
            // coo += longitude[0] + "%C2%B0" + longitude[1] + "'" + longitude[2] + "%22" +
            // lettre;
            latitude1.setText(latitude[0]);
            latitude1.setSize(Main.graphic.getInteger("size of main images button"), 50);
            Main.setTip("latitude1", latitude1);

            latitude2.setText(latitude[1]);
            latitude2.setSize(Main.graphic.getInteger("size of main images button"), 50);
            Main.setTip("latitude2", latitude2);

            latitude3.setText(latitude[2].replace(".", ","));
            latitude3.setSize(Main.graphic.getInteger("size of main images button"), 50);
            Main.setTip("latitude3", latitude3);

            latitude4.setText(latLettre);
            latitude4.setSize(Main.graphic.getInteger("size of main images button"), 50);
            Main.setTip("latitude4", latitude4);

            longitude1.setText(longitude[0]);
            longitude1.setSize(Main.graphic.getInteger("size of main images button"), 50);
            Main.setTip("longitude1", longitude1);

            longitude2.setText(longitude[1]);
            longitude2.setSize(Main.graphic.getInteger("size of main images button"), 50);
            Main.setTip("longitude2", longitude2);

            longitude3.setText(longitude[2].replace(".", ","));
            longitude3.setSize(Main.graphic.getInteger("size of main images button"), 50);
            Main.setTip("longitude3", longitude3);

            longitude4.setText(lonLettre);
            longitude4.setSize(Main.graphic.getInteger("size of main images button"), 50);
            Main.setTip("longitude4", longitude4);
        }

        textTable.addActor(latitude1);
        textTable.addActor(latitude2);
        textTable.addActor(latitude3);
        textTable.addActor(latitude4);
        textTable.addActor(longitude1);
        textTable.addActor(longitude2);
        textTable.addActor(longitude3);
        textTable.addActor(longitude4);

        textTable.addActor(label);
        label.setAlignment(Align.center);
        label.setPosition(0, 0);

        latitude1.setAlignment(Align.center);
        latitude1.setPosition(-Main.graphic.getInteger("size of main images button") / 2
                - Main.graphic.getInteger("size of main images button") * 4, -50);

        latitude2.setAlignment(Align.center);
        latitude2.setPosition(-Main.graphic.getInteger("size of main images button") / 2
                - Main.graphic.getInteger("size of main images button") * 3, -50);

        latitude3.setAlignment(Align.center);
        latitude3.setPosition(-Main.graphic.getInteger("size of main images button") / 2
                - Main.graphic.getInteger("size of main images button") * 2, -50);

        latitude4.setAlignment(Align.center);
        latitude4.setPosition(-Main.graphic.getInteger("size of main images button") / 2
                - Main.graphic.getInteger("size of main images button") * 1, -50);

        longitude1.setAlignment(Align.center);
        longitude1.setPosition(Main.graphic.getInteger("size of main images button") / 2
                + Main.graphic.getInteger("size of main images button"), -50);

        longitude2.setAlignment(Align.center);
        longitude2.setPosition(Main.graphic.getInteger("size of main images button") / 2
                + Main.graphic.getInteger("size of main images button") * 2, -50);
        longitude3.setAlignment(Align.center);
        longitude3.setPosition(Main.graphic.getInteger("size of main images button") / 2
                + Main.graphic.getInteger("size of main images button") * 3, -50);
        longitude4.setAlignment(Align.center);
        longitude4.setPosition(Main.graphic.getInteger("size of main images button") / 2
                + Main.graphic.getInteger("size of main images button") * 4, -50);

        Main.mainStage.addActor(textTable);
        Main.mainStage.addActor(validationTable);
        Main.placeImage(java.util.List.of("images/selected.png"), "basic button",
                new Vector2(0, 0),
                Main.mainStage,
                (o) -> {
                    try {
                        Integer.parseInt(latitude1.getText());
                        if (latitude1.getText().length() != 2 && latitude1.getText().length() != 1) {
                            latitude1.setText("00");

                        } else {
                            isLatitude1 = true;
                        }
                    } catch (Exception e) {
                        System.out.println("err 1");
                        latitude1.setText("00");
                    }
                    try {
                        Integer.parseInt(latitude2.getText());
                        if (latitude2.getText().length() != 2 && latitude2.getText().length() != 1) {
                            latitude2.setText("00");

                        } else {
                            isLatitude2 = true;
                        }
                    } catch (Exception e) {
                        System.out.println("err 2");

                        latitude2.setText("00");
                    }

                    try {
                        Integer.parseInt(latitude3.getText().split(",")[0]);
                        Integer.parseInt(latitude3.getText().split(",")[1]);

                        if (latitude3.getText().length() < 1 || latitude3.getText().length() > 6) {

                            latitude3.setText("00,000");

                        } else {
                            isLatitude3 = true;
                        }
                    } catch (Exception e) {
                        System.out.println("err 3");

                        latitude3.setText("00,000");
                    }
                    try {
                        // Integer.parseInt(latitude4.getText());
                        if (!latitude4.getText().equals("S") && !latitude4.getText().equals("N")) {
                            latitude4.setText("S");

                        } else {
                            isLatitude4 = true;
                        }
                    } catch (Exception e) {
                        System.out.println("err 4");

                        latitude4.setText("S");
                    }
                    try {
                        Integer.parseInt(longitude1.getText());
                        if (longitude1.getText().length() != 2 && longitude1.getText().length() != 1) {
                            longitude1.setText("00");

                        } else {
                            isLongitude1 = true;
                        }
                    } catch (Exception e) {
                        System.out.println("err 5");
                        longitude1.setText("00");
                    }
                    try {
                        Integer.parseInt(longitude2.getText());
                        if (longitude2.getText().length() != 1 && longitude2.getText().length() != 2) {
                            longitude2.setText("00");

                        } else {
                            isLongitude2 = true;
                        }
                    } catch (Exception e) {
                        System.out.println("err 6");

                        longitude2.setText("00");
                    }

                    try {
                        Integer.parseInt(longitude3.getText().split(",")[0]);
                        Integer.parseInt(longitude3.getText().split(",")[1]);
                        if (longitude3.getText().length() < 1 || longitude3.getText().length() > 6) {

                            longitude3.setText("00,000");

                        } else {
                            isLongitude3 = true;
                        }
                    } catch (Exception e) {
                        System.out.println("err 7");

                        longitude3.setText("00,00");
                    }
                    try {
                        // Integer.parseInt(longitude4.getText());
                        if (!longitude4.getText().equals("W") && !longitude4.getText().equals("E")) {
                            longitude4.setText("W");

                        } else {
                            isLongitude4 = true;
                        }
                    } catch (Exception e) {
                        System.out.println("err 8");

                        longitude4.setText("W");
                    }

                    if (isLatitude1 && isLatitude2 && isLatitude3 && isLatitude4 && isLongitude1 && isLongitude2
                            && isLongitude3 && isLongitude4) {
                        System.out.println("good");
                        String lat = latitude1.getText() + "° ";
                        lat += latitude2.getText() + "' ";
                        lat += latitude3.getText() + "\"" + "_";
                        lat += latitude4.getText();
                        String lon = longitude1.getText() + "° ";
                        lon += longitude2.getText() + "' ";
                        lon += longitude3.getText() + "\"" + "_";
                        lon += longitude4.getText();

                        String coords = lat + ":" + lon;
                        ImageData.getImageDataIfExist(ImageEdition.theCurrentImageName)
                                .setCoords(coords);
                        clear();
                        ImageData.saveImagesData();
                        ImageEdition.open(ImageEdition.theCurrentImageName, true);

                    } else {
                        Main.infoTextSet(Main.graphic.getString("text error loaction"), true);

                    }

                }, null, null,
                true, true, false, validationTable, true, true, "validate");

    }

    public static void reload() {

    }

    public static void load() {

    }

    public static void clear() {
        mainTable.clear();
        validationTable.clear();
        textTable.clear();

    }
}
