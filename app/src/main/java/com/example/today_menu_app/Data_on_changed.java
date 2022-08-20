package com.example.today_menu_app;

import android.graphics.Bitmap;

import com.example.today_menu_app.comment_recycler.CommentData;
import com.example.today_menu_app.data_objects.CommentDto;
import com.example.today_menu_app.data_objects.CommentsDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Data_on_changed extends Observable {
    private static Data_on_changed data_on_changed;
    private String menu_lunch;
    private String menu_dinner;
    private Bitmap bitmap_lunch;
    private Bitmap bitmap_dinner;
    private String[] comment_Data;
    private String[] comment_written_time;
    private CommentDto[] commentDtos;
    public List<Observer> observerList = new ArrayList<>();
    public void addObserver(Observer o){
        observerList.add(o);
    }

    @Override
    public void notifyObservers() {
        System.out.println("Data_on_changed.notifyObservers");
        super.notifyObservers();
        Data_on_changed.get_instance().observerList.get(0).update(this,this);
    }




    private Data_on_changed(){
        super();
    }
    public static Data_on_changed get_instance(){
        if (data_on_changed==null){
            synchronized (Data_on_changed.class){
                if (data_on_changed==null){
                    data_on_changed=new Data_on_changed();
                }
            }
        }
        return data_on_changed;
    }

    public String getMenu_lunch() {
        return menu_lunch;
    }

    public void setMenu_lunch(String menu_lunch) {
        this.menu_lunch = menu_lunch;
        this.notifyObservers();
    }

    public String getMenu_dinner() {
        return menu_dinner;
    }

    public void setMenu_dinner(String menu_dinner) {
        this.menu_dinner = menu_dinner;
        this.notifyObservers();
    }

    public Bitmap getBitmap_lunch() {
        return bitmap_lunch;
    }

    public void setBitmap_lunch(Bitmap bitmap_lunch) {
        this.bitmap_lunch = bitmap_lunch;
        this.notifyObservers();
    }

    public Bitmap getBitmap_dinner() {
        return bitmap_dinner;
    }

    public void setBitmap_dinner(Bitmap bitmap_dinner) {
        this.bitmap_dinner = bitmap_dinner;
        this.notifyObservers();
    }

    public CommentDto[] getComments_Data() {
        return commentDtos;
    }
    public void clear_comments(){
        this.comment_Data =new String[1];
        this.comment_Data[0]="댓글이 없습니다";
        this.notifyObservers();
    }
    public void setComment_Data(String [] comment_Data, String[] written_time) {
        this.comment_Data = comment_Data;
        this.comment_written_time=written_time;

        if(comment_Data!=null){
            commentDtos=new CommentDto[comment_Data.length];
            for (int i=0; i<comment_Data.length;i++){
                String t1 = comment_Data[i]==null?"댓글이 없습니다":comment_Data[i];
                String t2 =comment_written_time[i]==null?"":comment_written_time[i];
                commentDtos[i]=new CommentDto(t1,t2 );
            }
        }
        else {
            commentDtos=new CommentDto[1];
            commentDtos[0]=new CommentDto("댓글이 없습니다","");
        }
        this.notifyObservers();
    }
}
