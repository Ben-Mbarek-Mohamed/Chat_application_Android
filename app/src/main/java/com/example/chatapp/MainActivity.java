package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView imgprofile,imgLogOut,messagesimg,usersimg;
    private TextView textView,textViewUserName;
    private FirebaseAuth auth;
    private RecyclerView usersRv;
    private List<User> users =new ArrayList<>();
    private List<Message> messages=new ArrayList<>();
    UsersAdapter usersAdapter;
    MessageAdapter messageAdapter;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("chatApp").child("chat");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        messages.clear();
        users.clear();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth= FirebaseAuth.getInstance();

        textView=findViewById(R.id.textView);
        imgprofile=findViewById(R.id.imageProfile2);
        textViewUserName=findViewById(R.id.textView2);
        imgLogOut=findViewById(R.id.imageView3);
        usersRv=findViewById(R.id.messagesRecycleView);
        messagesimg=findViewById(R.id.msgs);
        usersimg=findViewById(R.id.usrs);

        usersRv.setHasFixedSize(true);
        usersRv.setLayoutManager(new LinearLayoutManager(this));
        users =new ArrayList<User>();
        usersAdapter=new UsersAdapter(MainActivity.this, users);
        messageAdapter=new MessageAdapter(MainActivity.this,messages);
        usersRv.setAdapter(usersAdapter);


        messagesimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messagesimg.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email3_24));
                usersimg.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_supervisor_account2_24));
                textView.setText("Messages");
                EventChangeListener();
            }
        });
        usersimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagesimg.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email2_24));
                usersimg.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_supervisor_account_24));
                textView.setText("Users");
                EventLisner2();

            }
        });


        String uid=auth.getCurrentUser().getUid();
        DocumentReference user=db.collection("users").document(uid);
        user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String usName= (String) documentSnapshot.get("username");
                textViewUserName.setText(usName);
                String imgProf= (String) documentSnapshot.get("Image");
                if (imgProf!=null){
                    byte[] bytes= Base64.decode(imgProf,Base64.DEFAULT);
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    imgprofile.setImageBitmap(bitmap);

                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to upload data", Toast.LENGTH_LONG).show();
            }
        });
        imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this,login.class));
                finish();
            }
        });

        EventChangeListener();
    }


    private void EventChangeListener() {
        usersAdapter.updateData(new ArrayList<>());
        messages.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();

                    for (DataSnapshot datasnap:snapshot.getChildren()
                         ) {
                        Message message=datasnap.getValue(Message.class);
                        if (message.getReceuverId().equals(auth.getCurrentUser().getUid())||message.getSenderId().equals(auth.getCurrentUser().getUid())) {
                            message.setCurrentUser(auth.getCurrentUser().getUid());
                            messages.add(message);
                            Collections.sort(messages, (m1, m2) ->
                                    Long.compare(m2.getMessageTime(), m1.getMessageTime()));

                        }
                        for (int i=0;i<messages.size()-1;i++){
                            Message m1=messages.get(i);
                            for (int j=1;j<messages.size();j++){
                                Message m2=messages.get(j);
                                if ((m1.getReceuverId().equals(m2.getReceuverId())&&m1.getSenderId().equals(m2.getSenderId()))||m1.getReceuverId().equals(m2.getSenderId())&&m1.getSenderId().equals(m2.getReceuverId())){
                                    if (m2.getMessageTime()>m1.getMessageTime()){
                                        messages.remove(i);

                                    }
                                    else if (m2.getMessageTime()<m1.getMessageTime()){
                                        messages.remove(j);

                                    }
                                }

                            }
                        }
                        usersRv.setAdapter(messageAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void EventLisner2(){
        users.clear();
        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
                }
                for (DocumentChange dc : value.getDocumentChanges()
                ) {
                    if (!dc.getDocument().getId().equals(auth.getCurrentUser().getUid())) {

                        if (dc.getType().equals(DocumentChange.Type.ADDED)) {
                            String name = (String) dc.getDocument().get("username");
                            String pic = (String) dc.getDocument().get("Image");
                            User user = new User(name,pic,dc.getDocument().getId(),auth.getCurrentUser().getUid());
                            users.add(user);
                            usersAdapter.updateData(users);
                            usersRv.setAdapter(usersAdapter);

                        }
                    }
                }
            }
        });

    }


}