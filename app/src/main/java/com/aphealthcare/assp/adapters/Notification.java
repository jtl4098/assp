package com.aphealthcare.assp.adapters;

import java.util.Calendar;

public class Notification {
    private String author;
    private String title;
    private String text;
    private long published_date;

    public Notification(){

    }

    public Notification(String author, String title, String text, long published_date){
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

    public void setPublished_date(long published_date){
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

    public String getPublished_date(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(published_date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String sYear = String.valueOf(year);
        String sMonth = String.valueOf(month);
        String sDay = String.valueOf(day);
        String date = sYear +"/" + sMonth + "/"+ sDay;
        return date;
    }
}
