package photoapp.main.windows;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.TimeUtils;

import photoapp.main.Main;

public class Keybord implements InputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        System.out.println(keycode);
        if ((keycode == Keys.LEFT) || keycode == Keys.UP
                || keycode == Input.Keys.Z) {
            previous();
        } else if ((keycode == Keys.RIGHT || keycode == Keys.DOWN
                || keycode == Keys.S)) {
            next();

        }

        return false;
    }

    public boolean keyUp(int keycode) {

        return false;
    }

    public boolean keyTyped(char character) {

        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // System.out.println("down " + pointer);
        if ((button == 0)) {
            Main.isOnClick = true;
            System.out.println("true");

        }

        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if ((button == 0)) {
            Main.isOnClick = false;
            System.out.println("false");

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
        if (amountY > 0) {
            next();
        } else if (amountY < 0) {
            previous();
        }
        return false;
    }

    public void previous() {
        if (Main.windowOpen.equals("Image Edition")) {
            ImageEdition.previousImage(ImageEdition.theCurrentImagePath);

        } else if (Main.windowOpen.equals("Main Images")) {
            MainImages.previousImages();

        }
        Main.lastTime = TimeUtils.millis();
    }

    public void next() {
        if (Main.windowOpen.equals("Image Edition")) {
            ImageEdition.nextImage(ImageEdition.theCurrentImagePath);

        } else if (Main.windowOpen.equals("Main Images")) {
            MainImages.nextImages();

        }

    }
}
