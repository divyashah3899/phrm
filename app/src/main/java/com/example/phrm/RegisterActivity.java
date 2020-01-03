package com.example.phrm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    String first_name,last_name,email,password,mobile;
    private EditText fname, lname, r_email, r_password, phone;
    private Button register;
    private TextView login;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String email = r_email.getText().toString().trim();
                    String password = r_password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                userSendData();
                                Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            }
                            else {
                                Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }

    private void setupUIViews() {
        fname = findViewById (R.id.fname);
        lname =  findViewById (R.id.lname);
        r_email =  findViewById (R.id.email);
        r_password =  findViewById (R.id.password);
        phone =  findViewById (R.id.phone);
        register =  findViewById (R.id.btn_register);
        login =  findViewById (R.id.login_link);
    }

    private boolean validate() {

        boolean result = false;

        first_name = fname.getText().toString();
        last_name = lname.getText().toString();
        email = r_email.getText().toString();
        password = r_password.getText().toString();
        mobile = phone.getText().toString();

        if (first_name.isEmpty() && last_name.isEmpty() && email.isEmpty() && password.isEmpty() && mobile.isEmpty()) {
            Toast.makeText(this,"Please Enter All The Details",Toast.LENGTH_SHORT).show();
        }
        else {
            result = true;
        }
        return result;
    }

    private void userSendData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(first_name,last_name,email,mobile);
        myRef.setValue(userProfile);
    }
}
