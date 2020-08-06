package com.example.calorieteststuff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    EditText emailId, password;
    Button buttonSignUp;
    TextView registerLink;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        link();
        register();

    }

    private void link(){
        registerLink = findViewById(R.id.lnkRegister_two);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    private void register(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText_email2);
        password = findViewById(R.id.editText_password2);
        buttonSignUp = findViewById(R.id.button_register);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Email Box Is Empty");
                    emailId.requestFocus();
                }else if(pwd.isEmpty()){
                    password.setError("Password Box Is Empty");
                    password.requestFocus();
                }
                else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this, "Registration Failed, Please Try Again",Toast.LENGTH_LONG).show();

                            }
                            else{
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            }
                        }
                    });
                }

            }
        });

    }


}
