package com.example.doanchuyennganh.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Items")
public class Items implements Serializable, Comparable<Items> {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="Title")
    private String Title;

    @ColumnInfo(name="LinkURL")
    private String LinkURL;

    @ColumnInfo(name="DateCreated")
    private String DateCreated;

    public Boolean getSeen() {
        return Seen;
    }

    public void setSeen(Boolean seen) {
        Seen = seen;
    }

    @ColumnInfo(name="Seen")
    private Boolean Seen;

    public Items() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLinkURL() {
        return LinkURL;
    }

    public void setLinkURL(String linkURL) {
        LinkURL = linkURL;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public Items(String title, String linkURL, String dateCreated, Boolean seen) {
        Title = title;
        LinkURL = linkURL;
        DateCreated = dateCreated;
        Seen = seen;
    }

    @NonNull
    @Override
    public String toString() {
        return Title + " " + LinkURL + " " + DateCreated;
    }

    @Override
    public int compareTo(Items items) {
        return this.id - items.getId();
    }
}
