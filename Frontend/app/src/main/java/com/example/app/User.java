package com.example.app;

import android.app.Activity;
import android.net.Uri;

import java.io.Serializable;
import java.net.URISyntaxException;

/**
 * User Class
 * @author Isaac
 * This class holds all user information that is needed for any instance of this application. Here we define the current user who is using the app and if the current session is active of invalid.
 */
public class User extends Activity implements Serializable {
    public int id;
    public String userName;    //database name
    public String displayName; //user-defined name
    public String password;
    public int highScore;
    public Boolean isActive;


    //this should be populated when logged in
    public User(int id, String userName, String displayName, String password, int highScore, boolean isActive) {
        this.id = id;
        this.displayName = displayName;
        this.userName = userName;
        this.password = password;
        this.highScore = highScore;
        this.isActive = isActive;
    }



    /**
     * Websocket Stuff
     */
//    private void connectWebSocket() {
//        URI uri;
//        try {
//            uri = new URI(Const.URL_websocket_test);
//        } catch (URISyntaxException e)  {
//            e.printStackTrace();
//            return;
//        }
//    }

//private String id;
//
//    //getter and setter methods
//    public int getId() {
//        return id;
//    }
//    public void setId(int idNumber)  {
//        this.id = idNumber;
//    }
//
//    public String getDisplayName()  {
//        return displayName; }
//    public void setDisplayName(String setterDisplayName)    {
//        this.displayName = setterDisplayName; }
//
//
//    public String getUserName() {
//        return displayName; }
//    public void setUserName(String userName)  {
//        this.userName = userName; }

}
