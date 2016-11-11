package ua.tykhonovp.mobidev.testapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tikho on 28.10.2016.
 */

public class BookList implements Serializable {
    @SerializedName("items")
    public List<Book> items = new ArrayList<>();
    /*String kind;
    int totalItems;*/

}
