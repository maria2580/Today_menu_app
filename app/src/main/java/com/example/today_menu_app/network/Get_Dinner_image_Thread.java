package com.example.today_menu_app.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Base64;

public class Get_Dinner_image_Thread extends Thread{
    public String day;
    public Bitmap bitmap;
    @Override
    public void run() {
        super.run();

        String encodedImage = null;
        try {
            encodedImage = CallRetrofit.get_image_D(day);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap = StringToBitmap(encodedImage);

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
