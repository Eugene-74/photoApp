package photoapp.main.graphicelements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.OrderedMap;

import photoapp.main.Main;
import photoapp.main.windows.LoadImage;

public class MixOfImage extends Group {
    public static List<String> LoadingList = new ArrayList<String>();
    public static List<String> toPlaceList = new ArrayList<String>();
    public static List<String> notToReLoadList = new ArrayList<String>();

    public static AssetManager manager = new AssetManager();
    public static boolean isLoading = false;
    public static boolean firstLoading = false;
    public static long lastTime = 0;

    public static List<String> willBeLoad = new ArrayList<String>();
    public static List<String> isOnLoading = new ArrayList<String>();
    public static List<String> forceSize = new ArrayList<String>();
    public static List<String> squareSize = new ArrayList<String>();
    public static List<String> notSquareSize = new ArrayList<String>();
    public static List<List<String>> toCreateImage100 = new ArrayList<List<String>>();

    public static OrderedMap<String, Integer> isLoaded = new OrderedMap<>();
    Integer espace = 8;

    public static void ini() {
        forceSize.add("10");
        forceSize.add("150");
        squareSize.add("100");
        squareSize.add("10");
        notSquareSize.add("150");

    }

    public static void startToLoadImage(String lookingFor) {
        FileHandle fileName = Gdx.files.absolute(lookingFor);
        if (fileName.exists()) {
            willBeLoad.add(lookingFor);
            isLoading = true;
        }

    }

    public static void loadImage(String lookingFor, boolean instant, boolean force) {
        FileHandle imagePath = Gdx.files.absolute(lookingFor);
        if (!imagePath.exists()) {
            imagePath = Gdx.files.internal(lookingFor);
            if (!imagePath.exists()) {
                System.out.println(imagePath + " does not exist");
                return;

            }
        }
        isOnLoading.add(lookingFor);
        if (instant) {

            manager.load(lookingFor, Texture.class);
            if (force) {
                manager.finishLoading();
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (Main.infoText == " " || Main.infoText == Main.graphic.getString("text.done")) {
                            Main.infoTextSet("loading .", false);
                        }

                        isLoading = true;
                        manager.load(lookingFor, Texture.class);

                        notToReLoadList.add(lookingFor);
                    } catch (Exception e) {
                        System.err.println("image can't be load");
                    } finally {
                    }
                }
            }).start();
        }
    }

    public static void stopLoading() {

        firstLoading = false;
        isLoading = false;
        LoadingList = new ArrayList<String>();
        willBeLoad = new ArrayList<String>();
    }

    public static Texture isInImageData(String imagePath,
            boolean force,
            boolean isFirstLoading, boolean isSquare) {
        FileHandle departureSizeImageHandle = Gdx.files.absolute(imagePath);

        if (force) {
            manager.load(departureSizeImageHandle.path(), Texture.class);
            manager.finishLoading();
        } else {
            imagePath = getImageSizedPath(imagePath, isSquare);
            departureSizeImageHandle = Gdx.files.absolute(imagePath);
            if (shouldBeForce(imagePath, isSquare)) {

                loadImage(departureSizeImageHandle.path(), true, true);

            }

        }
        if (manager.isLoaded(departureSizeImageHandle.path())) {
            return manager.get(departureSizeImageHandle.path(), Texture.class);
        } else {
            if (!manager.isLoaded(Main.graphic.getString("image error"), Texture.class)) {
                manager.load(Main.graphic.getString("image error"), Texture.class);
                manager.finishLoading();
            }
            isLoading = true;
            // manager.load(departureSizeImageHandle.path(), Texture.class);

            loadImage(departureSizeImageHandle.path(), false, false);

            if (isFirstLoading) {
                startToLoadImage(departureSizeImageHandle.path());
                firstLoading = true;
                LoadingList.add(departureSizeImageHandle.path());
                System.out.println("error : " + departureSizeImageHandle.path());
                return manager.get(Main.graphic.getString("image error"), Texture.class);

            }
        }
        System.out.println("error : " + departureSizeImageHandle.path());

        return manager.get(Main.graphic.getString("image error"), Texture.class);

    }

    public MixOfImage(List<String> imagePaths, float width, float height, String prefSizeName,
            boolean force, boolean isSquare) {

        FileHandle fileName = null;
        Texture texture;

        for (String imagePath : imagePaths) {
            Integer rotation = 0;
            imagePath = imagePath.replace("\\", "/");
            List<String> list = Main.departurePathAndImageNameAndFolder(imagePath);
            String departurePath = list.get(0);
            String[] ListImageName = imagePath.split("/");
            String folder = list.get(2);
            if (!folder.equals("images")) {
                fileName = Gdx.files.absolute(imagePath);

            }
            // System.err.println("file" + fileName);
            if (!Gdx.files.internal(imagePath).exists()) {
                Gdx.app.error(fileName.path(), "Do not exist");

                Main.infoTextSet("need to load image due to an error of loading", true);
                if (folder.equals("150")
                        || folder.equals("100")
                        || folder.equals("10")) {

                    Gdx.app.error(fileName.path(), "Creating ...");

                    Integer size = Integer.parseInt(folder);
                    String imageName = ListImageName[ListImageName.length - 1];
                    System.out.println("do not exist : " + departurePath + "--" + size);
                    MixOfImage.createAnImage(departurePath, departurePath + "/" + size, imageName, imageName,
                            size, isSquare,
                            true);
                }
                rotation = 0;

            } else {
                if (Main.getCurrentImageData(ListImageName[ListImageName.length - 1]) != null) {

                    rotation = Main.getCurrentImageData(ListImageName[ListImageName.length - 1]).getRotation();

                }
            }

            // }

            texture = isInImageData(imagePath, force, false,
                    isSquare);
            Image image = new Image(texture);

            Integer max;

            if (image.getWidth() > image.getHeight()) {
                max = (int) width;
            } else {
                max = (int) height;
            }
            image.rotateBy(rotation);
            if (width == Main.graphic.getInteger("size of main images button")
                    && height == Main.graphic.getInteger("size of main images button")) {
                image.setOrigin((max - espace) / 2, (max - espace) / 2);

            } else {

                image.setOrigin(width / 2, height / 2);

            }

            if (imagePath.endsWith("outline.png") || imagePath.endsWith("redOutline.png")) {
                image.setName("outline");
            } else {
                image.setName("image");
            }

            if (imagePaths.size() == 1) {

                if (isSquare) {
                    // Integer max;

                    if (image.getWidth() > image.getHeight()) {
                        max = (int) image.getWidth();
                    } else {
                        max = (int) image.getHeight();
                    }
                    setWidth(max);
                    setHeight(max);
                } else {

                    if (rotation == 90
                            || rotation == 270) {
                        Float w = image.getWidth();
                        Float h = image.getHeight();
                        setWidth(Main.graphic.getInteger("size of main image height"));
                        setHeight(Main.graphic.getInteger("size of main image height") * h / w);

                    } else {
                        setWidth(image.getWidth());
                        setHeight(image.getHeight());
                    }

                }

            }

            addActor(image);

        }
        if (fileName != null) {
            setName(fileName.name());

        } else {
            setName("noName");

        }

    }

    public static void createAnImage(String departurePath, String arrivalPath, String departureImageName,
            String arrivalImageName, Integer size,
            boolean isSquare, boolean force) {
        if (departurePath == null || departurePath.equals("")) {
            departurePath = "";
        } else {
            departurePath = departurePath + "/";
        }

        FileHandle departureImageHandle = Gdx.files.absolute(departurePath + departureImageName);
        FileHandle arrivalImageHandle = Gdx.files.absolute(arrivalPath + "/" + arrivalImageName);
        FileHandle departureHandle = Gdx.files.absolute(departurePath);
        FileHandle arrivalHandle = Gdx.files.absolute(arrivalPath);

        if (!departureHandle.exists()) {
            return;
        } else {
            if (!arrivalHandle.exists()) {
                arrivalHandle.mkdirs();
            }
            loadImage(departureImageHandle.path(), true, force);
            Texture texture = manager.get(departureImageHandle.path(), Texture.class);
            Pixmap pixmap = null;
            if (size != null) {
                if (isSquare) {
                    pixmap = LoadImage.resize(LoadImage.textureToPixmap(texture), size, size, true);
                } else {
                    if (texture.getHeight() > texture.getWidth()) {
                        pixmap = LoadImage.resize(LoadImage.textureToPixmap(texture), size,
                                (int) (size * texture.getHeight() / texture.getWidth()),
                                false);
                    } else {
                        pixmap = LoadImage.resize(LoadImage.textureToPixmap(texture),
                                (int) (size * texture.getWidth() / texture.getHeight()), size,
                                false);

                    }
                }
            }

            PixmapIO.writePNG(arrivalImageHandle, pixmap);
            pixmap.dispose();
        }
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);// super : celle de mon parents ici group
        for (Actor actor : getChildren()) {
            if (actor.getName().equals("image") && width == Main.graphic.getInteger("size of main images button")
                    && height == Main.graphic.getInteger("size of main images button")) {

                actor.setSize(Main.graphic.getInteger("size of main images button") - espace,
                        Main.graphic.getInteger("size of main images button") - espace);
                // actor.setPosition(0, 0);

                actor.setPosition(espace / 2, espace / 2);
            } else if (!actor.getName().endsWith("outline")
                    && width == Main.graphic.getInteger("size of basic button")
                    && height == Main.graphic.getInteger("size of basic button")) {

                actor.setSize(Main.graphic.getInteger("size of basic button") - 3,
                        Main.graphic.getInteger("size of basic button") - 3);
                actor.setPosition(3 / 2, 3 / 2);

            } else {
                actor.setSize(width, height);

            }

        }

    }

    public static String getImageSizedPath(String imagePath, boolean isSquare) {

        List<String> List = Main.departurePathAndImageNameAndFolder(imagePath);
        String departurePath = List.get(0);
        String name = List.get(1);
        String folder = List.get(2);
        List<String> size = new ArrayList<String>();
        if (folder.equals("images")) {
            return imagePath;
        }
        if (isSquare) {
            size = squareSize;
        } else {
            size = notSquareSize;
        }

        if (size.contains(folder)) {
            folder = "";
        } else {

            folder = "/" + folder;
        }

        if (manager.isLoaded(departurePath + folder + "/" + name) && !isSquare) {
            return (departurePath + folder + "/" + name);
        } else {
            for (String s : size) {
                if (manager.isLoaded(departurePath + folder + "/" + s + "/" + name)) {
                    return (departurePath + folder + "/" + s + "/" + name);
                }
            }
            FileHandle handle = Gdx.files
                    .absolute(departurePath + folder + "/" + size.get(size.size() - 1) + "/" + name);

            if (!handle.exists()) {
                createAnImage(departurePath + folder, departurePath + folder + "/" + size.get(size.size() - 1), name,
                        name,
                        Integer.parseInt(size.get(size.size() - 1)), isSquare, true);
            }
            return (departurePath + folder + "/" + size.get(size.size() - 1) + "/" + name);

        }
    }

    public static boolean shouldBeForce(String imagePath, boolean isSquare) {
        for (String size : forceSize) {
            if (Main.departurePathAndImageNameAndFolder(getImageSizedPath(imagePath, isSquare))
                    .get(2).equals(size)
                    || Main.departurePathAndImageNameAndFolder(getImageSizedPath(imagePath, isSquare))
                            .get(2).equals("images")) {
                return true;
            }
        }
        return false;
    }
}