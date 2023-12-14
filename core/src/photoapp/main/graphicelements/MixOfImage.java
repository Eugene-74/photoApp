package photoapp.main.graphicelements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.OrderedMap;

import photoapp.main.Main;
import photoapp.main.storage.ImageData;
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

    public static void startToLoadImage(String lookingFor) {
        FileHandle fileName = Gdx.files.absolute(lookingFor);
        // String[] ListImageName = lookingFor.split("/");

        if (fileName.exists()) {
            willBeLoad.add(lookingFor);
            isLoading = true;
        }

    }

    public static void loadImage(String lookingFor) {
        isOnLoading.add(lookingFor);
        String[] ListImageName = lookingFor.split("/");
        if (lookingFor.split("/")[ListImageName.length - 2].equals("150")) {
            manager.load(lookingFor, Texture.class);
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

    }

    public static Texture isInImageData(String lookingFor, boolean wait, String type) {
        FileHandle fileName = Gdx.files.absolute(lookingFor);
        String[] ListImageName = lookingFor.split("/");

        if (manager.isLoaded(fileName.path())) {
            return manager.get(fileName.path(), Texture.class);
        } else {

            isLoading = true;
            String errorImagePath = "images/loading button.png";
            if (!manager.isLoaded(errorImagePath, Texture.class)) {
                manager.load(errorImagePath, Texture.class);
                manager.finishLoading();
            }
            if (type.equals("force")) {
                manager.load(lookingFor, Texture.class);
                manager.finishLoading();

                return manager.get(lookingFor, Texture.class);
            } else if (type.equals("firstloading")) {
                startToLoadImage(lookingFor);
                firstLoading = true;
                LoadingList.add(fileName.path());

                return manager.get(errorImagePath, Texture.class);
            } else {
                if (fileName.path().split("/")[ListImageName.length - 2].equals("images")) {
                    manager.load(fileName.path(), Texture.class);
                    manager.finishLoading();
                    return manager.get(fileName.path(), Texture.class);

                } else if (fileName.path().split("/")[ListImageName.length - 2].equals("10")) {
                    fileName = Gdx.files
                            .absolute(ImageData.IMAGE_PATH + "/10/" + ListImageName[ListImageName.length - 1]);
                    if (!fileName.exists()) {
                        LoadImage.setSizeForce(ImageData.IMAGE_PATH + "/" + ListImageName[ListImageName.length - 1],
                                10);
                    }
                    manager.load(fileName.path(), Texture.class);
                    manager.finishLoading();
                    return manager.get(fileName.path(), Texture.class);
                }
                if (fileName.path().split("/")[ListImageName.length - 2].equals("peoples")
                        && fileName.path().split("/")[ListImageName.length - 2].equals("places")) {
                    // && fileName.path().split("/")[ListImageName.length - 2].equals("150")
                    loadImage(fileName.path());
                } else {
                    startToLoadImage(fileName.path());
                    fileName = Gdx.files
                            .absolute(ImageData.IMAGE_PATH + "/150/" + ListImageName[ListImageName.length - 1]);
                    ListImageName = fileName.path().split("/");

                }

                if (fileName.path().split("/")[ListImageName.length - 2].equals("150")) {
                    if (fileName.exists()) {
                        if (!manager.isLoaded(fileName.path())) {
                            manager.load(fileName.path(), Texture.class);
                            manager.finishLoading();
                        }
                        return manager.get(fileName.path(), Texture.class);
                    } else {
                        if (!type.equals("firstloading")) {
                            Main.infoTextSet("need to load image due to an error of loading", true);

                            Gdx.app.error(fileName.path(), "Do not exist");
                            if (fileName.path().split("/")[ListImageName.length - 2].equals("150")) {
                                Gdx.app.error(fileName.path(), "Creating ...");

                                LoadImage.setSizeForce(Main.nameWithoutToNameWithout150(fileName.path()), 150);
                                return manager.get(Main.nameWithoutToNameWithout150(fileName.path()), Texture.class);

                            }

                        }
                        if (!toPlaceList.contains(ListImageName[ListImageName.length - 1])) {
                            toPlaceList.add(ListImageName[ListImageName.length - 1]);
                        }
                    }
                }
                if (!toPlaceList.contains(ListImageName[ListImageName.length - 1])) {
                    toPlaceList.add(ListImageName[ListImageName.length - 1]);
                }
                return manager.get(errorImagePath, Texture.class);
            }
        }

    }

    public MixOfImage(List<String> imageNames, float width, float height, String prefSizeName) {
        FileHandle fileName = null;
        Texture texture;
        for (String imageName : imageNames) {
            Integer rotation = 0;
            imageName = imageName.replace("\\", "/");
            String[] ListImageName = imageName.split("/");

            if (imageName.split("/")[ListImageName.length - 2].equals("userImages")
                    || imageName.split("/")[ListImageName.length - 2].equals("150")
                    || imageName.split("/")[ListImageName.length - 2].equals("10")
                    || imageName.split("/")[ListImageName.length - 2].equals("peoples")
                    || imageName.split("/")[ListImageName.length - 2].equals("places")) {

                fileName = Gdx.files.absolute(imageName);
                if (!Gdx.files.internal(ImageData.IMAGE_PATH + imageName).exists()
                        && !Gdx.files.internal(imageName).exists()) {
                    Main.infoTextSet("need to load image due to an error of loading", true);

                    Gdx.app.error(fileName.path(), "Do not exist");
                    if (fileName.path().split("/")[ListImageName.length - 2].equals("150")) {
                        Gdx.app.error(fileName.path(), "Creating ...");
                        LoadImage.setSizeForce(Main.nameWithoutToNameWithout150(fileName.path()), 150);

                    }
                    // imageName = "images/error.png";
                    // ListImageName = imageName.split("/");
                    rotation = 0;

                } else {
                    if (Main.getCurrentImageData(ListImageName[ListImageName.length - 1]) != null) {

                        rotation = Main.getCurrentImageData(ListImageName[ListImageName.length - 1]).getRotation();

                    }
                }

            }
            // if (imageName.endsWith("error.png")) {
            // System.err.println("error");
            // fileName = Gdx.files.absolute("image/error.png");
            // }
            texture = isInImageData(imageName, false, "");
            Image image = new Image(texture);

            if (rotation != 0 && ListImageName.length > 2
                    && imageName.split("/")[ListImageName.length - 2].equals("150")) {
                image.rotateBy(rotation);
                if (width == 150 && height == 150) {
                    image.setOrigin((width - 8) / 2, (height - 8) / 2);
                } else {
                    image.setOrigin((width) / 2, (height) / 2);

                }
                // mettre - 8 en variable

            } else if (rotation != 0) {
                image.rotateBy(rotation);
                image.setOrigin(width / 2, height / 2);
            }

            if (imageName.endsWith("outline.png") || imageName.endsWith("redOutline.png")) {
                image.setName("outline");
            } else {
                image.setName("image");
            }

            if (imageNames.size() == 1) {

                // if (rotation == 90 || rotation == 270) {
                // // a changer la h des images rota doit etre <

                // // longueur :
                // setHeight(600);

                // // hauteur :
                // setWidth(Main.preferences.getInteger("size of " + prefSizeName + " height"));

                // } else {
                setWidth(image.getWidth());
                setHeight(image.getHeight());
                // }

            }

            addActor(image);

        }
        if (fileName != null) {
            setName(fileName.name());

        } else {
            setName("noName");

        }

    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);// super : celle de mon parents ici group
        for (Actor actor : getChildren()) {
            if (actor.getName().equals("image") && width == Main.preferences.getInteger("size of main images button")
                    && height == Main.preferences.getInteger("size of main images button")) {

                actor.setSize(Main.preferences.getInteger("size of main images button") - 8,
                        Main.preferences.getInteger("size of main images button") - 8);
                // mettre - 8 en variable

                actor.setPosition(8 / 2, 8 / 2);
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