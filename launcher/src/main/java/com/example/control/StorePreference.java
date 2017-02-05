package com.example.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Home on 31.01.2017.
 */

public class StorePreference {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor ed;

    private static final int MAX_ITEMS = 12;

    String [] pack = new String[MAX_ITEMS];

    public StorePreference(Context context) {

        sharedPreferences = context.getSharedPreferences("PACKAGE", Context.MODE_PRIVATE);

        ed = sharedPreferences.edit();
    }


    public void saveItems() {
        for (char i = 0; i < MAX_ITEMS; i++)
            ed.putString("BTN" + Integer.toString(i), "package " + Integer.toString(i));
        ed.commit();

    }

    public void restoreItems() {
        for (char i = 0; i < MAX_ITEMS; i++) {
            String str = sharedPreferences.getString("BTN" + Integer.toString(i), "");
            Log.i("myTag", "BTN" + Integer.toString(i) + " - " + str);
        }

    }

    public void turnItems(int item1, int item2){
        String str = pack[item1];
        pack[item1] = pack[item2];
        pack[item2] = str;

        saveItems();
    }

    public void removeItem(int item) {
        pack[item] = "";
        saveItems();
    }


    public int getLastFree(){
        for (int i = 0; i < MAX_ITEMS; i++) {
            if (pack[i].equals(""))
                return i;
        }

        return -1;
    }

    public int getAllFree(){
        int count = 0;
        for (int i = 0; i < MAX_ITEMS; i++) {
            if (!pack[i].equals(""))
                count++;
        }
        return count;
    }

    public int getMax() {
        return MAX_ITEMS;
    }

    public String getItem(int i){
        return pack[i];
    }

}
