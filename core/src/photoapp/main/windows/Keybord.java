package photoapp.main.windows;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.TimeUtils;

import photoapp.main.CommonFunction;
import photoapp.main.Main;
import photoapp.main.storage.ImageData;

public class Keybord implements InputProcessor {
    public static boolean crtl = false;
    static boolean previousDown = false;
    static boolean nextDown = false;
    static Long lastTime = (long) 0;
    static Integer index = 0;

    @Override
    public boolean keyDown(int keycode) {
        // System.out.println(keycode);
        if (crtl) {
        }
        if ((keycode == Keys.LEFT) || keycode == Keys.UP
                || keycode == Input.Keys.Z) {
            previousDown = true;
            previous();
            index = 0;
        } else if ((keycode == Keys.RIGHT || keycode == Keys.DOWN
                || keycode == Keys.S)) {
            nextDown = true;
            next();
            index = 0;

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
        if ((keycode == Keys.LEFT) || keycode == Keys.UP
                || keycode == Input.Keys.Z) {
            previousDown = false;

        } else if ((keycode == Keys.RIGHT || keycode == Keys.DOWN
                || keycode == Keys.S)) {
            // System.err.println("key up");

            nextDown = false;
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

    public static void previous() {
        if (Main.windowOpen.equals("ImageEdition")) {
            ImageEdition.previousImage(ImageEdition.theCurrentImageName);

        } else if (Main.windowOpen.equals("MainImages")) {
            MainImages.previousImages();

        } else if (Main.windowOpen.equals("BigPreview")) {
            BigPreview.previousImage(ImageEdition.theCurrentImageName);

        }
    }

    public static void next() {
        if (Main.windowOpen.equals("ImageEdition")) {
            ImageEdition.nextImage(ImageEdition.theCurrentImageName);

        } else if (Main.windowOpen.equals("MainImages")) {
            MainImages.nextImages();

        } else if (Main.windowOpen.equals("BigPreview")) {
            // ImageEdition.bigPreview = true;
            BigPreview.nextImage(ImageEdition.theCurrentImageName);

        }

    }

    public static void render() {
        if (TimeUtils.millis() - lastTime > 500 && index <= 2) {
            if (previousDown) {
                previous();
            } else if (nextDown) {
                next();
            }
            lastTime = TimeUtils.millis();
            index += 1;
        } else if (TimeUtils.millis() - lastTime > 300 && index <= 5) {
            if (previousDown) {
                previous();
            } else if (nextDown) {
                next();
            }
            lastTime = TimeUtils.millis();
            index += 1;

        } else if (TimeUtils.millis() - lastTime > 200 && index <= 10) {
            if (previousDown) {
                previous();
            } else if (nextDown) {
                next();
            }
            lastTime = TimeUtils.millis();
            index += 1;

        } else if (TimeUtils.millis() - lastTime > 100 && index > 10) {
            if (previousDown) {
                previous();
            } else if (nextDown) {
                next();
            }
            lastTime = TimeUtils.millis();
            index += 1;

        }

    }

}
