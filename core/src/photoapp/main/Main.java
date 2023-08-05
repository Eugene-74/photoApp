package photoapp.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import photoapp.main.graphicelements.MixOfImage;
import photoapp.main.storage.ImageData;
import photoapp.main.windows.ImageEdition;
import photoapp.main.windows.MainImages;

public class Main extends ApplicationAdapter {
	public static Stage mainStage;
	public static Preferences preferences;
	public static Table infoTable;
	static Integer numberOfLoadedImages = 0;
	static String nameOfFolderOfLoadedImages = "";
	static String nameOfFolderOfLoadedFolder = "";
	static Integer totalNumberOfLoadedImages = 0;
	public static Label labelInfoText;
	public static List<ImageData> imagesData;
	public static OrderedMap<String, Texture> imagesTextureData = new OrderedMap<>();
	Label.LabelStyle label1Style = new Label.LabelStyle();
	public static String infoText = " ";
	public static String toReload = "";
	public static List<String> toReloadList = List.of();
	public static String windowOpen = "Main";
	Integer newProgress;

	@Override
	public void create() {
		preferences = Gdx.app.getPreferences("graphic params");
		preferences.putInteger("size of main images height", 1000);
		preferences.putInteger("size of main images width", 1400);
		preferences.putInteger("size of main images button", 200);
		mainStage = new Stage(
				new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		Gdx.input.setInputProcessor(mainStage);
		createInfoTable();
		clear();

		ImageData.openDataOfImages();

		MixOfImage.manager.load("images/error.png", Texture.class);
		MixOfImage.manager.finishLoading();

		// for (ImageData imageData : imagesData) {
		// // System.out.println("loading image");
		// MixOfImage.loadImage(ImageData.IMAGE_PATH + "/" + imageData.getName());

		// }
		// Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("music/test.mp3"));

		// rainMusic.setLooping(true);

		// rainMusic.play();

		// start the playback of the background music immediately

		// bucket.x -= 200 * Gdx.graphics.getDeltaTime();

		System.out.println("starting");

		// System.out.println(imagesData + "images data main -----------------");
		// System.out.println(imagesData);
		// System.out.println(imagesData + "imagesData-1");
		FileHandle handle = Gdx.files.absolute(ImageData.IMAGE_PATH);
		if (!handle.exists()) {
			handle.mkdirs();
		}
		windowOpen = "Main Images";
		MainImages.createMainWindow();
		ImageEdition.imageEdtionCreate();
		// ImageEdition.imageEdtionCreate();
	}

	@Override
	public void render() {

		Integer progress = MixOfImage.manager.getAssetNames().size;
		// System.out.println("rendering" + progress);
		MixOfImage.manager.update();
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			ImageEdition.previousImage(ImageEdition.theCurrentImagePath);
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			ImageEdition.nextImage(ImageEdition.theCurrentImagePath);
		}
		ScreenUtils.clear(151 / 255f, 0 / 255f, 151 / 255f, 255 / 255f);
		if (!infoText.equals(" ")) {
			labelInfoText.setText(infoText);
		}
		mainStage.draw();
		// if (MixOfImage.manager.isFinished()) {
		// System.out.println("finish" + toReload + "");
		// }
		if (windowOpen.equals("Image Edition") && !toReloadList.isEmpty() && toReload.equals("imageEdition")) {
			ImageEdition.reloadImageEdition(false);
			toReload = "";
			toReloadList = List.of();
		}
		// if (windowOpen.equals("Main Images") && toReload.equals("mainImages")) {
		// for (String reload : toReloadList) {
		// if (MixOfImage.manager.isLoaded(reload)) {
		// System.out.println(reload + "reolad");
		// toReloadList.remove(reload);
		// toReload = "";
		// MainImages.reloadMainImages();
		// }
		// }
		// }
		// if (newProgress != null)
		// System.out.println(progress + newProgress);
		if (windowOpen.equals("Image Edition") &&
				toReload.equals("imageEdition")) {
			// || MixOfImage.manager.isFinished()
			if (progress != newProgress) {
				newProgress = progress;

				toReload = "";
				ImageEdition.reloadImageEdition(false);
			}
		}
		if (windowOpen.equals("Main Images") &&
				toReload.equals("mainImages")) {
			if (progress != newProgress) {
				newProgress = progress;
				infoTextSet(" ");
				toReload = "";
				MainImages.reloadMainImages();

			}
		}
		newProgress = progress;

	}

	public static void infoTextSet(String info) {
		labelInfoText.setText(info);
		infoText = info;

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
		createCloseButton();
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
			final Consumer<Object> onClicked,
			boolean isSquare, boolean inTable, boolean isMainImage, Table placeImageTable) {

		MixOfImage mixOfImages = new MixOfImage(imageNames, isSquare);

		if (isSquare) {
			mixOfImages.setSize(preferences.getInteger("size of " + prefSizeName, 100),
					preferences.getInteger("size of " + prefSizeName, 100));
		} else {
			mixOfImages.setSize(preferences.getInteger("size of " + prefSizeName + " width"),
					preferences.getInteger("size of " + prefSizeName + " height"));
		}

		mixOfImages.setPosition(position.x, position.y + 1);
		mixOfImages.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				// System.out.println("closing");
				// System.exit(0);
				if (onClicked != null) {
					onClicked.accept(null);
				}
			}
		});
		if (inTable) {
			// addd !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			placeImageTable.add(mixOfImages);
			// placeImageTable.addActorAt(1, mixOfImages);
			// placeImageTable.swapActor(0, 1);
			// continue actor setting
		} else {
			if (isMainImage) {
				if (ImageEdition.currentMainImage != null) {
					ImageEdition.currentMainImage.remove();

				}
				ImageEdition.currentMainImage = mixOfImages;
			}

			mainStage.addActor(mixOfImages);
		}

	}

	public void createCloseButton() {
		placeImage(List.of("images/close.png", "images/outline.png"), "close button",
				new Vector2(Gdx.graphics.getWidth() - preferences.getInteger("size of " + "close button", 50),
						Gdx.graphics.getHeight() - preferences.getInteger("size of " + "close button", 50)),
				mainStage,
				(o) -> {
					System.out.println("closing");
					dispose();
					System.exit(0);
				}, true, false, false, ImageEdition.table);// consumer en racourcis
	}

	public static ImageData getCurrentImageData(String currentImagePath) {
		for (ImageData imageData : imagesData) {
			// System.out.println(imageData + " : " + currentImagePath);
			// System.out.println(imageData.getName() + currentImagePath +
			// "test--------------------------------------");
			if ((imageData.getName())
					.equals(currentImagePath)) {
				return imageData;
			}

		}

		return null;
	}

	public static void reload(boolean returnToZero) {
		if (windowOpen.equals("Image Edition")) {
			ImageEdition.reloadImageEdition(returnToZero);
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
			// System.out.println("to remove NOW ");
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
		new Thread(new Runnable() {
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
							nameOfFolderOfLoadedImages = "All files loaded";
						}

					}
					f.dispose();

				}
			}

		}).start();
		reload(true);

	}

	public static void openImageInAFile(File dir) {
		FileHandle from = Gdx.files.absolute(dir.toString());
		byte[] data = from.readBytes();
		FileHandle to = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + dir.getName());
		to.writeBytes(data, false);
		openImageExif(dir.getName());
	}

	public static void openImageOfAFile(File dir) {

		File[] liste = dir.listFiles();
		if (liste != null) {
			for (File item : liste) {

				if (item.isFile()) {
					infoText = "Loding the folder : <" + dir.getName() + "> "
							+ "(" + numberOfLoadedImages
							+ " images load) / total loaded : " + totalNumberOfLoadedImages
							+ nameOfFolderOfLoadedFolder;

					if (item.getName().endsWith(".png") || item.getName().endsWith(".PNG")
							|| item.getName().endsWith(".jpg") || item.getName().endsWith(".JPG")) {
						FileHandle from = Gdx.files.absolute(dir + "/" + item.getName());
						byte[] data = from.readBytes();

						FileHandle to = Gdx.files.absolute(ImageData.IMAGE_PATH + "/" + item.getName());
						to.writeBytes(data, false);
						openImageExif(item.getName());
						numberOfLoadedImages += 1;
						totalNumberOfLoadedImages += 1;
					}

					else {
						System.out.println("fichier non lisible : " + item.getName());
					}
				} else if (item.isDirectory()) {

					openImageOfAFile(item);

					nameOfFolderOfLoadedFolder = " / The folder : " + item.getName() + " have been load";
				}
			}
			numberOfLoadedImages = 0;

		}
		infoTextSet("All files have been load");
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
			// file.disp
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

}
