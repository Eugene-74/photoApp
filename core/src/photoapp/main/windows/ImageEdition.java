package photoapp.main.windows;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import photoapp.main.CommonButton;
import photoapp.main.Main;
import photoapp.main.graphicelements.MixOfImage;
import photoapp.main.storage.ImageData;

public class ImageEdition {
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

	public static void imageEdtionCreate() {
		// Main.preferences = Gdx.app.getPreferences("graphic params");

		Main.preferences.putInteger("size of main image width", 1200);
		Main.preferences.putInteger("size of main image height", 800);
		Main.preferences.putInteger("size of preview image width", 150);
		Main.preferences.putInteger("size of preview image height", 150);
		Main.preferences.flush();
		// batch = new SpriteBatch();

		mainImageTable = new Table();

		mainImageTable.setSize(Main.preferences.getInteger("size of main image width"),
				Main.preferences.getInteger("size of main image height"));

		mainImageTable.setPosition(
				Main.preferences.getInteger("border"),
				Gdx.graphics.getHeight() - mainImageTable.getHeight() - Main.preferences.getInteger("border"));

		previewTable = new Table();
		previewTable.setSize(Main.preferences.getInteger("size of preview image width") * 5,
				Main.preferences.getInteger("size of preview image height"));
		previewTable.setPosition(
				Main.preferences.getInteger("border") + Main.preferences.getInteger("size of main image width") / 2
						- previewTable.getWidth() / 2,
				Gdx.graphics.getHeight() - mainImageTable.getHeight() - previewTable.getHeight()
						- Main.preferences.getInteger("border") * 2);

		Main.mainStage.addActor(previewTable);
		Main.mainStage.addActor(mainImageTable);

		// previewTable.setPosition(40,
		// Gdx.graphics.getHeight() - preferences.getInteger("size of " + "main image
		// height")
		// - (Gdx.graphics.getHeight() - preferences.getInteger("size of " + "main image
		// height")) / 2
		// + preferences.getInteger("size of preview image height"));

		table = new Table();
		// table.setSize(
		// Gdx.graphics.getWidth() - Main.preferences.getInteger("size of main image
		// width")
		// - Main.preferences.getInteger("border"),
		// Gdx.graphics.getHeight());
		// table.setPosition(
		// Main.preferences.getInteger("size of main image width") +
		// Main.preferences.getInteger("border"), 0);
		table.setSize(
				Gdx.graphics.getWidth() - Main.preferences.getInteger("size of main images width")
						- Main.preferences.getInteger("border") * 3,
				Gdx.graphics.getHeight() - Main.preferences.getInteger("border") * 2);
		table.setPosition(
				Main.preferences.getInteger("size of main images width") + Main.preferences.getInteger("border") * 2,
				Main.preferences.getInteger("border"));

		Main.mainStage.addActor(table);

		// newImageData();
		// System.out.println(Main.imagesData + "images data
		// --------------------------");
		// if (Main.imagesData != null && !Main.imagesData.isEmpty()) {
		// iniImageEdition(Main.imagesData.get(0).getName(), true);

		// }

	}

	public static void reloadImageEdition(boolean returnToZero) {
		Main.windowOpen = "Image Edition";
		Main.toReload = "imageEdition";

		ImageData.openDataOfImages();

		// System.out.println(imagesData.get(0).getName() + "0 name ----------");
		// System.out.println("return to zero : ? : " + returnToZero);
		if (returnToZero) {
			// System.out.println("return to zero" + imagesData.get(0).getName());
			// infoTextSet("in reload");
			iniImageEdition(Main.imagesData.get(0).getName(), true);
			// infoTextSet("in reload 2");
		} else {
			// System.out.println("go to : " + theCurrentImagePath);

			// infoTextSet("in reload");

			iniImageEdition(theCurrentImagePath, true);

		}
		// Main.infoTextSet("reload is done");
		// System.out.println("end reload" + Main.infoTex-t);
	}

	public static void openMainImage(String imageName) {
		Main.mainStage.getActors().get(0);
		Main.placeImage(List.of(ImageData.IMAGE_PATH + "/" + imageName), "main image", new Vector2(
				0, 0),
				Main.mainStage,
				null, false, false, true, table, false);
	}

	public static void placePreviewImage(String currentImagePath) {
		if (Main.imagesData.size() >= 5) {

			Integer index = 0;
			Integer imageIndex = 0;

			for (ImageData imageData : Main.imagesData) {
				if (imageData.getName().equals(currentImagePath)) {
					imageIndex = index;

				}
				index += 1;
			}

			Integer max = 5;
			index = 0;
			List<String> previewNames = new ArrayList<String>();
			Integer maxImageIndex = Main.imagesData.size() - 1;
			// System.out.println(imageIndex + "IMAGE INDEX ------------------" +
			// maxImageIndex);
			// System.out.println(imageIndex.equals(maxImageIndex));
			if (imageIndex == 0) {
				previewNames.add(Main.imagesData.get(maxImageIndex - 1).getName());
				previewNames.add(Main.imagesData.get(maxImageIndex).getName());
				previewNames.add(Main.imagesData.get(imageIndex).getName());
				previewNames.add(Main.imagesData.get(imageIndex + 1).getName());
				previewNames.add(Main.imagesData.get(imageIndex + 2).getName());
			} else if (imageIndex == 1) {
				previewNames.add(Main.imagesData.get(maxImageIndex).getName());
				previewNames.add(Main.imagesData.get(imageIndex - 1).getName());
				previewNames.add(Main.imagesData.get(imageIndex).getName());
				previewNames.add(Main.imagesData.get(imageIndex + 1).getName());
				previewNames.add(Main.imagesData.get(imageIndex + 2).getName());
			} else if (imageIndex == 2) {
				previewNames.add(Main.imagesData.get(imageIndex - 2).getName());
				previewNames.add(Main.imagesData.get(imageIndex - 1).getName());
				previewNames.add(Main.imagesData.get(imageIndex).getName());
				previewNames.add(Main.imagesData.get(imageIndex + 1).getName());
				previewNames.add(Main.imagesData.get(imageIndex + 2).getName());
			} else if (imageIndex.equals(maxImageIndex - 1)) {
				// System.out.println("MAX -1 -----------------");
				previewNames.add(Main.imagesData.get(imageIndex - 2).getName());
				previewNames.add(Main.imagesData.get(imageIndex - 1).getName());
				previewNames.add(Main.imagesData.get(imageIndex).getName());
				previewNames.add(Main.imagesData.get(imageIndex + 1).getName());
				previewNames.add(Main.imagesData.get(0).getName());
			} else if (imageIndex.equals(maxImageIndex)) {
				// System.out.println("MAX -----------------");
				previewNames.add(Main.imagesData.get(imageIndex - 2).getName());
				previewNames.add(Main.imagesData.get(imageIndex - 1).getName());
				previewNames.add(Main.imagesData.get(imageIndex).getName());
				previewNames.add(Main.imagesData.get(0).getName());
				previewNames.add(Main.imagesData.get(1).getName());
			} else if (imageIndex >= 3) {
				previewNames.add(Main.imagesData.get(imageIndex - 2).getName());
				previewNames.add(Main.imagesData.get(imageIndex - 1).getName());
				previewNames.add(Main.imagesData.get(imageIndex).getName());
				previewNames.add(Main.imagesData.get(imageIndex + 1).getName());
				previewNames.add(Main.imagesData.get(imageIndex + 2).getName());
			}

			Integer nbr = 0;
			for (String preview : previewNames) {
				ImageData imageData = Main.getCurrentImageData(preview);
				List<String> previewList = new ArrayList<>();
				previewList.add(ImageData.IMAGE_PATH + "/150/" + preview);

				if (nbr == 2) {
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
							iniImageEdition(preview, true);
						},
						false, true, false, previewTable, true);

				index += 1;
				if (index >= max) {
					return;

				}
				nbr += 1;
			}
		}
	}

	public static void placeImageOfPeoples(String currentImagePath) {
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		Integer max = 3;
		Integer index = 0;
		List<String> peopleNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/peoples");

		for (FileHandle f : handle.list()) {
			peopleNames.add(f.nameWithoutExtension());
		}

		if (!handle.exists()) {
			handle.mkdirs();
		}
		Integer maxPeople = 5;
		Integer i = 0;
		for (String people : peopleNames) {
			if (i < maxPeople) {
				i += 1;
				List<String> peopleList = new ArrayList<>();
				peopleList.add(ImageData.IMAGE_PATH + "/peoples/" + people + ".jpg");
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

						},
						true, true, false, table, true);

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
		Integer max = 3;
		Integer index = 0;
		List<String> placeNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/places");
		for (FileHandle f : handle.list()) {
			placeNames.add(f.nameWithoutExtension());
		}

		if (!handle.exists()) {
			handle.mkdirs();
		}
		Integer maxPlace = 5;
		Integer i = 0;

		if (!handle.exists()) {
			handle.mkdirs();
		}
		for (String place : placeNames) {
			if (i < maxPlace) {
				i += 1;
				List<String> placeList = new ArrayList<>();
				placeList.add(ImageData.IMAGE_PATH + "/places/" + place + ".jpg");
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
							addPlace(place, currentImagePath, true);
						},
						true, true, false, table, true);

				index += 1;
				if (index >= max) {
					table.row();
					index = 0;

				}
			}
		}
	}

	public static void nextImage(String currentImagePath) {
		boolean next = false;
		for (ImageData imageData : Main.imagesData) {
			if ((imageData.getName())
					.equals(currentImagePath)) {
				next = true;
			} else if (next) {
				iniImageEdition(imageData.getName(), true);
				Integer i = -3;
				if (Main.imagesData.indexOf(imageData) + i < 0) {
					i = Main.imagesData.size() + i;
				}
				Main.unLoadAnImage(ImageData.IMAGE_PATH + "/"
						+ Main.imagesData.get(Main.imagesData.indexOf(imageData) + i).getName());

				next = false;
			}
		}
		if (next) {
			iniImageEdition(Main.imagesData.get(0).getName(), true);
			if (Main.imagesData.size() > 3) {

				Main.unLoadAnImage(
						ImageData.IMAGE_PATH + "/" + Main.imagesData.get(Main.imagesData.size() - 3).getName());
			}

		}
	}

	public static void previousImage(String currentImagePath) {

		ImageData previous = null;
		for (ImageData imageData : Main.imagesData) {
			if ((imageData.getName())
					.equals(currentImagePath)) {

				if (previous == null) {
					iniImageEdition(
							Main.imagesData.get(Main.imagesData.size() - 1).getName(), true);
					if (Main.imagesData.size() > 3) {

						Main.unLoadAnImage(ImageData.IMAGE_PATH + "/"
								+ Main.imagesData.get(2).getName());
					}
				} else {
					iniImageEdition(previous.getName(), true);
					Integer i = 3;
					if (Main.imagesData.indexOf(previous) + 3 >= Main.imagesData.size()) {
						i = Main.imagesData.indexOf(previous) + i - Main.imagesData.size();
					}
					Main.unLoadAnImage(ImageData.IMAGE_PATH + "/"
							+ Main.imagesData.get(Main.imagesData.indexOf(previous) + i).getName());

					if (imageData.getName().equals(previous.getName())) {

					}

				}

			}
			previous = imageData;

		}
	}

	public static void addPeople(String peopleToAdd, String currentImagePath, boolean isReloadImageEdition) {

		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		List<String> peoples = imageData.getPeoples();
		peoples = Main.addToList(peoples, peopleToAdd);

		imageData.setPeoples(peoples);

		if (isReloadImageEdition) {
			iniImageEdition(currentImagePath, false);

		}
	}

	public static void addPlace(String placeToAdd, String currentImagePath, boolean isReloadImageEdition) {
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		List<String> places = imageData.getPlaces();
		places = Main.addToList(places, placeToAdd);
		imageData.setPlaces(places);
		if (isReloadImageEdition) {
			iniImageEdition(currentImagePath, false);

		}
	}

	public static void iniImageEdition(String currentImagePath, boolean OpenMain) {
		Main.toReload = "imageEdition";
		Main.windowOpen = "Image Edition";

		theCurrentImagePath = currentImagePath;
		table.clear();
		previewTable.clear();
		if (MainImages.imagesTable != null) {
			MainImages.imagesTable.clear();
		}
		if (MainImages.mainTable != null) {
			MainImages.mainTable.clear();
		}
		if (OpenMain) {
			openMainImage(currentImagePath);

		}

		placePreviewImage(currentImagePath);

		placeImageOfPeoples(currentImagePath);
		placePlusPeople();
		table.row();
		placeImageOfPlaces(currentImagePath);
		placePlusPlace();

		table.row();
		Main.placeImage(List.of("images/previous.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					previousImage(currentImagePath);
				},
				true, true, false, table, true);

		List<String> deletImages = new ArrayList<>();
		deletImages.add("images/delete.png");
		deletImages.add("images/outline.png");

		if (toDelete.contains(Main.getCurrentImageData(currentImagePath), false)) {
			deletImages.add("images/yes.png");
		}
		Main.placeImage(deletImages, "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					ImageData imageData = Main.getCurrentImageData(currentImagePath);
					if (imageData != null) {

						Integer index = 0;
						if (!toDelete.isEmpty()) {
							for (ImageData delet : toDelete) {
								if (delet.equals(imageData)) {

									toDelete.removeIndex(index);
									iniImageEdition(currentImagePath, true);
									return;

								}
								index += 1;
							}
							toDelete.add(imageData);

						}
						System.out.println("index : " + index);
						if (index == 0) {
							toDelete.add(imageData);

						}
						iniImageEdition(currentImagePath, true);
					} else {
						System.out.println("error null ----------------------------------------------");
					}
				},
				true, true, false, table, true);

		Main.placeImage(List.of("images/next.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					nextImage(currentImagePath);
				}, true, true, false, table, true);
		table.row();
		Main.placeImage(List.of("images/left.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					for (ImageData imageData : Main.imagesData) {
						if ((imageData.getName())
								.equals(currentImagePath)) {
							rotateAnImage(90, imageData.getName());
						}
					}
				},
				true, true, false, table, true);
		Main.placeImage(List.of("images/right.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				null,
				true, true, false, table, true);
		table.row();
		Main.placeImage(List.of("images/save.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					save();
				},
				true, true, false, table, true);

		Main.placeImage(List.of("images/add people.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					addAPeople();

				},
				true, true, false, table, true);
		Main.placeImage(List.of("images/add place.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					addAPlace();
				},
				true, true, false, table, true);
		table.row();
		CommonButton.createAddImagesButton(table);

		CommonButton.createRefreshButton(table);

		Main.placeImage(List.of("images/back.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					currentMainImage.clear();
					table.clear();
					previewTable.clear();

					MainImages.createMainWindow();
				},
				true, true, false, table, true);

	}

	public static void rotateAnImage(Integer degree, String imagePath) {
		Texture texture = MixOfImage.isInImageData(ImageData.IMAGE_PATH + "/" + imagePath, true, "");
		// Image img = new Image(texture);
		// img.rotateBy(degree);

		// Texture texture = new Texture(imagePath);
		// Texture texture = MixOfImage.manager.get(imageName, Texture.class);
		Pixmap pixmap = Main.textureToPixmap(texture);
		pixmap = rotatePixmap(pixmap, degree);
		FileHandle fh = new FileHandle(ImageData.IMAGE_PATH + "/" + imagePath);

		PixmapIO.writePNG(fh, pixmap);
		pixmap.dispose();
		ImageEdition.reloadImageEdition(false);
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

		ImageData.saveImagesData();
		deleteImageTodelete();

	}

	public static void deleteImageTodelete() {
		for (ImageData imageData : toDelete) {
			deletAnImage(imageData);
		}
		toDelete = new Array<ImageData>();
		reloadImageEdition(true);

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

	public static void addAPlace() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
							movePlace(fileRessource);
							savePlaceToFile();
							Main.reload(false);

						}

					}
					f.dispose();

				}
			}

		}).start();
	}

	public static void addAPeople() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
							movePeople(fileRessource);
							String name = fileRessource.getName().substring(0,
									fileRessource.getName().lastIndexOf("."));
							Main.peopleData.put(name, 0);
							savePeopleToFile();
							Main.reload(false);
						}

					}
					f.dispose();

				}
			}

		}).start();

	}

	public static void movePeople(File dir) {
		FileHandle from = Gdx.files.absolute(dir.toString());
		byte[] data = from.readBytes();

		FileHandle to = Gdx.files.absolute(ImageData.PEOPLE_IMAGE_PATH + "/" + dir.getName());
		to.writeBytes(data, false);
	}

	public static void movePlace(File dir) {
		FileHandle from = Gdx.files.absolute(dir.toString());
		byte[] data = from.readBytes();

		FileHandle to = Gdx.files.absolute(ImageData.PLACE_IMAGE_PATH + "/" + dir.getName());
		to.writeBytes(data, false);
	}

	public static void placePlusPeople() {
		Main.placeImage(List.of("images/pluspeople.png", "images/people outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					openPlusPeople();
				},
				true, true, false, table, true);
	}

	public static void placePlusPlace() {
		Main.placeImage(List.of("images/plusplace.png", "images/place outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					openPlusPlace();

				},
				true, true, false, table, true);
	}

	public static void savePeopleToFile() {

		String s = "";
		for (String people : Main.peopleData.keys()) {
			s += people + ":" + Main.peopleData.get(people) + "\n";
		}
		FileHandle handle = Gdx.files.absolute(ImageData.PEOPLE_SAVE_PATH);
		InputStream text = new ByteArrayInputStream(s.getBytes());
		handle.write(text, false);
	}

	public static void savePlaceToFile() {

		String s = "";
		for (String place : Main.placeData.keys()) {
			s += place + ":" + Main.placeData.get(place);
		}
		FileHandle handle = Gdx.files.absolute(ImageData.PLACE_SAVE_PATH);
		InputStream text = new ByteArrayInputStream(s.getBytes());
		handle.write(text, false);
	}

	public static void openPlusPeople() {
		table.clear();
		addAllPeopleToPlusTable();
	}

	public static void openPlusPlace() {
		table.clear();
		addAllPlaceToPlusTable();
	}

	public static void createPlusTable() {
		// ColorDrawable background = new ColorDrawable(0.7f, 0.9f, 0.9f, 1f);
		// backgroundColor.setColor(2, 179, 228, 255);
		if (plusTable != null) {
			plusTable.clear();
			plusTable = null;
		}
		plusTable = new Table();
		plusTable.setSize(table.getWidth(), table.getHeight());
		plusTable.setPosition(table.getX(), table.getY());
		// plusTable.setPosition(
		// Main.preferences.getInteger("size of main image width") +
		// Main.preferences.getInteger("border"), 0);

		// plusTable.setBackground(backgroundColor);
		plusTable.setColor(Color.BLUE);
		Main.mainStage.addActor(plusTable);

	}

	public static void addAllPeopleToPlusTable() {
		// table.clear();
		createPlusTable();
		ImageData imageData = Main.getCurrentImageData(theCurrentImagePath);
		float max = plusTable.getWidth() / Main.preferences.getInteger("size of basic button");
		Integer index = 0;
		List<String> peopleNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/peoples");

		for (FileHandle f : handle.list()) {
			peopleNames.add(f.nameWithoutExtension());
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
				peopleList.add(ImageData.IMAGE_PATH + "/peoples/" + people + ".jpg");
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
						},
						true, true, false, plusTable, true);

				index += 1;
				if (index >= max) {
					plusTable.row();
					index = 0;

				}
			}
		}
		plusTable.row();
		Main.placeImage(List.of("images/back.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					plusTable.clear();
					plusTable = null;
					Main.reload(false);
				},
				true, true, false, plusTable, true);

	}

	public static void addAllPlaceToPlusTable() {
		// table.clear();
		createPlusTable();
		ImageData imageData = Main.getCurrentImageData(theCurrentImagePath);
		float max = plusTable.getWidth() / Main.preferences.getInteger("size of basic button");
		Integer index = 0;
		List<String> placeNames = new ArrayList<String>();

		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/places");

		for (FileHandle f : handle.list()) {
			placeNames.add(f.nameWithoutExtension());
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

				placeList.add(ImageData.IMAGE_PATH + "/places/" + place + ".jpg");

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
							// long startTime = TimeUtils.millis();
							// System.out.println("try to add");
							addPlace(place, theCurrentImagePath, false);
							addAllPlaceToPlusTable();
							// System.out.println("added");
							// long stopTime = TimeUtils.millis();
							// System.out.println("done in 1 : ");
							// System.out.println(stopTime - startTime);
						},
						true, true, false, plusTable, true);

				index += 1;
				if (index >= max) {
					plusTable.row();
					index = 0;

				}
			}
		}
		plusTable.row();
		Main.placeImage(List.of("images/back.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					plusTable.clear();
					plusTable = null;
					Main.reload(false);
				},
				true, true, false, plusTable, true);

	}
}
