package agh.ics.oop.presenter.util;

import javafx.scene.image.Image;

import java.util.Objects;

public class ImageLoader {

    private static final String FIRE_IMAGE_PATH = "/fire.png";
    private static final String PLANT_IMAGE_PATH = "/plant.png";
    private static final String SNAIL_BACK_IMAGE_PATH = "/snail_back.png";
    private static final String SNAIL_FRONT_IMAGE_PATH = "/snail_front.png";
    private static final String SNAIL_SIDE_IMAGE_PATH = "/snail_side.png";
    private static final String BORDER_IMAGE_PATH = "/border.png";
    private static final String GRASS_IMAGE_BASE_PATH = "/grasses/grass";
    private static final String VERDANT_IMAGE_BASE_PATH = "/verdant/verdant";
    private static final String IMAGE_FILE_EXTENSION = ".png";

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
        fireImage = loadImage(FIRE_IMAGE_PATH);
        plantImage = loadImage(PLANT_IMAGE_PATH);
        snailBack = loadImage(SNAIL_BACK_IMAGE_PATH);
        snailFront = loadImage(SNAIL_FRONT_IMAGE_PATH);
        snailSide = loadImage(SNAIL_SIDE_IMAGE_PATH);
        borderImage = loadImage(BORDER_IMAGE_PATH);

        grassImages = new Image[AMOUNT_OF_GRASS_IMAGES];
        for (int i = 0; i < AMOUNT_OF_GRASS_IMAGES; i++) {
            grassImages[i] = loadImage(GRASS_IMAGE_BASE_PATH + (i + 1) + IMAGE_FILE_EXTENSION);
        }

        verdantImages = new Image[AMOUNT_OF_VERDANT_IMAGES];
        for (int i = 0; i < AMOUNT_OF_VERDANT_IMAGES; i++) {
            verdantImages[i] = loadImage(VERDANT_IMAGE_BASE_PATH + (i + 1) + IMAGE_FILE_EXTENSION);
        }
    }

    private Image loadImage(String path) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
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