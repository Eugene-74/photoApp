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
    public static OrderedMap<String, Integer> isLoaded = new OrderedMap<>();
    Integer espace = 8;
    static String errorImagePath = "images/loading button.png";

    public static void startToLoadImage(String lookingFor) {
        FileHandle fileName = Gdx.files.absolute(lookingFor);
        // String[] ListImageName = lookingFor.split("/");

        if (fileName.exists()) {
            willBeLoad.add(lookingFor);
            isLoading = true;
        }

    }

    public static void loadImage(String lookingFor, boolean instant, boolean force) {
        // FileHandle fileName = Gdx.files.absolute(lookingFor);
        // if (!fileName.exists()) {
        // return;
        // }
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

                        if (Main.infoText == " " || Main.infoText == Main.preferences.getString("text.done")) {
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

    public static Texture isInImageData(String imageName,
            boolean force,
            boolean isFirstLoading, boolean isSquare) {
        String departurePath = "";
        if (departurePath == null || departurePath.equals("")) {
            departurePath = "";
        } else {
            departurePath = departurePath + "/";
        }
        // System.err.println(imageName + " image Path");
        FileHandle departureHandle = Gdx.files.absolute(departurePath);
        FileHandle departureImageHandle = Gdx.files.absolute(departurePath + imageName);
        FileHandle departureSizeImageHandle = Gdx.files.absolute(imageName);
        FileHandle departureSizeHandle = null;

        if (force) {
            manager.load(departureSizeImageHandle.path(), Texture.class);
            manager.finishLoading();
        }
        if (manager.isLoaded(departureSizeImageHandle.path())) {
            return manager.get(departureSizeImageHandle.path(), Texture.class);
        } else {
            if (!manager.isLoaded(errorImagePath, Texture.class)) {
                manager.load(errorImagePath, Texture.class);
                manager.finishLoading();
            }
            isLoading = true;
            manager.load(departureSizeImageHandle.path(), Texture.class);

            if (isFirstLoading) {
                startToLoadImage(departureSizeImageHandle.path());
                firstLoading = true;
                LoadingList.add(departureSizeImageHandle.path());
                return manager.get(errorImagePath, Texture.class);

            } else {
                if (!departureSizeImageHandle.exists()) {
                    // createAnImage(departureHandle.path(), departureSizeHandle.path(), imageName,
                    // size, isSquare,
                    // true);
                    // loadImage(departureSizeHandle.path() + imageName, true, true);
                    // return manager.get(departureSizeImageHandle.path(), Texture.class);
                }

            }
        }
        return manager.get(errorImagePath, Texture.class);

    }

    // TODO refaire totalement
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
                    MixOfImage.createAnImage(departurePath, departurePath + "/" + size, imageName,
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

            if (rotation != 0 && ListImageName.length > 2
                    && (folder.equals("100")
                            || folder.equals("10"))) {
                if (isSquare) {

                    Integer max;

                    if (image.getWidth() > image.getHeight()) {
                        max = (int) width;
                    } else {
                        max = (int) height;
                    }
                    image.rotateBy(rotation);
                    if (width == Main.preferences.getInteger("size of main images button")
                            && height == Main.preferences.getInteger("size of main images button")) {
                        image.setOrigin(max / 2 - espace / 2, max / 2 - espace / 2);

                    } else {
                        image.setOrigin(max / 2, max / 2);

                    }

                }

            } else if (rotation != 0) {
                image.rotateBy(rotation);
                image.setOrigin(width / 2, height / 2);
            }

            if (imagePath.endsWith("outline.png") || imagePath.endsWith("redOutline.png")) {
                image.setName("outline");
            } else {
                image.setName("image");
            }

            if (imagePaths.size() == 1) {

                if (isSquare) {
                    Integer max;

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
                        setWidth(Main.preferences.getInteger("size of main image height"));
                        setHeight(Main.preferences.getInteger("size of main image height") * h / w);
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

    public static void createAnImage(String departurePath, String arrivalPath, String imageName, Integer size,
            boolean isSquare, boolean force) {
        if (departurePath == null || departurePath.equals("")) {
            departurePath = "";
        } else {
            departurePath = departurePath + "/";
        }

        FileHandle departureImageHandle = Gdx.files.absolute(departurePath + imageName);
        FileHandle arrivalImageHandle = Gdx.files.absolute(arrivalPath + "/" + imageName);
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
            if (actor.getName().equals("image") && width == Main.preferences.getInteger("size of main images button")
                    && height == Main.preferences.getInteger("size of main images button")) {

                actor.setSize(Main.preferences.getInteger("size of main images button") - espace,
                        Main.preferences.getInteger("size of main images button") - espace);
                // mettre - 8 en variable

                actor.setPosition(espace / 2, espace / 2);
            } else if (!actor.getName().endsWith("outline")
                    && width == Main.preferences.getInteger("size of basic button")
                    && height == Main.preferences.getInteger("size of basic button")) {
                actor.setSize(Main.preferences.getInteger("size of basic button") - 3,
                        Main.preferences.getInteger("size of basic button") - 3);
                actor.setPosition(3 / 2, 3 / 2);

            } else {
                actor.setSize(width, height);

            }

        }

    }
}