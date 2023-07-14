package com.example.musicplayer;
import com.example.musicplayer.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {


    EditText mEmail,mpassword;
    Button mLoginBtn;
    Button mCreateBtn;
    ProgressBar progressBar;


    FirebaseAuth fAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.create);
        progressBar = findViewById(R.id.progressBar);

        fAuth=FirebaseAuth.getInstance();


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String email=mEmail.getText().toString().trim();
                    String password=mpassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mpassword.setError("Password is required");
                    return;
                }
//                if (password.length() < 6) {
//                    mpassword.setError("Password must be greater than or equal to 8 characters");
//                    return;
//                }
                if (!isValidPassword(password)) {
                    mpassword.setError("Invalid password");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);


                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Log In Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }

                        else
                        {
                            Toast.makeText(getApplicationContext(), "error"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }

            private boolean isValidPassword(String password) {
                String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";

                // Compile the regular expression pattern
                Pattern pattern = Pattern.compile(passwordPattern);

                // Match the password against the pattern
                Matcher matcher = pattern.matcher(password);

                // Return true if the password matches the pattern, false otherwise
                return matcher.matches();
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();

            }
        });
    }
//    public void toregister(View view){
//        Intent intent=new Intent(Login.this,Register.class);
//        startActivity(intent);
//    }
    }
