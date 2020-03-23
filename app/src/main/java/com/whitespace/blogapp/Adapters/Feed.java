package com.whitespace.blogapp.Adapters;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Feed {

    private String title, desc;
    private @ServerTimestamp
    Date timestamp;

    public Feed() {
    }

    public Feed(String title, String desc, Date timestamp) {
        this.title = title;
        this.desc = desc;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
