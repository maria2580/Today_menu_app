package com.example.today_menu_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.today_menu_app.SQLite.MyDBhelper;
import com.example.today_menu_app.comment_recycler.CommentAdapter;
import com.example.today_menu_app.comment_recycler.CommentData;
import com.example.today_menu_app.crawling.MyThread;
import com.example.today_menu_app.data_objects.CommentsDto;
import com.example.today_menu_app.network.CallRetrofit;
import com.example.today_menu_app.network.Get_Dinner_image_Thread;
import com.example.today_menu_app.network.Get_Lunch_image_Thread;

public class MainActivity extends AppCompatActivity {

    private ArrayList<CommentData> arrayList;
    private CommentAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    Button button_prev;
    Button button_next;
    Button button_like;
    Button button_dislike;
    Button button_claim;
    Button button_add_comment;
    TextView textview_day;
    TextView textMenu;
    TextView textMenu2;
    ImageView imageView;
    ImageView imageView2;
    EditText editText_comment_input;
    //이미지 관련 변수
    Uri image_uri;
    int targetImage;//클릭한 이미지뷰가 뭔지 알려주는 변수 (1:점심 이미지뷰 클릭 2:저녁 이미지뷰 클릭) 동일한 기능의 함수를 이용하기 위해서 사용
    //시간 관련 변수
    long now;//현재 시간을 받는 변수
    String today;//현재 시간으로 부터 변환한 오늘 날짜
    String day;//탭에 표시되는 표기 날짜(버튼을 누를 때 하루씩 감소, 증가)

    SQLiteDatabase db;
    MyDBhelper myDBhelper;

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if( result.getResultCode() == RESULT_OK && result.getData() != null){
                        image_uri = result.getData().getData();
                    }

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
                        if (targetImage==1){
                            imageView.setImageBitmap(bitmap);
                        }else{
                            imageView2.setImageBitmap(bitmap);
                        }
                        System.out.println(image_uri);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        now = System.currentTimeMillis();
        myDBhelper = new MyDBhelper(this);
        db= myDBhelper.getWritableDatabase();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        today=sdf.format(new Date(now));//안드로이드에서 날짜 받아오는 함수 사용할 예정
        day=today;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_prev = findViewById(R.id.button_prev);
        button_next = findViewById(R.id.button_next);
        button_like = findViewById(R.id.button_like);
        button_dislike = findViewById(R.id.button_dislike);
        button_claim = findViewById(R.id.button_claim);
        button_add_comment = findViewById(R.id.button_add_comment);

        imageView = findViewById(R.id.imageView);
        textMenu = findViewById(R.id.textView2);
        textMenu2 = findViewById(R.id.textView2_1);
        imageView2 = findViewById(R.id.imageView2);

        recyclerView = findViewById(R.id.recyclerView);
        textview_day = findViewById(R.id.textview_day);
        editText_comment_input = findViewById(R.id.editText_input);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        getMenu_and_set_on_layout(day);
        getImages_and_set_on_layout(day);


        arrayList = new ArrayList<>();
        mainAdapter = new CommentAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);

        new Thread(() -> {
            setCommentOnLayout(day);

        }).start();
        mainAdapter.notifyDataSetChanged();

        button_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long t=86400000;//하루
                now-=t;
                day = sdf.format(new Date(now));//함수 호출로 day값 상승 하강 시키는 함수 만들 예정임

                arrayList = new ArrayList<>();
                mainAdapter = new CommentAdapter(arrayList);
                recyclerView.setAdapter(mainAdapter);

                new Thread(() -> {
                    setCommentOnLayout(day);
                }).start();
                getImages_and_set_on_layout(day);
                getMenu_and_set_on_layout(day);
                mainAdapter.notifyDataSetChanged();

            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long t=86400000;//하루
                now+=t;

                day = sdf.format(new Date(now));

                arrayList = new ArrayList<>();
                mainAdapter = new CommentAdapter(arrayList);
                recyclerView.setAdapter(mainAdapter);

                new Thread(() -> {
                    setCommentOnLayout(day);
                }).start();
                mainAdapter.notifyDataSetChanged();
                getMenu_and_set_on_layout(day);
                getImages_and_set_on_layout(day);

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImageOnLayout(1,day);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImageOnLayout(2,day);
            }
        });
        button_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVoteUp(day);
            }
        });
        button_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVoteDown(day);
            }
        });
        button_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(day);
            }
        });
        button_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment(editText_comment_input.getText().toString(),day);
                editText_comment_input.setText("");

            }
        });

    }




    void getMenu_and_set_on_layout(String day){
        String menu_lunch = "메뉴 불러오는 중";
        String menu_dinner = "메뉴 불러오는 중";
        textview_day.setText(day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        String year = day.substring(0,4);
        String month = day.substring(5,7);
        String dd = day.substring(8,10);

        try{
            date = sdf.parse(day);
        }
        catch (Exception e){
            e.printStackTrace( );
            date = null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.WEEK_OF_MONTH);


        int now = calendar.get(Calendar.DAY_OF_WEEK);
        int start = Integer.parseInt(dd)-(now-1);
        int end = Integer.parseInt(dd)+(7-now);

        String realstart = year+month+start;
        String realend = year+month+end;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekNum = cal.get(Calendar.WEEK_OF_YEAR);
        Document doc=null;

        MyThread t1 = new MyThread();
        t1.setYear(year);
        t1.setMonth(month);
        t1.setWeek(Integer.toString(week));
        t1.setRealstart(realstart);
        t1.setRealend(realend);
        t1.start();
        try {
            t1.join();
            doc = t1.getDoc();
        }
        catch (Exception e){e.printStackTrace();}



        System.out.println("year, month, week, dd, realstart, realend, now"+ year+" " +month+" "+week+" "+dd+" "+realstart+" "+realend+" "+now);

        try {

            Elements elements = doc.select("table[class=table-board food]");
            elements.select("tbody");

            Elements elements2 = elements.select("tbody");
            Element element3;
            int i = 0;
            for (element3 = elements2.select("tr").first(); i < 7; ) {
                Element element4 = element3.select("td[data-mqtitle=date]").first();
                String s = element4.text();
                if (s.equals(String.format("%s월 %s일", month, dd))) {

                    Element e = element3.select("td[data-mqtitle=lunch]").first();
                    Element e2 = element3.select("td[data-mqtitle=dinner]").first();
                    menu_lunch = e.text();
                    menu_dinner = e2.text();

                    System.out.println("true로 진입했습니다" + "menu_lunch" + menu_lunch + "\n" + element3.text());
                    break;
                }
                if (i == 1) {
                    element3 = elements2.select("tr").next().first();
                } else if (i == 2) {
                    element3 = elements2.select("tr").next().next().first();
                } else if (i == 3) {
                    element3 = elements2.select("tr").next().next().next().first();
                } else if (i == 4) {
                    element3 = elements2.select("tr").next().next().next().next().first();
                } else if (i == 5) {
                    element3 = elements2.select("tr").next().next().next().next().next().first();
                } else if (i == 6) {
                    element3 = elements2.select("tr").next().next().next().next().next().next().first();
                }
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(menu_lunch.equals("")){
            menu_lunch = "메뉴가 없습니다";

            textMenu.setText(menu_lunch);
        }
        else{
            textMenu.setText(menu_lunch);
        }
        if(menu_dinner.equals("")){
            menu_dinner = "메뉴가 없습니다";
            textMenu2.setText(menu_dinner);
        }
        else{
            textMenu2.setText(menu_dinner);
        }
    }//날짜 값을 주면 식단 불러와서 레이아웃에 추가해주는 함수, 예지파트
    void getImages_and_set_on_layout(String day){

            get_dinner_image_on_DB(day);
            get_lunch_image_on_DB(day);

    }
    void setImageOnLayout(int targetImage,String day){
        this.targetImage=targetImage;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityResult.launch(intent);
        if(targetImage==1){
            set_lunch_image_on_DB(day);}
        else {
            set_dinner_image_on_DB(day);
        }
    }//해당 날짜의 식단 사진을 업로드 하는 메서드,예지

    void set_lunch_image_on_DB(String day) {
        BitmapDrawable drawable =targetImage==1?(BitmapDrawable) imageView.getDrawable():(BitmapDrawable)imageView2.getDrawable();
        Bitmap bitmap=drawable.getBitmap();
        File file = new File(getDataDir()+"/images");
        file.mkdirs();
        File fileCacheItem = new File(getDataDir()+"/images/temp.jpg");

        OutputStream out = null;
        if(!fileCacheItem.exists()) {
            try {
                FileWriter fw;
                fw = new FileWriter(fileCacheItem);
                fw.write("What should I do is always study.\nhehe");
                fw.flush();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        try
        {
            fileCacheItem.createNewFile();
            int quality=100;
            while(fileCacheItem.length()<(long)1048576){
                out = new FileOutputStream(fileCacheItem);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                quality=quality-5;
                if (quality<20){
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        CallRetrofit.post_image_L(fileCacheItem, day);

    }
    void set_dinner_image_on_DB(String day){
        {
            BitmapDrawable drawable =targetImage==1?(BitmapDrawable) imageView.getDrawable():(BitmapDrawable)imageView2.getDrawable();
            Bitmap bitmap=drawable.getBitmap();
            File file = new File(getDataDir()+"/images");
            file.mkdirs();
            File fileCacheItem = new File(getDataDir()+"/images/temp.jpg");

            OutputStream out = null;
            if(!fileCacheItem.exists()) {
                try {
                    FileWriter fw;
                    fw = new FileWriter(fileCacheItem);
                    fw.write("What should I do is always study.\nhehe");
                    fw.flush();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }


            try
            {
                fileCacheItem.createNewFile();

                int quality=100;
                while(fileCacheItem.length()<(long)1048576){
                    out = new FileOutputStream(fileCacheItem);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    quality=quality-5;
                    if (quality<20){
                        break;
                    }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            CallRetrofit.post_image_D(fileCacheItem, day);

        }
    }
    void get_lunch_image_on_DB(String day){
        Bitmap bitmap;// BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.no_image);
        byte[] bytes = new byte[0];
        try {
            bytes = CallRetrofit.get_image_L(day);
        }catch(Exception e){e.printStackTrace();}
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length ) ;
        Get_Lunch_image_Thread t = new Get_Lunch_image_Thread();
        t.day =day;
        t.start();
        try {
            t.join();
        }catch (Exception e){e.printStackTrace();}
        bitmap= t.bitmap;
        imageView.setImageBitmap(bitmap);
    }
    void get_dinner_image_on_DB(String day){
        Bitmap bitmap;//= BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.no_image);
        Get_Dinner_image_Thread t = new Get_Dinner_image_Thread();
        t.day =day;
        t.start();
        try {
            t.join();
        }catch (Exception e){e.printStackTrace();}
        bitmap= t.bitmap;
        imageView2.setImageBitmap(bitmap);
    }

    void sendVoteUp(String day){
       // db.execSQL(String.format("insert into comments(ID, Content, Date) values(%d,'%s','%s');",4,"f","ate"));//실행만 하는 함수
        //flag 의 값이 0비추천 1추천 2둘다취소거나 그냥 데이터베이스에 들어온적 없는 상태
        Cursor cursor;
        int flag=2;
        cursor = db.rawQuery(String.format("SELECT * FROM groupTBL WHERE DATE = %s;",day),null);//데이터를 받아오는 경우
        while(cursor.moveToNext()){
            int t = cursor.getColumnIndex("content");
            flag = cursor.getInt(t);
        }
        if(flag==0){
            db.execSQL(String.format("update groupTBL SET content=1 WHERE DATE = %s;",day));
            //서버쪽 비추천수 1감소 추천수 1증가시키는 코드
        }
        if (flag==1){
            db.execSQL(String.format("DELETE * FROM groupTBL WHERE DATE = %s;",day));
            //서버쪽 추천수 1감소시키는 코드
        }
        if (flag==2){
            db.execSQL(String.format("insert into groupTBL(ID,content,DATE) values (%d,%d,%d)"));
            //서버쪽 추천수만 1증가시키는 코드
        }

    }//추천 비추천수를 DB에 반영시켜주느 메서드, 시현
    void sendVoteDown(String day){
        Cursor cursor;
        int flag=2;
        cursor = db.rawQuery(String.format("SELECT * FROM groupTBL WHERE DATE = %s;",day),null);//데이터를 받아오는 경우
        while(cursor.moveToNext()){
            int t = cursor.getColumnIndex("content");
            flag = cursor.getInt(t);
        }
        if(flag==0){
            db.execSQL(String.format("DELETE * FROM groupTBL WHERE DATE = %s;",day));
            //서버쪽 비추천수 1감소 시키는 코드
        }
        if (flag==1){
            db.execSQL(String.format("update groupTBL SET content=0 WHERE DATE = %s;",day));
            //서버쪽 추천수 1감소시키는 코드 비추천수 1증가시키는 코드
        }
        if (flag==2){
            db.execSQL(String.format("insert into groupTBL(ID,content,DATE) values (%d,%d,%d)"));
            //서버쪽 비추천수만 1증가시키는 코드
        }
    }//추천 비추천수를 DB에 반영시켜주느 메서드, 시현


    void sendRequest(String day){
        Intent intent= new Intent(getApplicationContext(), SubActivity.class);
        intent.putExtra("date", day);
        startActivity(intent);
    }//이 메서드가 실행되면 식단 개선 요청을 보내는 화면으로 전환됨, 현지



    String[] getCommentStrings(String day){
        String[] strings;
        //DB에서 댓글을 불러와서 스트링배열로 반환하는 메서드, 세진
        //해당 날짜의 댓글 DB에서 댓글 갯수를 먼저 읽어와서 인트형 변수로 저장하는 명령 작성
       System.out.println("get comments 함수 시작");
        CommentsDto commentsDto;
        System.out.println("comments Dto 객체 생성 완료");
        commentsDto= CallRetrofit.get_comments(day);

        System.out.println("REST GET 요청 완료 to "+day);
        System.out.println("상태 체크 of commentDto is "+commentsDto);
        try {
            strings = commentsDto.getContent();
        }catch (Exception e){
            System.out.println("이 문구가 나오면 스프링 서버가 작동중이지 않을 가능성이 높습니다.");
            System.out.println("따라서 댓글이 보이지 않는게 정상입니다.");
            strings=new String[1];
        }
        System.out.println("DTO에 담긴 내용 복사 "+strings);
        try {
            for (int i = 0; i < strings.length; i++) {
                System.out.println("댓글 " + i + " " + strings[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return strings;
    }

    void setCommentOnLayout(String day){
        //화면이 처음뜰때, 새로고침 버튼을 눌렀을 떄 호출 됨.
        String [] strings = getCommentStrings(day);// 댓글을 strings에 불러온 상태.
        // 이 아래로 화면에 추가해주는 코드 작성 바람, 은비 담당
        for(int i =0 ; i<strings.length;i++){
            CommentData mainData = new CommentData(i, strings[i],day );
            arrayList.add(mainData);
        }
    }
    void addComment(final String mycontent,String day){
        CallRetrofit.post_comment(mycontent,day);
        System.out.println("댓글 추가 완료: ");

        arrayList = new ArrayList<>();
        mainAdapter = new CommentAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);
        new Thread(() -> {
            setCommentOnLayout(day);
        }).start();

        mainAdapter.notifyDataSetChanged();



    }//메서드가 실행되면 매개변수의 스트링이 DB로 보내지는 함수, 세진


}

