package com.example.chatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText loginEmail, loginPass;
    private TextView singupR;
    private Button loginButton;
    private ProgressBar progressBarLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        loginEmail=findViewById(R.id.login_email);
        loginPass=findViewById(R.id.login_password);
        singupR=findViewById(R.id.singupR);
        loginButton=findViewById(R.id.login_button);
        progressBarLogin=findViewById(R.id.progress_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=loginEmail.getText().toString();
                String pass=loginPass.getText().toString();
                if (!email.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if (!pass.isEmpty()){
                        loading(true);
                        auth.signInWithEmailAndPassword(email,pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        loading(false);
                                        Toast.makeText(login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(login.this,MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loading(false);
                                        Toast.makeText(login.this,"login Failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else {
                        loginPass.setError("Password cant be empty");
                    }
                }
                else if (email.isEmpty()){
                    loginEmail.setError("Email cant be empty");
                }
                else {
                    loginEmail.setError("Invalid Email");
                }
            }
        });

        singupR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,singUp.class));
            }
        });


    }
    private void loading(boolean isLoading){
        if (isLoading){
            loginButton.setVisibility(View.INVISIBLE);
            progressBarLogin.setVisibility(View.VISIBLE);
        }
        else {
            loginButton.setVisibility(View.VISIBLE);
            progressBarLogin.setVisibility(View.INVISIBLE);
        }
    }
}