package com.example.a10108309.object;

public class Article {

    private String title;
    private String publishedAt;
    private String url;
    private String imageURL;
    private String source;

    public Article(String title, String publishedAt, String url, String imageURL, String source){
        this.title = title;
        this.publishedAt = publishedAt;
        this.url = url;
        this.imageURL = imageURL;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
