package photoapp.main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import photoapp.main.graphicelements.MixOfImage;
import photoapp.main.storage.ImageData;
import photoapp.main.storage.Text;
import photoapp.main.windows.BigPreview;
import photoapp.main.windows.EnterValue;
import photoapp.main.windows.FileChooser;
import photoapp.main.windows.ImageEdition;
import photoapp.main.windows.Keybord;
import photoapp.main.windows.LoadImage;
import photoapp.main.windows.MainImages;
import photoapp.main.windows.Parameter;

public class Main extends ApplicationAdapter {
	public static Stage mainStage;
	public static Preferences preferences;
	public static Table infoTable;
	public static Table linkTable, sizeTable;

	static Integer numberOfLoadedImages = 0;
	public static Label labelInfoText;
	public static List<ImageData> imagesData;

	public static Map<String, Integer> peopleData = new LinkedHashMap<>();
	public static Map<String, Integer> placeData = new LinkedHashMap<>();
	public static Map<String, Integer> fileData = new LinkedHashMap<>();

	public static Label.LabelStyle label1Style = new Label.LabelStyle();
	public static String infoText = " ";
	public static String windowOpen = "Main";
	Integer newProgress;
	public static ArrayList<String> toLoad = new ArrayList<String>();
	public static ArrayList<String> toSetSize150 = new ArrayList<String>();
	public static Long lastTime = (long) 0;
	public static Long lastTimebis = (long) 0;

	static Integer setSize150Int = 0;
	static List<String> toUnload = new ArrayList<String>();
	static Long lastTimeImageEdition = (long) 0;
	static InputMultiplexer multiplexer = new InputMultiplexer();

	public static Boolean isOnClick = false;

	public static ImageData lastImageData = null;

	public static Boolean brightMode = false;
	public static Boolean darkMode = false;
	static Skin skin;
	public static Skin skinTextField;
	public static boolean openWindow;
	public static Integer x, y, x3, y3;
	
	public static Integer iconSize = 100;
	public static Integer imageSize = 200;
	public static Integer littleIcon = 50;
	public static Integer zoom = 0;

	public static void main(){
		
	}

	public static void iniPreferences() {
		x = Gdx.graphics.getWidth();
		y = Gdx.graphics.getHeight();
		x3 = x / 3;

		preferences.putInteger("size of infoLabel width", 100);
		preferences.putInteger("size of infoLabel height", 10);

		preferences.putInteger("size of basic button", iconSize);

		preferences.putInteger("image load at the same time", 50);

		preferences.putInteger("size of close button", littleIcon);
		preferences.putInteger("border", 25);
		preferences.putInteger("little border", 5);

		// Main.preferences.putInteger("size of full width", 1200);
		// Main.preferences.putInteger("size of full height",
		// Gdx.graphics.getHeight() - preferences.getInteger("border", 25) * 2);

		Main.preferences.putInteger("size of full width", 2 * x3-preferences.getInteger("border", 25)*2);
		Main.preferences.putInteger("size of full height",
				Gdx.graphics.getHeight() - preferences.getInteger("border", 25) * 2);


		preferences.putInteger("size of main image width", 2 * x3-preferences.getInteger("border", 25)*2);
				Main.preferences.putInteger("size of preview image width", (x3*2-preferences.getInteger("border", 25)*2)/7);
				Main.preferences.putInteger("size of preview image height", preferences.getInteger("size of preview image width"));

		
		preferences.putInteger("size of main image height",
				Gdx.graphics.getHeight() - preferences.getInteger("border", 25) * 3
						- Main.preferences.getInteger("size of preview image height", imageSize));

		

		

		preferences.putInteger("size of total main images width", 2 * x3);
		preferences.putInteger("size of total main images height",
				Gdx.graphics.getHeight() - preferences.getInteger("border", 25) * 2);

		preferences.putInteger("size of main images button", imageSize);
		preferences.putInteger("size of full button", imageSize);

		preferences.putInteger("number of main images width",
				preferences.getInteger("size of total main images width", 2 * x3)
						/ preferences.getInteger("size of main images button", imageSize));

		preferences.putInteger("size of main images width",
				preferences.getInteger("number of main images width", 6)
						* preferences.getInteger("size of main images button", imageSize));

		preferences.putInteger("number of main images height",
				preferences.getInteger("size of total main images height", 2 * x3)
						/ preferences.getInteger("size of main images button", imageSize));

		preferences.putInteger("size of main images height",
				Main.preferences.getInteger("number of main images height", 9)
						* preferences.getInteger("size of main images button", imageSize));

		preferences.putInteger("number of full width",
				preferences.getInteger("size of full width", 2 * x3)
						/ preferences.getInteger("size of full button", imageSize));

		preferences.putInteger("size of full width",
				preferences.getInteger("number of full width", 6)
						* preferences.getInteger("size of full button", imageSize));

		preferences.putInteger("number of full height",
				preferences.getInteger("size of full height", 2 * x3)
						/ preferences.getInteger("size of full button", imageSize));

		preferences.putInteger("size of full height",
				Main.preferences.getInteger("number of full height", 9)
						* preferences.getInteger("size of full button", imageSize));

		Main.preferences.putInteger("image loaded when waiting in ImageEdition", 10);

		preferences.putString("text.done", " ");

		preferences.putInteger("size of links button width", littleIcon);
		preferences.putInteger("size of links button height", littleIcon);
		preferences.putInteger("size of link button", littleIcon);


		preferences.putInteger("size of size button width", littleIcon);
		preferences.putInteger("size of size button height", littleIcon);
		preferences.putInteger("size of size button", littleIcon);
		

		preferences.putInteger("size of big preview width",
				Gdx.graphics.getWidth() - Main.preferences.getInteger("border") * 2);
		preferences.putInteger("size of big preview height",
				Gdx.graphics.getHeight() - Main.preferences.getInteger("border") * 2);
		brightMode = preferences.getBoolean("option brightmode", false);
		// darkMode = preferences.getBoolean("option darkmode", false);

		preferences.putInteger("darkmode r", 17);
		preferences.putInteger("darkmode g", 17);
		preferences.putInteger("darkmode b", 17);

		preferences.putInteger("brightmode r", 35);
		preferences.putInteger("brightmode g", 180);
		preferences.putInteger("brightmode b", 255);

		preferences.putInteger("enter darkmode r", 30);
		preferences.putInteger("enter darkmode g", 30);
		preferences.putInteger("enter darkmode b", 30);

		preferences.putInteger("enter brightmode r", 45);
		preferences.putInteger("enter brightmode g", 190);
		preferences.putInteger("enter brightmode b", 255);

	}

	public static void iconSize(boolean plus) {
		if (plus && zoom<40) {
			System.out.println("plus");
			iconSize = iconSize + 2;
			imageSize = imageSize + 4;
			littleIcon = littleIcon + 1;
			zoom +=1;
		} else if (zoom >-30 && !plus) {
			System.out.println("minus");
			iconSize = iconSize - 2;
			imageSize = imageSize - 4;
			littleIcon = littleIcon - 1;
			zoom -=1;
		}
		iniPreferences();
		reload(false);
	}

	@Override
	public void create() {
		preferences = Gdx.app.getPreferences("graphic params");
		iniPreferences();
		Text.openText("en");

		MixOfImage.manager.load("images/loading button.png", Texture.class);
		MixOfImage.manager.load("images/error.png", Texture.class);

		MixOfImage.manager.finishLoading();
		mainStage = new Stage(
				new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		Gdx.graphics.setSystemCursor(SystemCursor.Hand);

		skin = new Skin(Gdx.files.internal("bitmapfont/textToolTip.json"));
		if (brightMode) {
			skinTextField = new Skin(Gdx.files.internal("bitmapfont/textFieldBrightmode.json"));
		} else {
			skinTextField = new Skin(Gdx.files.internal("bitmapfont/textFieldDarkmode.json"));
		}
		createMultiplexer();

		createCloseButton();
		createLinkButton();

		createSizeTable();
		createSizeButton();

		// ImageData.openDataOfImages();
		openPlaceData();
		openPeopleData();
		openFileData();

		System.out.println("starting");

		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH);
		if (!handle.exists()) {
			handle.mkdirs();
		}
		ImageEdition.create();
		FileChooser.create();
		MainImages.create();
		Parameter.create();
		BigPreview.create();
		EnterValue.create();

		createInfoTable();

		FileChooser.open();

		

	}

	public static void createMultiplexer() {
		multiplexer.addProcessor(new Keybord());
		multiplexer.addProcessor(mainStage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	public static void updateLoadingText() {
		if (Main.infoText == "loading ." && TimeUtils.millis() - lastTimebis >= 500) {
			Main.infoTextSet("loading ..", false);
			lastTimebis = TimeUtils.millis();

		} else if (Main.infoText == "loading .." && TimeUtils.millis() - lastTimebis >= 500) {
			Main.infoTextSet("loading ...", false);

			lastTimebis = TimeUtils.millis();

		} else if (Main.infoText == "loading ..." && TimeUtils.millis() - lastTimebis >= 500) {
			Main.infoTextSet("loading .", false);

			lastTimebis = TimeUtils.millis();
		}
	}

	@Override
	public void render() {
		// if (windowOpen.equals("")) {
		// FileChooser.open();
		// }
		MixOfImage.manager.update();
		if (openWindow) {
			openWindow = false;
			if (windowOpen.equals("ImageEdition")) {
				ImageEdition.create();
				ImageEdition.open(ImageEdition.theCurrentImagePath, true);
			} else if (windowOpen.equals("MainImages")) {
				MainImages.open();
			} else if (windowOpen.equals("LoadImage")) {
				LoadImage.open();
			} else if (windowOpen.equals("FileChooser")) {
				FileChooser.open();
			} else if (windowOpen.equals("BigPreview")) {
				BigPreview.open(ImageEdition.theCurrentImagePath);
			} else if (windowOpen.equals("")) {
				FileChooser.open();
			}
		} else {

			if (windowOpen.equals("ImageEdition")) {
				ImageEdition.render();
			} else if (windowOpen.equals("MainImages")) {
				MainImages.render();
			} else if (windowOpen.equals("LoadImage")) {
				LoadImage.render();
			} else if (windowOpen.equals("FileChooser")) {
				FileChooser.render();
			} else if (windowOpen.equals("BigPreview")) {
				BigPreview.render();
				// BigPreview.laod(ImageEdition.theCurrentImagePath);
			} else if (windowOpen.equals("")) {
				FileChooser.open();
			}
		}

		if (brightMode) {

			ScreenUtils.clear(Main.preferences.getInteger("brightmode r", 0) / 255f,
					Main.preferences.getInteger("brightmode g", 0) / 255f,
					Main.preferences.getInteger("brightmode b", 0) / 255f, 255 / 255f);
		} else {
			ScreenUtils.clear(Main.preferences.getInteger("darkmode r", 0) / 255f,
					Main.preferences.getInteger("darkmode g", 0) / 255f,
					Main.preferences.getInteger("darkmode b", 0) / 255f, 255 / 255f);

		}

		if (!infoText.equals(" ")) {
			labelInfoText.setText(infoText);
		}
		// System.out.println("act");
		mainStage.act();
		mainStage.draw();

		if (!windowOpen.equals("LoadImage")) {
			updateLoadingText();
			if (MixOfImage.manager.isFinished()) {
				infoTextSet(preferences.getString("text.done"), false);
			}
		}

		if (!MixOfImage.willBeLoad.isEmpty()
				&& MixOfImage.isOnLoading.size() < preferences.getInteger("image load at the same time")) {
			Integer placeInLoad = preferences.getInteger("image load at the same time") - MixOfImage.isOnLoading.size();
			if (placeInLoad > 0) {
				if (placeInLoad > MixOfImage.willBeLoad.size()) {
					placeInLoad = MixOfImage.willBeLoad.size();
				}
				for (int i = 0; i < placeInLoad; i++) {
					MixOfImage.loadImage(MixOfImage.willBeLoad.get(0));
					MixOfImage.willBeLoad.remove(0);
				}
			}
		}
		if (TimeUtils.millis() - lastTime > 500) {
			lastTime = TimeUtils.millis();
			List<String> toRemoveFromIsOnLoading = new ArrayList<String>();
			Boolean loadToDo = false;
			for (String imagePath : MixOfImage.isOnLoading) {
				if (MixOfImage.manager.isLoaded(imagePath)) {
					toRemoveFromIsOnLoading.add(imagePath);

				}
			}
			for (String imagePath : toRemoveFromIsOnLoading) {

				MixOfImage.isOnLoading.remove(imagePath);

				MixOfImage.isLoaded.put(imagePath,
						getImageDataIndex(imagePath.split("/")[imagePath.split("/").length - 1]));
				loadToDo = true;
			}
			if (loadToDo) {
				if (windowOpen.equals("ImageEdition")) {
					ImageEdition.load();
				}
			}
		}

	}

	public static void infoTextSet(String info, Boolean force) {
		if (Main.preferences.getBoolean("infoIsOn", true) || force) {

			labelInfoText.setText(info);
			infoText = info;
		}

	}

	public void createInfoTable() {
		infoTable = new Table();
		
		infoTable.setPosition(Main.preferences.getInteger("border")+ImageEdition.dateLabel.getWidth(), Gdx.graphics.getHeight()-Main.preferences.getInteger("border")*2);
		
		Float size = (float) 1;
		BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/dominican.fnt"));
		myFont.getData().setScale(size);
		
		label1Style.font = myFont;
		label1Style.fontColor = Color.RED;
		labelInfoText = new Label(" ", label1Style);
		infoTable.setSize(preferences.getInteger("size of infoLabel width"), preferences.getInteger("size of infoLabel height"));
		labelInfoText.setSize(preferences.getInteger("size of infoLabel width"), preferences.getInteger("size of infoLabel height"));
		infoTable.addActor(labelInfoText);

		mainStage.addActor(infoTable);
	}

	public static void clear() {
		if (windowOpen.equals("ImageEdition")) {
			ImageEdition.clear();
		} else if (windowOpen.equals("MainImages")) {
			MainImages.clear();
		} else if (windowOpen.equals("LoadImage")) {
			LoadImage.clear();
		} else if (windowOpen.equals("FileChooser")) {
			FileChooser.clear();
		}
	}

	@Override
	public void dispose() {
		ImageEdition.save();
		System.out.println("------------------ saved ---------------- ");
		mainStage.dispose();

	}

	public static void placeImage(List<String> imageNames, String prefSizeName,
			Vector2 position, Stage mainStage,
			final Consumer<Object> onClicked, final Consumer<Object> onEnter, final Consumer<Object> onExit,
			boolean isSquare, boolean inTable, boolean isMainImage, Table placeImageTable, boolean setSize,
			String tip) {

		MixOfImage mixOfImages;
		Integer width;
		Integer height;

		if (setSize) {
			if (isSquare) {
				width = preferences.getInteger("size of " + prefSizeName, 100);
				height = preferences.getInteger("size of " + prefSizeName, 100);

			} else {
				width = preferences.getInteger("size of " + prefSizeName + " height");
				height = preferences.getInteger("size of " + prefSizeName + " width");
			}
			mixOfImages = new MixOfImage(imageNames, width, height, prefSizeName);
			mixOfImages.setPosition(position.x, position.y + 1);

		} else {
			mixOfImages = new MixOfImage(imageNames, 0, 0, prefSizeName);

			float widthBis = mixOfImages.getWidth();
			float heightBis = mixOfImages.getHeight();

			float w = Math
					.abs(widthBis / preferences.getInteger("size of " + prefSizeName + " width"));
			float h = Math
					.abs(heightBis / preferences.getInteger("size of " + prefSizeName + " height"));
			if (w < h) {

				height = preferences.getInteger("size of " + prefSizeName + " height");
				width = (int) (preferences.getInteger("size of " + prefSizeName + " height")
						/ heightBis
						* widthBis);

			} else {

				height = (int) (preferences.getInteger("size of " + prefSizeName + " width")
						/ widthBis
						* heightBis);
				width = preferences.getInteger("size of " + prefSizeName + " width");

			}
			mixOfImages = new MixOfImage(imageNames, width, height, prefSizeName);
			mixOfImages.setPosition(0, 0);
		}

		mixOfImages.setSize(width, height);

		mixOfImages.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (onClicked != null) {
					onClicked.accept(null);

				}
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {

				if (onEnter != null) {

					onEnter.accept(null);
				}

			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, @Null Actor toActor) {

				if (onExit != null) {
					onExit.accept(null);
				}

			}

		});

		if (inTable) {
			for (Cell cell : placeImageTable.getCells()) {
				String[] imageNameList = imageNames.get(0).split("/");

				if (cell.getActor().getName().equals(imageNameList[imageNameList.length - 1])) {

					cell.setActor(mixOfImages);

					setTip(tip, mixOfImages);

					return;
				}
			}
			placeImageTable.add(mixOfImages);
		} else {
			if (isMainImage) {
				if (ImageEdition.currentMainImage != null) {
					ImageEdition.currentMainImage.remove();

				}
				ImageEdition.currentMainImage = mixOfImages;

				ImageEdition.mainImageTable.add(mixOfImages).align(Align.center);
			} else {
				mainStage.addActor(mixOfImages);

			}

		}

		setTip(tip, mixOfImages);

	}

	public static void setTip(String tip, MixOfImage mixOfImages) {
		
		if (!tip.equals("")) {
			if(!Main.peopleData.containsKey(tip) && !Main.placeData.containsKey(tip)&& !Main.fileData.containsKey(tip)){
			
				tip=preferences.getString("text "+tip,"no text");
			
			}
			TextTooltip textToolTip = new TextTooltip(tip, skin);
			textToolTip.setInstant(true);
			mixOfImages.addListener(textToolTip);
		}
	}

	public static Boolean isInTable(Table table, String ImageName) {

		for (Cell cell : table.getCells()) {
			if (cell.getActor().getName().equals(ImageName)) {

				return true;
			}
		}
		return false;
	}

	public void createCloseButton() {
		placeImage(List.of("images/round outline.png", "images/close.png"), "close button",
				new Vector2(Gdx.graphics.getWidth() - preferences.getInteger("size of " + "close button", 50),
						Gdx.graphics.getHeight() - preferences.getInteger("size of " + "close button", 50)),
				mainStage,
				(o) -> {
					System.out.println("closing");
					dispose();
					System.exit(0);
				}, null, null, true, false, false, ImageEdition.table, true, "close");
	}

	public static ImageData getCurrentImageData(String currentImagePath) {
		if (imagesData != null) {

			for (ImageData imageData : imagesData) {

				if ((imageData.getName())
						.equals(currentImagePath)) {
					return imageData;
				}

			}
		}

		return null;
	}

	public static Integer getImageDataIndex(String imageName) {
		int i = 0;
		if (imagesData != null) {

			for (ImageData imageData : imagesData) {

				if ((imageData.getName())
						.equals(imageName)) {
					return i;
				}
				i += 1;

			}
		}
		return null;
	}

	public static void reload(boolean returnToZero) {
		if (windowOpen.equals("ImageEdition")) {
			ImageEdition.reload(returnToZero);
		} else if (windowOpen.equals("MainImages")) {
			MainImages.reload();
		} else if (windowOpen.equals("FileChooser")) {
			FileChooser.reload();
		} else if (windowOpen.equals("LoadImage")) {
			LoadImage.reload();
		}
	}

	public static List<String> addToList(List<String> firstList, String toAdd) {
		List<String> newList = new ArrayList<String>();
		if (firstList == null) {
			firstList = new ArrayList<String>();
		}
		for (String inList : firstList) {
			newList.add(inList);

		}
		if (!newList.contains(toAdd)) {
			newList.add(toAdd);
		} else {
			return removeToList(firstList, toAdd);
		}
		return newList;
	}

	public static List<String> removeToList(List<String> firstList, String toRemove) {
		List<String> newList = new ArrayList<String>();
		for (String list : firstList) {
			if (!list.equals(toRemove)) {
				newList.add(list);
			}
		}
		return newList;
	}

	public void iniTable() {

		ImageEdition.table.add();
	}

	public static Boolean isAnImage(String imagePath) {
		if (imagePath.endsWith(".png") || imagePath.endsWith(".PNG")
				|| imagePath.endsWith(".jpg") || imagePath.endsWith(".JPG") || imagePath.endsWith(".HEIF")
				|| imagePath.endsWith(".HEVC")) {
			// imagePath.endsWith(".HEIC") ||
			return true;
		}
		return false;
	}

	/**
	 * Return the pixmap of a texture.
	 * 
	 * @param in texture
	 * @return the pixmap of a texture
	 */

	/**
	 * Resize a Pixmap.
	 * 
	 * @param inPm      Pixmap to resize
	 * @param outWidth  new width
	 * @param outheight new height
	 * @return resized Pixmap
	 */

	public static void unLoadAll() {
		MixOfImage.notToReLoadList = new ArrayList<String>();
		ArrayList<String> toUnput = new ArrayList<String>();
		for (String lookingFor : MixOfImage.manager.getAssetNames()) {
			String[] ListImageName = lookingFor.split("/");
			if (ListImageName.length > 2 && !lookingFor.split("/")[ListImageName.length - 2].equals("images")
					&& !lookingFor.split("/")[ListImageName.length - 2].equals("peoples")
					&& !lookingFor.split("/")[ListImageName.length - 2].equals("places")) {

				unLoadAnImage(lookingFor);
				toUnput.add(lookingFor);

			}

		}
		for (String imageNameI : toUnput) {
			MixOfImage.isLoaded.remove(imageNameI);
		}
	}

	public static Integer distance(Integer nbr, Integer range, Integer addRange) {
		return Math.abs(MainImages.imageI + addRange - nbr);

	}

	public static void checkToUnload(String imageName) {
		if (imageName == null) {
			Integer range = 0;
			Integer addRange = 0;
			if (Main.windowOpen.equals("MainImages")) {
				Integer column = Main.preferences.getInteger("size of main images width")
						/ Main.preferences.getInteger("size of main images button");
				Integer row = Main.preferences.getInteger("size of main images height")
						/ Main.preferences.getInteger("size of main images button");
				range = row * column
						+ Main.preferences.getInteger("image loaded when waiting in MainImages", column * row) * 2;
				addRange = column * row / 2;

			} else if (Main.windowOpen.equals("ImageEdition")) {
				range = 7 + Main.preferences.getInteger("image loaded when waiting in ImageEdition", 5);
			}

			ArrayList<String> toUnput = new ArrayList<String>();
			ArrayList<String> toUnputbis = new ArrayList<String>();

			for (ObjectMap.Entry<String, Integer> imageNameI : MixOfImage.isLoaded.entries()) {
				if (imageNameI.value != null) {

					if (distance(imageNameI.value, range, addRange) > range) {
						unLoadAnImage(imageNameI.key);
						toUnput.add(imageNameI.key);

					}

				}

			}
			for (String imagePath : MixOfImage.willBeLoad) {
				if (distance(getImageDataIndex(imagePath.split("/")[imagePath.split("/").length - 1]), range,
						addRange) > range) {
					toUnputbis.add(imagePath);

				}

			}
			for (String imageNameI : toUnput) {
				MixOfImage.isLoaded.remove(imageNameI);
			}
			for (String imageNameI : toUnputbis) {
				MixOfImage.willBeLoad.remove(imageNameI);
			}
		}

	}

	public static void unLoadAnImage(String imagePath) {
		imagePath = imagePath.replace("\\", "/");

		if (MixOfImage.manager.isLoaded(imagePath)) {
			MixOfImage.manager.unload(imagePath);
			MixOfImage.notToReLoadList.remove(imagePath);
			// use less ?
		} else {
			System.err.println("not loaded and unload ??? " + imagePath);
		}

	}

	public static void openPeopleData() {
		FileHandle handle = Gdx.files.absolute(ImageData.PEOPLE_SAVE_PATH);

		if (!handle.exists()) {
			return;
		} else {
			InputStream infos = handle.read();
			String infosString = new BufferedReader(new InputStreamReader(infos))
					.lines().collect(Collectors.joining("\n"));
			if (infosString.equals("") || infosString.equals("\n")) {
				return;
			}
			String[] imagesInfo = infosString.split("\n");
			for (String imageInfo : imagesInfo) {
				String[] inf = imageInfo.split(":");
				peopleData.put(inf[0], Integer.parseInt(inf[1]));
			}
		}
		// System.err.println(peopleData);

	}

	public static void openPlaceData() {
		FileHandle handle = Gdx.files.absolute(ImageData.PLACE_SAVE_PATH);

		if (!handle.exists()) {
			return;
		} else {
			InputStream infos = handle.read();
			String infosString = new BufferedReader(new InputStreamReader(infos))
					.lines().collect(Collectors.joining("\n"));
			if (infosString.equals("") || infosString.equals("\n")) {
				return;
			}
			String[] imagesInfo = infosString.split("\n");
			for (String imageInfo : imagesInfo) {
				String[] inf = imageInfo.split(":");

				placeData.put(inf[0], Integer.parseInt(inf[1]));
			}
		}
		for (java.util.Map.Entry<String, Integer> entry : entriesSortedByValues(placeData, true)) {

		}

	}

	public static void openFileData() {
		FileHandle handle = Gdx.files.absolute(ImageData.FILE_SAVE_PATH);

		if (!handle.exists()) {
			return;
		} else {
			InputStream infos = handle.read();
			String infosString = new BufferedReader(new InputStreamReader(infos))
					.lines().collect(Collectors.joining("\n"));
			if (infosString.equals("") || infosString.equals("\n")) {
				return;
			}
			String[] imagesInfo = infosString.split("\n");
			for (String imageInfo : imagesInfo) {
				String[] inf = imageInfo.split(":");

				fileData.put(inf[0], Integer.parseInt(inf[1]));
			}
		}
	}

	public static void createLinkTable() {
		linkTable = new Table();

		linkTable.setSize(preferences.getInteger("size of links button width"),
				preferences.getInteger("size of links button height"));
		linkTable.setPosition(Gdx.graphics.getWidth() - linkTable.getWidth() - preferences.getInteger("little border"),
				preferences.getInteger("little border"));
		mainStage.addActor(linkTable);
	}

	public static void createSizeTable() {
		sizeTable = new Table();

		sizeTable.setSize(
				preferences.getInteger("size of size button width") * 2,
				preferences.getInteger("size of size button height"));
		System.out.println(Gdx.graphics.getWidth() - sizeTable.getWidth()- linkTable.getWidth() - preferences.getInteger("little border"));
		System.out.println(Gdx.graphics.getWidth());
		sizeTable.setPosition(
				Gdx.graphics.getWidth() - sizeTable.getWidth()
						- linkTable.getWidth() - preferences.getInteger("little border"),
				preferences.getInteger("little border"));
		mainStage.addActor(sizeTable);
	}

	public static void createLinkButton() {

		createLinkTable();

		placeImage(List.of("images/discordLink.png"), "link button",
				new Vector2(0, 0),
				mainStage,
				(o) -> {
					Gdx.net.openURI("https://discord.gg/Q2HhZucmxU");

				}, null, null,
				true, true, false, linkTable, true, "open discord");

	}

	public static void createSizeButton() {

		placeImage(List.of("images/plus.png"), "size button",
				new Vector2(0, 0),
				mainStage,
				(o) -> {
					iconSize(true);
				}, null, null,
				true, true, false, sizeTable, true, "plus size button");
		placeImage(List.of("images/moins.png"), "size button",
				new Vector2(0, 0),
				mainStage,
				(o) -> {
					iconSize(false);
				}, null, null,
				true, true, false, sizeTable, true, "minus size button");

	}

	public static String timestampToDate(Long timestamp) {
		Timestamp time = new Timestamp(timestamp * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		Date date = new Date(time.getTime());
		return formatter.format(date);

	}

	public static void openInAMap(String coord) {
		try {
			Float zoom = (float) 15;
			Boolean minusLat = false;
			Boolean minusLon = false;

			String[] coords = coord.split(":");

			String[] latt = coords[0].split("_");
			if (coords[0].charAt(0) == '-') {
				minusLon = true;
			}
			String[] latitude = latt[0].replace("-", "").replace("°", "").replace("'", "")
					.replace("\"", "").replace(",", ".").split(" ");
			Float lat = Math.abs(Float.parseFloat(latitude[0])) + Math.abs(Float.parseFloat(latitude[1]) / 60)
					+ Math.abs(Float.parseFloat(latitude[2]) / 3600);

			if (coords[1].charAt(0) == '-') {
				minusLon = true;
			}
			String[] lonn = coords[1].split("_");
			String[] longitude = lonn[0].replace("-", "").replace("°", "").replace("'", "")
					.replace("\"", "").replace(",", ".").split(" ");
			Float lon = Math.abs(Float.parseFloat(longitude[0]))
					+ Math.abs(Float.parseFloat(longitude[1]) / 60)
					+ Math.abs(Float.parseFloat(longitude[2]) / 3600);

			String lettre = "";
			if (minusLat) {
				lettre = "S";
				lat = -lat;
			} else {
				lettre = "N";
			}
			String coo = latitude[0] + "%C2%B0" + latitude[1] + "'" + latitude[2] + "%22" + lettre;
			if (minusLon) {
				lettre = "W";
				lon = -lon;
			} else {
				lettre = "E";
			}
			coo += longitude[0] + "%C2%B0" + longitude[1] + "'" + longitude[2] + "%22" + lettre;

			Desktop.getDesktop().browse(
					new URI("https://www.google.com/maps/place/" + coo + "/@" + lat + "," + lon
							+ "," + zoom + "z?entry=ttu"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Given a decimal longitudinal coordinate such as <i>-79.982195</i> it will
	 * be necessary to know whether it is a latitudinal or longitudinal
	 * coordinate in order to fully convert it.
	 * 
	 * @param coord
	 *              coordinate in decimal format
	 * @return coordinate in D°M′S″ format
	 * @see <a href='https://goo.gl/pWVp60'>Geographic coordinate conversion
	 *      (wikipedia)</a>
	 */

	public static String nameWithoutToNameWithout150(String name) {
		String nameWithout150 = "";
		String[] ListImageName = name.split("/");
		for (int i = 0; i < ListImageName.length - 2; i++) {
			nameWithout150 += ListImageName[i] + "/";
		}
		nameWithout150 += ListImageName[ListImageName.length - 1];
		return nameWithout150;
	}

	// public static void sortList(OrderedMap<String, Integer> map) {
	// OrderedMap<String, Integer> result = new OrderedMap<String, Integer>();
	// for (Entry<String, Integer> entrie : map.val) {
	// result.put(entrie.key, entrie.value);
	// }

	// }
	public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map,
			Boolean reverse) {

		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
				new Comparator<Map.Entry<K, V>>() {
					@Override
					public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
						int res;
						if (reverse) {

							res = -e1.getValue().compareTo(e2.getValue());
						} else {
							res = e1.getValue().compareTo(e2.getValue());

						}
						return res != 0 ? res : 1;
					}
				});

		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}

	public static String error(String fileName, Exception e) {
		String list = e.getMessage();
		for (StackTraceElement trace : e.getStackTrace()) {
			list += trace.toString() + "\n";
		}
		return list;
	}

	public static void openFile(int type, final Consumer<Object> after, final Consumer<Object> doElse) {
		Thread threade = new Thread() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				JFileChooser chooser = new JFileChooser();
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				chooser.setFileSelectionMode(type);
				JFrame f = new JFrame();
				f.setVisible(true);
				f.toFront();
				f.setVisible(false);
				int res = chooser.showSaveDialog(f);
				f.dispose();
				f.setAlwaysOnTop(isAlive());

				File fileRessource = null;
				if (res == JFileChooser.APPROVE_OPTION) {
					if (chooser.getSelectedFile() != null) {
						fileRessource = chooser.getSelectedFile();
						if (after != null) {
							after.accept((File) fileRessource);
						}

					} else {
						Main.infoText = "export has been stoped";

					}

				} else {
					Main.infoText = "export has been stoped";

					if (doElse != null) {
						doElse.accept(fileRessource);
					}
				}
				f.dispose();

			}

		};
		threade.start();
	}
}