package com.example.control;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Home on 31.01.2017.
 */

public class StorePreference {

    SharedPreferences sharedPreferences;
    static SharedPreferences.Editor ed;
    public static final int MAX_ITEMS = 12;
    Map<String, ?> mapAll;
    static String [] pack = new String[MAX_ITEMS];

    public StorePreference(Context context) {
        sharedPreferences = context.getSharedPreferences("PACKAGE", Context.MODE_PRIVATE);
        mapAll = sharedPreferences.getAll();
        ed = sharedPreferences.edit();
        restoreItems();
    }


    public static void saveAllItems() {
        for (char i = 0; i < MAX_ITEMS; i++)
            ed.putString("BTN" + Integer.toString(i), pack[i]);
        ed.commit();
    }

    public static void removeAllItems() {
        for (char i = 0; i < MAX_ITEMS; i++) {
            pack[i] = "";
            ed.putString("BTN" + Integer.toString(i), pack[i]);
        }
        ed.commit();
    }

    public void restoreItems() {
        for (char i = 0; i < MAX_ITEMS; i++) {
            pack[i] = sharedPreferences.getString("BTN" + Integer.toString(i), "");
            Map<String, String> mapList = (Map<String, String>) sharedPreferences.getAll();
        }
    }

    public static void saveItem(int btn, String packag) {
        pack[btn] = packag;
        saveAllItems();
    }


    public static void turnItems(int item1, int item2) {
        String str = pack[item1];
        pack[item1] = pack[item2];
        pack[item2] = str;

        saveAllItems();
    }

    public static void removeItem(int item) {
        pack[item] = "";
        saveAllItems();
    }

    public static void removeItemStr(String string) {
        //pack[item] = "";
       // saveAllItems();
    }

    public static int getItemNumber(String str) {
        for (int i = 0; i < MAX_ITEMS; i++) {
            if (pack[i].equals(str))
                return i;
        }
        return -1;
    }


    public static int getLastFree(){
        for (int i = 0; i < MAX_ITEMS; i++) {
            if (pack[i].equals(""))
                return i;
        }

        return -1;
    }

    public static int getAllFree(){
        int count = 0;
        for (int i = 0; i < MAX_ITEMS; i++) {
            if (!pack[i].equals(""))
                count++;
        }
        return count;
    }

    public static boolean isCheck(String str) {
        boolean res = false;

        for (int i = 0; i < MAX_ITEMS; i++){
            if (pack[i].equals(str))
                res = true;
        }
        return res;
    }

    public int getMax() {
        return MAX_ITEMS;
    }

    public static String getItem(int i){
        return pack[i];
    }

}
