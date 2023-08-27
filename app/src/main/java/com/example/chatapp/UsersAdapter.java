package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyviewHolder> {
    Context context;
    List<User> users;


    public UsersAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.user_adapter_layout,parent,false);
        return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        User user=users.get(position);
        holder.userName.setText(user.getName());
        String pic=user.getPic();
        if (pic!=null){
            byte[] bytes= Base64.decode(pic,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            holder.pic.setImageBitmap(bitmap);
        }
        holder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Chat.class);
                intent.putExtra("currentUserId",user.getCurrentUserId());
                intent.putExtra("user2",user.getUserId());
                intent.putExtra("name",user.getName());
                intent.putExtra("Img", user.getPic());
                context.startActivity(intent);
            }
        });

    }
    public void updateData(List<User> users){
        this.users=users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public static class MyviewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        ImageView pic;
        LinearLayout layout1;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.userName);
            pic=itemView.findViewById(R.id.pp);
            layout1=itemView.findViewById(R.id.lyt);

        }
    }
}
