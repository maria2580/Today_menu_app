package com.example.today_menu_app.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.today_menu_app.Data_on_changed;

import java.util.Base64;

public class Get_Lunch_image_Thread extends Thread{
    public String day;
    public Bitmap bitmap;
    private static Get_Lunch_image_Thread thread;
    private Get_Lunch_image_Thread(){super();}
    public static Get_Lunch_image_Thread getInstance(){
        if (thread==null){
            synchronized (Get_Lunch_image_Thread.class){
                if (thread==null){
                    thread=new Get_Lunch_image_Thread();
                }
            }
        }
        return thread;
    }

    public static Get_Lunch_image_Thread newInstance(){
        thread =new Get_Lunch_image_Thread();
        return thread;
    }
    @Override
    public void run() {
        super.run();

        String encodedImage = null;
        try {
            encodedImage = CallRetrofit.get_image_L(day);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bitmap = StringToBitmap(encodedImage);
        Data_on_changed.get_instance().setBitmap_lunch(bitmap);
    }
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.getDecoder().decode(encodedString);
            Bitmap bit = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bit;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
