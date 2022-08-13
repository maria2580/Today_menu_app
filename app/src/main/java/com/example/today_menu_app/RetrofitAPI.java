package com.example.today_menu_app;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitAPI {

    //@POST("")
   // Call<Model__CheckAlready> postOverlapCheck(@Body Model__CheckAlready modelCheckAlready); //이건 바디 요청시 사용하는거

    @POST("add/comments/{date}")
    Call <CommentDto> postComment(@Body String content, @Path("date") String date); //이건 요청시 사용하는거 (*데이터를 보낼때)

    @GET("comments/{day}")
    Call <CommentsDto> getComments(@Path("day") String day); //이건 요청시 사용하는거 (*데이터를 보낼때)

    @POST("add/Suggestion/{date}")
    Call<SuggestionDto>postsuggestion(@Body String content, @Path("date") String date);

    @Multipart
    @POST("add/images/lunch/{date}")
    Call<String> postImage_L(@Part MultipartBody.Part file,@Path("date") String date);

    @Multipart
    @POST("add/images/dinner/{date}")
    Call<String> postImage_D(@Part MultipartBody.Part file,@Path("date") String date);

    @Multipart
    @GET("images/lunch/{date}")
    Call<String> getImage_L(@Path("date") String date);

    @Multipart
    @GET("images/dinner/{date}")
    Call<String> getImage_D(@Path("date") String date);

}