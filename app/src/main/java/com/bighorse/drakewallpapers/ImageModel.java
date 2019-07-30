package com.bighorse.drakewallpapers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class ImageModel implements Parcelable {

    private String uri;
    private String uriThumb;
    private String uriWallpaper;
    @Exclude
    private String name;
    @Exclude
    private String uriThumbDownload;
    @Exclude
    private String uriWallpaperDownload;

    public ImageModel(){}

    public ImageModel(String name, String uri, final String uriThumb, String uriWallpaper) {
        this.uri = uri;
        this.uriThumb = uriThumb;
        this.uriWallpaper = uriWallpaper;
    }


    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Exclude
    public String getName() {
        return name;
    }
    @Exclude
    public void setName(String name) {
        this.name = name;
    }

    public String getUriThumb() {
        return uriThumb;
    }
    public void setUriThumb(String uriThumb) {
        this.uriThumb = uriThumb;
    }

    public String getUriWallpaper() {
        return uriWallpaper;
    }
    public void setUriWallpaper(String uriWallpaper) {
        this.uriWallpaper = uriWallpaper;
    }

    public String getUriThumbDownload() {
        return uriThumbDownload;
    }
    public void setUriThumbDownload(String uriThumbDownload) {
        this.uriThumbDownload = uriThumbDownload;
    }

    public String getUriWallpaperDownload() {
        return uriWallpaperDownload;
    }
    public void setUriWallpaperDownload(String uriWallpaperDownload) {
        this.uriWallpaperDownload = uriWallpaperDownload;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uri);
        dest.writeString(this.uriThumb);
        dest.writeString(this.uriWallpaper);
        dest.writeString(this.name);
        dest.writeString(this.uriThumbDownload);
        dest.writeString(this.uriWallpaperDownload);
    }
    public ImageModel(Parcel in){
        this.uri = in.readString();
        this.uriThumb = in.readString();
        this.uriWallpaper = in.readString();
        this.name = in.readString();
        this.uriThumbDownload = in.readString();
        this.uriWallpaperDownload = in.readString();

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };
}
