package photoapp.main;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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
import photoapp.main.windows.DateEdition;
import photoapp.main.windows.EnterValue;
import photoapp.main.windows.FileChooser;
import photoapp.main.windows.ImageEdition;
import photoapp.main.windows.Keybord;
import photoapp.main.windows.LoadImage;
import photoapp.main.windows.LocationEdition;
import photoapp.main.windows.MainImages;
import photoapp.main.windows.Parameter;

public class Main extends ApplicationAdapter {
	public static Stage mainStage;
	public static Preferences graphic;
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
	public static Integer x, y, x4;

	public static Integer iconSize = 100;
	public static Integer imageSize = 200;
	public static Integer littleIcon = 50;
	public static Integer zoom = 0;

	public static void main() {

	}

	public static void inigraphic() {
		x = Gdx.graphics.getWidth();
		y = Gdx.graphics.getHeight();
		x4 = x / 4;

		iconSize = (x4 - graphic.getInteger("border") * 2) / 4;

		graphic.putInteger("size of infoLabel width", 100);
		graphic.putInteger("size of infoLabel height", 10);

		graphic.putInteger("size of basic button", iconSize);

		graphic.putInteger("image load at the same time", 50);

		graphic.putInteger("size of close button", littleIcon);
		graphic.putInteger("border", 25);
		graphic.putInteger("little border", 5);

		Main.graphic.putInteger("size of date", 50);

		// Main.graphic.putInteger("size of full width", 1200);
		// Main.graphic.putInteger("size of full height",
		// Gdx.graphics.getHeight() - graphic.getInteger("border", 25) * 2);

		Main.graphic.putInteger("size of full width", 3 * x4 - graphic.getInteger("border", 25) * 2);
		Main.graphic.putInteger("size of full height",
				Gdx.graphics.getHeight() - graphic.getInteger("border", 25) * 2);

		graphic.putInteger("size of main image width", 3 * x4 - graphic.getInteger("border", 25) * 2);

		graphic.putInteger("size of preview image", y / 10);

		graphic.putInteger("number of preview image",
				graphic.getInteger("size of main image width") / graphic.getInteger("size of preview image"));

		graphic.putInteger("size of main image height",
				Gdx.graphics.getHeight() - graphic.getInteger("border", 25) * 4
						- graphic.getInteger("size of preview image")
						- Main.graphic.getInteger("size of date"));

		graphic.putInteger("size of total main images width", 3 * x4 - graphic.getInteger("border", 25) * 2);
		graphic.putInteger("size of total main images height",
				Gdx.graphics.getHeight() - graphic.getInteger("border", 25) * 2);

		graphic.putInteger("size of main images button", imageSize);
		graphic.putInteger("size of full button", imageSize);

		graphic.putInteger("number of main images width",
				graphic.getInteger("size of total main images width")
						/ graphic.getInteger("size of main images button", imageSize));

		graphic.putInteger("size of main images width",
				graphic.getInteger("number of main images width", 6)
						* graphic.getInteger("size of main images button", imageSize) - 1);

		graphic.putInteger("number of main images height",
				graphic.getInteger("size of total main images height", 3 * x4)
						/ graphic.getInteger("size of main images button", imageSize));

		graphic.putInteger("size of main images height",
				Main.graphic.getInteger("number of main images height", 9)
						* graphic.getInteger("size of main images button", imageSize) - 1);

		graphic.putInteger("number of full width",
				graphic.getInteger("size of full width", 3 * x4)
						/ graphic.getInteger("size of full button", imageSize));

		graphic.putInteger("size of full width",
				graphic.getInteger("number of full width", 6)
						* graphic.getInteger("size of full button", imageSize));

		graphic.putInteger("number of full height",
				graphic.getInteger("size of full height", 3 * x4)
						/ graphic.getInteger("size of full button", imageSize));

		graphic.putInteger("size of full height",
				Main.graphic.getInteger("number of full height", 9)
						* graphic.getInteger("size of full button", imageSize));

		Main.graphic.putInteger("image loaded when waiting in ImageEdition", 10);

		graphic.putString("text.done", " ");

		graphic.putInteger("size of links button width", littleIcon);
		graphic.putInteger("size of links button height", littleIcon);
		graphic.putInteger("size of link button", littleIcon);

		graphic.putInteger("size of size button width", littleIcon);
		graphic.putInteger("size of size button height", littleIcon);
		graphic.putInteger("size of size button", littleIcon);

		graphic.putInteger("size of big preview width",
				Gdx.graphics.getWidth() - Main.graphic.getInteger("border") * 2);
		graphic.putInteger("size of big preview height",
				Gdx.graphics.getHeight() - Main.graphic.getInteger("border") * 2);
		brightMode = graphic.getBoolean("option brightmode", false);
		// darkMode = graphic.getBoolean("option darkmode", false);

		graphic.putInteger("darkmode r", 17);
		graphic.putInteger("darkmode g", 17);
		graphic.putInteger("darkmode b", 17);

		graphic.putInteger("brightmode r", 35);
		graphic.putInteger("brightmode g", 180);
		graphic.putInteger("brightmode b", 255);

		graphic.putInteger("enter darkmode r", 30);
		graphic.putInteger("enter darkmode g", 30);
		graphic.putInteger("enter darkmode b", 30);

		graphic.putInteger("enter brightmode r", 45);
		graphic.putInteger("enter brightmode g", 190);
		graphic.putInteger("enter brightmode b", 255);

		graphic.putString("image error", "images/error.png");

	}

	public static void iconSize(boolean plus) {
		if (plus && zoom < 40) {
			imageSize = imageSize + 4;
			littleIcon = littleIcon + 1;
			zoom += 1;
		} else if (zoom > -30 && !plus) {
			imageSize = imageSize - 4;
			littleIcon = littleIcon - 1;
			zoom -= 1;
		}
		inigraphic();
		reload(false);
	}

	@Override
	public void create() {

		graphic = Gdx.app.getPreferences("graphic params");
		MixOfImage.ini();

		inigraphic();
		Text.openText("fr");

		// MixOfImage.manager.load(Main.graphic.getString("image error"),
		// Texture.class);
		// MixOfImage.manager.load("images/error.png", Texture.class);

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
		DateEdition.create();
		LocationEdition.create();

		createInfoTable();

		FileChooser.open();

	}

	public static void changeDate(String departurePath, String arrivalPath) {
		// try {
		File jpegImageFile = new File(departurePath);
		File destinationFile = new File(arrivalPath);
		try (FileOutputStream fos = new FileOutputStream(destinationFile);
				OutputStream os = new BufferedOutputStream(fos)) {

			TiffOutputSet outputSet = null;

			// note that metadata might be null if no metadata is found.

			final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			String imageName = Main.departurePathAndImageNameAndFolder(departurePath).get(1);

			ImageData imageData = ImageData
					.getImageDataIfExist(imageName);

			if (null != jpegMetadata) {
				// note that exif might be null if no Exif metadata is found.
				final TiffImageMetadata exif = jpegMetadata.getExif();

				if (null != exif) {
					// TiffImageMetadata class is immutable (read-only).
					// TiffOutputSet class represents the Exif data to write.
					//
					// Usually, we want to update existing Exif metadata by
					// changing
					// the values of a few fields, or adding a field.
					// In these cases, it is easiest to use getOutputSet() to
					// start with a "copy" of the fields read from the image.
					outputSet = exif.getOutputSet();
				}
			}

			// if file does not contain any exif metadata, we create an empty
			// set of exif metadata. Otherwise, we keep all of the other
			// existing tags.
			if (null == outputSet) {
				outputSet = new TiffOutputSet();
			}

			{

				TiffOutputDirectory exifDir = outputSet.getOrCreateExifDirectory();

				if (imageData != null) {

					if (imageData.getDate() != null) {
						exifDir.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
						exifDir.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL,
								imageData.getDate());
					}
					if (!imageData.getCoords().equals("") && imageData.getCoords() != null) {

						String[] coords = imageData.getCoords().split(":");

						String[] latt = coords[0].split("_");

						String[] latitude = latt[0].replace("-", "").replace("°", "").replace("'", "")
								.replace("\"", "").replace(",", ".").split(" ");
						Float lat = Math.abs(Float.parseFloat(latitude[0]))
								+ Math.abs(Float.parseFloat(latitude[1]) / 60)
								+ Math.abs(Float.parseFloat(latitude[2]) / 3600);
						if (coords[0].endsWith("S")) {
							lat = -lat;
						}

						String[] lonn = coords[1].split("_");
						String[] longitude = lonn[0].replace("-", "").replace("°", "").replace("'", "")
								.replace("\"", "").replace(",", ".").split(" ");
						Float lon = Math.abs(Float.parseFloat(longitude[0]))
								+ Math.abs(Float.parseFloat(longitude[1]) / 60)
								+ Math.abs(Float.parseFloat(longitude[2]) / 3600);
						if (coords[1].endsWith("W")) {
							lon = -lon;
						}

						outputSet.setGPSInDegrees(lon, lat);
					}
				}

			}

			new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os, outputSet);
		} catch (ImageWriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImageReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createMultiplexer() {
		multiplexer.addProcessor(new Keybord());
		multiplexer.addProcessor(mainStage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	public static void updateLoadingText() {

		if ((Main.infoText == "loading ." || !Main.infoText.startsWith("loading"))
				&& TimeUtils.millis() - lastTimebis >= 500) {
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
		try {

			// if (windowOpen.equals("")) {
			// FileChooser.open();
			// }
			MixOfImage.manager.update();
			if (openWindow) {
				openWindow = false;
				if (windowOpen.equals("ImageEdition")) {
					ImageEdition.create();
					ImageEdition.open(ImageEdition.theCurrentImageName, true);
				} else if (windowOpen.equals("MainImages")) {
					MainImages.open();
				} else if (windowOpen.equals("LoadImage")) {
					LoadImage.open();
				} else if (windowOpen.equals("FileChooser")) {
					FileChooser.open();
				} else if (windowOpen.equals("BigPreview")) {
					BigPreview.open(ImageEdition.theCurrentImageName);
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
					// BigPreview.laod(ImageEdition.theCurrentImageName);
				} else if (windowOpen.equals("")) {
					FileChooser.open();
				}
			}

			if (brightMode) {

				ScreenUtils.clear(Main.graphic.getInteger("brightmode r", 0) / 255f,
						Main.graphic.getInteger("brightmode g", 0) / 255f,
						Main.graphic.getInteger("brightmode b", 0) / 255f, 255 / 255f);
			} else {
				ScreenUtils.clear(Main.graphic.getInteger("darkmode r", 0) / 255f,
						Main.graphic.getInteger("darkmode g", 0) / 255f,
						Main.graphic.getInteger("darkmode b", 0) / 255f, 255 / 255f);

			}
			if (!infoText.equals(" ")) {
				labelInfoText.setText(infoText);
			}
			mainStage.act();
			mainStage.draw();

			if (!windowOpen.equals("LoadImage") && infoText.startsWith("loading")) {
				if (MixOfImage.manager.isFinished()) {
					infoTextSet(graphic.getString("text.done"), false);
				}
			}
			if (!MixOfImage.manager.isFinished()) {
				updateLoadingText();
			}

			if (!MixOfImage.willBeLoad.isEmpty()
					&& MixOfImage.isOnLoading.size() < graphic.getInteger("image load at the same time")) {
				Integer placeInLoad = graphic.getInteger("image load at the same time")
						- MixOfImage.isOnLoading.size();
				if (placeInLoad > 0) {
					if (placeInLoad > MixOfImage.willBeLoad.size()) {
						placeInLoad = MixOfImage.willBeLoad.size();
					}
					for (int i = 0; i < placeInLoad; i++) {
						MixOfImage.loadImage(MixOfImage.willBeLoad.get(0), false, false);
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
			Keybord.render();
			createCheck();

		} catch (Exception e) {
			error("render", e);
		}

	}

	private void createCheck() {
		ArrayList<List<String>> toRemove = new ArrayList<List<String>>();
		for (List<String> files : MixOfImage.toCreateImage100) {
			MixOfImage.createAnImage(files.get(0), files.get(1), files.get(2), files.get(3), 100, true, true);
			toRemove.add(files);
		}
		for (List<String> files : toRemove) {
			MixOfImage.toCreateImage100.remove(files);
		}
		// toRemove.clear();

	}

	public static void infoTextSet(String info, Boolean force) {
		if (Main.graphic.getBoolean("infoIsOn", true) || force) {
			infoText = info;
			labelInfoText.setText(info);

		}

	}

	public void createInfoTable() {
		infoTable = new Table();

		infoTable.setPosition(Main.graphic.getInteger("border") + ImageEdition.dateLabel.getWidth(),
				Gdx.graphics.getHeight() - Main.graphic.getInteger("border") * 2);

		Float size = (float) 1;
		BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/dominican.fnt"));
		myFont.getData().setScale(size);

		label1Style.font = myFont;
		label1Style.fontColor = Color.RED;
		labelInfoText = new Label(" ", label1Style);
		infoTable.setSize(graphic.getInteger("size of infoLabel width"),
				graphic.getInteger("size of infoLabel height"));
		labelInfoText.setSize(graphic.getInteger("size of infoLabel width"),
				graphic.getInteger("size of infoLabel height"));
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

	@SuppressWarnings("unchecked")
	public static void placeImage(List<String> imageNames, String prefSizeName,
			Vector2 position, Stage mainStage,
			final Consumer<Object> onClicked, final Consumer<Object> onEnter, final Consumer<Object> onExit,
			boolean isSquare, boolean inTable, boolean isMainImage, Table placeImageTable, boolean force,
			boolean setSize,
			String tip) {

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
				width = graphic.getInteger("size of " + prefSizeName, 100);
				height = graphic.getInteger("size of " + prefSizeName, 100);
				mixOfImages = new MixOfImage(imageNames, width, height, prefSizeName, force,
						isSquare);

			} else {
				width = graphic.getInteger("size of " + prefSizeName + " height");
				height = graphic.getInteger("size of " + prefSizeName + " width");

				mixOfImages = new MixOfImage(imageNames, width, height, prefSizeName,
						force,
						isSquare);

			}
			mixOfImages.setPosition(position.x, position.y + 1);

		} else {
			// mixOfImages = new MixOfImage(imageNames, 0, 0, prefSizeName, false);

			mixOfImages = new MixOfImage(imageNames, 0, 0, prefSizeName,
					force,
					isSquare);

			float widthBis = mixOfImages.getWidth();
			float heightBis = mixOfImages.getHeight();

			float w = Math
					.abs(widthBis / graphic.getInteger("size of " + prefSizeName + " width"));
			float h = Math
					.abs(heightBis / graphic.getInteger("size of " + prefSizeName + " height"));
			if (w < h) {

				height = graphic.getInteger("size of " + prefSizeName + " height");
				width = (int) (graphic.getInteger("size of " + prefSizeName + " height")
						/ heightBis
						* widthBis);

			} else {

				height = (int) (graphic.getInteger("size of " + prefSizeName + " width")
						/ widthBis
						* heightBis);
				width = graphic.getInteger("size of " + prefSizeName + " width");

			}

			mixOfImages = new MixOfImage(imageNames, width, height, prefSizeName,
					false,
					isSquare);
			mixOfImages.setPosition(0, 0);
		}
		if (rotation == 90 || rotation == 270) {
			mixOfImages.setSize(height, height * height / width);
			// TODO mal placer

		} else {
			mixOfImages.setSize(width, height);

		}

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

		if (inTable && placeImageTable != null) {
			for (@SuppressWarnings("rawtypes")
			Cell cell : placeImageTable.getCells()) {
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
			if (!Main.peopleData.containsKey(tip) && !Main.placeData.containsKey(tip)
					&& !Main.fileData.containsKey(tip)) {

				tip = graphic.getString("text " + tip, "no text");

			}
			TextTooltip textToolTip = new TextTooltip(tip, skin);
			textToolTip.setInstant(true);
			mixOfImages.addListener(textToolTip);
		}
	}

	public static Boolean isInTable(Table table, String ImageName) {

		for (@SuppressWarnings("rawtypes")
		Cell cell : table.getCells()) {
			if (cell.getActor().getName().equals(ImageName)) {

				return true;
			}
		}
		return false;
	}

	public void createCloseButton() {
		placeImage(List.of("images/round outline.png", "images/close.png"), "close button",
				new Vector2(Gdx.graphics.getWidth() - graphic.getInteger("size of " + "close button", 50),
						Gdx.graphics.getHeight() - graphic.getInteger("size of " + "close button", 50)),
				mainStage,
				(o) -> {
					System.out.println("closing");
					dispose();
					System.exit(0);
				}, null, null, true, false, false, ImageEdition.table, true, true, "close");
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
				Integer column = Main.graphic.getInteger("size of main images width")
						/ Main.graphic.getInteger("size of main images button");
				Integer row = Main.graphic.getInteger("size of main images height")
						/ Main.graphic.getInteger("size of main images button");
				range = row * column
						+ Main.graphic.getInteger("image loaded when waiting in MainImages", column * row) * 2;
				addRange = column * row / 2;

			} else if (Main.windowOpen.equals("ImageEdition")) {
				range = graphic.getInteger("number of preview image")
						+ Main.graphic.getInteger("image loaded when waiting in ImageEdition", 5);
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
			ArrayList<String> imagesInfo = new ArrayList<String>();
			File f = new File(ImageData.PEOPLE_SAVE_PATH);
			FileReader fr;
			try {
				fr = new FileReader(f);

				BufferedReader br = new BufferedReader(fr);

				String strng;

				while ((strng = br.readLine()) != null) {
					imagesInfo.add(strng);
				}

				br.close();

				if (imagesInfo.isEmpty()) {
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			ArrayList<String> imagesInfo = new ArrayList<String>();
			File f = new File(ImageData.PLACE_SAVE_PATH);
			FileReader fr;
			try {
				fr = new FileReader(f);

				BufferedReader br = new BufferedReader(fr);

				String strng;

				while ((strng = br.readLine()) != null) {
					imagesInfo.add(strng);
				}

				br.close();

				if (imagesInfo.isEmpty()) {
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (String imageInfo : imagesInfo) {
				String[] inf = imageInfo.split(":");

				placeData.put(inf[0], Integer.parseInt(inf[1]));
			}
		}
		for (@SuppressWarnings("unused")
		java.util.Map.Entry<String, Integer> entry : entriesSortedByValues(placeData, true)) {

		}

	}

	public static void openFileData() {
		FileHandle handle = Gdx.files.absolute(ImageData.FILE_SAVE_PATH);

		if (!handle.exists()) {
			return;
		} else {
			ArrayList<String> imagesInfo = new ArrayList<String>();
			File f = new File(ImageData.PLACE_SAVE_PATH);
			FileReader fr;
			try {
				fr = new FileReader(f);

				BufferedReader br = new BufferedReader(fr);

				String strng;

				while ((strng = br.readLine()) != null) {
					imagesInfo.add(strng);
				}

				br.close();

				if (imagesInfo.isEmpty()) {
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (String imageInfo : imagesInfo) {
				String[] inf = imageInfo.split(":");

				fileData.put(inf[0], Integer.parseInt(inf[1]));
			}
		}
	}

	public static void createLinkTable() {
		linkTable = new Table();

		linkTable.setSize(graphic.getInteger("size of links button width"),
				graphic.getInteger("size of links button height"));
		linkTable.setPosition(Gdx.graphics.getWidth() - linkTable.getWidth() - graphic.getInteger("little border"),
				graphic.getInteger("little border"));
		mainStage.addActor(linkTable);
	}

	public static void createSizeTable() {
		sizeTable = new Table();

		sizeTable.setSize(
				graphic.getInteger("size of size button width") * 2,
				graphic.getInteger("size of size button height"));
		sizeTable.setPosition(
				Gdx.graphics.getWidth() - sizeTable.getWidth()
						- linkTable.getWidth() - graphic.getInteger("little border"),
				graphic.getInteger("little border"));
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
				true, true, false, linkTable, true, true, "open discord");

	}

	public static void createSizeButton() {

		placeImage(List.of("images/plus.png"), "size button",
				new Vector2(0, 0),
				mainStage,
				(o) -> {
					iconSize(true);
				}, null, null,
				true, true, false, sizeTable, true, true, "plus size button");
		placeImage(List.of("images/moins.png"), "size button",
				new Vector2(0, 0),
				mainStage,
				(o) -> {
					iconSize(false);
				}, null, null,
				true, true, false, sizeTable, true, true, "minus size button");

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
				minusLat = true;
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
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	public static String nameRemoveLastFolder(String name) {
		String nameWithoutFolder = "";
		String[] ListImageName = name.split("/");
		for (int i = 0; i < ListImageName.length - 2; i++) {
			nameWithoutFolder += ListImageName[i] + "/";
		}
		nameWithoutFolder += ListImageName[ListImageName.length - 1];
		return nameWithoutFolder;
	}

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
		if (e != null) {

			String list = e.getMessage();
			for (StackTraceElement trace : e.getStackTrace()) {
				list += trace.toString() + "\n";
			}
			return list;
		}
		return null;
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

	public static void setTip(String tip, Label dateLabel) {
		if (!tip.equals("")) {
			if (!Main.peopleData.containsKey(tip) && !Main.placeData.containsKey(tip)
					&& !Main.fileData.containsKey(tip)) {

				tip = graphic.getString("text " + tip, "no text");

			}
			TextTooltip textToolTip = new TextTooltip(tip, skin);
			textToolTip.setInstant(true);
			dateLabel.addListener(textToolTip);
		}
	}

	public static void setTip(String tip, TextField dateLabel) {
		if (!tip.equals("")) {
			if (!Main.peopleData.containsKey(tip) && !Main.placeData.containsKey(tip)
					&& !Main.fileData.containsKey(tip)) {

				tip = graphic.getString("text " + tip, "no text");

			}
			TextTooltip textToolTip = new TextTooltip(tip, skin);
			textToolTip.setInstant(true);
			dateLabel.addListener(textToolTip);
		}
	}

	public static boolean max(Float a, Float b) {
		if (a > b) {
			return true;
		} else {
			return false;
		}
	}

	public static float maxValue(Float a, Float b) {
		if (a > b) {
			return a;
		} else {
			return b;
		}
	}

	public static List<String> departurePathAndImageNameAndFolder(String path) {
		String[] ListImageName = path.split("/");
		String folder = ListImageName[ListImageName.length - 2];
		String departurePath = "";
		for (int i = 0; i < ListImageName.length - 2; i++) {
			if (i != ListImageName.length - 3) {
				departurePath += ListImageName[i] + "/";

			} else {
				departurePath += ListImageName[i];

			}
		}
		String imageName = ListImageName[ListImageName.length - 1];
		return List.of(departurePath, imageName, folder);
	}

}