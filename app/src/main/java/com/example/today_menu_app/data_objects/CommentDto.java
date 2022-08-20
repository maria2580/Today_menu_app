package com.example.today_menu_app.data_objects;

import com.google.gson.annotations.SerializedName;

public class CommentDto {
    @SerializedName("content") private String content;
    @SerializedName("date_written") private String date;
    public CommentDto(String content, String date){
        this.content=content;
        this.date=date;
    }
    public CommentDto(){    }
    public String getContent() {
        return content;
    }
    public String getDate() {
        return date;
    }
}
