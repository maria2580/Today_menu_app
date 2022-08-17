package com.example.today_menu_app.comment_recycler;

public class CommentData {
    private int ID;
    private String Content;
    private String Date_written;

    public CommentData(int ID, String content, String date_written) {
        this.ID = ID;
        Content = content;
        Date_written = date_written;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate_written() {
        return Date_written;
    }

    public void setDate_written(String date_written) {
        Date_written = date_written;
    }
}
