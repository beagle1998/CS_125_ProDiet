package com.example.prodiet;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private EditText profile_height;
    private EditText profile_weight;
    private EditText profile_birthyear;
    private EditText profile_gender;
    private CheckBox profile_vegan;
    private Button profile_update;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        profile_height = getView().findViewById(R.id.profile_height);
        profile_weight = getView().findViewById(R.id.profile_weight);
        profile_birthyear = getView().findViewById(R.id.profile_birthyear);
        profile_gender = getView().findViewById(R.id.profile_gender);
        profile_vegan = getView().findViewById(R.id.profile_vegan);
        profile_update = getView().findViewById(R.id.profile_update);

        profile_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    public void updateUser() {
        String height_string = profile_height.getText().toString().trim();
        final Double height = ( height_string.isEmpty() ? User.getHeight() : Double.valueOf(height_string) );

        String weight_string = profile_weight.getText().toString().trim();
        final Double weight = ( weight_string.isEmpty() ? User.getWeight() : Double.valueOf(weight_string) );

        String birthyear_string = profile_birthyear.getText().toString().trim();
        final Integer birthyear = ( birthyear_string.isEmpty() ? User.getBirthyear() : Integer.valueOf(birthyear_string) );

        String gender_string = profile_gender.getText().toString().trim();
        final String gender = ( gender_string.isEmpty() ? User.getGender() : gender_string );

        final Boolean vegan = profile_vegan.isChecked();


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", User.getUsername());
        hashMap.put("password", User.getPassword());
        hashMap.put("height", height);
        hashMap.put("weight", weight);
        hashMap.put("birthyear", birthyear);
        hashMap.put("gender", gender);
        hashMap.put("vegan", vegan);

        DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        fb_ref.child("Users/"+User.getUsername()).setValue(hashMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    User.setHeight(height);
                    User.setWeight(weight);
                    User.setBirthyear(birthyear);
                    User.setGender(gender);
                    User.setVegan(vegan);
                    Toast.makeText(getActivity(), "Update successful!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
