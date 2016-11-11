package ua.tykhonovp.mobidev.testapp.model;

import java.io.Serializable;

/**
 * Created by Tikho on 29.10.2016.
 */

public class ImageLinks implements Serializable {
    private String smallThumbnail;
    private String thumbnail;

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
