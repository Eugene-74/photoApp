package photoapp.main;

import photoapp.main.windows.BigPreview.BigPreview;
import photoapp.main.windows.DateEdition.DateEdition;
import photoapp.main.windows.EnterValue.EnterValue;
import photoapp.main.windows.FileChooser.FileChooser;
import photoapp.main.windows.ImageEdition.ImageEdition;
import photoapp.main.windows.LocationEdition.LocationEdition;
import photoapp.main.windows.MainImages.MainImages;
import photoapp.main.windows.Parameter.Parameter;

public class CommonFunction {
    public static void back() {
        if (Main.windowOpen.equals("ImageEdition")) {
            if (ImageEdition.plusTableOpen) {
                ImageEdition.plusTable.clear();
                ImageEdition.plusPeople = false;
                ImageEdition.plusPlace = false;
                ImageEdition.bigPreview = false;

                ImageEdition.open(ImageEdition.theCurrentImageName, false);
                ImageEdition.plusTableOpen = false;
            } else {
                ImageEdition.clear();
                MainImages.open();
            }
        } else if (Main.windowOpen.equals("MainImages")) {
            if (MainImages.selectModeIsOn) {
                MainImages.selectModeIsOn = false;
                MainImages.reload();
            } else {
                MainImages.clear();
                FileChooser.open();

            }
        } else if (Main.windowOpen.equals("BigPreview")) {
            ImageEdition.bigPreview = false;
            BigPreview.clear();
            ImageEdition.open(ImageEdition.theCurrentImageName, true);
        } else if (Main.windowOpen.equals("Parameter")) {
            Parameter.clear();
            FileChooser.open();
        } else if (Main.windowOpen.equals("DateEdition")) {
            DateEdition.clear();
            ImageEdition.open(ImageEdition.theCurrentImageName, true);
        } else if (Main.windowOpen.equals("EnterValue")) {
            EnterValue.clear();
            ImageEdition.open(ImageEdition.theCurrentImageName, true);
        } else if (Main.windowOpen.equals("LocationEdition")) {
            LocationEdition.clear();
            ImageEdition.open(ImageEdition.theCurrentImageName, true);
        }
    }
}
