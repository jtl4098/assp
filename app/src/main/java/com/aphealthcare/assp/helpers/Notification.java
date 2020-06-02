package com.aphealthcare.assp.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Notification {
    private String author;
    private String title;
    private String text;
    private String published_date;

    public Notification(){

    }

    public Notification(String author, String title, String text, String published_date){
        this.author = author;
        this.title = title;
        this.text = text;
        this.published_date = published_date;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAuthor(String author){
        this.author=author;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setPublished_date(String published_date){
        this.published_date = published_date;
    }

    public String getAuthor(){
        return this.author;
    }

    public String getTitle(){
        return this.title;
    }

    public String getText(){
        return this.text;
    }

    public String snippet(){
        if(text.length() > 20){
            String str = text.substring(0, 20);
            return str + "...";
        }

        return text;
    }

    public String getPublished_date(){

        return published_date;
    }
}
