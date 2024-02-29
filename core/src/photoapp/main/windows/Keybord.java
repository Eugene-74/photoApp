package photoapp.main.windows;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import photoapp.main.CommonFunction;
import photoapp.main.Main;
import photoapp.main.storage.ImageData;

public class Keybord implements InputProcessor {
    public static boolean crtl = false;

    @Override
    public boolean keyDown(int keycode) {
        // System.out.println(keycode);
        if (crtl) {
        }
        if ((keycode == Keys.LEFT) || keycode == Keys.UP
                || keycode == Input.Keys.Z) {
            previous();
        } else if ((keycode == Keys.RIGHT || keycode == Keys.DOWN
                || keycode == Keys.S)) {
            next();
        } else if ((keycode == 70) && (crtl)) {
            // = (signe sur le +)
            Main.iconSize(true);
        } else if ((keycode == 13) && (crtl)) {
            // - (signe sur le -)
            Main.iconSize(false);
        } else if ((keycode == Keys.D) && (crtl)) {
            if (Main.windowOpen == "ImageEdition") {
                ImageEdition.moveToDeleteAnImage(ImageData.getImageDataIfExist(ImageEdition.theCurrentImageName),
                        ImageEdition.theCurrentImageName);

            }
        } else if (keycode == Keys.ESCAPE) {
            CommonFunction.back();
        } else if (keycode == Keys.CONTROL_LEFT) {
            crtl = true;
        }

        return false;
    }

    public boolean keyUp(int keycode) {
        if (keycode == Keys.CONTROL_LEFT) {
            crtl = false;
        }
        return false;
    }

    public boolean keyTyped(char character) {

        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if ((button == 0)) {
            Main.isOnClick = true;

        }

        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if ((button == 0)) {
            Main.isOnClick = false;

        }
        return false;
    }

    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (crtl) {
            if (amountY > 0) {
                Main.iconSize(false); // unzoom
            } else if (amountY < 0) {
                Main.iconSize(true); // zoom
            }
        } else {

            if (amountY > 0) {
                next();
            } else if (amountY < 0) {
                previous();
            }
        }

        return false;
    }

    public void previous() {
        if (Main.windowOpen.equals("ImageEdition")) {
            ImageEdition.previousImage(ImageEdition.theCurrentImageName);

        } else if (Main.windowOpen.equals("MainImages")) {
            MainImages.previousImages();

        } else if (Main.windowOpen.equals("BigPreview")) {
            BigPreview.previousImage(ImageEdition.theCurrentImageName);

        }
    }

    public void next() {
        if (Main.windowOpen.equals("ImageEdition")) {
            ImageEdition.nextImage(ImageEdition.theCurrentImageName);

        } else if (Main.windowOpen.equals("MainImages")) {
            MainImages.nextImages();

        } else if (Main.windowOpen.equals("BigPreview")) {
            // ImageEdition.bigPreview = true;
            BigPreview.nextImage(ImageEdition.theCurrentImageName);

        }

    }

}
