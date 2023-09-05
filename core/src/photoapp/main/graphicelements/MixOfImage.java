package photoapp.main.graphicelements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import photoapp.main.Main;
import photoapp.main.storage.ImageData;

public class MixOfImage extends Group {
    public static List<String> LoadingList = new ArrayList<String>();
    public static List<String> notToReLoadList = new ArrayList<String>();

    public static AssetManager manager = new AssetManager();
    public static boolean isLoading = false;
    public static boolean firstLoading = false;
    public static long lastTime = 0;

    // public void mixOfImage() {
    // System.out.println("new HASH MAP ------------------------");
    // imagesData = new HashMap<>();
    // }

    public static void loadImage(String lookingFor) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (Main.infoText == " " || Main.infoText == "done") {
                    Main.infoTextSet("loading .");
                    // System.out.println(".");
                }
                if (Main.infoText == "loading ." && TimeUtils.millis() - lastTime >= 500) {
                    Main.infoTextSet("loading ..");
                    // System.out.println("..");
                    lastTime = TimeUtils.millis();

                } else if (Main.infoText == "loading .." && TimeUtils.millis() - lastTime >= 500) {
                    Main.infoTextSet("loading ...");

                    // System.out.println("...");

                    lastTime = TimeUtils.millis();

                } else if (Main.infoText == "loading ..." && TimeUtils.millis() - lastTime >= 500) {
                    Main.infoTextSet("loading .");

                    lastTime = TimeUtils.millis();
                }

                isLoading = true;
                manager.load(lookingFor, Texture.class);
                notToReLoadList.add(lookingFor);

            }
        }).start();
    }

    public static Texture isInImageData(String lookingFor, boolean wait, String type) {

        if (!manager.isLoaded(lookingFor) && !notToReLoadList.contains(lookingFor)) {
            loadImage(lookingFor);
            // System.out.println("loading : ------ : " + lookingFor);
            if (lookingFor.startsWith("images/") || wait) {
                while (!manager.isLoaded(lookingFor)) {
                    manager.update();
                }
                return manager.get(lookingFor, Texture.class);

            }
        }
        if (manager.isLoaded(lookingFor)) {

            return manager.get(lookingFor, Texture.class);
        } else {
            String fileName = "";
            String[] ListImageName = lookingFor.split("/");
            if (ListImageName.length > 2) {

                // System.out.println(lookingFor.split("/")[ListImageName.length - 2] +
                // "--------------" + lookingFor);

                if (type.equals("firstloading")) {

                    fileName = ImageData.IMAGE_PATH + "/" + ListImageName[ListImageName.length -
                            1];
                    firstLoading = true;
                    LoadingList.add(fileName);
                    return null;
                    // isLoading = true;
                    // manager.load(fileName, Texture.class);

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
                        // return isInImageData(fileName, true, "");
                        return manager.get(fileName, Texture.class);
                    }

                }

                // manager.finishLoading();
                // manager.update();
            } else {
                fileName = lookingFor;
            }

            if (!manager.isLoaded(fileName, Texture.class)) {
                if (manager.isLoaded("images/loading button.png", Texture.class)) {
                    return manager.get("images/loading button.png", Texture.class);
                } else {

                    return new Texture("images/loading button.png");
                }

                // manager.load(fileName, Texture.class);
                // System.out.println("not loaded");
                // return isInImageData(fileName, false);
            } else {
                return manager.get(fileName, Texture.class);
            }

        }

        // imagesTextureData.put(lookingFor, texture);

        // System.out.println(lookingFor + "----------" + texture);
        // imagesData.keySet(lookingFor).entrySet(texture);
        // System.out.println(imagesData);

    }

    public MixOfImage(List<String> imageNames, boolean isSquare) {
        for (String imageName : imageNames) {

            if (!Gdx.files.internal(ImageData.IMAGE_PATH + imageName).exists()
                    && !Gdx.files.internal(imageName).exists()) {
                imageName = "images/error.png";
            }
            Texture texture = isInImageData(imageName, false, "");

            Image image = new Image(texture);
            if (imageName.endsWith("outline.png")) {
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
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);// super : celle de mon parents ici group
        for (Actor actor : getChildren()) {
            if (actor.getName().equals("image") && width == Main.preferences.getInteger("size of main images button")
                    && height == Main.preferences.getInteger("size of main images button")) {
                actor.setSize(Main.preferences.getInteger("size of main images button") - 10,
                        Main.preferences.getInteger("size of main images button") - 10);
                actor.setPosition(10 / 2, 10 / 2);
                // actor.setPosition(125 / 4, 125 / 4, Align.center);
            } else {
                actor.setSize(width, height);

            }

            // System.out.println(actor.getName());
        }

    }
}
