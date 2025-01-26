package agh.ics.oop.presenter.util;

import javafx.scene.image.Image;

public class ImageLoader {
    private Image fireImage;
    private Image plantImage;
    private Image snailBack;
    private Image snailFront;
    private Image snailSide;
    private Image[] grassImages;
    private Image[] verdantImages;
    private Image borderImage;

    private static final int AMOUNT_OF_GRASS_IMAGES = 7;
    private static final int AMOUNT_OF_VERDANT_IMAGES = 5;

    public ImageLoader() {
        fireImage = loadImage("/fire.png");
        plantImage = loadImage("/plant.png");
        snailBack = loadImage("/snail_back.png");
        snailFront = loadImage("/snail_front.png");
        snailSide = loadImage("/snail_side.png");
        borderImage = loadImage("/border.png");

        // Load grass images
        grassImages = new Image[AMOUNT_OF_GRASS_IMAGES];
        for (int i = 0; i < AMOUNT_OF_GRASS_IMAGES; i++) {
            grassImages[i] = loadImage("/grasses/grass" + (i + 1) + ".png");
        }

        // Load verdant images
        verdantImages = new Image[AMOUNT_OF_VERDANT_IMAGES];
        for (int i = 0; i < AMOUNT_OF_VERDANT_IMAGES; i++) {
            verdantImages[i] = loadImage("/verdant/verdant" + (i + 1) + ".png");
        }
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
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