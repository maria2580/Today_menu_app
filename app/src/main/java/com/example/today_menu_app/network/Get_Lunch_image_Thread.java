package com.example.today_menu_app.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Get_Lunch_image_Thread extends Thread{
    public String day;
    public Bitmap bitmap;
    @Override
    public void run() {
        super.run();

        byte[] bytes = new byte[0];
        try {
            bytes = CallRetrofit.get_image_L(day);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
