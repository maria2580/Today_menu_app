package com.example.today_menu_app.network;


import android.util.Log;

import com.example.today_menu_app.data_objects.CommentDto;
import com.example.today_menu_app.data_objects.CommentsDto;
import com.example.today_menu_app.data_objects.SuggestionDto;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CallRetrofit {
    private static void post_image(File imageFile, String date){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", imageFile.getName(), requestFile);


        Retrofit retrofit = RetrofitClient.getInstance();
        RetrofitAPI service= retrofit.create(RetrofitAPI.class);
        Call<String> call = service.postImage_L(body,date);
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

/*
System.out.println("token = ${Api.authToken}");
        file = File.;//convertBitmapToFile(bitmap);
        RequestBody survey = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipart = MultipartBody.Part.createFormData("picture", "${imageId?.value}.jpg", survey);
        Api.auth.faceCheck(multipart).enqueue(Callback<Boolean> object{
            @Override
            void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() == true) System.out.println("인식 성공");
                else System.out.println("인식 실패");
            }
            @Override
            void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("네트워크 에러");
                Toast.makeText(this, "네트워크를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    } else {
        Toast.makeText(this, "사진을 업로드 해주세요.",Toast.LENGTH_SHORT).show();

    }
 */


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
        CommentsDto commentsDto=null;
        boolean result;
        Retrofit retrofit = RetrofitClient.getInstance();
        RetrofitAPI service= retrofit.create(RetrofitAPI.class);
        Call<CommentsDto> call = service.getComments(day);

        try {
            commentsDto=call.execute().body();
        }catch (Exception e){e.printStackTrace();}

        //Retrofit 호출



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