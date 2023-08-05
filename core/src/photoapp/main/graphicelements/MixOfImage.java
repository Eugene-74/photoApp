package photoapp.main.graphicelements;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.TimeUtils;

import photoapp.main.Main;
import photoapp.main.storage.ImageData;

public class MixOfImage extends Group {
    public static AssetManager manager = new AssetManager();

    // public void mixOfImage() {
    // System.out.println("new HASH MAP ------------------------");
    // imagesData = new HashMap<>();
    // }
    public static void clearImagesTextureData() {
        Main.imagesTextureData = new OrderedMap<>();
    }

    public static void loadImage(String lookingFor) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Main.infoTextSet("loading");
                manager.load(lookingFor, Texture.class);

            }
        }).start();
        // new Thread(new Runnable() {
        // @Override
        // public void run() {

        // }
        // }).start();

        // System.out.println(lookingFor + " is now loaded");
    }

    public Texture isInImageData(String lookingFor) {
        Texture texture;

        if (!manager.isLoaded(lookingFor)) {
            // System.out.println("to load : " + lookingFor);
            loadImage(lookingFor);
            // System.out.println("loading ...");
            // System.out.println(lookingFor + " : lookingfor");
            if (lookingFor.startsWith("images/")) {
                // System.out.println("waiting");
                while (!manager.isLoaded(lookingFor)) {
                    manager.update();

                }

            } else {
                Main.toReloadList = Main.addToList(Main.toReloadList, lookingFor);
                // System.out.println(Main.toReloadList);
            }
        }
        if (manager.isLoaded(lookingFor)) {
            // System.out.println("is loaded : " + lookingFor);

            texture = manager.get(lookingFor, Texture.class);
        } else {
            return new Texture("images/loading button.png");
        }

        // imagesTextureData.put(lookingFor, texture);

        // System.out.println(lookingFor + "----------" + texture);
        // imagesData.keySet(lookingFor).entrySet(texture);
        // System.out.println(imagesData);

        return texture;
    }

    public MixOfImage(List<String> imageNames, boolean isSquare) {

        for (String imageName : imageNames) {
            // System.out.println(ImageData.IMAGE_PATH + imageName + "\n" +
            // Gdx.files.internal(imageName));
            if (!Gdx.files.internal(ImageData.IMAGE_PATH + imageName).exists()
                    && !Gdx.files.internal(imageName).exists()) {
                imageName = "images/error.png";
            }

            // long startTimePlaceImageOfPeoples = TimeUtils.millis();
            Texture texture = isInImageData(imageName);
            // long stopTimePlaceImageOfPeoples = TimeUtils.millis();
            // System.out.println(
            // "-----------------" + ImageData.IMAGE_PATH + imageName + "create mix of image
            // done in : ");
            // if (stopTimePlaceImageOfPeoples - startTimePlaceImageOfPeoples >= 50) {
            // System.out.println("TO MUCH LAG !!!");
            // }
            // System.out.println(stopTimePlaceImageOfPeoples -
            // startTimePlaceImageOfPeoples);

            Image image = new Image(texture);
            addActor(image);
        }
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);// super : celle de mon parents ici group
        for (Actor actor : getChildren()) {
            actor.setSize(width, height);
        }

    }
}
