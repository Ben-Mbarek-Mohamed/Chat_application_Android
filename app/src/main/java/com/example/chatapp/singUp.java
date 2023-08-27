package com.example.chatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class singUp extends AppCompatActivity {
    private FirebaseAuth auth;
    private ImageView imageView;
    private EditText userName,singupEmail, singupPassword, singupPasswordConfirm;
    private Button singupButton;
    private ProgressBar singupProgressBar;
    private TextView loginR;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        auth=FirebaseAuth.getInstance();
        imageView=findViewById(R.id.imageProfile);
        userName=findViewById(R.id.singup_name);
        singupEmail=findViewById(R.id.singup_email);
        singupPassword=findViewById(R.id.singup_password);
        singupPasswordConfirm=findViewById(R.id.passconfirm);
        singupButton=findViewById(R.id.singup_button);
        singupProgressBar=findViewById(R.id.progress);
        loginR=findViewById(R.id.loginR);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });


       singupButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              loading(true);
               String name=userName.getText().toString().trim();
               String email=singupEmail.getText().toString().trim();
               String pass=singupPassword.getText().toString().trim();
               String passConfirm=singupPasswordConfirm.getText().toString().trim();
               if (name .isEmpty()){
                   userName.setError("User Name cant be empty");
                   loading(false);
               }
               if (email .isEmpty()){
                   singupEmail.setError("Email cant be empty");
                   loading(false);
               }
               if (pass .isEmpty()){
                   singupPassword.setError("Password cant be empty");
                   loading(false);
               }
               if (passConfirm .isEmpty()){
                   singupPasswordConfirm.setError("Confirm your password");
                   loading(false);
               }
               if (!passConfirm.isEmpty() && !pass.equals(passConfirm)){
                   singupPasswordConfirm.setError("Password & password confirm dont match");
                   loading(false);
               }
               else if (!name.isEmpty()&&!email.isEmpty()&&!pass.isEmpty()&&!passConfirm.isEmpty()&&pass.equals(passConfirm)){
                   auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               String uid=auth.getCurrentUser().getUid();
                               FirebaseFirestore db=FirebaseFirestore.getInstance();
                               HashMap<String,Object> user = new HashMap<>();
                               user.put("username",name);
                               user.put("Image", encodedImage);
                               db.collection("users").document(uid).set(user)
                                       .addOnSuccessListener(DocumentReference->{
                                           loading(false);
                                           Toast.makeText(getApplicationContext(),"User created",Toast.LENGTH_SHORT).show();
                                           startActivity(new Intent(singUp.this,MainActivity.class));
                                       })
                                       .addOnFailureListener(Exception ->{
                                           loading(false);
                                           Toast.makeText(getApplicationContext(),Exception.getMessage(),Toast.LENGTH_SHORT);

                                       });
                           }

                       }
                   });




               }

           }
       });
        loginR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(singUp.this,login.class));
            }
        });
    }
    private void loading(boolean isLoading){
        if (isLoading){
            singupButton.setVisibility(View.INVISIBLE);
            singupProgressBar.setVisibility(View.VISIBLE);
       }
        else {
            singupButton.setVisibility(View.VISIBLE);
            singupProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    private String encoderImage(Bitmap bitmap){
        int prevWith=150;
        int prevHeight=bitmap.getHeight()*prevWith/bitmap.getWidth();
        Bitmap prevBitmap= Bitmap.createScaledBitmap(bitmap,prevWith,prevHeight,false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        prevBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes= byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);

    }
    private final ActivityResultLauncher<Intent> pickImage =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
        if (result.getResultCode() == RESULT_OK){
            if (result.getData() != null){
                Uri imageUri=result.getData().getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    encodedImage=encoderImage(bitmap);

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }

            });


}