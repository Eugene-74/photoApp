package photoapp.main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Thread.State;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import photoapp.main.graphicelements.MixOfImage;
import photoapp.main.storage.ImageData;
import photoapp.main.windows.BigPreview;
import photoapp.main.windows.FileChooser;
import photoapp.main.windows.ImageEdition;
import photoapp.main.windows.Keybord;
import photoapp.main.windows.MainImages;
import photoapp.main.windows.Parameter;

public class Main extends ApplicationAdapter {
	public static Stage mainStage;
	public static Preferences preferences;
	public static Table infoTable;
	public static Table linkTable;
	static Integer numberOfLoadedImages = 0;
	public static Label labelInfoText;
	public static List<ImageData> imagesData;
	public static OrderedMap<String, Integer> peopleData = new OrderedMap<>();
	public static OrderedMap<String, Integer> placeData = new OrderedMap<>();

	Label.LabelStyle label1Style = new Label.LabelStyle();
	public static String infoText = " ";
	public static String toReload = "";
	public static String windowOpen = "Main";
	Integer newProgress;
	public static ArrayList<String> toLoad = new ArrayList<String>();
	public static ArrayList<String> toSetSize150 = new ArrayList<String>();
	static Thread thread = null;
	public static Long lastTime = (long) 0;
	public static Long lastTimebis = (long) 0;

	static Integer setSize150Int = 0;
	static List<String> toUnload = new ArrayList<String>();
	static Long lastTimeImageEdition = (long) 0;
	static InputMultiplexer multiplexer = new InputMultiplexer();

	static float lastX = 0;
	public static Boolean isOnClick = false;

	public static ImageData lastImageData = null;

	public void iniPreferences() {
		preferences.putInteger("image load at the same time", 20);

		preferences.putString("text.done", " ");
		preferences.putInteger("size of main images button", 150);

		preferences.putInteger("size of main images height",
				preferences.getInteger("size of main images button", 150) * 6);
		preferences.putInteger("size of main images width",
				preferences.getInteger("size of main images button", 150) * 9);
		preferences.putInteger("size of close button", 50);
		preferences.putInteger("border", 25);
		preferences.putInteger("little border", 5);

		preferences.putInteger("size of links button width", 50);
		preferences.putInteger("size of links button height", 50);

		preferences.putInteger("size of link button", 50);

		preferences.putInteger("size of big preview width",
				Gdx.graphics.getWidth() - Main.preferences.getInteger("border") * 2);
		preferences.putInteger("size of big preview height",
				Gdx.graphics.getHeight() - Main.preferences.getInteger("border") * 2);

	}

	@Override
	public void create() {
		preferences = Gdx.app.getPreferences("graphic params");
		iniPreferences();

		MixOfImage.manager.load("images/loading button.png", Texture.class);
		MixOfImage.manager.load("images/error.png", Texture.class);
		MixOfImage.manager.finishLoading();

		mainStage = new Stage(
				new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		Gdx.graphics.setSystemCursor(SystemCursor.Hand);
		createMultiplexer();

		createInfoTable();
		createCloseButton();
		createLinkButton();

		ImageData.openDataOfImages();
		openPlaceData();
		openPeopleData();

		System.out.println("starting");

		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH);
		if (!handle.exists()) {
			handle.mkdirs();
		}

		FileChooser.create();
		MainImages.create();
		ImageEdition.create();
		Parameter.create();
		BigPreview.create();

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
		Integer progress = MixOfImage.willBeLoad.size();
		MixOfImage.manager.update();

		ScreenUtils.clear(151 / 255f, 0 / 255f, 151 / 255f, 255 / 255f);
		if (!infoText.equals(" ")) {
			labelInfoText.setText(infoText);
		}
		mainStage.act();
		mainStage.draw();

		updateLoadingText();

		if (MixOfImage.manager.isFinished()) {
			infoTextSet(preferences.getString("text.done"), true);
		}

		if (progress != newProgress && !MixOfImage.LoadingList.isEmpty()) {
			for (String imagePath : MixOfImage.LoadingList) {

				if (setSize150AfterLoad(imagePath)) {
					setSize150Int += 1;
					MixOfImage.manager.unload(imagePath);
					infoTextSet("Please wait ! Loaded images : " + setSize150Int, true);
					if (toLoad.isEmpty()) {
						infoTextSet(setSize150Int + " images have been load / " + numberOfLoadedImages, true);
						reload(true);
					}
				}
			}
			MixOfImage.firstLoading = false;
		}

		newProgress = progress;
		if (thread != null) {
			if (thread.getState() == State.TERMINATED && !toLoad.isEmpty()) {
				System.out.println("terminated");
				loadImagesForTheFirstTime();
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
		infoTable.setPosition(10, 10);

		BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/dominican.fnt"));
		label1Style.font = myFont;
		label1Style.fontColor = Color.RED;
		labelInfoText = new Label(" ", label1Style);
		infoTable.addActor(labelInfoText);

		mainStage.addActor(infoTable);
	}

	public void clear() {

		mainStage.clear();
	}

	@Override
	public void dispose() {
		if (windowOpen.equals("ImageEdition")) {
			ImageEdition.save();

		}
		System.out.println("------------------ saved ---------------- ");
		mainStage.dispose();

	}

	public static void placeImage(List<String> imageNames, String prefSizeName,
			Vector2 position, Stage mainStage,
			final Consumer<Object> onClicked, final Consumer<Object> onEnter, final Consumer<Object> onExit,
			boolean isSquare, boolean inTable, boolean isMainImage, Table placeImageTable, boolean setSize) {

		MixOfImage mixOfImages;
		Integer width;
		Integer height;
		Integer rotation = 0;
		String[] ListImageName = imageNames.get(0).split("/");
		if (Main.getCurrentImageData(ListImageName[ListImageName.length - 1]) != null) {

			rotation = Main.getCurrentImageData(ListImageName[ListImageName.length - 1]).getRotation();

		}
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
				// System.out.println(
				// imageNameList[imageNameList.length - 1] + "---------------" +
				// cell.getActor().getName());
				if (cell.getActor().getName().equals(imageNameList[imageNameList.length - 1])) {

					cell.setActor(mixOfImages);

					return;
				}
			}
			placeImageTable.add(mixOfImages);
		} else

		{
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
				}, null, null, true, false, false, ImageEdition.table, true);
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
		}
		if (windowOpen.equals("Main Images")) {
			MainImages.reload();
		}
	}

	public static List<String> addToList(List<String> firstList, String toAdd) {
		List<String> newList = new ArrayList<String>();

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

	public static void openFile() {
		thread = new Thread() {
			@Override
			public void run() {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				JFrame f = new JFrame();
				f.setVisible(true);
				f.toFront();
				f.setVisible(false);
				int res = chooser.showSaveDialog(f);
				f.dispose();
				File fileRessource = null;
				if (res == JFileChooser.APPROVE_OPTION) {
					if (chooser.getSelectedFile() != null) {
						fileRessource = chooser.getSelectedFile();
						if (fileRessource.toString().endsWith(".png") || fileRessource.toString().endsWith(".PNG")
								|| fileRessource.toString().endsWith(".jpg")
								|| fileRessource.toString().endsWith(".JPG")) {
							openImageInAFile(fileRessource);
						} else {
							openImageOfAFile(fileRessource);
							infoTextSet("All files loaded", true);

						}

					}
					f.dispose();

				}
			}

		};
		thread.start();

	}

	public static void openImageInAFile(File dir) {
		FileHandle from = Gdx.files.absolute(dir.toString());
		byte[] data = from.readBytes();

		FileHandle to = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + dir.getName());
		to.writeBytes(data, false);

		openImageExif(dir.getName());

		FileHandle fromJson = Gdx.files.absolute(dir.toString() + ".json");
		if (fromJson.exists()) {
			byte[] dataJson = fromJson.readBytes();

			FileHandle toJson = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + dir.getName() + ".json");
			toJson.writeBytes(dataJson, false);
		}

		setSize150(ImageData.IMAGE_PATH + "/" + dir.getName(), dir.getName());

	}

	public static void loadImagesForTheFirstTime() {
		Integer index = 0;
		for (String imagePath : toLoad) {
			String[] nameList = imagePath.split("/");
			String name = nameList[nameList.length - 1];

			FileHandle from = Gdx.files.absolute(imagePath);
			byte[] data = from.readBytes();

			FileHandle to = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + name);
			to.writeBytes(data, false);

			FileHandle fromJson = Gdx.files.absolute(imagePath + ".json");
			if (fromJson.exists()) {
				byte[] dataJson = fromJson.readBytes();

				FileHandle toJson = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + name + ".json");
				toJson.writeBytes(dataJson, false);
			}

			openImageExif(name);
			setSize150(ImageData.IMAGE_PATH + "/" + name, name);
			index += 1;
		}
		toLoad = new ArrayList<String>();

	}

	public static void openImageOfAFile(File dir) {

		File[] liste = dir.listFiles();
		if (liste != null) {
			for (File item : liste) {

				if (item.isFile()) {
					infoText = "Loading the images : " + numberOfLoadedImages
							+ " images load";

					if (item.getName().endsWith(".png") || item.getName().endsWith(".PNG")
							|| item.getName().endsWith(".jpg") || item.getName().endsWith(".JPG")) {

						toLoad.add(dir + "/" + item.getName());
						numberOfLoadedImages += 1;
					}

					else {
					}
				} else if (item.isDirectory()) {

					openImageOfAFile(item);

				}
			}

		}

	}

	public static void openImageExif(String imagePath) {
		FileHandle file;
		try {
			file = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + imagePath);
			FileHandle fileJson = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + imagePath + ".json");

			if (fileJson.exists()) {
				System.out.println("google image");
			}

			Metadata metadata = ImageMetadataReader.readMetadata(file.read());
			ImageData imageData = ImageData.getImageDataIfExist(imagePath);
			String coords = "";
			Integer rotation = 0;

			for (Directory dir : metadata.getDirectories()) {
				// System.out.println(dir.getTags());
				// for (Tag tag : dir.getTags()) {
				// System.out.println(tag);
				// }

				if (dir != null && dir.getName() != null && dir.getName().equals("Exif SubIFD")) {
					for (Tag tag : dir.getTags()) {
						if (tag.getTagName().equals("Date/Time Original")) {
							imageData.setDate(tag.getDescription());
						}
					}
				} else if (dir != null && dir.getName() != null && dir.getName().equals("GPS")) {
					String lat = "";
					String lon = "";
					String minusLat = "";
					String minusLon = "";

					for (Tag tag : dir.getTags()) {
						if (tag.getTagName().equals("GPS Latitude")) {
							lat = tag.getDescription();

						} else if (tag.getTagName().equals("GPS Longitude")) {
							lon = tag.getDescription();
						} else if (tag.getTagName().equals("GPS Latitude Ref")) {

							minusLat = tag.getDescription();

						} else if (tag.getTagName().equals("GPS Longitude Ref")) {
							minusLon = tag.getDescription();

						}
					}
					if (lat == "" && lon == "" && minusLat == "" && minusLon == "") {
						coords = "";
					} else {
						coords = lat + "_" + minusLat + ":" + lon + "_" + minusLon;
					}
				} else if (dir != null && dir.getName() != null && dir.getName().equals("Exif IFD0")) {
					for (Tag tag : dir.getTags()) {
						if (tag.getTagName().equals("Orientation")) {
							if (tag.getDescription().contains("180")) {
								rotation = 180;
							} else if (tag.getDescription().contains("90")) {
								rotation = 90;
							} else if (tag.getDescription().contains("270")) {
								rotation = 270;
							}
						}
					}

				}
			}
			if (imageData.getRotation() == 0) {
				imageData.setRotation(rotation);
			}
			if (imageData.getCoords() == null) {
				imageData.setCoords(coords);
			}
			if (imageData.getName() == null) {
				imageData.setName(imagePath);
			}
			if (imageData.getPeoples() == null) {
				imageData.setPeoples(List.of());
			}
			if (imageData.getPlaces() == null) {
				imageData.setPlaces(List.of());
			}
			if (imageData.getLoved() == null || imageData.getLoved() == false) {
				imageData.setLoved(false);
			}
			addImageData(imageData);

			ImageData.saveImagesData();

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
		}
	}

	public static void addImageData(ImageData imgd) {
		if (imagesData == null) {
			imagesData = new ArrayList<>();
		}
		if (imagesData != null && imgd != null && !imagesData.contains(imgd)) {
			imagesData.add(imgd);
		}
	}

	public static void setSize150Force(String imagePath, String imageName) {
		setSize150(imagePath, imageName);
		MixOfImage.manager.finishLoading();
		setSize150AfterLoad(imagePath);
	}

	public static void setSize150(String imagePath, String imageName) {
		FileHandle handlebis = Gdx.files.absolute(ImageData.IMAGE_PATH + "/150/" + imageName);
		if (!handlebis.exists()) {

			MixOfImage.isInImageData(imagePath, false, "firstloading");
		} else {

			numberOfLoadedImages += 1;
		}
	}

	public static Boolean setSize150AfterLoad(String imagePath) {
		if (MixOfImage.manager.isLoaded(imagePath, Texture.class)) {

			String[] nameList = imagePath.split("/");
			String imageName = nameList[nameList.length - 1];

			Texture texture = MixOfImage.isInImageData(imagePath, true, "firstloading");

			Pixmap pixmap = resize(textureToPixmap(texture), 150, 150, true);
			FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/150");

			if (!handle.exists()) {
				handle.mkdirs();
			}
			String[] ListImageName = imageName.split("/");

			String fileName = ImageData.IMAGE_PATH + "/150/" + ListImageName[ListImageName.length - 1];
			FileHandle fh = new FileHandle(fileName);

			PixmapIO.writePNG(fh, pixmap);
			pixmap.dispose();
			MixOfImage.LoadingList = removeToList(MixOfImage.LoadingList, imagePath);
			toLoad.remove(imagePath);
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
	public static Pixmap textureToPixmap(Texture in) {
		// get image as pixmap
		if (!in.getTextureData().isPrepared()) {
			in.getTextureData().prepare();
		}
		return in.getTextureData().consumePixmap();
	}

	/**
	 * Resize a Pixmap.
	 * 
	 * @param inPm      Pixmap to resize
	 * @param outWidth  new width
	 * @param outheight new height
	 * @return resized Pixmap
	 */
	public static Pixmap resize(Pixmap inPm, int outWidth, int outheight, boolean cut) {
		Pixmap outPm = new Pixmap(outWidth, outheight, Pixmap.Format.RGBA8888);
		int srcWidth;
		int srcHeigth;
		int srcx = 0;
		int srcy = 0;
		if (cut) {
			int size = Math.min(inPm.getWidth(), inPm.getHeight());

			srcWidth = size;
			srcHeigth = size;
			srcx = (inPm.getWidth() - size) / 2;
			srcy = (inPm.getHeight() - size) / 2;

		} else {
			srcWidth = inPm.getWidth();
			srcHeigth = inPm.getHeight();
		}
		outPm.drawPixmap(inPm, srcx, srcy, srcWidth, srcHeigth, 0, 0, outWidth, outheight);
		inPm.dispose();
		return outPm;
	}

	public static void unLoadAll() {
		MixOfImage.notToReLoadList = new ArrayList<String>();
		ArrayList<String> toUnput = new ArrayList<String>();
		for (String lookingFor : MixOfImage.manager.getAssetNames()) {
			String[] ListImageName = lookingFor.split("/");

			if (!lookingFor.split("/")[ListImageName.length - 2].equals("images")
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
				range = row * column;
				addRange = row * column / 2;
			} else if (Main.windowOpen.equals("ImageEdition")) {
				range = 7;
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
	}

	public static void createLinkTable() {
		linkTable = new Table();

		linkTable.setSize(preferences.getInteger("size of links button width"),
				preferences.getInteger("size of links button height"));
		linkTable.setPosition(Gdx.graphics.getWidth() - linkTable.getWidth() - preferences.getInteger("little border"),
				preferences.getInteger("little border"));
		mainStage.addActor(linkTable);
	}

	public static void createLinkButton() {

		createLinkTable();

		placeImage(List.of("images/discordLink.png"), "link button",
				new Vector2(0, 0),
				mainStage,
				(o) -> {
					Gdx.net.openURI("https://discord.gg/Q2HhZucmxU");

				}, null, null,
				true, true, false, linkTable, true);

	}

	public static void date(String args[]) {
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			// degree = (int) Math.floor(Math.abs(coords.get(1)));
			// minute = (int) Math.floor((Math.abs(coords.get(1)) - degree) * 60);
			// seconde = ((((Math.abs(coords.get(1)) - degree) * 60 - minute))) * 60;
			// if (coords.get(1) < 0) {
			// lettre = "W";
			// } else {
			// lettre = "E";
			// }
			// coord += "+" + degree + "%C2%B0" + minute + "'" + seconde + "%22" + lettre;
			Desktop.getDesktop().browse(
					new URI("https://www.google.com/maps/place/" + coo + "/@" + lat + "," + lon
							+ "," + zoom + "z?entry=ttu"));

			// https://www.google.com/maps/place/49%C2%B038'47.5%22N+1%C2%B037'31.1%22W/@49.6465236,-1.6278672,17z/entry=ttu

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}