package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChatAdap extends RecyclerView.Adapter<ChatAdap.MyViewHolder> {
    private List<Message> list2;
    private Context context;
    private String uid;

    public ChatAdap(List<Message> list2, Context context, String uid) {
        this.list2 = list2;
        this.context = context;
        this.uid = uid;
    }
    public void updateData(List<Message> messageList){
        this.list2=messageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatAdap.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.chat_adapter_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message=list2.get(position);
        if (message.getCurrentUser().equals(message.getSenderId())){
            holder.sendedlayout.setVisibility(View.VISIBLE);
            holder.receivedlayout.setVisibility(View.INVISIBLE);
            holder.sendedMessage.setText(message.getMessage());
            String time=String.valueOf(message.getMessageTime());
            Long currentTimeMillis=Long.parseLong(time);
            SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String date1=simpleDateFormat.format(currentTimeMillis);
            String time1=simpleTimeFormat.format(currentTimeMillis);
            holder.sendedTime.setText(date1+"  "+time1);

        }
        else {
            holder.sendedlayout.setVisibility(View.INVISIBLE);
            holder.receivedlayout.setVisibility(View.VISIBLE);
            holder.receivedMessage.setText(message.getMessage());
            String time=String.valueOf(message.getMessageTime());
            Long currentTimeMillis=Long.parseLong(time);
            SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String date1=simpleDateFormat.format(currentTimeMillis);
            String time1=simpleTimeFormat.format(currentTimeMillis);
            holder.receivedTime.setText(date1+"  "+time1);

        }

    }

    @Override
    public int getItemCount() {
        return list2.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout receivedlayout;
        private LinearLayout sendedlayout;
        private TextView sendedMessage,receivedMessage,sendedTime,receivedTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            receivedlayout=itemView.findViewById(R.id.receivedlayout);
            sendedlayout=itemView.findViewById(R.id.sendedlayout);
            sendedMessage=itemView.findViewById(R.id.sendedMessage);
            receivedMessage=itemView.findViewById(R.id.recievedMessage);
            sendedTime=itemView.findViewById(R.id.sendedTime);
            receivedTime=itemView.findViewById(R.id.recTime);
        }
    }

}
