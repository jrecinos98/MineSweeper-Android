package com.game.recinos.myminesweepergame.util;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.game.recinos.myminesweepergame.R;
import com.game.recinos.myminesweepergame.Views.Grid.GridComponent;
import com.game.recinos.myminesweepergame.Views.Grid.GridComponent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by recinos on 2/5/18.
 */
public class Util {
    public static GridComponent[][] generateInitial(int width, int height){
        GridComponent[][] grid = new GridComponent[width][height];
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                grid[row][col] = new GridComponent(row,col,row+(col * width)); //position= x+(y * width);
            }
        }
        return grid;
    }
    public static void generateGrid(GridComponent[][] grid, int minenumber, int position) {
        int row = position%grid.length;
        int col= position/grid.length;
        Random r = new Random();
        ArrayList<Integer> neighbors=Finder.findNeighborPositions(row,col,grid);
        //First Place all the mines in the map.
        while (minenumber> 0) {
            int rowNum = r.nextInt(grid.length);
            int colNum = r.nextInt(grid[0].length);
            //10 is the mine
            if (!grid[rowNum][colNum].isMine() && !neighbors.contains(grid[rowNum][colNum].getPosition())) {
                grid[rowNum][colNum].makeMine();
                Finder.mineFinder(grid, rowNum, colNum);
                minenumber--;
            }
        }

    }
    public static int convertDpToPixel(float dp){
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            float px = dp * (metrics.densityDpi / 160f);
            return Math.round(px);
        }

    /**
     * Saves the current game (this) into a serialized file
     */
    public void save(Context context, String fileName) {
        //For Internal Storage.
        File path = context.getFilesDir();
        //For External Storage SD.
        File pathExt= context.getExternalFilesDir(null);
        File file = new File(path, "GameSave.ser");
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("GameSave.ser", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads a serializable object.
     *
     * @param context The application context.
     * @param <T> The object type.
     *
     * @return the serializable object.
     */
    public static<T extends Serializable> T loadPreviousGame(Context context, String fileName) {
        T save = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            save = (T) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return save;
    }
    /**
     * Removes a specified file.
     *
     * @param context The application context.
     */
    public static void deleteSave(Context context, String fileName) {
        if(saveExist(fileName))
            context.deleteFile("GameSave.ser");
    }
    /**
     * Checks whether a save file exists.
     * @return true if save file exists and false otherwise
     */
    public static boolean saveExist(String fileName){
        return (new File(fileName).exists());
    }
}
