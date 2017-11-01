package io.walter.manager.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by walter on 10/31/17.
 */

public class General {
   private static int []rangi={   Color.parseColor("#f44336"),
            Color.parseColor("#e91e63"),
            Color.parseColor("#9c27b0"),
            Color.parseColor("#880e4f"),
            Color.parseColor("#4a148c"),
            Color.parseColor("#3f51b5"),
            Color.parseColor("#2196f3"),
            Color.parseColor("#1a237e"),
            Color.parseColor("#006064"),
            Color.parseColor("#1b5e20"),
            Color.parseColor("#ff6f00"),
            Color.parseColor("#757575"),
            Color.parseColor("#ed09c3"),Color.parseColor("#440793"),Color.parseColor("#581845"),Color.parseColor("#C70039"),
            Color.parseColor("#a500ff"),Color.parseColor("#200668"),Color.parseColor("#900C3F"),Color.parseColor("#FF5733")};
    public static int randInt(int min, int max) {
        Random rand=new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    public static int generateRandomColor(){
        int i=randInt(0,rangi.length);
        return rangi[i];
    }
}
