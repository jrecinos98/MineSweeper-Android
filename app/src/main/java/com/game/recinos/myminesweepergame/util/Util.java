package com.game.recinos.myminesweepergame.util;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.game.recinos.myminesweepergame.Constants.Constants;
import com.game.recinos.myminesweepergame.Grid.Grid;
import com.game.recinos.myminesweepergame.Grid.GridComponent;

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
    public static <T extends Serializable> void save(Context context, String fileName, T objectToSave) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(objectToSave);

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
    public static<T extends Serializable> T loadGame(Context context, String fileName) {
        if (!saveExist(fileName, context.fileList())){
            return null;
        }
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
        if(saveExist(fileName,context.fileList()))
            context.deleteFile(fileName);
    }
    /**
     * Checks whether a save file exists.
     * @return true if save file exists and false otherwise
     */
    public static boolean saveExist(String fileName, String[] files){
        for (String file : files) {
            if (file.equals(fileName)) {
               // Log.d("Save", "True");
                return true;
            }
        }
        return false;
    }

    public static void saveToBundle(Bundle outState, Grid game, Constants.GAME_DIFFICULTY diff, Integer time, Constants.GAME_STATE state){
        outState.putParcelable("GameGrid", game);
        outState.putSerializable("Difficulty", diff);
        outState.putSerializable("State", state);
        outState.putInt("Time", time);
    }
}
