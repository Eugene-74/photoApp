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

    public static List<String> isOnLoading = new ArrayList<String>();
    public static OrderedMap<String, Integer> isLoaded = new OrderedMap<>();

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

        if (!manager.isLoaded(lookingFor) && !notToReLoadList.contains(lookingFor)) {
            loadImage(lookingFor);
            if (lookingFor.startsWith("images/") || wait) {
                while (!manager.isLoaded(lookingFor)) {
                    manager.update();
                }
                return manager.get(lookingFor, Texture.class);

            }
        }
        String fileName = "";
        String[] ListImageName = lookingFor.split("/");
        if (manager.isLoaded(lookingFor)) {
            if (lookingFor.split("/")[ListImageName.length - 2].equals("150")) {

            }

            return manager.get(lookingFor, Texture.class);
        } else {

            if (ListImageName.length > 2) {

                if (type.equals("firstloading")) {

                    fileName = ImageData.IMAGE_PATH + "/" + ListImageName[ListImageName.length -
                            1];
                    firstLoading = true;
                    LoadingList.add(fileName);

                    return null;

                } else {
                    if (!lookingFor.split("/")[ListImageName.length - 2].equals("images")
                            && !lookingFor.split("/")[ListImageName.length - 2].equals("peoples")
                            && !lookingFor.split("/")[ListImageName.length - 2].equals("places")
                            && !lookingFor.split("/")[ListImageName.length - 2].equals("150")) {
                        isLoading = true;
                        fileName = ImageData.IMAGE_PATH + "/150/" + ListImageName[ListImageName.length - 1];

                        if (!notToReLoadList.contains(fileName)) {

                            manager.load(fileName, Texture.class);
                            notToReLoadList.add(fileName);
                        }

                        manager.finishLoadingAsset(fileName);
                        manager.update();

                        return manager.get(fileName, Texture.class);
                    }

                }

            } else {
                fileName = lookingFor;
            }

            if (!manager.isLoaded(fileName, Texture.class)) {
                String loadingFile = "images/loading button.png";
                if (manager.isLoaded(loadingFile, Texture.class)) {

                    toPlaceList.add(ListImageName[ListImageName.length - 1]);

                    return manager.get(loadingFile, Texture.class);
                } else {

                    return new Texture(loadingFile);
                }

            } else {

                return manager.get(fileName, Texture.class);
            }

        }

    }

    public MixOfImage(List<String> imageNames) {
        FileHandle handle = null;
        for (String imageName : imageNames) {
            imageName = imageName.replace("\\", "/");
            String[] ListImageName = imageName.split("/");
            if (imageName.split("/")[ListImageName.length - 2].equals("150")
                    || imageName.split("/")[ListImageName.length - 2].equals("peoples")
                    || imageName.split("/")[ListImageName.length - 2].equals("places")) {

                handle = Gdx.files.absolute(imageName);
                if (!handle.exists()) {
                    Main.infoTextSet("need to load image due to an error of loading", false);
                    String nameWithout150 = "";
                    for (int i = 0; i < ListImageName.length - 2; i++) {
                        nameWithout150 += ListImageName[i] + "/";
                    }
                    nameWithout150 += ListImageName[ListImageName.length - 1];

                    Main.setSize150Force(nameWithout150, ListImageName[ListImageName.length - 1]);

                }
            }

            if (!Gdx.files.internal(ImageData.IMAGE_PATH + imageName).exists()
                    && !Gdx.files.internal(imageName).exists()) {
                imageName = "images/error.png";
            }
            Texture texture = isInImageData(imageName, false, "");

            Image image = new Image(texture);
            if (imageName.endsWith("outline.png") || imageName.endsWith("redOutline.png")) {
                image.setName("outline");
            } else {
                image.setName("image");
            }
            if (imageNames.size() == 1) {
                setWidth(image.getWidth());
                setHeight(image.getHeight());

            }

            addActor(image);
        }
        if (handle != null) {
            setName(handle.name());

        } else {
            // System.out.println(imageName);
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
