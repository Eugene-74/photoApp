package photoapp.main.windows;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import photoapp.main.CommonButton;
import photoapp.main.Main;
import photoapp.main.graphicelements.MixOfImage;
import photoapp.main.keybord.Keybord;
import photoapp.main.storage.ImageData;

public class ImageEdition {
	public static Actor currentMainImage;
	public static Table table;
	static Table previewTable;
	Table infoTable = Main.infoTable;
	static Integer numberOfLoadedImages = 0;
	static String nameOfFolderOfLoadedImages = "";
	static String nameOfFolderOfLoadedFolder = "";
	static Integer totalNumberOfLoadedImages = 0;
	static Array<ImageData> toDelete = new Array<ImageData>();
	Keybord inputProcessor = new Keybord();
	public static String theCurrentImagePath;
	Label.LabelStyle label1Style = new Label.LabelStyle();

	public static void imageEdtionCreate() {
		// Main.preferences = Gdx.app.getPreferences("graphic params");

		Main.preferences.putInteger("border", 40);
		Main.preferences.putInteger("size of main image width", 1200);
		Main.preferences.putInteger("size of main image height", 800);
		Main.preferences.putInteger("size of preview image width",
				Main.preferences.getInteger("size of main image width") / 5);
		Main.preferences.putInteger("size of preview image height", 110);
		Main.preferences.flush();
		// batch = new SpriteBatch();

		previewTable = new Table();
		previewTable.setSize(Main.preferences.getInteger("size of preview image height"),
				Main.preferences.getInteger("size of main image width"));
		// previewTable.setPosition(40,
		// Gdx.graphics.getHeight() - preferences.getInteger("size of " + "main image
		// height")
		// - (Gdx.graphics.getHeight() - preferences.getInteger("size of " + "main image
		// height")) / 2
		// + preferences.getInteger("size of preview image height"));
		previewTable.setPosition(
				Main.preferences.getInteger("size of main image width") / 2 + Main.preferences.getInteger("border"),
				-Main.preferences.getInteger("size of main image height") / 2 - 4,
				Align.bottom);
		Main.mainStage.addActor(previewTable);
		table = new Table();
		table.setSize(
				Gdx.graphics.getWidth() - Main.preferences.getInteger("size of main image width")
						- Main.preferences.getInteger("border"),
				Gdx.graphics.getHeight());
		table.setPosition(
				Main.preferences.getInteger("size of main image width") + Main.preferences.getInteger("border"), 0);

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
		// Main.mainStage.clear();
		Main.mainStage.getActors().get(0);
		// System.out.println("hauteur ----------------------");
		// System.out.println((Gdx.graphics.getHeight() - preferences.getInteger("size
		// of " + "main image height")));
		Main.placeImage(List.of(ImageData.IMAGE_PATH + "/" + imageName), "main image", new Vector2(
				Main.preferences.getInteger("border"),
				Gdx.graphics.getHeight() - Main.preferences.getInteger("size of " + "main image height")
						- (Gdx.graphics.getHeight() - Main.preferences.getInteger("size of " + "main image height")) / 2
						+ Main.preferences.getInteger("size of preview image height")),
				Main.mainStage,
				null, false, false, true, table);
	}

	public static void placePreviewImage(String currentImagePath) {
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

		// FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/peoples");
		// if (!handle.exists()) {
		// handle.mkdirs();
		// }
		Integer nbr = 0;
		for (String preview : previewNames) {
			ImageData imageData = Main.getCurrentImageData(preview);
			List<String> previewList = new ArrayList<>();
			previewList.add(ImageData.IMAGE_PATH + "/" + preview);

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
					false, true, false, previewTable);

			index += 1;
			if (index >= max) {
				return;

			}
			nbr += 1;
		}
	}

	public static void placeImageOfPeoples(String currentImagePath) {
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		Integer max = 3;
		Integer index = 0;
		List<String> peopleNames = new ArrayList<String>();

		peopleNames.add("test1");
		peopleNames.add("test2");
		peopleNames.add("test3");
		peopleNames.add("test4");
		peopleNames.add("test5");
		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/peoples");
		if (!handle.exists()) {
			handle.mkdirs();
		}
		for (String people : peopleNames) {
			List<String> peopleList = new ArrayList<>();
			// System.out.println(ImageData.IMAGE_PATH + "/peoples/" + people + ".jpg");
			peopleList.add(ImageData.IMAGE_PATH + "/peoples/" + people + ".jpg");
			peopleList.add("images/outline.png");
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
						// long startTime = TimeUtils.millis();
						// System.out.println("try to add");
						addPeople(people, currentImagePath);
						// System.out.println("added");
						// long stopTime = TimeUtils.millis();
						// System.out.println("done in 1 : ");
						// System.out.println(stopTime - startTime);
					},
					true, true, false, table);

			index += 1;
			if (index >= max) {
				table.row();
				index = 0;

			}
		}
	}

	public static void placeImageOfPlaces(String currentImagePath) {
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		Integer max = 3;
		Integer index = 0;
		List<String> placeNames = new ArrayList<String>();

		placeNames.add("city");
		placeNames.add("meadow");
		placeNames.add("beach");
		placeNames.add("mountains");
		placeNames.add("test5");
		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH + "/places");
		if (!handle.exists()) {
			handle.mkdirs();
		}
		for (String place : placeNames) {
			List<String> placeList = new ArrayList<>();
			placeList.add(ImageData.IMAGE_PATH + "/places/" + place + ".jpg");
			placeList.add("images/outline.png");
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
						addPlace(place, currentImagePath);
						// System.out.println("added");
						// long stopTime = TimeUtils.millis();
						// System.out.println("done in 1 : ");
						// System.out.println(stopTime - startTime);
					},
					true, true, false, table);

			index += 1;
			if (index >= max) {
				table.row();
				index = 0;

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
				// System.out.println("image changed");
				next = false;
			}
		}
		if (next) {
			iniImageEdition(Main.imagesData.get(0).getName(), true);
			System.out.println("image changed to 1st one");
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
				} else {
					iniImageEdition(previous.getName(), true);
					if (imageData.getName().equals(previous.getName())) {

					}

				}

			}
			previous = imageData;

		}
	}

	public static void addPeople(String peopleToAdd, String currentImagePath) {

		// // System.out.println("adding : " + peopleToAdd);
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		List<String> peoples = imageData.getPeoples();
		// long startTime = TimeUtils.millis();
		peoples = Main.addToList(peoples, peopleToAdd);
		// long stopTime = TimeUtils.millis();
		// System.out.println("done in 2: ");
		// System.out.println(stopTime - startTime);
		// List<String> peoplesList;
		// if (peoples.isEmpty()) {
		// peoplesList = List.of(peopleToAdd);
		// // peoples.add(peopleToAdd);

		// } else {
		// peoplesList = new ArrayList<>();
		// for (String people : peoples) {
		// peoplesList.add(people);

		// }
		// peoplesList.add(peopleToAdd);
		// }
		imageData.setPeoples(peoples);
		// System.out.println(peoples + "peoples");
		// System.out.println(imageData);
		// long startTimeb = TimeUtils.millis();
		iniImageEdition(currentImagePath, false);
		// long stopTimeb = TimeUtils.millis();
		// System.out.println("done in3 : ");
		// System.out.println(stopTimeb - startTimeb);
	}

	public static void addPlace(String placeToAdd, String currentImagePath) {
		ImageData imageData = Main.getCurrentImageData(currentImagePath);
		List<String> places = imageData.getPlaces();
		places = Main.addToList(places, placeToAdd);
		imageData.setPlaces(places);
		iniImageEdition(currentImagePath, false);
	}

	public static void iniImageEdition(String currentImagePath, boolean OpenMain) {
		Main.toReload = "imageEdition";
		Main.windowOpen = "Image Edition";

		// infoTextSet("testbis " + infoText);
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
			// long startTimeOpenMain = TimeUtils.millis();
			openMainImage(currentImagePath);
			// long stopTimeOpenMain = TimeUtils.millis();
			// System.out.println("OpenMain done in : ");
			// System.out.println(stopTimeOpenMain - startTimeOpenMain);

		}

		placePreviewImage(currentImagePath);

		// long startTimePlaceImageOfPeoples = TimeUtils.millis();
		placeImageOfPeoples(currentImagePath);
		// long stopTimePlaceImageOfPeoples = TimeUtils.millis();
		// System.out.println("PlaceImageOfPeoples done in : ");
		// System.out.println(stopTimePlaceImageOfPeoples -
		// startTimePlaceImageOfPeoples);
		table.row();
		placeImageOfPlaces(currentImagePath);
		table.row();
		Main.placeImage(List.of("images/previous.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					// System.out.println("next image");
					previousImage(currentImagePath);
				},
				true, true, false, table);

		// ImageData imageData = getCurrentImageData(currentImagePath);
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
									// System.out.println(index + "index");
									// System.out.println(toDelete + "1\n\n");

									toDelete.removeIndex(index);
									iniImageEdition(currentImagePath, true);
									return;
									// System.out.println(toDelete + "2\n\n");
									// // toDelete.removeIndex(0);
									// System.out.println("image already to delete : " + imageData.getName());

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
				true, true, false, table);

		Main.placeImage(List.of("images/next.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					nextImage(currentImagePath);
				}, true, true, false, table);
		table.row();
		Main.placeImage(List.of("images/left.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					System.out.println("left");
					for (ImageData imageData : Main.imagesData) {
						if ((imageData.getName())
								.equals(currentImagePath)) {
							rotateAnImage(90, imageData.getName());
						}
					}
				},
				true, true, false, table);
		Main.placeImage(List.of("images/right.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				null,
				true, true, false, table);
		table.row();
		Main.placeImage(List.of("images/save.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					save();
				},
				true, true, false, table);

		table.row();
		CommonButton.createAddImagesButton(table);
		// Main.placeImage(List.of("images/add images.png", "images/outline.png"),
		// "basic button",
		// new Vector2(0, 0),
		// Main.mainStage,
		// (o) -> {
		// Main.openFile();
		// },
		// true, true, false, table);

		CommonButton.createRefreshButton(table);
		Main.placeImage(List.of("images/refresh.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					// infoTextSet("test");
					// setSize100();
				},
				true, true, false, table);
		Main.placeImage(List.of("images/back.png", "images/outline.png"), "basic button",
				new Vector2(0, 0),
				Main.mainStage,
				(o) -> {
					currentMainImage.clear();
					table.clear();
					previewTable.clear();

					MainImages.createMainWindow();
				},
				true, true, false, table);

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
		// reloadImageEdition(true);
		// ImageData.openDataOfImages();

	}

	public static void deleteImageTodelete() {
		for (ImageData imageData : toDelete) {
			// System.out.println("imageData : ---------" + imageData);
			deletAnImage(imageData);
			// System.out.println("delete image ...");
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

}
