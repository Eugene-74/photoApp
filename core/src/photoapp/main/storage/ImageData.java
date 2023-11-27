package photoapp.main.storage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import photoapp.main.Main;

public class ImageData {
    public final static String SAVE_PATH = System.getenv("APPDATA").replace("\\", "/") + "/.photoApp/save.csv";
    public final static String PEOPLE_SAVE_PATH = System.getenv("APPDATA").replace("\\", "/") + "/.photoApp/people.csv";
    public final static String PLACE_SAVE_PATH = System.getenv("APPDATA").replace("\\", "/") + "/.photoApp/place.csv";

    public final static String IMAGE_PATH = System.getenv("APPDATA").replace("\\", "/") + "/.photoApp/userImages";
    public final static String PEOPLE_IMAGE_PATH = System.getenv("APPDATA").replace("\\", "/")
            + "/.photoApp/userImages/peoples";
    public final static String PLACE_IMAGE_PATH = System.getenv("APPDATA").replace("\\", "/")
            + "/.photoApp/userImages/places";

    private Map<String, Object> data;

    public ImageData() {
        data = new HashMap<>(6);
    }

    public ImageData setRotation(Integer rota) {
        if (data != null) {

            data.put("rotation", rota);
        }
        return this;
    }

    public Integer getRotation() {
        if (data != null) {

            if (data.get("rotation") != null) {
                return Integer.parseInt(data.get("rotation").toString());

            }
        }
        return 0;

    }

    public ImageData setName(String name) {
        if (data != null) {

            if (name != "" && name != null) {
                data.put("name", name);
            } else {
                data.put("name", "error in the name");

            }
        }
        return this;
    }

    public String getName() {
        if (data != null) {
            if (data.get("name") != null) {
                return (String) data.get("name");
            }
        }
        return "";
    }

    public ImageData setDate(String date) {
        if (data != null) {

            if (date != "" && date != null) {
                data.put("date", date);
            } else {
                data.put("date", " ");
                // date of now !!!!!!!!
            }
        }
        return this;
    }

    public String getDate() {
        if (data != null) {
            if (data.get("date") != null) {
                return (String) data.get("date");
            }
        }
        return null;

    }

    public ImageData setPlaces(List<String> places) {
        if (data != null) {

            if (!places.isEmpty()) {
                data.put("places", places);
            } else {
                data.put("places", "");
            }
        }
        return this;
    }

    public List<String> getPlaces() {
        if (data != null) {

            if (data.get("places") != null && !data.get("places").equals("")) {
                return (List<String>) data.get("places");
            }
        }
        return null;

    }

    public Boolean isInPlaces(String placeLookingFor) {
        if (data != null) {

            if (data != null) {
                if (data.get("places") != null) {
                    for (String place : (List<String>) data.get("places")) {
                        if (place.equals(placeLookingFor)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;

    }

    public ImageData setPeoples(List<String> peoples) {
        if (data != null) {

            if (!peoples.isEmpty()) {
                data.put("peoples", peoples);
            } else {
                data.put("peoples", "");
            }
        }
        return this;

    }

    public List<String> getPeoples() {
        if (data != null) {

            if (data.get("peoples") != null && !data.get("peoples").equals("")) {
                return (List<String>) data.get("peoples");
            }
        }

        return null;

    }

    public Boolean isInPeoples(String peopleLookingFor) {
        if (data != null) {
            if (data.get("peoples") != null) {
                for (String people : (List<String>) data.get("peoples")) {
                    if (people.equals(peopleLookingFor)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public ImageData setCoords(String coords) {
        if (data != null) {

            if (coords != "" && coords != null) {
                data.put("coords", coords);
            } else {
                data.put("coords", "");
            }
        }
        return this;
    }

    public String getCoords() {
        if (data != null) {
            return (String) data.get("coords");
        }
        return "";
    }

    public ImageData setLoved(Boolean loved) {
        if (data != null) {

            if (loved == true) {
                data.put("loved", true);
            } else {
                data.put("loved", false);
            }
        }
        return this;
    }

    public Boolean getLoved() {
        if (data != null) {

            return (Boolean) data.get("loved");
        }
        return false;
    }

    @Override
    public String toString() {
        String s = "";
        if (data.get("name") != null) {
            s += "Name    : ";
            s += data.get("name");
        } else {
            s += "\nError when loading image name X/";
        }
        if (data.get("date") != null) {
            s += "\ndate    : ";
            s += data.get("date");
        } else {
            s += "\nError when loading date X/";
        }
        if (data.get("rotation") != null) {
            s += "\nrotation   : ";
            s += data.get("rotation");
        }
        if (data.get("peoples") != null) {
            s += "\npeoples : ";
            s += data.get("peoples");
        }
        if (data.get("places") != null) {
            s += "\nplaces  : ";
            s += data.get("places");
        }
        if (data.get("coords") != null) {
            s += "\ncoords  : ";
            s += data.get("coords");
        }
        if (data.get("loved") != null) {
            s += "\nloved   : ";
            s += data.get("loved");
        }
        s += "\n";
        return s;
    }

    public static void openDataOfImages() {
        FileHandle handle = Gdx.files.absolute(SAVE_PATH);

        if (!handle.exists()) {
            return;
        } else {
            InputStream infos = handle.read();
            String infosString = new BufferedReader(new InputStreamReader(infos))
                    .lines().collect(Collectors.joining("\n"));
            if (infosString.equals("") || infosString.equals("\n")) {
                return;
            }
            String[] imagesInfo = infosString.split("\n");
            for (String imageInfo : imagesInfo) {

                String[] category = imageInfo.split(";");

                ImageData imageData = new ImageData()
                        .setName(category[0])
                        .setDate(category[1])
                        .setRotation(Integer.parseInt(category[2]))
                        .setPeoples(List.of(category[3].split(",")))
                        .setPlaces(List.of(category[4].split(",")))
                        .setCoords(category[5])
                        .setLoved(Boolean.parseBoolean(category[6]));

                Main.addImageData(imageData);

            }

        }

    }

    public static void saveImagesData() {
        sortImageData(Main.imagesData);
        String s = "";
        for (ImageData imageData : Main.imagesData) {
            s += imageData.toFileLine();
        }
        FileHandle handle = Gdx.files.absolute(SAVE_PATH);
        InputStream text = new ByteArrayInputStream(s.getBytes());
        handle.write(text, false);
    }

    public static void sortImageData(List<ImageData> imagesData) {
        if (Main.imagesData != null && !Main.imagesData.isEmpty()) {

            imagesData.sort((ImageData imageData1, ImageData imageData2) -> {

                try {
                    String date1;
                    String date2;
                    if (imageData1.getDate() == null) {
                        date1 = "0";
                    } else {

                        date1 = "";
                        String[] dateSplit1 = imageData1.getDate().split(" ");
                        String[] daySplit1 = dateSplit1[0].split(":");
                        String[] hourSplit1 = dateSplit1[1].split(":");
                        date1 += daySplit1[0] + "" + daySplit1[1]
                                + "" + daySplit1[2];
                        date1 += hourSplit1[0] + "" + hourSplit1[1]
                                + "" + hourSplit1[2];
                    }
                    if (imageData2.getDate() == null) {
                        date2 = "0";
                    } else {
                        date2 = "";
                        String[] dateSplit2 = imageData2.getDate().split(" ");
                        String[] daySplit2 = dateSplit2[0].split(":");
                        String[] hourSplit2 = dateSplit2[1].split(":");
                        date2 += daySplit2[0] + "" + daySplit2[1]
                                + "" + daySplit2[2];
                        date2 += hourSplit2[0] + "" + hourSplit2[1]
                                + "" + hourSplit2[2];
                    }

                    if (Long.parseLong(date1) > Long.parseLong(date2)) {
                        // System.out.println(1);
                        return -1;
                    } else {
                        // System.out.println(-1);
                        return 1;
                    }
                } catch (Exception e) {
                    System.err.println("bug when loading date : " + e);
                } finally {
                }
                return 0;
            });
        }
    }

    public String toFileLine() {
        String s = "";
        s += data.get("name");
        s += ";";
        s += data.get("date");
        s += ";";
        s += data.get("rotation");
        s += ";";
        if (data.get("peoples") == null) {
            s += data.get("peoples");
        } else {
            for (String people : (List<String>) data.get("peoples")) {
                s += people + ",";
            }
        }
        s += ";";
        if (data.get("places") == null) {
            s += data.get("places");
        } else {
            for (String place : (List<String>) data.get("places")) {
                s += place + ",";
            }
        }
        s += ";";
        s += data.get("coords");
        s += ";";
        s += data.get("loved");
        s += ";\n";
        return s;
    }

    public boolean equals(Object o) {
        if (o instanceof ImageData id) {
            return getName().equals(id.getName());
        }
        return false;
    }

    public static ImageData getImageDataIfExist(String imagePath) {
        return Main.imagesData.stream().filter(i -> i.getName().equals(imagePath)).findFirst()
                .orElse(new ImageData());
    }

}
