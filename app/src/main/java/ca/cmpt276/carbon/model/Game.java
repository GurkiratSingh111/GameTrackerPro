package ca.cmpt276.carbon.model;

import android.net.Uri;
import java.util.ArrayList;

import ca.cmpt276.carbon.R;

/**
 * Game Class stores the game name, highScore, lowScore and the imageID.
 */
public class Game {
    // Variables
    private ArrayList<Session> sessionsList;
    private String gameName;
    private int highScore;
    private int lowScore;
    private int imageID;
    private boolean isPhotoTaken;
    private String photo;

    // default constructor
    public Game() {
        this.sessionsList = new ArrayList<>();
        highScore = 0;
        lowScore = 0;
        gameName = "";
        imageID = R.drawable.p1;
        isPhotoTaken = false;
    }

    // custom constructor with parameters
    public Game(String g,int ls,int hs) {
        gameName = g;
        this.lowScore = ls;
        this.highScore = hs;
        this.sessionsList = new ArrayList<>();
    }

    // Getter/Setter methods
    public int getHighScore() {
        return highScore;
    }
    public int getLowScore() {
        return lowScore;
    }
    public String getGameName() {
        return gameName;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    public void setLowScore(int lowScore) {
        this.lowScore = lowScore;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getImageID() {
        return imageID;
    }
    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public boolean isPhotoTaken() {
        return isPhotoTaken;
    }
    public void setPhotoTaken(boolean photoTaken) {
        isPhotoTaken = photoTaken;
    }

    public Uri getPhoto() {
        if (photo == null) {
            return null;
        }
        return Uri.parse(photo);
    }
    public void setPhoto(Uri photo) {
        this.photo = photo.toString();
    }

    // Add, replace, delete new session
    public void addSession(Session s) {
        sessionsList.add(s);
    }
    public void deleteSession(int index) {
        sessionsList.remove(index);
    }

    // Helper methods
    public Session getSessionAtIndex(int index) {
        return sessionsList.get(index);
    }

    // Size of sessionsList
    public int getSize() {
        return sessionsList.size();
    }
}
