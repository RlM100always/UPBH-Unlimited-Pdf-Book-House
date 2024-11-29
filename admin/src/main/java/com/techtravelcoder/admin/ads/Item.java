package com.techtravelcoder.admin.ads;

public class Item {
    private String id;
    private String image;
    private String siteUrl;
    private String title;

    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    public Item(String id, String image, String url, String title) {
        this.id = id;
        this.image = image;
        this.siteUrl = url;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getTitle() {
        return title;
    }
}

