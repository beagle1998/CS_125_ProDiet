package com.example.prodiet;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainFragment extends Fragment {

    private TextView main_title;
    private ArrayList<Food> food_list;
    private ListView main_recommend;
    private FoodListViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        if (food_list == null) {
            food_list = new ArrayList<>();
        } else {
            food_list.clear();
        }
        adapter = new FoodListViewAdapter(food_list, getActivity());

        main_title = getView().findViewById(R.id.main_title);
        main_recommend = getView().findViewById(R.id.main_recommend);

        if (User.getUsername().length() < 7) {
            main_title.setText("Hello, "+User.getUsername());
        }

        main_recommend.setAdapter(adapter);
        main_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food food = food_list.get(position);
                updateHistory(food);
//                Toast.makeText(getActivity(), "You clicked: "+food.getFoodname(), Toast.LENGTH_LONG).show();
            }
        });

        getRecommend();
    }


    public void getRecommend() {

//        food_list.add(new Food("sample food 1", "12345", 723.5,
//                456.3, 123.2, 131.3, 55.1));
//        food_list.add(new Food("sample food 2", "14345", 312.1,
//                143.0, 423.1, 771.3, 21.1));

//        double proper_calorie = Utils.caloriePerMeal(User.getGender(), User.getHeight(), User.getWeight(), User.getBirthyear());
        double proper_calorie = 150.0; // TODO: add index to firebase and calculate calorie using above line

        DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        Query query = fb_ref.child("Recipes").orderByChild("calories").endAt(proper_calorie).limitToFirst(2);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    food_list.clear();
                    Map<?,?> food_map;
                    for (DataSnapshot food : dataSnapshot.getChildren()) {
                        food_map = (Map)food.getValue();

                        food_list.add(
                                new Food(
                                        (String)food_map.get("foodname"),
                                        (String)food_map.get("foodID"),
                                        (Double)food_map.get("calories"),
                                        (Double)food_map.get("carbohydrates"),
                                        (Double)food_map.get("fat"),
                                        (Double)food_map.get("protein"),
                                        (Double)food_map.get("servingSize")
                                )
                        );
                        adapter.notifyDataSetChanged();
//                        food_map.clear();
                    }
                }
                else {
                    //TODO: although there is no matched item, do sth, eg: give a low calorie food item
                    Toast.makeText(getActivity(), "No proper food found!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Read From Firebase Failed!", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void updateHistory(final Food food) {
        DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        fb_ref.child("History/"+User.getUsername()+"/"+food.getFoodname()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addHistory( food, (dataSnapshot.exists() ? (Long)dataSnapshot.getValue() + 1 : 1) );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addHistory(Food food, Long freq) {
        DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        fb_ref.child("History/"+User.getUsername()+"/"+food.getFoodname()).setValue(freq, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Food history recorded!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
