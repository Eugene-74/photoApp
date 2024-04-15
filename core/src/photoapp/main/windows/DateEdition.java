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

public class DateEdition {
        static Table mainTable, validationTable, textTable;
        static Label label;
        static TextField day, month, year, hour, min, s;
        static ArrayList<String> date = new ArrayList<String>();
        static boolean isYear, isMonth, isDay, isHour, isMin, isS;

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

                day = new TextField("00", Main.skinTextField);
                month = new TextField("00", Main.skinTextField);
                year = new TextField("0000", Main.skinTextField);
                hour = new TextField("00", Main.skinTextField);
                min = new TextField("00", Main.skinTextField);
                s = new TextField("00", Main.skinTextField);
                label = new Label(" ", Main.skinTextField);
        }

        public static void open() {
                Main.clear();
                Main.windowOpen = "DateEdition";

                // CommonButton.createBack(mainTable);

                label.setText("date : ");
                ImageData imageData = ImageData.getImageDataIfExist(ImageEdition.theCurrentImageName);
                String[] dateSplit = imageData.getDate().split(" ");
                String[] daySplit = dateSplit[0].split(":");
                String[] hourSplit = dateSplit[1].split(":");

                day.setText(daySplit[2]);
                day.setSize(Main.graphic.getInteger("size of main images button"), 50);
                Main.setTip("day", day);

                month.setText(daySplit[1]);
                month.setSize(Main.graphic.getInteger("size of main images button"), 50);
                Main.setTip("month", month);

                year.setText(daySplit[0]);
                year.setSize(Main.graphic.getInteger("size of main images button"), 50);
                Main.setTip("year", year);

                hour.setText(hourSplit[0]);
                hour.setSize(Main.graphic.getInteger("size of main images button"), 50);
                Main.setTip("hour", hour);

                min.setText(hourSplit[1]);
                min.setSize(Main.graphic.getInteger("size of main images button"), 50);
                Main.setTip("min", min);

                s.setText(hourSplit[2]);
                s.setSize(Main.graphic.getInteger("size of main images button"), 50);
                Main.setTip("s", s);

                textTable.addActor(day);
                textTable.addActor(month);
                textTable.addActor(year);
                textTable.addActor(hour);
                textTable.addActor(min);
                textTable.addActor(s);

                textTable.addActor(label);
                label.setAlignment(Align.center);
                label.setPosition(0, 0);

                day.setAlignment(Align.center);
                day.setPosition(-Main.graphic.getInteger("size of main images button") / 2
                                - Main.graphic.getInteger("size of main images button") * 2, -50);

                month.setAlignment(Align.center);
                month.setPosition(-Main.graphic.getInteger("size of main images button") / 2
                                - Main.graphic.getInteger("size of main images button"), -50);

                year.setAlignment(Align.center);
                year.setPosition(-Main.graphic.getInteger("size of main images button") / 2, -50);

                hour.setAlignment(Align.center);
                hour.setPosition(Main.graphic.getInteger("size of main images button") / 2, -50);

                min.setAlignment(Align.center);
                min.setPosition(Main.graphic.getInteger("size of main images button") / 2
                                + Main.graphic.getInteger("size of main images button"), -50);

                s.setAlignment(Align.center);
                s.setPosition(Main.graphic.getInteger("size of main images button") / 2
                                + Main.graphic.getInteger("size of main images button") * 2, -50);

                Main.mainStage.addActor(textTable);
                Main.mainStage.addActor(validationTable);
                Main.placeImage(java.util.List.of("images/selected.png"), "basic button",
                                new Vector2(0, 0),
                                Main.mainStage,
                                (o) -> {
                                        try {
                                                Integer.parseInt(year.getText());
                                                if (year.getText().length() != 4) {
                                                        year.setText("0000");

                                                } else {
                                                        isYear = true;
                                                }
                                        } catch (Exception e) {
                                                System.out.println("err");
                                                year.setText("0000");
                                        }
                                        try {
                                                Integer.parseInt(month.getText());
                                                if (month.getText().length() != 2) {
                                                        month.setText("00");

                                                } else {
                                                        isMonth = true;
                                                }
                                        } catch (Exception e) {
                                                System.out.println("err");

                                                month.setText("00");
                                        }

                                        try {
                                                Integer.parseInt(day.getText());
                                                if (day.getText().length() != 2) {

                                                        day.setText("00");

                                                } else {
                                                        isDay = true;
                                                }
                                        } catch (Exception e) {
                                                System.out.println("err");

                                                day.setText("00");
                                        }
                                        try {
                                                Integer.parseInt(hour.getText());
                                                if (hour.getText().length() != 2) {
                                                        hour.setText("00");

                                                } else {
                                                        isHour = true;
                                                }
                                        } catch (Exception e) {
                                                System.out.println("err");

                                                hour.setText("00");
                                        }
                                        try {
                                                Integer.parseInt(min.getText());
                                                if (min.getText().length() != 2) {
                                                        min.setText("00");

                                                } else {
                                                        isMin = true;
                                                }
                                        } catch (Exception e) {
                                                System.out.println("err");

                                                min.setText("00");
                                        }
                                        try {
                                                Integer.parseInt(s.getText());
                                                if (s.getText().length() != 2) {
                                                        s.setText("00");

                                                } else {
                                                        isS = true;
                                                }
                                        } catch (Exception e) {
                                                System.out.println("err");

                                                s.setText("00");
                                        }

                                        if (isYear && isMonth && isDay && isHour && isMin && isS) {
                                                date.add(year.getText());
                                                date.add(month.getText());
                                                date.add(day.getText());
                                                date.add(hour.getText());
                                                date.add(min.getText());
                                                date.add(s.getText());
                                                ImageData.getImageDataIfExist(ImageEdition.theCurrentImageName)
                                                                .setDate((date).get(0) + ":" + (date).get(1) + ":"
                                                                                + (date).get(2) + " " + (date).get(3)
                                                                                + ":" + (date).get(4) + ":"
                                                                                + (date).get(5));
                                                clear();
                                                ImageData.saveImagesData();
                                                ImageEdition.open(ImageEdition.theCurrentImageName, true);

                                        } else {
                                                Main.infoTextSet(Main.graphic.getString("text error date"), true);

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
