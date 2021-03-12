package com.example.prodiet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private EditText login_username;
    private EditText login_password;
    private Button login_login;
    private Button login_register;

    /**
     * Allow user to login when creating the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        login_login = findViewById(R.id.login_login);
        login_register = findViewById(R.id.login_register);

        login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginLogin();
            }
        });

        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRegister();
            }
        });
    }


    /**
     * Get user information from entry boxes and verify data by communication with Firebase.
     * and jump to MainActivity if user is verified
     */
    public void loginLogin() {
        final String username = login_username.getText().toString().trim();
        if (username.isEmpty() || username.contains(" ")) {
            Toast.makeText(LoginActivity.this, "Invalid username!", Toast.LENGTH_LONG).show();
            return;
        }

        final String password = login_password.getText().toString().trim();
        if (password.isEmpty() || password.contains(" ")) {
            Toast.makeText(LoginActivity.this, "Invalid password!", Toast.LENGTH_LONG).show();
            return;
        }


        final DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        fb_ref.child("Users/"+username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<?,?> user_map = (Map)dataSnapshot.getValue();
                    if ( password.equals((String)user_map.get("password")) ) {
                        Object temp;

                        User.setUsername((String) user_map.get("username"));
                        User.setPassword((String) user_map.get("password"));
                        User.setGender((String) user_map.get("gender"));
                        temp = user_map.get("height");
                        User.setHeight((temp instanceof Double ? (Double) temp : ((Long) temp).doubleValue()));
                        temp = user_map.get("weight");
                        User.setWeight((temp instanceof Double ? (Double) temp : ((Long) temp).doubleValue()));
                        temp = user_map.get("birthyear");
                        User.setBirthyear((temp instanceof Integer ? (Integer) temp : ((Long) temp).intValue()));
                        User.setVegan((Boolean) user_map.get("vegan"));

                        Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent_main);
                        return;
                    }
                }
                Toast.makeText(LoginActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Jump to RegisterActivity
     */
    public void loginRegister() {
        Intent intent_register = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent_register);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
}