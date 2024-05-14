package photoapp.main.storage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import photoapp.main.Main;
import photoapp.main.windows.LoadImage;

public class ImageData {

    // public final static String SAVE = System.getenv("APPDATA").replace("\\", "/") + "/.photoApp";

    public final static String SAVE_PATH = System.getenv("APPDATA").replace("\\", "/") + "/.photoApp/save.csv";
    public final static String PEOPLE_SAVE_PATH = System.getenv("APPDATA").replace("\\", "/") + "/.photoApp/people.csv";
    public final static String PLACE_SAVE_PATH = System.getenv("APPDATA").replace("\\", "/") + "/.photoApp/place.csv";
    public final static String FILE_SAVE_PATH = System.getenv("APPDATA").replace("\\", "/") + "/.photoApp/file.csv";
    public final static String ICON_SAVE_PATH = System.getenv("APPDATA").replace("\\", "/") + "/.photoApp/icon";

    public final static String TEXT_PATH = "text/text-";

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
        try {

            if (data != null) {

                data.put("rotation", rota);
            }
            return this;
        } catch (Exception e) {
            Gdx.app.error("setRotation", e.getStackTrace().toString());
            return null;
        }
    }

    public Integer getRotation() {
        try {
            if (data != null) {

                if (data.get("rotation") != null) {
                    return Integer.parseInt(data.get("rotation").toString());

                }
            }
            return 0;
        } catch (Exception e) {
            Gdx.app.error("getRotation", e.getStackTrace().toString());
            return null;
        }

    }

    public ImageData setName(String name) {
        try {
            if (data != null) {

                if (name != "" && name != null) {
                    data.put("name", name);
                } else {
                    data.put("name", "error in the name");

                }
            }
            return this;
        } catch (Exception e) {
            Gdx.app.error("setName", e.getStackTrace().toString());
            return null;
        }
    }

    public String getName() {
        try {
            if (data != null) {
                if (data.get("name") != null) {
                    return (String) data.get("name");
                }
            }
            return "";
        } catch (Exception e) {
            Gdx.app.error("getName", e.getStackTrace().toString());
            return null;
        }
    }

    public ImageData setDate(String date) {
        try {
            if (data != null) {

                if (date != "" && date != null && !date.equals("null")) {

                    data.put("date", date);
                }

            }
            return this;
        } catch (Exception e) {
            Gdx.app.error("setDate", e.getStackTrace().toString());
            return null;
        }
    }

    public String getDate() {
        try {
            if (data != null) {
                if (data.get("date") != null) {
                    return (String) data.get("date");
                }
            }
            return null;
        } catch (Exception e) {
            Gdx.app.error("getDate", e.getStackTrace().toString());
            return null;
        }

    }

    public ImageData setFiles(List<String> files) {
        try {
            if (data != null) {
                if (files != null) {

                    if (!files.isEmpty()) {
                        data.put("files", files);
                    } else {

                        data.put("files", null);
                    }
                }
                // data.put("places", null);
            }
            return this;
        } catch (Exception e) {
            Gdx.app.error("setFiles", e.getStackTrace().toString());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getFiles() {
        try {
            if (data != null) {

                if (data.get("files") != null) {
                    return (List<String>) data.get("files");
                }
            }
            return null;
        } catch (Exception e) {
            Gdx.app.error("getFiles", e.getStackTrace().toString());
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    public Boolean isInFiles(String fileLookingFor) {
        try {
            if (data != null) {
                if (data.get("files") != null && !data.get("files").equals("")) {
                    for (String file : (List<String>) data.get("files")) {
                        if (file.equals(fileLookingFor)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            Gdx.app.error("isInFiles", e.getStackTrace().toString());
            return null;
        }

    }

    public ImageData setPlaces(List<String> places) {
        try {
            if (data != null) {
                if (places != null) {

                    if (!places.isEmpty()) {
                        data.put("places", places);
                    } else {

                        data.put("places", null);
                    }
                }
                // data.put("places", null);
            }
            return this;
        } catch (Exception e) {
            Gdx.app.error("setPlaces", e.getStackTrace().toString());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getPlaces() {
        try {
            if (data != null) {

                if (data.get("places") != null) {
                    return (List<String>) data.get("places");
                }
            }
            return null;
        } catch (Exception e) {
            Gdx.app.error("getPlaces", e.getStackTrace().toString());
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    public Boolean isInPlaces(String placeLookingFor) {
        try {
            if (data != null) {
                if (data.get("places") != null && !data.get("places").equals("")) {
                    for (String place : (List<String>) data.get("places")) {
                        if (place.equals(placeLookingFor)) {
                            return true;
                        }
                    }
                }

            }
            return false;
        } catch (Exception e) {
            Gdx.app.error("isInPlaces", e.getStackTrace().toString());
            return null;
        }

    }

    public ImageData setPeoples(List<String> peoples) {
        try {
            if (data != null) {
                if (peoples != null) {

                    if (!peoples.isEmpty()) {
                        data.put("peoples", peoples);
                    } else {
                        data.put("peoples", null);
                    }
                }
            }
            return this;
        } catch (Exception e) {
            Gdx.app.error("setPeoples", e.getStackTrace().toString());
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    public List<String> getPeoples() {
        try {
            if (data != null) {

                if (data.get("peoples") != null && !data.get("peoples").equals("")) {
                    return (List<String>) data.get("peoples");
                }
            }

            return null;
        } catch (Exception e) {
            Gdx.app.error("getPeoples", e.getStackTrace().toString());
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    public Boolean isInPeoples(String peopleLookingFor) {
        try {
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
        } catch (Exception e) {
            Gdx.app.error("isInPeoples", e.getStackTrace().toString());
            return null;
        }
    }

    public ImageData setCoords(String coords) {
        try {
            if (data != null) {

                if (coords != "" && coords != null) {
                    // System.err.println("set coord : " + coords);
                    data.put("coords", coords);
                } else {
                    data.put("coords", "");
                }
            }
            return this;
        } catch (Exception e) {
            Gdx.app.error("setCoords", e.getStackTrace().toString());
            return null;
        }
    }

    public String getCoords() {
        try {
            if (data != null) {
                // System.out.println("coooord" + data.get("coords"));
                return (String) data.get("coords");
            }
            return "";
        } catch (Exception e) {
            Gdx.app.error("getCoords", e.getStackTrace().toString());
            return null;
        }
    }

    public ImageData setLoved(Boolean loved) {
        try {
            if (data != null) {
                if (loved == true) {
                    data.put("loved", true);
                } else {
                    data.put("loved", false);
                }
            }
            return this;
        } catch (Exception e) {
            Gdx.app.error("setLoved", e.getStackTrace().toString());
            return null;
        }
    }

    public Boolean getLoved() {
        try {
            if (data != null) {

                return (Boolean) data.get("loved");
            }
            return false;
        } catch (Exception e) {
            Gdx.app.error("getLoved", e.getStackTrace().toString());
            return null;
        }
    }

    @Override
    public String toString() {
        try {
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
        } catch (Exception e) {
            Gdx.app.error("toString", e.getStackTrace().toString());
            return null;
        }
    }

    public static void openDataOfImages(String fileName) {
        try {

            FileHandle handle = Gdx.files.absolute(SAVE_PATH);

            if (!handle.exists()) {
                return;
            } else {
                ArrayList<String> imagesInfo = new ArrayList<String>();
                File f = new File(SAVE_PATH);
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);

                String strng;
                while ((strng = br.readLine()) != null) {
                    imagesInfo.add(strng);
                }

                br.close();

                if (imagesInfo.isEmpty()) {
                    return;
                }
                for (String imageInfo : imagesInfo) {
                    // System.out.println(imageInfo + "info");
                    String[] category = imageInfo.split(";");
                    Integer i = 0;
                    for (String cat : category) {
                        if (cat.equals("null")) {
                            category[i] = null;
                        }
                        i += 1;
                    }
                    List<String> people = null;
                    List<String> place = null;
                    List<String> file = null;

                    if (category[3] != null) {
                        people = new ArrayList<String>();
                        for (String p : category[3].split(",")) {
                            people.add(p);
                        }
                    }
                    if (category[4] != null) {
                        place = new ArrayList<String>();
                        for (String p : category[4].split(",")) {
                            place.add(p);
                        }
                    }
                    if (category[7] != null) {
                        file = new ArrayList<String>();
                        for (String p : category[7].split(",")) {
                            file.add(p);
                        }
                    }

                    ImageData imageData = new ImageData()
                            .setName(category[0])
                            .setDate(category[1])
                            .setRotation(Integer.parseInt(category[2]))
                            .setPeoples(people)
                            .setPlaces(place)
                            .setCoords(category[5])
                            .setLoved(Boolean.parseBoolean(category[6]))
                            .setFiles(file);

                    if (fileName == null || (file != null && file.contains(fileName))) {
                        LoadImage.addImageData(imageData);
                    }

                }

            }
        } catch (Exception e) {
            for (StackTraceElement trace : e.getStackTrace()) {
                Gdx.app.error("openDataOfImages", trace.toString());
            }

        }

    }

    public static void saveImagesData() {
        try {
            String s = "";
            for (ImageData imageData : Main.imagesData) {
                s += imageData.toFileLine();
            }
            FileHandle handle = Gdx.files.absolute(SAVE_PATH);
            InputStream text = new ByteArrayInputStream(s.getBytes());
            handle.write(text, false);
        } catch (Exception e) {
            Gdx.app.error("saveImagesData", e.getStackTrace().toString());
        }
    }

    public static void sortImageData(List<ImageData> imagesData) {
        try {
            if (Main.imagesData != null && !Main.imagesData.isEmpty()) {

                imagesData.sort((ImageData imageData1, ImageData imageData2) -> {

                    try {

                        // if (imageData1.getDate() != null && imageData2.getDate() != null) {
                        String[] daySplit1;
                        String[] hourSplit1;
                        String[] daySplit2;
                        String[] hourSplit2;

                        if (imageData1.getDate() == null) {
                            daySplit1 = new String[] { "0", "0", "0" };
                            hourSplit1 = new String[] { "0", "0", "0" };
                        } else {
                            String[] dateSplit1 = imageData1.getDate().split(" ");
                            daySplit1 = dateSplit1[0].split(":");
                            hourSplit1 = dateSplit1[1].split(":");
                        }

                        if (imageData2.getDate() == null) {
                            daySplit2 = new String[] { "0", "0", "0" };
                            hourSplit2 = new String[] { "0", "0", "0" };
                        } else {
                            String[] dateSplit2 = imageData2.getDate().split(" ");
                            daySplit2 = dateSplit2[0].split(":");
                            hourSplit2 = dateSplit2[1].split(":");
                        }

                        if (compare(Integer.parseInt(daySplit1[0]), Integer.parseInt(daySplit2[0])) == null) {
                            if (compare(Integer.parseInt(daySplit1[1]), Integer.parseInt(daySplit2[1])) == null) {
                                if (compare(Integer.parseInt(daySplit1[2]), Integer.parseInt(daySplit2[2])) == null) {
                                    if (compare(Integer.parseInt(hourSplit2[0]),
                                            Integer.parseInt(hourSplit2[0])) == null) {
                                        if (compare(Integer.parseInt(hourSplit2[1]),
                                                Integer.parseInt(hourSplit2[1])) == null) {
                                            if (compare(Integer.parseInt(hourSplit2[2]),
                                                    Integer.parseInt(hourSplit2[2])) == null) {
                                                return 0;
                                            } else if (compare(Integer.parseInt(hourSplit1[2]),
                                                    Integer.parseInt(hourSplit2[2]))) {
                                                return 1;
                                            } else if (compare(Integer.parseInt(hourSplit2[2]),
                                                    Integer.parseInt(hourSplit1[2]))) {
                                                return -1;
                                            }
                                        } else if (compare(Integer.parseInt(hourSplit1[1]),
                                                Integer.parseInt(hourSplit2[1]))) {
                                            return 1;
                                        } else if (compare(Integer.parseInt(hourSplit2[1]),
                                                Integer.parseInt(hourSplit1[1]))) {
                                            return -1;
                                        }
                                    } else if (compare(Integer.parseInt(hourSplit1[0]),
                                            Integer.parseInt(hourSplit2[0]))) {
                                        return 1;
                                    } else if (compare(Integer.parseInt(hourSplit2[0]),
                                            Integer.parseInt(hourSplit1[0]))) {
                                        return -1;
                                    }
                                } else if (compare(Integer.parseInt(daySplit1[2]), Integer.parseInt(daySplit2[2]))) {
                                    return 1;
                                } else if (compare(Integer.parseInt(daySplit2[2]), Integer.parseInt(daySplit1[2]))) {
                                    return -1;
                                }
                            } else if (compare(Integer.parseInt(daySplit1[1]), Integer.parseInt(daySplit2[1]))) {
                                return 1;
                            } else if (compare(Integer.parseInt(daySplit2[1]), Integer.parseInt(daySplit1[1]))) {
                                return -1;
                            }
                        } else if (compare(Integer.parseInt(daySplit1[0]), Integer.parseInt(daySplit2[0]))) {
                            return 1;
                        } else if (compare(Integer.parseInt(daySplit2[0]), Integer.parseInt(daySplit1[0]))) {
                            return -1;
                        }
                        return 0;

                    } catch (Exception e) {
                        Main.error("bug when loading date : ", e);
                        // System.err.println("bug when loading date : " + e);
                    } finally {
                    }
                    return 0;
                });

            }
        } catch (Exception e) {
            Gdx.app.error("sortImageData", e.getStackTrace().toString());
        }

    }

    public static Boolean compare(Integer nbr1, Integer nbr2) {
        try {
            if (nbr1 > nbr2) {
                return false;
            } else if (nbr1 < nbr2) {
                return true;
            }
            return null;
        } catch (Exception e) {
            Gdx.app.error("compare", e.getStackTrace().toString());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public String toFileLine() {
        try {
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
            s += ";";
            if (data.get("files") == null) {
                s += data.get("files");
            } else {
                for (String file : (List<String>) data.get("files")) {
                    s += file + ",";
                }
            }
            s += ";\n";

            return s;
        } catch (Exception e) {
            Gdx.app.error("toFileLine", e.getStackTrace().toString());
            return null;
        }
    }

    public boolean equals(Object o) {
        try {
            if (o instanceof ImageData id) {
                return getName().equals(id.getName());
            }
            return false;
        } catch (Exception e) {
            Gdx.app.error("equals", e.getStackTrace().toString());
            return false;
        }
    }

    public static ImageData getImageDataIfExist(String imagePath) {
        try {
            return Main.imagesData.stream().filter(i -> i.getName().equals(imagePath)).findFirst()
                    .orElse(new ImageData());
        } catch (Exception e) {
            Gdx.app.error("getImageDataIfExist", e.getStackTrace().toString());
            return null;
        }
    }

}
