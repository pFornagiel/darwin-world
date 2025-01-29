package agh.ics.oop.presenter.util;

import agh.ics.oop.model.exception.resources.ImageFileCouldNotBeFoundException;
import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageLoader {
    private static final String BASE_PATH = "img";
    private static final String IMAGE_FILE_EXTENSION = ".png";
    private static final Path RESOURCES_DIRECTORY = Paths.get("src/main/resources");
    private static final String UNFORMATTED_FILE_NAME = "%s%d%s";

    private static final String FIRE_IMAGE_PATH = "fire.png";
    private static final String PLANT_IMAGE_PATH = "plant.png";
    private static final String SNAIL_BASE_PATH = "snail";
    private static final String SNAIL_BACK_IMAGE_PATH = "snail_back.png";
    private static final String SNAIL_FRONT_IMAGE_PATH = "snail_front.png";
    private static final String SNAIL_SIDE_IMAGE_PATH = "snail_side.png";
    private static final String BORDER_IMAGE_PATH = "border.png";

    private static final String GRASS_IMAGE_BASE_PATH = "grasses";
    private static final String GRASS_IMAGE_BASE_NAME = "grass";
    private static final String VERDANT_IMAGE_BASE_PATH = "verdant";
    private static final String VERDANT_IMAGE_BASE_NAME = "verdant";

    private static final int AMOUNT_OF_GRASS_IMAGES = 7;
    private static final int AMOUNT_OF_VERDANT_IMAGES = 5;

    private final Image fireImage;
    private final Image plantImage;
    private final Image snailBack;
    private final Image snailFront;
    private final Image snailSide;
    private final Image[] grassImages;
    private final Image[] verdantImages;
    private final Image borderImage;

    public ImageLoader() {
        fireImage = loadImage(Paths.get(BASE_PATH, FIRE_IMAGE_PATH));
        plantImage = loadImage(Paths.get(BASE_PATH, PLANT_IMAGE_PATH));
        snailBack = loadImage(Paths.get(BASE_PATH, SNAIL_BASE_PATH, SNAIL_BACK_IMAGE_PATH));
        snailFront = loadImage(Paths.get(BASE_PATH,SNAIL_BASE_PATH, SNAIL_FRONT_IMAGE_PATH));
        snailSide = loadImage(Paths.get(BASE_PATH,SNAIL_BASE_PATH, SNAIL_SIDE_IMAGE_PATH));
        borderImage = loadImage(Paths.get(BASE_PATH,BORDER_IMAGE_PATH));


        grassImages = new Image[AMOUNT_OF_GRASS_IMAGES];
        for (int i = 0; i < AMOUNT_OF_GRASS_IMAGES; i++) {
            String filename = UNFORMATTED_FILE_NAME.formatted(GRASS_IMAGE_BASE_NAME,i+1, IMAGE_FILE_EXTENSION);
            grassImages[i] = loadImage(Paths.get(BASE_PATH, GRASS_IMAGE_BASE_PATH, filename));
        }

        verdantImages = new Image[AMOUNT_OF_VERDANT_IMAGES];
        for (int i = 0; i < AMOUNT_OF_VERDANT_IMAGES; i++) {
            String filename = UNFORMATTED_FILE_NAME.formatted(VERDANT_IMAGE_BASE_NAME,i+1, IMAGE_FILE_EXTENSION);
            verdantImages[i] = loadImage(Paths.get(BASE_PATH, VERDANT_IMAGE_BASE_PATH, filename));
        }
    }

    private Image loadImage(Path path) {
        Path fullPath = RESOURCES_DIRECTORY.resolve(path);
        try {
            return new Image(Files.newInputStream(fullPath));
        } catch (IOException e) {
          throw new ImageFileCouldNotBeFoundException(fullPath);
        }
    }

    public Image getFireImage() {
        return fireImage;
    }

    public Image getPlantImage() {
        return plantImage;
    }

    public Image getSnailBack() {
        return snailBack;
    }

    public Image getSnailFront() {
        return snailFront;
    }

    public Image getSnailSide() {
        return snailSide;
    }

    public Image[] getGrassImages() {
        return grassImages;
    }

    public Image[] getVerdantImages() {
        return verdantImages;
    }

    public Image getBorderImage() {
        return borderImage;
    }
}