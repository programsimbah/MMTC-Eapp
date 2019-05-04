package com.pengembangsebelah.stmmappxo.model;

import java.util.List;

/**
 * Created by MBP on 5/6/18.
 */

public class RSSObject
{
    public String status;
    public FeedData feed;
    public List<Item> items;

    public RSSObject(String status, FeedData feed, List<Item> items) {
        this.status = status;
        this.feed = feed;
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FeedData getFeed() {
        return feed;
    }

    public void setFeed(FeedData feed) {
        this.feed = feed;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
