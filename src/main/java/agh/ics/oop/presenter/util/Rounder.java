package agh.ics.oop.presenter.util;

public class Rounder {
    public static double roundToTwoDecimal(double number){
        return Math.round(number / 100 ) * 100;
    }
}
