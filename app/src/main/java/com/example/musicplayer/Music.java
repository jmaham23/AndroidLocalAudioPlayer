package com.example.musicplayer;

public class Music {

    //member variables for music files
    private String title;
    private String album;
    private String artist;
    private String path;
    private String length;

    //default constructor
    public Music() {
    }

    //constructor
    public Music(String title, String album, String artist, String path, String length) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.path = path;
        this.length = length;
    }


    //getters and setters for all member variables
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
