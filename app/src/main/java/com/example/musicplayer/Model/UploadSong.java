package com.example.musicplayer.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.musicplayer.MainActivity;

//import com.google.firebase.database.Exclude;

public class UploadSong implements Parcelable {
    public String songsCategory, songTitle, artist, album_art, songDuration, songLink, mKey;


    public UploadSong(String songsCategory, String songTitle, String artist, String album_art, String songDuration, String songLink) {

//        if (songTitle.trim().equals("")) {
//
//            songTitle = "No title";
//
//        }

        if (songTitle == null || songTitle.trim().equals("")) {
            this.songTitle = (String) MainActivity.textViewImage.getText();
        } else {
            this.songTitle = songTitle.trim();
        }



        this.songsCategory = songsCategory;
//        this.songTitle = songTitle;
        this.artist = artist;
        this.album_art = album_art;
        this.songDuration = songDuration;
        this.songLink = songLink;
        //this.mKey = mKey;
    }



    public UploadSong(String songsCategory, String songTitle, String artist, String songDuration, String songLink) {
        if (songsCategory != null) {
            this.songsCategory = songsCategory.trim();
        }
        if (songTitle != null && !songTitle.isEmpty() && !songTitle.equals("default_thumbnail")) {
            this.songTitle = songTitle.trim();
        }
        if (artist != null) {
            this.artist = artist.trim();
        }
        if (songDuration != null) {
            this.songDuration = songDuration.trim();
        }
        if (songLink != null) {
            this.songLink = songLink.trim();
        }
    }






    protected UploadSong(Parcel in){
        songTitle = in.readString();
        songLink = in.readString();

    }
    public UploadSong(String songTitle,String artist,String songDuration){
        this.songTitle = songTitle;
        this.artist = artist;
        this.songDuration = songDuration;
    }

    public UploadSong() {
    }

    public UploadSong(String title1, String artist1, String duration1, String songlink1) {
//
//
    }
//public UploadSong(String mKey,String songTitle){
//        this.mKey=mKey;
//        this.songTitle=songTitle;
//
//}

    public static final Creator<UploadSong> CREATOR = new Creator<UploadSong>() {
        @Override
        public UploadSong createFromParcel(Parcel in) {
            return new UploadSong(in);
        }

        @Override
        public UploadSong[] newArray(int size) {
            return new UploadSong[size];
        }
    };

    public String getSongsCategory() {
        return songsCategory;
    }

    public void setSongsCategory(String songsCategory) {
        this.songsCategory = songsCategory;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(String album_art) {
        this.album_art = album_art;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }





    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(songTitle);
        dest.writeString(songLink);

    }
}

