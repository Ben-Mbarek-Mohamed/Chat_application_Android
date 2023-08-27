package com.example.chatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat extends AppCompatActivity {
    private ImageView btnBack,imaProf,btnSend;
    private TextView usName;
    private EditText messageEditText;
    private String currentUserId;
    private String user2Id;
    private String messagetxt;
    private RecyclerView chatRv;
    private List<ChatList> chatListList =new ArrayList<>();
    private ChatAdap chatAdapter;
    private List<Message> messageList=new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("chatApp").child("chat");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chatListList.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        String name=getIntent().getStringExtra("name");
        String img=getIntent().getStringExtra("Img");
        user2Id=getIntent().getStringExtra("user2");

        currentUserId=getIntent().getStringExtra("currentUserId");


        btnBack=findViewById(R.id.chatback);
        imaProf=findViewById(R.id.chatpp);
        btnSend=findViewById(R.id.sendbtn);
        usName=findViewById(R.id.chatname);
        messageEditText=findViewById(R.id.messagetext);
        chatRv=findViewById(R.id.chattingRV);
        messagetxt=messageEditText.getText().toString();
        usName.setText(name);
        chatRv.setHasFixedSize(true);
        chatRv.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        chatAdapter=new ChatAdap(messageList,Chat.this,currentUserId);
        chatRv.setAdapter(chatAdapter);
        EventLisner();



        if (img!=null){
            byte[] bytes= Base64.decode(img,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            imaProf.setImageBitmap(bitmap);
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Chat.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getMessageText=messageEditText.getText().toString();
                final String currentTime= String.valueOf(System.currentTimeMillis());
                Long crtTime=Long.parseLong(currentTime);
                if (!getMessageText.isEmpty()) {
                    Message message = new Message(getMessageText, currentUserId, user2Id, crtTime, "");

                    myRef.push().setValue(message);
                    messageEditText.setText("");
                    EventLisner();
                }


            }
        });

    }
    private void EventLisner(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                chatAdapter.updateData(messageList);
                for (DataSnapshot datasnap:snapshot.getChildren()
                ) {

                    Message message = datasnap.getValue(Message.class);
                    if (message.getReceuverId().equals(currentUserId) || message.getSenderId().equals(currentUserId)) {
                        if (user2Id.equals(message.getReceuverId()) || user2Id.equals(message.getSenderId())) {
                            message.setCurrentUser(currentUserId);
                            messageList.add(message);

                        }


                    }
                }
                chatRv.setAdapter(chatAdapter);
                chatRv.scrollToPosition(messageList.size()-1);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}