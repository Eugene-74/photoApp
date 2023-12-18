package photoapp.main.windows;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import photoapp.main.CommonButton;
import photoapp.main.Main;
import photoapp.main.graphicelements.MixOfImage;
import photoapp.main.storage.ImageData;

public class ImageEdition {
	final static String fileName = "ImageEdition";
	public static Actor currentMainImage;
	public static Table table;
	public static Table mainImageTable;
	public static Table plusTable;

	static Table previewTable;
	Table infoTable = Main.infoTable;
	static Integer numberOfLoadedImages = 0;
	static String nameOfFolderOfLoadedImages = "";
	static String nameOfFolderOfLoadedFolder = "";
	static Integer totalNumberOfLoadedImages = 0;
	static Array<ImageData> toDelete = new Array<ImageData>();
	public static String theCurrentImagePath;
	Label.LabelStyle label1Style = new Label.LabelStyle();
	static String lastPreview = "";
	public static String imageOpen = "";
	public static Boolean doNotLoad = false;
	public static Boolean reloadOnce = false;

	static Table dateTable;
	static Label dateLabel;
	static Label.LabelStyle datelabelStyle = new Label.LabelStyle();

	static public Long lastImageChange = (long) 0;
	static public Boolean imageWithGoodQuality = false;
	// static public String imageQualityPath;

	static public Boolean plusTableOpen = false;
	static public String lastImage = "";
	static Integer indexLoaded = 0;

	public static Boolean plusPeople = false;
	public static Boolean plusPlace = false;
	public static Boolean bigPreview = false;

	public static void create() {
		Gdx.app.log(fileName, "create");

		Main.preferences.flush();

		createMainImageTable();
		createPreviewTable();
		createTable();
		createPlusTable();

		createDateTable();

	}

	private static void createDateTable() {
		dateTable = new Table();

		BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/dominican.fnt"));
		Float size = (float) 0.5;
		myFont.getData().setScale(size);
		datelabelStyle.font = myFont;
		datelabelStyle.fontColor = Color.BLACK;
		dateLabel = new Label(" ", datelabelStyle);
		dateTable.setPosition(10, Gdx.graphics.getHeight() - 10 - dateLabel.getHeight());

		Main.mainStage.addActor(dateTable);
	}

	public static void open(String currentImagePath, boolean OpenMain) {
		Gdx.app.log(fileName, "open");
		ImageData imageData = Main.getCurrentImageData(currentImagePath);

		Main.windowOpen = "ImageEdition";

		dateTable.addActor(dateLabel);

		theCurrentImagePath = currentImagePath;
		imageWithGoodQuality = false;
		table.clear();
		previewTable.clear();

		if (imageData.getDate() != null) {
			try {

				String[] nomMois = { "January", "February", "March", "April", "May", "June", "July",
						"August", "Septembre", "Octobrer", "Novembrer", "Decembrer" };
				String date = "";
				String[] dateSplit = imageData.getDate().split(" ");
				String[] daySplit = dateSplit[0].split(":");
				String[] hourSplit = dateSplit[1].split(":");
				date += daySplit[2] + "  " + nomMois[Integer.parseInt(daySplit[1]) - 1] + "  " + daySplit[0];
				date += "\n";
				date += hourSplit[0] + "h " + hourSplit[1] + "min " + hourSplit[2] + "s ";

				// dateTable.addActor(dateLabel);
				dateLabel.setText(date);
			} catch (Exception e) {
				System.err.println("bug when loading the image");
			} finally {
			}
		} else {
			dateLabel.setText("no date");
		}

		if (MainImages.imagesTable != null) {
			MainImages.imagesTable.clear();
		}
		if (MainImages.mainTable != null) {
			MainImages.mainTable.clear();
		}
		if (OpenMain) {
			openMainImage(currentImagePath, false);

		}

		placePreviewImage(currentImagePath);
		// TODO rotation of the images fo place and people
		placeImageOfPeoples(currentImagePath);
		placePlusPeople();
		placeAddPeople();

		table.row();
		placeImageOfPlaces(currentImagePath);
		placePlusPlace();
		placeAddPlace();

		table.row();
		ArrayList<String> loveList = new ArrayList<String>();
		loveList.add("images/love.png");
		loveList.add("images/outline.png");

		if (imageData.getLoved()) {
			loveList.add("images/yes.png");
		} else {
			loveList.add("images/no.png");
		}
		Main.placeImage(loveList, "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					// ImageData imageData = Main.getCurrentImageData(theCurrentImagePath);
					if (imageData.getLoved()) {
						imageData.setLoved(false);
					} else {
						imageData.setLoved(true);
					}
					reload(false);
				}, null, null,
				true, true, false, table, true, "love");
		table.row();
		Main.placeImage(List.of("images/previous.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					previousImage(currentImagePath);
				}, null, null,
				true, true, false, table, true, "previous image");

		List<String> deletImages = new ArrayList<>();
		deletImages.add("images/delete.png");
		deletImages.add("images/outline.png");

		if (toDelete.contains(imageData, false)) {
			deletImages.add("images/yes.png");
		}
		Main.placeImage(deletImages, "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					if (imageData != null) {

						Integer index = 0;
						if (!toDelete.isEmpty()) {
							for (ImageData delet : toDelete) {
								if (delet.equals(imageData)) {

									toDelete.removeIndex(index);
									open(currentImagePath, true);
									return;

								}
								index += 1;
							}
							toDelete.add(imageData);

						}

						if (index == 0) {
							toDelete.add(imageData);

						}
						open(currentImagePath, true);
					} else {
						Gdx.app.error(fileName, "error null");
					}
				}, null, null,
				true, true, false, table, true, "delete the image");

		Main.placeImage(List.of("images/next.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					nextImage(currentImagePath);
				}, null, null, true, true, false, table, true, "next image");
		table.row();
		Main.placeImage(List.of("images/right.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					ImageData rotaImageData = Main.getCurrentImageData(theCurrentImagePath);
					Integer rotation = rotaImageData.getRotation();
					rotation -= 90;
					if (rotation < 0) {
						rotation += 360;
					}
					rotaImageData.setRotation(rotation);
					ImageData.saveImagesData();
					load();
				}, null, null,
				true, true, false, table, true, "rotate to the right");
		Main.placeImage(List.of("images/left.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					ImageData rotaImageData = Main.getCurrentImageData(theCurrentImagePath);
					Integer rotation = rotaImageData.getRotation();
					rotation += 90;
					if (rotation > 360) {
						rotation -= 360;
					}
					rotaImageData.setRotation(rotation);
					ImageData.saveImagesData();
					load();

				}, null, null,
				true, true, false, table, true, "rotate to the left");
		table.row();

		if (imageData.getCoords() != null && !imageData.getCoords().equals(" ") && !imageData.getCoords().equals("")) {
			table.row();
			Main.placeImage(List.of("images/map.png", "images/outline.png"), "basic button",
					new Vector2(0, 0),
					Main.mainStage,
					(o) -> {
						String coords = imageData.getCoords();
						Main.openInAMap(coords);
					}, null, null,
					true, true, false, table, true, "open the map");
		}
		table.row();

		// table.row();

		CommonButton.createSaveButton(table);
		CommonButton.createRefreshButton(table);
		CommonButton.createAddImagesButton(table);
		CommonButton.createBack(table);

		if (plusPeople) {
			openPlusPeople();
		} else if (plusPlace) {
			openPlusPlace();
		} else if (bigPreview) {
			BigPreview.open(currentImagePath);
		}

	}

	public static void reload(boolean returnToZero) {
		Gdx.app.log(fileName, "reload");

		plusTable.clear();
		Main.windowOpen = "ImageEdition";
		// ImageData.openDataOfImages();

		if (returnToZero) {
			open(Main.imagesData.get(0).getName(), true);
		} else {
			open(theCurrentImagePath, true);
		}
	}

	public static void clear() {
		Gdx.app.log(fileName, "clear");

		MixOfImage.stopLoading();
		plusTable.clear();
		previewTable.clear();
		table.clear();
		mainImageTable.clear();
		dateTable.clear();

	}

	public static void load() {
		Gdx.app.log(fileName, "load");
		if (doNotLoad) {
			doNotLoad = false;
			return;
		}
		if (reloadOnce) {

			doNotLoad = true;
			reloadOnce = false;

		}

		Main.windowOpen = "ImageEdition";

		placePreviewImage(theCurrentImagePath);

		openMainImage(theCurrentImagePath, false);

	}

	public static void reloadPeople() {
		placeImageOfPeoples(theCurrentImagePath);
	}

	public static void reloadPlace() {
		placeImageOfPlaces(theCurrentImagePath);
	}

	public static void reloadPeopleAndPlace() {
		placeImageOfPeoples(theCurrentImagePath);
		placeImageOfPlaces(theCurrentImagePath);

	}

	public static void openMainImage(String imageName, Boolean force) {
		Main.mainStage.getActors().get(0);
		String imagePath = ImageData.IMAGE_PATH + "/" + imageName;
		if (!MixOfImage.manager.isLoaded(imagePath) && !force) {
			imagePath = ImageData.IMAGE_PATH + "/150/" + imageName;

			Main.placeImage(List.of(imagePath), "main image height", new Vector2(
					0, 0),
					Main.mainStage,
					(o) -> {
						clear();
						BigPreview.open(imageName);
					}, null, null, true, false, true, table, true, "");
		} else {
			Main.placeImage(List.of(imagePath), "main image", new Vector2(
					0, 0),
					Main.mainStage,
					(o) -> {
						clear();
						BigPreview.open(imageName);
					}, null, null, false, false, true, table, false, "");
			imageWithGoodQuality = true;

		}
	}

	public static void placeAddPeople() {
		Main.placeImage(List.of("images/add people.png", "images/people outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					ImageEdition.clear();
					EnterValue.enterAValue(0, 0, (p) -> {

						addAPeople((String) p);
						// Main.windowOpen = "ImageEdition";

					}, "enter the people name : ");

				}, null, null,
				true, true, false, table, true, "add people");
	}

	public static void placeAddPlace() {

		Main.placeImage(List.of("images/add place.png", "images/place outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					ImageEdition.clear();
					EnterValue.enterAValue(0, 0, (p) -> {
						addAPlace((String) p);
						Main.windowOpen = "ImageEdition";

					}, "enter the place name : ");
				}, null, null,
				true, true, false, table, true, "add place");
	}

	public static void createMainImageTable() {
		mainImageTable = new Table();

		mainImageTable.setSize(Main.preferences.getInteger("size of main image width"),
				Main.preferences.getInteger("size of main image height"));

		mainImageTable.setPosition(
				Main.preferences.getInteger("border"),
				Gdx.graphics.getHeight() - mainImageTable.getHeight() - Main.preferences.getInteger("border"));

		Main.mainStage.addActor(mainImageTable);
	}

	public static void createPreviewTable() {
		previewTable = new Table();
		previewTable.setSize(Main.preferences.getInteger("size of preview image width") * 5,
				Main.preferences.getInteger("size of preview image height"));
		previewTable.setPosition(
				Main.preferences.getInteger("border") + Main.preferences.getInteger("size of main image width") / 2
						- previewTable.getWidth() / 2,
				Gdx.graphics.getHeight() - mainImageTable.getHeight() - previewTable.getHeight()
						- Main.preferences.getInteger("border") * 2);

		Main.mainStage.addActor(previewTable);
	}

	public static void createTable() {
		table = new Table();

		table.setSize(
				Gdx.graphics.getWidth() - Main.preferences.getInteger("size of main images width")
						- Main.preferences.getInteger("border") * 3,
				Gdx.graphics.getHeight() - Main.preferences.getInteger("border") * 2);
		table.setPosition(
				Main.preferences.getInteger("size of main images width") + Main.preferences.getInteger("border") * 2,
				Main.preferences.getInteger("border"));

		Main.mainStage.addActor(table);
	}

	public static void placePreviewImage(String currentImagePath) {
		if (Main.imagesData.size() >= 7) {

			Integer index = 0;
			Integer imageIndex = 0;

			for (ImageData imageData : Main.imagesData) {
				if (imageData.getName().equals(currentImagePath)) {
					imageIndex = index;

				}
				index += 1;
			}

			Integer max = 9;
			index = 0;
			List<String> previewNames = new ArrayList<String>();
			Integer maxImageIndex = Main.imagesData.size() - 1;

			// Code de Yann
			Integer increment = 0;
			for (int i = -4; i <= 4; i++) {
				if (i + imageIndex > maxImageIndex) {
					increment = -maxImageIndex - 1;
				} else if (i + imageIndex < 0) {
					increment = maxImageIndex + 1;
				} else {
					increment = 0;
				}
				if (i >= 4 || i <= -4) {
					if (!MixOfImage.manager.isLoaded(ImageData.IMAGE_PATH + "/150/"
							+ Main.imagesData.get(i + imageIndex + increment).getName())) {
						MixOfImage.startToLoadImage(ImageData.IMAGE_PATH + "/150/" +
								Main.imagesData.get(i + imageIndex + increment).getName());
					}
				} else {
					previewNames.add(Main.imagesData.get(i + imageIndex + increment).getName());
				}
			}

			Integer nbr = 0;
			for (String preview : previewNames) {
				ImageData imageData = Main.getCurrentImageData(preview);
				List<String> previewList = new ArrayList<>();
				previewList.add(ImageData.IMAGE_PATH + "/150/" + preview);

				if (nbr == 3) {
					previewList.add("images/outlineSelectedPreview.png");
				} else {
					previewList.add("images/outlinePreview.png");
				}
				if (imageData.getLoved()) {
					previewList.add("images/loved preview.png");
				}
				if (toDelete.contains(imageData, false)) {
					previewList.add("images/deleted preview.png");
				}
				Main.placeImage(previewList,
						"preview image",
						new Vector2(0, 0),
						Main.mainStage,
						(o) -> {
							open(preview, true);

							// ne s'ouvre pas toujours a cause de
							// a cause du load pour la grande qualite
							// ||
							// \/
						},
						(o) -> {
							// les ouvre infiniment c'est pas ouf
							// if (!preview.equals(lastPreview)) {

							showBigPreview(preview);

							// }

						}, (o) -> {
							closeBigPreview(currentImagePath);
						},
						false, true, false, previewTable, true, "");

				index += 1;
				if (index >= max) {
					return;

				}
				nbr += 1;
			}
		}

	}

	public static void showBigPreview(String preview) {

		imageWithGoodQuality = false;
		imageOpen = preview;
		openMainImage(preview, false);

		reloadOnce = true;

	}

	public static void closeBigPreview(String initialImage) {

		imageOpen = "";

		openMainImage(initialImage, false);
		reloadOnce = true;

	}

	public static void placeImageOfPeoples(String currentImagePath) {
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		Integer max = 4;
		Integer index = 0;
		List<String> peopleNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/peoples");
		// Main.entriesSortedByValues(, true)
		for (Entry<String, Integer> entry : Main.peopleData.entrySet()) {
			peopleNames.add(entry.getKey());
		}

		if (!handle.exists()) {
			handle.mkdirs();
		}
		Integer maxPeople = 6;
		Integer i = 0;
		for (String people : peopleNames) {
			if (i < maxPeople) {
				i += 1;
				List<String> peopleList = new ArrayList<>();
				FileHandle handlebis = Gdx.files.absolute(ImageData.IMAGE_PATH + "/150/" + people + ".png");
				if (handlebis.exists()) {
					peopleList.add(ImageData.IMAGE_PATH + "/150/" + people + ".png");
				} else {
					peopleList.add("images/error.png");
				}
				peopleList.add("images/people outline.png");
				if (imageData.isInPeoples(people)) {
					peopleList.add("images/yes.png");
				} else {
					peopleList.add("images/no.png");
				}

				Main.placeImage(peopleList,
						"basic button",
						new Vector2(0, 0),
						Main.mainStage,
						(o) -> {

							addPeople(people, currentImagePath, true);

						}, null, null,
						true, true, false, table, true, people);

				index += 1;
				if (index >= max) {
					table.row();
					index = 0;

				}
			}
		}
	}

	public static void placeImageOfPlaces(String currentImagePath) {
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		Integer max = 4;
		Integer index = 0;
		List<String> placeNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/places");

		for (Entry<String, Integer> entry : Main.placeData.entrySet()) {

			placeNames.add(entry.getKey());
		}

		if (!handle.exists()) {
			handle.mkdirs();
		}
		Integer maxPlace = 6;
		Integer i = 0;

		if (!handle.exists()) {
			handle.mkdirs();
		}
		for (String place : placeNames) {
			if (i < maxPlace - 1) {
				i += 1;
				List<String> placeList = new ArrayList<>();
				placeList.add(ImageData.IMAGE_PATH + "/150/" + place + ".png");
				placeList.add("images/place outline.png");
				if (imageData.isInPlaces(place)) {
					placeList.add("images/yes.png");
				} else {
					placeList.add("images/no.png");
				}
				// ln("place place" + place);
				Main.placeImage(placeList,
						"basic button",
						new Vector2(0, 0),
						Main.mainStage,
						(o) -> {
							addPlace(place, currentImagePath, true);
						}, null, null,
						true, true, false, table, true, place);

				index += 1;
				if (index >= max) {
					table.row();
					index = 0;

				}
			}
		}
	}

	public static void nextImage(String currentImagePath) {
		lastImageChange = TimeUtils.millis();
		imageWithGoodQuality = false;
		indexLoaded = 0;

		boolean next = false;
		for (ImageData imageData : Main.imagesData) {
			if ((imageData.getName())
					.equals(currentImagePath)) {
				next = true;
			} else if (next) {

				open(imageData.getName(), true);

				Integer i = -4;

				if (Main.imagesData.indexOf(imageData) + i < 0) {
					i = Main.imagesData.size() + i;
				}

				next = false;
			}
		}
		if (next) {
			open(Main.imagesData.get(0).getName(), true);

		}
		MainImages.imageI = Main.getImageDataIndex(currentImagePath);
		Main.checkToUnload(null);

	}

	public static void previousImage(String currentImagePath) {
		lastImageChange = TimeUtils.millis();
		imageWithGoodQuality = false;
		indexLoaded = 0;

		ImageData previous = null;
		for (ImageData imageData : Main.imagesData) {
			if ((imageData.getName())
					.equals(currentImagePath)) {

				if (previous == null) {
					open(
							Main.imagesData.get(Main.imagesData.size() - 1).getName(), true);
				} else {
					open(previous.getName(), true);
					Integer i = 4;
					if (Main.imagesData.indexOf(previous) + 4 >= Main.imagesData.size()) {
						i = i - Main.imagesData.size();
					}

				}

			}
			previous = imageData;

		}
		MainImages.imageI = Main.getImageDataIndex(currentImagePath);

		Main.checkToUnload(null);
	}

	public static void addPeople(String peopleToAdd, String currentImagePath, boolean isReloadImageEdition) {

		Main.peopleData.put(peopleToAdd, Main.peopleData.get(peopleToAdd) + 1);
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		List<String> peoples = imageData.getPeoples();

		peoples = Main.addToList(peoples, peopleToAdd);

		imageData.setPeoples(peoples);

		if (isReloadImageEdition) {
			reloadPeople();
			theCurrentImagePath = currentImagePath;

		}
	}

	public static void addPlace(String placeToAdd, String currentImagePath, boolean isReloadImageEdition) {
		Main.placeData.put(placeToAdd, Main.placeData.get(placeToAdd) + 1);
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		List<String> places = imageData.getPlaces();

		places = Main.addToList(places, placeToAdd);

		imageData.setPlaces(places);

		if (isReloadImageEdition) {
			reloadPlace();
			theCurrentImagePath = currentImagePath;

		}
	}

	public static Pixmap rotatePixmap(Pixmap src, float angle) {
		final int width = src.getWidth();
		final int height = src.getHeight();
		Pixmap rotated = new Pixmap(width, height, src.getFormat());

		final double radians = Math.toRadians(angle);
		final double cos = Math.cos(radians);
		final double sin = Math.sin(radians);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final int centerX = width / 2;
				final int centerY = height / 2;
				final int m = x - centerX;
				final int n = y - centerY;
				final int j = ((int) (m * cos + n * sin)) + centerX;
				final int k = ((int) (n * cos - m * sin)) + centerY;
				if (j >= 0 && j < width && k >= 0 && k < height) {
					rotated.drawPixel(x, y, src.getPixel(j, k));
				}
			}
		}
		return rotated;
	}

	public static void save() {
		MainImages.imageI = 0;
		ImageData.saveImagesData();
		deleteImageTodelete();
		savePeopleDataToFile();
		savePlaceDataToFile();
		saveFileDataToFile();

	}

	public static void deleteImageTodelete() {
		for (ImageData imageData : toDelete) {
			deletAnImage(imageData);
		}
		toDelete = new Array<ImageData>();
		Main.reload(true);

	}

	public static void deletAnImage(ImageData imageData) {
		FileHandle from = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + imageData.getName());
		byte[] data = from.readBytes();

		FileHandle to = Gdx.files.absolute(ImageData.IMAGE_PATH + "/bin/" + imageData.getName());
		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/bin");

		if (!handle.exists()) {
			handle.mkdirs();
		}
		to.writeBytes(data, false);
		Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + imageData.getName()).delete();
		Main.imagesData.remove(imageData);
		ImageData.saveImagesData();
	}

	public static void addAPlace(String name) {

		Main.openFile(JFileChooser.FILES_ONLY,
				(fileRessource) -> {
					if (Main.isAnImage(fileRessource.toString())) {
						movePlace((File) fileRessource, name);

						Main.placeData.put(name, 0);

						ImageEdition.savePlaceDataToFile();
					} else {
						Main.infoText = "the file isn't an image or is not suported";
					}
				}, null);
	}

	public static void addAPeople(String name) {
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// JFileChooser chooser = new JFileChooser();
		// chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// JFrame f = new JFrame();
		// f.setVisible(true);
		// f.toFront();
		// f.setVisible(false);
		// int res = chooser.showSaveDialog(f);
		// f.dispose();
		// File fileRessource = null;
		// if (res == JFileChooser.APPROVE_OPTION) {
		// if (chooser.getSelectedFile() != null) {

		// fileRessource = chooser.getSelectedFile();

		// }
		// f.dispose();

		// }
		// Main.openWindow = true;
		// }

		// }).start();
		Main.openFile(JFileChooser.FILES_ONLY,
				(fileRessource) -> {
					if (Main.isAnImage(fileRessource.toString())) {
						movePeople((File) fileRessource, name, true);

						if (!Main.peopleData.containsKey(name)) {
							Main.peopleData.put(name, 0);
						}
						ImageEdition.savePeopleDataToFile();
						Main.windowOpen = "ImageEdition";

					} else {
						Main.infoText = "the file isn't an image or is not suported";
					}
				}, null);
	}

	public static void movePeople(File dir, String name, Boolean absolut) {
		FileHandle from;
		if (absolut) {
			from = Gdx.files.absolute(dir.toString());
		} else {
			from = Gdx.files.internal(dir.toString());

		}
		byte[] data = from.readBytes();

		String nameWithExtension = name + ".png";

		FileHandle to = Gdx.files.absolute(ImageData.PEOPLE_IMAGE_PATH + "/" + nameWithExtension);
		FileHandle tobis = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + nameWithExtension);

		to.writeBytes(data, false);
		tobis.writeBytes(data, false);
		System.out.println(to.path() + " test " + nameWithExtension);
		LoadImage.setSize(to.path(), nameWithExtension, 150, false);
		// ImageEdition.load();
	}

	public static void movePlace(File dir, String name) {

		FileHandle from = Gdx.files.absolute(dir.toString());
		byte[] data = from.readBytes();

		// String[] nameSplit = dir.getPath().replace(".", ",").split(",");
		String nameWithExtension = name + "."
				+ ".png";
		FileHandle to = Gdx.files.absolute(ImageData.PLACE_IMAGE_PATH + "/" + nameWithExtension);
		FileHandle tobis = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + nameWithExtension);

		to.writeBytes(data, false);
		tobis.writeBytes(data, false);

		LoadImage.setSize(to.path(), nameWithExtension, 150, false);

	}

	public static void placePlusPeople() {
		Main.placeImage(List.of("images/pluspeople.png", "images/people outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					openPlusPeople();
				}, null, null,
				true, true, false, table, true, "see more people");
	}

	public static void placePlusPlace() {
		Main.placeImage(List.of("images/plusplace.png", "images/place outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					openPlusPlace();

				}, null, null,
				true, true, false, table, true, "see more place");
	}

	public static void savePeopleDataToFile() {
		String s = "";
		for (Map.Entry<String, Integer> entry : Main.entriesSortedByValues(Main.peopleData, true)) {
			s += entry.getKey() + ":" + entry.getValue() + "\n";
		}

		FileHandle handle = Gdx.files.absolute(ImageData.PEOPLE_SAVE_PATH);
		InputStream text = new ByteArrayInputStream(s.getBytes());
		handle.write(text, false);
	}

	public static void savePlaceDataToFile() {

		String s = "";
		for (Map.Entry<String, Integer> entry : Main.entriesSortedByValues(Main.placeData, true)) {
			s += entry.getKey() + ":" + entry.getValue() + "\n";
		}
		FileHandle handle = Gdx.files.absolute(ImageData.PLACE_SAVE_PATH);
		InputStream text = new ByteArrayInputStream(s.getBytes());
		handle.write(text, false);
	}

	public static void saveFileDataToFile() {

		String s = "";
		for (Map.Entry<String, Integer> entry : Main.entriesSortedByValues(Main.fileData, true)) {
			s += entry.getKey() + ":" + entry.getValue() + "\n";
		}
		FileHandle handle = Gdx.files.absolute(ImageData.FILE_SAVE_PATH);
		InputStream text = new ByteArrayInputStream(s.getBytes());
		handle.write(text, false);
	}

	public static void openPlusPeople() {
		plusPeople = true;
		table.clear();
		addAllPeopleToPlusTable();
	}

	public static void openPlusPlace() {
		plusPlace = true;

		table.clear();
		addAllPlaceToPlusTable();
	}

	public static void createPlusTable() {

		plusTable = new Table();
		plusTable.setSize(table.getWidth(), table.getHeight());
		plusTable.setPosition(table.getX(), table.getY());
		plusTable.setColor(Color.BLUE);
		Main.mainStage.addActor(plusTable);

	}

	public static void addAllPeopleToPlusTable() {
		plusTableOpen = true;
		plusTable.clear();
		createPlusTable();
		ImageData imageData = Main.getCurrentImageData(theCurrentImagePath);
		float max = plusTable.getWidth() / Main.preferences.getInteger("size of basic button");
		Integer index = 0;
		List<String> peopleNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/peoples");

		for (Entry<String, Integer> entry : Main.peopleData.entrySet()) {

			peopleNames.add(entry.getKey());
		}

		if (!handle.exists()) {
			handle.mkdirs();
		}
		float maxPeople = max * plusTable.getHeight() / Main.preferences.getInteger("size of basic button") - 1;
		Integer i = 0;
		for (String people : peopleNames) {
			if (i < maxPeople) {
				i += 1;
				List<String> peopleList = new ArrayList<>();
				peopleList.add(ImageData.IMAGE_PATH + "/150/" + people + ".png");
				peopleList.add("images/people outline.png");
				if (imageData.isInPeoples(people)) {
					peopleList.add("images/yes.png");
				} else {
					peopleList.add("images/no.png");
				}

				Main.placeImage(peopleList,
						"basic button",
						new Vector2(0, 0),
						Main.mainStage,
						(o) -> {
							addPeople(people, theCurrentImagePath, false);
							addAllPeopleToPlusTable();
						}, null, null,
						true, true, false, plusTable, true, people);

				index += 1;
				if (index >= max) {
					plusTable.row();
					index = 0;

				}
			}
		}
		plusTable.row();
		CommonButton.createBack(plusTable);

	}

	public static void addAllPlaceToPlusTable() {
		plusTableOpen = true;
		plusTable.clear();
		createPlusTable();
		ImageData imageData = Main.getCurrentImageData(theCurrentImagePath);
		float max = plusTable.getWidth() / Main.preferences.getInteger("size of basic button");
		Integer index = 0;
		List<String> placeNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/places");
		for (Entry<String, Integer> entry : Main.placeData.entrySet()) {
			// for (Entry<String, Integer> entry :
			// Main.entriesSortedByValues(Main.placeData, true)) {
			placeNames.add(entry.getKey());
		}

		if (!handle.exists()) {
			handle.mkdirs();
		}
		float maxPlace = max * plusTable.getHeight() / Main.preferences.getInteger("size of basic button") - 1;
		Integer i = 0;
		for (String place : placeNames) {
			if (i < maxPlace) {
				i += 1;
				List<String> placeList = new ArrayList<>();

				placeList.add(ImageData.IMAGE_PATH + "/150/" + place + ".png");

				// WORK ONLY WITH JPG
				placeList.add("images/place outline.png");
				if (imageData.isInPlaces(place)) {
					placeList.add("images/yes.png");
				} else {
					placeList.add("images/no.png");
				}

				Main.placeImage(placeList,
						"basic button",
						new Vector2(0, 0),
						Main.mainStage,
						(o) -> {

							addPlace(place, theCurrentImagePath, false);
							addAllPlaceToPlusTable();

						}, null, null,
						true, true, false, plusTable, true, place);

				index += 1;
				if (index >= max) {
					plusTable.row();
					index = 0;

				}
			}
		}
		plusTable.row();

		CommonButton.createBack(plusTable);

	}

	public static void render() {
		if (TimeUtils.millis() - lastImageChange > 200) {
			if (!imageWithGoodQuality) {

				if (!imageOpen.equals("")) {
					if (lastImage.equals(imageOpen)) {
						openMainImage(imageOpen, true);
						imageWithGoodQuality = true;

					} else {
						lastImage = imageOpen;
					}
				} else {
					if (lastImage.equals(theCurrentImagePath)) {
						openMainImage(theCurrentImagePath, true);
						imageWithGoodQuality = true;

					} else {
						lastImage = theCurrentImagePath;
					}

				}

				lastImageChange = TimeUtils.millis();

			}
			if (indexLoaded < Main.preferences.getInteger("image loaded when waiting in ImageEdition", 5)) {
				indexLoaded += 1;
				Integer index = Main.getImageDataIndex(theCurrentImagePath);
				Integer index1 = index + indexLoaded;
				Integer index2 = index - indexLoaded;
				if (index1 >= Main.imagesData.size()) {
					index1 = Main.imagesData.size() - index1;
				} else if (index1 < 0) {
					index1 = Main.imagesData.size() + index1;
				}
				if (index2 >= Main.imagesData.size()) {
					index2 = Main.imagesData.size() - index2;
				} else if (index2 < 0) {
					index2 = Main.imagesData.size() + index2;
				}
				if (index1 > 0 && index1 < Main.imagesData.size() && index2 > 0 && index2 < Main.imagesData.size()) {

					if (!MixOfImage.manager
							.isLoaded(ImageData.IMAGE_PATH + "/" + Main.imagesData.get(index1).getName())) {
						// System.out.println(Main.imagesData.get(index1).getName());
						MixOfImage.startToLoadImage(ImageData.IMAGE_PATH + "/" + Main.imagesData.get(index1).getName());
					}
					if (!MixOfImage.manager
							.isLoaded(ImageData.IMAGE_PATH + "/" + Main.imagesData.get(index2).getName())) {
						MixOfImage.startToLoadImage(ImageData.IMAGE_PATH + "/" + Main.imagesData.get(index2).getName());
					}
					lastImageChange = TimeUtils.millis();

				}
			}
		}

	}
}