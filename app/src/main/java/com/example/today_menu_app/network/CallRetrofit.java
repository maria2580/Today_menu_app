package com.example.today_menu_app.network;


import android.util.Log;

import com.example.today_menu_app.comment_recycler.CommentData;
import com.example.today_menu_app.data_objects.CommentDto;
import com.example.today_menu_app.data_objects.CommentsDto;
import com.example.today_menu_app.data_objects.SuggestionDto;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SplittableRandom;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CallRetrofit {
    public static void post_image_L(String encodedImage, String date){
        /*RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);//(image/jpeg)로도 시도할 것
        MultipartBody.Part multi_file = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);*/


        Retrofit retrofit = RetrofitClient.getInstance();
        RetrofitAPI service= retrofit.create(RetrofitAPI.class);
        Call<String> call = service.postImage_L(encodedImage,date);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("uploadChat()", "성공 : ");

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("uploadChat()", "에러 : " + t.getMessage());

            }
        });
    }
    public static void post_image_D(String encodedImage, String date){/*
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part multi_file = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);*/


        Retrofit retrofit = RetrofitClient.getInstance();
        RetrofitAPI service= retrofit.create(RetrofitAPI.class);
        Call<String> call = service.postImage_D(encodedImage,date);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("uploadChat()", "성공 : ");

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("uploadChat()", "에러 : " + t.getMessage());

            }
        });
    }
    public static String get_image_L(String date) {

        Retrofit retrofit = RetrofitClient.getInstance();
        RetrofitAPI service= retrofit.create(RetrofitAPI.class);
        Call<String> call = service.getImage_L(date);
        String response = null;
        try {
            response= call.execute().body();

        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }
    public static String get_image_D(String date)  {
        Retrofit retrofit = RetrofitClient.getInstance();
        RetrofitAPI service= retrofit.create(RetrofitAPI.class);
        Call<String> call = service.getImage_D(date);
        String response = null;
        try {
            response= call.execute().body();
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }


    public static void post_comment(String content, String date){
        boolean result;

        //Retrofit 호출

        Retrofit retrofit = RetrofitClient.getInstance();
        RetrofitAPI service= retrofit.create(RetrofitAPI.class);
        Call<CommentDto> call = service.postComment(content,date);

        call.enqueue(new Callback<CommentDto>() {

            @Override
            public void onResponse(Call<CommentDto> call, Response<CommentDto> response) {
                if(response.isSuccessful()){
                    CommentDto result= response.body();
                    Log.d("연결이 성공적 : ", response.body().getContent());

                    return;
                }
                Log.e("연결이 비정상적 : ", "error code : " + response.code());
            }
            @Override
            public void onFailure(Call<CommentDto> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

    }
    public static CommentsDto get_comments(String day){
        CommentsDto commentsDto = new CommentsDto();
        String [] arr=new String[1];
        arr[0]="댓글이 없습니다";
        commentsDto.setContent(arr);
        boolean result;
        Retrofit retrofit = RetrofitClient.getInstance();
        RetrofitAPI service= retrofit.create(RetrofitAPI.class);
        Call<CommentsDto> call = service.getComments(day);

        try {
            commentsDto=call.execute().body();
        }catch (Exception e){e.printStackTrace();}

        //Retrofit 호출
            try {
                if (commentsDto.getContent().length==0){
                    commentsDto=new CommentsDto();
                    arr=new String[1];
                    arr[0]="댓글이 없습니다";
                    commentsDto.setContent(arr);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        return commentsDto;
    }
    public static void post_suggestions(String content, String date){

        Retrofit retrofit = RetrofitClient.getInstance();
        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Call<SuggestionDto> call= service.postsuggestion(content, date);

        call.enqueue(new Callback<SuggestionDto>() {
            @Override
            public void onResponse(Call<SuggestionDto> call, Response<SuggestionDto> response) {
                if(response.isSuccessful()){
                    SuggestionDto result=response.body();
                    Log.d("연결이 성공적", response.body().getContent());
                    return;
                }
                Log.e("연결이 비정상적: ","error code: "+response.code());
            }

            @Override
            public void onFailure(Call<SuggestionDto> call, Throwable t) {
                Log.e("연결실패",t.getMessage());

            }
        });
    }




}