package com.roman.yoursound.models;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {
    public static final String sp_name = "userDetails";
    SharedPreferences userLocalDataBase;

    public UserLocalStore (Context context) {
        userLocalDataBase = context.getSharedPreferences(sp_name, 0);
    }

    public void changeUserData(String name, String about){
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putString("name", name);
        spEditor.putString("about", about);
        //spEditor.putString("image_path", imagePath);
        spEditor.apply();
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putInt("id", user.id);
        spEditor.putString("name", user.userName);
        spEditor.putString("password", user.password);
        spEditor.putString("email", user.eMail);
        spEditor.putString("about", user.about);
        spEditor.putString("image_path", user.imagePath);
        spEditor.apply();
    }

    public User getLoggedInUser() {
        int id = userLocalDataBase.getInt("id", -1);
        String userName = userLocalDataBase.getString("name", "");
        String password = userLocalDataBase.getString("password", "");
        String eMail = userLocalDataBase.getString("email", "");
        String about = userLocalDataBase.getString("about", "");
        String imagePath = userLocalDataBase.getString("image_path", "");

        User storedUser = new User(id, userName, password, eMail, about, imagePath);
        return storedUser;
    }

    public void setUserLoggedIn (boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putBoolean("LoggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean isUserLoggedIn(){
        if(userLocalDataBase.getBoolean("LoggedIn", false) == true) {
            return true;
        }
        else {
            return false;
        }
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
