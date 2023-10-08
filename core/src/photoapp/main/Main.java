package photoapp.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	// static String nameOfFolderOfLoadedImages = "";
	// static String nameOfFolderOfLoadedFolder = "";
	// static Integer totalNumberOfLoadedImages = 0;
	public static Label labelInfoText;
	public static List<ImageData> imagesData;
	// public static OrderedMap<String, Texture> imagesTextureData = new
	// OrderedMap<>();
	public static OrderedMap<String, Integer> peopleData = new OrderedMap<>();
	public static OrderedMap<String, Integer> placeData = new OrderedMap<>();

	Label.LabelStyle label1Style = new Label.LabelStyle();
	public static String infoText = " ";
	public static String toReload = "";
	// public static List<String> toReloadList = List.of();
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

	// static String lastActor = "";

	// public static List<String> isPlaced = new ArrayList<String>();

	static float lastX = 0;
	public static Boolean isOnClick = false;

	public void iniPreferences() {

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

		// MainImages.create();
		// ImageEdition.imageEdtionCreate();
		// ImageEdition.imageEdtionCreate();

		FileChooser.create();
		MainImages.create();
		ImageEdition.create();
		Parameter.create();

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
			// System.out.println("..");
			lastTimebis = TimeUtils.millis();

		} else if (Main.infoText == "loading .." && TimeUtils.millis() - lastTimebis >= 500) {
			Main.infoTextSet("loading ...", false);

			// System.out.println("...");

			lastTimebis = TimeUtils.millis();

		} else if (Main.infoText == "loading ..." && TimeUtils.millis() - lastTimebis >= 500) {
			Main.infoTextSet("loading .", false);

			lastTimebis = TimeUtils.millis();
		}
	}

	@Override
	public void render() {

		Integer progress = MixOfImage.manager.getAssetNames().size;
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

		if (windowOpen.equals("Image Edition") &&
				toReload.equals("imageEdition")) {
			if (progress != newProgress || MixOfImage.manager.isFinished() && MixOfImage.isLoading) {
				if (MixOfImage.manager.isFinished()) {
					// System.out.println("reload image edi");
					ImageEdition.load();
					MixOfImage.isLoading = false;

					infoTextSet(preferences.getString("text.done"), true);
					toReload = "";
					return;
				}
				// else if (TimeUtils.millis() - lastTimeImageEdition >= 500) {
				lastTimeImageEdition = TimeUtils.millis();

				ImageEdition.load();
				MixOfImage.isLoading = true;
				toReload = "imageEdition";

				// }
			}
		} else if (toReload.equals("mainImages")) {
			// System.out.println("try --------");
			if (progress != newProgress || MixOfImage.manager.isFinished() && MixOfImage.isLoading) {
				if (MixOfImage.manager.isFinished()) {
					// MainImages.reload();
					MainImages.load();
					MixOfImage.isLoading = false;
					infoTextSet(preferences.getString("text.done"), true);
					toReload = "";
					return;

				}
				// else if (TimeUtils.millis() - lastTimeImageEdition >= 500) {
				lastTimeImageEdition = TimeUtils.millis();
				// MainImages.reload();
				MainImages.load();
				MixOfImage.isLoading = true;
				toReload = "mainImages";

				// }
			}

		}

		newProgress = progress;
		if (thread != null) {
			if (thread.getState() == State.TERMINATED && !toLoad.isEmpty()) {
				System.out.println("terminated");
				loadImagesForTheFirstTime();
			}
		}
		// if (MixOfImage.manager.isFinished()) {
		// MixOfImage.isLoading = false;
		// }

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
		if (windowOpen.equals("Image Edition")) {
			ImageEdition.save();

		}
		System.out.println("------------------ saved ---------------- ");
		mainStage.dispose();

	}

	public static void placeImage(List<String> imageNames, String prefSizeName,
			Vector2 position, Stage mainStage,
			final Consumer<Object> onClicked, final Consumer<Object> onEnter, final Consumer<Object> onExit,
			boolean isSquare, boolean inTable, boolean isMainImage, Table placeImageTable, boolean setSize) {

		MixOfImage mixOfImages = new MixOfImage(imageNames);
		if (setSize) {
			if (isSquare) {
				mixOfImages.setSize(preferences.getInteger("size of " + prefSizeName, 100),
						preferences.getInteger("size of " + prefSizeName, 100));
			} else {
				mixOfImages.setSize(preferences.getInteger("size of " + prefSizeName + " width"),
						preferences.getInteger("size of " + prefSizeName + " height"));
			}
			mixOfImages.setPosition(position.x, position.y + 1);
		} else {

			float w = Math
					.abs(mixOfImages.getWidth() / preferences.getInteger("size of " + prefSizeName + " width"));
			float h = Math
					.abs(mixOfImages.getHeight() / preferences.getInteger("size of " + prefSizeName + " height"));

			if (w < h) {

				mixOfImages.setSize(
						preferences.getInteger("size of " + prefSizeName + " height") / mixOfImages.getHeight()
								* mixOfImages.getWidth(),
						preferences.getInteger("size of " + prefSizeName + " height"));
			} else {

				mixOfImages.setSize(preferences.getInteger("size of " + prefSizeName + " width"),
						preferences.getInteger("size of " + prefSizeName + " width")
								/ mixOfImages.getWidth() * mixOfImages.getHeight());
			}

			mixOfImages.setPosition(0, 0);

		}

		mixOfImages.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (onClicked != null) {
					onClicked.accept(null);
				}
			}

			public void touchDragged(InputEvent event, float x, float y, int pointerIndex) {

				if (onClicked != null) {
					onClicked.accept(null);
				}
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				System.out.println(x + "lastX : " + lastX);
				if (pointer == 0) {
					isOnClick = true;

				} else {
					isOnClick = false;
				}
				if (onEnter != null && (((x - lastX) >= 10) || ((x - lastX) <= 10))) {
					// marche pas

					// if (lastX == x) {
					// } else {
					lastX = x;

					// }
					// System.err.println("do");
					onEnter.accept(null);
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, @Null Actor toActor) {

				// System.out.println("exit");

				if (onExit != null) {
					onExit.accept(null);
				}
			}

		});

		if (inTable) {
			for (Cell cell : placeImageTable.getCells()) {
				String[] imageNameList = imageNames.get(0).split("/");
				if (cell.getActor().getName().equals(imageNameList[imageNameList.length - 1])) {
					// le faire que si c'est pas deja charger !!!
					String path;
					if (preferences.getInteger("size of " + prefSizeName, 0) == 150) {
						path = "/150/";

					} else {
						path = "/";
					}
					// if (MixOfImage.manager
					// .isLoaded(ImageData.IMAGE_PATH + path + imageNameList[imageNameList.length -
					// 1])
					// && !Main.isPlaced
					// .contains(ImageData.IMAGE_PATH + path + imageNameList[imageNameList.length -
					// 1])) {

					cell.setActor(mixOfImages);
					// Main.isPlaced.add(ImageData.IMAGE_PATH + path +
					// imageNameList[imageNameList.length - 1]);
					// }
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
		for (ImageData imageData : imagesData) {

			if ((imageData.getName())
					.equals(currentImagePath)) {
				return imageData;
			}

		}

		return null;
	}

	public static void reload(boolean returnToZero) {
		if (windowOpen.equals("Image Edition")) {
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
							// nameOfFolderOfLoadedImages = "All files loaded";

							// reload(false);

						}

					}
					f.dispose();

				}
			}

		};
		thread.start();
		// thread.getState();

	}

	public static void openImageInAFile(File dir) {
		FileHandle from = Gdx.files.absolute(dir.toString());
		byte[] data = from.readBytes();

		FileHandle to = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + dir.getName());
		to.writeBytes(data, false);

		openImageExif(dir.getName());

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
			// numberOfLoadedImages = 0;

		}
		// infoTextSet("All files have been load, please refresh");
		// loadImagesForTheFirstTime();

	}

	public static void openImageExif(String imagePath) {
		FileHandle file;
		try {
			file = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + imagePath);

			Metadata metadata = ImageMetadataReader.readMetadata(file.read());
			ImageData imageData = ImageData.getImageDataIfExist(imagePath);
			for (Directory dir : metadata.getDirectories()) {
				if (dir != null && dir.getName() != null && dir.getName().equals("Exif SubIFD")
						|| dir.getName().equals("GPS")) {

					for (Tag tag : dir.getTags()) {
						if (tag.getTagName().equals("Date/Time Original")) {
							imageData.setDate(tag.getDescription());
						}
					}
				}
			}
			imageData.setCoords(List.of());
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

		// MixOfImage.manager.finishLoading();

		setSize150AfterLoad(imagePath);
		// MixOfImage.manager.update();
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

	// if (imageName.endsWith(".JPG")) {
	// fileName = imageName.replace(".JPG", "-100.JPG");
	// } else if (imageName.endsWith(".png")) {
	// fileName = imageName.replace(".png", "-100.png");
	// }
	// else if (imageName.endsWith(".JPG")) {
	// fileName = imageName.replace(".jpg", "-100.jpg");
	// } else if (imageName.endsWith(".PNG")) {
	// fileName = imageName.replace(".png", "-100.png");
	// }

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
			// if (inPm.getWidth() > inPm.getHeight()) {
			// size = inPm.getWidth();
			// } else if (inPm.getWidth() < inPm.getHeight()) {
			// size = inPm.getHeight();
			// } else {
			// size = inPm.getWidth();
			// }
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
		for (String lookingFor : MixOfImage.manager.getAssetNames()) {
			String[] ListImageName = lookingFor.split("/");
			// String fileName = ImageData.IMAGE_PATH + "/" +
			// ListImageName[ListImageName.length -
			// 1];
			if (!lookingFor.split("/")[ListImageName.length - 2].equals("images")
					&& !lookingFor.split("/")[ListImageName.length - 2].equals("peoples")
					&& !lookingFor.split("/")[ListImageName.length - 2].equals("places")) {
				MixOfImage.manager.unload(lookingFor);

			}

		}
	}

	public static void unLoadAnImage(String imagePath) {
		imagePath = imagePath.replace("\\", "/");
		Set<String> toRemove = new HashSet<>();
		if (!toUnload.isEmpty()) {
			for (String unLoad : toUnload) {
				if (MixOfImage.manager.isLoaded(unLoad)) {
					MixOfImage.manager.unload(unLoad);
					MixOfImage.notToReLoadList.remove(unLoad);
					toRemove.add(unLoad);
				}
			}
		}
		toUnload.removeAll(toRemove);
		if (MixOfImage.manager.isLoaded(imagePath)) {

			MixOfImage.manager.unload(imagePath);
			MixOfImage.notToReLoadList.remove(imagePath);

		} else {
			toUnload.add(imagePath);
		}
		String[] ListImageName = imagePath.split("/");
		String fileName = ImageData.IMAGE_PATH + "/150/" + ListImageName[ListImageName.length - 1];

		if (MixOfImage.manager.isLoaded(fileName, Texture.class)) {

			MixOfImage.manager.unload(fileName);
			MixOfImage.notToReLoadList.remove(fileName);

		} else {
			toUnload.add(fileName);
		}

	}

	public static void openPeopleData() {
		FileHandle handle = Gdx.files.absolute(ImageData.PEOPLE_SAVE_PATH);

		if (!handle.exists()) {
			// return new ArrayList<>();
			return;
		} else {
			InputStream infos = handle.read();
			String infosString = new BufferedReader(new InputStreamReader(infos))
					.lines().collect(Collectors.joining("\n"));
			if (infosString.equals("") || infosString.equals("\n")) {
				// return new ArrayList<>();
				return;
			}
			String[] imagesInfo = infosString.split("\n");
			// List<ImageData> imagesData = new ArrayList<>();
			for (String imageInfo : imagesInfo) {
				String[] inf = imageInfo.split(":");

				peopleData.put(inf[0], Integer.parseInt(inf[1]));
			}
		}
	}

	public static void openPlaceData() {
		FileHandle handle = Gdx.files.absolute(ImageData.PLACE_SAVE_PATH);

		if (!handle.exists()) {
			// return new ArrayList<>();
			return;
		} else {
			InputStream infos = handle.read();
			String infosString = new BufferedReader(new InputStreamReader(infos))
					.lines().collect(Collectors.joining("\n"));
			if (infosString.equals("") || infosString.equals("\n")) {
				// return new ArrayList<>();
				return;
			}
			String[] imagesInfo = infosString.split("\n");
			// List<ImageData> imagesData = new ArrayList<>();
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
}

// update image by cell and not all