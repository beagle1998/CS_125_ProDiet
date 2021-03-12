package com.example.prodiet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    private EditText register_username;
    private EditText register_password;
    private EditText register_height;
    private EditText register_weight;
    private EditText register_birthyear;
    private EditText register_gender;
    private CheckBox register_vegan;
    private Button register_register;
    private Button register_login;

    /**
     * Allow user to sign up when creating the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_username = findViewById(R.id.register_username);
        register_password = findViewById(R.id.register_password);
        register_height = findViewById(R.id.register_height);
        register_weight = findViewById(R.id.register_weight);
        register_birthyear = findViewById(R.id.register_birthyear);
        register_gender = findViewById(R.id.register_gender);
        register_vegan = findViewById(R.id.register_vegan);
        register_register = findViewById(R.id.register_register);
        register_login = findViewById(R.id.register_login);

        register_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerRegister();
            }
        });

        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerLogin();
            }
        });
    }

    /**
     * Get user information from entry boxes and add user information into Firebase if not exists
     */
    public void registerRegister() {
        final String username = register_username.getText().toString().trim();
        if (username.isEmpty() || username.contains(" ")) {
            Toast.makeText(RegisterActivity.this, "Invalid username!", Toast.LENGTH_LONG).show();
            return;
        }

        final String password = register_password.getText().toString().trim();
        if (password.isEmpty() || password.contains(" ")) {
            Toast.makeText(RegisterActivity.this, "Invalid password!", Toast.LENGTH_LONG).show();
            return;
        }

        String height_string = register_height.getText().toString().trim();
        if (height_string.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Height cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }
        final Double height = Double.valueOf(height_string);

        String weight_string = register_weight.getText().toString().trim();
        if (weight_string.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Weight cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }
        final Double weight = Double.valueOf(weight_string);

        String birthyear_string = register_birthyear.getText().toString().trim();
        if (birthyear_string.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Birth year cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }
        final Integer birthyear = Integer.valueOf(birthyear_string);

        final String gender = register_gender.getText().toString().trim();
        if (gender.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Gender cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }

        final Boolean vegan = register_vegan.isChecked();


        DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        fb_ref.child("Users/"+username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {

                    User.setUsername(username);
                    User.setPassword(password);
                    User.setHeight(height);
                    User.setWeight(weight);
                    User.setBirthyear(birthyear);
                    User.setGender(gender);
                    User.setVegan(vegan);

                    addUser();
                    return;
                }
                Toast.makeText(RegisterActivity.this, "Username already exist!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Add user information into Firebase, and jump to MainActivity if success
     */
    public void addUser() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", User.getUsername());
        hashMap.put("password", User.getPassword());
        hashMap.put("height", User.getHeight());
        hashMap.put("weight", User.getWeight());
        hashMap.put("birthyear", User.getBirthyear());
        hashMap.put("gender", User.getGender());
        hashMap.put("vegan", User.getVegan());

        DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        fb_ref.child("Users/"+User.getUsername()).setValue(hashMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    User.clear();
                    Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent_main = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent_main);
                }
            }
        });
    }

    /**
     * Jump to LoginActivity
     */
    public void registerLogin() {
        Intent intent_login = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent_login);
    }
}