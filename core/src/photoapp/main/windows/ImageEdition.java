package photoapp.main.windows;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import photoapp.main.CommonButton;
import photoapp.main.CommonFunction;
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
	public static String theCurrentImageName;
	Label.LabelStyle label1Style = new Label.LabelStyle();
	static String lastPreview = "";
	public static String imageOpen = "";
	public static Boolean doNotLoad = false;
	public static Boolean reloadOnce = false;

	static Table dateTable;
	public static Label dateLabel;
	public static Label.LabelStyle datelabelStyle = new Label.LabelStyle();

	static public Long lastImageChange = (long) 0;
	static public Boolean imageWithGoodQuality = false;
	static public Boolean previewWithGoodQuality = false;

	// static public String imageQualityPath;

	static public Boolean plusTableOpen = false;
	static public String lastImage = "";
	static public String lastImageBis = "";
	static Integer indexLoaded = 0;

	public static Boolean plusPeople = false;
	public static Boolean plusPlace = false;
	public static Boolean bigPreview = false;

	public static void create() {
		Gdx.app.log(fileName, "create");

		Main.graphic.flush();

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
		dateTable.setSize(200, 50);
		dateLabel.setSize(200, 50);
		dateTable.setPosition(Main.graphic.getInteger("border"),
				Gdx.graphics.getHeight() - dateLabel.getHeight() - Main.graphic.getInteger("border"));

		dateLabel.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				DateEdition.open();

			}
		});
		Main.setTip("date label", dateLabel);
		Main.mainStage.addActor(dateTable);
	}

	public static void open(String currentImageName, boolean OpenMain) {
		Gdx.app.log(fileName, "open");
		ImageData imageData = Main.getCurrentImageData(currentImageName);

		Main.windowOpen = "ImageEdition";

		dateTable.addActor(dateLabel);

		theCurrentImageName = currentImageName;

		table.clear();
		previewTable.clear();

		if (imageData.getDate() != null) {
			try {
				String date = "";

				String[] nomMois = { "January", "February", "March", "April", "May", "June", "July",
						"August", "Septembre", "Octobrer", "Novembrer", "Decembrer" };

				String[] dateSplit = imageData.getDate().split(" ");
				String[] daySplit = dateSplit[0].split(":");
				String[] hourSplit = dateSplit[1].split(":");

				if (daySplit[0].equals("0000") && daySplit[1].equals("00") && daySplit[2].equals("00")
						&& hourSplit[0].equals("00") && hourSplit[1].equals("00") && hourSplit[2].equals("00")) {
					date = Main.graphic.getString("text no date");
				} else {
					date += daySplit[2] + "  " + nomMois[Integer.parseInt(daySplit[1]) - 1] + "  " + daySplit[0];
					date += "\n";
					date += hourSplit[0] + "h " + hourSplit[1] + "min " + hourSplit[2] + "s ";

				}
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
			openMainImage(currentImageName);

		}

		placePreviewImage(theCurrentImageName, false);
		// TODO rotation of the images fo place and people
		placeImageOfPeoples(currentImageName);
		placePlusPeople();
		placeAddPeople();

		table.row();
		placeImageOfPlaces(currentImageName);
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
					// ImageData imageData = Main.getCurrentImageData(theCurrentImageName);
					if (imageData.getLoved()) {
						imageData.setLoved(false);
					} else {
						imageData.setLoved(true);
					}
					reload(false);
				}, null, null,
				true, true, false, table, false, true, "love");
		table.row();
		Main.placeImage(List.of("images/previous.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					previousImage(currentImageName);
				}, null, null,
				true, true, false, table, true, true, "previous image");

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
					moveToDeleteAnImage(imageData, currentImageName);
					// if (imageData != null) {

					// Integer index = 0;
					// if (!toDelete.isEmpty()) {
					// for (ImageData delet : toDelete) {
					// if (delet.equals(imageData)) {

					// toDelete.removeIndex(index);
					// open(currentImagePath, true);
					// return;

					// }
					// index += 1;
					// }
					// toDelete.add(imageData);

					// }

					// if (index == 0) {
					// toDelete.add(imageData);

					// }
					// open(currentImagePath, true);
					// } else {
					// Gdx.app.error(fileName, "error null");
					// }
				}, null, null,
				true, true, false, table, true, true, "delete image");

		Main.placeImage(List.of("images/next.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					nextImage(currentImageName);
				}, null, null, true, true, false, table, true, true, "next image");
		table.row();
		Main.placeImage(List.of("images/right.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					ImageData rotaImageData = Main.getCurrentImageData(theCurrentImageName);
					Integer rotation = rotaImageData.getRotation();
					rotation -= 90;
					if (rotation < 0) {
						rotation += 360;
					}
					rotaImageData.setRotation(rotation);
					ImageData.saveImagesData();
					load();
				}, null, null,
				true, true, false, table, true, true, "rotate right");
		Main.placeImage(List.of("images/left.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					ImageData rotaImageData = Main.getCurrentImageData(theCurrentImageName);
					Integer rotation = rotaImageData.getRotation();
					rotation += 90;
					if (rotation > 360) {
						rotation -= 360;
					}
					rotaImageData.setRotation(rotation);
					ImageData.saveImagesData();
					load();

				}, null, null,
				true, true, false, table, true, true, "rotate left");
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
					true, true, false, table, true, true, "open map");
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
			BigPreview.open(currentImageName);
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
			open(theCurrentImageName, true);
		}
	}

	public static void moveToDeleteAnImage(ImageData imageData, String currentImagePath) {
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

		placePreviewImage(theCurrentImageName, false);

		openMainImage(theCurrentImageName);

	}

	public static void reloadPeople() {
		placeImageOfPeoples(theCurrentImageName);
	}

	public static void reloadPlace() {
		placeImageOfPlaces(theCurrentImageName);
	}

	public static void reloadPeopleAndPlace() {
		placeImageOfPeoples(theCurrentImageName);
		placeImageOfPlaces(theCurrentImageName);

	}

	public static void openMainImage(String imageName) {
		String imagePath = ImageData.IMAGE_PATH + "/" + imageName;
		// TODO ouvre infiniment
		// System.err.println("open : " + ImageData.IMAGE_PATH + "/" + imageName);

		Main.placeImage(List.of(imagePath), "main image", new Vector2(
				0, 0),
				Main.mainStage,
				(o) -> {
					clear();
					BigPreview.open(imageName);
				}, null, null, false, false, true, table, false, false, "");

	}

	public static void placeAddPeople() {
		Main.placeImage(List.of("images/add people.png", "images/blue outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					ImageEdition.clear();
					EnterValue.enterAValue((p) -> {
						String value = EnterValue.txtValue.getText();
						String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
						value = value.replaceAll(characterFilter, "");
						if (!Main.placeData.containsKey(value) && Main.peopleData.containsKey(value)) {

							addAPeople(value);
						} else {
							Main.infoText = "this name is already use !";
							Main.error("add people button : the file isn't an image or is not suported", null);
							CommonFunction.back();

						}
						// Main.windowOpen = "ImageEdition";

					}, "enter the people name : ");

				}, null, null,
				true, true, false, table, true, true, "add people");
	}

	public static void placeAddPlace() {

		Main.placeImage(List.of("images/add place.png", "images/green outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					ImageEdition.clear();
					EnterValue.enterAValue((p) -> {
						String value = EnterValue.txtValue.getText();
						String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
						value = value.replaceAll(characterFilter, "");

						if (!Main.placeData.containsKey(value) && Main.peopleData.containsKey(value)) {

							addAPlace(value);
						} else {
							Main.infoText = "this name is already use !";
							Main.error("add people button : the file isn't an image or is not suported", null);
							CommonFunction.back();
						}
					}, "enter the place name : ");
				}, null, null,
				true, true, false, table, true, true, "add place");
	}

	public static void createMainImageTable() {
		mainImageTable = new Table();

		mainImageTable.setSize(Main.graphic.getInteger("size of main image width"),
				Main.graphic.getInteger("size of main image height"));

		mainImageTable.setPosition(
				Main.graphic.getInteger("border"),
				Gdx.graphics.getHeight() - mainImageTable.getHeight() - Main.graphic.getInteger("border") * 2
						- Main.graphic.getInteger("size of date"));

		Main.mainStage.addActor(mainImageTable);
	}

	public static void createPreviewTable() {
		previewTable = new Table();
		previewTable.setSize(
				Main.graphic.getInteger("size of preview image")
						* Main.graphic.getInteger("number of preview image"),
				Main.graphic.getInteger("size of preview image"));
		previewTable.setPosition(
				Main.graphic.getInteger("border") + Main.graphic.getInteger("size of main image width") / 2
						- previewTable.getWidth() / 2,
				Gdx.graphics.getHeight() - Main.graphic.getInteger("size of main image height")
						- Main.graphic.getInteger("size of preview image") * (3 / 2)
						- Main.graphic.getInteger("border") * 3 - Main.graphic.getInteger("size of date"));

		Main.mainStage.addActor(previewTable);
	}

	public static void createTable() {
		table = new Table();

		table.setSize(
				Gdx.graphics.getWidth() - Main.graphic.getInteger("size of main image width")
						- Main.graphic.getInteger("border") * 3,
				Gdx.graphics.getHeight() - Main.graphic.getInteger("border") * 2);
		table.setPosition(
				Main.graphic.getInteger("size of main image width") + Main.graphic.getInteger("border") * 2,
				Main.graphic.getInteger("border"));

		Main.mainStage.addActor(table);
	}

	public static void placePreviewImage(String currentImagePath, boolean force) {
		if (Main.imagesData.size() >= Main.graphic.getInteger("number of preview image")) {

			Integer index = 0;
			Integer imageIndex = 0;

			for (ImageData imageData : Main.imagesData) {
				if (imageData.getName().equals(currentImagePath)) {
					imageIndex = index;

				}
				index += 1;
			}

			Integer max = Main.graphic.getInteger("number of preview image");
			index = 0;
			List<String> previewNames = new ArrayList<String>();
			Integer maxImageIndex = Main.imagesData.size() - 1;

			// Code de Yann
			Integer increment = 0;
			Integer number = (Main.graphic.getInteger("number of preview image")) / 2 + 1;
			for (int i = -number; i <= number; i++) {
				if (i + imageIndex > maxImageIndex) {
					increment = -maxImageIndex - 1;
				} else if (i + imageIndex < 0) {
					increment = maxImageIndex + 1;
				} else {
					increment = 0;
				}
				if (i >= number || i <= -number) {

					if (!MixOfImage.manager.isLoaded(ImageData.IMAGE_PATH + "/100/"
							+ Main.imagesData.get(i + imageIndex + increment).getName())) {

						MixOfImage.startToLoadImage(ImageData.IMAGE_PATH + "/100/" +
								Main.imagesData.get(i + imageIndex + increment).getName());
					}

				} else {
					previewNames.add(
							ImageData.IMAGE_PATH + "/" + Main.imagesData.get(i + imageIndex + increment).getName());
				}
			}

			Integer nbr = 0;
			for (String preview : previewNames) {

				ImageData imageData = Main.getCurrentImageData(Main.departurePathAndImageNameAndFolder(preview).get(1));
				List<String> previewList = new ArrayList<>();

				previewList.add(preview);

				if (nbr == number - 1) {
					previewList.add("images/blue outline.png");
				} else {
					previewList.add("images/outline.png");
				}
				if (imageData.getLoved()) {
					previewList.add("images/loved preview.png");
				}
				if (toDelete.contains(imageData, false)) {
					previewList.add("images/deleted preview.png");
				}
				String imageName = Main.departurePathAndImageNameAndFolder(preview).get(1);

				Main.placeImage(previewList,
						"preview image",
						new Vector2(0, 0),
						Main.mainStage,
						(o) -> {
							open(imageName, true);
						},
						(o) -> {
							showBigPreview(imageName);
						}, (o) -> {
							closeBigPreview(currentImagePath);
						},
						true, true, false, previewTable, false, true, "");

				index += 1;
				if (index >= max) {
					return;

				}
				nbr += 1;
			}
		}

	}

	public static void showBigPreview(String preview) {

		imageOpen = preview;
		openMainImage(preview);

		reloadOnce = true;

	}

	public static void closeBigPreview(String initialImage) {

		imageOpen = "";

		openMainImage(initialImage);
		reloadOnce = true;

	}

	public static void placeImageOfPeoples(String currentImagePath) {
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		Integer max = 4;
		Integer index = 0;
		List<String> peopleNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.PEOPLE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0));
		if (!handle.exists()) {
			handle.mkdirs();
		}
		for (Entry<String, Integer> entry : Main.peopleData.entrySet()) {
			peopleNames.add(entry.getKey());
		}

		Integer maxPeople = 6;
		Integer i = 0;
		for (String people : peopleNames) {
			if (i < maxPeople) {
				i += 1;
				List<String> peopleList = new ArrayList<>();
				FileHandle handlebis = Gdx.files
						.absolute(ImageData.PEOPLE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0) + "/" + people
								+ ".png");
				if (handlebis.exists()) {
					peopleList.add(
							ImageData.PEOPLE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0) + "/" + people + ".png");
				} else {
					peopleList.add("images/error.png");
				}
				peopleList.add("images/blue outline.png");
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
						true, true, false, table, true, true, people);

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

		FileHandle handle = Gdx.files.absolute(ImageData.PLACE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0));
		if (!handle.exists()) {
			handle.mkdirs();
		}
		for (Entry<String, Integer> entry : Main.placeData.entrySet()) {
			placeNames.add(entry.getKey());
		}

		Integer maxPlace = 6;
		Integer i = 0;
		for (String place : placeNames) {
			if (i < maxPlace) {
				i += 1;
				List<String> placeList = new ArrayList<>();
				FileHandle handlebis = Gdx.files
						.absolute(ImageData.PLACE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0) + "/" + place
								+ ".png");
				if (handlebis.exists()) {
					placeList.add(
							ImageData.PLACE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0) + "/" + place + ".png");
				} else {
					placeList.add("images/error.png");
				}
				placeList.add("images/green outline.png");
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

							addPlace(place, currentImagePath, true);

						}, null, null,
						true, true, false, table, true, true, place);

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
			theCurrentImageName = currentImagePath;

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
			theCurrentImageName = currentImagePath;

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
					if (!Main.placeData.containsKey(name) && Main.peopleData.containsKey(name)) {

						if (Main.isAnImage(fileRessource.toString())) {
							movePlace((File) fileRessource, name, true);

							Main.placeData.put(name, 0);

							ImageEdition.savePlaceDataToFile();

						} else {
							Main.infoText = "the file isn't an image or is not suported";
							Main.error("addAPlace : the file isn't an image or is not suported", null);
						}
					} else {
						Main.infoText = "name already use";
						Main.error("addAPlace : name already use", null);

					}
					Main.windowOpen = "ImageEdition";
				}, null);
	}

	public static void addAPeople(String name) {
		Main.openFile(JFileChooser.FILES_ONLY,
				(fileRessource) -> {
					if (!Main.placeData.containsKey(name) && Main.peopleData.containsKey(name)) {

						if (Main.isAnImage(fileRessource.toString())) {
							movePeople((File) fileRessource, name, true);

							Main.peopleData.put(name, 0);
							ImageEdition.savePeopleDataToFile();

						} else {
							Main.infoText = "the file isn't an image or is not suported";
							Main.error("addAPeople : the file isn't an image or is not suported", null);
						}
					} else {
						Main.infoText = "name already use";
						Main.error("addAPeople : name already use", null);

					}
					Main.windowOpen = "ImageEdition";

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

		to.writeBytes(data, false);

		List<String> paths = new ArrayList<String>();
		paths.add(ImageData.PEOPLE_IMAGE_PATH);
		paths.add(ImageData.PEOPLE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0));
		paths.add(nameWithExtension);
		paths.add(nameWithExtension);

		MixOfImage.toCreateImage100.add(paths);

	}

	public static void movePlace(File dir, String name, boolean absolut) {

		FileHandle from;
		if (absolut) {
			from = Gdx.files.absolute(dir.toString());
		} else {
			from = Gdx.files.internal(dir.toString());

		}
		byte[] data = from.readBytes();

		String nameWithExtension = name + ".png";

		FileHandle to = Gdx.files.absolute(ImageData.PLACE_IMAGE_PATH + "/" + nameWithExtension);

		to.writeBytes(data, false);

		List<String> paths = new ArrayList<String>();
		paths.add(ImageData.PLACE_IMAGE_PATH);
		paths.add(ImageData.PLACE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0));
		paths.add(nameWithExtension);
		paths.add(nameWithExtension);

		MixOfImage.toCreateImage100.add(paths);
	}

	public static void placePlusPeople() {
		Main.placeImage(List.of("images/pluspeople.png", "images/blue outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					openPlusPeople();
				}, null, null,
				true, true, false, table, true, true, "more people");
	}

	public static void placePlusPlace() {
		Main.placeImage(List.of("images/plusplace.png", "images/green outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					openPlusPlace();

				}, null, null,
				true, true, false, table, true, true, "more place");
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
		ImageData imageData = Main.getCurrentImageData(theCurrentImageName);
		float max = plusTable.getWidth() / Main.graphic.getInteger("size of basic button") - 1;
		Integer index = 0;
		List<String> peopleNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.PEOPLE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0));
		if (!handle.exists()) {
			handle.mkdirs();
		}
		for (Entry<String, Integer> entry : Main.peopleData.entrySet()) {

			peopleNames.add(entry.getKey());
		}

		float maxPeople = max * plusTable.getHeight() / Main.graphic.getInteger("size of basic button");
		Integer i = 0;
		for (String people : peopleNames) {
			if (i < maxPeople) {
				i += 1;
				List<String> peopleList = new ArrayList<>();
				peopleList.add(ImageData.PEOPLE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0) + "/" + people
						+ ".png");
				peopleList.add("images/blue outline.png");
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
							addPeople(people, theCurrentImageName, false);
							addAllPeopleToPlusTable();
						}, null, null,
						true, true, false, plusTable, true, true, people);

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
		ImageData imageData = Main.getCurrentImageData(theCurrentImageName);
		float max = plusTable.getWidth() / Main.graphic.getInteger("size of basic button") - 1;
		Integer index = 0;
		List<String> placeNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.PLACE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0));
		for (Entry<String, Integer> entry : Main.placeData.entrySet()) {
			placeNames.add(entry.getKey());
		}

		if (!handle.exists()) {
			handle.mkdirs();
		}
		float maxPlace = max * plusTable.getHeight() / Main.graphic.getInteger("size of basic button") - 1;
		Integer i = 0;
		for (String place : placeNames) {
			if (i < maxPlace) {
				i += 1;
				List<String> placeList = new ArrayList<>();

				placeList.add(ImageData.PLACE_IMAGE_PATH + "/" + MixOfImage.squareSize.get(0) + "/" + place
						+ ".png");

				// WORK ONLY WITH JPG
				placeList.add("images/green outline.png");
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

							addPlace(place, theCurrentImageName, false);
							addAllPlaceToPlusTable();

						}, null, null,
						true, true, false, plusTable, true, true, place);

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
		// System.out.println("render");
		if (TimeUtils.millis() - lastImageChange > 500) {
			lastImageChange = TimeUtils.millis();

			if (!MixOfImage.manager.isLoaded(ImageData.IMAGE_PATH + "/" + theCurrentImageName)
					&& !MixOfImage.isOnLoading.contains(ImageData.IMAGE_PATH + "/" + theCurrentImageName)) {
				MixOfImage.loadImage(ImageData.IMAGE_PATH + "/" + theCurrentImageName, true, true);

				Integer number = (Main.graphic.getInteger("number of preview image")) / 2 + 1;
				// System.out.println("number" + number);
				Integer increment = 0;
				Integer imageIndex = Main.getImageDataIndex(theCurrentImageName);
				Integer maxImageIndex = Main.imagesData.size() - 1;

				for (int i = -number; i <= number; i++) {

					if (i + imageIndex > maxImageIndex) {
						increment = -maxImageIndex - 1;
					} else if (i + imageIndex < 0) {
						increment = maxImageIndex + 1;
					} else {
						increment = 0;
					}
					System.out.println(i + imageIndex + increment);
					if (!MixOfImage.manager.isLoaded(ImageData.IMAGE_PATH + "/" + MixOfImage.squareSize.get(0) + "/"
							+ Main.imagesData.get(i + imageIndex + increment)
									.getName())) {
						MixOfImage.loadImage(ImageData.IMAGE_PATH + "/" + MixOfImage.squareSize.get(0) + "/"
								+ Main.imagesData.get(i + imageIndex + increment).getName(), false,
								false);
					}
				}

				// for (int i = -number; i <= number; i++) {
				// System.out.println("f");
				// // if (!MixOfImage.manager.isLoaded(ImageData.IMAGE_PATH + "/" +
				// // MixOfImage.squareSize.get(0) + "/"
				// // + Main.imagesData.get(i +
				// // Main.getImageDataIndex(theCurrentImageName)).getName())) {

				// MixOfImage.loadImage(ImageData.IMAGE_PATH + "/" +
				// MixOfImage.squareSize.get(0) + "/"
				// + Main.imagesData.get(i +
				// Main.getImageDataIndex(theCurrentImageName)).getName(),
				// false,
				// false);
				// // } else {
				// // System.out.println("not");
				// // }
				// System.out.println("ff");

				// }

				// reload(false);
			}

			if (indexLoaded < Main.graphic.getInteger("number of preview image")) {
				lastImageChange = TimeUtils.millis();

				if (lastImageBis.equals(theCurrentImageName)) {

					indexLoaded += 1;
					Integer index = Main.getImageDataIndex(theCurrentImageName);
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
					if (index1 > 0 && index1 < Main.imagesData.size() && index2 > 0
							&& index2 < Main.imagesData.size()) {

						if (!MixOfImage.manager
								.isLoaded(ImageData.IMAGE_PATH + "/" + Main.imagesData.get(index1).getName())) {
							MixOfImage.startToLoadImage(
									ImageData.IMAGE_PATH + "/" + Main.imagesData.get(index1).getName());
						}
						if (!MixOfImage.manager
								.isLoaded(ImageData.IMAGE_PATH + "/" + Main.imagesData.get(index2).getName())) {
							MixOfImage.startToLoadImage(
									ImageData.IMAGE_PATH + "/" + Main.imagesData.get(index2).getName());
						}

					}
				} else {
					lastImageBis = theCurrentImageName;
				}
			}
		}

	}
}