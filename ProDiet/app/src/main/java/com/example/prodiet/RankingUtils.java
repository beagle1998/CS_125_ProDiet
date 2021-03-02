package com.example.prodiet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class RankingUtils {

    public static void sortFoodList(ArrayList<Food> food_list, Map<String, Long> history_map) {
        Collections.sort(food_list, new Comparator<Food>() {

            @Override
            public int compare(final Food f1, final Food f2) {
                int c;
                c = f1.getFat().compareTo(f2.getFat());
                if (c == 0)
                    c = f1.getCarbohydrates().compareTo(f2.getCarbohydrates());
                if (c == 0)
                    c = f1.getProtein().compareTo(f2.getProtein());
                return c;
            }

        });
    }
}
