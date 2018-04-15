package com.example.administrator.thefirst.Collection;

/**
 * Created by ZZh on 2018/3/12.
 */

public class Advice {
    private String caption;

    private String article;

    public String image;
    //public  String caption;
    public String time;
    public String detail;
    public String tag;
    //public String article;
    public Advice(String image,String caption,String time,String detail,String tag,String article){
        this.image = image;
        this.caption = caption;
        this.time = time;
        this.detail = detail;
        this.tag = tag;
        this.article = article;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
