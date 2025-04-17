package com.example.today_menu_app.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.today_menu_app.Data_on_changed;
import com.example.today_menu_app.data_objects.CommentsDto;

import java.util.Base64;

public class Get_Comments_Thread extends Thread{
    public String day;

    private static Get_Comments_Thread thread;
    private Get_Comments_Thread(){super();}
    public static Get_Comments_Thread getInstance(){
        if (thread==null){
            synchronized (Get_Comments_Thread.class){
                if (thread==null){
                    thread=new Get_Comments_Thread();
                }
            }
        }
        return thread;
    }
    public static Get_Comments_Thread newInstance(){
        thread =new Get_Comments_Thread();
        return thread;
    }
    @Override
    public void run() {
        Data_on_changed data = Data_on_changed.get_instance();
        data.setComment_Data(null , null);
        //DB에서 댓글을 불러와서 스트링배열로 반환하는 메서드, 세진
        //해당 날짜의 댓글 DB에서 댓글 갯수를 먼저 읽어와서 인트형 변수로 저장하는 명령 작성
        System.out.println("get comments 함수 시작");
        CommentsDto commentsDto= CallRetrofit.get_comments(day);

        System.out.println("REST GET 요청 완료 to "+day);
        System.out.println("상태 체크 of commentDto is "+commentsDto);
        try {
            data.setComment_Data(commentsDto.getContent(), commentsDto.getWritten_time());
        }
        catch (Exception e){
            System.out.println("이 문구가 나오면 스프링 서버가 작동중이지 않을 가능성이 높습니다.");
            System.out.println("따라서 댓글이 보이지 않는게 정상입니다.");
            data.setComment_Data(new String[1],new String[1]);
        }
        System.out.println("DTO에 담긴 내용 복사 "+data.getComments_Data());
        try {
            for (int i = 0; i < data.getComments_Data().length; i++) {
                System.out.println("댓글 " + i + " " + data.getComments_Data()[i].getContent()+"쓰인 시간: "+ data.getComments_Data()[i].getDate());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
