package agh.ics.oop.presenter.util;

import javafx.scene.image.Image;

public class ImageLoader {
    private Image fireImage;
    private Image plantImage;
    private Image snailBack;
    private Image snailFront;
    private Image snailSide;

    public ImageLoader() {
        fireImage = loadImage("/fire.png");
        plantImage = loadImage("/plant.png");
        snailBack = loadImage("/snail_back.png");
        snailFront = loadImage("/snail_front.png");
        snailSide = loadImage("/snail_side.png");
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
}