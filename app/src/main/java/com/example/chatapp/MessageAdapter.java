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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    Context context;
    List<Message> messages;
    FirebaseFirestore db=FirebaseFirestore.getInstance();


    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.message_adapter_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        Message message=messages.get(position);
        String mestext=message.getMessage();
        String senderId=message.getSenderId();
        String reciverId=message.getReceuverId();
        String time=String.valueOf(message.getMessageTime());
        Long currentTimeMillis=Long.parseLong(time);
        SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date1=simpleDateFormat.format(currentTimeMillis);
        String time1=simpleTimeFormat.format(currentTimeMillis);
        holder.time.setText(date1+"  "+time1);
        holder.messagetxt.setText(mestext);
        String autherUserId="";
        if (message.getCurrentUser().equals(senderId)){
            autherUserId=reciverId;
        }
        else {
            autherUserId=senderId;
        }

        DocumentReference user=db.collection("users").document(autherUserId);
        String finalAutherUserId = autherUserId;
        user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String usName= (String) documentSnapshot.get("username");
                holder.name.setText(usName);
                String imgProf= (String) documentSnapshot.get("Image");
                if (imgProf!=null){
                    byte[] bytes= Base64.decode(imgProf,Base64.DEFAULT);
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    holder.pic.setImageBitmap(bitmap);

                }
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,Chat.class);
                        intent.putExtra("currentUserId",message.getCurrentUser());
                        intent.putExtra("user2", finalAutherUserId);
                        intent.putExtra("name",usName);
                        intent.putExtra("Img", imgProf);
                        context.startActivity(intent);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to upload data", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,messagetxt,time;
        ImageView pic;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.userName_message);
            messagetxt=itemView.findViewById(R.id.message_message);
            time=itemView.findViewById(R.id.message_time);
            pic=itemView.findViewById(R.id.pp_message);
            linearLayout=itemView.findViewById(R.id.lyt_message);
        }
    }
    public void updateData(List<Message> messages){

        this.messages=messages;
        notifyDataSetChanged();
    }
}
