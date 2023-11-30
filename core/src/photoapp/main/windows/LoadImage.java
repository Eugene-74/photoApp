package photoapp.main.windows;

import java.io.File;
import java.lang.Thread.State;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import photoapp.main.Main;
import photoapp.main.graphicelements.MixOfImage;
import photoapp.main.storage.ImageData;

public class LoadImage {
    public static Thread thread = null;
    public static Integer numberOfLoadedImages = 0;
    public static Integer numberOfImagesExif = 0;
    public static Integer numberOfImagesLoaded = 0;
    public static Integer numberOfImagesLoadedForTIme = 0;

    public static Integer numberOfImagesToLoad = 0;

    public static Float progress = (float) 0;
    public static Float lastProgress = (float) 0;

    public static ArrayList<String> toLoad = new ArrayList<String>();
    public static ArrayList<String> toRemove = new ArrayList<String>();
    public static ArrayList<String> isLoading = new ArrayList<String>();

    public static Long time = (long) 0;

    public static void open() {
        Main.windowOpen = "LoadImage";

        Main.unLoadAll();
        numberOfImagesExif = 0;
        openFile();
    }

    public static void reload() {
    }

    public static void clear() {
    }

    public static void render() {

        if (numberOfLoadedImages != 0 && numberOfImagesToLoad.equals(numberOfLoadedImages)) {
            numberOfLoadedImages = 0;
            // time = TimeUtils.millis();

            Main.infoText = "All images imported";
            MixOfImage.LoadingList.clear();

            for (String imagePath : toLoad) {
                String[] nameList = imagePath.split("/");
                String imageName = nameList[nameList.length - 1];

                openImageExif(imageName);
                numberOfImagesExif += 1;
                Main.infoText = "exporting data of image : " + numberOfImagesExif + "/" + numberOfImagesToLoad
                        + "(if you import a lot of image it will lag, but just wait)";
            }
            ImageData.saveImagesData();
            ImageData.sortImageData(Main.imagesData);

        } else if (numberOfImagesExif != 0 && numberOfImagesExif > 0) {
            if (numberOfImagesExif.equals(numberOfImagesToLoad)) {
                Main.infoText = "All datas imported";

                numberOfImagesExif = -1;
                for (String imagePath : toLoad) {
                    String[] ListImageName = imagePath.split("/");
                    String fileName150 = ImageData.IMAGE_PATH + "/150/" + ListImageName[ListImageName.length - 1];
                    String fileName10 = ImageData.IMAGE_PATH + "/10/" + ListImageName[ListImageName.length - 1];

                    FileHandle fileHandle150 = new FileHandle(fileName150);
                    FileHandle fileHandle10 = new FileHandle(fileName10);

                    if (fileHandle150.exists() && fileHandle10.exists()) {
                        toRemove.add(imagePath);
                        numberOfImagesLoaded += 1;
                    }

                }

                for (String imagePath : toRemove) {
                    toLoad.remove(imagePath);
                    // if (MixOfImage.manager.isLoaded(imagePath)) {
                    // MixOfImage.manager.unload(imagePath);
                    // }
                }

                for (String imagePath : toLoad) {

                    String[] nameList = imagePath.split("/");
                    String imageName = nameList[nameList.length - 1];
                    // TODO change because with a lot of image it crash (freeze)
                    FileHandle from = Gdx.files.absolute(imagePath);
                    byte[] data = from.readBytes();
                    FileHandle to = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + imageName);
                    if (!to.exists()) {
                        to.writeBytes(data, false);
                    }
                    FileHandle fromJson = Gdx.files.absolute(imagePath + ".json");
                    if (fromJson.exists()) {
                        byte[] dataJson = fromJson.readBytes();

                        FileHandle toJson = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + imageName + ".json");
                        toJson.writeBytes(dataJson, false);
                    }
                }
                if (numberOfImagesLoaded != 0) {

                    Main.infoText = "Creating images : " + numberOfImagesLoaded + "/" + numberOfImagesToLoad
                            + " sould be finish in : "
                            + (((TimeUtils.millis() - time) / numberOfImagesLoaded)
                                    * (numberOfImagesToLoad - numberOfImagesLoaded)) / 1000 / 60
                            + "min";
                }
                toRemove.clear();

            }
            // else {
            // Main.infoText = "exporting data of image : " + numberOfImagesExif + "/" +
            // numberOfImagesToLoad
            // + "(if you import a lot of image it will lag, but just wait)";
            // }
        } else if (numberOfImagesExif == -1) {
            progress = MixOfImage.manager.getProgress();
            if (progress != lastProgress) {
                lastProgress = progress;
                Integer max = Main.preferences.getInteger("image load at the same time");
                if (toLoad.size() < Main.preferences.getInteger("image load at the same time")) {
                    max = toLoad.size();
                }
                for (int i = 0; i < max; i++) {

                    if (isLoading.contains(toLoad.get(i)) && MixOfImage.manager.isLoaded(toLoad.get(i))) {
                        setSizeAfterLoad(toLoad.get(i), 150);
                        setSizeAfterLoad(toLoad.get(i), 10);

                        toRemove.add(toLoad.get(i));
                        numberOfImagesLoaded += 1;
                        numberOfImagesLoadedForTIme += 1;

                    } else if (!isLoading.contains(toLoad.get(i))) {

                        MixOfImage.loadImage(toLoad.get(i));
                        isLoading.add(toLoad.get(i));
                    }
                }

                for (String imagePath : toRemove) {
                    if (MixOfImage.manager.isLoaded(imagePath)) {
                        MixOfImage.manager.unload(imagePath);
                    }
                    toLoad.remove(imagePath);
                }
                toRemove.clear();

                if (numberOfImagesLoadedForTIme != 0) {

                    Main.infoText = "Creating images : " + numberOfImagesLoaded + "/" + numberOfImagesToLoad
                            + " sould be finish in : "
                            + (((TimeUtils.millis() - time) / numberOfImagesLoadedForTIme)
                                    * (numberOfImagesToLoad - numberOfImagesLoadedForTIme)) / 1000 / 60
                            + "min";
                }
            }
            if (numberOfImagesLoaded.equals(numberOfImagesToLoad) && numberOfImagesLoaded != 0) {
                Main.infoText = "All is loaded opening Main images";
                LoadImage.clear();
                MainImages.open();

            }

        }

    }

    public static void openFile() {
        thread = new Thread() {
            @Override
            public void run() {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                JFrame f = new JFrame();
                f.setVisible(true);
                f.toFront();
                f.setVisible(false);
                int res = chooser.showSaveDialog(f);
                f.dispose();
                File fileRessource = null;
                if (res == JFileChooser.APPROVE_OPTION) {
                    if (chooser.getSelectedFile() != null) {
                        fileRessource = chooser.getSelectedFile();
                        if (Main.isAnImage(fileRessource.toString())) {
                            numberOfImagesToLoad = 1;
                            time = TimeUtils.millis();
                            openImageInAFile(fileRessource);
                        } else {
                            time = TimeUtils.millis();
                            countImageToLoad(fileRessource);
                            openImageOfAFile(fileRessource);

                        }

                    }
                    f.dispose();
                } else {
                    clear();
                    MainImages.open();
                }
            }
        };
        thread.start();
    }

    private static void countImageToLoad(File fileRessource) {
        File[] liste = fileRessource.listFiles();
        if (liste != null) {
            for (File item : liste) {

                if (item.isFile()) {
                    if (Main.isAnImage(item.getName())) {
                        numberOfImagesToLoad += 1;
                    }
                } else if (item.isDirectory()) {
                    countImageToLoad(item);
                }
            }
        }
    }

    public static void openImageOfAFile(File dir) {

        File[] liste = dir.listFiles();
        if (liste != null) {
            for (File item : liste) {

                if (item.isFile()) {
                    Main.infoText = "import of images : " + numberOfLoadedImages
                            + "/" + numberOfImagesToLoad;

                    if (Main.isAnImage(item.getName())) {

                        toLoad.add((dir + "/" + item.getName()).replace("\\", "/"));
                        numberOfLoadedImages += 1;
                    }
                } else if (item.isDirectory()) {
                    openImageOfAFile(item);
                }
            }
        }

    }

    public static void openImageInAFile(File dir) {
        FileHandle from = Gdx.files.absolute(dir.toString());
        byte[] data = from.readBytes();

        FileHandle to = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + dir.getName());
        to.writeBytes(data, false);

        FileHandle fromJson = Gdx.files.absolute(dir.toString() + ".json");
        if (fromJson.exists()) {
            byte[] dataJson = fromJson.readBytes();

            FileHandle toJson = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + dir.getName() + ".json");
            toJson.writeBytes(dataJson, false);
        }
        openImageExif(dir.getName());

        setSize(ImageData.IMAGE_PATH + "/" + dir.getName(), dir.getName(), 150, false);
        setSize(ImageData.IMAGE_PATH + "/" + dir.getName(), dir.getName(), 10, false);

    }

    public static void exportImages() {

        Thread threade = new Thread() {
            @Override
            public void run() {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                JFrame f = new JFrame();
                f.setVisible(true);
                f.toFront();
                f.setVisible(false);
                int res = chooser.showSaveDialog(f);
                f.dispose();
                File fileRessource = null;
                if (res == JFileChooser.APPROVE_OPTION) {
                    if (chooser.getSelectedFile() != null) {
                        fileRessource = chooser.getSelectedFile();
                        for (ImageData imageData : Main.imagesData) {
                            String imageName = imageData.getName();

                            FileHandle from = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + imageName);
                            byte[] data = from.readBytes();

                            FileHandle to = Gdx.files.absolute(fileRessource + "/" + imageName);
                            to.writeBytes(data, false);
                        }
                        Main.infoText = "export done";

                    }
                    f.dispose();

                }
            }

        };
        threade.start();

    }

    public static void setSizeAfterLoad(String imagePath, Integer size) {
        String[] nameList = imagePath.split("/");
        String imageName = nameList[nameList.length - 1];

        String[] ListImageName = imageName.split("/");
        String fileName = ImageData.IMAGE_PATH + "/" + size + "/" + ListImageName[ListImageName.length - 1];
        FileHandle fh = new FileHandle(fileName);

        Texture texture = MixOfImage.manager.get(imagePath, Texture.class);

        Pixmap pixmap = resize(textureToPixmap(texture), size, size, true);
        FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + size);

        if (!handle.exists()) {
            handle.mkdirs();
        }

        PixmapIO.writePNG(fh, pixmap);
        pixmap.dispose();

    }

    public static void setSizeForce(String imagePath, Integer size) {
        String[] ListImageName = imagePath.split("/");
        setSize(imagePath, ListImageName[ListImageName.length - 1], size, true);
        setSizeAfterLoad(imagePath, size);
    }

    public static void openImageExif(String imagePath) {
        FileHandle file;
        try {
            file = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + imagePath);
            FileHandle fileJson = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + imagePath + ".json");
            ImageData imageData = ImageData.getImageDataIfExist(imagePath);

            String coords = "";
            Integer rotation = 0;
            if (fileJson.exists()) {
                JsonValue root = new JsonReader().parse(fileJson);
                Boolean favorited = false;
                if (root.has("favorited")) {
                    favorited = root.getBoolean("favorited");
                }
                JsonValue photoTakenTime = root.get("photoTakenTime");
                Long photoTakenTime_timestamp = photoTakenTime.getLong("timestamp");
                JsonValue people = root.get("people");
                ArrayList<String> peoplesNames = new ArrayList<String>();
                if (people != null) {

                    people.forEach((child) -> {
                        peoplesNames.add(child.getString("name"));
                    });
                }
                JsonValue geoData = root.get("geoData");
                Float geoData_latitude = geoData.getFloat("latitude");
                Float geoData_longitude = geoData.getFloat("longitude");
                Float geoData_altitude = geoData.getFloat("altitude");
                Float geoData_latitudeSpan = geoData.getFloat("latitudeSpan");
                Float geoData_longitudeSpan = geoData.getFloat("longitudeSpan");

                JsonValue geoDataExif = root.get("geoDataExif");
                Float geoDataExif_latitude = geoDataExif.getFloat("latitude");
                Float geoDataExif_longitude = geoDataExif.getFloat("longitude");
                Float geoDataExif_altitude = geoDataExif.getFloat("altitude");
                Float geoDataExif_latitudeSpan = geoDataExif.getFloat("latitudeSpan");
                Float geoDataExif_longitudeSpan = geoDataExif.getFloat("longitudeSpan");

                if (geoData_latitude != 0.0 && geoData_longitude != 0.0) {
                    float[] listCoord = { geoData_latitude, geoData_longitude };
                    coords = processCoordinates(listCoord);
                } else if (geoDataExif_latitude != 0.0 && geoDataExif_longitude != 0.0) {
                    float[] listCoord = { geoDataExif_latitude, geoDataExif_longitude };
                    coords = processCoordinates(listCoord);
                }

                if (favorited) {
                    imageData.setLoved(true);
                }
                if (photoTakenTime_timestamp != null) {
                    imageData.setDate(Main.timestampToDate(photoTakenTime_timestamp));
                }
                if (!peoplesNames.isEmpty()) {
                    imageData.setPeoples(peoplesNames);
                }

            }
            Metadata metadata = null;
            if (file.read() != null) {

                metadata = ImageMetadataReader.readMetadata(file.read());
            }
            if (metadata != null) {

                for (Directory dir : metadata.getDirectories()) {

                    if (dir != null && dir.getName() != null && dir.getName().equals("Exif SubIFD")) {
                        for (Tag tag : dir.getTags()) {
                            if (tag.getTagName().equals("Date/Time Original")) {
                                imageData.setDate(tag.getDescription());

                            }
                        }
                    } else if (dir != null && dir.getName() != null && dir.getName().equals("GPS")) {
                        String lat = "";
                        String lon = "";
                        String minusLat = "";
                        String minusLon = "";

                        for (Tag tag : dir.getTags()) {
                            if (tag.getTagName().equals("GPS Latitude")) {
                                lat = tag.getDescription();

                            } else if (tag.getTagName().equals("GPS Longitude")) {
                                lon = tag.getDescription();
                            } else if (tag.getTagName().equals("GPS Latitude Ref")) {

                                minusLat = tag.getDescription();

                            } else if (tag.getTagName().equals("GPS Longitude Ref")) {
                                minusLon = tag.getDescription();

                            }
                        }
                        if (lat == "" && lon == "" && minusLat == "" && minusLon == "") {
                            coords = "";
                        } else {
                            coords = lat + "_" + minusLat + ":" + lon + "_" + minusLon;
                        }
                    } else if (dir != null && dir.getName() != null && dir.getName().equals("Exif IFD0")) {
                        for (Tag tag : dir.getTags()) {
                            if (tag.getTagName().equals("Orientation")) {
                                if (tag.getDescription().contains("180")) {
                                    rotation = 180;
                                } else if (tag.getDescription().contains("270")) {
                                    rotation = 90;
                                } else if (tag.getDescription().contains("90")) {
                                    rotation = 270;
                                }
                            }
                        }

                    }
                }
            }

            if (imageData.getRotation() == 0) {
                imageData.setRotation(rotation);
            }

            if (imageData.getCoords() == "") {
                imageData.setCoords(coords);
            }

            if (imageData.getName() == "") {
                imageData.setName(imagePath);
            }
            if (imageData.getLoved() == null || imageData.getLoved() == false) {
                imageData.setLoved(false);
            }

            addImageData(imageData);

        } catch (

        Exception e) {
            System.out.println("openImageExif" + " -Error " + e);
        } finally {
        }
    }

    public static void addImageData(ImageData imgd) {
        if (Main.imagesData == null) {
            Main.imagesData = new ArrayList<>();
        }
        if (Main.imagesData != null && imgd != null && !Main.imagesData.contains(imgd)) {
            Main.imagesData.add(imgd);
        }
    }

    public static void setSize(String imagePath, String imageName, Integer size, Boolean force) {
        FileHandle handlebis = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + size + "/" + imageName);
        if (!handlebis.exists()) {
            if (force) {
                MixOfImage.isInImageData(imagePath, false, "force");

            } else {
                MixOfImage.isInImageData(imagePath, false, "firstloading");
            }

        } else {
            numberOfLoadedImages += 1;
        }
    }

    // public static void loadImagesForTheFirstTime() {
    // Integer index = 0;
    // for (String imagePath : toLoad) {
    // String[] nameList = imagePath.split("/");
    // String name = nameList[nameList.length - 1];

    // FileHandle from = Gdx.files.absolute(imagePath);
    // byte[] data = from.readBytes();

    // FileHandle to = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + name);
    // to.writeBytes(data, false);

    // FileHandle fromJson = Gdx.files.absolute(imagePath + ".json");
    // if (fromJson.exists()) {
    // byte[] dataJson = fromJson.readBytes();

    // FileHandle toJson = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + name +
    // ".json");
    // toJson.writeBytes(dataJson, false);
    // }

    // openImageExif(name);
    // setSize(ImageData.IMAGE_PATH + "/" + name, name, false);
    // index += 1;
    // }
    // toLoad = new ArrayList<String>();

    // }

    private static String processCoordinates(float[] coordinates) {
        String[] ORIENTATIONS = "N/S/E/W".split("/");
        String converted0 = decimalToDMS(coordinates[1]);
        final String dmsLat = coordinates[0] > 0 ? ORIENTATIONS[0] : ORIENTATIONS[1];
        converted0 = converted0.concat("_").concat(dmsLat);

        String converted1 = decimalToDMS(coordinates[0]);
        final String dmsLng = coordinates[1] > 0 ? ORIENTATIONS[2] : ORIENTATIONS[3];
        converted1 = converted1.concat("_").concat(dmsLng);

        return converted0.concat(":").concat(converted1);
    }

    public static Pixmap textureToPixmap(Texture in) {
        if (!in.getTextureData().isPrepared()) {
            in.getTextureData().prepare();
        }
        return in.getTextureData().consumePixmap();
    }

    public static Pixmap resize(Pixmap inPm, int outWidth, int outheight, boolean cut) {
        Pixmap outPm = new Pixmap(outWidth, outheight, Pixmap.Format.RGBA8888);
        int srcWidth;
        int srcHeigth;
        int srcx = 0;
        int srcy = 0;
        if (cut) {
            int size = Math.min(inPm.getWidth(), inPm.getHeight());

            srcWidth = size;
            srcHeigth = size;
            srcx = (inPm.getWidth() - size) / 2;
            srcy = (inPm.getHeight() - size) / 2;

        } else {
            srcWidth = inPm.getWidth();
            srcHeigth = inPm.getHeight();
        }
        outPm.drawPixmap(inPm, srcx, srcy, srcWidth, srcHeigth, 0, 0, outWidth, outheight);
        inPm.dispose();
        return outPm;
    }

    private static String decimalToDMS(float coord) {

        float mod = coord % 1;
        int intPart = (int) coord;
        String degrees = String.valueOf(intPart);
        coord = mod * 60;
        mod = coord % 1;
        intPart = (int) coord;
        if (intPart < 0)
            intPart *= -1;
        String minutes = String.valueOf(intPart);
        coord = mod * 60;
        intPart = (int) coord;
        if (intPart < 0)
            intPart *= -1;
        String seconds = String.valueOf(intPart);
        String output = Math.abs(Integer.parseInt(degrees)) + "° " + minutes + "' " + seconds + "\" ";

        return output;
    }

}