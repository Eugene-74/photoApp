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

    public ImageData setName(String name) {
        data.put("name", name);
        return this;
    }

    public String getName() {
        return (String) data.get("name");
    }

    public ImageData setDate(String date) {
        data.put("date", date);
        return this;
    }

    public String getDate() {
        return (String) data.get("date");
    }

    public ImageData setPlaces(List<String> places) {
        data.put("places", places);
        return this;
    }

    public List<String> getPlaces() {
        return (List<String>) data.get("places");
    }

    public Boolean isInPlaces(String placeLookingFor) {
        for (String place : (List<String>) data.get("places")) {
            if (place.equals(placeLookingFor)) {
                return true;
            }
        }
        return false;
    }

    public ImageData setPeoples(List<String> peoples) {
        data.put("peoples", peoples);
        return this;

    }

    public List<String> getPeoples() {
        return (List<String>) data.get("peoples");
    }

    public Boolean isInPeoples(String peopleLookingFor) {
        if (data != null) {
            for (String people : (List<String>) data.get("peoples")) {
                if (people.equals(peopleLookingFor)) {
                    return true;
                }
            }
            return false;

        } else {
            return null;
        }
    }

    public ImageData setCoords(List<Integer> coords) {
        data.put("coords", coords);
        return this;
    }

    public List<String> getCoords() {
        return (List<String>) data.get("coords");
    }

    public ImageData setLoved(Boolean loved) {
        data.put("loved", loved);
        return this;
    }

    public Boolean getLoved() {
        return (Boolean) data.get("loved");
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
                List<Integer> coords = List.of();
                if (!category[4].equals("")) {
                    for (String co : category[4].split(",")) {
                        coords.add(Integer.parseInt(co));
                    }
                }
                ImageData imageData = new ImageData()
                        .setName(category[0])
                        .setDate(category[1])
                        .setPeoples(List.of(category[2].split(",")))
                        .setPlaces(List.of(category[3].split(",")))
                        .setCoords(coords)
                        .setLoved(Boolean.parseBoolean(category[5]));

                Main.addImageData(imageData);

            }

        }

    }

    public static void saveImagesData() {

        String s = "";
        for (ImageData imageData : Main.imagesData) {
            s += imageData.toFileLine();
        }
        FileHandle handle = Gdx.files.absolute(SAVE_PATH);
        InputStream text = new ByteArrayInputStream(s.getBytes());
        handle.write(text, false);
    }

    public String toFileLine() {
        String s = "";
        s += data.get("name");
        s += ";";
        s += data.get("date");
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
        if (data.get("coords") == null) {
            s += data.get("coords");
        } else {
            List<Integer> coords = (List<Integer>) data.get("coords");
            for (int coord : coords) {
            }
        }
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
