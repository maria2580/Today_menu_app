package com.example.today_menu_app.data_objects;

import com.google.gson.annotations.SerializedName;

public class CommentsDto {
    @SerializedName("content") private String [] content;
    public CommentsDto(){

    }
    public void setContent(String [] content) {
        this.content=content;
        return ;
    }
    public String[] getContent() {

        return content;
    }
}
