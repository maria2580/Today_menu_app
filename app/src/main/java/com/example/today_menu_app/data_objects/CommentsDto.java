package com.example.today_menu_app.data_objects;

import com.example.today_menu_app.comment_recycler.CommentData;
import com.google.gson.annotations.SerializedName;

public class CommentsDto {
    @SerializedName("content") private String[] content;
    @SerializedName("date_written") private String[] written_time;
    public CommentsDto(){

    }
    public void setContent(String [] content) {
        this.content=content;
        return ;
    }
    public String[] getContent() {

        return content;
    }

    public String[] getWritten_time() {
        return written_time;
    }

    public void setWritten_time(String[] written_time) {
        this.written_time = written_time;
    }
}
