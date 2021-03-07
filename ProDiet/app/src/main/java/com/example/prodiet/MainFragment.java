package com.example.prodiet;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
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
    private Button main_get;
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

        main_title = getView().findViewById(R.id.main_title);
        main_get = getView().findViewById(R.id.main_get);
        main_recommend = getView().findViewById(R.id.main_recommend);

        main_title.setText("Hello, "+User.getUsername());


        main_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), main_get);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(getActivity(), "Clicked popup menu item " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("Light")) {
                            matching(6250, false, 0); // 5000 to 7499 steps
                        } else if (item.getTitle().equals("Moderate")) {
                            matching(8750, false, 0); // 7500 to 9999 steps
                        } else if (item.getTitle().equals("Active")) {
                            matching(11250, false, 0); // >= 10000 steps
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        if (food_list == null) {
            food_list = new ArrayList<>();
        } else {
            food_list.clear();
        }
        adapter = new FoodListViewAdapter(food_list, getActivity());
        main_recommend.setAdapter(adapter);
        main_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food food = food_list.get(position);
                feedback(food);
//                Toast.makeText(getActivity(), "You clicked: "+food.getFoodName(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void matching(final Integer steps, boolean less_possible, final int recur_counter) {

        double proper_calorie = MatchingUtils.caloriePerMeal(User.getGender(),
                User.getHeight(), User.getWeight(), User.getBirthyear(), steps);

        DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        Query query = less_possible ?
                fb_ref.child(MatchingUtils.categoryPath(User.getVegan()))
                        .orderByChild("calories").limitToFirst(7) :
                fb_ref.child(MatchingUtils.categoryPath(User.getVegan()))
                        .orderByChild("calories").startAt(0.1).endAt(proper_calorie).limitToFirst(7);
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
                                        (String)food_map.get("foodName"),
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
                    ranking();
                }
                else {
                    if (recur_counter < 3) {
                        matching(steps, true, recur_counter+1);
                    } else {
                        Toast.makeText(getActivity(), "No proper food found!", Toast.LENGTH_LONG).show(); // should never happen
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Read From Firebase Failed!", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void feedback(final Food food) {
        DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        fb_ref.child("History/"+User.getUsername()+"/"+food.getFoodName()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        fb_ref.child("History/"+User.getUsername()+"/"+food.getFoodName()).setValue(freq, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Food history recorded!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ranking() {
        DatabaseReference fb_ref = FirebaseDatabase.getInstance().getReference();
        fb_ref.child("History/"+User.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<?,?> history_map = (Map)dataSnapshot.getValue();
                    RankingUtils.rankFoodList(food_list, history_map);
                    adapter.notifyDataSetChanged();
//                        history_map.clear();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
