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
        String[] ListImageName = lookingFor.split("/");

        if (fileName.exists()) {
            willBeLoad.add(lookingFor);
            isLoading = true;
        } else {
            Gdx.app.error(fileName.path(), "Do not exist");
            if (fileName.path().split("/")[ListImageName.length - 2].equals("150")) {
                Gdx.app.error(fileName.path(), "Creating ...");

                forceCreation(fileName);
                return;
            }

        }

    }

    public static void loadImage(String lookingFor) {
        isOnLoading.add(lookingFor);
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (Main.infoText == " " || Main.infoText == Main.preferences.getString("text.done")) {
                    Main.infoTextSet("loading .", false);
                }

                isLoading = true;
                manager.load(lookingFor, Texture.class);

                notToReLoadList.add(lookingFor);

            }
        }).start();
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
            if (fileName.path().split("/")[ListImageName.length - 2].equals("images")) {
                manager.load(fileName.path(), Texture.class);
                manager.finishLoading();
                return manager.get(fileName.path(), Texture.class);

            }
            if (fileName.path().split("/")[ListImageName.length - 2].equals("peoples")
                    && fileName.path().split("/")[ListImageName.length - 2].equals("places")
                    && fileName.path().split("/")[ListImageName.length - 2].equals("150")) {
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
                    forceCreation(fileName);
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

    public static void forceCreation(FileHandle fileName) {
        String[] ListImageName = fileName.path().split("/");
        if (!fileName.exists()) {
            Main.infoTextSet("need to load image due to an error of loading", true);
            String nameWithout150 = "";
            for (int i = 0; i < ListImageName.length - 2; i++) {
                nameWithout150 += ListImageName[i] + "/";
            }
            nameWithout150 += ListImageName[ListImageName.length - 1];

            Main.setSize150Force(nameWithout150, ListImageName[ListImageName.length - 1]);

        }
    }

    public MixOfImage(List<String> imageNames, float width, float height, String prefSizeName) {
        // appliquer la rotation qui est dans les donnée de l'image !!!
        FileHandle handle = null;
        Texture texture;
        Boolean rota = false;
        for (String imageName : imageNames) {
            Integer rotation = 0;
            imageName = imageName.replace("\\", "/");
            String[] ListImageName = imageName.split("/");

            if (imageName.split("/")[ListImageName.length - 2].equals("userImages")
                    || imageName.split("/")[ListImageName.length - 2].equals("150")) {

                handle = Gdx.files.absolute(imageName);
                if (!Gdx.files.internal(ImageData.IMAGE_PATH + imageName).exists()
                        && !Gdx.files.internal(imageName).exists()) {
                    imageName = "images/error.png";
                }

                rotation = Main.getCurrentImageData(ListImageName[ListImageName.length - 1]).getRotation();

            }
            texture = isInImageData(imageName, false, "");
            Image image = new Image(texture);

            if (rotation != 0 && imageName.split("/")[ListImageName.length - 2].equals("150")) {
                image.rotateBy(rotation);
                image.setOrigin((image.getWidth() - 8) / 2, (image.getHeight() - 8) / 2);
                // mettre - 8 en variable

            } else if (rotation != 0) {
                image.rotateBy(rotation);
                image.setOrigin(width / 2, height / 2);
                rota = true;
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
        if (handle != null) {
            setName(handle.name());

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
// !!!!!!!!!!!!!!!!!!!!!! il manque une ligne a la fin des Mainsimages