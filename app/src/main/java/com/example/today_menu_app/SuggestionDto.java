package com.example.today_menu_app;

import com.google.gson.annotations.SerializedName;

public class SuggestionDto {
    @SerializedName("content") private String content;
    @SerializedName("date") private String date;
    public SuggestionDto(String content, String date){
        this.content=content;
        this.date=date;
    }
    public SuggestionDto(){ }
    public String getContent(){
        return content;
    }
    public String getDate(){
        return date;
    }
}
