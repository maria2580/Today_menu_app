package com.example.today_menu_app.comment_recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.today_menu_app.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CustomViewHolder>{
    private ArrayList<CommentData> arrayList;

    public CommentAdapter(ArrayList<CommentData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  //생성될 때 생명주기
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        CustomViewHolder holder =new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        //실제 추가될 떄의 생명주기
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (arrayList.get(position).getContent().equals("댓글이 없습니다")){
            holder.tv_Content.setText(arrayList.get(position).getContent());//만약 댓글이 없는 것으로 보이면 익명 %d 를 레이아웃에 추가 하지 않고 함수 종료
            return;
        }
        holder.tv_ID.setText(String.format("익명 %d",arrayList.get(position).getID()));
        holder.tv_Content.setText(arrayList.get(position).getContent());
        holder.tv_Date_written.setText(arrayList.get(position).getDate_written());
    }


    @Override
    public int getItemCount() {
        return (arrayList!=null ? arrayList.size():0);
    }

    public void remove(int position){
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_ID;
        protected TextView tv_Content;
        protected TextView tv_Date_written;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_ID=(TextView) itemView.findViewById(R.id.textView_ID);
            this.tv_Content=(TextView) itemView.findViewById(R.id.textView_content);
            this.tv_Date_written=(TextView)itemView.findViewById(R.id.textView_written_date);
        }
    }
}
