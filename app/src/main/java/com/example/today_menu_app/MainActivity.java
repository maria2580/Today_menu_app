package com.example.today_menu_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.today_menu_app.SQLite.MyDBhelper;
import com.example.today_menu_app.comment_recycler.CommentAdapter;
import com.example.today_menu_app.comment_recycler.CommentData;
import com.example.today_menu_app.crawling.MyThread;
import com.example.today_menu_app.network.CallRetrofit;
import com.example.today_menu_app.network.Get_Comments_Thread;
import com.example.today_menu_app.network.Get_Dinner_image_Thread;
import com.example.today_menu_app.network.Get_Lunch_image_Thread;

public class MainActivity extends AppCompatActivity {

    ArrayList<CommentData> arrayList;
    private CommentAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    int intiated=0;
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
    //????????? ?????? ??????
    Uri image_uri;
    int targetImage;//????????? ??????????????? ?????? ???????????? ?????? (1:?????? ???????????? ?????? 2:?????? ???????????? ??????) ????????? ????????? ????????? ???????????? ????????? ??????
    //?????? ?????? ??????
    long now;//?????? ????????? ?????? ??????
    String today;//?????? ???????????? ?????? ????????? ?????? ??????
    String day;//?????? ???????????? ?????? ??????(????????? ?????? ??? ????????? ??????, ??????)

    SQLiteDatabase db;
    MyDBhelper myDBhelper;

    Data_on_changed data;
    Observer observer;
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
                            set_lunch_image_on_DB(day);
                        }else{
                            imageView2.setImageBitmap(bitmap);
                            set_dinner_image_on_DB(day);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int check=0;
        data=Data_on_changed.get_instance();
       //System.out.println("hihihihihihi   :    where?    ---> "+ (check++)+ " " + getTaskId() );//1???
        observer=new Observer() {
            @Override
            public void update(Observable observable, Object o) {
               //System.out.println("MainActivity.update");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data=(Data_on_changed) observable;
                        if (intiated<3){
                            //System.out.println("MainActivity.update   initiated = "+intiated);
                            intiated++;
                            return;
                        }
                        update_changes_on_layout();
                    }
                });
            }
        };
        data.addObserver(observer);
        myDBhelper = new MyDBhelper(this);
        db = myDBhelper.getWritableDatabase();
        now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        today = sdf.format(new Date(now));//????????????????????? ?????? ???????????? ?????? ????????? ??????

        day = today;
       //System.out.println("hihihihihihi   :    where?    ---> "+ check++);//2???




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


        this.arrayList =new ArrayList<CommentData>();
        mainAdapter = new CommentAdapter(this.arrayList);
        recyclerView.setAdapter(mainAdapter);
       //System.out.println("hihihihihihi   :    where?    ---> "+ check++);//3???
        getMenu_on_web(day);
        get_lunch_image_on_DB(day);
        get_dinner_image_on_DB(day);
       Thread ttt= new Thread(){
           @Override
            public void run() {
                super.run();
                getCommentStrings(day);

            }

        };
       ttt.start();
       try {ttt.join();}catch (Exception e) {e.printStackTrace();}
       //System.out.println("hihihihihihi   :    where?    ---> "+ check++);//4???

       // mainAdapter.notifyDataSetChanged();
         // data.getCustomObservable().notifyObservers();

        textview_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getSupportFragmentManager(),"datePicker");
            }
        });


        button_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long t=86400000;//??????
                now-=t;
                update_data();
                //mainAdapter.notifyDataSetChanged();
                //data.notifyObservers();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long t=86400000;//??????
                now+=t;
                update_data();

                //mainAdapter.notifyDataSetChanged();
                //data.notifyObservers();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageOnLocal(1,day);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageOnLocal(2,day);
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
                //setCommentOnLayout(day);
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
    void update_data(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        day = sdf.format(new Date(now));
        arrayList=(new ArrayList<CommentData>());
        getMenu_on_web(day);
        get_lunch_image_on_DB(day);
        get_dinner_image_on_DB(day);
        Thread ttt= new Thread(){
            @Override
            public void run() {
                super.run();
                getCommentStrings(day);
            }
        };
        ttt.start();
        try {ttt.join();}catch (Exception e) {e.printStackTrace();}
    }

    void getMenu_on_web(String day){
       //System.out.println("MainActivity.getMenu_on_web");
        data.setMenu_lunch("?????? ???????????? ???");
        data.setMenu_dinner("?????? ???????????? ???");
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



       //System.out.println("year, month, week, dd, realstart, realend, now"+ year+" " +month+" "+week+" "+dd+" "+realstart+" "+realend+" "+now);

        try {

            Elements elements = doc.select("table[class=table-board food]");
            elements.select("tbody");

            Elements elements2 = elements.select("tbody");
            Element element3;
            int i = 0;
            for (element3 = elements2.select("tr").first(); i < 7; ) {
                Element element4 = element3.select("td[data-mqtitle=date]").first();
                String s = element4.text();
                if (s.equals(String.format("%s??? %s???", month, dd))) {

                    Element e = element3.select("td[data-mqtitle=lunch]").first();
                    Element e2 = element3.select("td[data-mqtitle=dinner]").first();
                    data.setMenu_lunch(e.text());
                    data.setMenu_dinner(e2.text());

                   //System.out.println("true??? ??????????????????" + "menu_lunch" + data.getMenu_lunch() + "\n" + element3.text());
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
    }//?????? ?????? ?????? ?????? ???????????? ??????????????? ??????????????? ??????, ????????????

    void getImageOnLocal(int targetImage,String day) {
       //System.out.println("MainActivity.getImageOnLocal");
        this.targetImage=targetImage;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityResult.launch(intent);
    }//?????? ????????? ?????? ????????? ????????? ?????? ?????????,??????

    void set_lunch_image_on_DB(String day) {
       //System.out.println("MainActivity.set_lunch_image_on_DB");
        BitmapDrawable drawable =targetImage==1?(BitmapDrawable) imageView.getDrawable():(BitmapDrawable)imageView2.getDrawable();//todo ????????? ????????? ???????????? ??? ?????????
        Bitmap bitmap=drawable.getBitmap();
       /* File file = new File(getCacheDir()+"/images");
        file.mkdirs();
        File fileCacheItem = new File(getCacheDir()+"/images/temp.jpg");

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
        try {
            fileCacheItem.createNewFile();
            int quality = 100;
            int i = 2;
           //System.out.println("MainActivity.set_lunch_image_on_DB   1" + " " + fileCacheItem.length());
           //System.out.println("MainActivity.set_lunch_image_on_DB   1" + " " + Files.size(fileCacheItem.toPath()));
            do {
                try {
                    bitmap=drawable.getBitmap();
                    out = new FileOutputStream(fileCacheItem);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                   //System.out.println("MainActivity.set_lunch_image_on_DB  " + (i++) + " " + bitmap.getByteCount());
                    quality = quality - 5;
                    if (quality < 5) {
                        break;
                    }
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (fileCacheItem.length()> (long) 1048576);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
        String encodedImage=BitmapToString(bitmap);
        CallRetrofit.post_image_L(encodedImage, day);

    }
    void set_dinner_image_on_DB(String day) {
       //System.out.println("MainActivity.set_dinner_image_on_DB");
        BitmapDrawable drawable =targetImage==1?(BitmapDrawable) imageView.getDrawable():(BitmapDrawable)imageView2.getDrawable();
        Bitmap bitmap=drawable.getBitmap();
        /*File file = new File(getCacheDir()+"/images");

        file.mkdirs();
        File fileCacheItem = new File(getCacheDir()+"/images/temp.jpg");

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
        try {
            fileCacheItem.createNewFile();

            int quality=100;
            do {
                try {
                    bitmap=drawable.getBitmap();
                    out = new FileOutputStream(fileCacheItem);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                    quality = quality - 5;
                    if (quality < 5) {
                        break;
                    }
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (bitmap.getByteCount()> (long) 1048576);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
        String encodedImage=BitmapToString(bitmap);
        CallRetrofit.post_image_D(encodedImage, day);
    }
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.getEncoder().encodeToString(bytes);
        return temp;
    }
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.getDecoder().decode(encodedString);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    void get_lunch_image_on_DB(String day){
       //System.out.println("MainActivity.get_lunch_image_on_DB");
       //System.out.println(image_uri);
        Get_Lunch_image_Thread t = Get_Lunch_image_Thread.getInstance();
        if(t.isAlive()){
            try {
                t.interrupt();
            }catch (Exception e){}
        }
        t=Get_Lunch_image_Thread.newInstance();
        t.day =day;
        t.start();
       //System.out.println("MainActivity.get_lunch_image_on_DB");
    }
    void get_dinner_image_on_DB(String day){
       //System.out.println("MainActivity.get_dinner_image_on_DB");
        Get_Dinner_image_Thread t = Get_Dinner_image_Thread.getInstance();
        if(t.isAlive()){
            try {
                t.sleep(100);
                t.interrupt();
            }catch (Exception e){}
        }
        t=Get_Dinner_image_Thread.newInstance();
        t.day =day;
        t.start();
    }

    void sendVoteUp(String day){
       // db.execSQL(String.format("insert into comments(ID, Content, Date) values(%d,'%s','%s');",4,"f","ate"));//????????? ?????? ??????
        //flag ??? ?????? 0????????? 1?????? 2?????????????????? ?????? ????????????????????? ???????????? ?????? ??????
        Cursor cursor;
        int flag=2;
        cursor = db.rawQuery(String.format("SELECT * FROM groupTBL WHERE DATE = %s;",day),null);//???????????? ???????????? ??????
        while(cursor.moveToNext()){
            int t = cursor.getColumnIndex("content");
            flag = cursor.getInt(t);
        }
        if(flag==0){
            db.execSQL(String.format("update groupTBL SET content=1 WHERE DATE = %s;",day));
            //????????? ???????????? 1?????? ????????? 1??????????????? ??????
        }
        if (flag==1){
            db.execSQL(String.format("DELETE * FROM groupTBL WHERE DATE = %s;",day));
            //????????? ????????? 1??????????????? ??????
        }
        if (flag==2) {
            int a;
            cursor = db.rawQuery("SELECT ID FROM groupTBL ORDER BY id DESC LIMIT 1;", null);
            if (cursor.moveToNext()) {
                a = cursor.getInt(0);
            } else {
                a = 0;
            }
            db.execSQL(String.format("insert into groupTBL(ID,content,DATE) values (%d,%d,'%s')", a + 1, 1, day));
            //????????? ???????????? 1??????????????? ??????
        }
    cursor.close();
    }//?????? ??????????????? DB??? ?????????????????? ?????????, ??????
    void sendVoteDown(String day){
        Cursor cursor;
        int flag=2;
        cursor = db.rawQuery(String.format("SELECT * FROM groupTBL WHERE DATE = %s;",day),null);//???????????? ???????????? ??????
        while(cursor.moveToNext()){
            int t = cursor.getColumnIndex("content");
            flag = cursor.getInt(t);
        }
        if(flag==0){
            db.execSQL(String.format("DELETE * FROM groupTBL WHERE DATE = %s;",day));
            //????????? ???????????? 1?????? ????????? ??????
        }
        if (flag==1){
            db.execSQL(String.format("update groupTBL SET content=0 WHERE DATE = %s;",day));
            //????????? ????????? 1??????????????? ?????? ???????????? 1??????????????? ??????
        }
        if (flag==2){
            int a;
            cursor = db.rawQuery("SELECT ID FROM groupTBL ORDER BY id DESC LIMIT 1;", null);
            if (cursor.moveToNext()) {
                a = cursor.getInt(0);
            } else {
                a = 0;
            }
            db.execSQL(String.format("insert into groupTBL(ID,content,DATE) values (%d,%d,'%s')", a + 1, 0, day));
            //????????? ??????????????? 1??????????????? ??????
        }
        cursor.close();
    }//?????? ??????????????? DB??? ?????????????????? ?????????, ??????


    void sendRequest(String day){
        Intent intent= new Intent(getApplicationContext(), SubActivity.class);
        intent.putExtra("date", day);
        startActivity(intent);
    }//??? ???????????? ???????????? ?????? ?????? ????????? ????????? ???????????? ?????????, ??????


    void getCommentStrings(String day){
        Get_Comments_Thread t = Get_Comments_Thread.getInstance();
        if(t.isAlive()){
            try {
                t.interrupt();
            }catch (Exception e){}
        }
        t=Get_Comments_Thread.newInstance();
        t.day =day;
        t.start();
    }

    void setCommentOnLayout(String day){
       //System.out.println("MainActivity.setCommentOnLayout");
        //????????? ????????????, ???????????? ????????? ????????? ??? ?????? ???.
        // ????????? strings??? ????????? ??????.
        // ??? ????????? ????????? ??????????????? ?????? ?????? ??????, ?????? ??????
       //System.out.println("!!! ?????? ????????? ??? ??????!!!!");
        if (data.getComments_Data()==null){
            return;
        }
        this.arrayList=new ArrayList<>();
        mainAdapter = new CommentAdapter(arrayList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        long t_now=System.currentTimeMillis();
        long here =new Date(t_now).getTime();

        for(int i = 0; i<data.getComments_Data().length; i++){
           //System.out.println("?????? ????????????????????? ?????????  "+i+"???");
            String target=data.getComments_Data()[i].getDate();
            try {
                Date date = sdf.parse(target);
                long written = date.getTime();

                long cha = here - written;// - DateUtils.HOUR_IN_MILLIS*9;
                String temppp = sdf.format(new Date(cha));
               //System.out.println("?????? ?????? -?????? ??? ?????? : " + cha + "  " + "?????? ????????? : " + temppp);
                String yy = data.getComments_Data()[i].getDate().substring(2, 4);
                String MM = data.getComments_Data()[i].getDate().substring(5, 7);
                String dd = data.getComments_Data()[i].getDate().substring(8, 10);
                String hh = data.getComments_Data()[i].getDate().substring(11, 13);
                String mm = data.getComments_Data()[i].getDate().substring(14, 16);
                String ss = data.getComments_Data()[i].getDate().substring(17, 19);
                if (cha / DateUtils.YEAR_IN_MILLIS == 0)
                    if (cha / (DateUtils.DAY_IN_MILLIS * 30) == 0)
                        if (cha / DateUtils.DAY_IN_MILLIS == 0)
                            if (cha / DateUtils.HOUR_IN_MILLIS == 0)
                                if (cha / DateUtils.MINUTE_IN_MILLIS == 0)
                                    target = (cha / 1000) + "??? ???";
                                else
                                    target = (cha / DateUtils.MINUTE_IN_MILLIS) + "??? ???";
                            else
                                target = (cha / DateUtils.HOUR_IN_MILLIS) + "?????? ???";
                        else
                            target = (cha / DateUtils.DAY_IN_MILLIS) + "??? ???";
                    else
                        target = mm + "??? " + dd + "??? " + hh + ":" + mm;
                else
                    target = yy + "??? " + mm + "??? " + dd + "??? " + hh + ":" + mm;
            }
            catch (Exception e){}

            this.arrayList.add(new CommentData(arrayList.size(),data.getComments_Data()[i].getContent(),target));
           //System.out.println("????????? ????????? : "+ arrayList.get(i).getContent()+" "+ arrayList.get(i).getDate_written());
        }

        mainAdapter = new CommentAdapter(arrayList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();

    }
    void addComment(final String mycontent,String day){
        if(mycontent.length()<3){
            return;}
       //System.out.println("MainActivity.addComment");
        CallRetrofit.post_comment(mycontent,day);
       //System.out.println("?????? ?????? ??????: ");
        arrayList.add(new CommentData(arrayList.size(),mycontent,day));

        mainAdapter.notifyDataSetChanged();



    }//???????????? ???????????? ??????????????? ???????????? DB??? ???????????? ??????, ??????
    void update_changes_on_layout(){
       //System.out.println("MainActivity.update_changes_on_layout");
        textview_day.setText(day);
        imageView.setImageBitmap(data.getBitmap_lunch());
        imageView2.setImageBitmap(data.getBitmap_dinner());

        if(data.getMenu_lunch()==null||data.getMenu_lunch().equals("")){
            textMenu.setText("????????? ????????????");
        }
        else{
            textMenu.setText(data.getMenu_lunch());
        }
        if(data.getMenu_dinner()==null||data.getMenu_dinner().equals("")){
            textMenu2.setText("????????? ????????????");
        }
        else{
            textMenu2.setText(data.getMenu_dinner());
        }
        setCommentOnLayout(day);
    }

    public void setDatefrompicker(int y, int m , int d){
        String temp= y+"-"+m+"-"+d;
        Calendar calendar =Calendar.getInstance();
        calendar.set(y,m,d);
        now = calendar.getTimeInMillis();
        update_data();
    }

}

