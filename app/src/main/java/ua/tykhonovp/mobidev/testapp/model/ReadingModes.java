package ua.tykhonovp.mobidev.testapp.model;

import java.io.Serializable;

/**
 * Created by Tikho on 29.10.2016.
 */

public class ReadingModes implements Serializable {
    private boolean text;
    private boolean image;

    public boolean getText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public boolean getImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }
}
